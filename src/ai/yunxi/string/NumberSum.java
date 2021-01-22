package ai.yunxi.string;

/**
 * 字符串中数字子串的求和
 * 给定一个字符串str，求其中全部数字串所代表的的数字之和
 * 要求：
 * 1.忽略小数点字符，如"A1.3"，其中包含两个数字1和3
 * 2.如果紧贴数字子串的左侧出现字符"-"，当连续出现的数量为奇数时，则数字视为负，
 * 连续出现的数量为偶数时，则数字视为正。例如"A-1BC--12"，其中包含数字位-1和12
 * 举例：
 * str="A1CD2E33"，返回36
 * str="A-1B--2C--D6E"，返回7
 */
public class NumberSum {

    /**
     * 本题能做到时间复杂度O(N)，额外空间复杂度O(1)。具体过程如下：
     * 1.生成三个变量，整型变量res，表示目前的累加和；整型变量num，表示当前收集到的数字；布尔型变量pos，
     * 表示如果把num累加到res里，num是正还是负。初始时，res=0，num=0，pos=true
     * 2.从左到右遍历str，假设遍历到字符ch，根据具体内容ch有不同的处理：
     * 2-1.如果ch是'0'~'9'，ch的值记为cur，例如str="123"，初始时num=0，pos=true。当ch='1'时，num变成1；
     * 当ch='2'时，num变成12；ch='3'时，num变成123。再如str="-123"，初始时num=0，pos=true。
     * 当ch='-'时，pos变成false；h='1'时，num变成-1；ch='2'时，num变成-12；ch='3'时，num变成-123。
     * 总之，num=num*10+(pos?cur:-cur)。
     * 2-2.如果ch不是'0'~'9'，此时不管ch是什么，都执行累加，令res+=num，然后令num=0。累加完后，再看ch的具体情况。
     * 如果ch不是字符'-'，令pos=true，即如果ch既不是数字字符也不是'-'，pos都变为true。如果ch是字符'-'，
     * 此时看ch的前一字符，如果前一字符也是'-'，则令pos求反，即pos=!pos，否则令pos=false
     * 2-3.既然我们把累加的时机放在了ch不是数字字符的时候，那么如果str是以数字结尾的，会出现最后一个数字没有累加的情况。
     * 所以在遍历完毕之后，在执行一次累加，防止最后的数字没有累加的情况
     */
    public int sum(String str) {
        if (str == null || str.length() == 0) return 0;
        char[] ch = str.toCharArray();
        int res = 0;
        int num = 0;
        boolean pos = true;
        int cur;
        for (int i = 0; i < ch.length; i++) {
            cur = ch[i] - '0';
            if (cur < 0 || cur > 9) {
                // 非数字字符的情况
                if (num != 0) res += num;
                num = 0;
                if (ch[i] == '-') {
                    // 判断前一字符
                    if (i > 0 && ch[i - 1] == '-') {
                        pos = !pos;
                    } else {
                        pos = false;
                    }
                } else {
                    pos = true;
                }
            } else {
                num = num * 10 + (pos ? cur : -cur);
            }
        }
        if (num != 0) res += num;
        return res;
    }

    public static void main(String[] args) {
        NumberSum sum = new NumberSum();
        System.out.println(sum.sum("A1CD2E33"));
        System.out.println(sum.sum("A-1B--2C--D6E"));
    }
}
