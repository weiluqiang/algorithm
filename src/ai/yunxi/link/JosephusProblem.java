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
     *
     * @param head
     * @param m
     * @return
     */
    public Node JosephusKill(Node head, int m) {
        if (head == null || head.next == head || m <= 1) return head;

        int count = 0;
        Node node = head;
        Node prev = head;
        while (node != null) {
            if (++count == m) {
                prev.next = node.next;
                count = 0;
            } else {
                prev = node;
            }
            node = node.next;

            if (node.next == node) break;
        }
        return node;
    }

    /**
     * 进阶方法不采用循环遍历删除，而是采用数学归纳法
     * 1.若环中只有1个节点，则幸存节点只能是它自己，记为Num(1)=1
     * 2.若环中有2个节点，则幸存节点与m有关，记为Num(2);
     * 3.若环中有n-1个节点，幸存节点为Num(n-1)，环中有n个节点，幸存节点为Num(n)
     * 4.分析Num(n-1)与Num(n)的关系：
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
        Node head = NodeUtil.createLinkNode(1, 13, 1, true);
        NodeUtil.printLoopLinkNode(joseph.JosephusKill(head, 4));
    }
}
