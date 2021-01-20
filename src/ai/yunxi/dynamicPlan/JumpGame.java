package ai.yunxi.dynamicPlan;

/**
 * 跳跃游戏
 * 给定数组arr，arr[i]=k表示可以从位置i向右跳1~k个距离。比如，arr[2]=3，代表可以从位置2
 * 跳到位置3、位置3、位置5。如果从位置0出发，返回最少跳几次能跳到arr最后的位置上。
 * 例如：arr=[3,2,3,1,1,4]，arr[0]=3，可以选择跳到位置1、2、3，跳到位置1，arr[1]=2，可以向后跳2步，
 * 跳到位置2可以向后跳3步，跳到位置3可以向后跳1步。其中第二种情况就可以跳到最后的位置，所以返回2。
 */
public class JumpGame {

    /**
     * 递归的方法
     */
    public int jump(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        return p(arr, 0);
    }

    private int p(int[] arr, int i) {
        if (i + arr[i] >= arr.length - 1) return 1;
        int step = Integer.MAX_VALUE;
        for (int m = 1; m <= arr[i]; m++) {
            int s = p(arr, i + m) + 1;
            if (step > s) {
                step = s;
            }
        }
        return step;
    }

    /**
     * 循环的方法：
     * 1.需要整型变量jump，代表跳了多少步。整型变量cur，代表如果只能跳jump步，最远能到达的位置。
     * 整型变量next，代表如果再跳一步，最远能到达的位置。初始时都为0
     * 2.从左到右遍历arr，假设遍历到位置i
     * 2-1.如果cur>=i，说明跳jump步可以到达位置i，此时什么也不用做
     * 2-2.如果cur<i，说明跳jump步不能到达位置i，需要多跳一步。此时令jump++，cur=next
     * 2-3.将next更新成Math.max(next,i+arr[i])，表示下一次多跳一步到达的位置
     * 3.返回最终的jump即可
     */
    public int jump1(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        int jump = 0;
        int cur = 0;
        int next = 0;
        for (int i = 0; i < arr.length; i++) {
            if (cur < i) {
                jump++;
                cur = next;
            }
            next = Math.max(next, i + arr[i]);
        }
        return jump;
    }

    public static void main(String[] args) {
        JumpGame game = new JumpGame();
        int[] arr = {3, 2, 3, 5, 1, 4, 1, 1};
        System.out.println(game.jump(arr));
        System.out.println(game.jump1(arr));
    }
}
