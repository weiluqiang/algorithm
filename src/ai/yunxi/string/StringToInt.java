package ai.yunxi.string;

/**
 * 将整数字符串转成整数值
 * 给定一个字符串str，如果str符合日常书写的整数形式，并且属于32位整数的范围，返回str所代表的整数值，否则返回0
 * 例如，str="123"，返回123
 * str="023"，因为"023"不符合日常的书写习惯，所以返回0
 * str="A23"，返回0
 * str="0"，返回0
 * str="2147483647"，返回2147483647
 * str="2147483648"，因为溢出了，所以返回0
 * str="-123"，返回-123
 */
public class StringToInt {

    /**
     * 首先检查str是否符合日常书写的整数形式：
     * 1.如果str不以"-"开头，也不以数字字符开头，例如str="A12"，返回false。
     * 2.如果str以"-"开头，但是str的长度为1，即str="-"，也返回false。
     * 如果str的长度大于1，但是"-"后面紧跟着"0"，例如str="-0"或"-012"，返回false。
     * 3.如果str以"0"开头，但是str的长度大于1，例如str="012"，返回false
     * 4.如果上面都没有返回false，接下来检查str[1..N-1]是否都是数字字符，如果有一个不是数字字符，
     * 返回false。如果都是数字字符，则说明str符合日常书写，返回true。
     */
    private boolean isValid(char[] ch) {
        if (ch[0] != '-' && (ch[0] < '0' || ch[0] > '9')) return false;
        if (ch[0] == '-' && (ch.length == 1 || ch[1] == '0')) return false;
        if (ch[0] == '0' && ch.length > 1) return false;
        for (int i = 1; i < ch.length; i++) {
            if (ch[i] < '0' || ch[i] > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * 如果str不符合日常书写的整数形式，那么直接返回0即可。如果符合，则按下面过程转换：
     * 1.生成4个变量。布尔型变量pos，表示转换的结果是负数还是正数。整型变量minQ，minQ等于Integer.MIN_VALUE/10。
     * 整型变量minR，minR等于Integer.MIN_VALUE%10。整型变量res，转换的结果，初始时res=0
     * 2.32位整型的最小值为-2147483648，最大值为2147483647。可以看出，最小值的绝对值更大，
     * 所以转换过程中的绝对值一律以负数的形式出现，然后根据pos决定最后返回什么
     * 3.如果str以"-"开头，从str[1]开始从左往右遍历str，否则从str[0]开始遍历。遍历的过程中，假设当前字符为a，
     * 则res=res*10+(-a)。如何判断res已经溢出了呢？因为'0'-a就是当前字符所代表的的数字的负数形式，记为cur，
     * 如果res在加上cur之前，发现res已经小于minQ，那么当res加上cur之后一定会溢出。如果res在加上cur之前，
     * 发现res等于minQ，但右发现cur小于minR，那么当res加上cur之后也会溢出。溢出的时候直接返回0
     * 4.遍历后得到的res根据pos的符号决定返回值。如果pos为true，则应返回正，否则应返回负。
     * 如果res正好是32位整数的最小值，同时又有pos为true，说明溢出，直接返回0
     */
    public int toInt(String str) {
        if (str == null || str.length() == 0) return 0;
        char[] ch = str.toCharArray();
        if (!isValid(ch)) {
            return 0;
        }
        boolean pos = ch[0] != '-';
        int minQ = Integer.MIN_VALUE / 10;
        int minR = Integer.MIN_VALUE % 10;
        int res = 0;
        int cur;
        for (int i = pos ? 0 : 1; i < ch.length; i++) {
            cur = '0' - ch[i];
            if (res < minQ || (res == minQ && cur < minR)) {
                return 0;
            }
            res = res * 10 + cur;
        }
        if (pos && res == Integer.MIN_VALUE) {
            return 0;
        }
        return pos ? -res : res;
    }

    public static void main(String[] args) {
        StringToInt toInt = new StringToInt();
        System.out.println(toInt.toInt("123"));
        System.out.println(toInt.toInt("023"));
        System.out.println(toInt.toInt("A12"));
        System.out.println(toInt.toInt("0"));
        System.out.println(toInt.toInt("2147483647"));
        System.out.println(toInt.toInt("2147483648"));
        System.out.println(toInt.toInt("-123"));
    }
}
