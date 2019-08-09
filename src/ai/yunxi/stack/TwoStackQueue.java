package ai.yunxi.stack;

import java.util.Stack;

/*
 * 由两个栈组成的队列
 *
 * 用两个栈实现队列，支持队列的基本操作
 * stackPush入数据，stackPop出数据，stackPop永远保存stackPush的倒序，为空的时候就去stackPush里取
 *
 * @author weiluqiang
 *
 * @param <E>
 */
public class TwoStackQueue<E> {

	private Stack<E> stackPush;
	private Stack<E> stackPop;

	public TwoStackQueue(Stack<E> stackPush, Stack<E> stackPop) {
		this.stackPush = stackPush;
		this.stackPop = stackPop;
	}

	public void add(E element) {
		stackPush.push(element);
	}

	public E poll() {
		if (stackPush.isEmpty() && stackPop.isEmpty()) {
			throw new RuntimeException("The queue is empty");
		} else {
			this.stackReverse();
		}
		return stackPop.pop();
	}

	public E peek() {
		if (stackPush.isEmpty() && stackPop.isEmpty()) {
			throw new RuntimeException("The queue is empty");
		} else {
			this.stackReverse();
		}
		return stackPop.peek();
	}

	private void stackReverse() {
		if (stackPop.isEmpty()) {
			while (!stackPush.isEmpty()) {
				stackPop.push(stackPush.pop());
			}
		}
	}
}
