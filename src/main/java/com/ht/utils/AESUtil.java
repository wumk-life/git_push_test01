package com.ht.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AESUtil {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    //测试=====================
    //测试21
    //测试22


    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key 加密密钥
     * @param iv 向量
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String key, String iv) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec ivParameter = new IvParameterSpec(iv.getBytes("utf-8"));

            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key),  ivParameter);

            // 加密
            byte[] result = cipher.doFinal(content.getBytes("utf-8"));

            //通过Base64转码返回
            return Base64.encode(result);

        } catch (Exception ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content 密文
     * @param key 加密密钥
     * @param iv 向量
     * @return 返回解密后的明文
     */
    public static String decrypt(String content, String key, String iv) {

        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec ivParameter = new IvParameterSpec(iv.getBytes("utf-8"));

            // 使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key), ivParameter);

            // 解密
            byte[] result = cipher.doFinal(Base64.decode(content));

            // 返回解密后的明文
            return new String(result, "utf-8");
        } catch (Exception ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(String key) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);

            //兼容linux和window的内核
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes(Charset.forName("UTF-8")));

            //AES 要求密钥长度为 128
            kg.init(128, secureRandom);

            //生成一个密钥
            SecretKey secretKey = kg.generateKey();

            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static void main(String[] args) {
        //String content = "hello,您好";
        String content = "{\"trade_no\":\"2020052548100995\",\"serail_no\":\"202052513\",\"pay_price\":\"1500.00\",\"isbank\":\"10\",\"bank\":{\"bankName\":\"中国工商银行\",\"pay_name\":\"王某人\",\"accountNo\":\"26356245\",\"pay_time\":1590385384}}";
        String key = "Pinming9158@!~sc";
        String iv = "Htzc1@2!3~4Sc0o.";
        System.out.println("content:" + content);
        String s1 = AESUtil.encrypt(content, key, iv);
        System.out.println("s1:" + s1);
        System.out.println("s2:"+AESUtil.decrypt(s1, key, iv));

    }
}
