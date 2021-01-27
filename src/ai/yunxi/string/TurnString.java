package ai.yunxi.string;

/**
 * 翻转字符串
 * 给定一个字符类型的数组ch[]，请在单词间做逆序调整。只要做到单词顺序逆序即可，对空格的位置没有特别要求。
 * 例如：把ch看作字符串为"dog loves pig"，调整成"pig loves dog"
 * 把ch看作字符串为"I am a student."，调整成"student. a am I"
 * <p>
 * 补充题目：给定一个字符类型的数组ch[]和整数size，请把大小为size的左半区整体移到右半区，右半区整体移到左边。
 * 例如：把ch看作字符串为"abCDE"，size=3，调整成"DEabC"
 * 要求：如果ch的长度为N，两道题都要求时间复杂度为O(N)，额外空间复杂度为O(1)
 */
public class TurnString {

    /**
     * 原问题：首先把ch整体逆序，之后遍历ch找到每一个单词，然后把每个单词里的字符逆序即可。
     */
    public void rotate(char[] ch) {
        if (ch == null || ch.length == 0) return;
        reverse(ch, 0, ch.length - 1);
        // 遍历ch数组，找到每一个单词
        int l = -1;//记录单词左边界
        int r = -1;//记录单词右边界
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] != ' ') {
                if (i == 0 || ch[i - 1] == ' ') l = i;
                if (i == ch.length - 1 || ch[i + 1] == ' ') r = i;
            }
            if (l != -1 && r != -1) {
                reverse(ch, l, r);
                l = -1;
                r = -1;
            }
        }
    }

    // 对指定范围的字符做逆序
    private void reverse(char[] ch, int start, int end) {
        char tmp;
        while (start < end) {
            tmp = ch[start];
            ch[start] = ch[end];
            ch[end] = tmp;
            start++;
            end--;
        }
    }

    /**
     * 补充问题方法一：先把ch[0..size-1]部分逆序，再把ch[size..N-1]部分逆序，最后把ch整体逆序即可。
     */
    public void turn(char[] ch, int size) {
        if (ch == null || size <= 0 || size >= ch.length) return;
        reverse(ch, 0, size - 1);
        reverse(ch, size, ch.length - 1);
        reverse(ch, 0, ch.length - 1);
    }

    /**
     * 补充问题方法二：举例说明，ch="12345abc"，size=5
     * 1.左部分为"12345"，右部分为"abc"，右部分的长度为3，比左部分小，所以把左部分的前3个字符与右部分交换，
     * ch变为"abc45123"，右部分小，所以右部分换过去之后不需要再移动，剩下的部分为ch[3..7]="45123"，
     * 接下来把"45"视为下一步的左部分，"123"视为下一步的右部分
     * 2.左部分为"45"，右部分为"123"，左部分的长度为2，比右部分小，所以把右部分的后2个字符与左部分交换，
     * 交换后ch[3..7]变为"23145"，换过来的"45"不需要动，剩下的部分为ch[3..5]="231"，
     * 接下来把"23"视为下一步的左部分，"1"视为下一步的右部分
     * 3.左部分为"23"，右部分为"1"，右部分的长度为1，比左部分小，同理按照步骤1交换右部分和左部分的前1个字符，
     * ch[3..5]变为"132"，"1"不需要动，接下来看ch[4..5]="32"
     * 4.左部分为"3"，右部分为"2"，一旦发现左部分和右部分的长度一样，那么左部分和右部分交换即可，
     * ch[4..5]变为"23"，整个过程完毕，此时ch="abc12345"。
     */
    public void turn1(char[] ch, int size) {
        if (ch == null || size <= 0 || size >= ch.length) return;
        int start = 0;
        int end = ch.length - 1;
        int lp = size;
        int rp = ch.length - size;
        int s = Math.min(lp, rp);
        while (true) {
            exchange(ch, start, end, s);
            if (lp == rp) {
                // 左右部分长度相等，交换完即可结束
                break;
            } else if (lp > rp) {
                // 左部分较长，右部分较短，则交换之后较短的右部分处于开始的s个位置，下一步交换这s个位置之后的位置
                start += s;
                lp = lp - rp;
            } else {
                // 右部分较长，左部分较短，则交换之后较短的左部分处于最后的s个位置，下一步交换这s个位置之前的位置
                end -= s;
                rp = rp - lp;
            }
            s = Math.min(lp, rp);
        }
    }

    // 交换数组中的左右大小为size的两部分
    public void exchange(char[] ch, int start, int end, int size) {
        // 交换的左部分从start开始，大小为size
        // 右部分从rs开始到end，大小也为size
        int rs = end - size + 1;
        char tmp;
        while (size-- > 0) {
            tmp = ch[start];
            ch[start] = ch[rs];
            ch[rs] = tmp;
            start++;
            rs++;
        }
    }

    public static void main(String[] args) {
        TurnString turn = new TurnString();
        char[] ch = "dog loves pig".toCharArray();
        turn.rotate(ch);
        System.out.println(String.valueOf(ch));
        ch = "I am a student.".toCharArray();
        turn.rotate(ch);
        System.out.println(String.valueOf(ch));
        ch = "abCDE".toCharArray();
        turn.turn(ch, 3);
        System.out.println(String.valueOf(ch));
        turn.turn1(ch, 3);
        System.out.println(String.valueOf(ch));
    }
}
