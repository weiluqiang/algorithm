package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 环形单链表的约瑟夫问题
 * <p>
 * 据说著名犹太历史学家 Josephus有过以下的故事：在罗马人占领乔塔帕特後，39 个犹太人与Josephus及他的朋友躲到一个洞中，
 * 39个犹太人决定宁愿死也不要被人抓到，于是决定了一个自杀方式，41个人排成一个圆圈，由第1个人开始报数，
 * 每报数到第3人该人就必须自杀，然后再由下一个重新报数，直到所有人都自杀身亡为止。
 * <p>
 * 输入：一个环形单链表的头节点head和报数m
 * 返回：最后生存下来的节点，此节点自己组成环形单向链表，其余节点都删掉
 * 进阶：如果链表节点数为N，要求时间复杂度为O(N)
 */
public class JosephusProblem {

    /**
     * 普通方法的时间复杂度为O(N*m)
     * <p>
     * 1.如果链接为空或者节点数为1，或者m<1，则直接返回
     * 2.循环遍历每个节点并计数，到第m个节点时，删除该节点
     * 3.把删除节点的前后节点链接，继续遍历，并从头开始计数
     * 4.直到只剩最后一个节点时结束
     */
    public Node JosephusKill(Node head, int m) {
        if (head == null || head.next == head || m <= 1) return head;

        int count = 0;
        Node now = head;//当前节点
        Node prev = head;//上一节点
        while (now != null) {
            if (++count == m) {
                System.out.println(now.value);
                prev.next = now.next;
                count = 0;
            } else {
                prev = now;
            }
            now = now.next;

            if (now.next == now) break;
        }
        return now;
    }

    /**
     * 普通方法需要删除n-1个节点，每次删除遍历m次，时间复杂度为O(m*n)
     * 进阶方法采用数学归纳法：
     * 假如链表为1->2->3->4->5，链表节点数n=5，m=3，最后可知节点4会存活
     * 1.首先，如果链表的节点数为n，我们做如下定义：从链表头节点开始编号，记为1，下一个节点为2，...，一直到n
     * 2.考虑如下问题：
     *   只有一个节点时，该节点在由自己组成的环中编号为1，记为Num(1)=1
     *   在由两个节点组成的环中，幸存节点的编号假设为Num(2)
     *   ......
     *   在由i-1个节点组成的环中，幸存节点的编号记为Num(i-1)
     *   在由i个节点组成的环中，幸存节点的编号记为Num(i)
     *   ......
     *   在由n个节点组成的环中，幸存节点的编号记为Num(n)
     * 3.已知Num(1)=1，找出Num(i-1)和Num(i)之间的关系
     *   1）假设现在共有i个节点
     */

    /**
     * 进阶方法
     *
     * @param head
     * @param m
     * @return
     */
    public Node JosephusKillAdvance(Node head, int m) {
        return null;
    }

    public static void main(String[] args) {
        JosephusProblem joseph = new JosephusProblem();
        Node head = NodeUtil.createLinkNode(1, 5, 1, true);
        NodeUtil.printLoopLinkNode(joseph.JosephusKill(head, 3));
    }
}
