package ai.yunxi.dynamicPlan;

/**
 * 最小编辑代价
 * 给定两个字符串str1和str2，再给定三个整数ic、dc和rc，分别代表插入、删除、替换一个字符的代价，返回将str1编辑成str2的最小代价
 * <p>
 * str1="abc"，str2="adc"，ic=5，dc=3，rc=2
 * 从"abc"编辑成"adc"，把'b'换成'd'代价最小，所以返回2
 * str1="abc"，str2="adc"，ic=5，dc=3，rc=2
 * 从"abc"编辑成"adc"，先删除'b'，然后插入'd'代价最小，所以返回8
 * str1="abc"，str2="abc"，ic=5，dc=3，rc=2
 * 不用编辑，本来就是一样的字符串，所以返回0
 */
public class EditString {

    /**
     * 如果str1的长度为M，str2的长度为N，经典动态规划的方法可以达到时间复杂度O(M*N)，额外空间复杂度O(M*N)。
     * 如果结合空间压缩的技巧，可以把额外空间复杂度降为O(min{M,N})
     * 先介绍经典动态规划的方法。首先生成大小为(M+1)*(N+1)的矩阵dp，dp[i][j]的值代表str1[0..i-1]编辑成str2[0..j-1]的最小代价
     * 举个例子，str1="ab12cd3"，str2="abcdf"，ic=5，dc=3，rc=2，dp是一个8*6的矩阵，最终计算结果如下：
     * --  ''   'a'  'b'  'c'  'd'  'f'
     * ''  0    5    10   15   20   25
     * 'a' 3    0    5    10   15   20
     * 'b' 6    3    0    5    10   15
     * '1' 9    6    3    2    7    12
     * '2' 12   9    6    5    4    9
     * 'c' 15   12   9    6    7    6
     * 'd' 18   15   12   9    6    9
     * '3' 21   18   15   12   9    8
     * <p>
     * 下面具体说明dp是如何计算的：
     * 1.dp[0][0]=0，表示str1的空字符串编辑成str2空串的最小代价是0
     * 2.dp的第一列即dp[0..M][0]。dp[i][0]表示把str1[0..i-1]编辑成空串的最小代价，毫无疑问是把所有字符删掉，所以dp[i][0]=dc*i
     * 3.dp的第一行即dp[0][0..N]。dp[0][j]表示把空串编辑成str2[0..j-1]的最小代价，就是插入所有字符，所以dp[i][0]=ic*j
     * 4.其他位置按照从左到右、从上到下计算，dp[i][j]只可能来自以下4种情况：
     * 4-1.str1[0..i-1]可以先编辑成str1[0..i-2]，也就是删除字符str1[i-1]，然后由str1[0..i-2]编辑成str2[0..j-1]，
     * dp[i-1][j]代表这个过程的最小代价，所以dp[i][j]可能等于dc+dp[i-1][j]
     * 4-2.str1[0..i-1]可以先编辑成str2[0..j-2]，然后插入字符str2[j-1]，所以dp[i][j]可能等于dp[i][j-1]+ic
     * 4-3.如果str1[i]!=str2[j]，就先把str1[0..i-2]编辑成str2[0..j-2]，然后把str1[i-1]替换成str2[j-1]，
     * 那么dp[i][j]可能等于dp[i-1][j-1]+rc
     * 4-4.如果str1[i]=str2[j]，那么直接把str1[0..i-2]编辑成str2[0..j-2]即可，dp[i][j]可能等于dp[i-1][j-1]
     * 以上4种可能的值中，选最小值作为dp[i][j]的值。dp最右下角的值就是最终结果。
     */
    public int minEdit(String str1, String str2, int ic, int dc, int rc) {
        if (str1 == null || str2 == null) return 0;
        char[] c1 = str1.toCharArray();
        char[] c2 = str2.toCharArray();
        int m = c1.length + 1;
        int n = c2.length + 1;
        int[][] dp = new int[m][n];
        for (int i = 1; i < m; i++) {
            dp[i][0] = dc * i;
        }
        for (int j = 1; j < n; j++) {
            dp[0][j] = ic * j;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (c1[i - 1] == c2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = dp[i - 1][j - 1] + rc;
                }
                dp[i][j] = Math.min(dp[i][j], dc + dp[i - 1][j]);
                dp[i][j] = Math.min(dp[i][j], dp[i][j - 1] + ic);
            }
        }
        return dp[m - 1][n - 1];
    }

    /**
     * 经典动态规划结合空间压缩的方法。本题的空间压缩的方法存在一点特殊，dp[i][j]依赖两个位置的值dp[i-1][j]和dp[i][j-1]，
     * 滚动数组从左向右更新，在求dp[j]的时候，dp[j]没有更新之前的值，相当于dp[i-1][j]，dp[j-1]更新后的值相当于dp[i][j-1]，
     * 而本题中dp[i][j]还依赖dp[i-1][j-1]的值，所以还需要一个变量来保存dp[j-1]更新之前的值
     */
    public int minEdit1(String str1, String str2, int ic, int dc, int rc) {
        if (str1 == null || str2 == null) return 0;
        char[] c1 = str1.toCharArray();
        char[] c2 = str2.toCharArray();
        char[] longs = c1.length > c2.length ? c1 : c2;
        char[] shorts = c1.length > c2.length ? c2 : c1;
        int[] dp = new int[shorts.length + 1];
        // 如果str1比str2更长，说明矩阵的列大于行，说明数组dp是从上向下滚动
        // 由更新后的dp[j-1]更新dp[j]的过程，是从矩阵的左变到右，str2变长了，需要插入字符，因此dp[j]=dp[j-1]+ic
        // 而由原来的dp[j]更新新的dp[j]的过程，是从矩阵的上变到下，str1变长了，需要删除字符，因此dp[j]=原dp[j]+dc
        // 相反的如果longs是c2，shorts是c1，说明数组dp是从左向右滚动，两个更新的过程也都要反过来，
        // 其实只要交换dc和ic的值就可以了，前面的两个更新过程就反过来了
        if (c1.length <= c2.length) {
            int tmp = dc;
            dc = ic;
            ic = tmp;
        }
        for (int i = 1; i <= shorts.length; i++) {
            dp[i] = ic * i;
        }
        for (int i = 1; i <= longs.length; i++) {
            int pre = dp[0];//pre记录当前值左上角的值
            dp[0] = dc * i;
            for (int j = 1; j <= shorts.length; j++) {
                int tmp = dp[j];
                if (longs[i - 1] == shorts[j - 1]) {
                    dp[j] = pre;
                } else {
                    dp[j] = pre + rc;
                }
                dp[j] = Math.min(dp[j], ic + dp[j - 1]);
                dp[j] = Math.min(dp[j], tmp + dc);
                pre = tmp;
            }
        }
        return dp[dp.length - 1];
    }

    public static void main(String[] args) {
        EditString edit = new EditString();
        String str1 = "ab12cd3";
        String str2 = "abcdf";
        System.out.println(edit.minEdit(str1, str2, 5, 3, 2));
        System.out.println(edit.minEdit1(str1, str2, 5, 3, 2));
    }
}
