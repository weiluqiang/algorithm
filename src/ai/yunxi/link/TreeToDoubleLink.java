package ai.yunxi.link;

import ai.yunxi.link.pojo.DoubleNode;
import ai.yunxi.link.pojo.NodeUtil;
import ai.yunxi.tree.pojo.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 将搜索二叉树转换成双向链表
 * 一棵搜索二叉树为：
 * ---------6
 * ------4-----7
 * ----2---5----9
 * --1--3------8
 * 它转换成双向链表后从头到尾依次为1-9
 */
public class TreeToDoubleLink {

    //利用队列容器收集二叉树的遍历结果，时间复杂度为O(n)，额外的空间复杂度O(n)
    private DoubleNode convert1(TreeNode root) {
        Queue<DoubleNode> queue = new LinkedList<>();
        transfer(root, queue);

        DoubleNode cur;
        DoubleNode prev = null;
        DoubleNode head = queue.peek();
        while (!queue.isEmpty()) {
            cur = queue.poll();
            cur.prev = prev;
            cur.next = queue.peek();
            prev = cur;
        }
        return head;
    }

    private void transfer(TreeNode node, Queue<DoubleNode> queue) {
        if (node != null) {
            transfer(node.left, queue);
            queue.offer(new DoubleNode(node.value));
            transfer(node.right, queue);
        }
    }

    //直接利用递归函数，时间复杂度为O(n)，额外的空间复杂度O(h)，其中h为二叉树的高度
    private DoubleNode convert2(TreeNode root) {
        if (root == null) {
            return null;
        }

        DoubleNode tail = process(root);
        DoubleNode head = tail.next;
        tail.next = null;
        return head;
    }

    //此递归函数负责把当前节点的左半部分和右半部分分别拼接在当前节点上，并返回链表的尾节点
    //由于拼接过程中需要左半部分的尾节点和右半部分的头节点，所以它返回的尾节点的next指向自己的头节点
    private DoubleNode process(TreeNode node) {
        if (node == null) {
            return null;
        }

        DoubleNode left = process(node.left);
        DoubleNode cur = new DoubleNode(node.value);
        DoubleNode right = process(node.right);

        //记录新链表的头节点和尾节点
        DoubleNode head = cur;
        DoubleNode tail = cur;
        //左半部分的拼接，尾节点left
        //若返回的left不为空，则left.next也不为空
        if (left != null) {
            head = left.next;
            cur.prev = left;
            left.next = cur;
        }
        //右半部分的拼接，尾节点right->头节点
        //若返回的right不为空，则right.next也不为空
        if (right != null) {
            tail = right;
            cur.next = right.next;
            right.next.prev = cur;
        }
        //重新链接新链表的头和尾
        tail.next = head;
        return tail;
    }

    public static void main(String[] args) {
        TreeNode n1 = new TreeNode(6);
        TreeNode n21 = new TreeNode(4);
        TreeNode n22 = new TreeNode(7);
        TreeNode n31 = new TreeNode(2);
        TreeNode n32 = new TreeNode(5);
        TreeNode n33 = new TreeNode(9);
        TreeNode n41 = new TreeNode(1);
        TreeNode n42 = new TreeNode(3);
        TreeNode n43 = new TreeNode(8);
        n1.left = n21;
        n1.right = n22;
        n21.left = n31;
        n21.right = n32;
        n22.right = n33;
        n31.left = n41;
        n31.right = n42;
        n33.left = n43;

        TreeToDoubleLink treeToLink = new TreeToDoubleLink();
        NodeUtil.printDoubleLinkNode(treeToLink.convert1(n1));
        NodeUtil.printDoubleLinkNode(treeToLink.convert2(n1));
    }
}
