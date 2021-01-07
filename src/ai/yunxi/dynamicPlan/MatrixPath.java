package ai.yunxi.dynamicPlan;

/**
 * 矩阵的最小路径和
 * 给定一个矩阵m，从左上角开始每次只能向右或者向下走，最后达到右下角的位置，
 * 路径上所有数字累加起来就是路径和，返回所有路径中最小的路径和。
 * 如果给定的m如下：
 * 1 3 5 9
 * 8 1 3 4
 * 5 0 6 1
 * 8 8 4 0
 * 路径1，3，1，0，6，1，0是所有路径中路径和最小的，所以返回12
 */
public class MatrixPath {

    /**
     * 假设矩阵m的大小为M*N，行数为M，列数为N，先生成大小和m一样的矩阵dp，dp[i][j]的值表示从左上角位置走到(i,j)位置的最小路径和
     * 对m的第一行的所有位置来说，走到这些位置只能向右走，所以(0,0)位置到(0,j)位置的路径和就是m[0][0..j]这些值的累加结果
     * 同理，对m的第一列的位置来说，它们对应的路径和就是m[0..i][0]这些值的累加结果
     * 以题目中的例子来说，dp第一行和第一列的值如下：
     * 1  4  9  18
     * 9
     * 14
     * 22
     * 除了第一行和第一列的其他位置(i,j)，它都有左边位置(i-1,j)和上边位置(i,j-1)。从(0,0)到(i,j)的路径必然经过(i-1,j)或(i,j-1)
     * 所以dp[i][j] = min{dp[i-1][j], dp[i][j-1]} + m[i][j]，
     * 含义是比较从(0,0)位置开始，经过(i-1,j)最终达到(i,j)的最小路径和经过(i,j-1)最终到达(i,j)的最小路径，哪条路径的和最小
     * 那么更小的路径和就是dp[i][j]的值。最终生成的dp如下：
     * 1  4  9  18
     * 9  5  8  12
     * 14 5  11 12
     * 22 13 15 12
     * 除第一行和第一列外，每一个位置都考虑从左边还是从上边达到自己的路径和更小。最右下角的值就是这个问题的答案
     * 矩阵中一共有M*N个位置，每个位置都计算一次从(0,0)到自己的最小路径和，计算的时候只比较了大小
     * 所以时间复杂度为O(M*N)，dp矩阵的大小是M*N，所以额外的空间复杂度是O(M*N)
     */
    public int minPath(int[][] m) {
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) return 0;
        int row = m.length;
        int col = m[0].length;
        int[][] dp = new int[row][col];
        dp[0][0] = m[0][0];
        // 生成第一列的值
        for (int i = 1; i < row; i++) {
            dp[i][0] = dp[i - 1][0] + m[i][0];
        }
        // 生成第一行的值
        for (int j = 1; j < col; j++) {
            dp[0][j] = dp[0][j - 1] + m[0][j];
        }
        // 求解dp数其余的值
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + m[i][j];
            }
        }
        return dp[row - 1][col - 1];
    }

    /**
     * 本题的经典动态规划方法在经过空间压缩之后，时间复杂度依然是O(M*N)，但额外的空间复杂度可以从O(M*N)减小至O(min{M,N})
     * 也就是只用大小为min{M,N}的arr数组，具体过程举例说明如下：
     * 1.创建长度为4的数组arr，先生成第一行的最小路径和数据：arr=[1,4,9,18]
     * 2.接着生成第二行的最小路径和，第二行第一个值就等于arr[0]+m[1][0]，用它来替换arr[0]的值
     * 3.对于第二行后面的值，生成逻辑和前面的方法一样，比较此时arr里的当前值和前一值，取较小者再加上m里的值，并更新到arr里
     * 4.重复上述步骤直到遍历到最后一行，返回arr数组里的最后一个值
     * 该方法只通过一个一维数组arr，滚动保存每一行的最小路径和，如果M>N则用长度为N的数组从左向右滚动
     * 如果M<N则用长度为M的数组从上向下滚动
     */
    public int minPath1(int[][] m) {
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) return 0;
        int more = Math.max(m.length, m[0].length);//行数与列数的较大者
        int less = Math.min(m.length, m[0].length);//行数与列数的较小者
        boolean rowMore = more == m.length;//滚动的方向，true时行数较大上下滚动，false时列数较大左右滚动
        int[] arr = new int[less];
        arr[0] = m[0][0];
        for (int i = 1; i < less; i++) {
            arr[i] = arr[i - 1] + (rowMore ? m[0][i] : m[i][0]);
        }
        // 滚动arr数组
        for (int i = 1; i < more; i++) {
            arr[0] = arr[0] + (rowMore ? m[i][0] : m[0][i]);
            for (int j = 1; j < less; j++) {
                arr[j] = Math.min(arr[j - 1], arr[j]) + (rowMore ? m[i][j] : m[j][i]);
            }
        }
        return arr[less - 1];
    }

    public static void main(String[] args) {
        MatrixPath matrix = new MatrixPath();
        int[][] m = {{1, 3, 5, 9}, {8, 1, 3, 4}, {5, 0, 6, 1}, {8, 8, 4, 0}};
        System.out.println(matrix.minPath(m));
        System.out.println(matrix.minPath1(m));
    }
}
