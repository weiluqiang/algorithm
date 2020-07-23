package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 合并两个有序的单链表
 * 给定两个有序单链表的头节点head1和head2，请合并两个量表，合并后的链表依然有序
 * 如：
 * 1->2->3->7->null
 * 1->3->5->7->9->11->null
 * 合并后的链表为：1->1->2->3->3->5->7->7->9->11->null
 */
public class MergeOrderlyLink {

    public Node merge(Node head1, Node head2) {
        if (head1 == null || head2 == null) {
            return head1 == null ? head2 : head1;
        }

        Node cur1 = head1;
        Node cur2 = head2;
        Node newHead;
        if (head1.value < head2.value) {
            newHead = head1;
            cur1 = cur1.next;
        } else {
            newHead = head2;
            cur2 = cur2.next;
        }
        Node cur = newHead;
        while (cur1 != null && cur2 != null) {
            if (cur1.value < cur2.value) {
                cur.next = cur1;
                cur = cur1;
                cur1 = cur1.next;
            } else {
                cur.next = cur2;
                cur = cur2;
                cur2 = cur2.next;
            }
        }
        if (cur1 == null) {
            cur.next = cur2;
        } else {
            cur.next = cur1;
        }
        return newHead;
    }

    //cur1作为最后合并的链表，把cur2上的节点全部插入到cur1中

    /**
     * cur1作为最后合并的链表，把cur2上的节点全部插入到cur1中
     * 1.cur1指向head值较小的节点，cur2的节点插入cur1的链表中
     * 2.若cur1的值小于等于cur2，则不需插入，cur1和prev直接指向下一个节点
     * 3.若cur1大于cur2，则把cur2节点插入cur1节点之前，
     * cur2指向cur2的下一个节点，cur1不变，prev指向cur1的前一节点即cur2
     */
    public Node merge1(Node head1, Node head2) {
        if (head1 == null || head2 == null) {
            return head1 == null ? head2 : head1;
        }

        Node head = head1.value < head2.value ? head1 : head2;
        Node cur1 = head;
        Node cur2 = head == head1 ? head2 : head1;
        Node next;
        Node prev = null;
        while (cur1 != null && cur2 != null) {
            if (cur1.value <= cur2.value) {
                //"<="判断保证head1无论大于、小于、等于head2，第一次循环都会进入这个分支
                //因此prev不会为空
                prev = cur1;
                cur1 = cur1.next;
            } else {
                next = cur2.next;
                if (prev != null) {
                    prev.next = cur2;
                }
                cur2.next = cur1;
                prev = cur2;
                cur2 = next;
            }
        }

        if (cur1 == null) {
            prev.next = cur2;
        }
        return head;
    }

    public static void main(String[] args) {
        Node head1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(7);
        head1.next = n2;
        n2.next = n3;
        n3.next = n4;
        Node head2 = NodeUtil.createLinkNode(1, 11, 2);

        MergeOrderlyLink mergeLink = new MergeOrderlyLink();
        NodeUtil.printLinkNode(mergeLink.merge1(head1, head2));
        NodeUtil.printLinkNode(mergeLink.merge(head1, head2));
    }
}
