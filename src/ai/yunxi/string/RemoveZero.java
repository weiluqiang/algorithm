package ai.yunxi.string;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 去掉字符串中连续出现k个0的子串
 * 给定一个字符串str和一个整数k，如果str中正好有连续的k个'0'字符出现时，
 * 把k个连续的'0'字符去掉，返回处理后的字符串。
 * 例如：
 * str="A00B"，k=2，返回"AB"
 * str="A0000B000"，k=3，返回"A0000B"
 */
public class RemoveZero {

    /**
     * 本题能做到时间复杂度O(N)，额外空间复杂度O(1)。解法的关键是如何从左到右遍历str时，
     * 将正好有连续的k个'0'找到，然后把字符'0'去掉。去掉过程如下：
     * 1.生成两个变量，整型变量count，表示目前连续个'0'的数量；整型变量start，
     * 表示连续个'0'出现的开始位置。初始时，count=0，start=-1
     * 2.从左到右遍历str，假设遍历到i位置的字符为ch，不同的字符有不同的处理逻辑：
     * 2-1.如果ch是字符'0'，令start = start==-1?i:start，表示如果start等于-1，
     * 说明之前处在没发现连续'0'的阶段，那么令start=i，表示连续的'0'从i位置开始，
     * 如果start不等于-1，说明正处在发现连续'0'的阶段，那么start不变。令count++。
     * 2-2.如果ch不是字符'0'，是去掉连续'0'的时刻。首先看此时count是否等于k，如果等于，说明可以去掉。
     * 如果不等于，则不能去掉。最后令count=0，start=-1。
     * 2-3.循环完毕后，再检查一下count是否等于k，防止最后一组连续k个'0'没有去掉的情况
     */
    public String remove(String str, int k) {
        if (str == null || str.length() == 0 || k < 1) return str;
        char[] ch = str.toCharArray();
        int count = 0;
        int start = -1;
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == '0') {
                count++;
                if (start == -1) start = i;
            } else {
                if (count == k) {
                    while (count-- != 0) {
                        ch[start++] = 0;
                    }
                }
                count = 0;
                start = -1;
            }
        }
        if (count == k) {
            while (count-- != 0) {
                ch[start++] = 0;
            }
        }
        return String.valueOf(ch);
    }

    // 额外生成一个双端队列，完全去掉最后的空字符
    public String remove1(String str, int k) {
        if (str == null || str.length() == 0 || k < 1) return str;
        char[] ch = str.toCharArray();
        int count = 0;
        Deque<Character> deque = new LinkedList<>();
        for (char c : ch) {
            if (c == '0') {
                count++;
            } else {
                if (count == k) {
                    while (count-- != 0) {
                        deque.pollFirst();
                    }
                }
                count = 0;
            }
            deque.offerFirst(c);
        }
        if (count == k) {
            while (count-- != 0) {
                deque.pollFirst();
            }
        }
        char[] chars = new char[deque.size()];
        count = 0;
        while (!deque.isEmpty()) {
            chars[count++] = deque.pollLast();
        }
        return String.valueOf(chars);
    }

    public static void main(String[] args) {
        RemoveZero zero = new RemoveZero();
        System.out.println(zero.remove("A00B", 2));
        System.out.println(zero.remove("A0000B000", 3));
        System.out.println(zero.remove1("A00B", 2));
        System.out.println(zero.remove1("A0000B000", 3));
    }
}
