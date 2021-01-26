package ai.yunxi.string;

/**
 * 判断字符数组中是否所有的字符都只出现一次
 * 给定一个字符类型数组char[] ch，判断ch中是否所有的字符都只出现过一次。
 * 请根据以下不同的两种要求实现两个函数。
 * 要求：
 * 1.实现时间复杂度为O(N)的方法
 * 2.在保证额外空间复杂度O(1)的前提下，实现时间复杂度尽量低的方法
 */
public class UniqueWord {

    /**
     * 要求1：遍历数组，记录每个字符出现的情况。可以用数组实现，也可以用哈希表实现。
     */
    public boolean isUnique(char[] ch) {
        if (ch == null) return true;
        boolean[] map = new boolean[256];
        for (char c : ch) {
            if (map[c]) {
                // 第二次出现直接返回false
                return false;
            }
            // 第二次出现设置为true
            map[c] = true;
        }
        return true;
    }

    /**
     * 要求2：整体思路是先将ch排序，排序后相同的字符就放在一起，然后判断就变得很容易。
     * 要实现额外空间复杂度O(1)的排序算法，而且要时间复杂度尽量低，堆排序是最好的选择。
     */
    public boolean isUnique1(char[] ch) {
        if (ch == null) return true;
        heapSort(ch);
        for (int i = 1; i < ch.length; i++) {
            if (ch[i] == ch[i - 1]) {
                return false;
            }
        }
        return true;
    }

    // 堆排序方法，额外空间复杂度O(1)，时间复杂度O(N*logN)
    private void heapSort(char[] ch) {
        // 经过第一次循环，每棵二叉树的最大值都在父节点，其中数组的最大值在根节点
        for (int i = 0; i < ch.length; i++) {
            heapInsert(ch, i);
        }
        // 根节点的位置，也就是位置0，此时位置0上值最大
        // 先把最大值交换到数组最后，就是length-1的位置
        // 然后调整size=length-1的数组，也就是前0到length-2上的值
        // 调整完之后，其中的最大值就在位置0上，然后把它交换到length-2的位置
        // 不断重复过程，并缩小调整范围，直到最后只有两个值，调整完之后整个数组就有序了
        for (int i = ch.length - 1; i > 0; i--) {
            swap(ch, 0, i);
            heap(ch, i);
        }
    }

    private void heapInsert(char[] ch, int i) {
        int parent;
        while (i != 0) {
            parent = (i - 1) / 2;
            if (ch[parent] < ch[i]) {
                swap(ch, parent, i);
                i = parent;
            } else {
                break;
            }
        }
    }

    private void heap(char[] ch, int size) {
        int i = 0;
        int left = 1;
        int right = 2;
        int largest = i;
        while (left < size) {
            // 每次进入循环时，i等于上次循环的最大值的位置，left、right为左右子节点的位置
            if (ch[left] > ch[i]) {
                largest = left;
            }
            if (right < size && ch[right] > ch[largest]) {
                largest = right;
            }
            // 经过两次判断，largest记录当前位置i和左右子节点位置中值最大的一个
            // 如果值最大的位置不是父节点，即位置i，则把最大值交换到父节点
            if (largest != i) {
                swap(ch, largest, i);
            } else {
                break;
            }
            // i记录上一个最大值的位置，left、right分别是其左右子节点
            i = largest;
            left = i * 2 + 1;
            right = i * 2 + 2;
        }
    }

    private void swap(char[] ch, int i, int j) {
        char c = ch[i];
        ch[i] = ch[j];
        ch[j] = c;
    }

    public static void main(String[] args) {
        UniqueWord word = new UniqueWord();
        String s1 = "abc";
        String s2 = "121";
        System.out.println(word.isUnique(s1.toCharArray()));
        System.out.println(word.isUnique(s2.toCharArray()));
        System.out.println(word.isUnique1(s1.toCharArray()));
        System.out.println(word.isUnique1(s2.toCharArray()));
    }
}
