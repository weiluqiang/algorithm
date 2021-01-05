package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;
import ai.yunxi.tree.pojo.TreeUtil;

/**
 * 统计完全二叉树的节点数
 * 给定一个完全二叉树的头节点head，返回这棵树的节点个数
 * 如果二叉树的节点个数为N，请实现时间复杂度低于O(N)的解法
 */
public class CBTNodeNum {

    /**
     * 遍历整棵树当然可以求出节点数，但这肯定不是最右解法
     * 如果二叉树的层数为h，下面的解法可以做到时间复杂度为O(h^2)，具体过程如下：
     * 1.如果head=null，说明是空树，直接返回0
     * 2.如果不是空树，就求出树的高度，求法是找到树的最左节点看能到多少层，记为h
     * 3.这一步采用了二分查找binary search的逻辑，记为bs(node,l,h)，node表示当前节点，l表示节点所在层数，h表示树高
     * bs(node,l,h)的返回值表示以node为头的完全二叉树的节点数是多少。初始时node为头节点head，l为1
     * 3-1.找到node右子树的最左节点，发现它能到最后一层，即l=h，说明node的整棵左子树都是满二叉树，并且层数为h-l层
     * 其节点个数为2^(h-l)-1，再加上node自己，那么节点数为2^(h-l)个，此时只需要知道node右子树的节点数
     * 那么node右子树的节点数怎么找呢？显然就是bs(node.right,l+1,h)的结果，递归即可得到
     * 整体返回值是2^(h-l)+bs(node.right,l+1,h)
     * 3-2.找到node右子树的最左节点，发现它没有到最后一层，说明node的整棵右子树都是满二叉树，并且层数为h-l-1
     * 其节点个数再加上node自己，就是2^(h-l-1)个，下面求node左子树的节点数，就是bs(node.left,l+1,h)
     * 最终整体返回的结果是2^(h-l-1)+bs(node.left,l+1,h)
     */
    public int getNum(TreeNode head) {
        if (head == null) return 0;
        return bs(head, 1, mostLeftLevel(head));
    }

    // 获取最左节点的层数
    private int mostLeftLevel(TreeNode node) {
        int level = 1;
        while (node.left != null) {
            level++;
            node = node.left;
        }
        return level;
    }

    // 递归求节点数
    private int bs(TreeNode node, int l, int h) {
        if (l == h) return 1;
        if (mostLeftLevel(node.right) == h - l) {
            return (1 << (h - l)) + bs(node.right, l + 1, h);
        } else {
            return (1 << (h - l - 1)) + bs(node.left, l + 1, h);
        }
    }

    public static void main(String[] args) {
        CBTNodeNum nodeNum = new CBTNodeNum();
        TreeNode head = TreeUtil.getDefault4();
        System.out.println(nodeNum.getNum(head));
    }
}
