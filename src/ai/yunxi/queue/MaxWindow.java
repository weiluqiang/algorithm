package ai.yunxi.queue;

import java.util.Arrays;
import java.util.LinkedList;

/*
 * 生成窗口最大值数组
 *
 * 有一个整型数组arr和一个大小为w的窗口从数组左端滑到右端，要求返回每一种窗口状态下的最大值
 *
 * 如数组[4,3,5,4,3,3,6,7]，窗口大小为3，则返回[5,5,5,4,6,7]
 *
 * 本题关键在于利用双端队列来实现最大值的更新
 *
 * 思路：要设计时间复杂度O(n)的算法，必须记录窗口状态下的最大值，以便后续比较。
 * 当窗口滑动一次，有新数值进入和旧数值弹出，若只记录了最大值，则最大值不弹出时，判断新入数据与最大值的大小。
 * 若移动窗口导致最大值弹出，则需与第二大值比较，得出最大值。 所以需记录最大值与第二大值。 若要得出第二大值，此时又需要与第三大值比较...
 * 综上来看，就需要保存一个从大到小的数据队列qmax来记录当前的大小值排序。
 */
public class MaxWindow {

    public static void main(String[] args) {
        int[] arr = {4, 3, 5, 4, 3, 3, 6, 7};

        MaxWindow maxWindow = new MaxWindow();
        int[] res = maxWindow.getMax(arr, 3, 1);
        System.out.println(Arrays.toString(res));
        int[] res1 = maxWindow.getMax(arr, 3, 2);
        System.out.println(Arrays.toString(res1));
    }

    public int[] getMax(int[] arr, int w, int method) {
        if (arr == null || w < 1) {
            return null;
        }
        if (arr.length < w) {
            // 直接返回数组arr中的最大值
            Arrays.sort(arr);
            return new int[]{arr[arr.length - 1]};
        }
        if (method == 1) {
            return getMaxWindow(arr, w);
        } else {
            return getMaxWindow1(arr, w);
        }
    }

    /*
     * 双端队列qmax存放数组arr的下标i
     *
     * qmax的放入规则：
     *
     * 1.若qmax里无数据，则直接放入，放入结束；
     *
     * 2.若qmax里有数据，则取出qmax队尾的数值，记为j；
     *
     * 3.若arr[j] > arr[i]，则把下标i放入qmax队尾，放入结束；
     *
     * 4.若arr[j] <= arr[i]，则把j从qmax弹出，接着取出下一个队尾数值再进行对比，直到放入结束。
     *
     * qmax的弹出规则：
     *
     * 1.若qmax队头的数值到当前位置i的距离超过了窗口宽度，则弹出
     *
     * @param arr
     * @param w
     * @return
     */
    public int[] getMaxWindow(int[] arr, int w) {
        LinkedList<Integer> qmax = new LinkedList<>();
        int[] res = new int[arr.length - w + 1];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            while (!qmax.isEmpty() && arr[qmax.peekLast()] <= arr[i]) {
                qmax.pollLast();
            }
            qmax.addLast(i);

            if (!qmax.isEmpty() && qmax.peekFirst() <= i - w) {
                qmax.pollFirst();
            }

            if (i >= w - 1 && !qmax.isEmpty()) {
                res[index++] = arr[qmax.peekFirst()];
            }
        }
        return res;
    }

    /*
     * 只记录最大值的算法，时间复杂度较大
     *
     * @param arr
     * @param w
     * @return
     */
    public int[] getMaxWindow1(int[] arr, int w) {
        int qmax = -1;
        int[] res = new int[arr.length - w + 1];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (qmax == -1) {// 第一次循环时
                qmax = i;
            } else {
                if (qmax <= i - w) {// 若最大值弹出，则只能遍历得出最大值
                    qmax = i;
                    if (w > 1) {
                        int first = arr[i];
                        for (int m = i - 1; m > i - w; m--) {
                            if (arr[m] >= first) {
                                first = arr[m];
                                qmax = m;
                            }
                        }
                    }
                } else {
                    if (arr[i] >= arr[qmax]) {// 与最大值比较，相等时，保存最新的数值
                        qmax = i;
                    }
                }
            }

            if (i >= w - 1) {
                res[index++] = arr[qmax];
            }
        }
        return res;
    }
}
