package ai.yunxi.string;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 公式字符串求值
 * 给定一个字符串str，str表示一个公式，公式里可能有整数、加减乘除符号和左右括号，返回公式的计算结果
 * 例如：str="48*((70-65)-43)+8*1"，返回-1816
 * 说明：
 * 1.可以认为给定的字符串一定是正确的公式
 * 2.如果是负数，就需要用括号括起来，比如"4*(-3)"。但如果公式作为公式的开头或括号部分的开头，则可以没有括号
 * 3.不用考虑计算中会发生溢出的情况
 * <p>
 * 由于括号的优先级最高，因此必须先计算括号中的部分，返回结果给上一级，因此考虑采用递归的方法。
 * 假设递归方法就是value，它的具体过程解释如下：
 * 从左到右遍历str，开始遍历或遇到'('时就进入递归过程。当发现str遍历完或遇到')'时，递归过程就结束，
 * 把当前公式的计算结果和str遍历到的位置返回给上一层，上一层拿到括号内的计算结果，接着计算当前公式。
 * 既然递归过程遇到'('时就交给下一层递归过程处理，自己只接受括号中的公式字符子串的结果，
 * 那么对所有的递归过程来说，都可以看做不含有括号的公式进行计算
 */
public class Formula {

    public int getValue(String str) {
        return value(str.toCharArray(), 0)[0];
    }

    /**
     * 递归计算的过程
     *
     * @param ch 字符串转成的char数组
     * @param i  当前遍历到的位置
     * @return int[0]表示当前子串的结果，int[1]表示当前子串的结束位置
     */
    private int[] value(char[] ch, int i) {
        // 由于要计算结果必须先获得完整的式子，所以将式子的数值、符号都依次记录在队列中，等得到完整的式子后再取出计算
        Deque<String> deque = new LinkedList<>();
        // 当前从字符串中解析出的数值
        int num = 0;
        // 下层递归方法返回的结果
        int[] res;
        // 直到公式结尾或')'处，当前公式子串才算结束
        while (i < ch.length && ch[i] != ')') {
            if (ch[i] >= '0' && ch[i] <= '9') {
                // 是数值型字符，则记入到num中
                // ch[i] - '0'正好等于Integer.parseInt(ch[i]);
                num = num * 10 + ch[i++] - '0';
            } else if (ch[i] != '(') {
                // 遇到加减乘除符号，则计算本次数值num结束，将数值和符号都存到队列中
                addNum(deque, num);
                deque.addLast(String.valueOf(ch[i++]));
                // 数值num归0，进入下一次数值计算
                num = 0;
            } else {
                // 遇到了左括号'('，则进入下一次递归
                res = value(ch, i + 1);
                // 记录括号中的结果，由于括号后面必然是加减乘除符号，下一次循环就会存入队列
                num = res[0];
                i = res[1] + 1;
            }
        }
        // 循环结束后最后一个数值num肯定还未放入队列
        addNum(deque, num);
        return new int[]{getNum(deque), i};
    }

    // 把数值num存入队列
    // 由于公式中乘除的优先级高于加减，因此整个过程要先计算乘除的结果，把乘除的结果作为一个数值存到队列中
    // 而整个式子的加减结则到式子结束时再统一从左往右计算
    private void addNum(Deque<String> deque, int num) {
        if (!deque.isEmpty()) {
            // 队列中数值的上一个元素必然是符号，取出队尾的符号
            String top = deque.pollLast();
            if (top.equals("+") || top.equals("-")) {
                // 如果是加减运算，则将符号放会队列，先不计算
                deque.addLast(top);
            } else if (top.equals("*") || top.equals("/")) {
                // 如果是乘除运算，则立即取出上一个数值，计算此式子的结果，并把结果作为一个数值放入队列
                if (!deque.isEmpty()) {
                    int pre = Integer.parseInt(deque.pollLast());
                    num = top.equals("*") ? pre * num : pre / num;
                }
            }
        }
        deque.addLast(String.valueOf(num));
    }

    // 最后统一进行公式的加减运
    // 由于乘除运算每次遇到都计算过了，因此此时队列里只有加减运算
    private int getNum(Deque<String> deque) {
        // 计算结果
        int res = 0;
        // 当前取出的字符串
        String cur;
        // 当前的计算逻辑，表示加或减
        boolean add = true;
        while (!deque.isEmpty()) {
            cur = deque.pollFirst();
            if (cur.equals("+")) {
                add = true;
            } else if (cur.equals("-")) {
                add = false;
            } else {
                int num = Integer.parseInt(cur);
                res = add ? res + num : res - num;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Formula formula = new Formula();
        System.out.println(formula.getValue("48*((70-65)-43)+8*1"));
        System.out.println(formula.getValue("3+1*4"));
        System.out.println(formula.getValue("-3+(1*4)"));
    }
}
