package ai.yunxi.dynamicPlan;

import java.util.Arrays;

/**
 * 最长递增子序列
 * 给定数组arr，返回arr的最长递增子序列
 * 例如：arr=[2,1,5,3,6,4,8,9,7]，返回最长的递增子序列为[1,3,4,8,9]
 * 如果arr的长度为N，请实现复杂度为O(N*logN)的方法
 */
public class IncreasingSeq {

    /**
     * 先介绍时间复杂度为O(N^2)的方法：
     * 1.生成长度为N的dp，dp[i]表示在以arr[i]为结尾的情况下，arr[0..i]中的最大递增子序列长度
     * 2.对于第一个数arr[0]来说，令dp[0]=1，接下来从左到右依次算出以每个位置的数结尾的情况下，最长递增子序列长度
     * 3.假设计算到位置i，如果最长递增子序列以arr[i]结尾，那么在arr[0..i-1]中，所有比arr[i]小的数，都可以作为倒数第二个数
     * 其中以哪个数结尾的最大递增子序列更大，就选它作为倒数第二个数，所以dp[i]=max{dp[j]+1}(0<=j<i,arr[j]<arr[i])
     * 如果arr[0..i-1]中所有的数都不比arr[i]小，那么dp[i]=1
     */
    private int[] getDP(int[] arr) {
        int[] dp = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {
                    dp[i] = Math.max(dp[j] + 1, dp[i]);
                }
            }
        }
        return dp;
    }

    /**
     * 接下来要根据求出的dp数组得到最长递增子序列，以题目的例子来说，求出的dp=[1,1,2,2,3,3,4,5,4]
     * 1.遍历dp数组，找到最大值及其位置。在本例中的最大值为5，位置是7
     * 2.从最大值位置7开始从右向左遍历，对于某一位置i，既有arr[i]<arr[7]，又有dp[i]=dp[7]-1
     * 说明arr[i]可以作为最长递增子序列的倒数第二个数，本例中arr[6]<arr[7]，且dp[6]=dp[7]-1，满足要求
     * 3.从arr数组的位置6开始继续向左遍历，按照同样的过程找倒数第三个数。本例中位置5就满足。
     * 可以看到位置4也满足，所以选arr[5]或arr[4]作为倒数第三个数都可以
     * 4.重复上面的过程，直到所有的数都找出来
     */
    public int[] maxSeq(int[] arr) {
        int[] dp = getDP(arr);
        return getMaxSeq(dp, arr);
    }

    private int[] getMaxSeq(int[] dp, int[] arr) {
        int max = 0;
        int idx = 0;
        // 先找dp数组的最大值
        for (int i = 0; i < dp.length; i++) {
            if (dp[i] > max) {
                max = dp[i];
                idx = i;
            }
        }

        int[] res = new int[max];
        res[--max] = arr[idx];
        // 从最大值位置开始向左遍历
        for (int i = idx - 1; i >= 0; i--) {
            if (arr[i] < arr[idx] && dp[i] == dp[idx] - 1) {
                res[--max] = arr[i];
                idx = i;
            }
        }
        return res;
    }

    /**
     * 普通的获取dp数组的方法，时间复杂度为O(N^2)，所以整个过程的时间复杂度是O(N^2)
     * 要想达到O(N*logN)的时间复杂度，需要优化获取dp数组的方法，可以通过二分查找来进行优化
     * 先生成一个长度为N的数组ends，令ends[0]=arr[0]，定义变量right，初始时right=0
     * 遍历的过程中，ends[0..right]为有效区，ends[right+1..N-1]为无效区
     * 对于有效区上的位置b，表示遍历到目前为止，在所有长度为b+1的递增序列中，最小的结尾数是ends[b]
     * <p>
     * 比如题目中的例子，初始时dp[0]=1，ends[0]=2，right=0，ends[0..0]为有效区
     * ends[0]=2的含义是，在遍历过arr[0]之后，所有长度为1的递增序列中，最小的结尾数是2
     * 1.遍历arr[1]=1，ends的有效区为ends[0..0]=[2]，在有效区中找到最左边的大于或等于arr[1]的数，
     * 发现是ends[0]，表示以arr[1]结尾的最长递增序列只有arr[1]，所以令dp[1]=1，然后令ends[0]=1，
     * 因为遍历到目前为止，在所有长度为1的递增序列中，最小的结尾数是1，而不再是2
     * 2.遍历arr[2]=5，ends的有效区为ends[0..0]=[1]，在有效区中找到最左边的大于或等于arr[2]的数，
     * 发现没有这样的数，表示以arr[2]结尾的最长递增序列长度=ends有效区长度+1，所以令dp[2]=2。
     * ends整个有效区都没有比arr[2]更大的数，说明发现了比ends有效区长度更长的递增序列，
     * 于是把有效区扩大，ends有效区为ends[0..1]=[1,5]
     * 3.遍历arr[3]=3，ends有效区为ends[0..1]=[1,5]，在有效区中采用二分法找到最左边的大于或等于arr[3]的数，
     * 发现是ends[1]，表示以arr[3]结尾的最长递增序列长度为2，所以令dp[3]-2，同步骤1令ends[1]=3
     * 4.遍历arr[4]=6，ends有效区为ends[0..1]=[1,3]，查找大于或等于arr[4]的数发现没有，因此令dp[4]=3，
     * 然后扩大有效区为ends[0..2]=[1,3,6]
     * 5.遍历arr[5]=4，nds有效区为ends[0..2]=[1,3,6]，查找大于或等于arr[5]的数发现是ends[2]，因此dp[5]=3，
     * 用4替换ends[2]，最小的结尾变为4
     * 6.遍历arr[6]=8，ends有效区为ends[0..2]=[1,3,4]，查找大于或等于arr[6]的数发现没有，因此令dp[6]=4，
     * 扩大有效区为ends[0..3]=[1,3,4,8]
     * 7.遍历arr[7]=9，ends有效区为ends[0..3]=[1,3,4,8]，查找大于或等于arr[7]的数发现没有，因此dp[7]=5，
     * 扩大有效区为ends[0..4]=[1,3,4,8,9]
     * 8.遍历arr[8]=7，ends有效区为ends[0..4]=[1,3,4,8,9],查找大于或等于arr[5]的数发现是ends[3]，因此dp[8]=4，
     * 然后修改ends[3]=7，表示在所有长度为4的递增序列中，最小的结尾数变为7
     */
    private int[] getDP1(int[] arr) {
        int[] dp = new int[arr.length];
        int[] ends = new int[arr.length];
        ends[0] = arr[0];
        dp[0] = 1;
        int right = 0;
        int l;
        int r;
        int m;
        for (int i = 1; i < arr.length; i++) {
            l = 0;
            r = right;
            // 通过二分法查找大于或等于当前值的数
            while (l <= r) {
                m = (l + r) / 2;
                if (ends[m] >= arr[i]) {
                    r = m - 1;
                } else {
                    l = m + 1;
                }
            }
            // 如果存在大于或等于当前值的数，那么二分查找的结果是：l就是最左的位置，且l<=right
            // 如果过不存在，则l会一直右移，直到l=right+1
            // 存在时，l<=right，它们的最大值就是right；不存在时，l=right+1，此时right应该+1，就是l
            right = Math.max(l, right);
            // 存在时，需更新此位置的值为arr[i]；不存在时，l处在无效区，赋值也不会有影响，并且以后有效区位置扩大时，也会重新赋值
            ends[l] = arr[i];
            // 存在时，dp[i]为0到此位置的长度，即是l+1；不存在时，dp[i]为扩大后的新的有效区的长度，新有效区最后的位置是l，因此也是l+1
            dp[i] = l + 1;
        }
        return dp;
    }

    public int[] maxSeq1(int[] arr) {
        int[] dp = getDP1(arr);
        return getMaxSeq(dp, arr);
    }

    public static void main(String[] args) {
        IncreasingSeq seq = new IncreasingSeq();
        int[] arr = {2, 1, 5, 3, 6, 4, 8, 9, 7};
        System.out.println(Arrays.toString(seq.maxSeq(arr)));
        System.out.println(Arrays.toString(seq.maxSeq1(arr)));
    }
}
