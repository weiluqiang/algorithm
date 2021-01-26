package ai.yunxi.string;

/**
 * 替换字符串中连续出现的指定字符串
 * 给定三个字符串str、from、to，把str中所有from的子串全部替换成to字符串，
 * 对连续出现from的部分要求只替换成一个to字符串，返回最终的结果字符串。
 * 例如：str="123abc"，from="abc"，to="4567"，返回"1234567"
 * str="123"，from="abc"，to="456"，返回"123"
 * str="123AbcAbc"，from="Abc"，to="X"，返回"123X"
 */
public class ReplaceString {

    /**
     * 如果把str看做字符类型的数组，首先把str中from部分所有位置的字符编码设为0，即空字符，比如str="12AbcAbcA4"，
     * from="Abc"，处理后str为['1','2','0','0','0','0','0','0','a','4']。具体过程如下：
     * 1.生成整型变量match表示目前匹配到from字符串的什么位置，初始时match=0
     * 2.从左到右遍历str中的每个字符，假设当前遍历到str[i]
     * 2-1.如果str[i]=from[match]。如果match是from最后一个字符的位置，说明在str中发现了from字符串，
     * 则从i位置向左的M个位置，都把字符编码设为0，M为from的长度，设置完成后令match=0。
     * 如果match不是from的最后一个字符的位置，令match++，继续遍历str的下一个字符
     * 2-2.如果str[i]!=from[match]。说明匹配失败。令match=0，即回到from的开头重新匹配，继续遍历str的下一个字符
     * 3.通过上面得到的数组，再进行替换。例如，str=['1','2','0','0','0','0','0','0','A','4']，
     * 将连续为0的部分用to来替换，即"12"+to+"A4"即可
     */
    public String replace(String str, String from, String to) {
        if (str == null || from == null || str.equals("") || from.equals("")) return str;
        char[] chs = str.toCharArray();
        char[] chf = from.toCharArray();
        int match = 0;
        for (int i = 0; i < chs.length; i++) {
            if (chs[i] == chf[match++]) {
                if (match == chf.length) {
                    clear(chs, i, chf.length);
                    match = 0;
                }
            } else {
                match = 0;
            }
        }

        // 根据替换后的字符数字拼接结果字符串
        StringBuilder res = new StringBuilder();
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < chs.length; i++) {
            if (chs[i] != 0) {
                cur.append(chs[i]);
            } else if (chs[i] == 0 && (i == 0 || chs[i - 1] != 0)) {
                res.append(cur).append(to);
                cur = new StringBuilder();
            }
        }
        if (cur.length() > 0) {
            res.append(cur);
        }
        return res.toString();
    }

    // 把指定位置的字符编码设为0
    private void clear(char[] ch, int end, int length) {
        while (length-- != 0) {
            ch[end--] = 0;
        }
    }

    public static void main(String[] args) {
        ReplaceString replace = new ReplaceString();
        System.out.println(replace.replace("123abc", "abc", "4567"));
        System.out.println(replace.replace("123", "abc", "456"));
        System.out.println(replace.replace("123AbcAbc", "Abc", "X"));
    }
}
