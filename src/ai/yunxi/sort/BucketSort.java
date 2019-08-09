package ai.yunxi.sort;

import java.util.ArrayList;
import java.util.List;

// 桶排序
public class BucketSort {

    /**
     * 桶排序是计数排序的升级版。它利用了函数的映射关系，高效与否的关键就在于这个映射函数的确定。
     * 桶排序 (Bucket sort)的工作的原理：假设输入数据服从均匀分布，将数据分到有限数量的桶里，每个桶再分别排序
     * 有可能再使用别的排序算法或是以递归方式继续使用桶排序
     */
    public List<Integer> bucketSort(List<Integer> array, int bucketSize) {
        if (array.size() < 2 || bucketSize < 1) return array;
        // 查找数组的最大和最小值
        int max = array.get(0), min = array.get(0);
        for (int i = 1; i < array.size(); i++) {
            if (array.get(i) > max)
                max = array.get(i);
            if (array.get(i) < min)
                min = array.get(i);
        }
        // 构建bucket
        int bucketCount = (max - min) / bucketSize + 1;
        List<List<Integer>> bucketList = new ArrayList<>(bucketCount);
        List<Integer> resultList = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) {
            bucketList.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < array.size(); i++) {
            bucketList.get((array.get(i) - min) / bucketSize).add(array.get(i));
        }
        // 从bucket里循环取数
        for (int i = 0; i < bucketCount; i++) {
            if (bucketCount == 1) bucketSize--;
            // 对每个桶进行排序
            List<Integer> temp = bucketSort(bucketList.get(i), bucketSize);
            for (int j = 0; j < temp.size(); j++) {
                resultList.add(temp.get(j));
            }
        }
        return resultList;
    }
}
