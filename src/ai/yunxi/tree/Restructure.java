package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 先序、中序、后序数组两两结合重构二叉树
 * 已知一个二叉树的所有节点值都不同，给定这棵二叉树正确的先序、中序、后序数组。
 * 请分别用三个函数实现任意两种数组结合重构原来的二叉树，并返回头节点
 */
public class Restructure {

    /**
     * 先序和中序结合重构二叉树：
     * 1.先序数组中的第一个值，就是就是树的头节点值，记为h。然后在中序数组中找到h，假设位置是i。
     * 那么i左边的数组就是头节点左子树的中序数组，假设长度为l，那么先序数组中h之后长度为l的数组，就是头节点左子树的先序数组
     * 比如：先序数组为[1,2,4,5,8,9,3,6,7]，中序数组为[4,2,8,5,9,1,6,3,7]，二叉树的头节点值是1
     * 在中序数组中找到1，它左边的数组是[4,2,8,5,9]，长度为5，那么左子树的先序数组就是[2,4,5,8,9]
     * 2.得到左子树的先序和中序数组后，递归整个过程构建左子树，返回的头节点记为left
     * 3.i右边的数组是头节点右子树的中序数组，假设长度为r，先序数组中左子树的先序数组后面，剩下的数组长度肯定也是r，就是右子树的先序数组
     * 接着上面的例子，中序数组中i右边的数组是[6,3,7]，得到头节点右子树的先序数组是[3,6,7]
     * 4.用右子树的先序和中序数组后，递归整个过程构建右子树，返回的头节点记为right
     * 5.建立头节点head，把head的左右孩子分别设为left、right，返回head过程结束
     * <p>
     * 如果二叉树的节点数为N，在中序数组中找到位置i的过程可以用哈希表来实现，这样总体的时间复杂度就是O(N)
     */
    public TreeNode preInToTree(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length) return null;
        return preIn(pre, 0, pre.length - 1, 0, getMap(in));
    }

    private Map<Integer, Integer> getMap(int[] in) {
        // 记录中序数组中，每个位置的值，对应的位置i
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < in.length; i++) {
            map.put(in[i], i);
        }
        return map;
    }

    // 递归构建整棵树的过程
    // p是先序数组，ps和pe是先序子数组的开始和结束，ns是中序子数组的开始
    private TreeNode preIn(int[] p, int ps, int pe, int ns, Map<Integer, Integer> map) {
        if (ps > pe) return null;
        // 头节点值在中序数组的位置
        int i = map.get(p[ps]);
        TreeNode head = new TreeNode(p[ps]);
        head.left = preIn(p, ps + 1, ps + i - ns, ns, map);
        head.right = preIn(p, ps + i - ns + 1, pe, i + 1, map);
        return head;
    }

    /**
     * 中序和后序结合重构二叉树的过程，和上面类似：
     * 上面的逻辑是通过先序数组的头节点值，来划分中序数组，找到左右子树的先序和中序数组再进行递归
     * 而后序遍历的时候，头节点值在后序数组的最后一个位置，仍然可以通过它来划分中序数组，找到左右子树的中序和后序数组再进行递归
     */
    public TreeNode inPostToTree(int[] in, int[] post) {
        if (in == null || post == null || in.length != post.length) return null;
        return inPost(0, post, 0, post.length - 1, getMap(in));
    }

    // ns是中序子数组的开始，s是后序数组，ss和se是后序子数组的开始和结束
    private TreeNode inPost(int ns, int[] s, int ss, int se, Map<Integer, Integer> map) {
        if (ss > se) return null;
        int i = map.get(s[se]);
        TreeNode head = new TreeNode(s[se]);
        head.left = inPost(ns, s, ss, ss + i - ns - 1, map);
        head.right = inPost(i + 1, s, ss + i - ns, se - 1, map);
        return head;
    }

    /**
     * 先序和后序结合重构二叉树：
     * 首先，就算是有正确的先序数组和后序数组，很多情况下也不能构建出原来的二叉树。
     * 最简单的比如：
     * 头节点是1、左孩子是2、右孩子是null，它的先序数组是[1,2]，后序数组是[2,1]
     * 头节点是1、左孩子是null、右孩子是2，它的先序数组也是[1,2]，后序数组也是[2,1]
     * 同一个先序、后序数组组合，可能对应不只一棵树，因此无法完成原树的构建
     * 如果一棵树上除了叶子节点，其他所有节点都有左孩子和右孩子，只有这样的树才能通过先序和后序数组重构出来
     * 重构的过程也是一个递归，最核心的逻辑在于，如何找到左右子树的先序和后序数组
     */
    public TreeNode prePostToTree(int[] pre, int[] post) {
        if (pre == null || post == null || pre.length != post.length) return null;
        return prePost(pre, 0, pre.length - 1, 0, getMap(post));
    }

    /**
     * 如果树的每个非叶子节点都有两个孩子，则对于当前的递归节点来说，要么左右孩子都为空，要么都不为空
     * 分析前序遍历的过程可知，如果当前节点左右都有孩子，那么当前数组值的下一个值，就是其左子树头节点的值，记为left
     * 再分析后序遍历的过程，它的顺序是左子树、右子树、根，并且子树的根节点，都是在整棵子树的最后一个遍历的
     * 同样在后序数组中找到left这个值，假设位置为i，那么i前面(包括i)的数组[0,i]，就是左子树的后序数组，
     * i+1到结尾倒数第二个位置的数组就是右子树的后序数组，那么再根据数组的长度，就可以知道左右子树的先序数组
     */
    private TreeNode prePost(int[] p, int ps, int pe, int ss, Map<Integer, Integer> map) {
        if (ps > pe) return null;
        TreeNode head = new TreeNode(p[ps]);
        // ps=pe时，子树只有一个节点，那么它就是叶子节点，可以直接返回
        if (ps != pe) {
            // 不是叶子节点的时候，p[ps+1]是子子树的头节点
            int i = map.get(p[ps + 1]);
            TreeNode left = prePost(p, ps + 1, ps + 1 + i - ss, ss, map);
            TreeNode right = prePost(p, ps + 1 + i - ss + 1, pe, i + 1, map);
            if ((left == null && right != null) || (left != null && right == null)) {
                throw new RuntimeException("不能重构原树");
            }
            head.left = left;
            head.right = right;
        }
        return head;
    }

    public static void main(String[] args) {
        Restructure restructure = new Restructure();
        PrintTree print = new PrintTree();
        int[] pre = {1, 2, 4, 5, 8, 9, 3, 6, 7};
        int[] in = {4, 2, 8, 5, 9, 1, 6, 3, 7};
        TreeNode h1 = restructure.preInToTree(pre, in);
        print.printTree(h1);
        int[] post = {4, 8, 9, 5, 2, 6, 7, 3, 1};
        TreeNode h2 = restructure.inPostToTree(in, post);
        print.printTree(h2);
        TreeNode h3 = restructure.prePostToTree(pre, post);
        print.printTree(h3);
    }
}
