package ai.yunxi.other;

import java.util.Arrays;

/**
 * KMP算法
 * KMP算法是由Donald Knuth、Vaughan Pratt和James H.Morris于1977年联合发明的
 * 可以在O(N+M)的时间复杂度内，解决字符串的匹配问题
 * KMP算法的讲解网上有很多，最主要的逻辑就是找到模式字符串p的next数组，根据next数组的值移动指针位置
 */
public class KMP {

    // 普通匹配算法，对两个字符串都进行循环遍历，时间复杂度O(N*M)
    public int normalMatch(String s, String p) {
        int sLen = s.length();
        int pLen = p.length();
        int i = 0;
        int j = 0;
        while (i < sLen && j < pLen) {
            if (s.charAt(i) == p.charAt(j)) {
                // 如果当前字符匹配成功，则i++，j++
                i++;
                j++;
            } else {
                // 如果失配，令i = i - (j - 1)，j = 0
                i = i - j + 1;
                j = 0;
            }
        }
        // 匹配成功，返回模式串p在文本串s中的位置，否则返回-1
        if (j == pLen)
            return i - j;
        else
            return -1;
    }

    /**
     * KMP字符串匹配算法，查找字符串p，在字符串s中的匹配到的位置
     * 其中字符串p被称作模式字符串
     */
    public int kmpSearch(String s, String p, int[] next) {
        int i = 0;
        int j = 0;
        int sLen = s.length();
        int pLen = p.length();
        while (i < sLen && j < pLen) {
            // 如果j = -1，或者当前字符匹配成功（即S[i] == P[j]），都令i++，j++
            if (j == -1 || s.charAt(i) == p.charAt(j)) {
                i++;
                j++;
            } else {
                // 如果j != -1，且当前字符匹配失败（即S[i] != P[j]），则令 i 不变，j = next[j]
                // next[j]即为j所对应的next值
                j = next[j];
            }
        }
        if (j == pLen)
            return i - j;
        else
            return -1;
    }

    public int kmpSearch(String s, String p) {
        int[] next = getOptimalNext(p);
        return kmpSearch(s, p, next);
    }

    // 获取模式字符串p的next数组，可以采用递归方式和循环的方式
    // 由于查找next数组的逻辑，本身就是字符串p的一种自匹配，所以也适用于KMP算法，与上面的kmpSearch很相似
    public int[] getNext(String p) {
        int pLen = p.length();
        int[] next = new int[pLen];
        next[0] = -1;
        int k = -1;
        int j = 0;
        while (j < pLen - 1) {
            // p[k]表示前缀，p[j]表示后缀
            if (k == -1 || p.charAt(j) == p.charAt(k)) {
                ++k;
                ++j;
                next[j] = k;
            } else {
                k = next[k];
            }
        }
        return next;
    }

    // 优化后的获取next数组的方法
    // 在循环的过程中，当p[j]!=p[k]时，我们令k=next[k]
    // 如果p[k]=p[next[k]]，那么显然有p[j]!=p[next[k]]，会接着进入next[next[k]]
    // 所以当出现p[k]=p[next[k]]时，直接令k=next[next[k]]
    public int[] getAdvanceNext(String p) {
        int pLen = p.length();
        int[] next = new int[pLen];
        next[0] = -1;
        int k = -1;
        int j = 0;
        while (j < pLen - 1) {
            // p[k]表示前缀，p[j]表示后缀
            if (k == -1 || p.charAt(j) == p.charAt(k)) {
                ++j;
                ++k;
                next[j] = k;
            } else {
                // 因为不能出现p[k]=p[next[k]]，所以当出现时，令k=next[next[k]]
                int n = next[k];
                while (n != -1 && p.charAt(k) == p.charAt(n)) {
                    n = next[n];
                }
                k = n;
            }
        }
        return next;
    }

    // 获取next数组的优化方法2
    // 相比于优化方法1，它直接用next[k]的值替换了next[j]的值
    // 当出现重复的时候，所得的next数组要比以前同位置的值更小，指针移动更快，遍历的次数也更少
    public int[] getOptimalNext(String p) {
        int pLen = p.length();
        int[] next = new int[pLen];
        next[0] = -1;
        int k = -1;
        int j = 0;
        while (j < pLen - 1) {
            // p[k]表示前缀，p[j]表示后缀
            if (k == -1 || p.charAt(j) == p.charAt(k)) {
                ++j;
                ++k;
                // 与第一次优化的算法不同，第一次采用while循环来找p[k]!=p[next[k]]的值，
                // 因为重复可能不只存在一次，有可能出现p[k]=p[next[k]]=p[next[next[k]]]...的情况
                // 本解法看起来也需要循环来，但其实不需要，假如存在p[k]=p[next[k]]=p[next[next[k]]]
                // 那么在之前的逻辑中，必然有p[next[k]]=p[next[next[k]]]，
                // 当时已经令next[k]=next[next[k]]，所以此时只要k=next[k]，那么必然k=next[next[k]]
                // 因此next[j]里保存的，一直都是重复出现时，最初位置的那个值
                if (p.charAt(j) != p.charAt(k))
                    next[j] = k;
                else
                    // 因为不能出现p[j]=p[next[j]]，所以当出现时需要继续递归，k=next[k]=next[next[k]]
                    next[j] = next[k];
            } else {
                k = next[k];
            }
        }
        return next;
    }

    public static void main(String[] args) {
        KMP kmp = new KMP();
        String p = "AAADAAAA";
        System.out.println(Arrays.toString(kmp.getNext(p)));
        System.out.println(Arrays.toString(kmp.getAdvanceNext(p)));
        String p1 = "abababab";
        System.out.println(Arrays.toString(kmp.getNext(p1)));
        System.out.println(Arrays.toString(kmp.getOptimalNext(p1)));

        String sp1 = "cdfababababa";
        System.out.println(kmp.normalMatch(sp1, p1));
        System.out.println(kmp.kmpSearch(sp1, p1, kmp.getNext(p1)));
        System.out.println(kmp.kmpSearch(sp1, p1, kmp.getAdvanceNext(p1)));
        System.out.println(kmp.kmpSearch(sp1, p1, kmp.getOptimalNext(p1)));
    }
}
