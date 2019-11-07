package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 将单向链表按照固定值分成左边小、中间等、右边大的形式
 * <p>
 * 例如：9->0->4->5->1，pivot=3，调整之后可以为1->0->4->9->5，也可以为0->1->9->5->4
 * 总之左边都是小于3的节点，中间是等于3的节点(本例中没有这部分)，右边都是大于3的节点即可，各部分顺序不要求
 * <p>
 * 进阶：
 * 1.要求每部分里的节点顺序和原链表中的先后顺序一致
 * 即9->0->4->5->1调整后只能是0->1->9->4->5
 * 2.要求长度为N的链表，操作的时间复杂度为O(N)，额外的空间复杂度为O(1)
 */
public class NodePartition {

    /**
     * 普通方法：
     * 把链表所有节点放入数组中，按照一种类似快速排序的算法，把数组调整为左边小右边大的形式，然后依次连起来
     */
    public Node partition(Node head, int pivot) {
        if (head == null) {
            return null;
        }

        Node cur = head;
        int i = 0;
        while (cur != null) {
            i++;
            cur = cur.next;
        }

        // 把节点都存入数组
        Node[] arr = new Node[i];
        cur = head;
        i = 0;
        while (cur != null) {
            arr[i++] = cur;
            cur = cur.next;
        }

        // 数组排序
        arraySort(arr, pivot);

        // 重新连接
        for (int ii = 0; ii < arr.length - 1; ii++) {
            arr[ii].next = arr[ii + 1];
        }
        arr[arr.length - 1].next = null;
        return arr[0];
    }

    // 数组排序，把小的放左边，等于的放中间，大的放右边
    private void arraySort(Node[] arr, int pivot) {
        int low = 0;
        int high = arr.length - 1;
        int index = 0;
        while (index != high) {
            if (arr[index].value < pivot) {
                swap(arr, low++, index++);
            } else if (arr[index].value == pivot) {
                index++;
            } else {
                swap(arr, high--, index);
            }
        }
    }

    private void swap(Node[] arr, int i, int j) {
        if (i != j) {
            Node tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }

    /**
     * 进阶方法：
     * 1.遍历整个链表，并按照节点大小拆成3个：small、equal、big
     * 2.依次连接small、equal和big
     */
    public Node partitionAdvance(Node head, int pivot) {
        Node smallHead = null;
        Node smallTail = null;
        Node equalHead = null;
        Node equalTail = null;
        Node bigHead = null;
        Node bigTail = null;

        while (head != null) {
            if (head.value < pivot) {
                if (smallTail == null) {
                    smallHead = head;
                    smallTail = head;
                } else {
                    smallTail.next = head;
                    smallTail = head;
                }
            } else if (head.value == pivot) {
                if (equalTail == null) {
                    equalHead = head;
                    equalTail = head;
                } else {
                    equalTail.next = head;
                    equalTail = head;
                }
            } else {
                if (bigTail == null) {
                    bigHead = head;
                    bigTail = head;
                } else {
                    bigTail.next = head;
                    bigTail = head;
                }
            }
            head = head.next;
        }

        if (smallTail != null) {
            if (equalHead != null) {
                smallTail.next = equalHead;
            } else {
                smallTail.next = bigHead;
            }
        }
        if (equalTail != null) {
            equalTail.next = bigHead;
        }
        if (bigTail != null) {
            bigTail.next = null;
        }
        return smallHead != null ? smallHead : (equalHead != null ? equalHead : bigHead);
    }

    public static void main(String[] args) {
        Node head = new Node(7);
        Node node1 = new Node(9);
        Node node2 = new Node(1);
        Node node3 = new Node(8);
        Node node4 = new Node(5);
        Node node5 = new Node(2);
        Node node6 = new Node(5);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;

        NodePartition partition = new NodePartition();
        //NodeUtil.printLinkNode(partition.partition(head, 5));
        //NodeUtil.printLinkNode(partition.partitionAdvance(head, 0));
        //NodeUtil.printLinkNode(partition.partitionAdvance(head, 1));
        NodeUtil.printLinkNode(partition.partitionAdvance(head, 5));
        //NodeUtil.printLinkNode(partition.partitionAdvance(head, 6));
        //NodeUtil.printLinkNode(partition.partitionAdvance(head, 9));
        //NodeUtil.printLinkNode(partition.partitionAdvance(head, 10));
    }
}
