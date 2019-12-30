package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

import java.util.Stack;

/**
 * 在单链表中删除指定值的节点
 * 例如：1->2->3->4->null，num=3，调整后的链表为1->2->4->null
 */
public class RemoveFixedNode {

    //利用栈收集节点的方法，时间复杂度为O(n)，额外的空间复杂度O(n)
    private Node removeNode1(Node head, int num) {
        Stack<Node> stack = new Stack<>();
        while (head != null) {
            if (head.value != num) {
                stack.push(head);
            }
            head = head.next;
        }

        while (!stack.isEmpty()) {
            stack.peek().next = head;
            head = stack.pop();
        }
        return head;
    }

    //直接遍历链表删除，时间复杂度为O(n)，额外的空间复杂度O(1)
    private Node removeNode2(Node head, int num) {
        Node cur = head;
        Node newHead = head;
        Node prev = null;
        Node next;
        while (cur != null) {
            if (cur.value == num) {
                if (prev == null) {
                    next = newHead.next;
                    newHead.next = null;
                    newHead = next;
                } else {
                    prev.next = cur.next;
                }
            } else {
                prev = cur;
            }
            cur = cur.next;
        }
        return newHead;
    }

    //直接遍历链表删除，逻辑稍有不同
    private Node removeNode3(Node head, int num) {
        //先找出新的head
        while (head != null) {
            if (head.value != num) {
                break;
            }
            head = head.next;
        }

        Node cur = head;
        Node prev = head;
        while (cur != null) {
            if (cur.value == num) {
                prev.next = cur.next;
            } else {
                prev = cur;
            }
            cur = cur.next;
        }
        return head;
    }

    public static void main(String[] args) {
        Node head = NodeUtil.createLinkNode(1, 12, 1);
        RemoveFixedNode remove = new RemoveFixedNode();
        Node head1 = remove.removeNode1(head, 3);
        NodeUtil.printLinkNode(head1);
        Node head2 = remove.removeNode1(head1, 1);
        NodeUtil.printLinkNode(head2);
        Node head3 = remove.removeNode2(head2, 11);
        NodeUtil.printLinkNode(head3);
        Node head4 = remove.removeNode2(head3, 2);
        NodeUtil.printLinkNode(head4);
        NodeUtil.printLinkNode(remove.removeNode3(head4, 9));
        NodeUtil.printLinkNode(remove.removeNode3(head4, 4));
    }
}
