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
}
