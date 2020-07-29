package ai.yunxi.stack;

import java.util.Stack;

/*
 * 由两个栈组成的队列
 *
 * 用两个栈实现队列，支持队列的基本操作
 * stackPush入数据，stackPop出数据，stackPop永远保存stackPush的倒序，为空的时候就去stackPush里取
 * @param <E>
 */
public class TwoStackQueue<E> {

    private final Stack<E> stackPush;
    private final Stack<E> stackPop;

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

    public static void main(String[] args) {
        Stack<Integer> stackPush = new Stack<>();
        Stack<Integer> stackPop = new Stack<>();
        TwoStackQueue<Integer> queue = new TwoStackQueue<>(stackPush, stackPop);
        queue.add(1);
        queue.add(2);
        queue.add(3);
        System.out.println(queue.peek());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }
}
