package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

/**
 * 遍历二叉树的神级方法
 * 给定一个二叉树的头节点head，完成二叉树的先序、中序、后序遍历
 * 如果二叉树的节点数为N，要求时间复杂度为O(N),额外的空间复杂度为O(1)
 * <p>
 * 普通的递归方式使用函数栈，费递归方式使用申请的栈，额外的空间复杂度都与树高有关，是O(h)，
 * 想要达到O(1)的空间复杂度，就需要充分利用二叉树本身，存在的大量null指针，这就是大名鼎鼎的Morris遍历，由Joseph Morris在1979年发明
 * <p>
 * 普通的遍历方法都使用了栈结构，以便处理完某个节点之后可以返回到上层去。
 * 这主要是因为二叉树的结构，每个节点都有指向子节点的指针，却没有指向父节点的，所以从下层返回上层需要栈结构辅助
 * <p>
 * Morris遍历就是避免了栈结构，而是让底层节点的null指针指回上层的某个节点，从而完成下层到上层的移动
 * 我们知道，二叉树有大量的空闲指针，比如，某些节点没有右孩子，那么这个节点的right就指向null，我们称之为空闲状态，Morris遍历就是利用了这些空闲指针
 */
public class MorrisForeach {

    /**
     * Morris的中序遍历过程如下：
     * 1.假设当前子树的头节点为h，让h的左子树的最右节点的right指向h，然后h的左子树继续重复这个步骤，直到遇到某个节点没有左子树，记为node
     * 2.从node开始通过每个节点的right指针进行移动，并依次打印，记为cur，对每个cur节点都判断cur节点的左子树中最右节点的right是否指向cur
     * 2-a.如果是，让cur节点的左子树中最右节点的right指向null，然后打印cur，继续通过cur的right指针移动到下一节点，重复步骤2
     * 2-b.如果不是，以cur为头的子树重回步骤1执行
     * 3.不断重复上述步骤，直到right指针指向null结束
     * <p>
     * 思路分析：
     * 中序遍历的核心就是，对每一个节点，先遍历做左子树，然后根节点，最后遍历右子树
     * 可见根节点的遍历，是在整个左子树遍历完成之后的，而它的左子树最后一个遍历的节点，正是其最右节点，
     * 通过左子树的最右节点的right指向根节点，正好符合中序遍历的顺序
     */
    public void inOrder(TreeNode head) {
        if (head == null) return;
        // 当前处理节点，通过right指针移动
        TreeNode cur1 = head;
        // 当前节点的左子节点
        TreeNode cur2;
        while (cur1 != null) {
            cur2 = cur1.left;
            if (cur2 != null) {
                // 查找cur1节点左子树的最右节点
                while (cur2.right != null && cur2.right != cur1) {
                    cur2 = cur2.right;
                }
                if (cur2.right == null) {
                    // 找到最右节点，令right指向cur1
                    cur2.right = cur1;
                    // cur1的步骤1处理完毕，继续cur1的左子节点
                    cur1 = cur1.left;
                    continue;
                } else {
                    // 第2种情况cur2.right == cur1，执行步骤2-a
                    cur2.right = null;
                }
            }
            // 打印当前cur1，说明左子树、根节点都已遍历，接下来遍历right
            System.out.print(cur1.value + " ");
            cur1 = cur1.right;
        }
    }

    /**
     * Morris的先序遍历的思路和中序遍历差不多，顺序为根、左、右
     * 而在遍历完左子树的时候，仍然需要回到根节点，然后从根节点找到右子树继续遍历
     * 所以左子树的最右节点的right仍然可以指向根节点，这个逻辑和中序遍历一样
     * 而不同之处在于，根节点需要先打印，因此打印就不能放在第2步了，要在第1步直接打印
     */
    public void preOrder(TreeNode head) {
        if (head == null) return;
        // 当前处理节点，通过right指针移动
        TreeNode cur1 = head;
        // 当前节点的左子节点
        TreeNode cur2;
        while (cur1 != null) {
            cur2 = cur1.left;
            if (cur2 != null) {
                // 查找cur1节点左子树的最右节点
                while (cur2.right != null && cur2.right != cur1) {
                    cur2 = cur2.right;
                }
                if (cur2.right == null) {
                    // 找到最右节点，令right指向cur1
                    cur2.right = cur1;
                    // 有左子节点的直接在此打印
                    System.out.print(cur1.value + " ");
                    // cur1的步骤1处理完毕，继续cur1的左子节点
                    cur1 = cur1.left;
                    continue;
                } else {
                    // 第2种情况cur2.right == cur1，执行步骤2-a
                    cur2.right = null;
                }
            } else {
                // 没有左子节点的在此打印
                System.out.print(cur1.value + " ");
            }
            cur1 = cur1.right;
        }
    }

    /**
     * 后序遍历的逻辑也是基于中序遍历的
     * 先是左子树，然后左子树的最右节点的right指向根节点，通过根节点找到右子树，遍历右子树，最后是才是根
     * 因此需要对打印语句的位置进行调整，在步骤2-a进行打印，此时要打印当前节点的所有右边界节点，并且是从低到高打印
     * 此时当前节点记为cur1，它的最右子节点记为cur2，cur2的right指向cur1的父节点
     * 1.如果从cur1到cur2的所有右边界节点都没有左子树，那么由于打印节点是先右后根，显然从低到高依次打印即可
     * 2.如果其中有节点有左子节点，因为节点都是先向左子节点移动的，那么在这一步之前，节点肯定已经先向左移动，
     * 所有的左子节点都经历过左移到自己，然后经历若干步之后，又通过自己的最右子节点返回自己的父节点，
     * 此时按照上面的逻辑，必须打印自己的全部右边界，因此cur1到cur2所有右边界的左子节点都打印过了
     * 以此类推，cur1到cur2所有右边界的所有左子树都是打印了的，只剩这些右边界节点需要打印，它们打印完则cur1节点的子树全部打印完毕
     * <p>
     * 从整个打印过程来看，最先打印的是位于最左的叶子节点，然后是它的父节点的右边界节点
     * （当然打印这些右边界之前，右边界上有左子树的话也肯定先打印完了），再然后回到父节点，然后是父节点右边界，...
     * 所有节点的打印逻辑顺序，都是先左后右，最后是根，都满足后序遍历
     * <p>
     * 打印当前节点的右边界时，一般需要一个栈结构，把全部有边界入栈，然后出栈打印
     * 但是由于空间复杂度O(1)的要求，不能使用栈，因此考虑先把有边界逆序，然后打印，打印之后再逆序，恢复原来的顺序
     */
    public void postOrder(TreeNode head) {
        if (head == null) return;
        // 当前处理节点，通过right指针移动
        TreeNode cur1 = head;
        // 当前节点的左子节点
        TreeNode cur2;
        while (cur1 != null) {
            cur2 = cur1.left;
            if (cur2 != null) {
                // 查找cur1节点左子树的最右节点
                while (cur2.right != null && cur2.right != cur1) {
                    cur2 = cur2.right;
                }
                if (cur2.right == null) {
                    // 找到最右节点，令right指向cur1
                    cur2.right = cur1;
                    // cur1的步骤1处理完毕，继续cur1的左子节点
                    cur1 = cur1.left;
                    continue;
                } else {
                    // 第2种情况cur2.right == cur1，执行步骤2-a
                    cur2.right = null;
                    printEdge(cur1.left);
                }
            }
            cur1 = cur1.right;
        }
        // 最后打印整棵树的右边界
        printEdge(head);
    }

    // 逆序打印当前node的右边界
    private void printEdge(TreeNode node) {
        TreeNode tail = reverseEdge(node);
        TreeNode cur = tail;
        while (cur != null) {
            System.out.print(cur.value + " ");
            cur = cur.right;
        }
        reverseEdge(tail);
    }

    // 对整个右边界逆序
    private TreeNode reverseEdge(TreeNode node) {
        TreeNode pre = null;
        TreeNode next;
        while (node != null) {
            next = node.right;
            node.right = pre;
            pre = node;
            node = next;
        }
        return pre;
    }

    public static void main(String[] args) {
        TreeNode head = TreeNode.getDefault();
        MorrisForeach morris = new MorrisForeach();
        morris.inOrder(head);
        System.out.println();
        morris.preOrder(head);
        System.out.println();
        morris.postOrder(head);
    }
}
