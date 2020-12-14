package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 二叉树的按层打印和ZigZag打印
 * 给定一个二叉树的头节点head，分别实现按层打印和ZigZag打印
 * 例如，二叉树如图：
 * -------1
 * ----2-----3
 * --4-----5---6
 * -------7-8
 * 按层打印时，输出的格式如下：
 * Level 1 : 1
 * Level 2 : 2 3
 * Level 3 : 4 5 6
 * Level 4 : 7 8
 * ZigZag打印时，输出的格式如下：
 * Level 1 from left to right : 1
 * Level 2 from right to left : 3 2
 * Level 3 from left to right : 4 5 6
 * Level 4 from right to left : 8 7
 */
public class ZigZagPrint {

    /**
     * 按层打印：
     * 按层打印的逻辑十分简单，本题的难度在于要知道每一层的开始和结束节点，层级变化后进行换行和打印层级信息
     * 分析整体逻辑可知，我们首先必须知道整个层级的最右节点，记为last，我们再记录一个nLast节点，它表示下一层的最右节点
     * 我们每一层都按从左到右的顺序遍历，如果发现遍历的节点是last，那么就处理换行。换行后令last=nLast，就可以接着打印下一层
     * 那么如何更新nLast呢？只需要让nLast一直记录打印队列中最新加入的节点即可
     * 结合题目的例子，整个过程应该是：
     * 1.开始时，last=节点1，nLast=null，把节点1放入队列queue，开始遍历，queue={1}
     * 2.从queue中弹出节点1并打印，然后把它的孩子依次放入queue，放入节点2时，nLast=节点2，放入节点3时，nLast=节点3
     * 3.然后判断弹出的节点1=last，执行换行，并令last=nLast=节点3，queue={2,3}
     * 4.从queue中弹出节点2并打印，再把它的孩子放入queue，最后nLast=节点4，queue={3,4}
     * 5.此时判断弹出的节点2!=last，不执行换行
     * 6.弹出节点3并打印，并把孩子放入queue，最后nLast=节点6，queue={4,5,6}，此时last=节点3，换行并更新last=节点6
     * 7.弹出节点4并打印，它没有孩子节点，不用做任何操作，也不用换行，此时nLast=节点6，queue={5,6}
     * 8.弹出节点5并打印，把节点7、8加入queue，nLast=节点8，queue={6,7,8}，不用换行
     * 9.弹出节点6并打印，不用做任何操作，需要换行并更新last=节点8，queue={7,8}
     * 10.按上面的逻辑分别打印节点7、8，整个打印过程完毕
     */
    public void printByLevel(TreeNode head) {
        if (head == null) return;
        TreeNode last = head;
        TreeNode nLast = null;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(head);
        int level = 1;
        System.out.print("Level " + (level++) + " : ");
        while (!queue.isEmpty()) {
            head = queue.poll();
            System.out.print(head.value + " ");
            if (head.left != null) {
                queue.offer(head.left);
                nLast = head.left;
            }
            if (head.right != null) {
                queue.offer(head.right);
                nLast = head.right;
            }
            if (head == last && !queue.isEmpty()) {
                System.out.print("\nLevel " + (level++) + " : ");
                last = nLast;
            }
        }
    }

    /**
     * ZigZag打印：
     * 与按层打印的不同之处在于，每次换行之后需要打印本次是从右到左还是从左到右，
     * 并且遍历顺序每层也要变化，因此放入队列的顺序也要调整，需要根据层级判断
     * 此外遍历打印的逻辑也要调整，因为对于偶数层来说，它的打印顺序是从右往左，
     * 但是它的下层节点顺序是从左往右，因此子节点入队顺序也必须从左往右，这一层的左到右和右到左两种顺序都必须记录
     * 要解决这个问题，首先可以考虑使用额外的栈空间，对需要的层级记录其逆序顺序，这样就同时记录了两种顺序
     * 在入队列时通过入栈出栈的逆序，把右到左的顺序调整回左到右的顺序
     */
    public void printZigZag(TreeNode head) {
        if (head == null) return;
        TreeNode last = head;
        TreeNode nLast = null;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(head);
        // 保存逆序的栈，单数层时打印顺序和子节点入队顺序一致，不需要逆序
        // 双数层时先打印全部节点，并入栈保存，在换行时全部出栈入队
        Stack<TreeNode> stack = new Stack<>();
        int level = 1;
        System.out.print("Level " + level + " from left to right" + " : ");
        while (!queue.isEmpty()) {
            head = queue.poll();
            System.out.print(head.value + " ");
            int i = level & 1;
            if (i == 1) {
                // 单数层，先放右子节点再放左子节点
                if (head.right != null) {
                    queue.offer(head.right);
                    nLast = head.right;
                }
                if (head.left != null) {
                    queue.offer(head.left);
                    nLast = head.left;
                }
            } else {
                // 双数层，先把当前节点入栈
                stack.push(head);
            }
            if (head == last) {
                // 打印到本层到最后的节点时，判断是否为双数层，双数层的入队操作在此时完成
                if (i == 0) {
                    while (!stack.isEmpty()) {
                        head = stack.pop();
                        if (head.left != null) {
                            queue.offer(head.left);
                            nLast = head.left;
                        }
                        if (head.right != null) {
                            queue.offer(head.right);
                            nLast = head.right;
                        }
                    }
                }
                if (!queue.isEmpty()) {
                    level++;
                    i = level & 1;
                    System.out.print("\nLevel " + level + (i == 1 ? " from left to right" : " from right to left") + " : ");
                    last = nLast;
                }
            }
        }
    }

    /**
     * ZigZag打印的第二种思路：
     * 第一种思路需要用到额外的栈空间，而且还需要再遍历一次栈，因此时间复杂度和空间复杂度都不够优秀
     * 那么更优秀的思路是什么呢？其实我们大可不必改变队列里的存储顺序，只需改变打印的顺序就可以了
     * 队列里都是按从左到右的顺序存储，打印的时候，根据层级选择左到右或者右到左的顺序，也即是从头打印或从尾打印
     * 显然，此时单向队列就不能满足要求了，我们需要使用双端队列
     */
    public void printZigZag2(TreeNode head) {
        if (head == null) return;
        TreeNode last = head;
        TreeNode nLast = null;
        Deque<TreeNode> deque = new LinkedList<>();
        deque.offer(head);
        int level = 1;
        System.out.print("Level " + level + " from left to right" + " : ");
        while (!deque.isEmpty()) {
            int i = level & 1;
            if (i == 1) {
                // 单数层从头取节点打印，从尾放入子节点
                // nLast记录最左节点，也即是本层遍历的第一个子节点
                head = deque.pollFirst();
                if (head.left != null) {
                    deque.offerLast(head.left);
                    if (nLast == null) nLast = head.left;
                }
                if (head.right != null) {
                    deque.offerLast(head.right);
                    if (nLast == null) nLast = head.right;
                }
            } else {
                // 双数层从尾取节点打印，子节点从头放入
                // nLast记录最右节点，因为本层遍历是从右到左，所以仍然是本层遍历的第一个子节点
                head = deque.pollLast();
                if (head.right != null) {
                    deque.offerFirst(head.right);
                    if (nLast == null) nLast = head.right;
                }
                if (head.left != null) {
                    deque.offerFirst(head.left);
                    if (nLast == null) nLast = head.left;
                }
            }
            System.out.print(head.value + " ");
            if (head == last && !deque.isEmpty()) {
                level++;
                i = level & 1;
                System.out.print("\nLevel " + level + (i == 1 ? " from left to right" : " from right to left") + " : ");
                last = nLast;
                // nLast赋值时会判断null值，因此每次换行都需要重新赋为null
                nLast = null;
            }
        }
    }

    public static void main(String[] args) {
        TreeNode n8 = new TreeNode(8);
        TreeNode n7 = new TreeNode(7);
        TreeNode n6 = new TreeNode(6);
        TreeNode n5 = new TreeNode(5, n7, n8);
        TreeNode n4 = new TreeNode(4);
        TreeNode n3 = new TreeNode(3, n5, n6);
        TreeNode n2 = new TreeNode(2, n4, null);
        TreeNode head = new TreeNode(1, n2, n3);

        ZigZagPrint print = new ZigZagPrint();
        print.printByLevel(head);
        System.out.println();
        print.printZigZag(head);
        System.out.println();
        print.printZigZag2(head);
    }
}
