package ai.yunxi.dynamicPlan;

/**
 * 字符串的交错组成
 * 给定三个字符串str1、str2和aim，如果aim包含且仅包含来自str1和str2的所有字符，
 * 而且在aim中属于str1、str2的字符分别保持它们在原串中的顺序，那么称aim是str1和str2的交错组成
 * 实现一个函数，判断aim是否是str1和str2的交错组成
 * 例如：str1="AB"，str2="12"，那么"AB12"、"A1B2"、"A12B"、"1A2B"和"1AB2"等都是str1和str2的交错组成
 */
public class CrossString {

    public boolean isCross(String str1, String str2, String aim, boolean compress) {
        if (str1 == null || str2 == null || aim == null) return false;
        char[] c1 = str1.toCharArray();
        char[] c2 = str2.toCharArray();
        char[] a = aim.toCharArray();
        if (a.length != c1.length + c2.length) {
            return false;
        }
        if (compress) {
            return compress(c1, c2, a);
        } else {
            return dynamic(c1, c2, a);
        }
    }

    /**
     * 如果str1的长度为M，str2的长度为N，经典动态规划的方法可以达到时间复杂度为O(M*N)，额外的空间复杂度O(M*N)。
     * 如果结合空间压缩的技巧，可以把空间复杂度降为O(min{M,N})。先介绍经典动态规划的方法：
     * 首先如果aim是str1和str2的交错组成，aim的长度一定是M+N，否则直接返回false
     * 然后生成大小为(M+1)*(N+1)的布尔类型的矩阵dp，dp[i][j]的值表示aim[0..i+j-1]是否是str1[0..i-1]和str2[0..j-1]的交错组成
     * 计算dp矩阵的时候，从左到右，再从上到下计算，dp[M][N]也就是dp最右下角的值，表示aim整体是否是str1和str2的交错组成
     * 下面具体说明dp矩阵每个位置的值，是如何计算的：
     * 1.dp[0][0]为true，aim为空串，是str1空串和str2空串的交错组成
     * 2.矩阵dp的第一列dp[0..M][0]。dp[i][0]表示aim[0..i-1]能否只被str1[0..i-1]交错组成，如果aim[0..i-1]=str1[0..i-1]，
     * 则令dp[i][0]=true，否则令dp[i][0]=false
     * 3.矩阵dp的第一行dp[0][0..N]。dp[0][j]表示aim[0..j-1]能否只被str2[0..j-1]交错组成，如果aim[0..j-1]=str2[0..j-1]，
     * 则令dp[0][j]=true，否则令dp[0][j]=false
     * 4.对于其他位置(i,j)，dp[i][j]的值由下面的情况决定：
     * 4-1.dp[i-1][j]表示aim[0..i+j-2]能否被str1[0..i-2]和str2[0..j-1]交错组成，如果可以，并且str1[i-1]=aim[i+j-1]，
     * 说明str1[i-1]又可以作为交错组成aim[i+j-1]的最后一个字符，令dp[i][j]=true
     * 4-2.dp[i][j-1]表示aim[0..i+j-2]能否被str1[0..i-1]和str2[0..j-2]交错组成，如果可以，并且str2[j-1]=aim[i+j-1]，
     * 说明str1[j-1]又可以作为交错组成aim[i+j-1]的最后一个字符，令dp[i][j]=true
     * 如果前两种情况都不满足，令dp[i][j]=false
     */
    private boolean dynamic(char[] c1, char[] c2, char[] a) {
        boolean[][] dp = new boolean[c1.length + 1][c2.length + 1];
        dp[0][0] = true;
        // 第一列的值
        for (int i = 1; i <= c1.length; i++) {
            if (a[i - 1] != c1[i - 1]) {
                break;
            }
            dp[i][0] = true;
        }
        // 第一行的值
        for (int j = 1; j <= c2.length; j++) {
            if (a[j - 1] != c2[j - 1]) {
                break;
            }
            dp[0][j] = true;
        }
        // 其他位置的值
        for (int i = 1; i <= c1.length; i++) {
            for (int j = 1; j <= c2.length; j++) {
                if ((dp[i - 1][j] && c1[i - 1] == a[i + j - 1]) || (dp[i][j - 1] && c2[j - 1] == a[i + j - 1])) {
                    dp[i][j] = true;
                }
            }
        }
        return dp[c1.length][c2.length];
    }

    /**
     * 经典动态规划结合空间压缩的方法
     */
    private boolean compress(char[] c1, char[] c2, char[] a) {
        char[] longs = c1.length > c2.length ? c1 : c2;
        char[] shorts = c1.length > c2.length ? c2 : c1;
        boolean[] dp = new boolean[shorts.length + 1];
        dp[0] = true;
        for (int i = 1; i <= shorts.length; i++) {
            if (shorts[i - 1] != a[i - 1]) {
                break;
            }
            dp[i] = true;
        }
        for (int i = 1; i <= longs.length; i++) {
            for (int j = 1; j <= shorts.length; j++) {
                // longs数组是dp的滚动方向，因此原来的dp[i - 1][j]的值，就是未更新前dp[j]的值
                // shorts数组和dp同方向，因此原来的dp[i][j - 1]的值，是更新之后的dp[j - 1]的值
                // 如果不修改dp[j]，它存的还是上一次的值，所以不管true或false都要修改dp[j]
                dp[j] = (dp[j] && longs[i - 1] == a[i + j - 1]) || (dp[j - 1] && shorts[j - 1] == a[i + j - 1]);
            }
        }
        return dp[shorts.length];
    }

    public static void main(String[] args) {
        CrossString cross = new CrossString();
        String str1 = "ABC";
        String str2 = "123";
        System.out.println(cross.isCross(str1, str2, "A2B31C", false));
        System.out.println(cross.isCross(str1, str2, "A1B2C3", false));
        System.out.println(cross.isCross(str1, str2, "A2B31C", true));
        System.out.println(cross.isCross(str1, str2, "A1B2C3", true));
    }
}
