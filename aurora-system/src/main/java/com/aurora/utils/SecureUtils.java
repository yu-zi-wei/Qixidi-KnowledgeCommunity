package com.aurora.utils;

import cn.hutool.core.codec.Base62;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 加密工具栏
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecureUtils {


    /**
     * Base62加密
     *
     * @param data
     * @return
     */
    public static String Base62Encryption(String data) {
        return Base62.encode(data);
    }

    /**
     * Base62解密
     *
     * @param data
     * @return
     */
    public static String Base62Decrypt(String data) {
        return Base62.decodeStr(data);
    }

    /**
     * Base62 比对
     *
     * @param data       明文
     * @param ciphertext 密文
     * @return
     */
    public static Boolean comparison(String data, String ciphertext) {
        String decrypt = Base62Decrypt(ciphertext);
        if (decrypt.equals(data)) {
            return true;
        }
        return false;
    }

    /**
     * MD5-SM3 加密
     *
     * @param data
     * @return
     */
    public static String digesters(String data) {
        Digester digester = DigestUtil.digester("sm3");
        String digestHex = digester.digestHex(data);
        return digestHex;
    }


    /**
     * 对字符进行加密，加密原理：当前字符的后一个字符并且是大写
     * 如果是数字则加1就好了，这里要注意如果是小写z,那么对应大写A.
     * 大写Z对象小写a.
     * <p>
     * 加密
     *
     * @param c
     * @return
     */
    public static char encryption(char c) {
        if (c >= 'a' && c < 'z')
            return (char) (c + 1 - 32);
        else if (c == 'z')
            return 'A';
        else if (c >= 'A' && c < 'Z')
            return (char) (c + 1 + 32);
        else if (c == 'Z')
            return 'a';
        else if (c >= '0' && c < '9')
            return (char) (c + 1);
        else if (c == '9')
            return '0';
        else
            return c;
    }


    /**
     * 字符解密
     *
     * @param c
     * @return
     */
    public static char decryption(char c) {
        if (c > 'a' && c <= 'z')
            return (char) (c - 1 - 32);
        else if (c == 'a')
            return 'Z';
        else if (c > 'A' && c <= 'Z')
            return (char) (c - 1 + 32);
        else if (c == 'A')
            return 'z';
        else if (c > '0' && c <= '9')
            return (char) (c - 1);
        else if (c == '0')
            return '9';
        else
            return c;
    }

    //加密，将输入的字符串中的每个字符进行加密。
    public static String enCryption(String s) {
        char[] cs = s.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cs.length; i++) {
            sb.append(encryption(cs[i]));
        }
        return sb.toString();
    }

    //解密
    public static String deCryption(String s) {
        char[] cs = s.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cs.length; i++) {
            sb.append(decryption(cs[i]));
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        String s = Base62Encryption("123456");
        String s1 = Base62Decrypt(s);
        Boolean comparison = comparison("123456", s);

        System.out.println(s + "-" + s1 + "-" + comparison);
    }

}
