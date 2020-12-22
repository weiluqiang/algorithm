package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

/**
 * 根据后序数组重建搜索二叉树
 * 给定一个整型数组arr，已知其中没有重复值，判断arr是否可能是节点类型为整型的搜索二叉树后序遍历的结果
 * 进阶：如果整型数组arr中没有重复值，且是一棵搜索二叉树后序遍历的结果，通过数组arr重构二叉树
 */
public class PostToBST {

    /**
     * 原问题的解法：
     * 二叉树的后序遍历为先左、再右、最后根的顺序，所以如果一个数组是二叉树后序遍历的结果，
     * 那么头节点一定会是数组的最后一个元素。根据搜索二叉树的性质，所以比最后元素值小的元素会在数组左边，
     * 比它值大的元素会在右边。比如arr=[2,1,3,6,5,7,4]，比4小的部分为[2,1,3]，比4大的部分为[6,5,7]。
     * 如果不满足这种情况，说明这个数组一定不可能是搜索二叉树后序遍历的结果。
     * 接下来数组划分成左边数组和右边数组，相当于二叉树的左子树和右子树，只要递归进行上面的判断即可
     */
    public boolean isPostOrder(int[] arr) {
        if (arr == null || arr.length == 0) return false;
        return isPostArray(arr, 0, arr.length - 1);
    }

    // 递归判断数组是否符合：左边数组都比最后元素值小，右边数组都比最后元素值大
    // start和end记录需要判断的数组的开始和结束位置
    private boolean isPostArray(int[] arr, int start, int end) {
        // 判断到只有一个元素时，返回true
        if (start == end) return true;
        int[] lessMore = getLessAndMore(arr, start, end);
        // 表示数组中的所有元素都大于或小于arr[end]，也即整棵树只有左子树或右子树
        if (lessMore[0] == -1 || lessMore[1] == end) {
            return isPostArray(arr, start, end - 1);
        }
        // 如果不满足数组左半边都小于arr[end]，右半边都大于arr[end]
        if (lessMore[0] != lessMore[1] - 1) {
            return false;
        }
        return isPostArray(arr, start, lessMore[0]) && isPostArray(arr, lessMore[1], end - 1);
    }

    /**
     * 进阶问题的分析和原问题同理，一棵树的后序数组中，最后一个值为二叉树头节点的值，
     * 剩下的左半部分用来构成左子树，右半部分构成右子树
     */
    public TreeNode arrayToBST(int[] arr) {
        if (arr == null || arr.length == 0) return null;
        return toBST(arr, 0, arr.length - 1);
    }

    // 同isPostArray的逻辑稍有不同，start=end时表示只有一个节点，应该直接返回该节点，当start>end时才返回null，递归结束
    private TreeNode toBST(int[] arr, int start, int end) {
        if (start > end) return null;
        TreeNode head = new TreeNode(arr[end]);
        int[] lessMore = getLessAndMore(arr, start, end);
        // 不用判断less == -1 || more == end的情况，当处于这种情况时，仍然可以进入下一层递归，处理逻辑一样
        // 又已知arr是搜索二叉树的后序数组，也不会出现less != more - 1的情况
        head.left = toBST(arr, start, lessMore[0]);
        head.right = toBST(arr, lessMore[1], end - 1);
        return head;
    }

    // 获取数组左右部分的分界
    private int[] getLessAndMore(int[] arr, int start, int end) {
        // less记录从左到右，最后一个小于arr[end]的位置
        int less = -1;
        // more记录从左到右，第一个大于arr[end]的位置
        int more = end;
        // 遍历从start到end-1的数组
        for (int i = start; i < end; i++) {
            if (arr[i] < arr[end]) {
                less = i;
            } else {
                if (more == end) {
                    more = i;
                }
            }
        }
        return new int[]{less, more};
    }

    public static void main(String[] args) {
        PostToBST post = new PostToBST();
        int[] arr = {2, 1, 3, 6, 5, 7, 4};
        System.out.println(post.isPostOrder(arr));
        TreeNode head = post.arrayToBST(arr);
        PrintTree print = new PrintTree();
        print.printTree(head);
    }
}
