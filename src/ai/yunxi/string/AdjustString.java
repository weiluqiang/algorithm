package ai.yunxi.string;

/**
 * 字符串的调整和替换
 * 给定一个字符类型的数组ch[]，ch右半区全是空字符，左半区不含有空字符，现在想将左半区中所有的空格字符替换成"%20"，
 * 假设ch右半区足够大，可以满足替换所需要的空间，请完成替换函数。
 * 例如：如果把ch的左半区看成字符串，为"a b  c"，替换后ch的左半区为"a%20b%20%20c"。
 * 要求：替换函数的时间复杂度为O(N)，额外空间复杂度为O(1)
 * <p>
 * 补充题目：
 * 给定一个字符类型的数组ch[]，其中只含有数字字符和"*"字符。现在想把所有的"*"字符挪到ch的左边，
 * 数字字符挪到ch的右边。请完成调整函数。
 * 例如：如果把ch看成字符串，为"12**345"。调整后ch为"**12345"。
 * 要求：1.调整函数的时间复杂度为O(N)，额外空间复杂度为O(1)
 * 2.不得改变数字字符从左到右出现的顺序
 */
public class AdjustString {

    /**
     * 原问题：遍历一遍可以得到两个信息，ch的左半区有多大，记为len，左半区的空格数有多少，记为num，
     * 那么可知空格字符被"%20"替换后，长度将是len+2*num。接下来从左半区的最后一个字符开始倒着遍历，
     * 同时将字符复制到新长度最后的位置，并依次向左倒着复制，这样就可以得到替换后的ch数组。
     */
    public void replace(char[] ch) {
        if (ch == null || ch.length == 0) return;
        int num = 0;
        int len = 0;
        for (; len < ch.length && ch[len] != 0; len++) {
            if (ch[len] == ' ') {
                num++;
            }
        }
        int j = len + 2 * num - 1;
        for (int i = len - 1; i >= 0; i--) {
            if (ch[i] == ' ') {
                ch[j--] = '0';
                ch[j--] = '2';
                ch[j--] = '%';
            } else {
                ch[j--] = ch[i];
            }
        }
    }

    /**
     * 补充问题：依然从右向左倒着复制，遇到数字字符则直接复制，遇到"*"则不复制。
     * 当把数字字符复制完，把左半区全部设置成"*"即可。
     */
    public void adjust(char[] ch) {
        if (ch == null || ch.length == 0) return;
        int j = ch.length - 1;
        for (int i = ch.length - 1; i >= 0; i--) {
            if (ch[i] != '*') {
                ch[j--] = ch[i];
            }
        }
        while (j >= 0) {
            ch[j--] = '*';
        }
    }

    public static void main(String[] args) {
        AdjustString adjust = new AdjustString();
        char[] ch = {'a', ' ', 'b', ' ', ' ', 'c', 0, 0, 0, 0, 0, 0};
        adjust.replace(ch);
        System.out.println(String.valueOf(ch));
        char[] ch1 = {'1', '2', '*', '*', '3', '4', '*', '5'};
        adjust.adjust(ch1);
        System.out.println(String.valueOf(ch1));
    }
}
