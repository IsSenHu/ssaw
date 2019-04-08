package com.ssaw.commons.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES 加解密实现
 *
 * @author HuSen
 * @date 2019/4/8 15:38
 */
@Slf4j
public class AesUtil {
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] bytes = content.getBytes(Charset.forName("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
            byte[] result = cipher.doFinal(bytes);
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            log.error("AES encrypt exception:", e);
        }

        return null;
    }

    /**
     * AES解密操作
     *
     * @param content  待解密内容
     * @param password 加密密码
     * @return 返回解密后的数据
     */
    public static String decrypt(String content, String password) {
        try {
            // 实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            // 使用秘钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));

            // 执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));
            return new String(result, Charset.forName("UTF-8"));
        } catch (Exception e) {
            log.error("AES decrypt exception:", e);
        }

        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @param password 加密密码
     * @return 加密秘钥
     */
    private static SecretKeySpec getSecretKey(final String password) {
        // 返回生成指定算法秘钥生成器的KeyGenerator对象
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);

            // AES要求长度为128
            kg.init(128, new SecureRandom(password.getBytes()));

            // 生成一个秘钥
            SecretKey secretKey = kg.generateKey();

            // 转换为AES专用秘钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.error("getSecretKey occur exception:", e);
        }

        return null;
    }
}