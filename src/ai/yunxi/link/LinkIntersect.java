package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 两个单链表相交问题
 * 给定两个单链表的头head1和head2，这两个链表可能有环也可能无环，可能相交也可能不相交。
 * 实现一个函数，如果两个链表相交则返回相交的第一个节点，如果不相交怎返回null
 * 要求：如果链表的长度分别为m和n，则时间复杂度为O(m+n)，额外的空间复杂度为O(1)
 * <p>
 * 本题可拆分成3个子问题：
 * 1.如何判断一个链表是否有环，如果有返回第一个进入环的节点，没有则返回null；
 * 2.如何判断两个链表是否相交，相交则返回第一个相交节点，不相交则返回null；
 * 3.如何判断两个有环链表是否相交，相交则返回第一个相交节点，不相交则返回null；
 * 注意：一个链表有环，另一个链表无环，则不可能相交，直接返回null。
 */
public class LinkIntersect {

    /**
     * 问题1：判断一个链表是否有环
     * 如果链表无环，则肯定有终点，如果有环则没有终点，判断过程如下：
     * 1.设置一个慢指针slow和一个快指针fast，都从链表头部head开始移动，
     * slow指针移动的步长为1，fast指针的步长为2；
     * 2.如果无环，则fast指针一定先到终点，直接返回null；
     * 3.如果有环，则两个指针一定会在环中的某个位置相遇，
     * 相遇时，slow指针不动，fast回到head节点，fast步长改为1，继续移动
     * 4.两个指针一定会再次相遇，并且相遇点就在第一个进入环的节点。
     * <p>
     * 证明：
     * 1.假设链表不在环中的节点长度为a，环中的节点长度为b
     * 对环中的每个节点位置进行标记，从第一个进入环的几点开始，依次记为0,1,2,...,b-1
     * 2.slow和fast指针同时开始移动，fast肯定先进入环中，
     * 继续移动，当slow第一次进入环中时，fast在环中的位置记为n，
     * 此时slow移动的步数是a，fast移动的步数是2a，fast多移动了a步，
     * 可知此时fast指针位置(n=a mod b)，则(a=b*x+n)，其中x为自然数，n为小于b的自然数
     * 3.此时slow在起始位置0，fast在位置n，此时可看成fast落后slow，落后(b-n)个位置，
     * fast追赶slow，当fast追上slow的时候，肯定多移动了(b-n)个位置，
     * fast每次比slow多移动1个位置，说明两个指针又都移动了(b-n)步，
     * slow从位置0开始，相遇时它的位置肯定就是(b-n)
     * 4.然后fast回到head节点，以步长1重新移动，它再次进入环时候，移动了a步，
     * 此时slow也移动了a步，它现在的位置是(b-n+a)=(b-n+b*x+n)=(1+x)*b，
     * 可以看出，slow也回到了初始位置0，正好和fast相遇
     */
    private Node getLoopFirstNode(Node head) {
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        Node slow = head.next;
        Node fast = head.next.next;
        //第一次移动
        while (slow != fast) {
            if (fast.next == null || fast.next.next == null) {
                return null;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        //fast回到head处，按步长1重新移动
        fast = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    /**
     * 问题2：判断两个无环链表是否相交
     * 两个无环链表相交，那么从相交节点开始，一直到两个链表的终点这一段，肯定都是共享节点
     * 1.遍历链表1，长度记为len1，终点记为end1；
     * 2.遍历链表12，长度记为len2，终点记为end2；
     * 3.如果end1!=end2，则说明链表不相交，返回null；
     * 4.如果end1==end2，则链表相交，此时再从头遍历两个链表，其中长度较长的链表先移动，
     * 先移动|end1-end2|个节点，较短的链表再跟着一起移动，则他们一定会同时到达第一个相交节点
     */
    private Node getNoLoopIntersectNode(Node head1, Node head2) {
        Node cur1 = head1;
        int n = 0;
        while (cur1.next != null) {
            cur1 = cur1.next;
            n++;
        }
        Node cur2 = head2;
        while (cur2.next != null) {
            cur2 = cur2.next;
            n--;
        }
        if (cur1 != cur2) {
            return null;
        }

        cur1 = head1;
        cur2 = head2;
        if (n > 0) {
            while (n != 0) {
                cur1 = cur1.next;
                n--;
            }
        } else if (n < 0) {
            while (n != 0) {
                cur2 = cur2.next;
                n++;
            }
        }

        while (cur1 != cur2) {
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        return cur1;
    }

    /**
     * 问题3：判断两个有环链表是否相交
     * 两个有环链表的第一个环节点分别为loop1，loop2
     * 1.如果loop1==loop2，那么环节点都是相交节点，这时寻找第一个相交节点的逻辑，
     * 和问题2中end1==end2的逻辑相同；
     * 2.如果loop1!=loop2，那么链表可能相交也可能不相交
     * a)如果相交，那么所有环节点依然都是相交节点，loop2肯定也在链表1的环上，
     * 此时继续遍历链表1，如果发现有节点等于loop2，则链表相交，
     * 那么loop1和loop2都是第一次相交的节点，返回其中一个即可；
     * b)如果继续遍历链表1，直到重新返回loop1节点，都没有节点等于loop2，
     * 那么链表不相交，则返回null
     */
    private Node getLoopIntersectNode(Node head1, Node loop1, Node head2, Node loop2) {
        if (loop1 == loop2) {
            //链表相交，寻找第一个相交节点
            Node cur1 = head1;
            Node cur2 = head2;
            int n = 0;
            while (cur1 != loop1) {
                cur1 = cur1.next;
                n++;
            }
            while (cur2 != loop2) {
                cur2 = cur2.next;
                n--;
            }

            cur1 = n > 0 ? head1 : head2;
            cur2 = cur1 == head1 ? head2 : head1;
            n = Math.abs(n);
            while (n != 0) {
                cur1 = cur1.next;
                n--;
            }
            while (cur1 != cur2) {
                cur1 = cur1.next;
                cur2 = cur2.next;
            }
            return cur1;
        } else {
            Node cur = loop1.next;
            while (cur != loop1) {
                if (cur == loop2) {
                    return loop1;
                }
                cur = cur.next;
            }
            return null;
        }
    }

    //判断链表是否相交
    private Node getIntersectNode(Node head1, Node head2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        Node loop1 = getLoopFirstNode(head1);
        Node loop2 = getLoopFirstNode(head2);
        if (loop1 == null && loop2 == null) {
            return getNoLoopIntersectNode(head1, head2);
        } else if (loop1 != null && loop2 != null) {
            return getLoopIntersectNode(head1, loop1, head2, loop2);
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        LinkIntersect intersect = new LinkIntersect();
        Node head1 = NodeUtil.createLinkNode(0, 5, 1);
        Node head2 = NodeUtil.createLinkNode(6, 10, 1);
        System.out.println(intersect.getIntersectNode(head1, head2));

        Node loop1 = NodeUtil.createLinkNode(100, 105, 1, true);
        System.out.println(intersect.getIntersectNode(head1, loop1));

        Node loop2 = NodeUtil.createLinkNode(106, 110, 1, true);
        System.out.println(intersect.getIntersectNode(loop1, loop2));

        Node node1 = new Node(0);
        Node node2 = new Node(1);
        Node node3 = new Node(2);
        Node node4 = new Node(3);
        Node node5 = new Node(4);
        Node node6 = new Node(5);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;

        Node node11 = new Node(100);
        Node node12 = new Node(101);
        Node node13 = new Node(102);
        Node node14 = new Node(103);
        Node node15 = new Node(104);
        Node node16 = new Node(105);
        node11.next = node12;
        node12.next = node13;
        node13.next = node14;
        node14.next = node15;
        node15.next = node16;

        Node node111 = new Node(1000);
        Node node112 = new Node(1001);
        Node node113 = new Node(1002);
        Node node114 = new Node(1003);
        Node node115 = new Node(1004);
        node6.next = node111;
        node16.next = node111;
        node111.next = node112;
        node112.next = node113;
        node113.next = node114;
        node114.next = node115;
        System.out.println(intersect.getIntersectNode(node1, node11).value);

        node115.next = node111;
        System.out.println(intersect.getIntersectNode(node1, node11).value);

        node3.next = node14;
        System.out.println(intersect.getIntersectNode(node1, node11).value);

        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        Node n6 = new Node(6);
        Node n7 = new Node(11);
        Node n8 = new Node(12);
        Node n9 = new Node(13);
        Node n10 = new Node(14);
        Node n11 = new Node(15);
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        n5.next = n6;
        n6.next = n7;
        n7.next = n8;
        n8.next = n9;
        n9.next = n10;
        n10.next = n11;
        n11.next = n7;

        Node nn1 = new Node(-1);
        Node nn2 = new Node(-2);
        Node nn3 = new Node(-3);
        Node nn4 = new Node(-4);
        nn1.next = nn2;
        nn2.next = nn3;
        nn3.next = nn4;
        nn4.next = n9;
        System.out.println(intersect.getIntersectNode(n1, nn1).value);
    }
}
