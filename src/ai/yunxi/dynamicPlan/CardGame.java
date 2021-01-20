package ai.yunxi.dynamicPlan;

/**
 * 排成一条线的纸牌博弈问题
 * 给定一个整型数组arr，代表数值不同的纸牌排成一条线。玩家A和玩家B依次拿走每张纸牌，规定玩家A先拿，玩家B后拿，
 * 但是每个玩家每次只能拿走最左或最右的纸牌，最后分数多的获胜。假设玩家A和玩家B都绝顶聪明，请返回最后获胜者的分数。
 * <p>
 * 举例，arr=[1,2,100,4]，开始玩家A只能拿走1或4，如果拿4，那么B就可以拿走100，最后B将会获胜，所以A必须先拿1，
 * 接下来B就只能拿4，然后自己拿100，这样就是A获胜，最后的分数为101
 * arr=[1,100,2]，开始A只能拿2，然后100就会被B拿走，最后B获胜，分数就是100
 */
public class CardGame {

    /**
     * 暴力递归的方法。定义递归函数f(i,j)，表示如果arr[i..j]这个排列上的纸牌被聪明绝顶的人先拿，最终能获得什么分数。
     * 定义递归函数s(i,j)，表示如果arr[i..j]的纸牌被聪明绝顶的人后拿，最终能获得什么分数。
     * 首先来分析f(i,j)的逻辑：
     * 1.如果i==j，那么只剩一张纸牌了，必然会被先拿的人拿走，所以返回arr[i]
     * 2.如果i!=j，先拿纸牌的人有两种选择，要么拿走arr[i]，要么拿走arr[j]。如果拿走arr[i]，那么剩下的是arr[i+1..j]，
     * 他成了后拿的人，所以后续他能获得分数是s(i+1,j)。如果拿走arr[j]，那么剩下的是arr[i..j-1]，他成立后拿的人，
     * 后续能获得的分数是s(i,j-1)。他必定会在两种决策中选择最优的，所以返回max{arr[i]+s(i+1,j),arr[j]+s(i,j-1)}
     * 然后分析s(i,j)的逻辑：
     * 1.如果i==j，只剩一张纸牌，那么作为后拿的人，肯定什么也拿不到，所以返回0
     * 2.如果i!=j，根据函数s的定义，玩家的对手先拿牌。对手要么拿arr[i]，剩下arr[i+1..j]，要么拿arr[j]，
     * 剩下arr[i..j-1]。因为对手也是聪明绝顶的人，必然会把最差的情况留给玩家，所以返回min{f(i+1,j),f(i,j-1)}
     * <p>
     * 递归方法中，递归函数一共有N层，并且f和s是交替出现的。f有两个s分支，s也有两个f分支，
     * 所以整体的时间复杂度为O(2^N)，额外空间复杂度为O(N)
     */
    public int win(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        return Math.max(f(arr, 0, arr.length - 1), s(arr, 0, arr.length - 1));
    }

    private int f(int[] arr, int i, int j) {
        if (i == j) return arr[i];
        return Math.max(arr[i] + s(arr, i + 1, j), arr[j] + s(arr, i, j - 1));
    }

    private int s(int[] arr, int i, int j) {
        if (i == j) return 0;
        return Math.min(f(arr, i + 1, j), f(arr, i, j - 1));
    }

    /**
     * 动态规划的方法。如果arr的长度为N，生成两个大小为N*N矩阵f和s，f[i][j]表示上面的函数f(i,j)的返回值，
     * s[i][j]表示s(i,j)的返回值，两个矩阵的计算逻辑和上面相同
     * <p>
     * 动态规划方法中，两个矩阵都有N^2个位置，每个位置计算的过程都是O(1)，
     * 所以动态规划方法的时间复杂度为O(N^2)，额外空间复杂度也为O(N^2)
     */
    public int win1(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        int[][] f = new int[arr.length][arr.length];
        int[][] s = new int[arr.length][arr.length];
        // 先计算i==j时的值，此时f[i][j]==arr[i]，s[i][j]==0
        for (int i = 0; i < arr.length; i++) {
            f[i][i] = arr[i];
            // 有i==j时的值，可以计算f[0][i]和s[0][i]的值，采取从后向前计算
            // 从f[i][i]开始，计算s[i-1][i]=min{f[i][i],f[i-1][i-1]}
            // 从s[i][i]开始，计算f[i-1][i]=max{arr[i-1]+s[i][i],arr[i]+s[i-1][i-1]}
            // (f[i-1][i-1]在上一次循环里计算了，s[i-1][i-1]就是0)
            // 有了f[i-1][i]和s[i-1][i]，可以接着计算s[i-2][i]和f[i-2][i]
            // s[i-2][i]=min{f[i-1][i],f[i-2][i-1]}
            // f[i-2][i]=max{arr[i-2]+s[i-1][i],arr[i]+s[i-2][i-1]}
            // (f[i-2][i-1]和s[i-2][i-1]在上一次循环里也计算了)
            // 按照上面的逻辑一直从后向前计算，直到最开始的位置0
            for (int j = i - 1; j >= 0; j--) {
                f[j][i] = Math.max(arr[j] + s[j + 1][i], arr[i] + s[j][i - 1]);
                s[j][i] = Math.min(f[j + 1][i], f[j][i - 1]);
            }
        }
        return Math.max(f[0][arr.length - 1], s[0][arr.length - 1]);
    }

    public static void main(String[] args) {
        CardGame game = new CardGame();
        int[] arr = {1, 2, 100, 4};
        int[] arr1 = {1, 100, 2};
        System.out.println(game.win(arr));
        System.out.println(game.win(arr1));
        System.out.println(game.win1(arr));
        System.out.println(game.win1(arr1));
    }
}
