package ai.yunxi.link.pojo;

import java.util.ArrayList;
import java.util.List;

public class NodeUtil {

    public static Node createLinkNode(int start, int end, int step) {
        return createLinkNode(start, end, step, false);
    }

    public static Node createLinkNode(int start, int end, int step, boolean isLoop) {
        Node prev = null;
        Node head = null;
        for (int i = start; i <= end; i += step) {
            Node node = new Node(i);
            if (i == start)
                head = node;
            else
                prev.next = node;

            prev = node;
        }
        if (isLoop) {
            if (prev != null) {
                prev.next = head;
            }
        }
        return head;
    }

    public static void printLinkNode(Node head) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.value);
            head = head.next;
        }
        System.out.println(list);
    }

    public static void printLoopLinkNode(Node head) {
        List<Integer> list = new ArrayList<>();
        Node node = head;
        do {
            list.add(node.value);
            node = node.next;
        } while (node != head);
        System.out.println(list);
    }

    public static DoubleNode createDoubleLinkNode(int start, int end, int step) {
        DoubleNode prev = null;
        DoubleNode head = null;
        for (int i = start; i <= end; i += step) {
            DoubleNode node = new DoubleNode(i);
            if (i == start)
                head = node;
            else {
                prev.next = node;
                node.prev = prev;
            }

            prev = node;
        }
        return head;
    }

    public static void printDoubleLinkNode(DoubleNode head) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.value);
            head = head.next;
        }
        System.out.println(list);
    }
}
