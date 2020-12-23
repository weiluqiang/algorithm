package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

/**
 * 通过有序数组生成平衡搜索二叉树
 * 给定一个有序数组sortArr，已知其中没有重复值，用这个有序数组生成一棵平衡搜索二叉树，并且该树中序遍历的结果与sortArr一致
 */
public class SortedToAVL {

    /**
     * 本题的思路比较简单，用有序数组最中间的数生成树的头节点，然后用它的左边生成左子树，右边生成右子树即可
     */
    public TreeNode arrayToAVL(int[] sortArr) {
        if (sortArr == null || sortArr.length == 0) return null;
        return toAVL(sortArr, 0, sortArr.length - 1);
    }

    // 递归生成AVL树
    private TreeNode toAVL(int[] arr, int start, int end) {
        if (start > end) return null;
        int mid = (start + end) / 2;
        TreeNode head = new TreeNode(arr[mid]);
        head.left = toAVL(arr, start, mid - 1);
        head.right = toAVL(arr, mid + 1, end);
        return head;
    }

    public static void main(String[] args) {
        SortedToAVL avl = new SortedToAVL();
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
        TreeNode h = avl.arrayToAVL(arr);
        PrintTree print = new PrintTree();
        print.printTree(h);
    }
}
