package ai.yunxi.encrypt;

/**
 * AES(Advanced Encryption Standard)高级加密标准，又称Rijndael加密法
 * 有五种加密模式：
 * 1.电码本模式 Electronic Codebook Book (ECB)
 * 2.密码分组链接模式 Cipher Block Chaining (CBC)
 * 3.计算器模式 Counter (CTR)
 * 4.密码反馈模式 Cipher FeedBack (CFB)
 * 5.输出反馈模式 Output FeedBack (OFB)
 */
public class AES {

    int dataLen = 16; //需要加密数据的长度
    int encLen = 4; //加密分段的长度
    int[] encTable = {1, 0, 1, 0}; //置换表
    int[] cipherText = new int[16]; //密文

    //切片加密函数
    void encode(int[] arr) {
        for (int i = 0; i < encLen; i++) {
            arr[i] = arr[i] ^ encTable[i];
        }
    }

    //数据明文切片
    int[][] slice(int[] arr, int x, int y) {
        int[][] a = new int[x][y];
        int i = 0; //位置变量
        for (int k = 0; k < x; k++) {
            for (int t = 0; t < y; t++) {
                a[k][t] = arr[i];
                i++;
            }
        }
        return a;
    }

    void print() {
        //输出密文
        for (int t1 = 0; t1 < dataLen; t1++) {
            if (t1 != 0 && t1 % 4 == 0) {
                System.out.println();
            }
            System.out.print(cipherText[t1] + "\t");
        }
        System.out.println();
        System.out.println("---------------------------------------------");
    }

    //电码本模式加密，4位分段
    void ECB(int[] arr) {
        //数据明文切片
        int[][] a = slice(arr, 4, 4);
        int index = 0;
        for (int i = 0; i < dataLen; i = i + encLen) {
            int r = i / encLen;//行
            int l = 0;//列
            int[] encQue = new int[4]; //编码片段
            for (int j = 0; j < encLen; j++) {
                encQue[j] = a[r][l];
                l++;
            }

            //切片加密
            encode(encQue);

            //添加到密文表中
            for (int p = 0; p < encLen; p++) {
                cipherText[index] = encQue[p];
                index++;
            }
        }
        System.out.println("ECB加密的密文为：");
        print();
    }

    //密码分组链接模式，4位分段
    void CCB(int[] arr) {
        int[][] a = slice(arr, 4, 4);
        int index = 0;//重置位置变量
        int[] init = {1, 1, 0, 0};//初始异或运算输入
        //初始异或运算
        for (int i = 0; i < dataLen; i = i + encLen) {
            int r = i / encLen;//行
            int[] encQue = new int[4]; //编码片段

            //初始化异或运算
            for (int k = 0; k < encLen; k++) {
                a[r][k] = a[r][k] ^ init[k];
            }

            //与Key加密的单切片
            if (encLen >= 0) System.arraycopy(a[r], 0, encQue, 0, encLen);

            //切片加密
            encode(encQue);

            //添加到密文表中
            for (int p = 0; p < encLen; p++) {
                cipherText[index] = encQue[p];
                index++;
            }

            //变换初始输入
            if (encLen >= 0) System.arraycopy(encQue, 0, init, 0, encLen);
        }
        System.out.println("CCB加密的密文为：");
        print();
    }

    //计算器模式，4位分段
    void CTR(int[] arr) {
        int[][] a = slice(arr, 4, 4);
        int index = 0;//重置位置变量
        int[][] init = {{1, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 1, 0}, {0, 1, 0, 0}}; //算子表
        int l = 0; //明文切片表列

        //初始异或运算
        for (int i = 0; i < dataLen; i = i + encLen) {
            int r = i / encLen;//行
            int[] encQue = new int[4]; //编码片段

            //将算子切片
            if (encLen >= 0) System.arraycopy(init[r], 0, encQue, 0, encLen);

            //算子与key加密
            encode(encQue);

            //最后的异或运算
            for (int k = 0; k < encLen; k++) {
                encQue[k] = encQue[k] ^ a[l][k];
            }
            l++;

            //添加到密文表中
            for (int p = 0; p < encLen; p++) {
                cipherText[index] = encQue[p];
                index++;
            }
        }
        System.out.println("CTR加密的密文为：");
        print();
    }

    //密码反馈模式，4位分段
    void CFB(int[] arr) {
        operate(arr, "CFB");
        System.out.println("CFB加密的密文为：");
        print();
    }

    //输出反馈模式，4位分段
    void OFB(int[] arr) {
        operate(arr, "OFB");
        System.out.println("OFB加密的密文为：");
        print();
    }

    private void operate(int[] arr, String mode) {
        //数据明文切片,切成2 * 8 片
        int[][] a = slice(arr, 8, 2);
        int index = 0; //恢复初始化设置
        int[] lv = {1, 0, 1, 1}; //初始设置的位移变量
        int[] encQue = new int[2]; //K的高两位
        int[] k = new int[4]; //K

        //外层加密循环
        for (int i = 0; i < 2 * encLen; i++) {
            //产生K
            if (encLen >= 0) System.arraycopy(lv, 0, k, 0, encLen);

            encode(k);

            System.arraycopy(k, 0, encQue, 0, 2);

            //K与数据明文异或产生密文
            for (int j = 0; j < 2; j++) {
                cipherText[index] = a[index / 2][j] ^ encQue[j];
                index++;
            }

            //lv左移变换
            lv[0] = lv[2];
            lv[1] = lv[3];
            if (mode.equals("OFB")) {
                lv[2] = encQue[0];
                lv[3] = encQue[1];
            } else {
                lv[2] = cipherText[index - 2];
                lv[3] = cipherText[index - 1];
            }
        }
    }

    public static void main(String[] args) {
        int[] data = {1, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0}; //明文
        AES aes = new AES();
        aes.ECB(data);
        aes.CCB(data);
        aes.CTR(data);
        aes.CFB(data);
        aes.OFB(data);
    }
}
