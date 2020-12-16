package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

/**
 * 判断t1树是否包含t2树的全部拓扑结构
 * 给定彼此独立的两棵树头节点分别为t1和t2，判读t1树是否包含t2树的全部拓扑结构
 * 如图所示的t1树：
 * ----------1
 * ------2-------3
 * ---4-----5---6-7
 * --8-9--10
 * t2树：
 * -----2
 * ---4---5
 * --8
 * t1树包含t2树的全部拓扑结构，返回true
 */
public class ContainTree {

    /**
     * 首先遍历t1树，寻找是否包含t2树的头节点，若不包含则直接返回false
     * 若包含，则分别按一定顺序遍历以t2为头的t1子树和整棵t2树，所有节点都包含则返回true
     * 其实要判断t1是否包含t2，需要判断t1上的所有子树，是否符合t2的拓扑结构
     * 这个符合包含两方面的意思：
     * 1.首先头节点值必须相等
     * 2.除了头节点，其他子节点的顺序和值也必须相等
     * 当然t1子树的节点可以多于t2树，但是不能少于，并且可能不只一颗子树满足这样的情况，找到第一个就可以返回true了
     * 当遍历完t1的所有子树都不符合，那么t1就是不包含t2
     */
    public boolean contains(TreeNode t1, TreeNode t2) {
        // 1.如果t1树和t2树的头节点值相等，那么会直接进入check方法的递归，直接判断出t1是否包含t2
        // 1-1.如果t1包含t2，那么递归最后会返回true，不需要接下来的判断，整个方法直接返回true
        // 1-2.如果不包含，那么递归会返回false，这样表明t1为头的子树，不符合t2的拓扑结构，
        // 那么保持t2节点不变，移动t1到它的左子节点和右子节点，分别判断左右子树是否符合，并不停递归
        // 直到找到符合的子树，则check返回true，递归停止
        // 若直到最后也没返回true，则整个方法返回false
        // 2.如果头节点不相等，那么check方法不会进入递归，会直接返回false
        // 此时同步骤1-2一样，都是表示当前子树不符合t2的拓扑结构
        // 应该保持t2的不变，递归其子节点，直到得到最终的结果
        // 注意：t1为空时会返回false，执行下一步判断子节点时会有空指针问题
        return check(t1, t2) || (t1 != null && (contains(t1.left, t2) || contains(t1.right, t2)));
    }

    // 先序遍历h树和t2树的各个节点
    // 判断两个节点值是否相等，不相等则直接返回false，不进入下面的递归
    // 如果两个节点值相等，那么开始按先序遍历的顺序，递归比较它们的子节点
    // 直到递归到叶子节点，都相等就返回true，有不相等的就直接返回false结束递归
    private boolean check(TreeNode h, TreeNode t2) {
        // t2为空表示t2树已经遍历到叶子节点，如果前面没有返回false，则此时应该返回true，
        // 说明这条遍历路径上的节点值都相等
        if (t2 == null) return true;
        // h为空，但是t2不为空，显然应该返回false
        // 两者的值不相等，也应返回false
        if (h == null || h.value != t2.value) return false;
        // 当前两个节点的值相等，则继续判断其左右子节点
        return check(h.left, t2.left) && check(h.left, t2.left);
    }

    public static void main(String[] args) {
        TreeNode n10 = new TreeNode(10);
        TreeNode n9 = new TreeNode(9);
        TreeNode n8 = new TreeNode(8);
        TreeNode n7 = new TreeNode(7);
        TreeNode n6 = new TreeNode(6);
        TreeNode n5 = new TreeNode(5, n10, null);
        TreeNode n4 = new TreeNode(4, n8, n9);
        TreeNode n3 = new TreeNode(3, n6, n7);
        TreeNode n2 = new TreeNode(2, n4, n5);
        TreeNode t1 = new TreeNode(1, n2, n3);

        TreeNode o8 = new TreeNode(8);
        TreeNode o5 = new TreeNode(5);
        TreeNode o4 = new TreeNode(4, o8, null);
        TreeNode t2 = new TreeNode(2, o4, o5);

        ContainTree tree = new ContainTree();
        System.out.println(tree.contains(t1, t2));
        System.out.println(tree.contains(t2, t1));
        System.out.println(tree.contains(t1, null));
        System.out.println(tree.contains(null, t2));
    }
}
