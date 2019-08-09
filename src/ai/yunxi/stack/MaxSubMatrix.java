package ai.yunxi.stack;

import java.util.Stack;

/*
 * 求最大子矩阵的大小
 *
 * 给定一个整型矩阵的map，其中的值只有0和1，求其中全是1的所有矩形区域中，最大的矩形区域1的数量
 *
 * 解答：
 * 1.矩阵的行数为N，以每一行做切割，统计以当前行为底的情况下，每个位置往上的1的数量，用高度数组height表示
 * 2.对于每一行的切割后得到的高度数组height，都调用maxRecArea方法，获取每个高度能组合成的最大矩形的大小
 * 3.通过对步骤2中所有的最大矩形的大小比较，得到的最大值就是答案
 */
public class MaxSubMatrix {

    private int maxRecSize(int[][] map) {
        if (map == null || map.length == 0 || map[0].length == 0)
            return 0;

        int maxSize = 0;
        //高度数组
        int[] height = new int[map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                //计算每一行的高度
                //第一行：height[j]初始值为0，则map值为0时即为0，为1时即为1
                //第二行：height[j]此时为第一行的高度，map值为0时即为0，为1时用第一行的高度+1
                //剩下的行以此类推
                height[j] = map[i][j] == 0 ? 0 : height[j] + 1;
            }

            //得到一整行的高度数组后，调用maxRecArea方法获得此行中的最大值
            maxSize = maxRecArea(height);
        }
        return maxSize;
    }

    //计算高度数组中能组合出的最大矩形的大小
    //实质是在一个大的直方图中，求最大矩形的面积，以每一个柱子扩展出去的最大矩形，其中面积最大的就是这一组的maxArea
    //要找到每一个柱子能扩展出去的左右边界，右边界是右边第一个比它小的值-1，左边界是左边第一个比它小的值+1
    //为了得到每一个柱子能扩展出的最大矩形，需要借助栈stack
    //stack中保存的是height数组的坐标，并要求大压小，即从栈顶到栈底，栈中坐标指向的值由大到小排列
    //入栈：栈为空，则直接压入；
    //      不为空，则当前值cur大于栈顶值top时直接压入，当前值cur小于等于栈顶值top则先弹出栈顶；
    //      再比较当前值cur与下一个栈顶值，不断重复直到入栈
    //出栈：元素出栈时，需确定此坐标指向的柱子能扩展出的最大矩形的面积，此时栈顶值top是大于等于当前值cur的
    //      若top等于cur，分析可知，top和cur指向的两根柱子扩展出的最大矩形是同一个，直接弹出top压入cur
    //      若top大于cur，分析可知，top能扩展的右边界是cur-1，能扩展的左边界是栈中下一个值next+1
    //      所以最大面积是(cur-1-(next+1)+1)*top指向的值，即(cur-next-1)*top指向的值
    private int maxRecArea(int[] height) {
        if (height == null || height.length == 0)
            return 0;

        int maxArea = 0;
        //存height的坐标index，并且满足大值压小值
        Stack<Integer> stack = new Stack<>();
        for (int index = 0; index < height.length; index++) {
            while (!stack.isEmpty() && height[index] <= height[stack.peek()]) {
                int top = stack.pop();
                //两值相等则只用弹出栈顶，不用做任何操作
                if (height[index] == height[top]) {
                    continue;
                }
                //栈顶值较大，则确定此时的左右边界，计算最大矩形面积
                if (height[index] < height[top]) {
                    int next = stack.isEmpty() ? -1 : stack.peek();
                    //左边界next+1，右边界index-1
                    int curArea = (index - next - 1) * height[top];
                    maxArea = Math.max(maxArea, curArea);
                }
            }

            //当前值入栈
            stack.push(index);
        }

        //若结束时栈不为空，则全部弹出，此时有边界为height.length-1
        while (!stack.isEmpty()) {
            int top = stack.pop();
            int next = stack.isEmpty() ? -1 : stack.peek();
            int curArea = (height.length - next - 1) * height[top];
            maxArea = Math.max(maxArea, curArea);
        }
        return maxArea;
    }

    public static void main(String[] args) {
        MaxSubMatrix matrix = new MaxSubMatrix();
        int[][] arr = {{1, 0, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 0}};
        print_array(arr);
        int maxSize = matrix.maxRecSize(arr);
        System.out.println(maxSize);

        int[][] arr2 = {{1, 0, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 0}, {1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 0, 1}};
        print_array(arr2);
        System.out.println(matrix.maxRecSize(arr2));
    }

    private static void print_array(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + "  ");
            }
            System.out.println("");
        }
    }
}
