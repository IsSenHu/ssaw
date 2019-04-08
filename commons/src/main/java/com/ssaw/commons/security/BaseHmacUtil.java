package com.ssaw.commons.security;

/**
 * @author HuSen
 * @date 2019/4/8 17:32
 */
class BaseHmacUtil {

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    static String byteArrayToHexString(byte[] bytes) {
        StringBuilder hs = new StringBuilder();
        String temp;
        for (byte b : bytes) {
            temp = Integer.toHexString(b & 0XFF);
            if (temp.length() == 1) {
                hs.append("0");
            }
            hs.append(temp);
        }
        return hs.toString();
    }
}