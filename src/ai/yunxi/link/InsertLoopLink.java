package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 向有序的环形单链表中插入新节点
 * 一个环形单链表从头节点head开始顺序排列，同时最后的节点指向头节点。
 * 给定这样一个链表的头节点head和一个整数num，请生成节点值为num的新节点，
 * 并插入到这个环形链表中，保证调整后的链表依然有序
 */
public class InsertLoopLink {

    //以升序链表为例
    public Node insertNode(Node head, int num) {
        Node node = new Node(num);
        if (head == null) {
            return node;
        }

        Node cur = head.next;
        Node prev = head;
        while (cur != head) {
            if (num <= cur.value) {
                break;
            }
            prev = cur;
            cur = cur.next;
        }

        //如果第一次就找到了，需要再与head节点比较
        if (prev == head && num <= head.value) {
            while (cur != head) {
                prev = cur;
                cur = cur.next;
            }
            head = node;
        }
        prev.next = node;
        node.next = cur;
        return head;
    }

    //以升序链表为例
    public Node insertNode1(Node head, int num) {
        Node node = new Node(num);
        if (head == null) {
            return node;
        }

        Node prev = head;
        Node cur = head.next;
        while (cur != head) {
            if (num >= prev.value && num <= cur.value) {
                break;
            }
            prev = cur;
            cur = cur.next;
        }
        prev.next = node;
        node.next = cur;
        return num < head.value ? node : head;
    }

    public static void main(String[] args) {
        Node head = NodeUtil.createLinkNode(3, 5, 1, true);
        InsertLoopLink loopLink = new InsertLoopLink();
        head = loopLink.insertNode(head, 6);
        NodeUtil.printLoopLinkNode(head);
        head = loopLink.insertNode(head, 2);
        NodeUtil.printLoopLinkNode(head);
        head = loopLink.insertNode(head, 6);
        NodeUtil.printLoopLinkNode(head);
        head = loopLink.insertNode(head, 2);
        NodeUtil.printLoopLinkNode(head);

        head = loopLink.insertNode1(head, 7);
        NodeUtil.printLoopLinkNode(head);
        head = loopLink.insertNode1(head, 1);
        NodeUtil.printLoopLinkNode(head);
        head = loopLink.insertNode1(head, 7);
        NodeUtil.printLoopLinkNode(head);
        head = loopLink.insertNode1(head, 1);
        NodeUtil.printLoopLinkNode(head);
    }
}
