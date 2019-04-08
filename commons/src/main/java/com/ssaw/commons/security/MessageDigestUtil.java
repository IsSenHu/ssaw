package com.ssaw.commons.security;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author HuSen
 * @date 2019/4/8 17:43
 */
@Slf4j
public class MessageDigestUtil extends BaseUtil {

    /**
     * 使用指定哈希算法计算摘要信息
     *
     * @param content   内容
     * @param algorithm 哈希算法
     * @return 内容摘要
     */
    public static String getMD5Digest(String content, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(content.getBytes(Charset.forName("UTF-8")));
            return byteArrayToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("getMD5Digest exception:", e);
        }
        return null;
    }
}