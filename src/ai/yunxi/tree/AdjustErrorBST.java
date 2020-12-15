package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

import java.util.Arrays;
import java.util.Stack;

/**
 * 调整搜索二叉树中两个错误的节点
 * 一个二叉树原本是搜索二叉树，但是其中有两个节点调换了位置，使得它不再是搜索二叉树，请找到这两个错误节点并返回。
 * 已知二叉树中所有节点的值都不一样，给定二叉树的头节点head，返回一个长度为2的节点数组，分别记录两个错误节点
 * <p>
 * 进阶问题：找到了两个错误节点之后，我们当然可以通过交换两个节点值的方式，让二叉树重新成为搜索二叉树。
 * 但现在要求不能这么做，而是在结构上完全交换两个节点的位置，请实现调整函数。
 */
public class AdjustErrorBST {

    /**
     * 原问题：找到两个错误节点
     * 搜索二叉树的特点是，左边的值总是小于父节点，右边的值总是大于父节点
     * 如果对所有节点的值都不一样的搜索二叉树做中序遍历，那么节点值一定会升序排列
     * 所以如果有两个节点位置错了，就一定会出现降序
     * 1.如果在中序遍历时出现了两次降序，第一个错误的节点为第一次降序时较大的节点，第二个错误的节点为第二次降序时较小的节点
     * 以{1,2,3,4,5}为例，如果因为两个节点的位置错了出现了{1,5,3,4,2}的遍历结果，第一次降序为5->3，所以第一个错误节点为5，
     * 第二次降序为4->2，所以第二个错误节点为2
     * 2.如果在中序遍历时只出现了一次降序，第一个错误的节点就是这次降序中较大的节点，第二个错误的节点就是其中较小的节点
     * 比如{1,2,4,3,5}这种情况，只有一次降序4->3，所以第一个错误节点为4，第二个错误节点为3
     * 因此可以总结为：第一个错误节点为第一次降序时的较大节点，第二个错误节点为最后一次降序时的较小节点
     * 中序遍历采用递归、非递归、Morris遍历都可以
     */
    public TreeNode[] getErrorNodes(TreeNode head) {
        TreeNode[] err = new TreeNode[2];
        if (head != null) {
            // 采用栈+循环进行中序遍历
            Stack<TreeNode> stack = new Stack<>();
            TreeNode pre = null;
            while (!stack.isEmpty() || head != null) {
                if (head != null) {
                    stack.push(head);
                    head = head.left;
                } else {
                    head = stack.pop();
                    // 中序遍历打印节点值的位置，改成判断节点是否降序
                    if (pre != null && pre.value > head.value) {
                        // err[0]记录第一次降序的节点
                        if (err[0] == null) {
                            err[0] = pre;
                        }
                        // err[1]记录最后一次降序的节点
                        err[1] = head;
                    }
                    pre = head;
                    head = head.right;
                }
            }
        }
        return err;
    }

    /**
     * 进阶问题：在结构上交换两个错误节点。首先要找到错误节点的父节点，可以再遍历一次二叉树
     * 找到两个错误节点的父节点后，第一个错误节点记为e1，第二个错误节点记为e2
     * e1的父节点记为e1P，左孩子记为e1L，右孩子记为e1R。e2的父节点记为e2P，左孩子记为e2L，右孩子记为e2R
     * 整个交换的过程，就是互换两个节点的环境，把孩子节点作为对方的孩子，父节点作为对方的父节点
     * 但在实际交换时，有许多特殊情况需要考虑，比如如果e1是头节点，e1P就是null，操作时要考虑空指针的问题
     * 再比如，如果e1本身就是e2的做孩子，e1=e2L，那么让e2L成为e1的左孩子时，e1的指针会指向自己，造成树结果错误
     * 因此，我们必须理清两个节点，及其上下环境之间的关系，以及两个环境之间是否有联系
     * <p>
     * 我们需要关注以下三个问题和一个特别注意：
     * 1.e1和e2是否有一个是头节点？谁是头？
     * 2.e1和e2是否相邻？若相邻谁是父节点？
     * 3.e1和e2分别是各自父节点的左孩子还是右孩子？
     * 特别注意：因为是在中序遍历时先找到e1后找到e2，e1在e2之前，所以e1一定不是e2的右孩子，e2也一定不是e1的左孩子
     * <p>
     * 对上面的问题进行排列组合和整理，总共得到14种特殊情况需要特殊处理：
     * 1.e1是头，e1是e2的父，此时e2只能是e1的右孩子
     * 2.e1是头，e1不是e2的父，e2是e2P的左孩子
     * 3.e1是头，e1不是e2的父，e2是e2P的右孩子
     * 4.e2是头，e2是e1的父，此时e1只能是e2的左孩子
     * 5.e2是头，e2不是e1的父，e1是e1P的左孩子
     * 6.e2是头，e2不是e1的父，e1是e1P的右孩子
     * 7.e1和e2都不是头，e1是e2的父，此时e2只能是e1的右孩子，e1是e1P的左孩子
     * 8.e1和e2都不是头，e1是e2的父，此时e2只能是e1的右孩子，e1是e1P的右孩子
     * 9.e1和e2都不是头，e2是e1的父，此时e1只能是e2的左孩子，e2是e2P的左孩子
     * 10.e1和e2都不是头，e2是e1的父，此时e1只能是e2的左孩子，e2是e2P的右孩子
     * 11.e1和e2都不是头，两个节点也不相邻，e1是e1P的左孩子，e2是e2P的左孩子
     * 12.e1和e2都不是头，两个节点也不相邻，e1是e1P的左孩子，e2是e2P的右孩子
     * 13.e1和e2都不是头，两个节点也不相邻，e1是e1P的右孩子，e2是e2P的左孩子
     * 14.e1和e2都不是头，两个节点也不相邻，e1是e1P的右孩子，e2是e2P的右孩子
     * 当发生情况1、2、3时，二叉树新的头节点应该为e2，发生情况4、5、6时，新的头节点应该为e1，其他情况头节点不变
     */
    public TreeNode recoverTree(TreeNode head) {
        TreeNode[] err = getErrorNodes(head);
        if (err[0] == null || err[1] == null) return head;
        TreeNode[] parent = getErrorParents(head, err[0], err[1]);
        TreeNode e1 = err[0];
        TreeNode e1P = parent[0];
        TreeNode e1L = e1.left;
        TreeNode e1R = e1.right;
        TreeNode e2 = err[1];
        TreeNode e2P = parent[1];
        TreeNode e2L = e2.left;
        TreeNode e2R = e2.right;
        if (e1 == head) {
            if (e1 == e2P) {//情况1，由于e1R=e2，所以交换时e2.right=e1
                exchange(e1, e1L, e1, e2, e2L, e2R);
            } else {//情况2,3
                headExchange(e1, e1L, e1R, e2, e2L, e2R, e2P);
            }
            return e2;
        } else if (e2 == head) {
            if (e2 == e1P) {//情况4，由于e2L=e1，所以交换时e1.left=e2
                exchange(e1, e1L, e1R, e2, e2, e2R);
            } else {//情况5,6
                headExchange(e2, e2L, e2R, e1, e1L, e1R, e1P);
            }
            return e1;
        } else {
            if (e1 == e2P) {
                exchange(e1, e1L, e1, e2, e2L, e2R);
                if (e1 == e1P.left) {//情况7
                    e1P.left = e2;
                } else {//情况8
                    e1P.right = e2;
                }
            } else if (e2 == e1P) {
                exchange(e1, e1L, e1R, e2, e2, e2R);
                if (e2 == e2P.left) {//情况9
                    e2P.left = e1;
                } else {//情况10
                    e2P.right = e1;
                }
            } else {//情况11,12,13,14
                exchange(e1, e1L, e1R, e2, e2L, e2R);
                if (e1 == e1P.left) {
                    e1P.left = e2;
                } else {
                    e1P.right = e2;
                }
                if (e2 == e2P.left) {
                    e2P.left = e1;
                } else {
                    e2P.right = e1;
                }
            }
            return head;
        }
    }

    // 包含头节点，且节点不相邻时的交换，h表示头节点，n表示另一个非头节点
    private void headExchange(TreeNode h, TreeNode hL, TreeNode hR, TreeNode n, TreeNode nL, TreeNode nR, TreeNode nP) {
        exchange(h, hL, hR, n, nL, nR);
        // 由于h点没有父，所以不用判断h是左孩子还是右孩子
        if (n == nP.left) {
            nP.left = h;
        } else {
            nP.right = h;
        }
    }

    // 交换两个节点的子节点
    private void exchange(TreeNode e1, TreeNode e1L, TreeNode e1R, TreeNode e2, TreeNode e2L, TreeNode e2R) {
        e1.left = e2L;
        e1.right = e2R;
        e2.left = e1L;
        e2.right = e1R;
    }

    // 获取两个错误节点的父节点，可以通过遍历整棵树获得
    private TreeNode[] getErrorParents(TreeNode head, TreeNode err0, TreeNode err1) {
        TreeNode[] parent = new TreeNode[2];
        if (head != null) {
            // 采用栈+循环进行先序遍历，查找父节点
            Stack<TreeNode> stack = new Stack<>();
            stack.push(head);
            while (!stack.isEmpty()) {
                head = stack.pop();
                if (head.left == err0 || head.right == err0) {
                    parent[0] = head;
                }
                if (head.left == err1 || head.right == err1) {
                    parent[1] = head;
                }
                if (head.right != null) {
                    stack.push(head.right);
                }
                if (head.left != null) {
                    stack.push(head.left);
                }
            }
        }
        return parent;
    }

    public static void main(String[] args) {
        AdjustErrorBST error = new AdjustErrorBST();
        TreeNode n7 = new TreeNode(7);
        TreeNode n6 = new TreeNode(2);
        TreeNode n5 = new TreeNode(3);
        TreeNode n4 = new TreeNode(1);
        TreeNode n3 = new TreeNode(6, n6, n7);
        TreeNode n2 = new TreeNode(5, n4, n5);
        TreeNode head = new TreeNode(4, n2, n3);

        TreeNode[] err = error.getErrorNodes(head);
        System.out.println(Arrays.toString(err));
        TreeNode h2 = error.recoverTree(head);
        PrintTree pt = new PrintTree();
        pt.printTree(h2);
    }
}
