package ai.yunxi.string;

/**
 * 添加最少字符使字符串整体都是回文字符串
 * 给定一个字符串str，如果可以在str的任意位置添加字符串，请返回在添加字符最少的情况下，让str整体都是回文字符串的一种结果。
 * 例如：str="ABA"，str本身就是回文串，不需要添加字符，所以返回"ABA"。
 * str="AB"，可以在前面添加"B"，返回"BAB"。也可以在后面添加"A"，返回"ABA"。
 * <p>
 * 进阶问题：
 * 给定一个字符串str，再给定str的最长回文子序列字符串strLps，请返回在添加字符最少的情况下，
 * 让str整体都是回文字符串的一种结果。进阶问题比原问题多了一个参数，请做到时间复杂度比原来的低。
 * 例如：str="A1B21C"，strLps="121"，返回"AC1B2B1CA"或者"CA1B2B1AC"。
 */
public class Palindrome {

    /**
     * 再求解原问题之前，先来解决如果可以在str的任意位置添加字符，最少需要添加几个字符可以把str变成回文字符串。
     * 这个问题可以用动态规划的方法求解。如果str的长度为N，动态规划表是一个N*N的矩阵记为dp[][]。
     * dp[i][j]的值代表子串str[i..j]最少添加几个字符可以使str[i..j]变成回文串。求dp[i][j]的值有以下三种情况：
     * 1.如果str[i..j]只有一个字符，此时dp[i][j]=0，因为它已经是回文串了，不必添加任何字符
     * 2.如果str[i..j]有两个字符，此时如果两个字符相等，那么dp[i][j]=0。如果两个字符不相等，那么dp[i][j]=1
     * 3.如果str[i..j]多于两个字符，如果str[i]=str[j]，那么dp[i][j]=dp[i+1][j-1]。
     * 例如str[i..j]="A124521A"，只要能把"124521"变成回文串，然后在左右两边都加上"A"就是str[i..j]变成回文串的结果，
     * 不需要额外的添加字符，所以dp[i][j]和dp[i+1][j-1]的结果是一样的。如果str[i]!=str[j]，把str[i..j]变成回文串的方法有两种，
     * 一种是让str[i..j-1]先变成回文串，然后在左边加上字符str[j]。另一种是让str[i+1..j]先变成回文串，然后在右边加上字符str[i]。
     * 两种方法中哪个代价小就选哪个，即dp[i][j]=min{dp[i][j-1],dp[i+1][j]}+1
     */
    private int[][] getDP(char[] ch) {
        // 整个矩阵不需要计算全部数值，只需要左下半部分的数值
        int[][] dp = new int[ch.length][ch.length];
        // j=0时只需计算一个值dp[0][0]，它的值就是默认值0
        for (int j = 1; j < ch.length; j++) {
            // i=j的值不用算，就是默认的值0
            // 先计算i=j-1的值
            dp[j - 1][j] = ch[j - 1] == ch[j] ? 0 : 1;
            // 接下来计算i<j-1的值
            for (int i = j - 2; i >= 0; i--) {
                if (ch[i] == ch[j]) {
                    // 所有需要的dp[?][j-1]的值都在上一次循环中计算过了
                    dp[i][j] = dp[i + 1][j - 1];
                } else {
                    // dp[i + 1][j]的值也计算过了
                    dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]) + 1;
                }
            }
        }
        return dp;
    }

    /**
     * 下面介绍如何根据dp矩阵，在添加字符最少的情况下，把str变成回文串的一种结果。
     * 首先dp[0][N-1]的值，代表需要最少需要添加的字符数，所以，如果最后的结果记为res，res的长度=dp[0][N-1]+str的长度，
     * 然后依次设置res左右两头的字符，具体过程如下：
     * 1.如果str[i..j]中str[i]=str[j]，那么str[i..j]变成的回文串=str[i]+str[i+1..j-1]的回文串+str[j]，
     * 此时res左右两头的字符都为str[i](也是str[j])，然后继续根据str[i+1..j-1]和矩阵dp来设置res的中间部分。
     * 2.如果str[i]!=str[j]，看dp[i][j-1]和dp[i+1][j]哪个小。如果dp[i][j-1]更小，
     * 那么str[i..j]变成的回文串=str[j]+str[i..j-1]的回文串+str[j]，此时res左右两头的字符都为str[j]，
     * 然后根据str[i..j-1]和矩阵dp来设置res的中间部分。而如果dp[i+1][j]更小，
     * 那么str[i..j]变成的回文串=str[i]+str[i+1..j]的回文串+str[i]，此时res左右两头的字符都为str[i]，
     * 然后根据str[i+1..j]和矩阵dp来设置res的中间部分。如果一样大，任选一种方式都可以得到结果。
     * 3.如果发现res的所有位置都设置完毕，过程结束。
     * <p>
     * 求dp矩阵的时间复杂度为O(N^2)，根据str和dp求最终结果的过程的时间复杂度为O(N)，所以总的时间复杂度为O(N^2)
     */
    public String palindrome(String str) {
        if (str == null || str.length() < 2) return str;
        char[] ch = str.toCharArray();
        int[][] dp = getDP(ch);
        char[] res = new char[dp[0][ch.length - 1] + ch.length];
        int i = 0;
        int j = ch.length - 1;
        int m = 0;
        int n = res.length - 1;
        while (i <= j) {
            if (ch[i] == ch[j]) {
                res[m++] = ch[i++];
                res[n--] = ch[j--];
            } else {
                if (dp[i][j - 1] < dp[i + 1][j]) {
                    res[m++] = ch[j];
                    res[n--] = ch[j--];
                } else {
                    res[m++] = ch[i];
                    res[n--] = ch[i++];
                }
            }
        }
        return String.valueOf(res);
    }

    /**
     * 进阶问题：如果有最长回文子序列字符串strLps，那么求解的时间复杂度可以加速到O(N)，如果str的长度为N，strLps的长度为M，
     * 则整体回文串的长度应该是2*N-M，本次提供的解法类似于"剥洋葱"的过程，具体如下：
     * str="A1BC22DE1F"，strLps="1221"，回文串记为res，它的长度为2*N-M
     * 1.洋葱的第0层由strLps[0]和strLps[M-1]组成，即"1..1"。从str最左侧开始找字符'1'，发现'A'是str的第0个字符，
     * '1'是str的第1个字符，所以左侧第0层洋葱圈外的部分为"A"，记为leftPart。从str最右侧开始找字符'1'，发现右侧第0层洋葱圈外的部分为"F"，
     * 记为rightPart。把leftPart+(rightPart的逆序)复制到res左侧未设值的部分，把rightPart+(leftPart的逆序)复制到res右侧未设值的部分，
     * 即res变为"AF..FA"，把洋葱的第0层复制进res的左右两侧未设值的部分，即res变为"AF1..1FA"。
     * 2.洋葱的第1层由strLps[1]和strLps[M-2]组成，即"2..2"。从str左侧的洋葱第0层往右找"2"，发现左侧第1层洋葱圈外的部分为"BC"，
     * 记为leftPart。从str右侧的洋葱第0层往左找"2"，发现右侧第1层洋葱圈外的部分为"DE"，记为rightPart。同样复制到res的两侧，
     * res变为"AF1BCED..DECB1FA"。把洋葱第1层复制进res的左右两侧未设值的部分，即res变为"AF1BCED22DECB1FA"。
     * 整个过程就是不断找到洋葱圈的左部分和右部分，然后复制到res中，洋葱剥完则过程结束
     */
    public String palindrome(String str, String strLps) {
        if (str == null || str.equals("")) return "";
        char[] ch = str.toCharArray();
        char[] lps = strLps.toCharArray();
        char[] res = new char[ch.length * 2 - lps.length];
        // 原字符串左侧遍历到的位置
        int chL = 0;
        // 原字符串右侧遍历到的位置
        int chR = ch.length - 1;
        // 最长回文子串左侧遍历到的位置
        int lpsL = 0;
        // 最长回文子串右侧遍历到的位置
        int lpsR = lps.length - 1;
        // 结果串左侧复制到的位置
        int resL = 0;
        // 结果串右侧复制到的位置
        int resR = res.length - 1;
        // 每次剥洋葱，leftPart的左边界，右边界每次要重新查找
        int tmpL;
        // 每次剥洋葱，rightPart的右边界，左边界每次要重新查找
        int tmpR;
        while (lpsL <= lpsR) {
            tmpL = chL;
            tmpR = chR;
            // 从str最左侧开始找回文子串的字符
            // 找到的位置就是leftPart的右边界
            while (ch[chL] != lps[lpsL]) {
                chL++;
            }
            // 从str最右侧开始找回文子串的字符
            // 找到的位置就是rightPart的左边界
            while (ch[chR] != lps[lpsR]) {
                chR--;
            }
            // 找到leftPart和rightPart之后，分别复制到res中
            int[] resArr = setRes(res, resL, resR, ch, tmpL, chL, chR, tmpR);
            resL = resArr[0];
            resR = resArr[1];
            // 复制洋葱的第N层到res中
            res[resL++] = lps[lpsL++];
            res[resR--] = lps[lpsR--];
            // chL和chR现在指向洋葱的第N层的位置，需要分别移动1位
            chL++;
            chR--;
        }
        return String.valueOf(res);
    }

    // 复制leftPart和rightPart到res中，返回res设置后的位置
    private int[] setRes(char[] res, int resL, int resR, char[] ch, int ls, int le, int rs, int re) {
        // 复制leftPart到res的左侧，leftPart的逆序到res右侧
        for (int i = ls; i < le; i++) {
            res[resL++] = ch[i];
            res[resR--] = ch[i];
        }
        // 复制rightPart到res的右侧，rightPart的逆序到res左侧
        for (int i = re; i > rs; i--) {
            res[resL++] = ch[i];
            res[resR--] = ch[i];
        }
        return new int[]{resL, resR};
    }

    public static void main(String[] args) {
        Palindrome palindrome = new Palindrome();
        System.out.println(palindrome.palindrome("A1B21C"));
        System.out.println(palindrome.palindrome("A124521A"));
        System.out.println(palindrome.palindrome("A1B21C", "121"));
        System.out.println(palindrome.palindrome("A1BC22DE1F", "1221"));
    }
}
