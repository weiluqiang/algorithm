package ai.yunxi.dynamicPlan;

/**
 * N皇后问题
 * N皇后问题是指在N*N的棋盘上要摆N个皇后，要求任何两个皇后不同行、不同列，也不在同一条斜线上。
 * 给定一个整数n，返回n皇后的摆法有多少种。
 * 例如：n=1，返回1
 * n=2或3，2皇后或3皇后问题怎么摆都不行，所以返回0
 * n=8，返回92
 * <p>
 * 本题是非常著名的问题，甚至可以用人工智能相关算法和遗传算法进行求解，同时可以用多线程技术达到缩短运行时间的效果。
 */
public class NQueens {

    /**
     * 在介绍最优解之前，先来介绍一个容易理解的算法：
     * 如果在(i,j)位置(第i行,第j列)放置了一个皇后，接下来哪些位置不能放皇后呢？
     * 1.整个第i行的位置都不能放置
     * 2.整个第j列的位置都不能放置
     * 3.如果位置(a,b)满足|a-i|=|b-j|，说明(a,b)与(i,j)处在同一条斜线上，也不能放置
     * 把递归过程直接设计成逐行放置皇后的方式，可以避开条件1的那些不能放置的位置。接下来用一个数组来保存已经放置的位置。
     * 假设数组为record，record[i]表示第i行皇后所在的列数。在递归计算到第i行、第j列时，查看record[0..k](k<i)的值，
     * 看是否有j相等的值，若有则说明(i,j)不能放置皇后。再看是否有|k-i|=|record[k]-j|，若有也说明(i,j)不能放置皇后。
     */
    public int num(int n) {
        if (n == 1) return 1;
        if (n < 1 || n == 2 || n == 3) return 0;
        int[] record = new int[n];
        return process(0, record, n);
    }

    private int process(int i, int[] record, int n) {
        // i==n，表示有n-1个皇后都放置了，所以有n-1行和n-1列不能放，能放的只有1个位置
        if (i == n) return 1;
        int res = 0;
        for (int j = 0; j < n; j++) {
            if (isValid(record, i, j)) {
                record[i] = j;
                res += process(i + 1, record, n);
            }
        }
        return res;
    }

    // 查看record[0..k](k<i)的值，看是否满足要求
    private boolean isValid(int[] record, int i, int j) {
        for (int k = 0; k < i; k++) {
            if (j == record[k] || Math.abs(k - i) == Math.abs(record[k] - j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 下面介绍最优解，基本过程与上面一样，但使用了位运算来加速。
     * 具体加速的递归过程中，找到每一行还有哪些位置可以放置皇后。
     * 变量upperLim表示当前行哪些位置是可以放置皇后的，1代表可以放置，0代表不能放置。
     * 8皇后问题中，初始时upperLim为00000000000000000000000011111111，即32位整数的255
     * 32皇后问题中，初始时upperLim为11111111111111111111111111111111，即32位整数的-1
     */
    public int num1(int n) {
        // 本方法中位运算的载体是int型变量，所以该方法只能计算1~32皇后问题
        if (n < 1 || n > 32) return 0;
        int upperLim = n == 32 ? -1 : (1 << n) - 1;
        return process1(upperLim, 0, 0, 0);
    }

    /**
     * 接下来介绍process1方法，先介绍每个参数：
     * 1.upperLim：已经介绍过了，而且这个变量的值在递归过程中是始终不变的
     * 2.colLim：表示递归计算到上一行为止，在哪些列上已经放置了皇后，1代表已经放置，0代表没有放置
     * 3.leftDiaLim：表示递归计算到上一行为止，因为受已放置的所有皇后的左下方斜线的影响，导致当前行不能放置皇后的位置，
     * 1代表不能放置，0代表可以放置，举个例子，如果在第0行第4列放置了皇后，它的左下方分别是第1行第3列、第2行第2列、
     * 第3行第1列、第4行第0列，从第5行开始，剩下的行都不再受第0行第4列左下方的影响，也就是说，
     * leftDiaLim每左移一次，就可以得到之前所有皇后的最下方斜线对当前行的影响
     * 4.rightDiaLim：表示递归计算到上一行为止，因为受已放置的所有皇后的右下方斜线的影响，导致当前行不能放置皇后的位置，
     * 1代表不能放置，0代表可以放置。与leftDiaLim类似，rightDiaLim每右移一次，就可以得到之前所有皇后的对当前行的影响
     * <p>
     * process1方法的返回值，代表剩余的皇后在之前皇后的影响下，有多少种合法的摆法。其中变量pos，
     * 代表当前行在colLim、leftDiaLim、rightDiaLim这三个的影响下，还有哪些位置是可以选择的，
     * 1代表可以选择，0代表不能选择。变量mostRightOne代表在pos中，最右边的1是在什么位置。
     * 然后从右到左一次筛选出pos中，可选择的位置进行递归尝试
     */
    private int process1(int upperLim, int colLim, int leftDiaLim, int rightDiaLim) {
        // colLim==upperLim，表示当前所有可放的列上，都放置了皇后，递归结束
        if (colLim == upperLim) return 1;
        int mostRightOne;
        // colLim|leftDiaLim|rightDiaLim表示综合列、左下、右下三种情况，不能放置的位置都标记成1
        // 取反之后1的位置就是能放置的，0的是不能放置的
        // 再与upperLim位与，返回的是在有效位(8皇后有效位就是低8位，高位都是0)中，能放置皇后的位置
        int pos = upperLim & (~(colLim | leftDiaLim | rightDiaLim));
        int res = 0;
        while (pos != 0) {
            // pos取反再+1，会先把0和1互换，低位的0全部变成了1，然后再+1，1又都变成了0，并且原来最低位的1先取反变为0，+1后变回了1
            // 整体的操作相当于最低位上的1和后边的0都保持不变，其他位置都取反了
            // 再和自己位与，最低位上的1和后边的0仍然不变，高位全部变成0，得到了最右边1的位置
            mostRightOne = pos & (~pos + 1);
            // pos再减去最右边的1，这一位就变成了0，下次再进入循环，找到的就是倒数第2右的1，如此循环直到得到所有的1
            // 每个1都是1个可以放置皇后的位置，所有可以放的位置都再次进行递归获取每种情况数，加起来就是总的情况数
            pos = pos - mostRightOne;
            // 进入下一次递归，upperLim不变，colLim需要增加本次放皇后的位置，就是每次循环最右的1的位置，
            // 位或之后该位置由0变成了1，其他位置的0和1保持不变
            // leftDiaLim和rightDiaLim同理记录皇后位置，但是在下次递归时需要左移和右移才可以
            res += process1(upperLim, colLim | mostRightOne, (leftDiaLim | mostRightOne) << 1,
                    (rightDiaLim | mostRightOne) >>> 1);
        }
        return res;
    }

    public static void main(String[] args) {
        NQueens queens = new NQueens();
        System.out.println(queens.num(4));
        System.out.println(queens.num(8));
        System.out.println(queens.num1(2));
        System.out.println(queens.num1(4));
        System.out.println(queens.num1(8));
    }
}
