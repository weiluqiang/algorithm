package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 在二叉树中找到累加和为指定值的最长路径长度
 * 给定一个二叉树的头节点head和整数sum，二叉树节点值类型为整型，求累加和为sum的最长路径长度。
 * 路径是指从某个节点往下，每次最多选择一个孩子节点或者不选所形成的节点链
 * <p>
 * 例如如图所示的二叉树：
 * --------------(-3)
 * --------(3)----------(-9)
 * -----(1)---(0)-----(2)---(1)
 * ---------(1)-(6)
 * 如果sum=6，那么累加和为6的最长路径为：-3、3、0、6，所以返回4
 * 如果sum=-9，那么累加和为-9的最长路径为：-9，所以返回1
 */
public class MaxLengthPath {

    /**
     * 二叉树的的节点数为N，高度为h，本解法可做到时间复杂度O(N)，额外的空间复杂度O(h)
     * 1.已知二叉树头节点head和固定值sum，变量len负责记录累加和等于sum的最长路径长度
     * 2.生成哈希表sumMap，负责记录从head开始的一条路径上累加和出现的情况，累加和也是从head节点开始累加的
     * sumMap的key表示某个累加和，value表示这个累加和在路径中最早出现的层数。
     * 如果在遍历cur节点的时候，能够知道从head到cur节点这条路径上累加和出现的情况，那么求以cur节点结尾的累加和为指定值的最长路径长度就非常容易
     * 那么如何去更新sumMap，才能做到在遍历到任何一个节点的时候，都能有从head到这个节点的路径上的所有累加和出现情况呢？
     * 3.首先在sumMap中加入记录(0,0)，它表示累加和0不用包括任何节点就可以得到
     * 然后按照先序遍历的方式遍历节点，遍历到的当前节点记为cur，从head到cur父节点的累加和记为preSum，cur所在的层数记为level
     * preSum+cur.value为当前节点的累加和，记为curSum
     * 如果sumMap包含了curSum的记录，则说明curSum在上层出现过，那么就不用更新；如果不包含，那么就将(curSum,level)放入sumMap
     * 接下来求解以cur为结尾的情况下，累加和为指定值的最长路径：
     * 从sumMap查找key值为curSum-sum的记录
     * a.如果存在，值记为val，则说明在head到cur的路径上存在累加和为sum的子路径，子路径是从此路径的val+1层到当前level层
     * 计算此子路径的长度应该为level-(val+1)+1=level-val，并与len比较取较大者
     * b.如果不存在，则说明在head到cur的路径上不存在累加和为sum的子路径，不用做操作
     * 4.遍历二叉树上的全部节点，每个节点都会求解以本节点为结尾的路径上是否存在累加和为sum的子路径，存在则记录其中最长的路径长度
     */
    public int maxLength(TreeNode head, int sum) {
        Map<Integer, Integer> sumMap = new HashMap<>();
        sumMap.put(0, 0);
        return preOrder(head, sum, 0, 1, 0, sumMap);
    }

    // 递归方式进行先序遍历
    // cur-当前节点，sum-固定值，preSum-节点累加和，level-当前节点高度，len-最长路径长度
    private int preOrder(TreeNode cur, int sum, int preSum, int level, int len, Map<Integer, Integer> sumMap) {
        if (cur == null) return len;
        int curSum = preSum + cur.value;
        if (!sumMap.containsKey(curSum)) {
            sumMap.put(curSum, level);
        }
        // 求head到cur节点路径上累加和为sum的最长路径长度
        if (sumMap.containsKey(curSum - sum)) {
            // 注意此时的level，因为当cur=head时，level=1，head正好是第1层，因此level表示的正是当前的层数，不用加1
            len = Math.max(level - sumMap.get(curSum - sum), len);
        }
        // 每次给len赋值时都会判断较大者并返回，所以不论遍历顺序如何，len都记录最后的返回值即可
        // 比如下面的逻辑，假如len在遍历left后没有变化，则直接传给下一步
        // 假如len在遍历left后变更大了，那么把left的返回值赋给len再传给下一步
        len = preOrder(cur.left, sum, curSum, level + 1, len, sumMap);
        len = preOrder(cur.right, sum, curSum, level + 1, len, sumMap);
        // 二叉树的遍历存在数据相互影响的情况，因为sumMap是同一个，从head到当前节点路径上的全部累加和，可能会有重复
        // 在整棵树上，可能会存在两个不同的节点cur1和cur2，它们也都不在对方的子树上，是两个独立的节点
        // 但是从head到cur1的全部累加和，到cur2的全部累加和，两组数据可能存在累加和相等的情况，这样在sumMap中就会相互影响
        // 为了避免这种影响，在遍历完当前节点之后，返回上一层之前，应该判断当前的curSum是否放入了sumMap，并及时清理掉
        if (level == sumMap.get(curSum)) {
            sumMap.remove(curSum);
        }
        return len;
    }

    public static void main(String[] args) {
        TreeNode n9 = new TreeNode(1);
        TreeNode n8 = new TreeNode(6);
        TreeNode n7 = new TreeNode(1);
        TreeNode n6 = new TreeNode(0, n9, n8);
        TreeNode n5 = new TreeNode(2);
        TreeNode n4 = new TreeNode(1);
        TreeNode n3 = new TreeNode(3, n7, n6);
        TreeNode n2 = new TreeNode(-9, n5, n4);
        TreeNode head = new TreeNode(-3, n3, n2);

        MaxLengthPath path = new MaxLengthPath();
        System.out.println(path.maxLength(head, 6));
    }
}
