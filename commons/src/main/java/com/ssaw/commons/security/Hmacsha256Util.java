package com.ssaw.commons.security;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 * @author HuSen
 * @date 2019/4/8 17:05
 */
@Slf4j
public class Hmacsha256Util extends BaseUtil {

    private static final String MAC_NAME = "HmacSHA256";

    private static final String ENCODING = "UTF-8";

    /**
     * 使用HMAC-SHA256进行签名
     *
     * @param content 待签名的内容
     * @param key     秘钥
     * @return 签名
     */
    public static String encrypt(String content, String key) {
        try {
            Mac mac = Mac.getInstance(MAC_NAME);
            SecretKey secretKey = new SecretKeySpec(key.getBytes(Charset.forName(ENCODING)), MAC_NAME);
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(content.getBytes(Charset.forName(ENCODING)));
            return byteArrayToHexString(bytes);
        } catch (Exception e) {
            log.error("HMAC-SHA256 encrypt exception:", e);
        }

        return null;
    }
}