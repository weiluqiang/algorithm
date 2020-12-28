package ai.yunxi.tree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过先序和中序数组生成后序数组
 * 已知一个二叉树所有节点的值都不同，给定这棵树正确的先序和中序数组，不要重建整棵树，而是生成正确的后序数组
 */
public class PostArray {

    /**
     * 和Restructure中，通过先序和中序数组重构二叉树的逻辑一样，通过先序数组中头节点的值，
     * 定位到中序数组中头节点的位置，从而分别找到左右子树的先序和中序数组，然后递归遍历
     * 和重构二叉树不同，不需要生成节点，而是把节点值放入后序数组中正确的位置，而头节点在后序数组中就是数组最后的位置
     */
    public int[] getArray(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length) return null;
        int len = pre.length;
        int[] post = new int[len];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < in.length; i++) {
            map.put(in[i], i);
        }
        setPost(pre, 0, len - 1, 0, map, len - 1, post);
        return post;
    }

    // pi为后序数组中头节点的位置
    // 因为后序数组是按左、右、根的顺序递归出来的数组，根节点永远都在最后一个位置，
    // 所以我们生成数组时应该按照根、右、左的顺序放置，和后序顺序正好相反
    // 先放根节点，就是pi位置，然后把右子树的节点全部放进去，每棵右子树的根节点，都是pi-1的位置
    // 由于每放入一个节点，紧接着就会有一次pi-1，因此当右子树全部放入时，此时的pi就指向左子树的最后一个位置
    // 也就是左子树头节点的位置，直接传入pi递归左子树
    private int setPost(int[] p, int ps, int pe, int ns, Map<Integer, Integer> map, int pi, int[] post) {
        if (ps > pe) return pi;
        post[pi] = p[ps];
        int i = map.get(p[ps]);
        pi = setPost(p, ps + i - ns + 1, pe, i + 1, map, pi - 1, post);
        return setPost(p, ps + 1, ps + i - ns, ns, map, pi, post);
    }

    public static void main(String[] args) {
        PostArray array = new PostArray();
        int[] pre = {1, 2, 4, 5, 8, 9, 3, 6, 7};
        int[] in = {4, 2, 8, 5, 9, 1, 6, 3, 7};
        int[] post = array.getArray(pre, in);
        System.out.println(Arrays.toString(post));
    }
}
