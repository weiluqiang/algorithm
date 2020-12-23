package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;
import ai.yunxi.tree.pojo.TreeUtil;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 判断一棵树是否为搜索二叉树和完全二叉树
 * 给定一个二叉树的头节点head，已知其中没有重复的节点，实现两个函数分别判断这棵树是否是搜索二叉树和完全二叉树
 */
public class BSTAndCBT {

    /**
     * 判断一棵二叉树是否是搜索二叉树，根据我们AdjustErrorBST中的分析，只要对二叉树进行中序遍历，
     * 遍历的过程中节点值都是递增的，因此判断中序遍历过程中是否有节点值变小即可
     * 本方法采用的是Morris中序遍历的方式，时间复杂度是O(N)，额外的空间复杂度是O(1)
     * 需要注意的是：Morris遍历包括调整二叉树结构和恢复二叉树结构两个阶段，
     * 所以当发现节点值降序时，不能直接返回false，这样就破坏了原来树的结构
     */
    public boolean isBST(TreeNode head) {
        if (head == null) return true;
        boolean res = true;
        // 中序遍历过程的上一个节点
        TreeNode pre = null;
        TreeNode cur1 = head;
        TreeNode cur2;
        while (cur1 != null) {
            cur2 = cur1.left;
            if (cur2 != null) {
                // 查找cur1节点左子树的最右节点
                while ((cur2.right != null) && (cur2.right != cur1)) {
                    cur2 = cur2.right;
                }
                if (cur2.right == null) {
                    cur2.right = cur1;
                    cur1 = cur1.left;
                    continue;
                } else {
                    cur2.right = null;
                }
            }
            // 不再打印节点值，而是判断两个节点值的大小
            // 判断不满足时不能直接返回false，应该记录返回的false并继续遍历
            if (pre != null && pre.value > cur1.value) {
                res = false;
            }
            pre = cur1;
            cur1 = cur1.right;
        }
        return res;
    }

    /**
     * 一个深度为k，节点个数为2^k-1的二叉树为满二叉树。这个概念很好理解，就是一棵树，深度为k，并且没有空位。
     * 对满二叉树按照广度优先遍历（从左到右）的顺序进行编号。
     * 一颗深度为k二叉树，有n个节点，然后，也对这棵树进行编号，如果所有的编号都和满二叉树对应，那么这棵树是完全二叉树。
     * 其实就是对于一棵树，每一层从左往右编号，编号不能中断。
     * <p>
     * 判断一棵二叉树是否是完全二叉树，根据以下标准，会使判断过程变得简单：
     * 1.按层遍历二叉树，每层从左到右依次遍历所有的节点
     * 2.如果当前节点只有右孩子，没有左孩子，直接返回false
     * 3.如果当前节点并不是左右孩子都有，那它之后的节点必须全为叶子节点，否则返回false
     * 4.遍历过程中如果不返回false，则遍历结束后返回true
     */
    public boolean isCBT(TreeNode head) {
        if (head == null) return true;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(head);
        TreeNode l;
        TreeNode r;
        // leaf记录是否需要当前节点是叶子节点，当前一节点只有左孩子或者没有孩子时，所有后续节点都必须是叶子节点
        boolean leaf = false;
        while (!queue.isEmpty()) {
            head = queue.poll();
            l = head.left;
            r = head.right;
            if ((leaf && (l != null || r != null)) || (l == null && r != null)) {
                return false;
            }
            if (l != null) {
                queue.offer(l);
            }
            // 根据上面的逻辑，如果l为空，则r也必须为空，否则会直接返回false
            // 因此对于情况3的判断，不用判断l，只判断r是否为空即可
            if (r != null) {
                queue.offer(r);
            } else {
                leaf = true;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        BSTAndCBT bst = new BSTAndCBT();
        TreeNode head = TreeUtil.getDefault4();
        System.out.println(bst.isBST(head));
        System.out.println(bst.isCBT(head));
    }
}
