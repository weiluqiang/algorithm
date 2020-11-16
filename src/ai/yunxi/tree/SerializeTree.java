package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

/**
 * 二叉树的序列化和反序列化
 * <p>
 * 二叉树被记录成文件的过程叫序列化，通过文件重建原二叉树的过程叫反序列化
 * 给定一个二叉树的头结点head，并已知二叉树节点值的类型为32位整型，
 * 请设计一种二叉树序列化和反序列化的方案，并用代码实现
 */
public class SerializeTree {

    /**
     * 二叉树的先序、中序、后序遍历能按照一定顺序完整的遍历一棵二叉树
     * 只需遍历整棵树，并记录每个节点的值，节点之间以特殊字符分隔，就能得到一个字符串作为序列化结果
     * 再按遍历逻辑反过来操作一次结果字符串，就能得到反序列化的树
     */
    // 根据前序遍历序列化
    // 以#代替空节点，!进行分隔
    public String serialByPre(TreeNode head) {
        if (head == null) return "#!";

        String result = head.value + "!";
        result += serialByPre(head.left);
        result += serialByPre(head.right);
        return result;
    }

    public static void main(String[] args) {
        TreeNode head = new TreeNode(1);
        TreeNode n2 = new TreeNode(2);
        TreeNode n3 = new TreeNode(3);
        TreeNode n4 = new TreeNode(4);
        TreeNode n5 = new TreeNode(5);
        TreeNode n6 = new TreeNode(6);
        TreeNode n7 = new TreeNode(7);
        head.left = n2;
        head.right = n3;
        n2.left = n4;
        n4.right = n7;
        n3.left = n5;
        n3.right = n6;

        SerializeTree serialize = new SerializeTree();
        System.out.println(serialize.serialByPre(head));
    }
}
