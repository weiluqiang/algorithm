package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

import java.util.Stack;

/**
 * 判断一个链表是否为回文结构
 * <p>
 * 如：
 * 1->2->1，返回true
 * 1->2->2->1，返回true
 * 15->6->7->6->15，返回true
 * 1->2->3->1，返回false
 * 进阶：
 * 如果链表长度为N，要求时间复杂度为O(N)，额外的控件复杂度为O(1)
 */
public class Palindrome {

    /**
     * 普通方法：利用栈结构
     * 从左到右遍历链表，把节点依次压入栈中。
     * 再把所有的节点依次出栈，由于栈先进后出的特性，节点顺序完全倒转
     * 如果此时还能与链表节点完全匹配，则链表是回文结构
     */
    public boolean isPalindrome(Node head) {
        Stack<Node> stack = new Stack<>();
        Node cur = head;
        while (cur != null) {
            stack.push(cur);
            cur = cur.next;
        }

        while (head != null) {
            if (head.value != stack.pop().value) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    /**
     * 普通方法2：仍然利用栈
     * 分析可知，回文结构是左右完全对称的，所以只用将一半压入栈，再与另一半的节点对比就能得出结果
     */
    public boolean isPalindrome2(Node head) {
        if (head == null) {
            return true;
        }
        // 找到中间节点
        Node cur = head;//每次移动1步
        Node twoStep = head;//每次移动2步
        while (twoStep != null && twoStep.next != null) {
            cur = cur.next;
            twoStep = twoStep.next.next;
        }

        // 把中间节点之后的节点入栈
        Stack<Node> stack = new Stack<>();
        while (cur != null) {
            stack.push(cur);
            cur = cur.next;
        }

        // 再循环判断
        while (!stack.empty()) {
            if (stack.pop().value != head.value) {
                return false;
            }
            head = head.next;
        }
        return true;
    }

    /**
     * 进阶方法：
     * 1.首先改变链表右半区的结构，令其反转，最后指向中间节点
     * 例如1->2->3->2->1，反转后变为1->2->3<-2<-1
     * 我们将左半区的第一个节点记为leftStart(即原来的head)，反转后右半区的第一个记为rightStart(即原来的tail)
     * 2.从leftStart和rightStart同时向中间移动，比较所有的节点是否相同，如果都相同则是回文结构
     * 3.不管结果如何，返回之前应把链表恢复成原来的样子
     */
    public boolean isPalindromeAdvance(Node head) {
        if (head == null || head.next == null) {
            return true;
        }

        // 找到中间节点(双数时为中间偏左节点)
        Node cur = head;//每次移动1步
        Node twoStep = head;//每次移动2步
        while (twoStep.next != null && twoStep.next.next != null) {
            cur = cur.next;
            twoStep = twoStep.next.next;
        }

        // 反转中间节点之后的链表
        Node pre = null;
        Node temp;
        while (cur != null) {
            temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }

        // 此时，head即为leftStart，pre即为rightStart
        // 开始对比左右半区数据
        Node rightStart = pre;
        boolean result = true;
        while (head != null && pre != null) {
            if (head.value != pre.value) {
                result = false;
                break;
            }
            head = head.next;
            pre = pre.next;
        }

        // 恢复链表
        while (rightStart != null) {
            temp = rightStart.next;
            rightStart.next = cur;
            cur = rightStart;
            rightStart = temp;
        }
        return result;
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        Node node1 = new Node(2);
        Node node2 = new Node(3);
        Node node3 = new Node(2);
        Node node4 = new Node(1);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;

        Node head1 = new Node(1);
        Node node11 = new Node(2);
        Node node12 = new Node(4);
        Node node13 = new Node(4);
        Node node14 = new Node(2);
        Node node15 = new Node(1);
        head1.next = node11;
        node11.next = node12;
        node12.next = node13;
        node13.next = node14;
        node14.next = node15;

        Palindrome palindrome = new Palindrome();
        System.out.println(palindrome.isPalindrome(head));
        System.out.println(palindrome.isPalindrome(head1));
        System.out.println(palindrome.isPalindrome2(head));
        System.out.println(palindrome.isPalindrome2(head1));
        System.out.println(palindrome.isPalindromeAdvance(head));
        System.out.println(palindrome.isPalindromeAdvance(head1));
        NodeUtil.printLinkNode(head);
        NodeUtil.printLinkNode(head1);
    }
}
