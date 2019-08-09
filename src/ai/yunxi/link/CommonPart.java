package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 打印两个有序链表的公共部分
 * <p>
 * 给定两个有序链表的头指针head1和head2，打印它们的公共部分
 * <p>
 * 1.如果head1的值小于head2，则head1往下移动
 * 2.如果head2的值小于head1，则head2往下移动
 * 3.如果head1和head2的值相等，则打印这个值，都往下移动
 * 4.两者有任何一个为null，则打印终止
 */
public class CommonPart {

    public static void main(String[] args) {
        Node node1 = NodeUtil.createLinkNode(1, 5, 1);
        Node nod1 = NodeUtil.createLinkNode(1, 7, 2);

        CommonPart commonPart = new CommonPart();
        commonPart.printCommonPart(node1, nod1);
    }

    public void printCommonPart(Node head1, Node head2) {
        while (head1 != null && head2 != null) {
            if (head1.value < head2.value)
                head1 = head1.next;
            else if (head1.value > head2.value)
                head2 = head2.next;
            else {
                System.out.println("公共部分: " + head1.value);
                head1 = head1.next;
                head2 = head2.next;
            }
        }
    }
}
