package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;
import ai.yunxi.tree.pojo.TreeUtil;

/**
 * 判断二叉树是否为平衡二叉树
 * 平衡二叉树的性质为：要么是一棵空树，要么任何一个节点的左右子树的高度差不超过1。
 * 给定一个二叉树的头节点head，判断它是否是平衡二叉树
 * 如果二叉树的节点数为N，要求时间复杂度为O(N)
 */
public class SymmetricTree {

    /**
     * 对于任何一个节点，都要先获取它的左子树的树高lh和右子树的树高rh，判断他们的差值的绝对值是否<=1
     * 1.若<=1，则说明此节点平衡，继续遍历下一个节点
     * 2.若>1，则说明节点不平衡，那么可以直接返回false
     * 判断完左右子树，取其中高度的较大者+1，就是当前节点的树高，返回给上一层
     * 整个就是对二叉树的后续遍历
     */
    public boolean isSymmetric(TreeNode head) {
        boolean[] res = new boolean[1];
        res[0] = true;
        getHeight(head, 1, res);
        return res[0];
    }

    // 递归获取树高的方法
    // 此递归方法还要记录是否是平衡二叉树，采用传入boolean数组参数的方式，记录是否是平衡二叉树
    private int getHeight(TreeNode node, int level, boolean[] res) {
        if (node == null) return level;
        // 获取左子树的树高
        int lh = getHeight(node.left, level + 1, res);
        if (!res[0]) {
            // 只要不是平衡二叉树，就可以结束递归直接返回
            return level;
        }
        // 获取右子树的树高
        int rh = getHeight(node.right, level + 1, res);
        if (!res[0]) {
            return level;
        }
        // 判断左右子树的高度差
        if (Math.abs(lh - rh) > 1) {
            res[0] = false;
        }
        return Math.max(lh, rh);
    }

    public static void main(String[] args) {
        SymmetricTree tree = new SymmetricTree();
        TreeNode h1 = TreeUtil.getDefault();
        System.out.println(tree.isSymmetric(h1));

        TreeNode h2 = TreeUtil.getDefault3();
        System.out.println(tree.isSymmetric(h2));
    }
}
