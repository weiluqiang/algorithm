package ai.yunxi.sort;

// 选择排序
public class SelectSort {

    /**
     * 简单选择排序
     * <p>
     * 在要排序的一组数中，选出最小的一个数与第一个位置的数交换；
     * 然后在剩下的数当中再找最小的与第二个位置的数交换，如此循环到倒数第二个数和最后一个数比较为止。
     */
    public void simpleSelect(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int min = array[i];
            int n = i; // 最小数的索引
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < min) { // 找出最小的数
                    min = array[j];
                    n = j;
                }
            }
            array[n] = array[i];
            array[i] = min;
        }
    }

    /**
     * 堆排序
     * <p>
     * 初始时把要排序的数的序列看作是一棵顺序存储的二叉树，调整它们的存储序，使之成为一个堆，
     * 这时堆的根节点的数最大，然后将根节点与堆的最后一个节点交换。然后对前面(n-1)个数重新调整使之成为堆。
     * 依此类推，直到只有两个节点的堆，并对它们作交换，最后得到有n个节点的有序序列。
     */
    public void heapSort(int[] array) {
        int length = array.length;
        // 构建一个最大堆
        buildMaxHeap(array, length);
        // 将堆首位(最大值)与末位交换，然后调整堆
        while (length > 0) {
            swap(array, 0, length - 1);
            length--;
            adjustHeap(array, 0, length);
        }
    }

    // 建立最大堆，把数组看成一个堆结构，0号元素为堆顶
    // i号元素的左子节点为2*i+1号元素，右子节点为2*i+2号元素
    private void buildMaxHeap(int[] array, int length) {
        // 从最后一个非叶子节点开始向前调整堆
        // length个元素的最后一个非叶子节点为length/2-1
        for (int i = length / 2 - 1; i >= 0; i--) {
            adjustHeap(array, i, length);
        }
    }

    // 调整元素顺序使之成为最大堆
    private void adjustHeap(int[] array, int i, int length) {
        int maxIndex = i;
        // 如果有左子节点，且值大于父节点
        int left = i * 2 + 1;
        if (left < length && array[left] > array[maxIndex])
            maxIndex = left;
        // 如果有右子节点，且值大于父节点和左子节点的最大者
        int right = i * 2 + 2;
        if (right < length && array[right] > array[maxIndex])
            maxIndex = right;
        // 若三个节点的最大值不是父节点，交换父节点和最大值节点
        if (maxIndex != i) {
            swap(array, maxIndex, i);
            // 若进行了节点交换，则交换后的子节点也要调整
            adjustHeap(array, maxIndex, length);
        }
    }

    // 对data数组从0到lastIndex建大顶堆
    private void buildMaxHeap2(int[] data, int lastIndex) {
        // 从lastIndex处节点（最后一个节点）的父节点开始
        for (int i = (lastIndex - 1) / 2; i >= 0; i--) {
            // k保存正在判断的节点
            int k = i;
            // 如果当前k节点的子节点存在
            while (k * 2 + 1 <= lastIndex) {
                // k节点的左子节点的索引
                int biggerIndex = 2 * k + 1;
                // 如果biggerIndex小于lastIndex，即biggerIndex+1代表的k节点的右子节点存在
                if (biggerIndex < lastIndex) {
                    // 若果右子节点的值较大
                    if (data[biggerIndex] < data[biggerIndex + 1]) {
                        // biggerIndex总是记录较大子节点的索引
                        biggerIndex++;
                    }
                }
                // 如果k节点的值小于其较大的子节点的值
                if (data[k] < data[biggerIndex]) {
                    // 交换他们
                    swap(data, k, biggerIndex);
                    // 将biggerIndex赋予k，开始while循环的下一次循环，重新保证k节点的值大于其左右子节点的值
                    k = biggerIndex;
                } else {
                    break;
                }
            }
        }
    }

    // 交换元素
    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
