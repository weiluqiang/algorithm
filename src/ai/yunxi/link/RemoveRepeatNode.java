package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 删除无序链表中值重复的节点
 * 例如：1->2->3->3->4->4->2->1->1->null，删除之后为1->2->3->4->null
 */
public class RemoveRepeatNode {

    //利用无重复集合set，时间复杂度O(n)，额外的空间复杂度O(n)
    private void removeRepeat1(Node head) {
        if (head != null) {
            Set<Integer> set = new HashSet<>();
            set.add(head.value);

            Node prev = head;
            Node cur = head.next;
            Node next;
            while (cur != null) {
                next = cur.next;
                if (set.contains(cur.value)) {
                    //删除当前节点
                    prev.next = next;
                } else {
                    set.add(cur.value);
                    prev = cur;
                }
                cur = next;
            }
        }
    }

    //利用当前节点之前的节点都是不重复的，额外的空间复杂度只要O(1)，但时间复杂度为O(n*n)
    private void removeRepeat2(Node head) {
        if (head != null) {
            Node prev = head;
            Node cur = head.next;
            Node next;
            Node temp;
            boolean isRepeat;
            while (cur != null) {
                next = cur.next;
                //判断是否有重复值
                temp = head;
                isRepeat = false;
                while (temp != cur) {
                    if (temp.value == cur.value) {
                        isRepeat = true;
                        break;
                    }
                    temp = temp.next;
                }

                if (isRepeat) {
                    prev.next = next;
                } else {
                    prev = cur;
                }
                cur = next;
            }
        }
    }

    //利用一种类似选择排序的算法，每个节点与其后的所有节点比较，删除重复值
    //额外的空间复杂度为O(1)，时间复杂度为O(n*n)
    private void removeRepeat3(Node head) {
        if (head != null) {
            Node cur = head;
            Node start;
            Node prev;
            while (cur != null) {
                start = cur.next;
                prev = cur;
                while (start != null) {
                    if (start.value == cur.value) {
                        prev.next = start.next;
                    } else {
                        prev = start;
                    }
                    start = start.next;
                }
                cur = cur.next;
            }
        }
    }

    public static void main(String[] args) {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        n1.next = n2;
        Node n3 = new Node(3);
        n2.next = n3;
        Node n4 = new Node(3);
        n3.next = n4;
        Node n5 = new Node(4);
        n4.next = n5;
        Node n6 = new Node(4);
        n5.next = n6;
        Node n7 = new Node(2);
        n6.next = n7;
        Node n8 = new Node(1);
        n7.next = n8;
        n8.next = new Node(1);

        RemoveRepeatNode repeat = new RemoveRepeatNode();
        repeat.removeRepeat1(n1);
        repeat.removeRepeat2(n1);
        repeat.removeRepeat3(n1);
        NodeUtil.printLinkNode(n1);
    }
}
