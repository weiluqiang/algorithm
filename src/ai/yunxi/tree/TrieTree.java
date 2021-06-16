package ai.yunxi.tree;

import ai.yunxi.tree.pojo.TrieNode;

/**
 * 字典树的实现
 * 字典树又称为前缀树或者Trie树，是处理字符串常见的数据结构。
 * 假设组成所有单词的字符仅是'a'~'z'，请实现字典树结构，并包含以下四个主要功能
 * 1.void insert(String word)：添加word，可以重复添加
 * 2.void delete(String word)：删除word，如果word添加过多次，仅删除一个
 * 3.boolean search(String word)：查询word是否在字典树中
 * 4.int prefixNum(String pre)：返回以pre为前缀的单词数量
 * <p>
 * 字典树的基本性质如下：
 * 1.根节点没有字符路径。除根节点外，每一个节点都被一个字符路径找到
 * 2.从根节点到某一节点，将路径上经过的字符连接起来，为扫过的对应字符串
 * 3.每个节点向下所有的字符路径上的字符都不相同
 * <p>
 * 在字典树上搜索添加过的单词的步骤为：
 * 1.从根节点开始搜索
 * 2.取得要查找单词的第一个字母，并根据该字母选择对应的字符路径向下继续搜索
 * 3.字符路径指向的第二层节点上，根据第二个字母选择对应的字符路径向下继续搜索
 * 4.一直向下搜索，如果单词能够整个搜索完毕，找到的最后一个节点是终止节点，说明字典树中含有这个单词。
 * 如果不是终止节点，说明字典树中不含有这个单词。
 * 如果单词没有搜索完，后面已经没有节点了，也说明字典树中不含有这个单词。
 */
public class TrieTree {

    private final TrieNode root;

    public TrieTree() {
        this.root = new TrieNode();
    }

    /**
     * 添加word的方法，可以重复添加
     * 假设单词word的长度为N，从左到右遍历word中的每个字符，并依次从头节点开始，根据每一个word[i]查找下一个节点。
     * 如果找不到就建立新节点，记为a，并令a.path=1。
     * 如果节点存在，记为b，令b.path++。
     * 通过最后一个字符word[N-1]找到最后一个节点时，记为e，令e.path++，e.end++
     */
    public void insert(String word) {
        if (word == null || word.length() == 0) {
            return;
        }
        char[] w = word.toCharArray();
        TrieNode node = root;
        int i;
        for (char c : w) {
            i = c - 'a';
            if (node.map[i] == null) {
                node.map[i] = new TrieNode();
            }
            node = node.map[i];
            node.path++;
        }
        // 最后一个节点时end++
        node.end++;
    }

    /**
     * 删除word，如果word添加过多次，仅删除一个
     * 删除word之前必须先调用search方法，只有word在这棵树上才执行删除
     * 删除的时候也是从左到右遍历word中的每个字符，并依次从头节点开始，根据每一个word[i]查找下一个节点。
     * 在找的过程中，把扫过的每一个节点的path值减1，如果发现下一个节点的path值减完之后为0了，
     * 说明下面的单词字符都只有1份了，直接删除后续的所有路径，返回即可
     * 如过扫到最后一个节点，记为e，令e.path--，e.end--
     */
    public void delete(String word) {
        if (search(word)) {
            char[] w = word.toCharArray();
            TrieNode node = root;
            int i;
            for (char c : w) {
                i = c - 'a';
                if (node.map[i].path-- == 1) {
                    node.map[i] = null;
                    return;
                }
                node = node.map[i];
            }
            node.end--;
        }
    }

    /**
     * 查询word是否在字典树中
     * 从左到右遍历word中的每个字符，并依次从头节点开始，根据每一个word[i]查找下一个节点。
     * 如果未找到，说明这个单词的整个部分没有添加进字典树，否则找的过程中不可能节点未找到，直接返回false。
     * 如果能通过word[N-1]找到最后一个节点，记为e，如果e.end!=0说明有整个单词的所有字符，并以e结尾，返回true。
     * 如果e.end=0说明虽然有所有的字符，但没有到此结束，也就是word不存在，返回false
     */
    public boolean search(String word) {
        if (word == null || word.length() == 0) {
            return false;
        }
        char[] w = word.toCharArray();
        TrieNode node = root;
        int i;
        for (char c : w) {
            i = c - 'a';
            if (node.map[i] == null) {
                return false;
            }
            node = node.map[i];
        }
        return node.end != 0;
    }

    /**
     * 查找以pre为前缀的单词数量
     * 和查找单词的过程相同，先根据pre不断找节点，找完之后假设最后的节点为e，返回e.path即可
     */
    public int prefixNum(String pre) {
        if (pre == null || pre.length() == 0) {
            return 0;
        }
        char[] w = pre.toCharArray();
        TrieNode node = root;
        int i;
        for (char c : w) {
            i = c - 'a';
            if (node.map[i] == null) {
                return 0;
            }
            node = node.map[i];
        }
        return node.path;
    }

    public static void main(String[] args) {
        TrieTree tree = new TrieTree();
        tree.insert("abc");
        tree.insert("abcd");
        tree.insert("abd");
        tree.insert("b");
        tree.insert("bcd");

        System.out.println(tree.prefixNum("ab"));
        System.out.println(tree.prefixNum("b"));
        System.out.println(tree.prefixNum("abc"));
        System.out.println(tree.prefixNum("bce"));

        System.out.println(tree.search("a"));
        System.out.println(tree.search("b"));
        System.out.println(tree.search("abcde"));
        System.out.println(tree.search("abd"));

        tree.insert("abd");
        tree.delete("abd");
        System.out.println(tree.search("abd"));
        tree.delete("abd");
        System.out.println(tree.search("abd"));
    }
}
