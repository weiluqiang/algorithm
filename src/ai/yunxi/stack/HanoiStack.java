package ai.yunxi.stack;

import ai.yunxi.stack.pojo.HanoiAction;
import ai.yunxi.stack.pojo.HanoiActionRecord;

import java.util.Stack;

/*
 * 用栈来求解汉诺塔问题
 * <p>
 * 分析：设移动盘子数为n，为了将这n个盘子从A杆移动到C杆，可以做以下三步：
 * <p>
 * 1.以C杆为中介，从A杆将1至n-1号盘移至B杆；
 * <p>
 * 2.将A杆中剩下的第n号盘移至C杆；
 * <p>
 * 3.以A杆为中介，从B杆将1至n-1号盘移至C杆。
 * <p>
 * 但实际操作中，只有第二步可直接完成，而第一、三步又成为移动的新问题。以上操作的实质是把移动n个盘子的问题转化为移动n-1个盘
 * 依据该原理，层层递推，即可将原问题转化为解决移动n-2、n-3 … 3、2，直到移动1个盘的操作
 */
public class HanoiStack {

    public static void main(String[] args) {
        HanoiStack hanoiStack = new HanoiStack();
        hanoiStack.process(3, "A", "C", "B");
        System.out.println("------------");
        hanoiStack.process2(3, "L", "R", "M");
        System.out.println("------------");
        hanoiStack.process3(3);
        System.out.println("------------");
        hanoiStack.process4(3);
    }

    /*
     * 递归算法-将这num个盘子从from移动到to
     *
     * @param num
     * @param from
     * @param to
     */
    public void process(int num, String from, String to, String mid) {
        // 移动1个盘的操作
        if (num == 1) {
            System.out.println("move 1 from " + from + " to " + to);
        } else {
            // 以to为中介，从from将1至num-1号盘移至mid
            this.process(num - 1, from, mid, to);

            // 将from中剩下的第num号盘移至to
            System.out.println("move " + num + " from " + from + " to " + to);

            // 以from为中介，将mid上的1至num-1号盘移至to
            this.process(num - 1, mid, to, from);
        }
    }

    /*
     * 递归算法2-将这num个盘子从from移动到to
     * <p>
     * 限制不能直接从from移动到to，必须经过mid
     *
     * @param num
     * @param from
     * @param to
     */
    public void process2(int num, String from, String to, String mid) {
        // 移动1个盘的操作
        if (num == 1) {
            System.out.println("move 1 from " + from + " to " + mid);
            System.out.println("move 1 from " + mid + " to " + to);
        } else {
            // 以mid为中介，从from将1至num-1号盘移至to
            this.process2(num - 1, from, to, mid);

            // 将from中剩下的第num号盘移至mid
            System.out.println("move " + num + " from " + from + " to " + mid);

            // 以mid为中介，将to上的1至num-1号盘移至from
            this.process2(num - 1, to, from, mid);

            // 将mid中剩下的第num号盘移至to
            System.out.println("move " + num + " from " + mid + " to " + to);

            // 以mid为中介，将from上的1至num-1号盘移至to
            this.process2(num - 1, from, to, mid);
        }
    }

    /*
     * 将这num个盘子从L移动到R，限制不能直接从L移动到R，必须经过M
     * <p>
     * 利用3个栈模拟整个过程：
     * <p>
     * 1.必须满足小压大原则；
     * <p>
     * 2.必须满足相邻不可逆原则；
     * <p>
     * 因为限制必须经过M，则只有4种操作：L->M，M->L，R->M，M->R，上一步L->M紧接着M->L显然不合理，故相邻不可逆
     * <p>
     * 假设某一步是M->L，则下一步不能是L->M，则只能是R->M或M->R其中一个，根据M和R栈顶的值大小可判断
     *
     * @param num
     */
    public void process3(int num) {
        Stack<Integer> L = new Stack<>();
        for (int i = num; i > 0; i--) {
            L.push(i);
        }
        System.out.println(L);

        Stack<Integer> R = new Stack<>();
        Stack<Integer> M = new Stack<>();

        int step = 1;// 记录上一步操作
        // 第一步默认L->M
        this.execute(step, L, R, M);
        while (R.size() != num) {
            int nowStep = this.getStep(step, L.isEmpty() ? num + 1 : L.peek(), R.isEmpty() ? num + 1 : R.peek(),
                    M.isEmpty() ? num + 1 : M.peek());
            this.execute(nowStep, L, R, M);
            step = nowStep;
        }
        System.out.println(R);
    }

    // 根据上一步操作返回下一步操作
    // 1:L->M，2:M->L，3:R->M，4:M->R，0表示第一步
    private int getStep(int prevStep, int l, int r, int m) {
        switch (prevStep) {
            case 1:
            case 2:
                return r < m ? 3 : 4;
            case 3:
            case 4:
                return l < m ? 1 : 2;

            default:
                return -1;
        }
    }

    // 根据step执行操作
    private void execute(int step, Stack<Integer> L, Stack<Integer> R, Stack<Integer> M) {
        int num;
        switch (step) {
            case 1:
                num = L.pop();
                M.push(num);
                System.out.println("move " + num + " from L to M");
                break;
            case 2:
                num = M.pop();
                L.push(num);
                System.out.println("move " + num + " from M to L");
                break;
            case 3:
                num = R.pop();
                M.push(num);
                System.out.println("move " + num + " from R to M");
                break;
            case 4:
                num = M.pop();
                R.push(num);
                System.out.println("move " + num + " from M to R");
                break;

            default:
                break;
        }
    }

    /*
     * 将这num个盘子从L移动到R，限制不能直接从L移动到R，必须经过M
     * <p>
     * 利用3个栈模拟整个过程
     *
     * @param num
     */
    public void process4(int num) {
        Stack<Integer> L = new Stack<>();
        Stack<Integer> R = new Stack<>();
        Stack<Integer> M = new Stack<>();
        for (int i = num; i > 0; i--) {
            L.push(i);
        }
        System.out.println(L);

        // 记录上一步操作
        // 设计到参数传递机制，必须用封装类而不能直接用枚举
        HanoiActionRecord record = new HanoiActionRecord(null);
        int step = 0;
        while (R.size() != num) {
            // 不断循环四步操作
            step += this.fStackTotStack(record, HanoiAction.MToL, HanoiAction.LToM, L, M, "L", "M");
            step += this.fStackTotStack(record, HanoiAction.LToM, HanoiAction.MToL, M, L, "M", "L");
            step += this.fStackTotStack(record, HanoiAction.MToR, HanoiAction.RToM, R, M, "R", "M");
            step += this.fStackTotStack(record, HanoiAction.RToM, HanoiAction.MToR, M, R, "M", "R");
        }
        System.out.println(step);
        System.out.println(R);
    }

    private int fStackTotStack(HanoiActionRecord record, HanoiAction preAct, HanoiAction nowAct, Stack<Integer> fStack,
                               Stack<Integer> tStack, String from, String to) {
        // null时第一步默认操作
        if (record.getAction() == null || (record.getAction() != preAct && (fStack.isEmpty() ? Integer.MAX_VALUE
                : fStack.peek()) < (tStack.isEmpty() ? Integer.MAX_VALUE : tStack.peek()))) {
            tStack.push(fStack.pop());
            System.out.println("move " + tStack.peek() + " from " + from + " to " + to);
            record.setAction(nowAct);
            return 1;
        }
        return 0;
    }
}