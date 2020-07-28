package ai.yunxi.sort;

import java.util.Arrays;

// 归并排序
public class MergeSort {

    /**
     * 归并排序
     * <p>
     * 归并（Merge）排序法是将两个（或两个以上）有序表合并成一个新的有序表。
     * 即把待排序序列分为若干个子序列，每个子序列是有序的。
     * 然后再把有序子序列合并为整体有序序列。
     */
    public int[] mergeSort(int[] array) {
        if (array.length < 2) return array;

        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(mergeSort(left), mergeSort(right));
    }

    // 将两组排序好的数组合并成一个
    private int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, l = 0, r = 0; index < result.length; index++) {
            if (l >= left.length) {
                result[index] = right[r++];
            } else if (r >= right.length) {
                result[index] = left[l++];
            } else if (left[l] > right[r]) {
                result[index] = right[r++];
            } else {
                result[index] = left[l++];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        MergeSort sort = new MergeSort();
        int[] arr = {15, 3, 8, 5, 98, 23, 88, 53, 1, 10, 7, 19};
        System.out.println(Arrays.toString(sort.mergeSort(arr)));
    }
}
