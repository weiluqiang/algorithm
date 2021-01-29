package ai.yunxi.string;

/**
 * 括号字符串的有效性和最长有效长度
 * 给定一个字符串str，判断是不是整体有效的括号字符串。
 * 例如：str="()"，返回true。str="(()())"，返回true。str="(())"，返回true。
 * str="())"，返回false。str="()("，返回false。str="()a()"，返回false。
 * <p>
 * 补充题目：
 * 给定一个括号字符串str，返回最长的有效括号子串的长度。
 * 例如：str="(()())"，返回6。str="())"，返回2。str="()(()()("，返回4。
 */
public class Parenthesis {

    /**
     * 原问题，判断过程如下：
     * 1.从左到右遍历str，判断每一个字符是不是'('或')'，如果不是就直接返回false
     * 2.遍历到每一个字符时，都检查到目前为止'('和')'的数量，如果')'更多则直接返回false
     * 3.遍历后检查'('和')'的数量，如果一样多则返回true，不一样多则返回false
     */
    public boolean isValid(String str) {
        if (str == null || str.equals("")) return false;
        char[] ch = str.toCharArray();
        int status = 0;
        for (char c : ch) {
            if (c != '(' && c != ')') {
                return false;
            }
            if (c == ')') {
                status--;
                if (status < 0) {
                    return false;
                }
            } else {
                status++;
            }
        }
        return status == 0;
    }

    /**
     * 补充问题：用动态规划求解，可以做到时间复杂度O(N)，额外空间复杂度O(N)。
     * 首先生成长度和str一样的数组dp[]，dp[i]的含义是str[0..i]中必须以str[i]结尾的最长有效括号字符串的长度。
     * 那么dp[i]的值可以按如下方式求解：
     * 1.dp[0]=0，只有一个字符肯定不是有效括号字符串，因此长度为0
     * 2.从左到右依次遍历str[1..N-1]的每个字符，假设遍历到str[i]
     * 2-1.如果str[i]='('，有效括号字符串必然是以')'结尾，所以dp[i]=0
     * 2-2.如果str[i]=')'，那么以str[i]结尾的最长有效括号子串可能存在。dp[i-1]的值代表以str[i-1]结尾的
     * 最长有效括号子串的长度，所以如果i-dp[i-1]-1的位置上的字符是'('，就能与str[i]再匹配出一对有效括号。
     * 比如str="(()())"，遍历到最后一个字符')'时，以它前一个位置结尾的最长有效括号子串是"()()"，长度为4，
     * i-dp[i-1]-1=0，0位置的字符是'('，那么它可以与最后的字符组成一对有效括号。此时dp[i]的值至少是dp[i-1]+2。
     * 但是这并不一定是完整的结果。比如str="()(())"，最后的字符是')'，它前一位置的最长有效括号子串是"()"，
     * i-dp[i-1]-1位置是'('，那么也能组成一对有效括号，dp[i]=dp[i-1]+2=4，但是它前面还有一段"()"，
     * 这段可以和"(())"结合在一起构成更大的有效括号子串"()(())"，这时还应该把dp[i-dp[i-1]-2]的值加到dp[i]中。
     * 这么做把str[i-dp[i-1]-2]结尾的最长有效括号子串接到了前面，才能得到当前字符结尾的最长有效括号子串。
     * 3.dp[0..N-1]中的最大值，就是最终的结果
     */
    public int maxValid(String str) {
        if (str == null || str.equals("")) return 0;
        char[] ch = str.toCharArray();
        int[] dp = new int[ch.length];
        int max = 0;
        int pre;
        for (int i = 1; i < ch.length; i++) {
            // ch[i]='('默认就是0，不用处理
            if (ch[i] == ')') {
                pre = i - dp[i - 1] - 1;
                if (pre >= 0 && ch[pre] == '(') {
                    dp[i] = dp[i - 1] + 2;
                    if (pre > 0) {
                        dp[i] += dp[pre - 1];
                    }
                    if (dp[i] > max) {
                        max = dp[i];
                    }
                }
            }
        }
        return max;
    }

    public static void main(String[] args) {
        Parenthesis p = new Parenthesis();
        System.out.println(p.isValid("(()())"));
        System.out.println(p.isValid("()a()"));
        System.out.println(p.isValid("()("));
        System.out.println(p.maxValid("(()())"));
        System.out.println(p.maxValid("()(()()("));
    }
}
