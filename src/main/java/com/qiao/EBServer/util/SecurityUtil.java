package com.qiao.EBServer.util;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * <b>安全相关类</b>
 *
 * <p>主要包含:</p>
 * <p>1.对称与非对称加密</p>
 * <p>1.1 MD5,SHA,MAC(密钥)</P>
 * <p>2.与对称算法的加密与解密</p>
 * <p>2.1 BASE64(非密钥),DES(密钥)</p>
 * @author 林文富 2011-1-1
 * @see
 * @since 1.0
 */
public class SecurityUtil
{
    /**
     * 非对称加密算法MD5
     */
    public static final String ALGORITHM_MD5 = "MD5";  
    
    /**
     * 非对称加密算法SHA
     */
    public static final String ALGORITHM_SHA1 = "SHA-1";  
    
   
    
    /**
     * <p>非对称加密算法:</p>
     * <p>MAC算法可选以下多种算法</p>
     * 
     * <p>HmacMD5 HmacSHA1 HmacSHA256 HmacSHA384 HmacSHA512</p>
     * 
     */
    public static final String KEY_MAC = "HmacMD5";
    
    public static final String ALGORITHM_DES = "DES";
    
    private static final String HmacSHA="HmacSHA256";
    
    /**
     * BASE64解密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception
    {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception
    {
        return (new BASE64Encoder()).encodeBuffer(key);
    }
    
    /**
     * MD5加密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception
    {

        MessageDigest md5 = MessageDigest.getInstance(ALGORITHM_MD5);
        md5.update(data);

        return md5.digest();

    }

    /**
     * SHA加密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception
    {

        MessageDigest sha = MessageDigest.getInstance(ALGORITHM_SHA1);
        sha.update(data);

        return sha.digest();

    }
    
    /**
     * 初始化HMAC密钥
     * 
     * @return
     * @throws Exception
     */
    public static String getMacKey() throws Exception
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);

        SecretKey secretKey = keyGenerator.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }

    /**
     * HMAC加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptHMAC(byte[] data, String key) throws Exception
    {

        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);

        return mac.doFinal(data);
        
    }
    
    /**
     * 生成DES密钥
     * 
     * @param seed 生成密钥源数据，一般为要加密的数据
     *             为了生成密钥更安全，建议这个参数不为空
     * @return
     * @throws Exception
     */
    public static String getDESSecretKey(String... seed) throws Exception
    {
        if(seed != null && seed.length > 1)
        {
            throw new Exception("参数数组不能多于1个");
        }
        
        SecureRandom secureRandom = null;

        if (seed != null && seed.length > 0)
        {
            secureRandom = new SecureRandom(decryptBASE64(seed[0]));
        }
        else
        {
            secureRandom = new SecureRandom();
        }

        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_DES);
        kg.init(secureRandom);

        SecretKey secretKey = kg.generateKey();

        return encryptBASE64(secretKey.getEncoded());
    }
    
    /**
     * 将生成的DES密钥转换为密钥对象
     * 
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception
    {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
        SecretKey secretKey = keyFactory.generateSecret(dks);

        // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
        // SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);

        return secretKey;
    }
    
    /**
     * DES解密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptDES(byte[] data, String key) throws Exception
    {
        Key k = toKey(decryptBASE64(key));

        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        cipher.init(Cipher.DECRYPT_MODE, k);

        return cipher.doFinal(data);
    }

    /**
     * DES加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptDES(byte[] data, String key) throws Exception
    {
        Key k = toKey(decryptBASE64(key));
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        cipher.init(Cipher.ENCRYPT_MODE, k);

        return cipher.doFinal(data);
    }
    
    
    public static byte[] encodeHmacSHA256(byte[] data, String key) throws Exception {  
        SecretKey secretKey = new SecretKeySpec(key.getBytes(),HmacSHA);  
        // 实例化Mac  
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
        //初始化mac  
        mac.init(secretKey);  
        //执行消息摘要  
        byte[] digest = mac.doFinal(data);  
        return digest;
    } 

}
