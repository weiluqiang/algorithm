package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

/**
 * 较为直观的打印二叉树
 * 二叉树可以用常规的三种遍历结果来描述其结构，但不够直观
 * 请实现一个打印二叉树的函数，可以直观的展示树的形状
 * <p>
 * 例如图所示的二叉树：
 * ---------1
 * -------2---3
 * -----4----5-6
 * ------7
 * 设计的打印结果如下：
 * --------6↓
 * ----3↓
 * --------5↑
 * 1H
 * ----2↑
 * ------------7↓
 * --------4↑
 * 其中字母H表示头节点
 * ↓表示父节点在下面，自己的前一列，并且离自己最近的节点
 * ↑表示父节点在上面，自己的前一列，并且离自己最近的节点
 * <p>
 * 为了打印形状的直观，还需考虑所有节点占用的长度一致，比如2和4433555，不统一长度，会造成节点错位
 * 整型的数值，算上符号最长为11位，加上后缀和2个空格，规定每个节点占14位，不够用空格补齐
 * <p>
 * 打印的时候，最右节点在上方，最左节点在下方，头节点在中间，因此遍历的时候，先遍历右子树，再打印父节点，最后遍历左子树
 * 遍历时还要注意节点的层级，并在前面补充层级数相应的空白节点
 */
public class PrintTree {

    /**
     * 打印二叉树的方法
     */
    public void printTree(TreeNode head) {
        System.out.println("Binary Tree:");
        printInOrder(head, "H", 0);
    }

    // 按右、根、左的顺序打印，h为节点层级
    private void printInOrder(TreeNode node, String suffix, int h) {
        if (node == null) return;
        // 1、打印右子树
        printInOrder(node.right, "↓", h + 1);
        // 2、打印当前节点
        doPrint(node, suffix, h);
        // 3、打印左子树
        printInOrder(node.left, "↑", h + 1);
    }

    // 打印节点
    private void doPrint(TreeNode node, String suffix, int h) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < h; i++) {
            // 暂时固定6个空格
            sb.append("      ");
        }
        String val = node.value + suffix;
        sb.append(val);
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        TreeNode n7 = new TreeNode(7);
        TreeNode n4 = new TreeNode(4, null, n7);
        TreeNode n2 = new TreeNode(2, n4, null);
        TreeNode n6 = new TreeNode(6);
        TreeNode n5 = new TreeNode(5);
        TreeNode n3 = new TreeNode(3, n5, n6);
        TreeNode head = new TreeNode(1, n2, n3);

        PrintTree pt = new PrintTree();
        pt.printTree(head);
    }
}
