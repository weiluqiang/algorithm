package ai.yunxi.link;

import ai.yunxi.link.pojo.DoubleNode;
import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 删除单链表和双链表中倒数第K个节点
 * <p>
 * 1.若链表为null或K小于1，则参数无效直接返回
 * 2.让链表从头开始移动尾，每移动一次K值减1
 * 3.若最后K大于0，则K大于链表长度N，返回整个链表
 * 4.若K等于0，则K等于N，倒数第K个节点就是head，返回head.next
 * 5.若K小于0，则此时为K-N，重新从头遍历，每次移动值加1，直到值为0，此时遍历了N-K个节点，它的下一个节点就是倒数第K个
 */
public class LastKNode {

    public static void main(String[] args) {
        Node node1 = NodeUtil.createLinkNode(1, 5, 1);
        LastKNode lastKNode = new LastKNode();
        Node res1 = lastKNode.removeLastKNode(node1, 5);
        NodeUtil.printLinkNode(res1);

        System.out.println("-------------");

        DoubleNode nod1 = NodeUtil.createDoubleLinkNode(1, 6, 1);
        DoubleNode res2 = lastKNode.removeLastKNode(nod1, 1);
        NodeUtil.printDoubleLinkNode(res2);

    }

    // 删除单链表中倒数第K个节点
    public Node removeLastKNode(Node head, int K) {
        if (head == null || K < 1)
            return head;

        Node cur = head;
        while (cur != null) {
            K--;
            cur = cur.next;
        }

        if (K == 0)
            head = head.next;

        if (K < 0) {
            cur = head;
            while (++K != 0)
                cur = cur.next;

            //此时cur正是倒数第K+1的节点，删除它的下一个节点
            cur.next = cur.next.next;
        }
        return head;
    }

    // 删除双链表中倒数第K个节点
    public DoubleNode removeLastKNode(DoubleNode head, int K) {
        if (head == null || K < 1)
            return head;

        DoubleNode cur = head;
        while (cur != null) {
            K--;
            cur = cur.next;
        }

        if (K == 0) {
            head = head.next;
            head.prev = null;
        }

        if (K < 0) {
            cur = head;
            while (++K != 0)
                cur = cur.next;

            //此时cur正是倒数第K+1的节点，删除它的下一个节点
            DoubleNode newNode = cur.next.next;
            cur.next = newNode;
            if (newNode != null)
                newNode.prev = cur;
        }
        return head;
    }
}
