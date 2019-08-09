package ai.yunxi.stack;

import ai.yunxi.stack.pojo.MaxTreeNode;

import java.util.HashMap;
import java.util.Stack;

/*
 * 构造数组的MaxTree
 * <p>
 * 定义一个二叉树的节点MaxTreeNode，一个数组的MaxTree必须满足：
 * <p>
 * 1.数组必须没有重复元素
 * 2.MaxTree是一棵二叉树，数组的每一个值对应一个二叉树节点
 * 3.包括MaxTree在内的每一棵子树上，值最大的节点都是树的头
 * <p>
 * 可以用以下原则来建立这棵树：
 * 1.每一个数的父节点，是它左边第一个比它大的数和右边第一个比它大的数中，较小的那个
 * 2.如果一个数左边没有比它大的数，右边也没有，那么它是这个数组的最大值，也是MaxTree的头节点
 * <p>
 * 证明：
 * 1.通过这个方法肯定能生成一棵树而不是多棵，左右两边子树必将指向同一个父节点，也就是数组的最大值
 * 2.通过这个方法所有的节点最多只有两个孩子（可以假设一个节点有3个孩子，通过反证法证明）
 * <p>
 * 可以利用栈记录遍历过程中，左边的数值的一个大小顺序，以便比较左右两边数值的大小
 */
public class MaxTree {

    private MaxTreeNode getMaxTree(int[] arr) {
        //遍历数组，构造Node数组
        int length = arr.length;
        MaxTreeNode[] nArr = this.initNodeArray(arr, length);

        Stack<MaxTreeNode> stack = new Stack<>();
        HashMap<MaxTreeNode, MaxTreeNode> lmap = new HashMap<>();
        //第一次循环，找出一个数和左边第一个比它大的数的映射
        //只要curNode的value比栈顶的值大，会一直执行此方法，弹出比curNode小的值，直到小于栈顶的值，然后入栈
        //因此stack保存的值，其实是从栈底到栈顶，由大到小排列的，并且保持了数组里从前到后的顺序
        //lmap保存的就是一个数和左边第一个比它大的数的映射
        for (int i = 0; i < length; i++) {
            this.compareValue(nArr[i], stack, lmap);
        }

        //循环stack把剩下的值全部弹出
        while (!stack.isEmpty()) {
            popStackSetMap(stack, lmap);
        }

        HashMap<MaxTreeNode, MaxTreeNode> rmap = new HashMap<>();
        //第二次循环，找出一个数和右边第一个比它大的数的映射
        //同理，rmap保存的就是一个数和右边第一个比它大的数的映射
        for (int j = length - 1; j >= 0; j--) {
            this.compareValue(nArr[j], stack, rmap);
        }

        //循环stack把剩下的值全部弹出
        while (!stack.isEmpty()) {
            popStackSetMap(stack, rmap);
        }

        MaxTreeNode head = null;
        //根据lmap和rmap拼接整棵树
        for (int k = 0; k < length; k++) {
            MaxTreeNode curNode = nArr[k];
            MaxTreeNode left = lmap.get(curNode);
            MaxTreeNode right = rmap.get(curNode);
            if (left == null && right == null) {
                head = curNode;
            } else if (left == null) {
                if (right.left == null)
                    right.left = curNode;
                else
                    right.right = curNode;
            } else if (right == null) {
                if (left.left == null)
                    left.left = curNode;
                else
                    left.right = curNode;
            } else {
                //比较左右父节点的大小，取其中较小者为父节点
                MaxTreeNode parent = left.value < right.value ? left : right;
                if (parent.left == null)
                    parent.left = curNode;
                else
                    parent.right = curNode;
            }
        }
        return head;
    }

    private MaxTreeNode[] initNodeArray(int[] arr, int length) {
        MaxTreeNode[] nArr = new MaxTreeNode[length];
        for (int i = 0; i < length; i++) {
            nArr[i] = new MaxTreeNode(arr[i]);
        }
        return nArr;
    }

    //因为stack一开始为empty，所以必须放在&&左边
    private void compareValue(MaxTreeNode curNode, Stack<MaxTreeNode> stack, HashMap<MaxTreeNode, MaxTreeNode> map) {
        while (!stack.isEmpty() && stack.peek().value < curNode.value)
            popStackSetMap(stack, map);
        stack.push(curNode);
    }

    //map的key是当前取出的节点，value是stack里的下一个节点，若没有则为null
    private void popStackSetMap(Stack<MaxTreeNode> stack, HashMap<MaxTreeNode, MaxTreeNode> map) {
        MaxTreeNode popNode = stack.pop();
        if (stack.isEmpty()) {
            map.put(popNode, null);
        } else {
            map.put(popNode, stack.peek());
        }
    }

    //自己写的方法，逻辑大致相同，不会专门记录lmap和rmap，在弹出时直接找出父节点
    private MaxTreeNode getMyMaxTree(int[] arr) {
        //遍历数组，构造Node数组
        int length = arr.length;
        MaxTreeNode[] nArr = this.initNodeArray(arr, length);

        Stack<MaxTreeNode> stack = new Stack<>();
        for (int i = 0; i < length; i++) {
            this.compareMyValue(nArr[i], stack);
        }

        //循环stack把剩下的值全部弹出
        //stack弹出的最后一个元素就是head节点
        MaxTreeNode head = null;
        while (!stack.isEmpty()) {
            if (stack.size() == 1)
                head = stack.peek();
            popMyNode(null, stack);
        }
        return head;
    }

    //栈顶的值总是最小的，且小于当前要比较的值时弹出
    private void compareMyValue(MaxTreeNode curNode, Stack<MaxTreeNode> stack) {
        while (!stack.isEmpty() && curNode.value > stack.peek().value)
            this.popMyNode(curNode, stack);
        stack.push(curNode);
    }

    //弹出时，popNode右边第一个比它大的就是curNode，左边第一个比它大的就是栈中的下一个Node
    private void popMyNode(MaxTreeNode right, Stack<MaxTreeNode> stack) {
        MaxTreeNode popNode = stack.pop();
        MaxTreeNode left = stack.isEmpty() ? null : stack.peek();
        //right和left都为空时即为数组的最大值，tree的head节点
        if (right != null && left != null) {
            //比较左右两个值的大小，取其中较小者为父节点
            MaxTreeNode parent = left.value < right.value ? left : right;
            if (parent.left == null)
                parent.left = popNode;
            else
                parent.right = popNode;
        } else if (right != null) {
            if (right.left == null)
                right.left = popNode;
            else
                right.right = popNode;
        } else if (left != null) {
            if (left.left == null)
                left.left = popNode;
            else
                left.right = popNode;
        }
    }

    public static void main(String[] args) {
        MaxTree maxTree = new MaxTree();
        int[] arr = {3, 4, 5, 10, 1, 2, 13, 8, 6, 12, 11, 7, 9, 14};
        MaxTreeNode head1 = maxTree.getMaxTree(arr);
        print_node(head1, 0);

        System.out.println("------------------------------");
        MaxTreeNode head2 = maxTree.getMyMaxTree(arr);
        print_node(head2, 0);
    }

    private static void print_node(MaxTreeNode node, int level) {
        if (node == null) {
            padding(level);
            System.out.println("NIL");
        } else {
            print_node(node.right, level + 1);
            padding(level);
            System.out.printf("%d\n", node.value);

            print_node(node.left, level + 1);
        }
    }

    private static void padding(int n) {
        int i;
        for (i = 0; i < n; i++)
            System.out.println("\t");
    }
}
