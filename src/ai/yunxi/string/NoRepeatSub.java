package ai.yunxi.string;

import java.util.Arrays;

/**
 * 找到字符串的最长无重复字符子串
 * 给定一个字符串str，返回str的最长无重复字符子串的长度
 * 举例：
 * 1.str="abcd"，返回4
 * 2.str="aabcb"，最长无重复字符子串的为"abc"，返回3
 * 如果str的长度为N，请实现时间复杂度为O(N)的方法
 */
public class NoRepeatSub {

    /**
     * 如果字符串的长度为N，字符编码范围是M，本方法可做到时间复杂度O(N)，额外的空间复杂度O(M)、
     * 1.先申请几个变量：
     * 哈希表map，key表示某个字符，value为这个字符最近一次出现的位置；
     * 整型变量pre，如果当前遍历到字符str[i]，pre表示在必须以str[i-1]结尾的情况下，最长无重复字符子串的开始位置的前一个位置，初始时pre=-1。
     * 整型变量len，记录以每一个字符结尾的情况下，最长无重复字符子串长度的最大值，初始时len=0
     * 然后从左到右遍历str，假设遍历到str[i]，接下来求必须以str[i]结尾的情况下，最长无重复字符子串的长度。
     * 2.map(str[i])的值表示之前的遍历中，最近一次出现str[i]的位置，假设在a位置。
     * 想要求以str[i]结尾的最长无重复子串，a位置必然不能包含进来，因为str[a]=str[i]。
     * 3.根据pre的定义，pre+1表示在必须以str[i-1]结尾的情况下，最长无重复子串的开始位置，
     * 也就是说，以str[i-1]结尾的最长无重复子串是向左扩展到pre位置停止的。
     * 4.如果pre位置在a位置的左边，因为str[a]不能包含进来，而str[a+1..i-1]都是不重复的，
     * 所以以str[i]结尾的最长无重复子串就是str[a+1..i]。如果pre位置在a位置的右边，
     * 以str[i-1]结尾的最长无重复子串是向左扩展到pre位置停止的，所以以str[i]结尾的最长无重复子串
     * 向左扩展到pre位置也必然会停止，而且str[pre+1..i-1]这一段上肯定不含有str[i]，
     * 所以以str[i]结尾的最长无重复子串就是str[pre+1..i]。
     * 5.计算完长度之后，pre位置和a位置哪一个在右边，就作为新的pre值。然后计算下一个位置的字符，
     * 整个过程中求得的所有长度的最大值，用len记录下返回即可。
     */
    public int maxLength(String str) {
        if (str == null || str.length() == 0) return 0;
        char[] ch = str.toCharArray();
        int[] map = new int[256];
        Arrays.fill(map, -1);
        int len = 0;
        int pre = -1;
        for (int i = 0; i < ch.length; i++) {
            pre = Math.max(pre, map[ch[i]]);
            map[ch[i]] = i;
            len = Math.max(len, i - pre);
        }
        return len;
    }

    public static void main(String[] args) {
        NoRepeatSub sub = new NoRepeatSub();
        System.out.println(sub.maxLength("noRepeatSub"));
    }
}
