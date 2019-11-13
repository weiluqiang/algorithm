package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

import java.util.Stack;

/**
 * 两个单链表生成相加链表
 * <p>
 * 假设链表中每一个节点的值都在0-9之间，那么链表整体就可以代表一个整数
 * 例如：9->3->7，就可以代表整数937
 * 给定两个这种链表的头节点head1和head2，请生成代表两个整数相加值的链表
 * 例如：9->3->7和8->3，结果为1->0->2->0
 */
public class LinkListAdd {

    /**
     * 方法1：先把两个链表转化成一个整数，相加之后再转回链表
     * 注意：链表很长的话有可能超过整数最大值，不推荐
     */
    public Node addLinkList1(Node head1, Node head2) {
        long sum = listToLong(head1) + listToLong(head2);
        char[] num = String.valueOf(sum).toCharArray();
        int num0 = (int) ('0');
        Node newHead = new Node(num[0] - num0);
        Node pre = newHead;
        for (int i = 1; i < num.length; i++) {
            Node node = new Node(num[i] - num0);
            pre.next = node;
            pre = node;
        }
        return newHead;
    }

    private long listToLong(Node head) {
        if (head == null) {
            return 0L;
        }
        int count = 0;
        Node cur = head;
        while (cur != null) {
            count++;
            cur = cur.next;
        }

        char[] num = new char[count];
        cur = head;
        count = 0;
        while (cur != null) {
            num[count++] = (char) (cur.value + '0');
            cur = cur.next;
        }
        return Long.valueOf(new String(num));
    }

    /**
     * 方法2：利用栈结构，把链表都转成栈结构，然后依次出栈相加，并存入新的链表中
     */
    public Node addLinkList2(Node head1, Node head2) {
        Stack<Integer> stack1 = new Stack<>();
        while (head1 != null) {
            stack1.push(head1.value);
            head1 = head1.next;
        }
        Stack<Integer> stack2 = new Stack<>();
        while (head2 != null) {
            stack2.push(head2.value);
            head2 = head2.next;
        }

        int carryBit = 0;
        int num1;
        int num2;
        int num;
        Node node = null;
        Node pre = null;
        while (!stack1.isEmpty() || !stack2.isEmpty()) {
            num1 = stack1.isEmpty() ? 0 : stack1.pop();
            num2 = stack2.isEmpty() ? 0 : stack2.pop();
            num = num1 + num2 + carryBit;
            if (num >= 10) {
                carryBit = 1;
            } else {
                carryBit = 0;
            }

            node = new Node(num < 10 ? num : num - 10);
            node.next = pre;
            pre = node;
        }

        // 最后一次求和如果有进位，需在链表头再补一位
        if (carryBit > 0) {
            node = new Node(carryBit);
            node.next = pre;
        }
        return node;
    }

    public static void main(String[] args) {
        Node head1 = new Node(9);
        Node node1 = new Node(3);
        Node node2 = new Node(7);
        head1.next = node1;
        node1.next = node2;

        Node head2 = new Node(8);
        head2.next = new Node(3);

        LinkListAdd listAdd = new LinkListAdd();
        NodeUtil.printLinkNode(listAdd.addLinkList1(head1, head2));
        NodeUtil.printLinkNode(listAdd.addLinkList2(head1, head2));
    }
}
