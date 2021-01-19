package ai.yunxi.dynamicPlan;

/**
 * 表达式得到期望结果的组成种数
 * 给定一个只有0(假)、1(真)、&(逻辑与)、|(逻辑或)、^(异或)五种字符组成的字符串express，再给定一个布尔值desired。
 * 返回express能有多少种组合方式，可以达到desired的结果。
 * 例如：express="1^0|0|1"，desired=false
 * 只有1^((0|0)|1)和1^(0|(0|1))的组合可以得到false，返回2
 * express="1"，desired=false，无组合可以得到false，返回0
 */
public class ExpressProblem {

    /**
     * 首先判断express是否合乎题目要求，比如"1^"和"10"都不是有效的表达式。总结起来有以下三个判断标准：
     * 1.表达式的长度必须是奇数
     * 2.表达式下标为偶数的位置的字符一定是'0'或'1'
     * 3.表达式下标为奇数的位置的字符一定是'&'或'|'或'^'
     * 只要符合以上三个标准，表达式必然是有效的
     */
    private boolean notValid(char[] exp) {
        if ((exp.length & 1) == 0) {
            return true;
        }
        for (int i = 0; i < exp.length; i += 2) {
            if (exp[i] != '0' && exp[i] != '1') {
                return true;
            }
        }
        for (int i = 1; i < exp.length; i += 2) {
            if (exp[i] != '&' && exp[i] != '|' && exp[i] != '^') {
                return true;
            }
        }
        return false;
    }

    /**
     * 暴力递归法。在判断express符合标准后，将express划分成左右两个部分，求出各种划分情况下，
     * 能得到desired的种数是多少。以本题的例子进行举例说明，express="1^0|0|1"，desired=false：
     * 1.根据'^'划分，左部分为"1"，右部分为"0|0|1"，因为当前划分的逻辑符号为'^'，
     * 所以要想最终得到false，可能的情况有两种：左右都为真，或者左右都为假
     * 结果1=左部分为真的种数*右部分为真的种数 + 左部分为假的种数*右部分为假的种数
     * 2.根据第一个'|'划分，左部分为"1^0"，右部分为"0|1"，因为当前划分的逻辑符号为'|'，
     * 所以要想得到false只有1种情况：左右部分都为假
     * 结果2=左部分为假的种数*右部分为假的种数
     * 3.根据第二个'|'划分，左部分为"1^0|0"，右部分为"1"，因为当前划分的逻辑符号为'|'，
     * 同理可知，结果3=左部分为假的种数*右部分为假的种数
     * 4.结果1+结果2+结果3就是总的种数，也就是说，一个字符串中有几个逻辑符号，就有多少种划分，
     * 把每种划分能够得到最终结果的种数全加起来，就是最后所需的结果
     */
    public int num(String str, boolean desired) {
        if (str == null || str.equals("")) return 0;
        char[] exp = str.toCharArray();
        if (notValid(exp)) {
            return 0;
        }
        return process(exp, desired, 0, exp.length - 1);
    }

    /**
     * 一个长度为N的express，假设计算express[i..j]的过程记为p(i,j)，那么计算p(0,N-1)需要计算p(0,0)与p(1,N-1)、
     * p(0,1)与p(2,N-1)...p(0,i)与p(i+1,N-1)...p(0,N-2)与p(N-1,N-1)，起码2N种状态。对于每一组p(0,i)与p(i+1,N-1)来说，
     * 两者相加的划分种数又是N-1种，所以起码要2(N-1)种状态。所以计算一个长度为N的express，总的时间复杂度为O(N!)，
     * 额外的空间复杂度为O(N)。之所以递归方法的时间复杂度这么高，是因为每一种状态计算过后没有保存下来，导致大量重复计算的发生
     */
    private int process(char[] exp, boolean desired, int l, int r) {
        if (l == r) {
            if (exp[l] == '1') {
                return desired ? 1 : 0;
            } else {
                return desired ? 0 : 1;
            }
        }
        int res = 0;
        if (desired) {
            for (int i = l + 1; i < r; i += 2) {
                switch (exp[i]) {
                    case '&':
                        res += process(exp, true, l, i - 1) * process(exp, true, i + 1, r);
                        break;
                    case '|':
                        res += process(exp, true, l, i - 1) * process(exp, true, i + 1, r);
                        res += process(exp, false, l, i - 1) * process(exp, true, i + 1, r);
                        res += process(exp, true, l, i - 1) * process(exp, false, i + 1, r);
                        break;
                    case '^':
                        res += process(exp, false, l, i - 1) * process(exp, true, i + 1, r);
                        res += process(exp, true, l, i - 1) * process(exp, false, i + 1, r);
                        break;
                }
            }
        } else {
            for (int i = l + 1; i < r; i += 2) {
                switch (exp[i]) {
                    case '&':
                        res += process(exp, false, l, i - 1) * process(exp, false, i + 1, r);
                        res += process(exp, false, l, i - 1) * process(exp, true, i + 1, r);
                        res += process(exp, true, l, i - 1) * process(exp, false, i + 1, r);
                        break;
                    case '|':
                        res += process(exp, false, l, i - 1) * process(exp, false, i + 1, r);
                        break;
                    case '^':
                        res += process(exp, true, l, i - 1) * process(exp, true, i + 1, r);
                        res += process(exp, false, l, i - 1) * process(exp, false, i + 1, r);
                        break;
                }
            }
        }
        return res;
    }

    /**
     * 动态规划的方法。如果express的长度N，生成两个大小为N*N的矩阵t和f，t[i][j](j<i)表示express[j..i]组成true的种数，
     * f[i][j]表示express[j..i]组成false的种数，t[i][j]和f[i][j]的计算方式还是枚举express[j..i]上的每种划分
     * <p>
     * 矩阵t和f的大小为N*N，每个位置在计算的时候都有枚举的过程，所以时间复杂度为O(N^3)，额外空间复杂度为O(N^2)
     */
    public int num1(String str, boolean desired) {
        if (str == null || str.equals("")) return 0;
        char[] exp = str.toCharArray();
        if (notValid(exp)) {
            return 0;
        }
        int[][] t = new int[exp.length][exp.length];
        int[][] f = new int[exp.length][exp.length];
        t[0][0] = exp[0] == '0' ? 0 : 1;
        f[0][0] = exp[0] == '1' ? 0 : 1;
        for (int i = 2; i < exp.length; i += 2) {
            // i和j相等时，矩阵的值只和当前字符有关
            t[i][i] = exp[0] == '0' ? 0 : 1;
            f[i][i] = exp[0] == '1' ? 0 : 1;
            // 有了i和j相等时的结果，下面能计算的是i-2到i的结果，再下面是i-4到i，等等，一直到0到i
            for (int j = i - 2; j >= 0; j -= 2) {
                // 每一步计算i-2n到i的结果时，同样都是根据逻辑字符分成左右两部分计算，假设位置为k+1，
                // 左部分的结果值，是i-2n到k的值，因为k<i，这个值在以前i的循环中计算过了
                // 右部分的结果值，是k+2到i的值，因为k+2>i-2n，这个值是在前几次j的循环中计算过了
                // 因此计算i-2n到i的结果所需的所有值都计算过了，可以直接计算其结果
                for (int k = j; k < i; k += 2) {
                    // 根据k+1位置的逻辑字符划分
                    if (exp[k + 1] == '&') {
                        t[j][i] += t[j][k] * t[k + 2][i];
                        f[j][i] += f[j][k] * f[k + 2][i] + t[j][k] * f[k + 2][i] + f[j][k] * t[k + 2][i];
                    } else if (exp[k + 1] == '|') {
                        t[j][i] += t[j][k] * t[k + 2][i] + t[j][k] * f[k + 2][i] + f[j][k] * t[k + 2][i];
                        f[j][i] += f[j][k] * f[k + 2][i];
                    } else {
                        t[j][i] += t[j][k] * f[k + 2][i] + f[j][k] * t[k + 2][i];
                        f[j][i] += t[j][k] * t[k + 2][i] + f[j][k] * f[k + 2][i];
                    }
                }
            }
        }
        return desired ? t[0][exp.length - 1] : f[0][exp.length - 1];
    }

    public static void main(String[] args) {
        ExpressProblem express = new ExpressProblem();
        String str = "1^0|0|1";
        System.out.println(express.num(str, true));
        System.out.println(express.num(str, false));
        System.out.println(express.num1(str, true));
        System.out.println(express.num1(str, false));
    }
}
