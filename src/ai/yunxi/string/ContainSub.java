package ai.yunxi.string;

/**
 * 最小包含子串的长度
 * 给定字符串str1和str2，求str1的子串中含有str2所有字符的最小子串长度
 * 举例：
 * 1.str1="abcde"，str2="ac"。因为"abc"包含str2的所有字符，并且在所有满足这一条件的str1的子串中，"abc"是最短的，所有返回3
 * 2.str1="12345"，str2="344"。最小包含子串不存在，返回0
 */
public class ContainSub {

    /**
     * 如果str1的长度为N，str2的长度为M，本方法的时间复杂度为O(N)
     * 如果str1或str2为空，则直接返回0。如果N小于M，那么必然不能全部包含，也返回0
     * 接下来讨论一般情况，现在以str1="adabbca"，str2="acb"来举例说明整个过程：
     * <p>
     * 1.先遍历str2，生成哈希表map，里面的记录如下：{'a':1,'c':1,'b':1}
     * 每条记录的意义是：对于当前这个key字符，str1字符串目前还欠str2字符串value个。
     * <p>
     * 2.定义如下4个变量：
     * a.left：遍历str1的过程中，str1[left..right]表示被框住的子串，left表示这个子串的左边界，初始时left=0
     * b.right：right表示被框住子串的右边界，初始时right=0
     * c.match：表示对所有的字符来说，str1[left..right]目前一共欠str2多少个
     * 对于本例子，初始时match=3，也就是欠1个'a'、1个'c'、1个'b'
     * d.min：最终想要的最小包含子串长度，初始时为Integer.MAX_VALUE
     * <p>
     * 3.接下来开始通过right变量从左到右遍历str1
     * a.right=0，str1[0]='a'。在map中把key为'a'的值减1，减完后变成('a',0)。减完之后为0，说明减之前大于0，
     * 那么str1归还了1个'a'，match值也要减1，表示对str2的所有字符来说，str1目前归还了1个。
     * 目前map={'a':0,'c':1,'b':1}，left=0，right=0，match=2，min=Integer.MAX_VALUE
     * b.right=1，str1[0]='d'。此时map中没有'd'，就默认为0，减1之后变成('d',-1)，表示'd'字符str1多归还了1个。
     * 此时value等于-1，说明当前这个字符是str2不需要的，所以match不变。
     * 目前map={'a':0,'c':1,'b':1,'d':-1}，left=0，right=1，match=2，min=Integer.MAX_VALUE
     * c.right=2，str1[0]='a'。在map中，把key为'a'的值减1，变成('a',-1)。减之后value等于-1，
     * 说明减之前str1根本就不欠str2当前的字符，是多归还的，故match不变
     * 目前map={'a':-1,'c':1,'b':1,'d':-1}，left=0，right=2，match=2，min=Integer.MAX_VALUE
     * d.right=3，str1[0]='b'。把map中('b',1)变为('b',0)，减之后value为0，说明当前字符'b'归还有效，故match减1
     * 目前map={'a':-1,'c':1,'b':0,'d':-1}，left=0，right=3，match=1，min=Integer.MAX_VALUE
     * e.right=4，str1[0]='b'。把map中('b',0)变为('b',-1)，当前字符'b'归还无效，match不变
     * 目前map={'a':-1,'c':1,'b':-1,'d':-1}，left=0，right=4，match=1，min=Integer.MAX_VALUE
     * f.right=5，str1[0]='c'。('c',1)变为('c',0)，减之后为0，字符'c'归还有效，故match减1
     * 目前map={'a':-1,'c':0,'b':-1,'d':-1}，left=0，right=5，match=0，min=Integer.MAX_VALUE
     * 此时match第一为0，说明str1把需要归还的字符都归还了，此时被框住的子串也就是str1[0..5]，肯定是包含str2所有字符的。
     * 但是当前被框住的子串是以位置5结尾的情况下最短的吗？不一定，因为有些字符是有归还多余的，所以还要进行下面的过程：
     * <p>
     * f-1.left开始向右移动，left=0，str1[0]='a'，map中'a'的记录为('a',-1)，当前value=-1，说明即使str1拿回这个字符，
     * 也不会欠str2.所以拿回来，令记录变为('a',0)，left++
     * f-2.left=1，str1[1]='d'，当前记录为('d',-1)，value=-1，同样也可以拿回来'd'，记录变为('d',0)，left++
     * f-3.left=1，str1[1]='a'，当前记录为('a',0)，value=0，此时str1若要拿回字符'a'，就要亏欠str2，
     * 所以此时left停止移动，str1[2..5]就是以位置5结尾的情况下，最小包含的字符子串，此时min更新为4
     * <p>
     * 上面这一步揭示了整个解法最关键的逻辑，先通过right向右扩，让str2所有的字符都被归还。
     * 都还完时，被框住的子串肯定是包含str2全部字符的。然后通过left向右缩来看被框住的子串能不能变得更短。
     * 至此，以位置5结尾的最短包含子串已经找到，同时它也是以位置left为开始的最短包含子串。
     * 之后如果有更短的子串，也不会从位置left开始，至少要从left+1位置开始。
     * 所以此时，我们接着把当前的字符'a'归还，把map中的记录变成('a',1)，令left++，match++，
     * 表示现在str[3..5]又开始欠str2字符了，接着我们继续向右移动right
     * 目前map={'a':1,'c':0,'b':-1,'d':-1}，left=3，right=5，match=1，min=4
     * <p>
     * g.right=6，str1[0]='a'。记录('a',1)变为('a',0)，减之后value为0，归还有效，令match减1。match又一次变为0了，
     * 再进入left向右缩的过程：
     * g-1.left=3，str1[3]='b'，可以拿回当前字符，变为('b',0)，left++
     * g-2.left=4，str1[3]='b'，当前记录为('b',0)，不能拿回'b'，left停止，此时str1[4..6]就是以位置6结尾的最短包含子串。
     * 令min更新为3，接着跟上一步一样，继续移动left，记录变为('b',1)，left++，match++，表示str1[5..6]又开始欠str2字符了。
     * 此时map={'a':0,'c':0,'b':1,'d':-1}，left=5，right=6，match=1，min=3
     * h.right=7，已经超出了str1的范围，遍历结束。
     * <p>
     * 4.如果遍历结束后min仍然为Integer.MAX_VALUE，说明没有找到包含str2全部字符的子串，所以返回0。否则返回min。
     */
    public int minLength(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() < str2.length()) return 0;
        char[] ch1 = str1.toCharArray();
        char[] ch2 = str2.toCharArray();
        // 先遍历str2生成map
        int[] map = new int[256];
        for (char c : ch2) {
            map[c]++;
        }
        int left = 0;
        int right = 0;
        int match = ch2.length;
        int min = Integer.MAX_VALUE;
        // 开始遍历str1
        while (right < ch1.length) {
            map[ch1[right]]--;
            if (map[ch1[right]] >= 0) {
                match--;
            }
            if (match == 0) {
                // left向右缩
                while (map[ch1[left]] < 0) {
                    map[ch1[left++]]++;
                }
                min = Math.min(min, right - left + 1);
                // 记录完min后令left++，match++，继续右移right
                match++;
                map[ch1[left++]]++;
            }
            right++;
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    public static void main(String[] args) {
        ContainSub sub = new ContainSub();
        System.out.println(sub.minLength("AdAbbCA", "ACb"));
    }
}
