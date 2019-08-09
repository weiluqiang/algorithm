package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 删除链表的中间节点和a/b处的节点
 * <p>
 * 1.删除中间节点：总共N个节点
 * N=0或1，不删除节点
 * N=2，删除1节点
 * N=3或4，删除2节点
 * N=5或6，删除3节点
 * ...
 * <p>
 * 2.给定整数a和b，删除位于a/b处的节点：总共N个节点，a/b的值为r
 * r=0，不删除任何节点
 * r在(0,1/N]区间内，删除节点1
 * r在(1/N,2/N]区间内，删除节点2
 * r在(2/N,3/N]区间内，删除节点3
 * ...
 */
public class RemoveKNode {

    public static void main(String[] args) {
        RemoveKNode removeKNode = new RemoveKNode();
        Node node1 = NodeUtil.createLinkNode(1, 16, 1);
        Node res1 = removeKNode.removeMidNode(node1);
        NodeUtil.printLinkNode(res1);

        Node node2 = NodeUtil.createLinkNode(1, 7, 1);
        Node res2 = removeKNode.removeByRatio(node2, 6, 8);
        NodeUtil.printLinkNode(res2);
    }

    public Node removeMidNode(Node head) {
        // N=0或1，不删除节点
        if (head == null || head.next == null)
            return head;

        // N=2，删除1节点
        if (head.next.next == null)
            return head.next;

        // 以后链表长度每增加2，要删除的节点后移1位
        Node prev = head;
        Node cur = head.next.next;
        while (cur.next != null && cur.next.next != null) {
            prev = prev.next;//删除节点后移1位
            cur = cur.next.next;//当前节点后移2位
        }
        // prev节点后的节点即为中间节点
        prev.next = prev.next.next;
        return head;
    }

    public Node removeByRatio(Node head, int a, int b) {
        if (head == null || a < 1 || a > b)
            return head;

        Node cur = head;
        int n = 0;
        while (cur != null) {
            n++;
            cur = cur.next;
        }

        // 此时a*n再除以b再向上取证就是要删除的节点位置
        n = (int) Math.ceil((double) (a * n) / (double) b);
        if (n == 1)
            head = head.next;
        else if (n > 1) {
            cur = head;
            while (--n != 1)
                cur = cur.next;

            cur.next = cur.next.next;
        }
        return head;
    }
}
