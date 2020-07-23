package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

import java.util.Stack;

/**
 * 单链表的每K个节点之间逆序
 * 给定一个单链表的头节点head，实现一个调整单链表的函数，使得每K个节点逆序，
 * 如果最后不够K个节点一组，则不调整最后几个节点
 * 例如：1->2->3->4->5->6->7->8->null，K=3
 * 调整后为：3->2->1->6->5->4->7->8->null，其中7、8不够一组不调整
 */
public class ReverseKNode {

    /**
     * 1.利用栈结构逆序，时间复杂度为O(n)，额外空间复杂度为O(K)
     */
    private Node reverseKNode1(Node head, int K) {
        if (K < 2) {
            return head;
        }
        Stack<Node> stack = new Stack<>();
        Node cur = head;
        Node newHead = null;
        Node prev = null;
        Node next;
        Node temp;
        Node tempNext;
        while (cur != null) {
            next = cur.next;
            stack.push(cur);
            if (stack.size() == K) {
                temp = stack.pop();
                if (prev == null) {
                    //返回新的头节点
                    newHead = temp;
                } else {
                    //重新链接新的头节点
                    prev.next = temp;
                }
                while (!stack.isEmpty()) {
                    tempNext = stack.pop();
                    temp.next = tempNext;
                    temp = tempNext;
                }
                temp.next = next;
                prev = temp;
            }
            cur = next;
        }
        return newHead;
    }

    /**
     * 2.直接在原链表中调整，时间复杂度为O(n)，额外空间复杂度为O(1)
     */
    private Node reverseKNode2(Node head, int K) {
        if (K < 2) {
            return head;
        }
        Node cur = head;
        Node newHead = null;
        Node next;
        Node left = null;
        Node right;
        Node start = head;
        int count = 0;
        while (cur != null) {
            next = cur.next;
            count++;
            if (count == K) {
                if (newHead == null) {
                    newHead = cur;
                }
                right = next;
                reverseLink(start, cur, left, right);
                left = start;//反转之后，下一次的左边界是start不是end
                start = next;
                count = 0;
            }
            cur = next;
        }
        return newHead;
    }

    //反转从start到end的链表，并链接在left和right之间
    private void reverseLink(Node start, Node end, Node left, Node right) {
        Node cur = start;
        Node prev = right;
        Node next;
        while (cur != end) {
            next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        //最后的end节点
        cur.next = prev;
        if (left != null) {
            left.next = cur;
        }
    }

    public static void main(String[] args) {
        ReverseKNode reverse = new ReverseKNode();
        Node head = NodeUtil.createLinkNode(1, 8, 1);
        Node head1 = reverse.reverseKNode1(head, 3);
        NodeUtil.printLinkNode(head1);
        NodeUtil.printLinkNode(reverse.reverseKNode2(head1, 3));

        Node h = NodeUtil.createLinkNode(1, 8, 1);
        Node h1 = reverse.reverseKNode2(h, 4);
        NodeUtil.printLinkNode(h1);
        NodeUtil.printLinkNode(reverse.reverseKNode1(h1, 4));
    }
}
