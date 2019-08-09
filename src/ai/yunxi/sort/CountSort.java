package ai.yunxi.sort;

// 计数排序
public class CountSort {

    /**
     * 计数排序的核心在于将输入的数据值转化为键存储在额外开辟的数组空间中。
     * 作为一种线性时间复杂度的排序，计数排序要求输入的数据必须是有确定范围的整数。
     * 计数排序使用一个额外的数组C，其中第i个元素是待排序数组A中值等于i的元素的个数。
     * 然后根据数组C来将A中的元素排到正确的位置。
     */
    public void countSort(int[] array) {
        // 查找数组的最大和最小值
        int max = array[0], min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max)
                max = array[i];
            if (array[i] < min)
                min = array[i];
        }
        // 构建数组bucket来存储array中值等于bucket坐标的元素个数
        int[] bucket = new int[max - min + 1];
        for (int i = 0; i < array.length; i++) {
            bucket[array[i] - min]++;
        }
        // 从bucket依次取数，取值为bucket坐标
        int n = 0;
        for (int i = 0; i < bucket.length; i++) {
            while (bucket[i] > 0) {
                array[n++] = i + min;
                bucket[i]--;
            }
        }
    }
}
