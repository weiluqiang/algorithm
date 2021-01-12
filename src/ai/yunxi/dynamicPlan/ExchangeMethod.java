package ai.yunxi.dynamicPlan;

/**
 * 换钱的方法数
 * 给定数组arr，arr中所有的值都为正数且不重复。每个值代表一种面值的货币，每种货币可以使用任意张
 * 再给定一个正数aim代表要找的钱数，求换钱有多少种方法
 * 例如：
 * arr=[5,10,25,1]，aim=0
 * 组成0元的方法有1种，就是所有面值的货币都不使用，因此返回1
 * arr=[5,10,25,1]，aim=15
 * 组成15元的方法有6种，3张5元、1张10元+1张5元、1张10元+5张1元、10张1元+1张5元、2张5元+5张1元、15张1元，因此返回6
 * arr=[3,5]，aim=2
 * 任何方法都无法组成2，因此返回0
 * <p>
 * 本题的经典之处在于它可以体现暴力递归、记忆搜索和动态规划之间的关系，并可以在动态规划的基础上再一次进行优化
 * 面试中出现的大量暴力递归的题目都有类似的优化轨迹
 */
public class ExchangeMethod {

    /**
     * 首先是暴力递归的方法，假设arr=[5,10,25,1]，aim=1000，分析过程如下：
     * 1.不使用5元的货币，让[10,25,1]组成剩下的1000，最终方法数记为res1；
     * 2.使用1张5元的货币，让[10,25,1]组成剩下的995，最终方法数记为res2；
     * 3.使用2张5元的货币，让[10,25,1]组成剩下的990，最终方法数记为res3；
     * ......
     * 201.用200张5元的货币，让[10,25,1]组成0，最终方法数记为res201；
     * 那么res1+res2+res3+...+res201就是总的方法数。根据上述过程定义递归函数process(arr,index,aim)
     * 它的含义是如果用arr[index..N-1]这些面值的钱组成aim，能组的总方法数
     * 因为存在大量重复计算，所以暴力递归的时间复杂度非常高，最差情况下为O(aim^N)
     */
    public int number(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) return 0;
        return process(arr, 0, aim);
    }

    private int process(int[] arr, int index, int aim) {
        int res = 0;
        if (index == arr.length) {
            res = aim == 0 ? 1 : 0;
        } else {
            for (int i = 0; arr[index] * i <= aim; i++) {
                res += process(arr, index + 1, aim - arr[index] * i);
            }
        }
        return res;
    }

    /**
     * 接下来是基于暴力递归的初步优化方法，也就是记忆搜索的方法。暴力递归中存在着大量的重复计算，比如上面的例子
     * 当已经使用0张5元+1张10元的情况下，和已经使用2张5元+0张10元的情况下，后续都是求[25,1]组成剩下的990的方法数
     * 都需要递归求解process(arr,2,990)，因此存在大量的重复计算
     * 为了避免重复计算，可以把每次计算的结果缓存到一个数组cache中，当下次进行同样的递归时，可以直接从缓存中取值
     * 观察递归方法process(arr,index,aim)发现，每次arr都是一样的，递归时只有index和aim变化
     * 因此缓存cache可以采用一个二维数组，cache[i][j]就记录递归过程process(arr,i,j)的结果
     * cache数组中的值需要注意，默认是0，因此cache[i][j]=0表示此递归过程没有计算过
     * 当计算结果返回为0时，可以令cache[i][j]=-1，其他结果则直接记录
     * 记忆搜索的时间复杂度为O(N*aim^2)
     */
    public int number1(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) return 0;
        int[][] cache = new int[arr.length + 1][aim + 1];
        return process1(arr, 0, aim, cache);
    }

    private int process1(int[] arr, int index, int aim, int[][] cache) {
        int res = 0;
        if (index == arr.length) {
            res = aim == 0 ? 1 : 0;
        } else {
            int val;
            for (int i = 0; arr[index] * i <= aim; i++) {
                // 先从缓存中取值
                val = cache[index + 1][aim - arr[index] * i];
                if (val == 0) {
                    res += process1(arr, index + 1, aim - arr[index] * i, cache);
                } else {
                    res += val == -1 ? 0 : val;
                }
            }
        }
        cache[index][aim] = res == 0 ? -1 : res;
        return res;
    }

    /**
     * 动态规划方法。生成行数为N、列数为aim+1的矩阵dp，dp[i][j]的含义是在使用arr[0..i]货币的情况下，组成钱数j有多少种方法
     * dp[i][j]值的求解过程如下：
     * 1.对于矩阵第一列dp[..][0]的值，表示组成钱数为0的方法数，很明显是1种，所以dp第一列的值都设成1
     * 2.对于矩阵第一行dp[0][..]的值，表示只能用arr[0]这一种货币的情况下，组成钱的方法数，比如arr[0]=5
     * 能组成的钱数只有5、10、15...所以令dp[0][k*arr[0]]=1(0<=k*arr[0]<=aim)
     * 3.除第一行和第一列的其他位置，dp(i,j)的值是以下几个值的累加：
     * 不用arr[i]的货币，只使用arr[0..i-1]货币时，方法数为dp[i-1][j]
     * 用1张arr[i]货币，剩下的钱用arr[0..i-1]货币组成，方法数为dp[i-1][j-arr[i]]
     * 用2张arr[i]货币，剩下的钱用arr[0..i-1]货币组成，方法数为dp[i-1][j-2*arr[i]]
     * ......
     * 用k张arr[i]货币，剩下的钱用arr[0..i-1]货币组成，方法数为dp[i-1][j-k*arr[i]]，其中j-k*arr[i]>=0
     * 4.最终dp[N-1][aim]的值就是最终结果
     * 在最差的情况下，对于位置(i,j)来说，求解dp[i][j]的过程需要枚举dp[i-1][0..j]上的所有值
     * dp一共有N*aim个位置，所以总体的时间复杂度为O(N*aim^2)
     * <p>
     * 记忆搜索的过程在本质上等价于动态规划的过程。记忆搜索不关心到达某一个递归过程的路径，只是单纯的对计算过的递归过程进行记录
     * 避免重复的递归，而动态规划则是规定好每一个递归过程的计算顺序依次进行计算，后计算的过程严格依赖前面计算的过程
     * 两者各有优缺点，记忆搜索的方法使用了递归函数，这在工程上的开销较大，而动态规划严格规定了计算顺序，将递归计算变成顺序计算
     * 记忆搜索的方法也有自己的优势，比如arr=[20000,10000,1000]，aim=200000，如果是动态规划的方法
     * 需要严格计算3*200000个位置。而对于记忆搜索来说，由于最小面值为1000，所以百位为(1~9)，
     * 十位为(1~9)或者个位为(1~9)的钱数是不可能出现的，所以也不需要计算，记忆搜索只计算必要的递归过程
     */
    public int number2(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) return 0;
        int[][] dp = initDP(arr, aim);
        // 其他位置的值
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= aim; j++) {
                // 位置(i,j)的值求和
                int num = 0;
                for (int k = 0; arr[i] * k <= j; k++) {
                    num += dp[i - 1][j - arr[i] * k];
                }
                dp[i][j] = num;
            }
        }
        return dp[arr.length - 1][aim];
    }

    private int[][] initDP(int[] arr, int aim) {
        int[][] dp = new int[arr.length][aim + 1];
        // 生成第一列的值
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }
        // 生成第一行的值
        for (int j = 1; arr[0] * j <= aim; j++) {
            dp[0][arr[0] * j] = 1;
        }
        return dp;
    }

    /**
     * 接下来介绍时间复杂度为O(N*aim)的动态规划方法。对于上一个动态规划方法中的步骤3，也就是最关键的枚举过程中，
     * 第1种情况的方法数为dp[i-1][j]，把第2到最后一种情况的方法数全部求和，其值其实就是dp[i][j-arr[i]]的值
     * 所以步骤3可以简化为dp[i][j]=dp[i-1][j]+dp[i][j-arr[i]]，省去了枚举过程，时间复杂度也下降了
     */
    public int number3(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) return 0;
        int[][] dp = initDP(arr, aim);
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= aim; j++) {
                if (j - arr[i] >= 0) {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - arr[i]];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[arr.length - 1][aim];
    }

    /**
     * 时间复杂度为O(N*aim)的动态规划方法再加上空间压缩
     */
    public int number4(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) return 0;
        int[] dp = new int[aim + 1];
        for (int i = 0; arr[0] * i <= aim; i++) {
            dp[arr[0] * i] = 1;
        }
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= aim; j++) {
                if (j - arr[i] >= 0) {
                    dp[j] += dp[j - arr[i]];
                }
            }
        }
        return dp[aim];
    }

    public static void main(String[] args) {
        ExchangeMethod method = new ExchangeMethod();
        int[] arr = {5, 10, 25};
        System.out.println(method.number(arr, 100));
        System.out.println(method.number(arr, 0));
        System.out.println(method.number(arr, 14));
        System.out.println(method.number1(arr, 100));
        System.out.println(method.number2(arr, 100));
        System.out.println(method.number3(arr, 100));
        System.out.println(method.number4(arr, 100));
    }
}
