package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

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
    public String serialize(TreeNode head) {
        if (head == null) return "#!";

        String result = head.value + "!";
        result += serialize(head.left);
        result += serialize(head.right);
        return result;
    }

    /**
     * 针对前序遍历的反序列化流程：
     * 1.获取全部节点的数组，第一个节点是头节点，接下来的一批节点都是它的左子树节点
     * 2.接下来构建左子树，当前节点仍是左子树的父节点，下面继续构建左子节点的左子树
     * 3.当遇到第一个#号时，表示它的上一个节点的左边空，把这个节点记为A
     * 4.接下来的节点构建A的右子树
     * 4-a.如果下一节点为#，则A为叶子节点，A已经构建完毕，返回上一个节点继续处理A的父节点的右子树
     * 4-b.如果下一节点不为#，则需构建右子树，构建过程跟上面一样，直到构建完毕，返回A的父节点继续处理
     */
    public TreeNode deserialize(String str) {
        String[] values = str.split("!");
        // 为了操作方便，把数组转成队列
        Queue<String> queue = new LinkedList<>();
        Collections.addAll(queue, values);
        return buildTree(queue);
    }

    // 通过队列构建树
    private TreeNode buildTree(Queue<String> queue) {
        if (queue.isEmpty()) return null;
        String val = queue.poll();
        if (val.equals("#")) {//空节点
            return null;
        }
        // 把当前节点作为父节点，构建完之后返回
        TreeNode head = new TreeNode(Integer.parseInt(val));
        head.left = buildTree(queue);
        head.right = buildTree(queue);
        return head;
    }

    /**
     * 按层级的方式遍历树
     * 1.打印当前树，按照头、左、右的顺序依次打印
     * 2.如果左右子节点不为空，则按步骤1依次打印左右树
     * 遍历的时候需借助一个队列，用来按顺序记录需要打印的节点
     */
    public String serializeByLevel(TreeNode head) {
        if (head == null) return "#!";

        StringBuilder res = new StringBuilder(head.value + "!");
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(head);
        // 循环队列按顺序打印每个节点
        while (!queue.isEmpty()) {
            head = queue.poll();
            if (head.left != null) {
                res.append(head.left.value).append("!");
                // 左节点不为空，放入队列，下次循环打印左节点
                queue.offer(head.left);
            } else {
                res.append("#!");
            }
            if (head.right != null) {
                res.append(head.right.value).append("!");
                // 右节点不为空，也放入队列，下次(或下下次)循环打印右节点
                queue.offer(head.right);
            } else {
                res.append("#!");
            }
        }
        return res.toString();
    }

    /**
     * 按层级方式遍历的反序列化，其逻辑和序列化十分相似
     * 1.取出的第一个节点为根节点
     * 2.接下来取出的节点2个一组，第1个是左子节点，第2个是右子节点
     * 3.用一个队列保存未处理完的节点，每次从队头取1个节点，它的左右子节点分别是步骤2里取出的节点
     * 4.步骤2中取的节点不为空，则表明它有子节点，放入队尾
     */
    public TreeNode deserializeByLevel(String str) {
        String[] values = str.split("!");
        if (values.length == 0) return null;
        int index = 0;
        TreeNode head = createNode(values[index++]);

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(head);
        TreeNode node;
        while (!queue.isEmpty()) {
            node = queue.poll();
            node.left = createNode(values[index++]);
            node.right = createNode(values[index++]);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return head;
    }

    private TreeNode createNode(String val) {
        if (val.equals("#")) return null;
        return new TreeNode(Integer.parseInt(val));
    }

    public static void main(String[] args) {
        TreeNode head = new TreeNode(1);
        TreeNode n2 = new TreeNode(2);
        TreeNode n3 = new TreeNode(3);
        TreeNode n4 = new TreeNode(4);
        TreeNode n5 = new TreeNode(5);
        TreeNode n6 = new TreeNode(6);
        TreeNode n7 = new TreeNode(7);
        TreeNode n8 = new TreeNode(8);
        TreeNode n9 = new TreeNode(9);
        head.left = n2;
        head.right = n3;
        n2.left = n4;
        n4.left = n7;
        n4.right = n8;
        n3.left = n5;
        n3.right = n6;
        n7.left = n9;

        SerializeTree serialize = new SerializeTree();
        String str = serialize.serialize(head);
        System.out.println(str);
        TreeNode node = serialize.deserialize(str);
        String str1 = serialize.serializeByLevel(node);
        System.out.println(str1);
        node = serialize.deserializeByLevel(str1);
        System.out.println(serialize.serialize(node));
    }
}
