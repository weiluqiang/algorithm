package ai.yunxi.link;

import ai.yunxi.link.pojo.Node;
import ai.yunxi.link.pojo.NodeUtil;

/**
 * 一种怪异的节点是删除方式
 * 链表节点值类型为int，给定一个链表中的节点node，但不给定头节点，
 * 如何删除在链表中的node，要求时间复杂度为O(1)
 */
public class RemoveNodeOther {

    /**
     * 如链表1->2->3->null，已知节点2，删除该节点
     * 那么只需要把节点2的值变成节点3的值，然后删除节点3
     * 存在的问题：
     * 1.无法删除最后一个节点
     * 2.这种方式本质上只是修改了node的值，然后删除了node的下一个节点，
     * 这样在实际工程中可能会产生各种问题
     */
    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        Node next = node.next;
        if (next == null) {
            throw new RuntimeException("last node cannot remove");
        }
        node.value = next.value;
        node.next = next.next;
    }

    public static void main(String[] args) {
        Node head = NodeUtil.createLinkNode(1, 3, 1);
        RemoveNodeOther nodeOther = new RemoveNodeOther();
        nodeOther.removeNode(head);
        NodeUtil.printLinkNode(head);
    }
}
