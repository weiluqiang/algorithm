package ai.yunxi.stack;

import java.util.Stack;

/*
 * 用递归函数和栈操作逆序一个栈
 *
 * 实现栈中元素的逆序，只能用递归函数来实现，不能用其他数据结构
 *
 * @author weiluqiang
 *
 */
public class ReverseStack {

	public static void main(String[] args) {
		Stack<Integer> stack = new Stack<>();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		stack.push(5);
		System.out.println(stack);

		Stack<Integer> result = new Stack<>();
		ReverseStack reverseStack = new ReverseStack();
		reverseStack.reverse(stack, result);
		System.out.println(result);
		reverseStack.reverse1(result);
		System.out.println(result);
	}

	public <E> void reverse(Stack<E> stack, Stack<E> result) {
		if (!stack.isEmpty()) {
			result.push(stack.pop());
			this.reverse(stack, result);
		}
	}

	public <E> E getAndRemoveLastElement(Stack<E> stack) {
		E result = stack.pop();
		if (stack.isEmpty()) {
			return result;
		} else {
			E last = getAndRemoveLastElement(stack);
			stack.push(result);
			return last;
		}
	}

	public <E> void reverse1(Stack<E> stack) {
		if (!stack.isEmpty()) {
			E i = getAndRemoveLastElement(stack);
			reverse1(stack);
			stack.push(i);
		}
	}
}
