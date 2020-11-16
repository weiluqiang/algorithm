package ai.yunxi.dynamicPlan;

/**
 * 斐波那契系列问题的递归和动态规划
 * 斐波那契数列以如下被以递推的方法定义：F(1)=1，F(2)=1, F(n)=F(n-1)+F(n-2)
 * <p>
 * 1、给定整数N，返回斐波那契数列的第N项
 * 2、给定整数N，代表台阶数，一次可跨2个或1个台阶，返回有多少种走法
 * 3、假设1头成熟的母牛每年会生1头小母牛，并且不会死。第1年农场只有1头成熟的母牛，从第2年开始母牛开始生小母牛，
 * 每头小母牛3年后成熟又会生小母牛，给定整数N，求N年后母牛的数量。
 */
public class FibonacciSeries {

    // 斐波那契数列问题
    // 由于第N项的值与它的前两项相关，故采用递归计算，时间复杂度O(N)
    public int sequence1(int n) {
        if (n < 1) {
            return 0;
        } else if (n == 1 || n == 2) {
            return 1;
        }

        return sequence1(n - 1) + sequence1(n - 2);
    }

    // 采用循环代替递归，从第1项计算到第N项，时间复杂度O(N)
    public int sequence2(int n) {
        if (n < 1) {
            return 0;
        } else if (n == 1 || n == 2) {
            return 1;
        }

        int pre_2 = 1;
        int pre_1 = 1;
        int now = 0;
        for (int i = 3; i <= n; i++) {
            now = pre_1 + pre_2;
            pre_2 = pre_1;
            pre_1 = now;
        }
        return now;
    }

    /**
     * O(logN)时间复杂度的算法
     * 根据递推公式计算的时间复杂度较大，考虑根据递推公式推出通项公式，然后求值
     * 一阶递推公式：
     * 有F(n) = m * F(n-1)，代入可得F(n) = m的2次方 * F(n-2) = ... = m的n-1次方 * F(1)
     * 对于二阶递推公式：
     * 有F(n)=F(n-1)+F(n-2)，它可以用矩阵的乘法表示
     * |F(n), F(n-1)| = |F(n-1), F(n-2)| * |1, 1| = ... = |F(2), F(1)| * |1, 1|的n-2次方
     * ------------------------------------|1, 0|                        |1, 0|
     * 这样，求波那契数列的第N项值的问题，就转化成了求一个矩阵的N次方的问题
     * 下面来研究O(logN)时间复杂度的求矩阵的N次方，这个问题和求一个数的N次方是一样的
     * 想要达到O(logN)的时间复杂度，采用二分的思想，依次求m2，m4，m8，... ，m的2n次，然后相乘
     * 这样问题就转化成了把N用2的指数来表示的问题，即N = 1 + 2 + 4 + 8 + ... + 2的n次
     * 也就是把N变成2进制，比如75，二进制表示就是1001011，N = 1 + 2 + 2的3次 + 2的6次
     * 求m的75次方，就变成了m * m2 * m2的3次 * m2的6次，这样时间复杂度就降低了
     */
    public int sequence3(int n) {
        if (n < 1) {
            return 0;
        } else if (n == 1 || n == 2) {
            return 1;
        }

        // 根据通项公式，先求base矩阵的n-2次方
        int[][] base = {{1, 1}, {1, 0}};
        int[][] res = matrixPower(base, n - 2);
        // 最后乘以矩阵|F(2), F(1)|，即|1, 1|
        return res[0][0] + res[1][0];
    }

    // 求矩阵的N次方
    private int[][] matrixPower(int[][] x, int N) {
        assert x.length == x[0].length;
        int[][] res = new int[x.length][x.length];
        // 先把res初始化为单位矩阵，相当于整数1
        for (int i = 0; i < res.length; i++) {
            res[i][i] = 1;
        }

        int[][] tmp = x;// tmp记录矩阵的每次乘积
        // 通过右移，找到N二进制的每一位，从低到高
        for (; N != 0; N >>= 1) {
            if ((N & 1) != 0) {// 判断每一位是否为0
                res = multiMatrix(res, tmp);
            }
            tmp = multiMatrix(tmp, tmp);// 记录矩阵的2次、4次、8次、...
        }
        return res;
    }

    // 矩阵的乘法实现
    private int[][] multiMatrix(int[][] x, int[][] y) {
        // 矩阵乘法要求数组的的长度分别为m * p和p * n，最后得到m * n的矩阵
        assert x[0].length == y.length;
        int p = y.length, m = x.length, n = y[0].length;
        int[][] res = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < p; k++) {
                    res[i][j] += x[i][k] * y[k][j];
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        FibonacciSeries fibonacci = new FibonacciSeries();
        System.out.println(fibonacci.sequence1(10));
        System.out.println(fibonacci.sequence2(10));
        System.out.println(fibonacci.sequence3(10));
    }
}
