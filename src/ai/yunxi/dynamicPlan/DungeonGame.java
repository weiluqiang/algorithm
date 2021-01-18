package ai.yunxi.dynamicPlan;

/**
 * 龙与地下城游戏问题
 * 给定一个二维数组map，含义是一张地图，例如：
 * -2 -3  3
 * -5 -10 1
 * 0  30  -5
 * 游戏的规则如下：
 * 1.骑士从左上角出发，每次只能向右或向下走，最后达到右下角见到公主
 * 2.地图中每个位置的值，代表骑士要遭遇的事情。如果是负数，代表此处有怪兽，骑士要损失血量。
 * 如果是非负数，代表此处有血瓶，能让骑士回血
 * 3.骑士从左上角到右下角的过程中，走到任何一个位置时，血量都不能少于1
 * 为了保证骑士能见到公主，初始血量至少是多少？根据map，返回初始血量
 */
public class DungeonGame {

    /**
     * 经典动态规划的方法：先定义和地图大小一样的矩阵dp，dp[i][j]的含义是要走上位置(i,j)，并且从该位置选一条最优的路径，
     * 最后走到右下角，骑士起码应该具备的血量。根据dp的定义，我么最终需要的是dp[0][0]的结果。
     * 以题目的例子来说，map[2][2]的值为-5，所以骑士要走上这个位置，需要6点血才能让自己不死。
     * 同时(2,2)已经是最右下角的位置，没有后续的路径，所以dp[2][2]=6
     * 那么dp[i][j]的值该怎么计算呢？
     * 骑士还是要面临向下或是向右的选择，dp[i][j+1]是骑士选择当前向右走，并最终到达右下角的血量要求。
     * 同理，dp[i+1][j]是向下走的要求。如果骑士决定向右走，那么骑士在当前位置加完血或者扣完血之后的血量只要等于dp[i][j+1]即可
     * 那么骑士在加血或扣血之前的血量要求，就是dp[i][j+1]-map[i][j]。同时骑士的血量要所示不少于1，
     * 所以向右的要求为max{dp[i][j+1]-map[i][j],1}。如果骑士决定向下走，同理可知，向下的要求为max{dp[i+1][j]-map[i][j],1}
     * 骑士有两种选择，要选其中最优的一条，所以dp[i][j]=min{向右的要求,向下的要求}。
     * 计算dp矩阵时从右下角开始，然后依次从右到左、再从下到上计算即可。
     */
    public int minHP(int[][] map) {
        if (map == null || map.length == 0 || map[0] == null || map[0].length == 0) return 1;
        int row = map.length;
        int col = map[0].length;
        int[][] dp = new int[row--][col--];
        dp[row][col] = map[row][col] >= 0 ? 1 : 1 - map[row][col];
        // 最后一行的数据
        for (int j = col - 1; j >= 0; j--) {
            dp[row][j] = Math.max(dp[row][j + 1] - map[row][j], 1);
        }
        // 其他数据
        int right;
        int down;
        for (int i = row - 1; i >= 0; i--) {
            dp[i][col] = Math.max(dp[i + 1][col] - map[i][col], 1);
            for (int j = col - 1; j >= 0; j--) {
                right = Math.max(dp[i][j + 1] - map[i][j], 1);
                down = Math.max(dp[i + 1][j] - map[i][j], 1);
                dp[i][j] = Math.min(right, down);
            }
        }
        return dp[0][0];
    }

    /**
     * 结合空间压缩的方法
     */
    public int minHP1(int[][] map) {
        if (map == null || map.length == 0 || map[0] == null || map[0].length == 0) return 1;
        int more = Math.max(map.length, map[0].length);
        int less = Math.min(map.length, map[0].length);
        boolean rowMore = map.length >= map[0].length;
        int[] dp = new int[less];
        int tmp = map[map.length - 1][map[0].length - 1];
        dp[less - 1] = tmp >= 0 ? 1 : 1 - tmp;
        int row;
        int col;
        for (int j = less - 2; j >= 0; j--) {
            row = rowMore ? more - 1 : j;
            col = rowMore ? j : more - 1;
            dp[j] = Math.max(dp[j + 1] - map[row][col], 1);
        }
        int choose1;
        int choose2;
        for (int i = more - 2; i >= 0; i--) {
            row = rowMore ? i : less - 1;
            col = rowMore ? less - 1 : i;
            dp[less - 1] = Math.max(dp[less - 1] - map[row][col], 1);
            for (int j = less - 2; j >= 0; j--) {
                row = rowMore ? i : j;
                col = rowMore ? j : i;
                choose1 = Math.max(dp[j] - map[row][col], 1);
                choose2 = Math.max(dp[j + 1] - map[row][col], 1);
                dp[j] = Math.min(choose1, choose2);
            }
        }
        return dp[0];
    }

    public static void main(String[] args) {
        DungeonGame game = new DungeonGame();
        int[][] map = {{-2, -3, 3}, {-5, -10, 1}, {0, 30, -5}};
        System.out.println(game.minHP(map));
        System.out.println(game.minHP1(map));
    }
}
