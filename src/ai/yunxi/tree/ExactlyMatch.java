package ai.yunxi.tree;

import ai.yunxi.other.KMP;
import ai.yunxi.tree.pojo.TreeNode;
import ai.yunxi.tree.pojo.TreeUtil;

import javax.rmi.CORBA.Util;
import java.util.Stack;

/**
 * 判断t1树中是否有与t2树拓扑结构完全相同的子树
 * 给定彼此独立的两棵树头节点分别为t1和t2，判读t1树中是否有与t2树拓扑结构完全相同的子树
 * 如图所示的t1树：
 * ---------1
 * -----2-------3
 * --4-----5---6-7
 * ---8--9
 * t2树：
 * -----2
 * --4-----5
 * ---8--9
 * t1树中有与t2树拓扑结构完全相同的子树，所以返回true
 * 但如果t2树是下图这样的：
 * -----2
 * --4-----5
 * ---8
 * 则t1树就没有与t2树拓扑结构完全相同的子树，返回false
 */
public class ExactlyMatch {

    /**
     * 假设t1树的节点个数为N，t2树的节点个数为M
     * 对于t1的每棵子树，都去判断是否与t2树的拓扑结构完全一样，这个过程的时间复杂度为O(M)
     * t1的子树有N棵，所以总的时间复杂度为O(N*M)
     * 对t1树采取按层遍历
     */
    public boolean normalMatch(TreeNode t1, TreeNode t2) {
        if (t2 == null) {
            return true;
        }
        if (t1 == null) {
            return false;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.push(t1);
        while (!stack.isEmpty()) {
            t1 = stack.pop();
            if (isMatch(t1, t2)) {
                return true;
            }
            if (t1.right != null) {
                stack.push(t1.right);
            }
            if (t1.left != null) {
                stack.push(t1.left);
            }
        }
        return false;
    }

    // 判断两棵子树是否完全一样，
    // 对两棵子树都采取先序遍历的逻辑
    private boolean isMatch(TreeNode h1, TreeNode h2) {
        if (h1 == null && h2 == null) {
            return true;
        } else if (h1 == null || h2 == null) {
            return false;
        } else if (h1.value != h2.value) {
            return false;
        }

        Stack<TreeNode> s1 = new Stack<>();
        Stack<TreeNode> s2 = new Stack<>();
        s1.push(h1);
        s2.push(h2);
        while (!s1.isEmpty() && !s2.isEmpty()) {
            h1 = s1.pop();
            h2 = s2.pop();
            if ((h1.right == null && h2.right != null) || (h1.right != null && h2.right == null)) {
                return false;
            } else if (h1.right != null) {
                if (h1.right.value != h2.right.value) {
                    return false;
                } else {
                    s1.push(h1.right);
                    s2.push(h2.right);
                }
            }

            if ((h1.left == null && h2.left != null) || (h1.left != null && h2.left == null)) {
                return false;
            } else if (h1.left != null) {
                if (h1.left.value != h2.left.value) {
                    return false;
                } else {
                    s1.push(h1.left);
                    s2.push(h2.left);
                }
            }
        }

        // 其中一棵树遍历完毕，如果另一棵树还有节点则返回false，否则返回true
        return s1.isEmpty() && s2.isEmpty();
    }

    /**
     * 进阶的匹配方法，时间复杂度为O(N+M)
     * 首先是把t1树和t2树按先序遍历的方式序列化，以题目的例子来说，t1树的序列化结果为1!2!4!#!8!#!#!5!9!#!#!#!3!6!#!#!7!#!#!，
     * t2树的序列化结果为2!4!#!8!#!#!5!9!#!#!#!，第二棵t2的序列化结果为2!4!#!8!#!#!5!#!#!，
     * 那么判断的逻辑就转化成了字符串是否包含的问题
     * 关于字符串的包含问题，可以采用KMP算法，其时间复杂度为O(N+M)，当然还有更快速的BM算法和Sunday算法，都能在线性时间内匹配字符串
     */
    public boolean advanceMatch(TreeNode t1, TreeNode t2) {
        SerializeTree serialize = new SerializeTree();
        String s1 = serialize.serialize(t1);
        String s2 = serialize.serialize(t2);
        KMP kmp = new KMP();
        return kmp.kmpSearch(s1, s2) > -1;
    }

    public static void main(String[] args) {
        TreeNode t1 = TreeUtil.getDefault3();
        TreeNode o9 = new TreeNode(9);
        TreeNode o8 = new TreeNode(8);
        TreeNode o5 = new TreeNode(5, o9, null);
        TreeNode o4 = new TreeNode(4, null, o8);
        TreeNode t2 = new TreeNode(2, o4, o5);
        TreeNode d8 = new TreeNode(8);
        TreeNode d5 = new TreeNode(5);
        TreeNode d4 = new TreeNode(4, null, d8);
        TreeNode t3 = new TreeNode(2, d4, d5);

        ExactlyMatch match = new ExactlyMatch();
        System.out.println(match.normalMatch(t1, t2));
        System.out.println(match.normalMatch(t1, t3));
        System.out.println(match.advanceMatch(t1, t2));
        System.out.println(match.advanceMatch(t1, t3));
    }
}