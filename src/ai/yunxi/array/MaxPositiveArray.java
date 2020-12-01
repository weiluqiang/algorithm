package ai.yunxi.array;

/**
 * 未排序正数数组中累加和为给定值的最长子数组长度
 * 给定一个数组arr，该数组无序，但均为正数，再给定一个正数k，求arr的所有子数组中所有元素相加和为k的最长子数组长度
 * 例如：
 * arr=[1,2,1,1,1]，k=3。累加和为3的最长子数组为[1,1,1]，因此返回3
 */
public class MaxPositiveArray {

    /**
     * 最普通的逻辑，遍历全部子数组，求每个子数组的和是否满足条件，并记录满足条件的最长长度
     */
    public int maxLength(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k <= 0) return 0;
        int len = 0;
        // 从数组第一个元素开始依次遍历
        for (int i = 0; i < arr.length; i++) {
            // 以此元素作为开始的全部子数组，j表示子数组长度
            for (int j = 1; j <= arr.length - i; j++) {
                // 遍历从arr[i]开始，长度为j的子数组的所有元素，并求和判断
                int sum = 0;
                for (int m = 0; m < j; m++) {
                    sum += arr[i + m];
                }
                if (sum == k && j > len) {
                    len = j;
                }
            }
        }
        return len;
    }

    /**
     * 优化后的解法可做到时间复杂度O(N)，额外的空间复杂度为O(1)
     * 首先用两个位置来标记子数组的左右两头，记为left和right，开始都在数组的最左边
     * 1.开始时，left=0，right=0，代表子数组arr[left..right]
     * 2.变量sum表示数组arr[left..right]的和，开始时sum=arr[0]，即arr[0..0]的和
     * 3.变量len记录累加和为k的子数组中最大子数组的长度，开始时len=0
     * 4.根据sum和len的比较结果决定是left移动还是right移动：
     * 4-a.如果sum==k，判断arr[left..right]的长度大于len，则更新len，因为数组中全为正数，因此所有left开始到right之后的的子数组，
     * 即arr[left..i(i>right)]的累加和一定大于k，所以此时要left+1，sum-=arr[left]，开始关注left+1开始的子数组
     * 4-b.如果sum<k，说明arr[left..right]还需要加上right后面的值，累加和才可能达到k，所以此时right+1，sum+=arr[right]
     * 需要注意right+1之后要判断是否越界
     * 4-c.如果sum>k，则有arr[left..i(i>right)]之和也大于k，因此同4-a一样，令left+1，sum-=arr[left]
     * 5.如果right<arr.length，不断重复步骤4，否则结束，返回len
     */
    public int optimalMax(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k <= 0) return 0;
        int left = 0; //子数组的左边界
        int right = 0; //子数组的右边界
        int sum = arr[0];//当前子数组的累加和
        int len = 0;//累加和为k的最长子数组长度
        while (right < arr.length) {
            if (sum < k) {
                //步骤4-b
                right++;
                if (right == arr.length) {
                    break;
                }
                sum += arr[right];
            } else {
                //步骤4-a和4-c
                if (sum == k) {
                    len = Math.max(len, right - left + 1);
                }
                sum -= arr[left++];
            }
        }
        return len;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 1, 1, 1};
        MaxPositiveArray array = new MaxPositiveArray();
        System.out.println(array.maxLength(arr, 3));
        System.out.println(array.optimalMax(arr, 3));
    }
}
