package com.ssaw.commons.security;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 * @author HuSen
 * @date 2019/4/8 16:39
 */
@Slf4j
public class Hmacsha1Util extends BaseUtil {
    private static final String MAC_NAME = "HmacSHA1";

    private static final String ENCODING = "UTF-8";

    /**
     * 使用HMAC-SHA1 签名方法对内容进行签名
     * 在发送方和接收方共享机密秘钥的前提下，HMAC可用于确定通过不安全信道的消息是否已被篡改
     *
     * @param content    待签名的内容
     * @param encryptKey 秘钥
     * @return 签名
     */
    public static String encrypt(String content, String encryptKey) {
        try {
            byte[] data = encryptKey.getBytes(Charset.forName(ENCODING));

            // 根据给定的字节数组构造一个秘钥，第二参数指定一个秘钥算法的名称
            SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);

            // 生成一个指定MAC算法的Mac对象
            Mac mac = Mac.getInstance(MAC_NAME);

            // 用给定秘钥初始化Mac对象
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(content.getBytes(Charset.forName(ENCODING)));
            return byteArrayToHexString(bytes);
        } catch (Exception e) {
            log.error("HMAC-SHA1 encrypt exception:", e);
        }

        return null;
    }
}