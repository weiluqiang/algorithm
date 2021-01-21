package ai.yunxi.string;

/**
 * 判断两个字符串是否互为变形词
 * 给定两个字符串str1和str2，如果str1和str2中出现的字符种类一样且每种字符出现的次数也一样，
 * 那么str1和str2互为变形词。请实现函数判断两个字符串是否互为变形词
 * 例如：str1="123"，str2="231"，返回true；str1="123"，str2="2331"，返回false
 */
public class Anagram {

    /**
     * 如果str1和str2长度不同，直接返回false。如果长度相同，假设出现字符编码的值在0~255之间，
     * 那么先申请一个长度为256的整型数组map，map[a]=b代表字符编码为a的字符出现了b次，初始值都是0。
     * 然后遍历字符串str1，统计每种字符出现的次数，出现字符a了就map[a]++。再遍历字符串str2，
     * 每遍历到一个字符再把次数减下来，如果减少之后小于0，直接返回false。
     * 如果遍历完str2也没有出现负值，则可以返回true。
     * <p>
     * 如果字符的种类很多，可以用哈希表代替数组，整体过程不变。
     * 如果出现的字符种类为M，字符串的长度为N，那么方法的时间复杂度为O(N)，额外空间复杂度为O(M)
     */
    public boolean isAnagram(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() != str2.length() || str1.length() == 0) {
            return false;
        }
        char[] ch1 = str1.toCharArray();
        char[] ch2 = str2.toCharArray();
        int[] map = new int[256];
        for (char c1 : ch1) {
            map[c1]++;
        }
        for (char c2 : ch2) {
            if (map[c2]-- == 0) {
                return false;
            }
        }
        return !str1.equals(str2);
    }

    public static void main(String[] args) {
        Anagram anagram = new Anagram();
        System.out.println(anagram.isAnagram("123", "321"));
        System.out.println(anagram.isAnagram("123", "123"));
        System.out.println(anagram.isAnagram("124", "321"));
    }
}
