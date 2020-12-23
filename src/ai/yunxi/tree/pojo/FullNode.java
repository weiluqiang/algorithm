package ai.yunxi.tree.pojo;

/**
 * 比普通二叉树节点多了指向父节点的parent指针
 */
public class FullNode {

    public int value;
    public FullNode left;
    public FullNode right;
    public FullNode parent;

    public FullNode(int value) {
        this.value = value;
    }

    public FullNode(int value, FullNode left, FullNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "value: " + value;
    }
}
