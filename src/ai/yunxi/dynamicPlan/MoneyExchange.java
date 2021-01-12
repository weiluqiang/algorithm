package ai.yunxi.dynamicPlan;

/**
 * 换钱的最少货币数
 * 给定数组arr，arr中所有的值都为正数且不重复。每个值代表一种面值的货币，
 * 每种货币可以使用任意张，再给定一个正数aim代表要找的钱数，求组成aim的最少货币数
 * 例如，arr=[5,2,3]，aim=20
 * 4张5元的可以组成20元，其他的找钱方案都要使用更多张的货币，因此返回4
 * arr=[5,2,3]，aim=0
 * 不用任何货币就可以组成0元，因此返回0
 * arr=[5,3]，aim=2
 * 根本无法组成2，不能找钱的情况下返回-1
 * <p>
 * 补充题目：给定数组arr，arr中所有的值都为正数。每个值仅代表一张钱的面值，
 * 再给定一个正数aim代表要找的钱数，求组成aim的最少货币数
 * 例如，arr=[5,2,3]，aim=20
 * 三种面值的货币各有一张，一共也不到20元，所以返回-1
 * arr=[5,2,5,3]，aim=10
 * 5元的货币有2张，可以组成10元，且是张数最少的方案，因此返回2
 * arr=[5,2,5,3]，aim=15
 * 所有钱加起来才能组成15元，因此返回4
 * arr=[5,2,5,3]，aim=0
 * 不用任何货币就可以组成0元，因此返回0
 */
public class MoneyExchange {

    /**
     * 如果arr的长度为N，生成行数为N，列数为aim+1的动态规划表dp
     * dp[i][j]的含义是，在可以任意使用arr[0..i]货币的情况下，组成j所需要的最小张数
     * 根据这个定义，dp[i][j]的值按如下方式计算：
     * 1.dp[0..N-1][0]的值表示找钱为0时所需要的最少货币数，默认为0即可
     * 2.dp[0][0..aim]的值表示只能用arr[0]货币的情况下，找某个钱数的最少货币数，比如arr[0]=2，那么能找的钱数为2,4,6,8..
     * 所以令dp[0][2]=1，dp[0][4]=2，dp[0][6]=3... 其他位置的钱数找不开，都设为最大的int值
     * 3.剩下的位置依次从左到右，再从上到下计算。假设计算到位置(i,j)，dp[i][j]的值可能来自下面的情况：
     * 3-1.完全不使用当前货币arr[i]情况下的最少货币数，即dp[i-1][j]的值
     * 3-2.只使用1张当前货币arr[i]情况下的最少货币数，即dp[i-1][j-arr[i]]+1
     * 3-3.只使用2张当前货币arr[i]情况下的最少货币数，即dp[i-1][j-2*arr[i]]+2
     * 3-4.只使用3张当前货币arr[i]情况下的最少货币数，即dp[i-1][j-3*arr[i]]+3
     * ......
     * 所有情况中，取张数最小的，所以：
     * dp[i][j] = min{dp[i-1][j-k*arr[i]]+k} (k>=0)
     * 即是：dp[i][j] = min{dp[i-1][j], min{dp[i-1][j-x*arr[i]]+x}} (x>=1)
     * 令x=y+1，有：dp[i][j] = min{dp[i-1][j], min{dp[i-1][j-arr[i]-y*arr[i]]+y+1}} (y>=0)
     * 根据第一个等式又有：dp[i][j-arr[i]] = min{dp[i-1][j-arr[i]-y*arr[i]]+y} (y>=0)
     * 因此最终得出：dp[i][j] = min{dp[i-1][j], dp[i][j-arr[i]]+1}
     * 如果j-arr[i]<0，即发生越界了，说明arr[i]太大，用1张都会超过钱数j，令dp[i][j]=dp[i-1][j]即可
     * 整个过程的时间复杂度和额外空间复杂度都是O(N*aim)，N为数组arr的长度
     */
    public int minNumber(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) return -1;
        int n = arr.length;
        int[][] dp = new int[n][aim + 1];
        // dp第一列的值默认是0，不用再赋值
        // 求dp第一行的值，即步骤2的过程
        for (int j = 1; j <= aim; j++) {
            if (j - arr[0] >= 0 && dp[0][j - arr[0]] != Integer.MAX_VALUE) {
                dp[0][j] = dp[0][j - arr[0]] + 1;
            } else {
                dp[0][j] = Integer.MAX_VALUE;
            }
        }
        // 求dp其他行列的值，即步骤3的过程
        int left;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= aim; j++) {
                if (j - arr[i] >= 0 && dp[i][j - arr[i]] != Integer.MAX_VALUE) {
                    left = dp[i][j - arr[i]] + 1;
                } else {
                    left = Integer.MAX_VALUE;
                }
                dp[i][j] = Math.min(left, dp[i - 1][j]);
            }
        }
        // 返回位置(n-1,aim)处的结果
        return dp[n - 1][aim] == Integer.MAX_VALUE ? -1 : dp[n - 1][aim];
    }

    /**
     * 原问题可以在动态规划的基础上采用空间压缩的方法。我们选择生成一个长度为aim+1的一维数组dp，然后按行更新dp
     * 值所以之所以不选按列更新，是因为根据dp[i][j] = min{dp[i-1][j], dp[i][j-arr[i]]+1}可以，位置(i,j)依赖位置(i-1,j)
     * 也依赖位置(i,j-arr[i])，所以按行更新只需要一个一维数组，按列更新需要的一维数组的个数，与arr中货币的最大值有关
     * 如果最大的货币为a，说明最差情况要向左侧移动a下，相应的就要准备a个一维数组不断的滚动复用，这样的实现就会很麻烦
     * 空间压缩后的时间复杂度为O(N*aim)，额外的空间复杂度为O(aim)
     */
    public int minNumber1(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) return -1;
        int n = arr.length;
        int[] dp = new int[aim + 1];
        for (int j = 1; j <= aim; j++) {
            if (j - arr[0] >= 0 && dp[j - arr[0]] != Integer.MAX_VALUE) {
                dp[j] = dp[j - arr[0]] + 1;
            } else {
                dp[j] = Integer.MAX_VALUE;
            }
        }
        setArray(dp, arr, n, aim);
        return dp[aim] == Integer.MAX_VALUE ? -1 : dp[aim];
    }

    private void setArray(int[] dp, int[] arr, int n, int aim) {
        int left;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= aim; j++) {
                if (j - arr[i] >= 0 && dp[j - arr[i]] != Integer.MAX_VALUE) {
                    left = dp[j - arr[i]] + 1;
                } else {
                    left = Integer.MAX_VALUE;
                }
                dp[j] = Math.min(left, dp[j]);
            }
        }
    }

    /**
     * 补充问题的动态规划解法，跟原问题的类似。如果arr的长度为N，生成行数为N、列数为aim+1的动态规划表dp
     * dp[i][j]的含义是，在可以任意使用arr[0..i]货币的情况下，每个货币只能使用一次，组成j所需的最小张数，同理：
     * 1.dp[0..N-1][0]的值表示找钱为0时所需要的最少货币数，默认为0即可
     * 2.dp[0][0..aim]的值表示只能用arr[0]货币的情况下，找某个钱数的最少货币数，比如arr[0]=2，那么能找开的钱数就是2
     * 所以令dp[0][2]=1。因为只有一张钱，所以其他位置代表的钱数都找不开，因此都设成int最大值
     * 3.剩下的位置依次从左到右，再从上到下计算。对于位置(i,j)，dp[i][j]的值可能来自下面两种情况：
     * 3-1.dp[i-1][j]的值代表在可以任意使用arr[0..i-1]货币的情况下，组成j所需的最小张数，而可以任意使用arr[0..i]货币的情况
     * 当然包括不使用当前货币arr[i]，而只任意使用arr[0..i-1]的货币，因此dp[i][j]的值可能等于dp[i-1][j]
     * 3-2.因为arr[i]只有一张不能重复使用，所以我们考虑dp[i-1][j-arr[i]]的值，它代表任意使用arr[0..i-1]货币的情况下
     * 组成j-arr[i]所需的最小张数，从钱数j-arr[i]到钱数j，只需加上当前的这张arr[i]即可。所以dp[i][j]的值可能等于dp[i-1][j-arr[i]]+1
     * 4.如果j-arr[i]<0，也就是位置越界了，说明arr[i]太大，只用1张都会超过钱数j，令dp[i][j]=dp[i-1][j]即可
     * 否则dp[i][j]=min{dp[i-1][j],dp[i-1][j-arr[i]]+1}
     */
    public int minNumber2(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) return -1;
        int n = arr.length;
        int[][] dp = new int[n][aim + 1];
        for (int j = 1; j <= aim; j++) {
            dp[0][j] = Integer.MAX_VALUE;
        }
        if (arr[0] <= aim) {
            dp[0][arr[0]] = 1;
        }
        int left;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= aim; j++) {
                if (j - arr[i] >= 0 && dp[i - 1][j - arr[i]] != Integer.MAX_VALUE) {
                    left = dp[i - 1][j - arr[i]] + 1;
                } else {
                    left = Integer.MAX_VALUE;
                }
                dp[i][j] = Math.min(left, dp[i - 1][j]);
            }
        }
        return dp[n - 1][aim] == Integer.MAX_VALUE ? -1 : dp[n - 1][aim];
    }

    /**
     * 补充问题的空间压缩解法
     */
    public int minNumber3(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) return -1;
        int n = arr.length;
        int[] dp = new int[aim + 1];
        for (int j = 1; j <= aim; j++) {
            dp[j] = Integer.MAX_VALUE;
        }
        if (arr[0] <= aim) {
            dp[arr[0]] = 1;
        }
        setArray(dp, arr, n, aim);
        return dp[aim] == Integer.MAX_VALUE ? -1 : dp[aim];
    }

    public static void main(String[] args) {
        MoneyExchange exchange = new MoneyExchange();
        int[] arr = {5, 2, 3};
        System.out.println(exchange.minNumber(arr, 15));
        System.out.println(exchange.minNumber(arr, 11));
        System.out.println(exchange.minNumber(arr, 0));
        System.out.println(exchange.minNumber(arr, 1));
        System.out.println(exchange.minNumber1(arr, 11));
        System.out.println(exchange.minNumber2(arr, 5));
        System.out.println(exchange.minNumber2(arr, 11));
        System.out.println(exchange.minNumber3(arr, 5));
    }
}
