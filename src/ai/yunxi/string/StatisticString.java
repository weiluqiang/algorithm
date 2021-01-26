package ai.yunxi.string;

/**
 * 字符串的统计字符串
 * 给定一个字符串str，返回str的统计字符串。例如，"aaabbadddffc"的统计字符串为"a_3_b_2_a_1_d_3_f_2_c_1"
 * <p>
 * 补充题目：
 * 给定一个字符串的统计字符串cs，再给定一个整数index，返回cs所代表的原始字符串上的第index个字符。
 * 例如，"a_1_b_100"所代表的原始字符串上第0个字符是'a'，第50个字符是'b'
 */
public class StatisticString {

    /**
     * 1.如果str为空，那么统计字符串不存在
     * 2.如果str不为空，首先生成字符串类型的变量res，表示统计字符串，还有整型变量num，代表当前字符的数量。
     * 初始时字符串res只包含str的第0个字符str[0]，同时num=1
     * 3.从str[1]开始，从左到右遍历str，假设遍历到位置i。如果str[i]=str[i-1]，说明当前连续出现的字符还未结束，
     * 令num++，然后继续遍历下一个字符。如果str[i]!=str[i-1]，说明当前连续出现的字符已经结束，
     * 令res=res+"_"+num+"_"+str[i]，然后令num=1，继续遍历下一个字符。
     * 4.对于步骤3中的每一个字符，无论连续还是不连续，都是在发现一个新字符的时候将其放到res的尾部。
     * 所以当遍历结束时，最后一个字符的统计还未放入，因此要先放入再返回
     */
    public String statistic(String str) {
        if (str == null || str.equals("")) return "";
        char[] ch = str.toCharArray();
        StringBuilder res = new StringBuilder();
        res.append(ch[0]);
        int num = 1;
        for (int i = 1; i < ch.length; i++) {
            if (ch[i] == ch[i - 1]) {
                num++;
            } else {
                res.append("_").append(num).append("_").append(ch[i]);
                num = 1;
            }
        }
        res.append("_").append(num);
        return res.toString();
    }

    // 补充问题：通过遍历字符数组求解
    public char charAt(String cs, int index) {
        if (cs == null || cs.equals("")) return 0;
        char[] ch = cs.toCharArray();
        // true阶段读取字符，false阶段读取数值
        boolean stage = true;
        // 当前字符
        char cur = 0;
        // 当前字符的统计数值
        int num = 0;
        // 统计总共的字符数
        int sum = 0;
        for (char c : ch) {
            if (c == '_') {
                // 变换阶段
                stage = !stage;
            } else if (stage) {
                sum += num;
                if (sum > index) {
                    return cur;
                }
                num = 0;
                cur = c;
            } else {
                num = num * 10 + c - '0';
            }
        }
        return sum + num > index ? cur : 0;
    }

    // 补充问题：通过分割字符串求解
    public char charAt1(String cs, int index) {
        if (cs == null || cs.equals("")) return 0;
        String[] arr = cs.split("_");
        int i = 1;
        int sum = 0;
        while (i < arr.length) {
            sum += Integer.parseInt(arr[i]);
            if (sum > index) {
                break;
            }
            i += 2;
        }
        return sum > index ? arr[i - 1].charAt(0) : 0;
    }

    public static void main(String[] args) {
        StatisticString string = new StatisticString();
        System.out.println(string.statistic("aaabbadddffc"));
        System.out.println(string.charAt("a_1_b_100", 100));
        System.out.println(string.charAt1("a_1_b_100", 100));
    }
}
