package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 在二叉树中找到两个节点的最近公共祖先
 * 给定一个二叉树的头节点head，以及这棵树中的两个节点o1和o2，请返回o1和o2的最近公共祖先节点。
 * 例如图所示的二叉树：
 * -------1
 * ----2------3
 * --4---5--6---7
 * ------------8
 * 节点4和5的最近公共祖先节点是2，节点5和2的最近公共祖先节点是2
 * 节点6和8的最近公共祖先节点是3，节点5和8的最近公共祖先节点是1
 * <p>
 * 进阶：如果查询两个节点的最近公共祖先的操作很频繁，想办法减少单条查询的查询时间
 * <p>
 * 再进阶：给定二叉树的头节点head，同时给定所有想要进行的查询。
 * 二叉树的节点数为N，查询条数为M，请在时间复杂度O(N+M)内返回所有查询的结果。
 * 这个问题的解决请查看“Tarjan算法与并查集解决二叉树节点间最近公共祖先的批量查询问题”
 */
public class CommonAncestor {

    /**
     * 原问题解法：后序遍历二叉树，假设遍历到当前节点为cur，因为是后序遍历，所以先处理两棵子树。
     * 假设处理cur左子树返回的节点是left，处理cur右子树返回的节点是right
     * 1.如果发现cur为null，或者是o1、o2，那么直接返回cur
     * 2.如果left和right都为空，说明cur的整棵子树上没有发现o1或o2，则返回null
     * 3.如果left和right都不为空，说明子树上存在o1和o2，且只能是一边一个，说明o1和o2向上时在此首次相遇，cur就是要找的节点，返回cur
     * 4.如果left和right只有一个为空，那么此时有两种情况，要么返回的是o1或者o2其中的一个，要么返回的是他们的最近公共祖先
     * 不论哪种情况，都直接返回不为空的那个即可
     */
    public TreeNode findCommon(TreeNode node, TreeNode o1, TreeNode o2) {
        // 情况1，直接返回node
        if (node == null || node == o1 || node == o2) return node;
        // 遍历左右子树
        TreeNode left = findCommon(node.left, o1, o2);
        TreeNode right = findCommon(node.right, o1, o2);
        if (left != null && right != null) {
            // 情况3，返回当前节点node
            return node;
        } else {
            // 情况1、4，返回不为空的那个，都为空就返回空
            return left == null ? right : left;
        }
    }

    private Map<TreeNode, TreeNode> record = null;

    /**
     * 进阶问题其实就是利用额外的空间，存储每个节点到父节点的映射关系记录，每次查询的时候根据关系记录查即可
     * 结构一：利用哈希表存储父子关系记录
     * 结构一建立记录的过程时间复杂度为O(N)，额外空间复杂度为O(N)
     * 查询时，时间复杂度为O(h)，h为二叉树的高度
     */
    public TreeNode getCommon(TreeNode head, TreeNode o1, TreeNode o2) {
        if (record == null) {
            record = new HashMap<>();
            if (head != null) record.put(head, null);
            initMap(head);
        }

        // 根据哈希表查找最近公共祖先
        Set<TreeNode> set = new HashSet<>();
        // 把o1的所有祖先节点放入set
        while (o1 != null) {
            set.add(o1);
            o1 = record.get(o1);
        }
        // 遍历o2的所有祖父节点，去set中查找是否存在公共祖先
        while (!set.contains(o2)) {
            o2 = record.get(o2);
        }
        return o2;
    }

    // 初始化哈希表
    private void initMap(TreeNode node) {
        if (node == null) return;
        if (node.left != null) {
            record.put(node.left, node);
        }
        if (node.right != null) {
            record.put(node.right, node);
        }
        initMap(node.left);
        initMap(node.right);
    }

    private Map<TreeNode, Map<TreeNode, TreeNode>> ancestors = null;

    /**
     * 结构二：直接建立两个节点之间的最近公共祖先记录，便于以后直接查询
     * 建立时，对二叉树中的每棵子树都进行如下操作：
     * 1.假设子树的头节点是h，h的所有后代节点和h的最近公共祖先都是h
     * 2.h左子树的所有节点和右子树的所有节点的最近公共祖先都是h
     * 记录的存储采用哈希表内套哈希表的结构
     * 如果二叉树的节点数为N，要记录两个节点之间的信息，需要的条数为(N-1)*N/2
     * 所以建立结构二的过程的额外空间复杂度为O(N^2)，时间复杂度为O(N^2)，单次查询的时间复杂度为O(1)
     */
    public TreeNode getCommon2(TreeNode head, TreeNode o1, TreeNode o2) {
        if (ancestors == null) {
            ancestors = new HashMap<>();
            initMap2(head);
            setMap(head);
        }
        if (o1 == o2) {
            return o1;
        }
        // 由于没有对key为head的二级哈希表进行处理，因此当o1为head时，取出哈希表是空的，此时会直接返回null
        // 为避免这种情况，要么像findCommon里一样对head进行预先处理，要么当取key=o1为空时，再取key=o2返回
        TreeNode ancestor = null;
        if (ancestors.containsKey(o1)) {
            ancestor = ancestors.get(o1).get(o2);
        }
        if (ancestor == null && ancestors.containsKey(o2)) {
            ancestor = ancestors.get(o2).get(o1);
        }
        return ancestor;
    }

    // 首先初始化全部二级哈希表
    private void initMap2(TreeNode node) {
        if (node == null) return;
        ancestors.put(node, new HashMap<>());
        initMap2(node.left);
        initMap2(node.right);
    }

    // 保存每棵子树的全部节点关系记录
    private void setMap(TreeNode node) {
        if (node == null) return;
        headRecord(node.left, node);
        headRecord(node.right, node);
        subRecord(node);
        setMap(node.left);
        setMap(node.right);
    }

    // 保存情况1的记录，头节点和所有后代节点
    private void headRecord(TreeNode n, TreeNode h) {
        if (n == null) return;
        ancestors.get(n).put(h, h);
        headRecord(n.left, h);
        headRecord(n.right, h);
    }

    // 保存情况2的记录，全部左子树节点和全部右子树节点
    private void subRecord(TreeNode h) {
        if (h == null) return;
        // 先序递归遍历左子树，在每个左子树节点再先序递归遍历右子树
        preLeft(h.left, h.right, h);
    }

    // 先序遍历左子树
    private void preLeft(TreeNode l, TreeNode r, TreeNode h) {
        if (l == null) return;
        preRight(l, r, h);
        preLeft(l.left, r, h);
        preLeft(l.right, r, h);
    }

    // 先序遍历右子树
    private void preRight(TreeNode l, TreeNode r, TreeNode h) {
        if (r == null) return;
        ancestors.get(l).put(r, h);
        preRight(l, r.left, h);
        preRight(l, r.right, h);
    }

    public static void main(String[] args) {
        TreeNode n8 = new TreeNode(8);
        TreeNode n7 = new TreeNode(7, n8, null);
        TreeNode n6 = new TreeNode(6);
        TreeNode n5 = new TreeNode(5);
        TreeNode n4 = new TreeNode(4);
        TreeNode n3 = new TreeNode(3, n6, n7);
        TreeNode n2 = new TreeNode(2, n4, n5);
        TreeNode head = new TreeNode(1, n2, n3);

        CommonAncestor common = new CommonAncestor();
        System.out.println(common.findCommon(head, head, n7));
        System.out.println(common.findCommon(head, n5, n8));
        System.out.println(common.findCommon(head, n6, n8));
        System.out.println(common.getCommon(head, head, n7));
        System.out.println(common.getCommon(head, n5, n8));
        System.out.println(common.getCommon(head, n6, n8));
        System.out.println(common.getCommon2(head, head, n7));
        System.out.println(common.getCommon2(head, n5, n8));
        System.out.println(common.getCommon2(head, n6, n8));
    }
}
