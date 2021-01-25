package ai.yunxi.string;

/**
 * 判断两个字符串是否互为旋转词
 * 如果一个字符串str，把字符串str前面任意的部分挪到后面形成的字符串叫做str的旋转词。比如，str="12345"，
 * str的旋转词有"12345"、"23451"、"34512"、"45123"和"51234"。给定两个字符串a和b，请判断a和b是否互为旋转词。
 * 例如：a="cd1ab2"，b="ab2cd1"，返回true
 * a="1ab2"，b="ab12"，返回false
 * a="2ab1"，b="ab12"，返回true
 * 要求：
 * 如果a和b长度不一样，那么a和b必然不互为旋转词，可以直接返回false。
 * 当a和b长度一样，都为N时，要求解法的时间复杂度为O(N)
 */
public class RevolveWord {

    /**
     * 本题的解法非常简单，如果a和b的长度不一样，那么他们不可能互为旋转词。如果a和b的长度一样，先生成一个大字符串b2，
     * b2是两个字符串b拼在一起的结果，即String b2 = b + b，然后看b2中是否包含字符串a，如果包含，
     * 说明字符串a和b互为旋转词，否则不互为旋转词。这是为什么呢？因为如果一个字符串b长度为N，
     * 那么在通过b生成的b2中，任意长度为N的子串，都是b的旋转词，并且b2中包含字符串b的所有旋转词。
     */
    public boolean isRevolve(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) return false;
        String b2 = b + b;
        return getIndexOf(b2, a) != -1;
    }

    // 判断字符串a是否包含整个字符串b
    // 要想整个过程的时间复杂度为O(N)，那么判断是否包含的逻辑的时间复杂度就必须为O(N)
    // 这个过程可以参考KMP算法，这里采用的是Sunday算法
    private int getIndexOf(String a, String b) {
        if (a.length() < b.length()) return -1;
        char[] ch1 = a.toCharArray();
        char[] ch2 = b.toCharArray();
        int i = 0;
        int j = 0;
        int tmp;
        int idx;
        while (i < ch1.length) {
            if (ch1[i] == ch2[j]) {
                // 如果此时已经是b的最后1位字符，则a包含b
                if (j == ch2.length - 1) {
                    return i - ch2.length + 1;
                }
                // 否则继续匹配下一位
                i++;
                j++;
            } else {
                // 如果字符不相等，则看b最后的字符的后一位，对应的位置tmp在a上的字符是否在b中
                tmp = ch2.length - j + i;
                if (tmp > ch1.length - 1) {
                    return -1;
                }
                idx = indexOf(ch1[tmp], ch2);
                if (idx == -1) {
                    // 如果tmp位置的字符不在b上，则下次循环从tmp+1位置开始判断
                    i = tmp + 1;
                } else {
                    // 如果tmp位置的字符在b上也有，则移动字符串b使这两个位置对齐
                    // 对齐后，i和j移动到b的首位，开始继续往后判断
                    i = tmp - idx;
                }
                j = 0;
            }
        }
        return -1;
    }

    // 判断字符c在字符数组ch中的位置
    // Sunday算法中，要求此时字符串尽量小的移动，因此采取从右边开始匹配
    // ch如果包含了不只一个c字符，那么此方法应该返回尽量靠右的那个位置
    // 这样返回的index会尽量大，i的值会尽量小，字符串移动的步数也会小
    private int indexOf(char c, char[] ch) {
        for (int i = ch.length - 1; i >= 0; i--) {
            if (c == ch[i]) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        RevolveWord revolve = new RevolveWord();
        System.out.println(revolve.isRevolve("cd1ab2", "ab2cd1"));
        System.out.println(revolve.isRevolve("1ab2", "ab12"));
        System.out.println(revolve.isRevolve("2ab1", "ab12"));
    }
}
