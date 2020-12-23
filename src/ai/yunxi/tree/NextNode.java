package ai.yunxi.tree;

import ai.yunxi.tree.pojo.FullNode;

import java.util.Stack;

/**
 * 在二叉树中找到一个节点的后继节点
 * 该树节点比普通二叉树节点多了指向父节点的parent指针，其中头节点的parent指向null。
 * 只给定一个在树中的某个节点node(不给头节点)，请实现返回node的后继节点的函数。
 * 在二叉树的中序遍历的序列中，node的下一个节点，叫做node的后继节点。
 * <p>
 * 例如图所示的二叉树：
 * ---------6
 * ----3---------9
 * --1---4-----8--10
 * ---2---5---7
 * 中序遍历的结果为：1,2,3,4,5,6,7,8,9,10
 * 所以节点1的后继节点为2，节点2的后继节点为3，···，节点10的后继节点为null
 */
public class NextNode {

    /**
     * 第一种解法的时间和空间复杂程度较高，但逻辑更容易理解。
     * 已知的节点node有parent指针，那么可以根据parent一直找到树的头节点head
     * 从头节点head开始，按中序遍历的顺序遍历整棵树，生成中序遍历的序列
     * 然后找到序列中该节点的位置返回下一个位置的节点即可
     */
    public FullNode findNext(FullNode node) {
        if (node == null) return null;
        // 寻找树的头节点
        FullNode head = node;
        while (head.parent != null) {
            head = head.parent;
        }
        // 进行中序遍历，找到node节点和后继节点
        boolean isFind = false;
        Stack<FullNode> stack = new Stack<>();
        while (!stack.isEmpty() || head != null) {
            if (head != null) {
                stack.push(head);
                head = head.left;
            } else {
                head = stack.pop();
                if (isFind) {
                    return head;
                } else if (head == node) {
                    isFind = true;
                }
                head = head.right;
            }
        }
        return null;
    }

    /**
     * 最优解法不必遍历所有的节点，如果node节点和后继节点之间的实际距离为L，最优解法只需走过L个节点，时间复杂度O(L)，额外的空间复杂度O(1)
     * 接下来详细说明最优解是如何找到node的后继节点的：
     * 1.如果node有右子树，那么它的后继节点就是右子树上最左边的节点
     * 因为是中序遍历，先遍历node的左子树，遍历完之后是node节点，然后是右子树，右子树上第一个要遍历的节点，就是右子树的最左节点
     * 2.如果node没有右子树，那么先看node是不是其父节点的左孩子，如果是，那么node的父节点就是它的后继节点；
     * 如果是右孩子，就向上寻找，假设向上移动到的节点为s，s的父节点为p，如果发现s是p的左孩子，那么p就是后继节点，否则一直向上移动
     * 没有右子树的时候，遍历完node节点则整棵子树都遍历完了，如果node刚好是父节点的左孩子，那么接下来就是遍历父节点，所以父节点就是后继节点
     * 如果node是父节点的右孩子，则父节点所在子树都遍历完毕，接下来看父节点，情况和node节点相同，父节点是祖父节点的左孩子，
     * 则接下来遍历祖父节点，祖父节点即是后继节点，是右孩子则祖父节点的子树遍历完毕，考察祖父节点的父节点......
     * 3.如果在情况2中一直向上寻找，直到空节点时仍然没有发现node的后继节点，说明node节点不存在后继节点
     * <p>
     * 情况1和情况2遍历的节点就是node到后继节点这条路径上的节点，情况3遍历的节点也不会超过树的高度
     */
    public FullNode getNextNode(FullNode node) {
        if (node == null) return null;
        if (node.right != null) {
            // 情况1，寻找右子树的最左节点
            return getLeftMost(node.right);
        } else {
            // 情况2和情况3，循环向上查找直到满足当前节点是父节点的左子节点
            FullNode parent = node.parent;
            while (parent != null && parent.left != node) {
                node = parent;
                parent = node.parent;
            }
            return parent;
        }
    }

    private FullNode getLeftMost(FullNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public static void main(String[] args) {
        FullNode n2 = new FullNode(2);
        FullNode n5 = new FullNode(5);
        FullNode n7 = new FullNode(7);
        FullNode n1 = new FullNode(1, null, n2);
        n2.parent = n1;
        FullNode n4 = new FullNode(4, null, n5);
        n5.parent = n4;
        FullNode n8 = new FullNode(8, n7, null);
        n7.parent = n8;
        FullNode n10 = new FullNode(10);
        FullNode n3 = new FullNode(3, n1, n4);
        n1.parent = n3;
        n4.parent = n3;
        FullNode n9 = new FullNode(9, n8, n10);
        n8.parent = n9;
        n10.parent = n9;
        FullNode head = new FullNode(6, n3, n9);
        n3.parent = head;
        n9.parent = head;

        NextNode next = new NextNode();
        System.out.println(next.findNext(head));
        System.out.println(next.findNext(n5));
        System.out.println(next.findNext(n10));
        System.out.println(next.getNextNode(head));
        System.out.println(next.getNextNode(n5));
        System.out.println(next.getNextNode(n10));
    }
}
