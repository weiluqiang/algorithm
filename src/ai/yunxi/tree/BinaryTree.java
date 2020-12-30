package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * 统计和生成所有不同的二叉树
 * 给定一个整数N，如果N<1，代表空树结构，否则代表中序遍历的结果为{1,2,3,...,N}，请返回可能的二叉树结构有多少
 * 例如，N=-1时代表空树结构，返回1；N=2时，满足中序遍历为{1,2}的二叉树结构只有两种：
 * -----1
 * --nil--2
 * 和
 * -----2
 * --1---nil
 * 所以返回结果为2
 * 进阶：N的含义不变，假设可能的二叉树结构有M种，请返回M个二叉树的头节点，每一棵二叉树代表一种可能结构
 */
public class BinaryTree {

    /**
     * 如果中序遍历有序且无重复值，则二叉树必为搜索二叉树
     * 假设num(a)代表a个节点的搜索二叉树有多少种可能，再假设序列为{1,...,i,...,N}
     * 如果以1作为头节点，1不可能有左子树，故以1为头节点有多少种可能结构，完全取决于1的右子树有多少种可能结构
     * 而1的右子树有N-1个节点，所以有num(N-1)种
     * 如果以i作为头节点，i的左子树有i-1个节点，可能的结构有num(i-1)种，右子树有N-i个节点，
     * 可能的结构有num(N-i)种，故以i为头节点的可能结构有num(i-1)*num(N-i)种
     * 如果以N作为头节点，N不可能有右子树，同理以N为头节点的结构可能有num(N-1)种
     * 把1到N分别作为头节点时，所有可能的结果加起来就是答案，可以利用动态规划来加速计算过程，从而达到O(N^2)的时间复杂度
     */
    public int treeNums(int n) {
        if (n < 2) return 1;
        // 节点数为i时，它对应的二叉树的可能数记录在num[i]中
        int[] num = new int[n + 1];
        num[0] = 1;
        // 循环求解节点数是1到n时，每种情况的可能数，求解完保存在num数组中
        for (int i = 1; i <= n; i++) {
            // 求解可能数，是1到i所有可能之和
            for (int j = 1; j <= i; j++) {
                num[i] += num[j - 1] * num[i - j];
            }
        }
        return num[n];
    }

    /**
     * 进阶问题与原问题的过程很类似。如果要生成中序遍历是{a,...,b}的所有结构，
     * 就从a开始到b，枚举每一个值作为头节点，把每次生成的二叉树的头节点保存下来即可。
     * 假设其中一次是以i为头节点的(a<=i<=b)，以i为头节点的所有结构按如下步骤生成：
     * 1.用{a,...,i-1}递归生成左子树的所有结构，假设所有结构的头节点保存在listLeft链表中
     * 2.用{i+1,...,b}递归生成右子树的所有结构，假设所有结构的头节点保存在listRight链表中
     * 3.在以i为头的前提下，listLeft中的每一种结构都可以与listRight中的每一种结构构成单独的结构，且和其他结构都不同。
     * 为了保证所有的结构之间不相互交叉，所以对每一种结构都复制出新的树，并记录在总的链表res中
     */
    public List<TreeNode> allTrees(int n) {
        return generate(1, n);
    }

    // 循环所有的值，递归生成每棵树
    private List<TreeNode> generate(int start, int end) {
        List<TreeNode> res = new LinkedList<>();
        if (start > end) {
            // 为了保证能进入listLeft和listRight的循环，必须添加一个null值
            res.add(null);
        }
        TreeNode head;
        for (int i = start; i <= end; i++) {
            head = new TreeNode(i);
            List<TreeNode> listLeft = generate(start, i - 1);
            List<TreeNode> listRight = generate(i + 1, end);
            for (TreeNode left : listLeft) {
                for (TreeNode right : listRight) {
                    head.left = left;
                    head.right = right;
                    // 不能直接放入链表，应该放copy之后的
                    res.add(cloneTree(head));
                }
            }
        }
        return res;
    }

    // 复制整棵树
    private TreeNode cloneTree(TreeNode head) {
        if (head == null) return null;
        TreeNode node = new TreeNode(head.value);
        node.left = cloneTree(head.left);
        node.right = cloneTree(head.right);
        return node;
    }

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();
        System.out.println(tree.treeNums(2));
        System.out.println(tree.treeNums(4));

        PrintTree print = new PrintTree();
        List<TreeNode> list = tree.allTrees(4);
        for (TreeNode h : list) {
            print.printTree(h);
        }
    }
}
