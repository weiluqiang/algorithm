package ai.yunxi.sort;

import java.util.Arrays;

// 交换排序
public class SwapSort {

    /**
     * 冒泡排序
     * <p>
     * 在要排序的一组数中，对当前还未排好序的范围内的全部数，自上而下对相邻的两个数依次进行比较和调整，让较大的数往下沉，较小的往上冒。
     * 即：每当两相邻的数比较后发现它们的排序与排序要求相反时，就将它们互换。
     */
    public void bubble(int[] array) {
        for (int i = 0; i < array.length; i++) {
            // 每一轮冒泡，array最大值都会沉到最底下，所以每次循环都减i
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    /**
     * 快速排序
     * <p>
     * 选择一个基准元素,通常选择第一个元素或者最后一个元素
     * 通过一趟扫描，将待排序列分成两部分,一部分比基准元素小,一部分大于等于基准元素,
     * 此时基准元素在其排好序后的正确位置,然后再用同样的方法递归地排序划分的两部分。
     */
    public void quick(int[] array, int method) {
        if (array.length > 0) {
            if (method == 1) {
                quickSort(array, 0, array.length - 1);
            } else {
                quickSortRandom(array, 0, array.length - 1);
            }
        }
    }

    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int index = partitionFirst(array, low, high);
            if (index > low)
                quickSort(array, low, index - 1);
            if (index < high)
                quickSort(array, index + 1, high);
        }
    }

    private void quickSortRandom(int[] array, int low, int high) {
        if (low < high) {
            int index = partitionRandom(array, low, high);
            if (index > low)
                quickSortRandom(array, low, index - 1);
            if (index < high)
                quickSortRandom(array, index + 1, high);
        }
    }

    // 以随机元素为基准进行分区
    private int partitionRandom(int[] array, int low, int high) {
        int pivot = (int) (low + Math.random() * (high - low + 1));//选随机一个元素为基准
        int index = low - 1;
        swap(array, pivot, high);
        for (int i = low; i <= high; i++) {
            if (array[i] <= array[high]) {
                index++;
                if (i > index) swap(array, i, index);
            }
        }
        return index;
    }

    // 以第一个元素为基准进行分区，把小于基准值的交换到数组左端，大于基准值的交换到右端
    private int partitionFirst(int[] array, int low, int high) {
        int index = low;//记录小于基准元素的当前位置
        for (int i = low + 1; i <= high; i++) {
            if (array[i] <= array[low]) {
                index++;
                swap(array, i, index);
            }
        }
        // 最后交换index与pivot，把基准元素调整到中间
        swap(array, index, low);
        return index;
    }

    // 交换元素
    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) {
        SwapSort sort = new SwapSort();
        int[] arr = {15, 3, 8, 5, 98, 23, 88, 53, 1, 10, 7, 19};
        sort.bubble(arr);
        sort.quick(arr, 1);
        sort.quick(arr, 2);
        System.out.println(Arrays.toString(arr));
    }
}
