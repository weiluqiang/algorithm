package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 反转部分单向链表
 * <p>
 * 给定一个单向链表的head和两个整数from、to，在链表上把第from和第to个节点之前的部分进行反转
 * 如：1->2->3->4->5->null，from=2,to=4，调整结果为：1->4->3->2->5->null
 * 再如：1->2->3->null，from=1,to=3，调整结果为：3->2->1->null
 * <p>
 * 要求：
 * 1.如果链表的长度为N，要求时间复杂度为O(N)，额外的空间复杂度为O(1)
 * 2.如果不满足1<=from<=to<=N，则不用调整
 */
public class ReversePartLink {

    /**
     * 1.先判断是否满足1<=from<=to<=N，不满足则直接返回
     * 2.找到第from-1个节点fromPrev和第to+1个节点toNext，把反转的部分先反转，然后正确的链接fromPrev和toNext
     * 3.如果fromPrev为null，则说明反转部分包含头节点，则返回新的头节点，也就是反转部分的最后一个节点，否则返回原来的头节点
     */
    public Node reversePart(Node head, int from, int to) {
        if (from > to || from < 1) return head;

        int length = 0;
        Node node = head;
        Node fromPrev = null;
        Node toNext = null;
        while (node != null) {
            length++;
            if (length == from - 1) fromPrev = node;
            if (length == to + 1) toNext = node;
            node = node.next;
        }
        if (to > length) return head;

        node = fromPrev == null ? head : fromPrev.next;
        Node node2 = node.next;
        node.next = toNext;
        Node next;
        while (node2 != toNext) {
            next = node2.next;
            node2.next = node;
            node = node2;
            node2 = next;
        }

        if (fromPrev != null) {
            fromPrev.next = node;
            return head;
        } else {
            return node;
        }
    }

    public static void main(String[] args) {
        ReversePartLink reserve = new ReversePartLink();
        Node head = NodeUtil.createLinkNode(1, 5, 1);
        NodeUtil.printLinkNode(reserve.reversePart(head, 1, 4));
    }
}
