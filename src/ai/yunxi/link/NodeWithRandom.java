package ai.yunxi.link;

import java.util.HashMap;
import java.util.Map;

/**
 * 复制有随机指针节点的链表
 * <p>
 * 比如：链表1->2->3->null，其中1的rand指针指向3，2的rand指针指向null，3的rand指针指向1
 * 复制后的链表1'->2'->3'->null，并且满足1'的rand指针指向3'，2'的指向null，3'的指向1'
 * <p>
 * 进阶：要求不使用额外的数据结构，只用有限几个变量，并且时间复杂度为O(N)
 */
public class NodeWithRandom {

    /**
     * 普通方法：利用HashMap数据结构保存新节点与旧节点的映射
     */
    public Node copyListWithRandom(Node head) {
        Map<Node, Node> map = new HashMap<>();
        // 复制旧节点
        Node cur = head;
        while (cur != null) {
            map.put(cur, new Node(cur.value));
            cur = cur.next;
        }

        // 连接复制的新节点
        cur = head;
        while (cur != null) {
            map.get(cur).next = map.get(cur.next);
            map.get(cur).rand = map.get(cur.rand);
            cur = cur.next;
        }
        return map.get(head);
    }

    /**
     * 进阶方法：不要额外的数据结构，把新节点的存在旧节点的next里，变成2N长度的新链表，然后进行拆分
     */
    public Node copyListWithRandomAdvance(Node head) {
        if (head == null) {
            return null;
        }
        // 复制旧节点并组成2N长度的新链表
        Node cur = head;
        Node next;
        while (cur != null) {
            next = cur.next;
            cur.next = new Node(cur.value);
            cur.next.next = next;
            cur = next;
        }

        // 设置新节点的rand指针
        cur = head;
        while (cur != null && cur.next != null) {
            cur.next.rand = cur.rand == null ? null : cur.rand.next;
            cur = cur.next.next;
        }

        // 拆分成两个链表
        cur = head;
        Node newHead = cur.next;
        while (cur != null && cur.next != null) {
            next = cur.next;
            cur.next = cur.next.next;
            next.next = cur.next == null ? null : cur.next.next;
            cur = cur.next;
        }
        return newHead;
    }

    public static void main(String[] args) {
        NodeWithRandom nodeWithRandom = new NodeWithRandom();
        NodeWithRandom.Node head = new Node(1);
        NodeWithRandom.Node node1 = new Node(2);
        NodeWithRandom.Node node2 = new Node(3);
        head.next = node1;
        node1.next = node2;
        head.rand = node2;
        node2.rand = head;

        printNode(nodeWithRandom.copyListWithRandom(head));
        printNode(head);

        printNode(nodeWithRandom.copyListWithRandomAdvance(head));
        printNode(head);
    }

    private static void printNode(Node head) {
        Node cur = head;
        while (cur != null) {
            String rand = cur.rand == null ? "null" : String.valueOf(cur.rand.value);
            System.out.print(cur.value + "(" + rand + ")->");
            cur = cur.next;
        }
        System.out.print("null");
        System.out.print("\n");
    }

    static class Node {
        public int value;
        public Node next;
        public Node rand;

        public Node(int value) {
            this.value = value;
        }
    }
}
