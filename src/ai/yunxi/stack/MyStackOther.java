package ai.yunxi.stack;

import java.util.Stack;

/*
 * 设计一个有getMin功能的栈
 *
 * 与MyStack区别在于，压入数据的时候，增加stackMin里的重复数据，减少pop的取数据操作
 *
 * @author weiluqiang
 *
 */
public class MyStackOther {

	private Stack<Integer> stackData;
	private Stack<Integer> stackMin;

	public MyStackOther(Stack<Integer> stackData, Stack<Integer> stackMin) {
		this.stackData = stackData;
		this.stackMin = stackMin;
	}

	// 必须增加最小值=的判断，否则无法记录重复的最小值，pop的时候无法判断stackMin是否应该pop
	public void push(int newNum) {
		stackData.push(newNum);
		if (stackMin.isEmpty()) {
			stackMin.push(newNum);
		} else {
			if (newNum <= this.getMin()) {
				stackMin.push(newNum);
			} else {
				int newMin = stackMin.peek();
				stackMin.push(newMin);
			}
		}
	}

	public int pop() {
		if (stackData.isEmpty()) {
			throw new RuntimeException("The stack is empty");
		}
		// 取出的值如果是最小值，则stackMin也要取出这个值
		stackMin.pop();
		return stackData.pop();
	}

	public int getMin() {
		if (stackMin.isEmpty()) {
			throw new RuntimeException("The stack is empty");
		}
		return stackMin.peek();
	}
}
