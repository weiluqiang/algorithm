package ai.yunxi.string;

import java.util.HashMap;
import java.util.Map;

/**
 * 数组中两个字符串的最小距离
 * 给定一个字符串数组str，再给定两个字符串s1和s2，返回在str中s1与s2的最小距离。
 * 如果s1或s2为null，或者不在str中，返回-1
 * 例如，str=["1","3","3","3","2","3","1"]，s1="1"，s2="2"，返回2
 * str=["CD"]，s1="CD"，s2="AB"，返回-1
 * <p>
 * 进阶题目：如果查询发生的次数有很多，如何把每次查询的时间复杂度降为O(1)
 */
public class MinDistance {

    /**
     * 原问题：从左到右遍历str，用变量last1记录最近一次出现s1的位置，last2记录最近一次出现s2的位置。
     * 如果遍历到s1，那么i-last2就是当前的s1和左边离它最近的s2之间的距离。
     * 如果遍历到s2，那么i-last1就是当前的s2和左边离它最近的s1之间的距离。
     * 用min记录这些距离的最小值即可。
     */
    public int min(String[] str, String s1, String s2) {
        if (str == null || str.length == 0 || s1 == null || s2 == null) return -1;
        if (s1.equals(s2)) return 0;
        int last1 = -1;
        int last2 = -1;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < str.length; i++) {
            if (s1.equals(str[i])) {
                last1 = i;
                if (last2 != -1) {
                    min = Math.min(min, i - last2);
                }
            } else if (s2.equals(str[i])) {
                last2 = i;
                if (last1 != -1) {
                    min = Math.min(min, i - last1);
                }
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    private Map<String, Map<String, Integer>> record;

    public void setRecord(String[] str) {
        if (str != null) {
            record = new HashMap<>();
            // indexMap记录遍历中每个字符串最后一次出现的位置
            Map<String, Integer> indexMap = new HashMap<>();
            for (int i = 0; i < str.length; i++) {
                String cur = str[i];
                updateMap(indexMap, cur, i);
                indexMap.put(cur, i);
            }
        }
    }

    // 生成最小距离记录
    private void updateMap(Map<String, Integer> indexMap, String s, int i) {
        Map<String, Integer> strMap = record.computeIfAbsent(s, k -> new HashMap<>());
        for (Map.Entry<String, Integer> entry : indexMap.entrySet()) {
            String key = entry.getKey();
            int index = entry.getValue();
            if (!key.equals(s)) {
                Map<String, Integer> lastMap = record.get(key);
                int curMin = i - index;
                if (strMap.containsKey(key)) {
                    int preMin = strMap.get(key);
                    if (curMin < preMin) {
                        strMap.put(key, curMin);
                        lastMap.put(s, curMin);
                    }
                } else {
                    strMap.put(key, curMin);
                    lastMap.put(s, curMin);
                }
            }
        }
    }

    /**
     * 进阶问题：就是通过数组str先生成某种记录，查找的时候直接从记录中查找。
     * 本解法实现的记录是一个哈希表，它的key为String类型，value为哈希表类型。
     * 为了描述清楚，我们把这个哈希表叫做外哈希表，把value代表的哈希表叫做内哈希表。
     * 外哈希表的key代表str中的某个字符串，key对应的内哈希表代表其他字符串到key字符串的最小距离。
     * 如果str的长度为N，那么生成记录的时间复杂度为O(N^2)，记录的空间复杂度为O(N^2)，查询的时间复杂度为O(1)
     */
    public int min1(String[] str, String s1, String s2) {
        if (str == null || str.length == 0 || s1 == null || s2 == null) return -1;
        if (s1.equals(s2)) return 0;
        if (record.containsKey(s1)) {
            Map<String, Integer> strMap = record.get(s1);
            if (strMap.containsKey(s2)) {
                return strMap.get(s2);
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        MinDistance distance = new MinDistance();
        String[] str = {"1", "3", "3", "3", "2", "3", "1"};
        String[] str1 = {"CD"};
        System.out.println(distance.min(str, "1", "2"));
        System.out.println(distance.min(str1, "CD", "AB"));
        distance.setRecord(str1);
        System.out.println(distance.min1(str1, "CD", "AB"));
        distance.setRecord(str);
        System.out.println(distance.min1(str, "1", "2"));
    }
}
