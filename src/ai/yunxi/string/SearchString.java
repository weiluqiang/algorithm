package ai.yunxi.string;

/**
 * 在有序但含有空的数组中查找字符串
 * 给定一个字符串数组str[]，在str中有些位置为null，但在不为null的位置上，
 * 其字符串是按照字典顺序由小到大依次出现的。再给定一个字符串是，返回s在str中出现的最左的位置。
 * 例如：str=[null,"a",null,"a",null,"b",null,"c"]
 * s="a"，返回1；s=null，只要为null就返回-1；s="d"，返回-1
 */
public class SearchString {

    /**
     * 本题解法尽可能多的使用了二分查找，具体过程如下：
     * 1.在str[left..right]上进行查找的过程，全局整型变量res表示字符串s在str中最左的位置。
     * 初始时，left=0，right=str.length-1，res=-1
     * 2.令mid=(left+right)/2，则str[mid]为str[left..right]中间位置的字符串
     * 3.如果字符串str[mid]和s一样，说明找到了s，令res=mid，但要找的是最左的位置，所以还要在左半区寻找，
     * 看看有没有更左的s出现，所以令right=mid-1，重复步骤2
     * 4.如果字符串str[mid]和s不一样，且str[mid]!=null，此时可以比较str[mid]和s，如果str[mid]的字典顺序比s小，
     * 说明整个左半区不会出现s，需要在右半区寻找，所以令left=mid+1，重复步骤2
     * 5.如果字符串str[mid]=null，此时从mid开始，从右到左遍历左半区。如果整个左半区都为null，
     * 那么继续用二分法在右半区上查找，令left=mid+1，然后重复步骤2。如果整个右半区不都为null，
     * 假设从右到左遍历时发现第一个不为null的位置为i，那么把s和str[i]进行比较。如果str[i]的字典顺序小于s，
     * 同样说明整个左半区没有s，令left=mid+1然后重复步骤2。如果str[i]=s，说明找到了s，令res=mid，
     * 要找最左的位置，所以还要在str[left..i-1]上继续寻找，看看有没有更左的s出现，令right=i-1重复步骤2。
     * 如果str[i]的字典顺序大于s，说明str[i..right]上都没有s，需要在str[left..i-1]上找，令right=i-1重复步骤2。
     */
    public int indexOf(String[] str, String s) {
        if (str == null || str.length == 0 || s == null) return -1;
        int res = -1;
        int left = 0;
        int right = str.length - 1;
        int mid;
        int i;
        while (left <= right) {
            mid = (left + right) / 2;
            if (str[mid] == null) {
                i = mid;
                while (str[i] == null) {
                    i--;
                    if (i < left) break;
                }
                if (i < left || str[i].compareTo(s) < 0) {
                    left = mid + 1;
                } else {
                    if (str[i].equals(s)) {
                        res = i;
                    }
                    right = i - 1;
                }
            } else if (str[mid].equals(s)) {
                res = mid;
                right = mid - 1;
            } else {
                if (str[mid].compareTo(s) < 0) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        SearchString search = new SearchString();
        String[] str = {null, null, "a", null, "a", null, "b", null, "c"};
        System.out.println(search.indexOf(str, "a"));
        System.out.println(search.indexOf(str, "d"));
        System.out.println(search.indexOf(str, null));
    }
}
