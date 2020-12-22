package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TreeNode;
import ai.yunxi.tree.pojo.TreeUtil;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 找到二叉树中符合搜索二叉树条件的最大拓扑结构
 * 给定一个二叉树的头节点head，已知其所有节点的值都不一样，返回其中最大的符合搜索二叉树条件的拓扑结构的大小
 * 例如，二叉树如图所示：
 * -------------6
 * -----1---------------12
 * --0----3-------10----------13
 * ------------4------14----20--16
 * -----------2-5---11--15
 * 其中最大的且符合搜索二叉树条件的拓扑结构是：
 * -----------6
 * -----1-----------12
 * --0----3-----10------13
 * -------------------20--16
 * 这个拓扑结构的节点数为8，所以返回8
 */
public class MaxTopologyBST {

    /**
     * 方法一：二叉树的节点为N，时间复杂度为O(N*N)
     * 以节点h为头的树中，在拓扑结构中也必须以h为头的情况下，怎么找到符合搜索二叉树的最大结构呢？
     * 比较容易的解法是，我们先考察h的孩子节点，根据孩子节点的值从h开始，按照二叉搜索的方式移动，
     * 如果能移动到同一个孩子节点上，说明这个孩子节点可以作为拓扑的一部分，并继续考察孩子节点的孩子，一直延续下去
     * <p>
     * 以题目的例子做一下说明：假设考察以12为头的子树，要求拓扑结构也必须以12为头
     * 1.先考察12节点的左右孩子，考察队列={10,13}
     * 2.考察节点10，最开始比较10和12，发现10应该往12的左边找，于是正好找到节点10，它可以加入拓扑结构
     * 同时10的孩子节点进入考察队列，考察队列={13,4,14}
     * 3.考察节点13，比较13和12，那么13应该在12右边，于是正好找到，节点13也可以加入拓扑结构
     * 把13的孩子节点进入考察队列，考察队列={4,14,20,16}
     * 4.考察节点4，4也满足，进入拓扑结构，孩子节点加入考察队列，考察队列={14,20,16,2,5}
     * 5.考察节点14，不符合，不能进入拓扑结构，此时考察队列={20,16,2,5}
     * 6.按照这个逻辑继续考察，发现20不符合，16,2,5都符合，它们也没有孩子节点
     * 因此考察结束，所有符合的节点是12,10,13,4,16,2,5，最大的拓扑结构大小为7
     */
    public int biggestBST1(TreeNode head) {
        if (head == null) return 0;
        int max = maxTopology(head, head);
        // 递归考察左右子节点的最大拓扑结构大小
        max = Math.max(biggestBST1(head.left), max);
        max = Math.max(biggestBST1(head.right), max);
        return max;
    }

    // 查找以当前节点为头的最大拓扑结构
    // 进入此方法的head不会为空
    private int maxTopology(TreeNode node, TreeNode head) {
        if (node != null && isBST(node, head, node.value)) {
            return maxTopology(node.left, head) + maxTopology(node.right, head) + 1;
        }
        return 0;
    }

    // 从头节点h开始，value值按二叉搜索的方式移动，判断是不是和n相等，相等则表示n符合要求
    // n不为空才会进入此方法
    // h节点会不停变化，直到为空也没找到和n相等的节点，则返回false
    private boolean isBST(TreeNode n, TreeNode h, int value) {
        if (h == null) return false;
        if (h == n) return true;
        return isBST(n, value < h.value ? h.left : h.right, value);
    }

    /**
     * 方法二：在方法一的基础上进行优化
     * 方法一在判断节点是不是符合二叉搜索的方式时，采用从头节点一直遍历到叶子节点的方式，过程很长
     * a.其实进行这个判断时并不需要遍历到叶子节点，可以通过记录层高，遍历到当前节点所在层没有符合，就可以返回了
     * b.我们还可以通过额外的存储结构记录从头节点到当前节点的路径，再用当前节点的值判断路径上的节点是否都符合二叉搜索的方式
     * 只要有不符合的，直接返回false
     */
    public int biggestBST2(TreeNode head) {
        if (head == null) return 0;
        // 通过一个双端队列记录从head到当前节点路径，是左节点就记录-1，右节点就记录1
        Deque<Integer> deque = new LinkedList<>();
        int max = maxTopology2(head, head, deque);
        // 递归考察左右子节点的最大拓扑结构大小
        max = Math.max(biggestBST2(head.left), max);
        max = Math.max(biggestBST2(head.right), max);
        return max;
    }

    // 递归遍历全部节点
    private int maxTopology2(TreeNode node, TreeNode head, Deque<Integer> deque) {
        if (node != null && isBST2(head, deque, node.value)) {
            // 深入左节点求值，此时路径向左移动，从队尾向队列中添加-1
            deque.addLast(-1);
            int max1 = maxTopology2(node.left, head, deque);
            deque.removeLast();
            // 深入右节点求值，此时路径向右移动，从队尾向队列中添加1
            deque.addLast(1);
            int max2 = maxTopology2(node.right, head, deque);
            deque.removeLast();
            return max1 + max2 + 1;
        }
        return 0;
    }

    // 根据路径队列判断节点是否符合二叉搜索
    private boolean isBST2(TreeNode h, Deque<Integer> deque, int value) {
        // 队列为空，表示是头节点本身刚进入递归
        if (deque.size() == 0) return true;

        // 从头到尾遍历碎裂
        for (int i : deque) {
            if (i < 0) {
                // i < 0表明节点在h的左子树上，此时必须满足value < h.value才是二叉搜索
                if (value > h.value) {
                    return false;
                }
                h = h.left;
            } else {
                // i > 0表明节点在h的右子树上，此时必须满足value > h.value才是二叉搜索
                if (value < h.value) {
                    return false;
                }
                h = h.right;
            }
        }
        return true;
    }

    /**
     * 方法一：二叉树的节点为N，时间复杂度最好为O(N)，最差为O(N*logN)
     * 先来说明一个非常重要的概念--拓扑贡献记录：
     * 注意题目中，以节点10为头的子树，本身就是一棵搜索二叉树，那么整棵树都可以作为以节点10为头的符合搜索二叉树条件的拓扑结构
     * 如果对它建立贡献记录，那么应该是如图的样子：
     * --------------10(3,3)
     * ------4(1,1)----------14(1,1)
     * --2(0,0)-5(0,0)---11(0,0)--15(0,0)
     * 每个节点旁边括号里的两个值，我们把它称之为当前节点对头节点的拓扑贡献记录
     * 第一个值代表左子树可以为当前头节点的拓扑贡献几个节点，第二个值代表右子树可以为当前头节点的拓扑贡献几个节点
     * 同样，我们也可以建立以节点13为头的拓扑贡献记录：
     * ------13(0,1)
     * --20(0,0)--16(0,0)
     * 整个方法二的核心就是如果分别得到了h左右两个孩子为头的拓扑贡献记录，那么可以快速得到以h为头的拓扑贡献记录
     * <p>
     * 接下来求解10和13的头节点12的拓扑贡献记录：
     * 先来看节点12的左子树，只需依次考察左子树的右边界节点即可，为什么不用考察其他节点呢？
     * 以节点10为例，它的值比节点12的值小，所以节点10的左子树原来能给节点10贡献多少个节点，这些节点的值一定小于10，那么也小于12，
     * 所以就一定能给节点12贡献多少个节点。
     * 相反，如果节点的值比父节点大，那么整棵左子树就都不能满足搜索二叉树的条件，当前左子树对父节点的贡献必然为0
     * 由于节点10的值小于节点12，那么接下来可以考察节点14，它的值大于节点12，
     * 说明以节点14为头的整棵树都不能成为以节点12为头的拓扑结构的左边部分，那么它的后续节点也无需考察了
     * 这时需要从节点10减去的贡献值是多少呢？这个可以通过节点14的贡献记录得到
     * 节点14的贡献记录为14(1,1)，再加上它本身，因此减去的贡献应该是3
     * 节点10的贡献记录就由10(3,3)变成了10(3,0)
     * 此时节点12的左子树的贡献值也可以确定了，节点10的贡献记录求和再加上它本身，就是4
     * <p>
     * 假设S的左子树上这么一个右边界S(?,?)->A(a1,a2)->B(b1,b2)->C(c1,c2)->D(d1,d2)
     * 我们从A节点开始考察，假设A、B、C的值都小于S，那么它们的记录值的第一个都不用修改
     * D的值大于S，所以删除D节点，让D子树上的所有节点都和S的拓扑结构断开，总共删掉的贡献值为d1+d2+1
     * 那么D节点的所有父节点的贡献记录的第二个值都要减去d1+d2+1，A的贡献记录变成了A(a1,a2-d1-d2-1)
     * 因此S的左子树对S的贡献值就是a1+a2-d1-d2-1+1=a1+a2-d1-d2
     * <p>
     * 同理，节点12的右子树的贡献值，只考察右子树的左边界节点即可，过程跟上面的逻辑相似，最后得到节点12的贡献记录为12(4,2)
     */
    public int biggestBST3(TreeNode head) {
        // 节点的拓扑贡献记录
        Map<TreeNode, int[]> map = new HashMap<>();
        return postOrder3(head, map);
    }

    // 后序遍历整棵树，返回以当前节点为头的树上，最大的拓扑结构贡献值
    // 注意：这个最大拓扑结构，并不一定以当前节点为头
    private int postOrder3(TreeNode node, Map<TreeNode, int[]> map) {
        if (node == null) return 0;
        // left为左子树的最大贡献值
        int left = postOrder3(node.left, map);
        // right为右子树的最大贡献值
        int right = postOrder3(node.right, map);
        modifyMap(node.left, node.value, map, true);
        modifyMap(node.right, node.value, map, false);
        int[] l = map.get(node.left);
        int[] r = map.get(node.right);
        int lc = l == null ? 0 : l[0] + l[1] + 1;
        int rc = r == null ? 0 : r[0] + r[1] + 1;
        map.put(node, new int[]{lc, rc});
        // 比较左子树、右子树和自己的最大贡献值，返回其中的最大值
        // 当前节点有可能直接不满足拓扑结构，贡献值为0，但是它的子树上任然有可能有满足拓扑结构的记录，必须比较大小后返回
        // 例如：
        // ------------26
        // -----1---------------12
        // --0----3-------10----------13
        // ------------4------14----20--16
        // -----------2-5---11--15
        // 对于头节点26，它的最大拓扑结构只包括左半部分，节点数为4
        // 但是它的右子树上，以节点10为头的子树满足拓扑结构，节点数是7
        // 节点12也有满足的子拓扑结构，节点数也是7
        // 因此此时返回的值应该是7
        return Math.max(lc + rc + 1, Math.max(left, right));
    }

    /**
     * 递归遍历左子树的右边界，或者右子树的左边界，并修改子树节点的贡献记录
     *
     * @param n 当前边界节点
     * @param v 父节点的值
     * @param m 节点贡献记录集合
     * @param s true表示左子树，false表示右子树
     * @return 当某个节点不能满足拓扑结构时，返回它的贡献值之和，它的所有父节点都要减掉这个值
     */
    private int modifyMap(TreeNode n, int v, Map<TreeNode, int[]> m, boolean s) {
        // 到达叶子节点时停止递归
        // 如果某节点不满足拓扑结构，会从map中删除，也不需要再往下遍历了
        if (n == null || (!m.containsKey(n))) return 0;
        int[] arr = m.get(n);
        // 判断节点值是否满足拓扑结构，左子树必须小于v，右子树必须大于v
        if ((s && n.value > v) || (!s && n.value < v)) {
            // 不满足拓扑结构，则直接从map中删除，并返回要减去的贡献值
            m.remove(n);
            return arr[0] + arr[1] + 1;
        } else {
            // 满足拓扑结构，则继续向下遍历，直到结束或不满足为止
            // 并获取不满足节点要减去贡献值，修改自己的贡献值，然后一直返回要减去的贡献值
            int minus = modifyMap(s ? n.right : n.left, v, m, s);
            if (s) {
                arr[1] -= minus;
            } else {
                arr[0] -= minus;
            }
            return minus;
        }
    }

    // 在解法3的基础上修改，在遍历子树的边界时，采用循环而不是递归
    public int biggestBST4(TreeNode head) {
        Map<TreeNode, int[]> map = new HashMap<>();
        return postOrder4(head, map);
    }

    private int postOrder4(TreeNode node, Map<TreeNode, int[]> map) {
        if (node == null) return 0;
        int left = postOrder3(node.left, map);
        int right = postOrder3(node.right, map);
        int lc = foreachEdge(node.left, node.value, map, true);
        int rc = foreachEdge(node.right, node.value, map, false);
        map.put(node, new int[]{lc, rc});
        return Math.max(lc + rc + 1, Math.max(left, right));
    }

    // 循环遍历左子树的右边界，或者右子树的左边界，返回子树对当前节点的贡献值
    private int foreachEdge(TreeNode cur, int v, Map<TreeNode, int[]> m, boolean s) {
        if (cur == null || (!m.containsKey(cur))) return 0;

        TreeNode n = cur;
        int[] arr = m.get(cur);
        while (n != null && m.containsKey(n)) {
            if ((s && n.value > v) || (!s && n.value < v)) {
                int[] ar = m.get(n);
                m.remove(n);
                return arr[0] + arr[1] - ar[0] - ar[1];
            } else {
                n = s ? n.right : n.left;
            }
        }
        return arr[0] + arr[1] + 1;
    }

    public static void main(String[] args) {
        TreeNode head = TreeUtil.getDefault2();
        MaxTopologyBST topology = new MaxTopologyBST();
        System.out.println(topology.biggestBST1(head));
        System.out.println(topology.biggestBST2(head));
        System.out.println(topology.biggestBST3(head));
        System.out.println(topology.biggestBST4(head));
        head.value = 26;
        System.out.println(topology.biggestBST3(head));
        System.out.println(topology.biggestBST4(head));
    }
}
