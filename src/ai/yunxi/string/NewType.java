package ai.yunxi.string;

/**
 * 找到被指的新类型字符
 * 新类型的字符定义如下：
 * 1.新类型字符是长度为1或2的字符串
 * 2.表现形式可以仅是单个小写字母，如"e"；也可以是大写字母+小写字母，如"Ab"；
 * 还可以是大写字母+大写字母，如"DC"。不能是其他形式
 * 现给定一个字符串str，str一定是若干个新类型字符正确组合的结果，比如"eaCCBi"，由新类型字符"e"、"a"、"CC"、"Bi"拼成。
 * 再给定一个整数k，代表str中的位置。请返回被k指中的新类型字符。
 * 举例：
 * str="aaABCDEcBCg"
 * 1.k=7，指向的是'c'，最后返回"Ec"；
 * 2.k=4，指向的是'C'，最后返回"CD"；
 * 3.k=10，指向的是'g'，最后返回"g"；
 */
public class NewType {

    /**
     * 1.最普通的方法是：
     * 从str[0]开始，根据规则依次划分出新类型字符，到k位置的时候就能知道它指向的新类型字符是什么。
     * <p>
     * 2.普通方法需要从头遍历，其实我们不需要遍历那么多。
     * 首先观察新类型字符的定义，我们可以发现它有两个特点：
     * a.小写字母一定作为新类型字符的结尾出现，不管是单独一个，还是和大写字母一起出现；
     * b.大写字母不能单独出现，必须和小写字母组合或大写字母组合出现；
     * <p>
     * 根据特点a，我们发现不用从头开始遍历，只用从k位置向左，遍历到第一次出现小写字母的位置，因为它一定是一个新类型字符的结束。
     * 也就是说，我们要从k位置开始，一直向左统计连续出现的大写字母，假设统计结果为num个大写字母。
     * i.如果num是偶数，那么连续偶数个大写字母，只能两两组合成num/2个新类型字符。
     * 此时str[k]如果是小写字母，那么只能是它自己作为一个新类型字符，返回str[k]；
     * 而如果str[k]是大写字母，那么它肯定不能单独组成新类型字符，所以返回str[k..k+1]
     * j.如果num是奇数，那么前num-1个大写字母就两两组合成新类型字符，最后一个大写字母不能单独组合，
     * 所以不管str[k]是大写还是小写，都必须与这个最后的大写字母组合，所以返回str[k-1..k]
     */
    public String getNewChar(String str, int k) {
        if (str == null || str.length() == 0 || k < 0 || k >= str.length()) return "";
        char[] ch = str.toCharArray();
        int num = 0;
        int i = k - 1;
        while (i >= 0 && Character.isUpperCase(ch[i--])) {
            num++;
        }

        if ((num & 1) == 0) {
            // num为偶数
            if (Character.isUpperCase(ch[k])) {
                return str.substring(k, k + 2);
            } else {
                return String.valueOf(ch[k]);
            }
        } else {
            // num为奇数
            return str.substring(k - 1, k + 1);
        }
    }

    public static void main(String[] args) {
        NewType type = new NewType();
        String str = "aaABCDEcBCg";
        System.out.println(type.getNewChar(str, 7));
        System.out.println(type.getNewChar(str, 4));
        System.out.println(type.getNewChar(str, 10));
    }
}
