package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

/**
 * 打印二叉树的边界节点
 * 给定一个二叉树的头节点head，按照下面两个标准分别实现边界节点的逆时针打印
 * 标准一：
 * 1、头结点为边界节点
 * 2、叶节点为边界节点
 * 3、如果节点在其所在的层中是最左或最右，那么也是边界节点
 * 标准二：
 * 1、头结点为边界节点
 * 2、叶节点为边界节点
 * 3、树左边界延伸下去的路径为边界节点
 * 4、树右边界延伸下去的路径为边界节点
 * 如图所示的二叉树：
 * ------------1
 * -------2          3
 * --------4-------5  6
 * -------7 8--------9 10
 * ----------11----12
 * --------13 14-15 16
 * 按标准一打印的结果为：1，2，4，7，11，13，14，15，16，12，10，6，3
 * 按标准二打印的结果为：1，2，4，7，13，14，15，16，10，6，3
 * <p>
 * 要求：
 * 1、如果节点数为N，要求两种实现的时间复杂度为O(N)，额外的空间复杂度为O(h)，h为二叉树的树高
 * 2、两种标准都要逆时针打印，且打印节点不重复
 */
public class EdgeNode {

    /**
     * 按标准一打印
     * 利用一个二维数组存储左右边界节点
     */
    public void printEdge1(TreeNode head) {
        if (head == null) return;
        // 先求树高
        int height = getHeight(head, 0);
        // 每一层的边界节点
        TreeNode[][] edgeNode = new TreeNode[height][2];
        setEdgeNode(edgeNode, head, 0);
        // 先打印全部左边界节点
        for (TreeNode[] nodes : edgeNode) {
            System.out.print(nodes[0].value + " ");
        }
        // 然后打印最下层节点，但不包括左边界和右边界
        printBottom(head, 0, height, edgeNode);
        // 打印右边界节点，但不包括左边界
        for (int i = edgeNode.length - 1; i >= 0; i--) {
            if (edgeNode[i][1] != edgeNode[i][0]) {
                System.out.print(edgeNode[i][1].value + " ");
            }
        }
    }

    // 递归方式计算树高，h代表当前层级
    // 每向下深入一个层级，h就加1，直到为空
    private int getHeight(TreeNode node, int h) {
        if (node == null) return h;
        return Math.max(getHeight(node.left, h + 1), getHeight(node.right, h + 1));
    }

    // 递归查找每一层的左右边界节点
    // 由于每层都是从左往右遍历，因此第一个节点就是左边界，最后一个节点就是右边界
    private void setEdgeNode(TreeNode[][] edgeNode, TreeNode node, int h) {
        if (node == null) return;
        // 左边界
        if (edgeNode[h][0] == null) {
            edgeNode[h][0] = node;
        }
        // 右边界
        edgeNode[h][1] = node;
        setEdgeNode(edgeNode, node.left, h + 1);
        setEdgeNode(edgeNode, node.right, h + 1);
    }

    // 打印最下层节点，不包括左右边界
    private void printBottom(TreeNode node, int h, int height, TreeNode[][] en) {
        if (node == null) return;
        if (h == height - 1 && node != en[h][0] && node != en[h][1]) {
            System.out.print(node.value + " ");
        }
        printBottom(node.left, h + 1, height, en);
        printBottom(node.right, h + 1, height, en);
    }

    /**
     * 按标准二打印，采用递归方式
     */
    public void printEdge2(TreeNode head) {
        int height = this.getHeight1(head);
        this.printEdge2(head, height, 0);
    }

    // 计算树高
    // 树中任意一个节点，它的高度等于左右子树的较高高度+1
    private int getHeight1(TreeNode node) {
        if (node == null) return 0;
        return Math.max(getHeight1(node.left), getHeight1(node.right)) + 1;
    }

    private void printEdge2(TreeNode node, int height, int h) {
        if (node == null) return;
        System.out.print(node.value + " ");

        if (node.left != null && node.right != null) {
            printLeftEdge(node.left, true, height, h + 1);
            printRightEdge(node.right, true, height, h + 1);
        } else {
            // 只有一个子节点，则该路径就是边界路径，直接打印
            printEdge2(node.left == null ? node.right : node.left, height, h + 1);
        }
    }

    // 打印左边界路径
    private void printLeftEdge(TreeNode node, boolean print, int height, int h) {
        if (node == null) return;
        if (print || (node.left == null && node.right == null && h == height - 1)) {
            // 最下层的叶子节点要打印
            System.out.print(node.value + " ");
        }
        printLeftEdge(node.left, print, height, h + 1);
        printLeftEdge(node.right, print && node.left == null, height, h + 1);
    }

    // 打印右边界路径
    // 因为要逆时针打印，所以右边界的打印顺序和左边界是相反的
    private void printRightEdge(TreeNode node, boolean print, int height, int h) {
        if (node == null) return;
        printRightEdge(node.left, print && node.right == null, height, h + 1);
        printRightEdge(node.right, print, height, h + 1);
        if (print || (node.left == null && node.right == null && h == height - 1)) {
            // 最下层的叶子节点必须要打印
            System.out.print(node.value + " ");
        }
    }

    public static void main(String[] args) {
        TreeNode n15 = new TreeNode(15);
        TreeNode n16 = new TreeNode(16);
        TreeNode n12 = new TreeNode(12, n15, n16);
        TreeNode n13 = new TreeNode(13);
        TreeNode n14 = new TreeNode(14);
        TreeNode n11 = new TreeNode(11, n13, n14);
        TreeNode n10 = new TreeNode(10);
        TreeNode n9 = new TreeNode(9, n12, null);
        TreeNode n8 = new TreeNode(8, null, n11);
        TreeNode n7 = new TreeNode(7);
        TreeNode n6 = new TreeNode(6, n9, n10);
        TreeNode n5 = new TreeNode(5);
        TreeNode n4 = new TreeNode(4, n7, n8);
        TreeNode n3 = new TreeNode(3, n5, n6);
        TreeNode n2 = new TreeNode(2, null, n4);
        TreeNode head = new TreeNode(1, n2, n3);

        EdgeNode en = new EdgeNode();
        en.printEdge1(head);
        System.out.println();
        en.printEdge2(head);
    }
}