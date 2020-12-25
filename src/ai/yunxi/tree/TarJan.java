package ai.yunxi.tree;

import ai.yunxi.other.UnionFindSet;
import ai.yunxi.tree.pojo.Query;
import ai.yunxi.tree.pojo.TreeNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * TarJan算法与并查集解决二叉树节点间最近公共祖先的批量查询问题
 * 一个Query类的实例表示一条查询语句，表示想要查询o1节点和o2节点的最近公共祖先节点
 * 给定一棵二叉树的头节点head，并给定所有的查询语句，即一个Query类型的数组Query[] ques，请返回TreeNode类型的数组TreeNode[] ans，
 * ans[i]表示ques[i]这条查询的答案，即ques[i].o1和ques[i].o2的最近公共祖先节点
 * 要求：如果二叉树的节点数为N，查询语句的条数为M，整个处理过程的时间复杂度要达到O(N+M)
 * <p>
 * 例如二叉树的节点如图所示：
 * -------1
 * ----2-----3
 * --4---5----6
 * -----7-8--9
 * 想要查询的节点ques[0]=(4,7)，ques[1]=(7,8)，ques[2]=(8,9)，ques[3]=(9,3)，ques[4]=(6,6)，ques[5]=(null,5)，ques[6]=(null,null)
 */
public class TarJan {

    private final Map<TreeNode, LinkedList<TreeNode>> queryMap = new HashMap<>();
    private final Map<TreeNode, LinkedList<Integer>> indexMap = new HashMap<>();
    private final UnionFindSet set = new UnionFindSet();
    private final Map<TreeNode, TreeNode> ancestorMap = new HashMap<>();

    /**
     * 本题的解法利用了TarJan算法与并查集结构的相结合，以题目中的ques数组为例，
     * 首先生成和ques长度一样的ans数组，如下三种情况是可以直接得到答案的：
     * 1.如果o1等于o2，那么答案就为o1(或o2)
     * 2.如果o1和o2有一个为null，那么答案就是不为null的那个
     * 3.如果o1和o2都为null，那么答案就是null
     * <p>
     * 对于不能直接得到答案的查询，我们把查询的格式转换一下，具体过程如下：
     * 1.生成两个哈希表queryMap和indexMap。queryMap类似于邻接表，key表示查询涉及的某个节点，value是一个链表，表示key与哪些节点之间有查询
     * indexMap的key也表示查询涉及的某个节点，value也是链表，表示如果依次解决有关key节点的每个问题，该把答案放在ans的什么位置
     * 也就是如果一个节点为node，node与哪些节点有查询任务，放在queryMap中，获得的答案放在ans的什么位置，放在indexMap中
     * 比如根据ques[0~3]，queryMap和indexMap生成的记录如下：
     * 节点4：{节点7}，{0}
     * 节点7：{节点4,节点8}，{0,1}
     * 节点8：{节点7,节点9}，{1,2}
     * 节点9：{节点8,节点3}，{2,3}
     * 节点3：{节点9}，{3}
     * 每个查询语句在两个哈希表中都生成了两次。这么做的目的是为了处理时方便找到关于每个节点的查询任务，也方便设置答案
     * 接下来是TarJan算法处理M条查询的过程，整个过程是二叉树的先左、再根、再右、最后回到根的遍历，以例子中的二叉树来说明：
     * a.对每个节点生成各自的集合，{1},{2},...,{9}，开始时每个节点的祖先节点设为空
     * b.遍历节点4，发现它属于集合{4}，设置集合{4}的祖先为节点4，发现有关于节点4和节点7的查询任务，
     * 节点7属于集合{7}，但它的祖先节点为空，说明还没遍历到，所以暂时不执行这个任务
     * c.遍历节点2，发现它属于集合{2}，设置集合{2}的祖先为节点2，此时左孩子节点4属于集合{4}，将集合{4}与集合{2}合并，
     * 两个集合一旦合并，小的不再存在，而是生成更大的集合{4,2}，并设置集合{4,2}的祖先为当前节点2
     * d.遍历节点7，发现它属于集合{7}，设置集合{7}的祖先为节点7，发现有关于节点7和节点4的查询任务，
     * 节点4属于集合{4,2}，祖先为节点2，说明节点7和节点4都已遍历到，根据indexMap知道答案应放在位置0，所以设置ans[0]=节点2，
     * 又发现有节点7和8的查询任务，8属于集合{8}，祖先为空，说明还没遍历到，忽略
     * e.遍历节点5，发现它属于集合{5}，设置集合{5}的祖先为节点5，此时左孩子节点7属于集合{7}，
     * 将集合{7}与集合{5}合并，合并之后为{7,5}，祖先为节点5
     * f.遍历节点8，发现它属于集合{8}，设置集合{8}的祖先为节点8，发现有节点8和7的查询任务，节点7属于集合{7,5}，
     * 祖先为节点5，设置ans[1]=节点5，发现有节点8和9的查询任务，忽略
     * g.从节点5的右子树重新回到节点5，节点5属于集合{7,5}，节点5的右孩子节点8属于集合{8}，两个集合合并为{7,5,8}，并设置祖先为节点5
     * h.从节点2的右子树重新回到节点2，节点2属于集合{4,2}，节点2的右孩子节点5属于集合{7,5,8}，合并为{4,2,7,5,8}，设置祖先为节点2
     * i.遍历节点1，{4,2,7,5,8}与{1}合并为{4,2,7,5,8,1}，设置祖先为节点1
     * j.遍历节点3，发现它属于集合{3}，设置集合{3}的祖先为节点3，发现有节点3和9的查询任务，忽略
     * k.遍历节点9，发现它属于集合{9}，设置集合{9}的祖先为节点9；发现有节点9和8的查询任务，节点8属于{4,2,7,5,8,1}，祖先为节点1，
     * 因此设置ans[1]=节点1；发现有节点9和3的查询任务，3属于{3}，祖先为节点3，因此设置ans[3]=节点3
     * l.遍历节点6，发现它属于集合{6}，设置集合{6}的祖先为节点6，合并之后为{6,9}，祖先为节点6
     * m.回到节点3，合并集合为{6,9,3}，祖先为节点3
     * n.回到节点1，合并集合为{4,2,7,5,8,1,6,9,3}，祖先为节点1
     * 遍历完毕，所有查询都有了答案
     * <p>
     * 假设查询的两个节点分别为o1、o2，分析整个过程发现，可以分成3种情况：
     * 1.o1是o2左子树上的节点(反之同理)，那么肯定先遍历到节点o1，当遍历到o2时，o2的左子树都遍历完毕了，先合并左子树和o2的集合，
     * 并设置集合的祖先为节点o2，此时o1也在集合中，查询的答案就是o1所在集合的祖先节点，也即是o2
     * 2.o1是o2右子树上的节点(反之同理)，那么先遍历的是o2，此时合并了o2和o2的左子树，祖先节点是o2，
     * 当遍历到o1时，查询的答案是o2所在集合的祖先节点，也是o2
     * 3.o1和o2分别在不同的子树上，他们的最近祖先节点是p，那么他们分别在p的左右子树上，假设o1在左子树，o2在右子树，先遍历的是o1，
     * 当遍历到o2时，显然o1所在的左子树都遍历完了，并且和p的集合也合并完了，祖先节点就是p。答案是o1所在集合的祖先节点，也就是p
     * 不管o1、o2哪个先遍历，当遍历到o1时，如果o2已遍历过，那么自然可以得到答案，然后在key=o1的链表中，删除和o2有关的查询任务；
     * 如果此时o2还没有遍历到，也可以删除相关任务，因为这个任务会在遍历到o2时重新被发现，因为o2也存了一份。
     * <p>
     * 如果二叉树的节点数为N，那么生成集合操作O(N)次，合并集合操作O(N)次，根据节点找到所在集合O(N+M)次。
     * 所以上述过程想要达到时间复杂度O(N+M)，就要求有关集合的单次操作，平均时间复杂度为O(1)
     * 要做到平均时间复杂度O(1)，那么就要用到并查集结构，具体见UnionFindSet类
     * <p>
     * 最后解释一下如何设置一个集合的祖先节点，上述步骤中有把当前节点node所在集合的祖先节点设为node的操作
     * 在流程开始之前，建立一张哈希表，记为ancestorMap，我们知道在并查集中，每个集合都是用该集合的代表节点表示的
     * 所以如果想把node所在集合的祖先节点设为node，只用先找到其所在集合的代表节点，并把记录(findFather(node),node)放入ancestorMap即可
     * 同理，如果想获取一个节点a所在集合的祖先节点，令key=findFather(a)，然后从ancestorMap中找到相应value即可
     * ancestorMap还可以表示一个节点是否被访问过
     */
    public TreeNode[] tarJanQuery(TreeNode head, Query[] ques) {
        if (head == null || ques == null) return null;
        if (ques.length == 0) return new TreeNode[0];

        TreeNode[] ans = new TreeNode[ques.length];
        setQueryAndIndex(ques, ans);
        // 先初始化查并集：步骤a
        set.makeSet(head);
        setAnswers(head, ans);
        return ans;
    }

    // 设置queryMap和indexMap的数据
    private void setQueryAndIndex(Query[] ques, TreeNode[] ans) {
        TreeNode o1;
        TreeNode o2;
        for (int i = 0; i < ques.length; i++) {
            o1 = ques[i].o1;
            o2 = ques[i].o2;
            if (o1 == o2 || o1 == null || o2 == null) {
                // 直接得到答案的三种情况，不用放入queryMap
                ans[i] = o1 == null ? o2 : o1;
            } else {
                // 没有时先初始化
                if (!queryMap.containsKey(o1)) {
                    queryMap.put(o1, new LinkedList<>());
                    indexMap.put(o1, new LinkedList<>());
                }
                if (!queryMap.containsKey(o2)) {
                    queryMap.put(o2, new LinkedList<>());
                    indexMap.put(o2, new LinkedList<>());
                }
                queryMap.get(o1).add(o2);
                indexMap.get(o1).add(i);
                queryMap.get(o2).add(o1);
                indexMap.get(o2).add(i);
            }
        }
    }

    // 查找并设置返回的答案
    private void setAnswers(TreeNode head, TreeNode[] ans) {
        if (head == null) return;
        // 根据TarJan算法遍历二叉树，先遍历左子树
        setAnswers(head.left, ans);
        // 回到根节点，合并左子树集合和根节点集合
        set.union(head.left, head);
        // 设置合并之后集合的祖先节点：put(findFather(node),node)
        ancestorMap.put(set.findFather(head), head);
        // 遍历右子树
        setAnswers(head.right, ans);
        // 合并右子树集合和根节点所在的集合
        set.union(head.right, head);
        // 设置合并之后集合的祖先节点
        ancestorMap.put(set.findFather(head), head);
        // 判断是否有关于当前节点查询
        LinkedList<TreeNode> queries = queryMap.get(head);
        LinkedList<Integer> indexes = indexMap.get(head);
        TreeNode node;
        TreeNode father;
        Integer index;
        while (queries != null && !queries.isEmpty()) {
            node = queries.poll();
            index = indexes.poll();
            father = set.findFather(node);
            if (ancestorMap.containsKey(father) && index != null) {
                ans[index] = ancestorMap.get(father);
            }
        }
    }

    public static void main(String[] args) {
        TreeNode n9 = new TreeNode(9);
        TreeNode n8 = new TreeNode(8);
        TreeNode n7 = new TreeNode(7);
        TreeNode n6 = new TreeNode(6, n9, null);
        TreeNode n5 = new TreeNode(5, n7, n8);
        TreeNode n4 = new TreeNode(4);
        TreeNode n3 = new TreeNode(3, null, n6);
        TreeNode n2 = new TreeNode(2, n4, n5);
        TreeNode head = new TreeNode(1, n2, n3);

        Query[] queries = new Query[8];
        queries[0] = new Query(n4, n7);
        queries[1] = new Query(n7, n8);
        queries[2] = new Query(n8, n9);
        queries[3] = new Query(n9, n3);
        queries[4] = new Query(n6, n6);
        queries[5] = new Query(n6, head);
        queries[6] = new Query(null, n5);
        queries[7] = new Query(null, null);
        TarJan tarJan = new TarJan();
        TreeNode[] ans = tarJan.tarJanQuery(head, queries);
        for (TreeNode an : ans) {
            System.out.println(an);
        }
    }
}
