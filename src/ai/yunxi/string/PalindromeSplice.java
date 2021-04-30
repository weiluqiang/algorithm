package ai.yunxi.string;

/**
 * 回文最少分割数
 * 给定一个字符串str，返回把str全部切成回文子串的最小分割数
 * 举例：
 * 1.str="ABA"，它本身就是回文，不需要分割，所以返回0
 * 2.str="ACDCDCDAD"，最少需要切2次变成三个回文子串："A"、"CDCDC"、"DAD"，所以返回2
 */
public class PalindromeSplice {

    /**
     * 这是一个经典的动态规划题目。
     * 定义动态规划数组dp，dp[i]的含义是子串str[i..len-1]至少需要切割几次，
     * 才能把str[i..len-1]全部切成回文子串。那么，dp[0]就是最终要的结果。
     * <p>
     * 从右往左依次计算dp[i]的值，i初始为len-1，具体过程如下：
     * 1.假设j处在i和len-1之间，即i<=j<len，如果str[i..j]是回文串，那么dp[i]的值可能是dp[j+1]+1，
     * 其含义是在str[i..len-1]上，既然str[i..j]是回文串，那么它可以自己作为一个分割的部分，
     * 剩下的部分str[j+1..len-1]继续做最经济的切割，而dp[j+1]的含义正好是str[j+1..len-1]的最少回文分割数
     * <p>
     * 2.让j在i到len-1位置上枚举，那么所有可能情况中的最小值就是dp[i]的值，
     * 即dp[i]=min{dp[j+1]+1 (i<=j<len，且str[i..j]必须是回文串)}
     * <p>
     * 3.如何快速判断str[i..j]是否回文串呢？过程如下：
     * 3-a.定义一个二维数组boolean[][] p，如果p[i][j]值为true，说明字符串str[i..j]是回文串，否则不是。
     * 在计算dp数组的过程中，希望能够同步、快速的计算出矩阵p。
     * 3-b.p[i][j]如果为true，一定是以下三种情况：
     * 情况一、str[i..j]由一个字符组成
     * 情况二、str[i..j]由两个字符组成，且两个字符一样
     * 情况三、str[i+1..j-1]是回文串，即p[i+1][j-1]为true，且str[i]=str[j]，即str[i..j]首尾两个字符一样
     * 3-c.在计算dp数组的过程中，位置i是从右往左依次计算的。而对每一个i来说，又依次从i位置向右枚举所有的位置j(i<=j<len)，
     * 以此来决策dp[i]的值，所以对于p[i][j]来说，p[i+1][j-1]的值一定计算过。
     * <p>
     * 4.最终返回p[0]的值，过程结束。
     */
    public int lessCut(String str) {
        if (str == null || str.length() == 0) return 0;
        char[] ch = str.toCharArray();
        int len = ch.length;
        int[] dp = new int[len + 1];
        dp[len] = -1;
        // 第一次进入循环时j=i=len-1，需要p[i + 1][j - 1]也就是p[len][len - 2]的值，暂时看来p的长度应为len+1
        // 但是分析下面的判断回文逻辑可知，第一次时ch[i] == ch[j]为true，j - i < 2也为true
        // 因此只要把条件j - i < 2写在条件p[i + 1][j - 1]的左边，就不需要p[len][len - 2]，也不会报错
        boolean[][] p = new boolean[len][len];
        for (int i = len - 1; i >= 0; i--) {
            // dp[i]如果不在此赋值，就是默认的0，下面的最小值比较就没有意义了
            dp[i] = Integer.MAX_VALUE;
            for (int j = i; j < len; j++) {
                // 判断str[i..j]是否回文串
                // j=i时，j - i < 2为true，ch[i]此时也肯定等于ch[j]，符合情况一
                // j=i+1时，j - i < 2为true，只有ch[i]等于ch[j]时为回文串，符合情况二
                // j>i+1时，只有p[i+1][j-1]为true，且同时ch[i]等于ch[j]时为回文串，符合情况三
                if (ch[i] == ch[j] && (j - i < 2 || p[i + 1][j - 1])) {
                    p[i][j] = true;
                    // 当第一次进入此循环时，j=i=len-1，dp[j+1]=dp[len]，所以dp的长度应当是len+1
                    // 且dp[len]不能默认为0，需要提前赋值
                    // 因为此时只有一个字符dp[i]的值应该是0，所以dp[len]应该赋值为-1，保证第一次dp[i]的结果为0
                    dp[i] = Math.min(dp[i], dp[j + 1] + 1);
                }
            }
        }
        return dp[0];
    }

    public static void main(String[] args) {
        PalindromeSplice splice = new PalindromeSplice();
        System.out.println(splice.lessCut("ABA"));
        System.out.println(splice.lessCut("ACDCDCDAD"));
    }
}
