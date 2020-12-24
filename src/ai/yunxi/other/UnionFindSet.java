package ai.yunxi.other;

import ai.yunxi.tree.pojo.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 并查集结构(union-find disjoint sets)
 * 在一些有N个元素的集合应用问题中，我们通常是在开始时让每个元素构成一个单元素的集合，然后按一定顺序将属于同一组的元素所在的集合合并，
 * 其间要反复查找一个元素在哪个集合中。这种结构，最好采用并查集来描述。
 * 并查集是一种树型的数据结构，用于处理一些不相交集合（disjoint sets）的合并及查询问题。
 * 它是由Bernard A.Galler和Micheal J.Fisher在1964年发明，直到1989年才彻底证明完毕，其过程可阅读《算法导论》
 * 这里只重点介绍并查集的结构和各种造作的细节，并实现针对二叉树的并查集结构，这是一种经常使用的高级数据结构
 * <p>
 * 并查集由一群集合构成，比如一棵二叉树：
 * -------1
 * ----2-----3
 * --4---5----6
 * -----7-8--9
 * 它的每个节点都生成自己的集合，所有集合的全体构成一个并查集={{1},{2},...,{9}}。
 * 这些集合可以合并，如果最终合并成一个大集合，那么此时并查集中只有一个元素，即并查集={{1,2,...,9}}
 * <p>
 * 并查集先进性初始化的过程，把每个节点都生成一个只有自己的集合。
 * 如果集合中只有一个元素时，记为节点a，它的father为自己，也就意味着这个集合的代表节点就是唯一的那个元素。
 * 实现记录节点father信息的方式有很多，这里采用哈希表来保存并查集中所有集合的所有元素的father信息，记为fatherMap
 * 对于这个集合，在fatherMap中肯定有一条记录为{节点a(key),节点a(value)}，表示key节点的father为value节点
 * 每个元素除了father信息，还有另一个信息叫rank，rank为整数，代表一个节点的秩，秩的概念可以粗略理解为一个节点下面还有多少层节点
 * 但是并查集结构对每个节点秩的更新并不严格，所以秩只能粗略描述该节点下面的深度，正是由于这种不严格换来了极好的时间复杂度
 * 集合中只有一个元素时，这个元素的rank初始化为0。所有节点的秩信息保存在rankMap中
 * 对二叉树结构并查集初始化的过程请参考下面的makeSet方法
 * <p>
 * 当集合有多个节点时，下层节点的father为上层节点，最上层的节点father指向自己，最上层的节点又叫集合的代表节点
 * 在并查集中，要查一个节点属于哪个集合，就是在查这个节点所在集合的代表节点是什么
 * 一个节点通过father信息逐渐找到最上层的节点，这个节点的father是自己，代表整个集合
 * 如图：
 * ------a
 * ---b--c--d
 * --e-f
 * --g
 * 任何一个节点最终都找到节点a，比如节点g。如果另外一个节点z找到的代表节点不是节点a，那么可以肯定节点g和节点z不在一个集合中
 * 通过一个节点找到所在集合代表节点的过程，叫做findFather过程。findFather最终会返回代表节点，但是不仅如此，它还会把整个查找路径压缩
 * 比如，执行findFather(g)，找到最终节点a之后，会把a到g这条路径上所有节点的father都设置为a，集合会变成下面这个样子：
 * --------a
 * --e--g--b--c--d
 * --------f
 * 经过路径压缩之后，路径上每个节点下次再找代表节点的时候，都只需经过一次移动的过程，这也是并查集结构设计中最重要的优化
 * 根据一个节点查找所在集合代表节点过程参照findFather方法
 * <p>
 * 接下来介绍集合如何合并。首先，两个集合进行合并操作时，参数并不是两个集合，而是并查集中任意的两个节点，记为a和b，
 * 所以集合合并更准确的说法是，根据a找到代表节点aF，根据b找到代表节点bF，然后用如下逻辑决定由哪个代表节点作为合并后大集合的代表节点：
 * 1.如果aF=bF，说明a、b在一个集合中，不用合并
 * 2.如果aF!=bF，假设aF的rank值为aFRank，bF的rank值为bFRank，rank值可以粗略描述一个节点下面的层数，而aF和bF又是各自集合中最上面的节点，
 * 所以aFRank粗略描述a所在集合的总层数，bFRank粗略描述b所在集合的总层数。
 * 如果aFRank<bFRank，那么把aF的father设为bF，表示a所在集合因为层数较少所以挂在了b所在集合的下面，这样合并后大集合的rank不会有变化
 * 相反如果aFRank>bFRank，那么把bF的father设为aF
 * 如果aFRank=bFRank，那么谁做大集合的代表都可以，这里是用aF作为代表，把bF的father设为aF，aF的rank值加1
 * 根据两个节点合并两个集合的过程参照union方法
 */
public class UnionFindSet {

    // 所有节点的father节点
    public Map<TreeNode, TreeNode> fatherMap;
    // 所有节点的rank值
    public Map<TreeNode, Integer> rankMap;

    public UnionFindSet() {
        fatherMap = new HashMap<>();
        rankMap = new HashMap<>();
    }

    // 初始化两个map
    public void makeSet(TreeNode head) {
        fatherMap.clear();
        rankMap.clear();
        preOrderMake(head);
    }

    // 按先序遍历的顺序遍历整棵树，初始化每个节点的father和rank
    // father初始化为自己本身，rank初始化为0
    private void preOrderMake(TreeNode node) {
        if (node == null) return;
        fatherMap.put(node, node);
        rankMap.put(node, 0);
        preOrderMake(node.left);
        preOrderMake(node.right);
    }

    // 查找节点所在集合的代表节点
    public TreeNode findFather(TreeNode node) {
        TreeNode father = fatherMap.get(node);
        if (father != node) {
            // 开始递归查找，直到找到father为自己本身的节点
            father = findFather(father);
        }
        // 把查找路径上所有节点的father都设为最终找到的代表节点
        fatherMap.put(node, father);
        return father;
    }

    // 合并两个集合，最终目的其实就是找到合并之后大集合的代表节点和rank值，并更新到map里
    public void union(TreeNode a, TreeNode b) {
        if (a == null || b == null) return;
        TreeNode aF = findFather(a);
        TreeNode bF = findFather(b);
        if (aF != bF) {
            int aFRank = rankMap.get(aF);
            int bFRank = rankMap.get(bF);
            if (aFRank < bFRank) {
                // 把aF的father设为bF，rank值不变
                fatherMap.put(aF, bF);
            } else if (aFRank > bFRank) {
                // 把bF的father设为aF，rank值不变
                fatherMap.put(bF, aF);
            } else {
                // 把bF的father设为aF
                fatherMap.put(bF, aF);
                // 注意要把aF的rank值加1
                rankMap.put(aF, aFRank + 1);
            }
        }
    }
}
