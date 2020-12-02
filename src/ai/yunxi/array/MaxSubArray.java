package ai.yunxi.array;

import java.util.HashMap;
import java.util.Map;

/**
 * 未排序数组中累加和为给定值的最长子数组系列问题
 * 1.给定一个无序数组arr，其中元素可正、可负、可0，给定一个整数k，求arr的所有子数组中累加和为k的最长子数组长度
 * 2.给定一个无序数组arr，其中元素可正、可负、可0，求arr的所有子数组中正数与负数个数相等的最长子数组长度
 * 3.给定一个无序数组arr，其中元素只能是1或0，求arr的所有子数组中0和1个数相等的最长子数组长度
 * 求解的逻辑并不难，但是要求时间复杂度为O(N)，额外的空间复杂度为O(N)
 */
public class MaxSubArray {

    /**
     * 为了说明解法，定义一个概念s，s(i)表示子数组arr[0..i]的累加和，那么子数组arr[j..i](0<=j<=i<arr.length)的累加和为s(i)-s(j-1)
     * 具体的求解过程为：
     * 1.设置变量sum，记录从0到i的所有元素之和，设置变量len=0，表示累加和为k的最长子数组长度
     * 设置哈希表map，key表示从arr从最左边开始累加的过程中出现的sum值，value则表示sum值最早出现的位置
     * 2.从左到右开始遍历，当前元素为arr[i]
     * 2-1.令sum=sum+arr[i]，即之前所有元素的累加和s(i)，在map中查看是否存在sum-k
     * 2-1-a.如果存在，取出value值，记为j，j代表从左到右累加过程中第一次出现sum-k的位置
     * 此时s(i)=sum，s(j)=sum-k，因此arr[j+1..i]的累计和为sum-(sum-k)=k，并且j是累加和sum-k最早出现的位置，
     * 所以此时的arr[j+1..i]是以arr[i]结尾的所有子数组中，最长的累加和为k的那个子数组，如果它的长度大于len，则更新len
     * 2-1-b.如果不存在，则说明以arr[i]结尾的所有子数组中，没有累加和为k的子数组
     * 2-2.检查当前的sum是否在map中，若不存在，则说明此时的sum是第一次出现，就把(sum,i)加入到map中。
     * 若存在，则说明sum不是第一次出现，不用做任何操作
     * 3.继续遍历下一个元素，直到所有的元素遍历完
     * <p>
     * 要注意的是：
     * 根据概念s的定义，数组arr[j..i](0<=j<=i<arr.length)表示s(i)-s(j-1)的累加和，显然存在特例s(-1)，必须要有s(-1)的值，
     * 才能计算数组arr[0..i]的累加和，-1表示不在数组中，因此应该定义s(-1)=0
     */
    public int maxLength1(int[] arr, int k) {
        if (arr == null || arr.length == 0) return 0;
        Map<Integer, Integer> map = new HashMap<>();
        // 定义s(-1)=0
        map.put(0, -1);
        int len = 0;
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];//当前i的累加和s(i)
            if (map.containsKey(sum - k)) {
                // 2-1-a
                int j = map.get(sum - k);
                if (i - j > len) {
                    len = i - j;
                }
            }
            if (!map.containsKey(sum)) {
                // 2-2
                map.put(sum, i);
            }
        }
        return len;
    }

    /**
     * 问题2：把正数看做1，负数看做-1，然后就转化成了问题1中k=0的问题
     */
    public int maxLength2(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int len = 0, sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 0) {
                sum++;
            } else if (arr[i] < 0) {
                sum--;
            }
            if (map.containsKey(sum)) {
                int j = map.get(sum);
                if (i - j > len) {
                    len = i - j;
                }
            } else {
                map.put(sum, i);
            }
        }
        return len;
    }

    /**
     * 问题2的特殊解法：
     * 要达到正数和负数个数相等的最长子数组，那么这个子数组的正数个数和负数个数都要尽量的多，而且要保持相等，
     * 那么最多到什么程度呢？那必然是包含全部负数或全部正数的时候，根据两者个数的较小者确定
     * 此外还要尽量多的加入0，因为0的加入并不影响正负数个数相等，只会增加子数组长度
     */
    public int maxLength2_2(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        int positive = 0, negative = 0, zero = 0;
        for (int i : arr) {
            if (i > 0) {
                positive++;
            } else if (i < 0) {
                negative++;
            } else {
                zero++;
            }
        }
        return Math.min(positive, negative) * 2 + zero;
    }

    // 问题3，就是问题2的一种特列，正数为1，负数为0，且不存在0值的特列
    public int maxLength3(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int len = 0, sum = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1) {
                sum++;
            } else {
                sum--;
            }
            if (map.containsKey(sum)) {
                int j = map.get(sum);
                if (i - j > len) {
                    len = i - j;
                }
            } else {
                map.put(sum, i);
            }
        }
        return len;
    }

    // 显然问题3也可采用特殊解法
    public int maxLength3_2(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        int one = 0, zero = 0;
        for (int i : arr) {
            if (i == 1) {
                one++;
            } else {
                zero++;
            }
        }
        return Math.min(one, zero) * 2;
    }

    public static void main(String[] args) {
        MaxSubArray array = new MaxSubArray();
        int[] arr = {1, 2, 3, 3};
        System.out.println(array.maxLength1(arr, 6));
        int[] arr1 = {-1, -2, -3, 0, 3, 0, 1};
        System.out.println(array.maxLength2(arr1));
        System.out.println(array.maxLength2_2(arr1));
        int[] arr2 = {0, 0, 0, 1, 1, 1, 1, 1};
        System.out.println(array.maxLength3(arr2));
        System.out.println(array.maxLength3_2(arr2));
    }
}
