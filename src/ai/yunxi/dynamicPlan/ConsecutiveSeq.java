package ai.yunxi.dynamicPlan;

import java.util.HashMap;
import java.util.Map;

/**
 * 数组中的最长连续序列
 * 给定无序数组arr，返回其中最长的连续序列的长度
 * 例如：arr=[100,4,200,1,3,2]，最长的连续序列为[1,2,3,4]，所以返回4
 */
public class ConsecutiveSeq {

    /**
     * 本题利用哈希表可以实现时间复杂度O(N)，额外空间复杂度O(N)的方法：
     * 1.先生成哈希表HashMap<Integer, Integer> map，key代表arr中遍历过的某个数，value代表这个数
     * 所在的最长连续序列的长度。同时map还可以表示arr中的一个数之前是否出现过。
     * 2.从左到右遍历arr，假设遍历到arr[i]。如果arr[i]之前出现过，直接遍历下一个数。如果没出现过，
     * 先在map中加入(arr[i],1)，然后看map中是否有arr[i]-1。如果有，则说明arr[i]-1所在的序列可以和arr[i]合并，
     * 合并之后记为A序列。利用map可以得到A序列的长度，记为lenA，最小值记为leftA，最大值记为rightA，
     * 只在map中更新与leftA、rightA有关的记录，更新成(leftA,lenA)和(rightA,lenA)。接下来看map中是否有arr[i]+1，
     * 如果有，则说明arr[i]+1可以和序列A合并，合并后记为序列B。B序列的长度为lenB，最小值为leftB，最大值为rightB，
     * 更新map中与leftB、rightB有关的记录，更新成(leftB,lenB)和(rightB,lenB)。
     * 3.遍历的过程中，用全局变量max记录每次合并出的序列的长度最大值，最后返回max。
     * <p>
     * 整个过程中，只有每个连续序列的最大值和最小值有意义。中间数的记录不会更新，因为再也不会用到。
     * 这是因为我们只处理以前没出现过的数，如果一个没出现过的数能够把某个连续区间扩大，
     * 或者把两个连续区间合并在一起，那么它只能是区间最小值-1或者最大值+1
     */
    public int longest(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        int max = 1;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i : arr) {
            if (!map.containsKey(i)) {
                map.put(i, 1);
                if (map.containsKey(i - 1)) {
                    int len = merge(map, i - 1, i);
                    if (len > max) {
                        max = len;
                    }
                }
                if (map.containsKey(i + 1)) {
                    int len = merge(map, i, i + 1);
                    if (len > max) {
                        max = len;
                    }
                }
            }
        }
        return max;
    }

    private int merge(Map<Integer, Integer> map, int less, int more) {
        int left = less - map.get(less) + 1;
        int right = more + map.get(more) - 1;
        int len = right - left + 1;
        map.put(left, len);
        map.put(right, len);
        return len;
    }

    public static void main(String[] args) {
        ConsecutiveSeq seq = new ConsecutiveSeq();
        int[] arr = {100, 4, 101, 99, 200, 102, 1, 98, 3, 2};
        System.out.println(seq.longest(arr));
    }
}
