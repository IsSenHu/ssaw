package com.ssaw.commons.security;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Date;

/**
 * @author HuSen
 * @date 2019/4/8 17:53
 */
@Slf4j
public class RsaUtil extends BaseUtil {

    private static final String RSA_NAME = "RSA";

    private static final String ENCODING = "UTF-8";

    /**
     * 生成私钥 公钥
     *
     * @param publicKeyPath  公钥保存路径
     * @param privateKeyPath 私钥保存路径
     */
    public static void generate(String publicKeyPath, String privateKeyPath) {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(RSA_NAME);
            SecureRandom secureRandom = new SecureRandom(Date.from(Instant.now()).toString().getBytes());
            keyPairGenerator.initialize(1024, secureRandom);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            FileOutputStream fileOutputStream = new FileOutputStream(publicKeyPath);
            fileOutputStream.write(publicKeyBytes);
            fileOutputStream.close();

            byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
            fileOutputStream = new FileOutputStream(privateKeyPath);
            fileOutputStream.write(privateKeyBytes);
            fileOutputStream.close();
        } catch (Exception e) {
            log.error("RSA generate public/private key fail:", e);
        }
    }

    /**
     * 获取公钥
     *
     * @param filename 公钥文件路径
     * @return 公钥
     */
    public static PublicKey getPublicKey(String filename) {
        try {
            byte[] bytes = getKeyBytes(filename);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
            KeyFactory instance = KeyFactory.getInstance(RSA_NAME);
            return instance.generatePublic(spec);
        } catch (Exception e) {
            log.error("RSA getPublicKey fail:", e);
        }
        return null;
    }

    /**
     * 获取私钥
     *
     * @param fileName 私钥文件路径
     * @return 私钥
     */
    public static PrivateKey getPrivateKey(String fileName) {
        try {
            byte[] bytes = getKeyBytes(fileName);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory instance = KeyFactory.getInstance(RSA_NAME);
            return instance.generatePrivate(spec);
        } catch (Exception e) {
            log.error("RSA getPrivateKey fail:", e);
        }
        return null;
    }

    /**
     * 获取秘钥字节数组
     *
     * @param fileName 文件路径
     * @return 秘钥字节数组
     * @throws IOException IO异常
     */
    private static byte[] getKeyBytes(String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        byte[] bytes = new byte[(int) file.length()];
        dataInputStream.readFully(bytes);
        dataInputStream.close();
        return bytes;
    }

    /**
     * 加密
     *
     * @param content   待加密内容
     * @param publicKey 公钥
     * @return 加密结果
     */
    public static String encrypt(String content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = cipher.doFinal(content.getBytes(Charset.forName(ENCODING)));
            return byteArrayToHexString(bytes);
        } catch (Exception e) {
            log.error("RSA encrypt fail:", e);
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content    待解密内容
     * @param privateKey 私钥
     * @return 解密结果
     */
    public static String decrypt(String content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_NAME);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytes = cipher.doFinal(hexStringToByteArray(content));
            return new String(bytes, Charset.forName(ENCODING));
        } catch (Exception e) {
            log.error("RSA decrypt fail:", e);
        }
        return null;
    }
}