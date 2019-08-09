package ai.yunxi.sort;

import java.util.ArrayList;
import java.util.List;

// 基数排序
public class RadixSort {

    /**
     * 将所有待比较数值（正整数）统一为同样的数位长度，数位较短的数前面补零。
     * 然后，从最低位开始，依次进行一次排序。
     * 这样从最低位排序一直到最高位排序完成以后,数列就变成一个有序序列。
     */
    public void radixSort(int[] array) {
        // 查找最大值
        int max = 0;
        for (int i = 0; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }
        // 判断最大位数
        int digit = 0;
        while (max > 0) {
            max = max / 10;
            digit++;
        }
        // 建立十个bucket
        List<List<Integer>> buckets = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            buckets.add(new ArrayList<>());
        }
        // 进行digit次分配和收集
        for (int i = 0; i < digit; i++) {
            // 将数据按每一位上的数值分配
            for (int j = 0; j < array.length; j++) {
                int index = array[j] % (int) Math.pow(10, i + 1) / (int) Math.pow(10, i);
                buckets.get(index).add(array[j]);
            }
            // 按顺序收集
            int count = 0;
            for (int j = 0; j < buckets.size(); j++) {
                List<Integer> bucket = buckets.get(j);
                for (int m = 0; m < bucket.size(); m++) {
                    array[count++] = bucket.get(m);
                }
                bucket.clear();
            }
        }
    }
}
