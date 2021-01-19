package ai.yunxi.dynamicPlan;

/**
 * 数字字符串转换为字母组合的种数
 * 给定一个字符串str，str全部由数字字符组成，如果str中某一个或某相邻两个字符组成的子串值在1~26之间，则这个子串可以转换为一个字母。
 * 规定"1"转换为"A"，"2"转换为"B"，"3"转换为"C"..."26"转换为"Z"。写一个函数，求str有多少种不同的转换结果。
 * 例如：str="1111"，能转换出的结果有"AAAA"、"KAA"、"AKA"、"AAK"、"KK"，返回5
 * str="01"，"0"没有对应的字母，而"01"根据规定不可转换，返回0
 * str="10"，能转换出的结果是"J"，返回1
 */
public class NumToLetter {

    /**
     * 暴力递归的方法，假设str的长度为N，先定义递归函数p(i)(0<=i<=N)。p(i)的含义是str[0..i-1]已经转换完毕，
     * 而str[i..N-1]还没转换的情况下，最终合法的转换种数有多少并返回。特别指出，p(N)表示str[0..N-1]都已转换完，
     * 没有后续的字符了，那么合法的转换种数为1，即p(N)=1。比如str="111123"，p(4)表示str[0..3]已经转换完毕，
     * 具体结果是什么不重要，反正已经转换完毕并且不可变，没转换的部分是str[4..5]，即"23"，可以转换为"BC"或"W"两种，
     * 所以p(4)=2。p(6)表示str整体已经转换完毕，所以p(6)=1。那么p(i)如何计算呢？只有下面四种情况
     * 1.如果i=N，根据上文对p(N)的解释，应该返回1
     * 2.如果不满足情况1，又有str[i]='0'。str[0..i-1]已经转换完毕，而str[i..N-1]此时又以'0'开头，
     * str[i..N-1]无论怎样都不可能合法转换，所以直接返回0
     * 3.如果不满足情况1和情况2，说明str[i]属于'1'到'9'，str[i]可以转换为'A'~'I'，
     * 那么p(i)的值一定包含p(i+1)的值，即p(i)=p(i+1)
     * 4.如果不满足情况1和情况2，说明str[i]属于'1'到'9'，如果又有str[i..i+1]在"10"~"26"之间，
     * str[i..i+1]可以转换为'J'~'Z'，那么p(i)的值一定包含p(i+2)的值，即p(i)=p(i)+p(i+2)
     */
    public int num(String str) {
        if (str == null || str.equals("")) return 0;
        return count(str.toCharArray(), 0);
    }

    // 计算p(i)的值
    private int count(char[] ch, int i) {
        if (i == ch.length) return 1;
        if (ch[i] == '0') return 0;
        int res = count(ch, i + 1);
        if (i + 1 < ch.length && (ch[i] - '0') * 10 + (ch[i + 1] - '0') < 27) {
            res += count(ch, i + 2);
        }
        return res;
    }

    /**
     * 上面的递归过程中，p(i)最多可能有两个递归分支p(i+1)和p(i+2)，一共有N层递归，所以时间复杂度为O(2^N)，
     * 额外的空间复杂度就是递归使用的函数栈的大小O(N)。但是研究一下就会发现，p(i)最多依赖p(i+1)和p(i+2)的值，
     * 这是可以从后往前顺序计算的，也就是先计算p(N)和p(N-1)，然后根据这两个值计算p(N-2)，再根据p(N-1)和p(N-2)
     * 计算p(N-3)，最后计算出p(0)即可，类似斐波那契数列的求解过程
     * <p>
     * 因为是顺序计算，所以时间复杂度为O(N)，同时只用了cur、next、tmp进行滚动更新，所以额外空间复杂度为O(1)。
     * 但是本题并不能像斐波那契数列问题那样用矩阵的乘法将时间复杂度为优化O(logN)，
     * 这是因为斐波那契数列是严格的f(i)=f(i-1)+f(i-2)，但是本题并不严格，
     * str[i]的具体情况决定了p(i)是等于0，还是等于p(i+1)，还是等于p(i+1)+p(i+2)
     */
    public int num1(String str) {
        if (str == null || str.equals("")) return 0;
        char[] ch = str.toCharArray();
        // 记录上一次计算的值
        int cur = ch[ch.length - 1] == '0' ? 0 : 1;
        // 记录上上一次计算的值
        int next = 1;
        int tmp;
        for (int i = ch.length - 2; i >= 0; i--) {
            if (ch[i] == '0') {
                next = cur;
                cur = 0;
            } else {
                tmp = cur;
                if ((ch[i] - '0') * 10 + (ch[i + 1] - '0') < 27) {
                    cur += next;
                }
                next = tmp;
            }
        }
        return cur;
    }

    public static void main(String[] args) {
        NumToLetter letter = new NumToLetter();
        String str = "111123";
        System.out.println(letter.num(str));
        System.out.println(letter.num1(str));
    }
}
