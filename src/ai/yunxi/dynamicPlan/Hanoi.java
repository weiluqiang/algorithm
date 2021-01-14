package ai.yunxi.dynamicPlan;

/**
 * 汉诺塔问题
 * 给定一个整数n，代表汉诺塔游戏中从小到大放置的n个圆盘，假设开始时所有的圆盘都放在左边的柱子上，
 * 按游戏要求想要把圆盘都移到右边的柱子上，实现函数打印最优移动轨迹
 * 举例n=1时，打印：
 * move from left to right
 * n=2时，打印：
 * move from left to mid
 * move from left to right
 * move from mid to right
 * <p>
 * 进阶题目：给定一个整型数组arr，其中只含有1、2、3，代表所有圆盘目前的状态，1代表左柱，2代表中柱，3代表右柱，
 * arr[i]的值代表第i+1个圆盘的位置。比如arr=[3,3,2,1]，代表第1个圆盘在右柱上，第2个圆盘在右柱上，第3个圆盘在中柱上，
 * 第4个圆盘在左柱上。如果arr代表的状态是最右移动轨迹过程中出现的状态，返回arr这种状态是最优移动轨迹中的第几个状态。
 * 如果arr代表的状态不是最右移动轨迹过程中出现的状态，返回-1
 * 举例：arr=[1,1]，两个圆盘都在左柱上，也就是初始状态，所以返回0
 * arr=[2,1]，第1个圆盘在中柱上，第2个圆盘在左柱上，这是2个圆盘的游戏中最优移动轨迹的第1步，因此返回1
 * arr=[3,3]，两个圆盘都在右柱上，这是2个圆盘的游戏中最优移动轨迹的第3步，因此返回3
 * arr=[2,2]，两个圆盘都在中柱上，这个状态不会在最优移动轨迹中出现，因此返回-1
 * 如果arr的长度为N，请实现时间复杂度O(N)，额外的空间复杂度O(1)的方法
 */
public class Hanoi {

    /**
     * 原问题，假设有from柱子、mid柱子、to柱子，把都在from柱子的圆盘1~i移动到to，最优过程为：
     * 1.把圆盘1~i-1从from移动到mid
     * 2.把圆盘i从from移动到to
     * 3.把圆盘1~i-1从mid移动到to
     * 如果圆盘只有1个，把它直接从from移动到to即可
     */
    public void hanoi(int n) {
        if (n > 0) move(n, "left", "mid", "right");
    }

    private void move(int n, String from, String mid, String to) {
        if (n == 1) {
            System.out.println("move from " + from + " to " + to);
        } else {
            move(n - 1, from, to, mid);
            move(1, from, mid, to);
            move(n - 1, mid, from, to);
        }
    }

    /**
     * 进阶题目：首先要求都在from柱子上的圆盘1~i，都移动到to上的最少步骤数，假设为S(i)。
     * 根据上面的步骤，S(i)=步骤1的步数+1+步骤3的步数=S(i-1)+1+S(i-1)，S(1)=1
     * 因此S(i)+1=2*(S(i-1)+1)，S(1)+1=2，根据等比数列的求和公式可得S(i)+1=2^i，所以S(i)=2^i-1
     * 对于数组arr来说，arr[N-1]表示最大圆盘N在哪个柱子上，情况有以下三种：
     * 1.圆盘N在左柱上，说明步骤1或者没有完成，或者已经完成，需要考查圆盘1~N-1的状况
     * 2.圆盘N在右柱上，说明步骤1已经完成，起码走完了2^(N-1)-1步，步骤2也已经完成，起码又走了1步，
     * 所以当前状况起码是最优步骤的2^(N-1)步，剩下的步骤咋么确定还得继续考查圆盘1~N-1的状况
     * 3.圆盘N在中柱上，这是不可能的，最优步骤不会出现这种情况，直接返回-1
     * <p>
     * 所以整个过程可以总结为：对圆盘1~i来说，如果目标为from到to，那么情况有三种：
     * 1.圆盘i在from上，需要考查圆盘1~i-1的状况，圆盘1~i-1的目标为from到mid
     * 2.圆盘i在to上，说明起码走完了2^(i-1)步，剩下的步骤怎么确定还得继续考查圆盘1~i-1的状况，圆盘1~i-1的目标是从mid到to
     * 3.圆盘i在mid上，直接返回-1
     */
    public int step(int[] arr) {
        if (arr == null || arr.length == 0) return -1;
        return process(arr, arr.length - 1, 1, 2, 3);
    }

    private int process(int[] arr, int i, int from, int mid, int to) {
        if (i == -1) return 0;
        // 圆盘在mid上，直接返回-1
        if (arr[i] != from && arr[i] != to) return -1;
        if (arr[i] == from) {
            // 考查圆盘1~i-1，目标为from到mid
            return process(arr, i - 1, from, to, mid);
        } else {
            // 考查圆盘1~i-1，目标为mid到to
            int rest = process(arr, i - 1, mid, from, to);
            if (rest == -1) return -1;
            return (1 << i) + rest;
        }
    }

    /**
     * 递归方式的时间复杂度为O(N)，由于使用了函数栈，所以额外的空间复杂度也为O(N)
     * 为了达到题目的额外空间复杂度的要求，需要改成非递归的方式
     */
    public int step1(int[] arr) {
        if (arr == null || arr.length == 0) return -1;
        int from = 1;
        int mid = 2;
        int to = 3;
        int i = arr.length - 1;
        int tmp;
        int res = 0;
        while (i >= 0) {
            if (arr[i] != from && arr[i] != to) return -1;
            if (arr[i] == from) {
                // 交换mid和to
                tmp = to;
                to = mid;
            } else {
                res += (1 << i);
                // 交换from和mid
                tmp = from;
                from = mid;

            }
            mid = tmp;
            i--;
        }
        return res;
    }

    public static void main(String[] args) {
        Hanoi hanoi = new Hanoi();
        hanoi.hanoi(3);
        int[] arr = {1, 2, 3};
        System.out.println(hanoi.step(arr));
        System.out.println(hanoi.step1(arr));
        int[] arr1 = {3, 2, 1};
        System.out.println(hanoi.step(arr1));
        System.out.println(hanoi.step1(arr1));
        int[] arr2 = {1, 2, 2};
        System.out.println(hanoi.step(arr2));
        System.out.println(hanoi.step1(arr2));
    }
}
