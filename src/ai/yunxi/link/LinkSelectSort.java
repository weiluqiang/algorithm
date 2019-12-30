package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 单链表的选择排序
 * 给定一个无序单链表的头节点head，实现单链表的选择排序
 * 要求：额外的空间复杂度为O(1)
 */
public class LinkSelectSort {

    public Node selectSort(Node head) {
        Node cur = head;//当前的外部遍历节点
        Node cur1;//当前的内部遍历节点
        Node prev = null;//当前节点的前一节点
        Node sort = null;//有序链表的当前节点
        Node min;//当前的最小值节点
        Node minPrev;//当前最小值节点的前一节点
        while (cur != null) {
            //找出最小值节点
            min = cur;
            minPrev = prev;
            cur1 = cur.next;
            prev = cur;
            while (cur1 != null) {
                if (cur1.value < min.value) {
                    min = cur1;
                    minPrev = prev;
                }
                prev = cur1;
                cur1 = cur1.next;
            }

            //从原链表删除最小值节点
            if (cur == min) {
                cur = min.next;
            } else {
                minPrev.next = min.next;
            }

            //拼接有序链表
            if (sort == null) {
                head = min;
            } else {
                sort.next = min;
            }
            sort = min;
        }
        return head;
    }

    public static void main(String[] args) {
        Node n1 = new Node(6);
        Node n2 = new Node(8);
        Node n3 = new Node(7);
        Node n4 = new Node(4);
        Node n5 = new Node(9);
        Node n6 = new Node(2);
        Node n7 = new Node(1);
        Node n8 = new Node(3);
        Node n9 = new Node(5);
        Node n10 = new Node(10);
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        n5.next = n6;
        n6.next = n7;
        n7.next = n8;
        n8.next = n9;
        n9.next = n10;

        LinkSelectSort selectSort = new LinkSelectSort();
        NodeUtil.printLinkNode(selectSort.selectSort(n1));
    }
}
