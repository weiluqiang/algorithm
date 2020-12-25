package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;
import ai.yunxi.tree.pojo.TreeUtil;

/**
 * 二叉树节点间的最大距离问题
 * 从二叉树的节点A出发，可以向上或者向下走，但沿途的节点只能经过一次，到达节点B时，路径上的节点数叫做A到B的距离
 * 比如图所示的二叉树：
 * -------1
 * ----2------3
 * --4---5--6---7
 * 节点4和节点2的距离为2，节点5和节点6的距离为5
 * 给定一个二叉树的头节点head，求整棵树上节点间的最大距离
 * 如果二叉树的节点数为N，要求时间复杂度为O(N)
 */
public class MaxDistance {

    /**
     * 一棵以h为头的树上，最大距离只可能来自以下三种情况：
     * 1.h的左子树上的最大距离
     * 2.h的右子树上的最大距离
     * 3.h左子树上离h.left最远的距离 + 1 + h右子树上离h.right最远的距离
     * 三个距离中最大的那个就是整棵h树中的最大距离，逻辑如下：
     * 1.设计一个方法，返回两个值：以当前节点为头的子树上的最大距离maxDis，和离自己最远的节点之间的距离maxFrom
     * 2.每次调用该方法时，先递归调用它的左右子节点，分别获取左右子节点的两个返回值，然后计算当前节点的值
     * 3-1.以当前节点为头的子树上的最大距离maxDis，就是比较上面三种情况取最大者
     * 3-2.当前节点和离自己最远的节点之间的距离maxFrom，是左右两个子节点的maxFrom中的较大者+1
     * 4.返回本节点的两个值
     * 分析可知，整个遍历过程就是对二叉树的后序遍历，时间复杂度为O(N)
     */
    public int getMax(TreeNode head) {
        return postOrder(head)[0];
    }

    // 整个后序遍历的过程，其中返回数组中，位置0的值是maxDis，位置1的值是maxFrom
    private int[] postOrder(TreeNode node) {
        if (node == null) return new int[]{0, 0};
        int[] left = postOrder(node.left);
        int[] right = postOrder(node.right);
        int maxDis = Math.max(Math.max(left[0], right[0]), left[1] + right[1] + 1);
        int maxFrom = Math.max(left[1], right[1]) + 1;
        return new int[]{maxDis, maxFrom};
    }

    /**
     * 第一棵二叉树示意：
     * ---------1
     * ------2-----3
     * ----4---5
     * ---6-----7
     * --8-------9
     */
    public static void main(String[] args) {
        MaxDistance max = new MaxDistance();
        TreeNode n9 = new TreeNode(9);
        TreeNode n8 = new TreeNode(8);
        TreeNode n7 = new TreeNode(7, null, n9);
        TreeNode n6 = new TreeNode(6, n8, null);
        TreeNode n5 = new TreeNode(5, null, n7);
        TreeNode n4 = new TreeNode(4, n6, null);
        TreeNode n3 = new TreeNode(3);
        TreeNode n2 = new TreeNode(2, n4, n5);
        TreeNode head = new TreeNode(1, n2, n3);
        System.out.println(max.getMax(head));
        System.out.println(max.getMax(TreeUtil.getDefault()));
        System.out.println(max.getMax(TreeUtil.getDefault2()));
        System.out.println(max.getMax(TreeUtil.getDefault3()));
    }
}
