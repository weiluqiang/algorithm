package ai.yunxi.string;

/**
 * 0左边必有1的二进制字符串数量
 * 给定一个整数N，求由'0'和'1'组成的长度为N的所有字符串中，满足'0'的左边必有'1'的字符串数量
 * 举例：
 * 1.N=1，长度为1的所有字符串有："0"和"1"，只有字符串"1"满足要求，所以返回1
 * 2.N=2，长度为2的所有字符串有："00"、"01"、"10"、"11"，其中"10"和"11"满足要求，所以返回2
 * 3.N=3，长度为2的所有字符串有："000"、"001"、"010"、"011"、"100"、"101"、"110"、"111"，
 * 其中"101"、"110"、"111"满足要求，所以返回3
 * <p>
 * 解法：
 * 1.先说一种暴力的解法，就是检查每一个长度为N的字符串，看又多少符合要求。
 * 检查是否符合要求的时间复杂度为O(N)，长度为N的字符串的数量为O(2^N)，所以整体的时间复杂度为O(N*2^N)
 */
public class BinaryString {

    /**
     * 时间复杂度为O(2^N)的解法：
     * 假设第0位的字符为最高位字符，很明显它不能为'0'。
     * 假设p(i)表示0~i-1位置上的字符已经确定，这一段符合要求且第i-1位置的字符为'1'时，
     * 如果穷举i~N-1位置上的所有情况会产生多少种符合要求的字符串。
     * 比如N=5，p(3)表示0~2位置上的字符已经确定，这一段符合要求，并且2位置上的字符为'1'，假设这一段为"101.."。
     * 在这种情况下，穷举3、4位置所有可能的情况会产生多少种符合要求的字符串，有"10101"、"10110"、"10111"，
     * 所以p(3)=3。也可以假设前3位是"111"，p(3)同样等于3。有了p(i)的定义，同时知道不管N是多少，
     * 最高位的字符只能为'1'，那么根据定义，只要求出p(1)就是所有符合要求的字符串数量。
     * 那么p(i)的值该怎么求呢？分两种情况：
     * 根据p(i)的定义，位置i-1的字符必然为'1'，那么位置i的字符可能为'0'或'1'。
     * 1.如果位置i的字符为'1'，那么0~i的位置上的字符都已确定，这一段肯定符合要求且第i位置的字符为'1'，
     * 这种情况下，p(i)的这一部分的值就是穷举i+1~N位置上所有情况会产生多少种结果，也就是p(i+1)的值。
     * 2.如果位置i的字符为'0'，那么0~i的位置上的字符都已确定，但这一段不一定符合要求且第i位置的字符也不是'1'，
     * 但是位置i为'0'时，i+1位置就必须为'1'，同理这种情况下，p(i)的这一部分的值就是p(i+2)。
     * 因此可以得出：p(i)=p(i+1)+p(i+2)。
     * 然后p(N-1)就表示出了最后位置N-1的字符串外，前面0~N-2位置的字符串都已确定且符合要求，N-2的位置的字符为'1'时，
     * 此时N最后位置N-1可以是'1'，也可以是'0'，都符合，所有p(N-1)=2
     * p(N)表示所有的字符串都已确定且符合要求，最后一个字符也为'1'，所以p(N)=1。
     * 总结p(i)的求法如下：
     * 1.i=N时，p(i)=1
     * 2.i=N-1时，p(i)=2
     * 3.i<N-1时，p(i)=p(i+1)+p(i+2)
     * 采用递归的方式就可以实现，时间复杂度为O(2^N)
     */
    public int getNum1(int N) {
        if (N < 1) return 0;
        // 最终的结果就是p(1)的值
        return process(1, N);
    }

    // 计算p(i)的递归逻辑
    private int process(int i, int N) {
        if (i == N) return 1;
        if (i == N - 1) return 2;
        return process(i + 1, N) + process(i + 2, N);
    }

    // 根据p(i)的求解公式，很明显可以看出类似于动态规划中的斐波那契数列，只是初始项为1，2
    // 参考当时的方法，很容易就能写出时间复杂度O(N)，额外空间复杂度O(1)的方法
    public int getNum2(int N) {
        if (N < 1) return 0;
        if (N == 1) return 1;
        int pre = 1;
        int cur = 1;
        int tmp;
        for (int i = 2; i <= N; i++) {
            tmp = cur;
            cur += pre;
            pre = tmp;
        }
        return cur;
    }

    // 根据当时的解法，还可以利用矩阵，写出时间复杂度O(logN)的方法
    public int getNum3(int N) {
        if (N < 1) return 0;
        if (N == 1 || N == 2) return N;
        int[][] base = {{1, 1}, {1, 0}};
        int[][] res = matrixPower(base, N - 2);
        return 2 * res[0][0] + res[1][0];
    }

    // 计算矩阵的p次方
    private int[][] matrixPower(int[][] m, int p) {
        int[][] res = new int[m.length][m[0].length];
        for (int i = 0; i < res.length; i++) {
            res[i][i] = 1;
        }
        int[][] tmp = m;
        for (; p != 0; p >>= 1) {
            if ((p & 1) != 0) {
                res = multiMatrix(res, tmp);
            }
            tmp = multiMatrix(tmp, tmp);
        }
        return res;
    }

    // 计算两个矩阵的乘积
    private int[][] multiMatrix(int[][] m1, int[][] m2) {
        int[][] res = new int[m1.length][m2[0].length];
        for (int i = 0; i < m2[0].length; i++) {
            for (int j = 0; j < m1.length; j++) {
                for (int k = 0; k < m2.length; k++) {
                    res[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        BinaryString string = new BinaryString();
        System.out.println(string.getNum1(4));
        System.out.println(string.getNum1(5));
        System.out.println(string.getNum2(5));
        System.out.println(string.getNum3(5));
    }
}
