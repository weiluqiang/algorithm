package ai.yunxi.stack;

import java.util.Stack;

/*
 * 设计一个有getMin功能的栈
 *
 * 实现一个特殊的栈，增加能返回栈最小值的操作 pop、push、getMin的时间复杂度都是O(1)
 * 设计两个栈，一个用于保存当前元素，一个用于保存每步的最小值 注意：最小值有重复时，必须全部放入
 *
 * @author weiluqiang
 *
 */
public class MyStack {

	private Stack<Integer> stackData;
	private Stack<Integer> stackMin;

	public MyStack(Stack<Integer> stackData, Stack<Integer> stackMin) {
		this.stackData = stackData;
		this.stackMin = stackMin;
	}

	// 必须增加最小值=的判断，否则无法记录重复的最小值，pop的时候无法判断stackMin是否应该pop
	public void push(int newNum) {
		stackData.push(newNum);
		if (stackMin.isEmpty()) {
			stackMin.push(newNum);
		} else {
			if (newNum <= this.getMin())
				stackMin.push(newNum);
		}
	}

	public int pop() {
		if (stackData.isEmpty()) {
			throw new RuntimeException("The stack is empty");
		}
		int val = stackData.pop();
		// 取出的值如果是最小值，则stackMin也要取出这个值
		if (val == this.getMin()) {
			stackMin.pop();
		}
		return val;
	}

	public int getMin() {
		if (stackMin.isEmpty()) {
			throw new RuntimeException("The stack is empty");
		}
		return stackMin.peek();
	}
}
