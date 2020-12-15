package ai.yunxi.tree.pojo;

public class TreeNode {

    public int value;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int value) {
        this.value = value;
    }

    public TreeNode(int value, TreeNode left, TreeNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public static TreeNode getDefault() {
        TreeNode n13 = new TreeNode(13);
        TreeNode n12 = new TreeNode(12, n13, null);
        TreeNode n11 = new TreeNode(11);
        TreeNode n10 = new TreeNode(10, n11, null);
        TreeNode n9 = new TreeNode(9, null, n12);
        TreeNode n8 = new TreeNode(8);
        TreeNode n7 = new TreeNode(7, n9, n10);
        TreeNode n6 = new TreeNode(6);
        TreeNode n5 = new TreeNode(5);
        TreeNode n4 = new TreeNode(4, n7, n8);
        TreeNode n3 = new TreeNode(3, n5, n6);
        TreeNode n2 = new TreeNode(2, n4, null);
        return new TreeNode(1, n2, n3);
    }

    public static TreeNode getDefault2() {
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
        return new TreeNode(6, n11, n12);
    }

    @Override
    public String toString() {
        return "value: " + value;
    }
}
