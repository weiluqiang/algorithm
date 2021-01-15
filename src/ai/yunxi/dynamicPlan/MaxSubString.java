package ai.yunxi.dynamicPlan;

/**
 * 最长公共子串问题
 * 给定两个字符串str1和str2，返回两个字符串的最长公共子串
 * 例如：str1="1AB2345CD"，str2="12345EF"，返回"2345"
 * 如果str1的长度为M，str2的长度为N，实现时间复杂度为O(M*N)，额外的空间复杂度为O(1)的方法
 */
public class MaxSubString {

    /**
     * 经典动态规划方法可以做到时间复杂度为O(M*N)，额外的空间复杂度O(M*N)
     * 首先生成动态规划表，生成大小为M*N的矩阵dp，行数为M，列数为N。
     * dp[i][j]的含义是，在必须把str1[i]和str2[j]当做公共子串最后一个字符的情况下，公共子串最长能有多长。
     * 比如，str1="A1234B"，str2="CD1234"，dp[3][4]的含义是在必须把str1[3]和str2[4]当做公共子串最后一个字符的情况下，
     * 公共子串最长能有多长。这种情况下的最长公共子串为"123"，所以dp[3][4]为3。再如，str1="A12E4B"，str2="CD12F4"，
     * dp[3][4]的含义是必须把'E''、F'当做公共子串最后一个字符，这种情况下不能构成公共子串，因此dp[3][4]=0
     * 求解dp[i][j]的具体过程如下：
     * 1.矩阵dp的第一列dp[0..M-1][0]。对于某一个位置(i,0)来说，如果str1[i]=str2[0]，那么dp[i][0]=1，否则dp[i][0]=0
     * 2.矩阵dp的第一行dp[0][0..N-1]。同理对于某一个位置(0,j)来说，如果str1[0]=str2[j]，那么dp[0][j]=1，否则dp[0][j]=0
     * 3.其他位置按照从左到右、从上到下来计算，dp[i][j]的值只能有以下两种情况：
     * 3-1.如果str1[i]!=str2[j]，说明把它们当做公共子串最后一个字符的不可能的，所以dp[i][j]=0
     * 3-2.如果str1[i]=str2[j]，说明它们可以作为公共子串最后一个字符，从最后一个字符向左能扩多大的长度呢？
     * 就是dp[i-1][j-1]的值，所以dp[i][j]=dp[i-1][j-1]+1
     */
    private int[][] getDP(char[] str1, char[] str2) {
        int[][] dp = new int[str1.length][str2.length];
        // 生成第一列的值
        for (int i = 0; i < str1.length; i++) {
            if (str1[i] == str2[0]) {
                dp[i][0] = 1;
            }
        }
        // 生成第一行的值
        for (int j = 1; j < str2.length; j++) {
            if (str1[0] == str2[j]) {
                dp[0][j] = 1;
            }
        }
        // 其他位置的值
        for (int i = 1; i < str1.length; i++) {
            for (int j = 1; j < str2.length; j++) {
                if (str1[i] == str2[j]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                }
            }
        }
        return dp;
    }

    /**
     * 生成了动态规划表dp之后，再求最长公共子串就非常容易了。假设矩阵dp中的最大值是dp[3][4]=3，说明最长公共子串的长度为3
     * 它的最后一个字符是str1[3]，也是str2[4]，然后从str1[3]开始向左3个字符就是最长公共子串，即是str1[1..3]，也是str2[2..4]
     * 因此遍历dp找到最大值及其位置，就可以找到最长公共子串了
     */
    public String maxSub(String str1, String str2) {
        if (str1 == null || str1.equals("") || str2 == null || str2.equals("")) return "";
        char[] c1 = str1.toCharArray();
        char[] c2 = str2.toCharArray();
        int[][] dp = getDP(c1, c2);
        int max = 0;
        int end = 0;
        for (int i = 0; i < c1.length; i++) {
            for (int j = 0; j < c2.length; j++) {
                if (dp[i][j] > max) {
                    max = dp[i][j];
                    end = i;
                }
            }
        }
        return str1.substring(end - max + 1, end + 1);
    }

    /**
     * 观察生成dp的过程可知，dp[i][j]的值最多只需要其左上方dp[i-1][j-1]的值，所以遍历的时候，从矩阵的左、上边界开始，
     * 沿斜线一直向右下遍历，直到最右下的边界，整个斜线上的位置(i,j)，如果满足str1[i]=str2[j]，则dp[i][j]=dp[i-1][j-1]+1，
     * 否则dp[i][j]=0，只需有一个变量记录dp[i-1][j-1]的值即可，遍历完整条斜线后，用一个变量记录其中的最大值，
     * 并和其他斜线的最大值比较，所有中的最大者就是最长公共子串的长度，记录这个长度和位置
     */
    public String maxSub1(String str1, String str2) {
        if (str1 == null || str1.equals("") || str2 == null || str2.equals("")) return "";
        char[] c1 = str1.toCharArray();
        char[] c2 = str2.toCharArray();
        int row = 0;//斜线开始位置的行
        int col = c2.length - 1;//斜线开始位置的列
        int max = 0;//记录最大长度
        int end = 0;//记录最大长度时，公共子串的结束位置
        while (row < c1.length) {
            int i = row;
            int j = col;
            int len = 0;//记录斜线上一个位置的值
            while (i < c1.length && j < c2.length) {
                if (c1[i] == c2[j]) {
                    len++;
                } else {
                    len = 0;
                }
                // 记录最大长度和位置
                if (len > max) {
                    max = len;
                    end = i;
                }
                i++;
                j++;
            }
            if (col > 0) {
                col--;//开始位置先向左移动
            } else {
                row++;//等移动到最左上后再向下移动
            }
        }
        return str1.substring(end - max + 1, end + 1);
    }

    public static void main(String[] args) {
        MaxSubString string = new MaxSubString();
        String str1 = "1AB2345CD";
        String str2 = "12345EF";
        System.out.println(string.maxSub(str1, str2));
        System.out.println(string.maxSub1(str1, str2));
    }
}
