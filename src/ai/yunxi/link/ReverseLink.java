package ai.yunxi.link;

import ai.yunxi.link.pojo.DoubleNode;
import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 实现反转单向和双向链表
 */
public class ReverseLink {

    public static void main(String[] args) {
        ReverseLink reverseLink = new ReverseLink();
        Node node1 = NodeUtil.createLinkNode(1, 9, 1);
        Node res1 = reverseLink.reverseSingle(node1);
        NodeUtil.printLinkNode(res1);

        DoubleNode node2 = NodeUtil.createDoubleLinkNode(1, 9, 1);
        DoubleNode res2 = reverseLink.reverseDouble(node2);
        NodeUtil.printDoubleLinkNode(res2);
    }

    public Node reverseSingle(Node head) {
        Node prev = null;
        Node next;
        while (head != null) {
            next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }

    public DoubleNode reverseDouble(DoubleNode head) {
        DoubleNode prev = null;
        DoubleNode next;
        while (head != null) {
            next = head.next;
            head.next = prev;
            head.prev = next;
            prev = head;
            head = next;
        }
        return prev;
    }
}
