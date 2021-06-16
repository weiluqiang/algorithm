package ai.yunxi.tree.pojo;

/**
 * 字典树节点
 */
public class TrieNode {

    // path表示有多少个单词公用这个节点
    public int path;
    // end表示有多少个单词以这个节点为结尾
    public int end;
    // map是一个哈希表，key代表该节点的一条字符路径，value代表字符路径指向的节点
    // 根据题目，将map设计成了长度为26的数组，在字符种类较多的时候也可采用真正的哈希表
    public TrieNode[] map;

    public TrieNode() {
        path = 0;
        end = 0;
        map = new TrieNode[26];
    }
}
