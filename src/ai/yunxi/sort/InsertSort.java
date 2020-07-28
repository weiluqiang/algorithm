package ai.yunxi.sort;

import java.util.Arrays;

// 插入排序
public class InsertSort {

    /**
     * 直接插入排序
     * <p>
     * 每步将一个待排序的记录，按其顺序码大小插入到前面已经排序的字序列的合适位置
     * 从后向前找到合适位置后，直到全部插入排序完为止。
     */
    public void directInsert(int[] array) {
        for (int i = 1; i < array.length; i++) {
            // 待插入元素
            int temp = array[i];
            int j;
            for (j = i - 1; j >= 0 && array[j] > temp; j--) {
                // 将大于temp的往后移动一位
                array[j + 1] = array[j];
            }
            array[j + 1] = temp;
        }
    }

    /**
     * 二分插入法排序
     * <p>
     * 二分法插入排序的思想和直接插入一样，只是找合适的插入位置的方式不同，这里是按二分法找到合适的位置，可以减少比较的次数。
     */
    public void dichotomy(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int temp = array[i];
            int left = 0;
            int right = i - 1;
            int mid;
            while (left <= right) {
                mid = (left + right) / 2;
                if (temp < array[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            if (i - left >= 0) {
                System.arraycopy(array, left, array, left + 1, i - left);
            }
            //for (int j = i - 1; j >= left; j--) {
            //    array[j + 1] = array[j];
            //}
            if (left != i) {
                array[left] = temp;
            }
        }
    }

    /**
     * 希尔排序
     * <p>
     * 先取一个小于n的整数d1作为第一个增量，把文件的全部记录分成d1个组。
     * 所有距离为d1的倍数的记录放在同一个组中。先在各组内进行直接插入排序；
     * 然后，取第二个增量d2<d1重复上述的分组和排序，直至所取的增量dt=1(dt<dt-l<…<d2<d1)，
     * 即所有记录放在同一组中进行直接插入排序为止。该方法实质上是一种分组插入方法。
     */
    public void shell(int[] array) {
        int temp, d = array.length / 2;
        while (d > 0) {
            for (int x = 0; x < d; x++) {
                for (int i = x; i < array.length; i = i + d) {
                    temp = array[i];
                    int j;
                    for (j = i - d; j >= 0 && array[j] > temp; j = j - d) {
                        array[j + d] = array[j];
                    }
                    array[j + d] = temp;
                }
            }
            d /= 2;
        }
    }

    public static void main(String[] args) {
        InsertSort sort = new InsertSort();
        int[] arr = {15, 3, 8, 5, 98, 23, 88, 53, 1, 10, 7, 19};
        sort.directInsert(arr);
        sort.dichotomy(arr);
        sort.shell(arr);
        System.out.println(Arrays.toString(arr));
    }
}
