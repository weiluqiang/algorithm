package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

/**
 * 找到二叉树中的最大搜索二叉子树
 * 给定一个二叉树的头节点head，已知其所有节点的值都不一样，找到含有节点最多的搜索二叉子树，并返回它的头节点
 * 例如，二叉树如图所示：
 * -------------6
 * -----1---------------12
 * --0----3-------10----------13
 * ------------4------14----20--16
 * -----------2-5---11--15
 * 它的最大搜索二叉子树是：
 * ------10
 * ---4------14
 * --2-5---11--15
 * 如果节点数为N，要求时间复杂度为O(N)，额外的空间复杂度为O(h)，h为树的高度
 */
public class MaxSubBST {

    /**
     * 以节点node为头的树中，最大的搜索二叉子树只可能来自以下两种情况：
     * a.如果来自node左子树上的最大搜索二叉子树是以node.left为头的，来自node右子树上的最大搜索二叉子树是以node.right为头的
     * node左子树上的最大搜索二叉子树的最大值小于node.value，node右子树上的最大搜索二叉子树的最小值大于node.value
     * 那么节点node为头的整棵树就是搜索二叉树
     * b.如果不满足第一种情况，那么节点node为头的整棵树不能构成搜索二叉树。这种情况下，它的最大搜索二叉子树是
     * node左子树上的最大搜索二叉子树和右子树上的最大搜索二叉子树中，节点数较多的那个
     * <p>
     * 通过上述分析，具体的求解过程如下：
     * 1.根据上面的逻辑，整体过程应采用后序遍历
     * 2.遍历到当前节点cur时，需先遍历它的左右子树，并收集左右子树的4个信息：头节点head、节点数size、最大值max、最小值min
     * 3.根据步骤2收集的信息，判断是否满足情况a，若满足则返回cur节点，不满足则比较节点数返回较大者的头节点
     * 4.可使用全局变量的方式实现步骤2中收集节点数、最大值、最小值的问题
     */
    public TreeNode biggestBST(TreeNode head) {
        // 3个位置分别存储节点数、最大值、最小值
        int[] num = new int[3];
        return postOrder(head, num);
    }

    // 后序遍历整棵树
    private TreeNode postOrder(TreeNode node, int[] num) {
        if (node == null) {
            num[0] = 0;
            num[1] = Integer.MIN_VALUE;//最大值设为minInt
            num[2] = Integer.MAX_VALUE;//最小值设为maxInt
            return null;
        }

        int[] left_num = new int[3];
        TreeNode left = postOrder(node.left, left_num);
        int[] right_num = new int[3];
        TreeNode right = postOrder(node.right, right_num);
        // 满足情况a，则当前节点的树就是搜索二叉树
        // 分析可知，当left为空或right为空或两个都为空的时候此条件也成立
        if (left == node.left && right == node.right && left_num[1] < node.value && right_num[2] > node.value) {
            num[0] = left_num[0] + right_num[0] + 1;
            // 考虑到子节点为null时会返回minInt和maxInt，因此需要加上max和min的判断
            num[1] = Math.max(right_num[1], node.value);
            num[2] = Math.min(left_num[2], node.value);
            return node;
        }
        // 情况b，判断左右的最大搜索二叉子树的size
        // 当发生情况b的时候，返回上一层节点时必然还是情况b，因此从此向上所有节点都是情况b
        // num数组的第1、2位置的数据以后都不再需要，因此只定义num[0]就可以了
        num[0] = Math.max(left_num[0], right_num[0]);
        return left_num[0] > right_num[0] ? left : right;
    }

    public static void main(String[] args) {
        TreeNode n41 = new TreeNode(2);
        TreeNode n42 = new TreeNode(5);
        TreeNode n43 = new TreeNode(11);
        TreeNode n44 = new TreeNode(15);
        TreeNode n31 = new TreeNode(4, n41, n42);
        TreeNode n32 = new TreeNode(14, n43, n44);
        TreeNode n33 = new TreeNode(20);
        TreeNode n34 = new TreeNode(16);
        TreeNode n21 = new TreeNode(0);
        TreeNode n22 = new TreeNode(3);
        TreeNode n23 = new TreeNode(10, n31, n32);
        TreeNode n24 = new TreeNode(13, n33, n34);
        TreeNode n11 = new TreeNode(1, n21, n22);
        TreeNode n12 = new TreeNode(12, n23, n24);
        TreeNode head = new TreeNode(6, n11, n12);
        MaxSubBST subBST = new MaxSubBST();
        System.out.println(subBST.biggestBST(head).value);
    }
}
