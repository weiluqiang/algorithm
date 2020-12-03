package ai.yunxi.array;

/**
 * 未排序数组中累加和小于或等于给定值的最长子数组
 * 给定一个无序数组arr，其中元素可正、可负、可0，给定一个整数k，求arr的所有子数组中累加和小于或等于k的最长子数组长度
 * 例如：arr=[3,-2,-4,0,6]，k=-2，相加小于等于-2的最长子数组为[3,-2,-4,0]，所以返回4
 */
public class MaxLessArray {

    /**
     * 本解法可以做到时间复杂度O(N*logN)，额外的空间复杂度O(N)
     * 对于一个数组，如果从0到j的累加和为sum[0..j]，此时想求以j位置结尾的累加和小于等于k的最长子数组，
     * 那么只要知道大于等于sum[0..j]-k的累加和最早出现在j之前的什么位置就可以
     * 假设这个位置是i，那么arr[i+1..j]就是在j位置结尾的累加和小于等于k的最长子数组
     * <p>
     * 为了找到大于等于某个值的累加和最早出现的位置，可以按照如下方法生成辅助数组helpArr：
     * 1.首先生成arr每个位置从左到右的累加和数组sumArr，以[1,2,-1,5,-2]为例，生成的sumArr=[0,1,3,2,7,5]
     * 注意：要知道arr[i+1..j]的累加和，就必须找到sum[0..j]和sum[0..i]。同理，为了能有arr[0..j]的累加和的值，
     * 就必须有sum[0..j]和sum[-1]的值，因此必须定义sum[-1]=0，否则会丢失位置0开始的累加和的情况，
     * 因此sumArr的长度要比arr多1，并且sumArr[i]对应的是arr[0..i-1]的累加和
     * 2.根据sumArr生成左侧最大值数组helpArr=[0,1,3,3,7,7]，为什么2和5要变成3和7呢？
     * 因为我们只关心大于等于某个值的累加和最早出现的位置，而累加和3出现在2之前，并且大于等于3必然大于等于2，
     * 所以，当3不满足大于等于某个值，那么后面的2必然也不满足，可以直接跳过3后面，所有累加和小于等于3的数据的判断
     * 经过这样的操作变成helpArr之后，既不会影响总体的判断逻辑，还可以把数组变成一个有序的数组，就可以采用二分法查找位置了
     */
    public int maxLength(int[] arr, int k) {
        if (arr == null || arr.length == 0) return 0;
        int[] h = new int[arr.length + 1];//辅助数组helpArr
        h[0] = 0;//定义sum[-1]为0
        int sum = 0;
        // 生成辅助数组
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            h[i + 1] = Math.max(sum, h[i]);
        }
        // 遍历arr查找子数组
        sum = 0;
        int len = 0;
        int idx;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            idx = getLessIndex(h, sum - k);
            // idx=-1表示不存在
            if (idx != -1) {
                len = Math.max(i - idx + 1, len);
            }
        }
        return len;
    }

    // 采用二分法查找有序数组中，值大于等于num的最小索引
    private int getLessIndex(int[] arr, int num) {
        int low = 0;//查找左边界
        int high = arr.length - 1;//查找右边界
        int mid;//查找中间值
        int res = -1;//返回的最小索引结果
        while (low <= high) {
            mid = (high + low) / 2;
            if (arr[mid] >= num) {
                res = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        MaxLessArray array = new MaxLessArray();
        int[] arr = {0, 1, 3, 3, 7, 7, 7};
        System.out.println(array.getLessIndex(arr, 4));
        int[] arr1 = {3, -2, -4, 0, 6};
        System.out.println(array.maxLength(arr1, -2));
    }
}
