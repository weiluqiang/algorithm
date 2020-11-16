package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

import java.util.Stack;

/**
 * 用递归、非递归的方式实现二叉树的先序、中序、后序遍历
 * 先序遍历的顺序为：根、左、右
 * 中序遍历的顺序为：左、根、右
 * 后序遍历的顺序为：左、右、根
 */
public class ForeachTree {

    // 递归方式进行先序遍历
    public void preOrder(TreeNode head) {
        if (head == null) return;
        System.out.print(head.value + " ");
        preOrder(head.left);
        preOrder(head.right);
    }

    /**
     * 非递归方式实现先序遍历
     * 对于任意的树，此过程都是先打印根节点，然后再看左右子树的情况，发现右子树需要遍历，就暂时存在stack中，
     * 然后看左子树，如也需要遍历，则直接开始遍历左子树，否则就遍历右子树。子树的遍历同理。
     * 此过程打印顺序，就是根节点、左子树、右子树，每个子树都遵循这个顺序，因此可实现先序遍历
     * <p>
     * 1.申请一个栈记为stack，然后将head节点压入stack中
     * 2.从stack中弹出栈顶节点，打印该节点的值，然后将它的右子节点、左子节点(不为空时)依次压入栈中
     * 3.不断重复步骤2，知道stack为空结束
     */
    public void preOrder1(TreeNode head) {
        if (head != null) {
            Stack<TreeNode> stack = new Stack<>();
            stack.push(head);
            while (!stack.isEmpty()) {
                head = stack.pop();
                System.out.print(head.value + " ");
                if (head.right != null) {
                    stack.push(head.right);
                }
                if (head.left != null) {
                    stack.push(head.left);
                }
            }
        }
    }

    // 递归方式进行中序遍历
    public void inOrder(TreeNode head) {
        if (head == null) return;
        inOrder(head.left);
        System.out.print(head.value + " ");
        inOrder(head.right);
    }

    /**
     * 非递归方式实现中序遍历
     * 中序遍历的思路是，先不断查找树的左子节点，直到没有左子节点，此时就可以打印当前节点了
     * 然后查看当前节点的右子节点，如果有右子节点，那么按照这个逻辑再遍历右子树
     * 这个打印过程，就是不停的找左子树，直到左子树为空，那么就打印根节点，然后打印右子树，
     * 右子树打印完毕后，现在stack保存的就是它的父节点，此时父节点的左子树也就打印完成了，
     * 然后打印父节点、父节点的右子树，整体打印顺序就是左、中、右的中序顺序
     * <p>
     * 1.申请一个栈记为stack，当前节点默认为head
     * 2.把当前节点压入栈中，令当前节点等于它的左子节点
     * 3.不断重复步骤2，把树的左边界依次压入栈中，直到当前节点为空
     * 4.此时从stack中弹出栈顶节点，可知此节点就是本次循环节点的父节点，本节点为空，可打印其父节点的值
     * 5.然后令当前节点等于父节点的右子节点，开始遍历右子树，遍历的方法同样
     */
    public void inOrder1(TreeNode head) {
        if (head != null) {
            Stack<TreeNode> stack = new Stack<>();
            while (!stack.isEmpty() || head != null) {
                if (head != null) {
                    stack.push(head);
                    head = head.left;
                } else {
                    head = stack.pop();
                    System.out.print(head.value + " ");
                    head = head.right;
                }
            }
        }
    }

    // 递归方式进行后序遍历
    public void posOrder(TreeNode head) {
        if (head == null) return;
        posOrder(head.left);
        posOrder(head.right);
        System.out.print(head.value + " ");
    }

    /**
     * 两个栈的方式实现后序遍历
     * 两个栈实现后序遍历的关键，就在于用一个栈记录节点顺序。
     * 对于一棵树，它的根节点肯定是最后一个打印的，所以先把根节点入栈，然后把右、左子树依次入栈，子树入栈的时候同样。
     * 当右子树完成入栈了的时候，此时我们需要找到右子树的左兄弟树，因此需要另一个栈结构
     * 新栈中节点入栈时，必须是先左子节点，然后是右子节点
     * 当新栈取出的是右子节点时，那么下一个取出的就是此节点的左兄弟节点
     * 而取出的是左子节点时，则表示该子树的左右子树都完成入栈了，那么
     *
     * 子树入栈的时候，同样也是先根节点入栈，然后右子树入栈，不断重复，直到右子树没有了
     * 此时可以把当前节点的左子树入栈，等左子树都入栈了，那么当前节点的父节点的右子树也都完成入栈
     * 找到它的父节点的左子节点，同样把左子树入栈
     * 这样整体入栈顺序按照根、右、左，遍历之后就变成了左、右、根的后序顺序
     * <p>
     * 1.申请两个栈记为stack1、stack2，然后将头结点压入stack1中
     * 2.从stack1中弹出一个节点，并压入stack2中，然后将该节点的左子节点和右子节点依次压入stack1中
     * 3.不断重复上述步骤，直到stack1为空
     * 4.从stack2中依次弹出节点并打印，打印顺序就是后序遍历
     */
    public void posOrder1(TreeNode head) {
        if (head != null) {
            Stack<TreeNode> stack1 = new Stack<>();
            Stack<TreeNode> stack2 = new Stack<>();
            stack1.push(head);
            while (!stack1.isEmpty()) {
                head = stack1.pop();
                stack2.push(head);
                if (head.left != null) {
                    stack1.push(head.left);
                }
                if (head.right != null) {
                    stack1.push(head.right);
                }
            }

            while (!stack2.isEmpty()) {
                head = stack2.pop();
                System.out.print(head.value + " ");
            }
        }
    }

    // 非递归方式实现后序遍历
    public void posOrder2(TreeNode head) {
        if (head != null) {
            Stack<TreeNode> stack = new Stack<>();
            stack.push(head);
            TreeNode c;
            // head为上次打印的节点
            while (!stack.isEmpty()) {
                c = stack.peek();
                if (c.left != null && head != c.left && head != c.right) {
                    stack.push(c.left);
                } else if (c.right != null && head != c.right) {
                    stack.push(c.right);
                } else {
                    System.out.print(c.value + " ");
                    stack.pop();
                    head = c;
                }
            }
        }
    }

    public static void main(String[] args) {
        TreeNode head = new TreeNode(1);
        TreeNode n1 = new TreeNode(2);
        TreeNode n2 = new TreeNode(3);
        TreeNode n3 = new TreeNode(4);
        TreeNode n4 = new TreeNode(5);
        TreeNode n5 = new TreeNode(6);
        TreeNode n6 = new TreeNode(7);
        TreeNode n7 = new TreeNode(8);
        TreeNode n8 = new TreeNode(9);
        head.left = n1;
        head.right = n2;
        n1.left = n3;
        n1.right = n4;
        n2.left = n5;
        n2.right = n6;
        n5.left = n7;
        n5.right = n8;

        ForeachTree foreach = new ForeachTree();
        foreach.preOrder(head);
        System.out.println();
        foreach.preOrder1(head);
        System.out.println();
        foreach.inOrder(head);
        System.out.println();
        foreach.inOrder1(head);
        System.out.println();
        foreach.posOrder(head);
        System.out.println();
        foreach.posOrder1(head);
        System.out.println();
        foreach.posOrder2(head);
    }
}
