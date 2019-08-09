package ai.yunxi.stack;

import java.util.Stack;

/*
 * 用一个栈实现另一个栈的排序
 *
 * 将该栈从顶到底按从大到小的顺序排列，只许申请一个栈，可以申请变量，但不能申请新的数据结构
 *
 * 要排序的栈为stack，辅助的栈为help，在stack执行pop，弹出的元素记为cur
 *
 * 1.若cur小于等于help的栈顶元素，则将cur直接压入help；
 *
 * 2.若cur大于help的栈顶元素，则依次弹出help的元素并压入stack，直到cur小于等于help的栈顶元素，再把cur压入help；
 *
 * @author weiluqiang
 *
 */
public class SortStack {

	public static void main(String[] args) {
		SortStack sortStack = new SortStack();
		Stack<Integer> stack = new Stack<>();
		stack.push(3);
		stack.push(2);
		stack.push(4);
		stack.push(1);
		stack.push(5);
		stack.push(5);
		stack.push(2);
		System.out.println(stack);
		sortStack.sortStackByStack(stack);
		System.out.println(stack);
	}

	public void sortStackByStack(Stack<Integer> stack) {
		Stack<Integer> help = new Stack<>();
		while (!stack.isEmpty()) {
			Integer cur = stack.pop();
			while (!help.isEmpty() && cur > help.peek()) {
				stack.push(help.pop());
			}
			help.push(cur);
		}

		while (!help.isEmpty()) {
			stack.push(help.pop());
		}
	}
}
