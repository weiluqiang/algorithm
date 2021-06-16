package ai.yunxi.string;

/**
 * 字符串匹配问题
 * 给定字符串str，其中绝对不含有字符'.'和'*'。再给定字符串exp，其中可以含有'.'或'*'，
 * '*'不能是exp的首字符，并且任意两个'*'不相邻。exp中的'.'代表任何一个字符，
 * exp中的'*'表示'*'的前一个字符可以有0个或者多个。请写一个函数，判断str是否能被exp匹配。
 * 举例：
 * 1.str="abc"，exp="abc"，返回true
 * 2.str="abc"，exp="a.c"，exp中单个'.'可以代表任意字符，所以返回true
 * 3.str="abcD"，exp=".*"，exp中'*'的前一个字符是'.'，所以可以表示任意数量的'.'字符，
 * 当时exp是"...."时与"abcD"，返回true
 * 4.str=""，exp="..*"，exp中'*'的前一个字符是'.'，可以表示任意数量的'.'字符，
 * 但是".*"之前还有一个'.'字符，它不受'*'影响，所以str起码有一个字符才能被exp匹配，所以返回false
 */
public class StringMatch {

    /**
     * 首先解决str和exp的有效性问题：
     * 1.str中不能含有字符'.'和'*'
     * 2.exp中'*'不能是首字符，并且任意两个'*'字符不相邻
     */
    private boolean isValid(char[] s, char[] e) {
        for (char c : s) {
            if (c == '.' || c == '*') {
                return false;
            }
        }
        for (int i = 0; i < e.length; i++) {
            if (e[i] == '*' && (i == 0 || e[i - 1] == '*')) {
                return false;
            }
        }
        return true;
    }

    // 通过递归的方式求解
    public boolean isMatch(String str, String exp) {
        if (str == null || exp == null) return false;
        char[] s = str.toCharArray();
        char[] e = exp.toCharArray();
        return isValid(s, e) && process(s, e, 0, 0);
    }

    /**
     * 递归过程process函数的意义是，从str的si为止开始，一直到str结束位置的字符串，即str[si..sl]，
     * 能不能被exp从ei开始到结束位置的字符串，即exp[ei..el]所匹配，所以process(s,e,0,0)就是最终的结果
     * <p>
     * 那么在递归过程中如何判断str[si..sl]是否能被exp[ei..el]匹配呢？
     * 1.如果ei为exp的结束位置，即ei=el，且si也是str的结束位置，si=sl，那么返回true，因为""显然可以匹配""。
     * 如果si不是str的结束位置，那么exp的""不能匹配str的最后一个字符，所以返回false。
     * 2.如果ei下一位置的字符(即exp[ei+1])不为'*'，那么就必须关注str[si]是否和exp[ei]匹配，
     * 如果能匹配，即(e[ei] == s[si] || e[ei] == '.')，还要关注str后续部分是否能被exp后续部分匹配，
     * 即process(s,e,si+1,ei+1)的值。如果str[si]和exp[ei]不匹配，那么可以直接返回false。
     * 3.如果当前ei的下一个字符exp[ei+1]为'*'：
     * 3-1.如果str[si]和exp[ei]不匹配，那么只能让exp[ei..ei+1]这部分为""才能继续匹配，
     * 也就是exp[ei+1]='*'前一个字符(任意字符，记为'?')的数量为0才行，
     * 也就是exp[ei..ei+1]="?*"只能匹配str中的一个空字符""，接下来si不动，ei移到exp[ei+1]='*'之后，
     * 也就是接下来要考察process(s,e,si,ei+2)的值。
     * 举个例子，exp[ei..el]为"a*XXX"，str[si..sl]为"bYYY"，当前'a'!='b'，所以"a*"这个字符串只能匹配一个""，
     * 然后看exp[ei+2..el](即"XXX")能否被str[si..sl](即"bYYY")匹配
     * 3-2.如果str[si]和exp[ei]能匹配，接下来举例说明
     * str[si..sl]为"aaaXXX"，其中"XXX"指不再连续出现'a'字符的后续字符串，exp[ei..el]为"a*YYY"，
     * "YYY"为'*'的后续字符串，此时有多种匹配可能
     * a.如果令"a"和"a*"匹配，那么看后续"aaXXX"和"YYY"是否匹配，是则返回true
     * b.如果令"aa"和"a*"匹配，那么看后续"aXXX"和"YYY"是否匹配，是则返回true
     * c.如果令"aaa"和"a*"匹配，那么看后续"XXX"和"YYY"是否匹配，是则返回true
     * 也就是说，exp[ei..ei+1]即"a*"的部分如果能匹配str后续很多位置的时候，只要有一个返回true，就可以直接返回true
     */
    private boolean process(char[] s, char[] e, int si, int ei) {
        // 情况1，ei为exp的结束位置
        if (ei == e.length) {
            return si == s.length;
        }
        // 情况2，ei为exp的最后一个字符，或者exp[ei+1]不为'*'
        if (ei + 1 == e.length || e[ei + 1] != '*') {
            return si != s.length && (e[ei] == s[si] || e[ei] == '.') && process(s, e, si + 1, ei + 1);
        }
        // 情况3，ei不是最后一个字符，并且exp[ei+1]为'*'
        // 如果str[si]和exp[ei]不匹配，则exp[ei..ei+1]只能匹配空字符，不进入循环，直接看process(s,e,si,ei+2)
        // 如果str[si]和exp[ei]匹配，则可能有多种匹配情况，需要进入循环，遍历每种情况，看是否匹配，有一种匹配则返回true
        // 循环的逻辑，假如exp[ei..ei+1]为"a*"：
        // 1.首先判断1个字符的情况，要求s[si]='a'，成立则进入第一次循环，看process(s,e,si,ei+2)的结果
        // 2.然后是2个字符，要求要求s[si..si+1]='aa'，由于第一次循环s[si]='a'，显然此时只要s[si+1]='a'
        // 就可以进入第二次循环，看process(s,e,si+1,ei+2)的结果
        // 3.接下来是3个字符，同理只要s[si+2]='a'，则进入第三次循环，看process(s,e,si+2,ei+2)的结果
        // 4.如此一致循环下去，判断每一种匹配情况，直到s中某一位置的值不是'a'
        // 5.最后如果以上情况都不匹配，则判断0个字符的情况，同上一个逻辑一样，直接看process(s,e,si,ei+2)
        while (si != s.length && (e[ei] == s[si] || e[ei] == '.')) {
            if (process(s, e, si, ei + 2)) {
                return true;
            }
            si++;
        }
        return process(s, e, si, ei + 2);
    }

    /**
     * 动态规划方式求解
     * 通过观察上面的递归方法，我们很容易发现递归函数process(s,e,si,ei)在每次调用的时候，参数s和e始终不变
     * 那么说明process函数的状态就是si和ei值的组合。所以，如果把递归函数p，在所有不同参数si和ei的情况下，
     * 所有的返回值看做一个范围，那么这个范围就是一个(sl+1)*(el+1)的二维数组。
     * 并且在求解所有p(si,ei)的值的过程中，只会依赖p(si+1,ei+1)或者p(si+k(k>=0),ei+2)的结果
     * 假设二维数组dp[i][j]代表p(i,j)的返回值，它只依赖dp[i+1][j+1]或者dp[i+k(k>=0)][j+2]的值
     * 也就是说，想要求dp[i][j]的值，只需要知道它数组右下方的某些位置的值
     * 所以只需要从右向左，从下到上依次计算出dp每个位置的值即可，而dp[0][0]就是我们要的最终结果
     * <p>
     * 如果str的长度为N，exp的长度为M，因为有枚举的过程，所以整体的时间复杂度为O(N^2 * M)，额外空间复杂度为O(N * M)
     */
    public boolean isMatchDP(String str, String exp) {
        if (str == null || exp == null) return false;
        char[] s = str.toCharArray();
        char[] e = exp.toCharArray();
        if (!isValid(s, e)) {
            return false;
        }
        boolean[][] dp = initDPMap(s, e);
        // 依次从右向左、从下到上计算dp中，除了最后一行、最后一列、倒数第二列的值
        for (int i = s.length - 1; i >= 0; i--) {
            for (int j = e.length - 2; j >= 0; j--) {
                // 递归过程的情况1计算过了，j也不会是exp的最后一个字符，所以情况2只剩下exp[j+1]不为'*'
                // i != s.length显然成立，process(s, e, i + 1, j + 1)的值就是dp[i+1][j+1]
                if (e[j + 1] != '*') {
                    dp[i][j] = (e[j] == s[i] || e[j] == '.') && dp[i + 1][j + 1];
                } else {
                    // 情况3
                    int si = i;
                    while (si != s.length && (e[j] == s[si] || e[j] == '.')) {
                        if (dp[i][j + 2]) {
                            dp[i][j] = true;
                            break;
                        }
                        si++;
                    }

                    if (!dp[i][j]) {
                        dp[i][j] = dp[si][j + 2];
                    }
                }
            }
        }
        return dp[0][0];
    }

    // 生成动态数组dp，dp可以看做一个行数为sl+1，列数为el+1的矩阵
    private boolean[][] initDPMap(char[] s, char[] e) {
        int sl = s.length;
        int el = e.length;
        boolean[][] dp = new boolean[sl + 1][el + 1];
        // dp[sl][el]的情况，它表示s和e都已结束，剩下的字符串都是""，显然可以匹配
        dp[sl][el] = true;

        // dp最后一行的值，也就是dp[sl][0..el-1]的部分，记为dp[sl][i]
        // dp[sl][i]的含义是s已经结束，剩下的字符串为""，而e却没有结束，剩下的字符串为e[i..el-1]
        // 那么什么情况下e[i..el-1](不为"")可以匹配""呢？
        // 只有一种情况，那就是连续出现的"?*"(?表示任意字符)字符，比如"A*"，"A*B*"，"A*B*C*"...
        // 也就是说，在从右向左计算dp[sl][0..el-1]的过程中，看e是不是从右往左重复出现"?*"
        // 那么显然，如果i到el-1是奇数个位置的时候，必然不可能是重复的"?*"，所以不用管默认false即可
        // 如果是偶数的时候，那么重复出现时，e[i]='?'，e[i+1]='*'则dp[sl][i]=true；e[i]='*'，e[i+1]='?'则dp[sl][i]=false
        // 如果不是重复出现，那么dp[sl][i]=false，并且后面dp[sl][0..i-1]的部分也只能是false
        for (int j = el - 2; j >= 0; j -= 2) {
            if (e[j] != '*' && e[j + 1] == '*') {
                dp[sl][j] = true;
            } else {
                // 不是重复出现的"?*"，则后面的都是false，直接跳出循环
                break;
            }
        }

        // 再看dp最后一列，也就是dp[0..sl-1][el]的值
        // 它表示e已经结束，剩下的是""，而s没有结束，不管剩下什么字符，都不能匹配，那么都是默认false即可

        // dp倒数第二列，也就是dp[0..sl-1][el-1]的值
        // 这表示e还剩一个字符，如果s剩的多于一个字符，也就是dp[0..sl-2][el-1]的时候，无论如何都不能匹配
        // 如果s也剩一个字符，就是dp[sl-1][el-1]，只要s[sl-1]=e[el-1]，或者e[el-1]='.'就可以匹配，其他情况也不能匹配
        if (el > 0 && sl > 0) {
            if (e[el - 1] == '.' || s[sl - 1] == e[el - 1]) {
                dp[sl - 1][el - 1] = true;
            }
        }
        return dp;
    }

    public static void main(String[] args) {
        StringMatch match = new StringMatch();
        System.out.println(match.isMatch("abc", "abc"));
        System.out.println(match.isMatch("abc", "a.c"));
        System.out.println(match.isMatch("abcD", ".*"));
        System.out.println(match.isMatch("", ".*"));
        System.out.println(match.isMatch("", "..*"));

        System.out.println(match.isMatchDP("abc", "abc"));
        System.out.println(match.isMatchDP("abc", "a.c"));
        System.out.println(match.isMatchDP("abcD", ".*"));
        System.out.println(match.isMatchDP("", ".*"));
        System.out.println(match.isMatchDP("", "..*"));
    }
}
