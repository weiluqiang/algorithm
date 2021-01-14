package ai.yunxi.dynamicPlan;

/**
 * 最长公共子序列问题
 * 给定两个字符串str1和str2，返回两个字符串的最长公共子序列
 * 例如：str1="1A2C3D4B56"，str2="B1D23CA45B6A"
 * "123456"或者"12C4B6"都是最长公共子序列，返回哪一个都行
 */
public class MaxSubSeq {

    /**
     * 先介绍求解动态规划表的过程：
     * 如果str1的长度为M，str2的长度为N，生成大小为M*N的矩阵dp，行数为M，列数为N。
     * dp[i][j]的含义是str1[0..i]和str2[0..j]的最长公共子序列的长度，从左到右再从上到下计算矩阵dp
     * 1.矩阵dp第一列即dp[0..M-1][0]，dp[i][0]的含义是str1[0..i]与str2[0]的最长公共子序列长度
     * str2[0]只有一个字符，所以dp[i][0]最大为1。如果str1[i]=str2[0]，令dp[i][0]=1，
     * 一旦dp[i][0]被设置为1，之后的dp[i+1..M-1][0]也都为1
     * 2.矩阵dp第一行即dp[0][0..N-1]与步骤1同理，如果str1[0]=str2[j]，令dp[0][j]=1，
     * 一旦dp[0][j]被设为1，之后的dp[0][j+1..N-1]也都是1
     * 3.对于其他位置(i,j)，dp[i][j]的值只可能来自以下三种情况：
     * 3-1.可能是dp[i-1][j]，代表str1[0..i-1]与str2[0..j]的最长公共子序列长度，
     * 比如str1="A1BC2"，str2="AB34C"，str1[0..3]与str2[0..4]的最长公共子序列为"ABC"，即dp[3][4]=3，
     * str1[0..4]与str2[0..4]的最长公共子序列也是"ABC"，所以dp[4][4]也是3
     * 3-2.可能是dp[i][j-1]，代表str1[0..i]与str2[0..j-1]的最长公共子序列长度，
     * 比如str1="A1B2C"，str2="AB34C"，str1[0..4]与str2[0..3]的最长公共子序列为"ABC"，即dp[4][3]=3，
     * str1[0..4]与str2[0..4]的最长公共子序列也是"ABC"，所以dp[4][4]也是3
     * 3-3.如果str1[i]=str2[j]，还可能是dp[i-1][j-1]+1，比如str1="ABC1"，str2="ABC1"，
     * str1[0..2]与str2[0..2]的最长公共子序列为"ABC"，即dp[2][2]=3，因为str1[3]=str2[3]='1'，
     * 所以str1[0..3]与str2[0..3]的最长公共子序列为"ABC1"
     * 从这三个可能的值中，选最大的最为dp[i][j]的值
     */
    private int[][] getDP(char[] str1, char[] str2) {
        int[][] dp = new int[str1.length][str2.length];
        dp[0][0] = str1[0] == str2[0] ? 1 : 0;
        // 按步骤1生成第一列数据
        for (int i = 1; i < str1.length; i++) {
            dp[i][0] = dp[i - 1][0] | (str1[i] == str2[0] ? 1 : 0);
        }
        // 按步骤2生成第一行数据
        for (int j = 1; j < str2.length; j++) {
            dp[0][j] = dp[0][j - 1] | (str2[j] == str1[0] ? 1 : 0);
        }
        // 生成其他位置的数据
        for (int i = 1; i < str1.length; i++) {
            for (int j = 1; j < str2.length; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                if (str1[i] == str2[j]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + 1);
                }
            }
        }
        return dp;
    }

    /**
     * dp矩阵中最右下角的值代表str1和str2整体的最长公共子序列长度，通过整个dp矩阵的状态，可以得到最长公共子序列：
     * 1.从矩阵的右下角开始，有三种移动方式：向左、向上、向左上，假设移动的过程中，i代表此时的行数，
     * j代表此时的列数，同时用一个变量res来存储最长公共子序列
     * 2.如果dp[i][j]大于dp[i-1][j]和dp[i][j-1]，说明在计算dp[i][j]的时候，一定是选择了dp[i-1][j-1]+1，
     * 可以确定str1[i]=str2[j]，并且这个字符一定属于最长公共子序列，把它放入res，然后向左上方移动
     * 3.如果dp[i][j]等于dp[i-1][j]，说明在计算dp[i][j]的时候，dp[i-1][j-1]+1不是必须的选择，向上移动即可
     * 4.如果dp[i][j]等于dp[i][j-1]，和步骤3同理，向左移动即可
     * 5.如果dp[i][j]同时等于dp[i-1][j]和dp[i][j-1]，向上和向左都可以，并不会错过必须选择的字符
     * <p>
     * 也就是说，通过dp求解最长公共子序列的过程，就是还原出当时如何求解dp的过程，来自哪个策略就朝哪个方向移动
     */
    public String maxSeq(String str1, String str2) {
        char[] c1 = str1.toCharArray();
        char[] c2 = str2.toCharArray();
        int[][] dp = getDP(c1, c2);
        int i = c1.length - 1;
        int j = c2.length - 1;
        char[] res = new char[dp[i][j]];
        int idx = res.length - 1;
        while (idx >= 0) {
            if (i > 0 && dp[i][j] == dp[i - 1][j]) {
                i--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1]) {
                j--;
            } else {
                res[idx--] = c1[i];
                i--;
                j--;
            }
        }
        return String.valueOf(res);
    }

    public static void main(String[] args) {
        MaxSubSeq subSeq = new MaxSubSeq();
        String str1 = "1A2C3D4B56";
        String str2 = "B1D23CA45B6A";
        System.out.println(subSeq.maxSeq(str1, str2));
    }
}
