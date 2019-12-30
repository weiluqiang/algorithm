package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 按照左右半区的方式重新组合单链表
 * 给定一个单链表的头节点head，长度为N，若N为偶数，则左右半区都为N/2，
 * 若N为奇数，则左半区为N/2，右半区为N/2+1。
 * 左半区记为L1->L2->null，右半区记为R1->R2->null，
 * 则组合后的链表为L1->R1->L2->R2->null
 * 例如：
 * 1->null，调整为1->null
 * 1->2->null，调整为1->2->null
 * 1->2->3->null，调整为1->2->3->null
 * 1->2->3->4->null，调整为1->3->2->4->null
 * 1->2->3->4->5->null，调整为1->3->2->4->5->null
 * 1->2->3->4->5->6->null，调整为1->4->2->5->3->6->null
 */
public class RelocateLink {

    /**
     * 假设链表长度为N，要求时间复杂度为O(N)，额外的空间复杂度为O(1)
     * 思路：
     * 1.若链表为null或者长度为1，不用调整；
     * 2.若长度大于1，则寻找左半区的最后一个节点，记为mid
     * 长度为2是，mid=1，长度为3时，mid=1，长度为4时，mid=2，
     * 长度为5时，mid=2，长度为6时，mid=3，...
     * 可见长度从2开始，每增加2，mid的位置加1
     * 3.找到mid之后，把链表分成左右两个半区，分别记为left(head)和right(mid.next)
     * 4.将左右半区依次链接
     */
    public void relocate(Node head) {
        if (head == null || head.next == null) {
            return;
        }

        Node mid = head;
        Node right = head.next;
        while (right.next != null && right.next.next != null) {
            mid = mid.next;
            right = right.next.next;
        }
        right = mid.next;
        mid.next = null;

        Node left = head;
        Node rightNext;
        while (left.next != null) {
            rightNext = right.next;
            right.next = left.next;
            left.next = right;
            left = right.next;
            right = rightNext;
        }
        left.next = right;
    }

    public static void main(String[] args) {
        RelocateLink relocateLink = new RelocateLink();
        Node h1 = NodeUtil.createLinkNode(1, 1, 1);
        relocateLink.relocate(h1);
        NodeUtil.printLinkNode(h1);
        h1 = NodeUtil.createLinkNode(1, 2, 1);
        relocateLink.relocate(h1);
        NodeUtil.printLinkNode(h1);
        h1 = NodeUtil.createLinkNode(1, 3, 1);
        relocateLink.relocate(h1);
        NodeUtil.printLinkNode(h1);
        h1 = NodeUtil.createLinkNode(1, 4, 1);
        relocateLink.relocate(h1);
        NodeUtil.printLinkNode(h1);
        h1 = NodeUtil.createLinkNode(1, 5, 1);
        relocateLink.relocate(h1);
        NodeUtil.printLinkNode(h1);
        h1 = NodeUtil.createLinkNode(1, 6, 1);
        relocateLink.relocate(h1);
        NodeUtil.printLinkNode(h1);
    }
}
