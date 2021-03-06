package org.smartjq.plugin.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lake on 17-4-12.
 */
public class RSACoder {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAN1bwr7a47rnKSvy\r\n" + 
    		"DyswFRzHaNQXXBYXhlaff6fYlyYnD4olTxOYtEGeKnDztem320wcZNCxDl1JUz4B\r\n" + 
    		"N6KK3EwrYQmiu6jAExdfjIPclSwZIu9rxcak8GDZBCFX7RcHFUDPiOVv7oC5zmxq\r\n" + 
    		"1AgInrIdUrt1+gk8mXzi7FIFpgtrAgMBAAECgYB5wmyn8N1NRLP38Uj4t5UVZHa8\r\n" + 
    		"GzFTEZOaeFeOquvRV7ELiyQpIlkcsvSjdtr8eV6OShwGUy4UkC3SsHa62Oj9koxm\r\n" + 
    		"kEjL3RZZccl1Bey/16DbdRFCTiE/AUniMx4mci2UJyqh93Tbg+a4LA3sdFYrce1x\r\n" + 
    		"6WZtacKeGyO9f1SPYQJBAPz3KYkNKQyOgCtn6RdkgiI1KDr5WUj9tLfjjyi/i3CW\r\n" + 
    		"72khraTBI7b1tq+kbQrEquqCmRbufXV6RA8/S6GJ9psCQQDgA4khk21UpB/XdKOj\r\n" + 
    		"I+lQWjPM9+u/eIT7gSskblIVyzP1kBCD0rh+X98FRk/vQPcSBsROfw83lltVoie9\r\n" + 
    		"KyNxAkEAxsq9O1S6Q4Hguzunc5iqo5M+kEnPnoqx8CjGIfGU9N/IOMgcm3KkTG2A\r\n" + 
    		"KTt/7Yxr/GqxP9IfiaiRv6GwH7w5rQJAI7rMo4tVM9vdrErRUtMGgqdwfa/JbUe2\r\n" + 
    		"xjqq++uQsiMwDWnGCJCSlq2J79hwJXQSNFDZ/L8tWStlM/piJjz8MQJAQtVB37Js\r\n" + 
    		"9iFdKAgNTCqfEz/G4GC+MQoZM6oUnOnC2s2hqE5mvlrGPRKj0F4V8CzFC4XMiZWh\r\n" + 
    		"CQgn/UPulzuHBQ==";
    private static final String PRIVATE_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDdW8K+2uO65ykr8g8rMBUcx2jU\r\n" + 
    		"F1wWF4ZWn3+n2JcmJw+KJU8TmLRBnipw87Xpt9tMHGTQsQ5dSVM+ATeiitxMK2EJ\r\n" + 
    		"oruowBMXX4yD3JUsGSLva8XGpPBg2QQhV+0XBxVAz4jlb+6Auc5satQICJ6yHVK7\r\n" + 
    		"dfoJPJl84uxSBaYLawIDAQAB";

    public static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }

    public static String encryptBASE64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * ????????????????????????????????????
     *
     * @param data       ????????????
     * @param privateKey ??????
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // ?????????base64???????????????
        byte[] keyBytes = decryptBASE64(privateKey);
        // ??????PKCS8EncodedKeySpec??????
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM ?????????????????????
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // ??????????????????
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // ????????????????????????????????????
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }

    /**
     * ??????????????????
     *
     * @param data      ????????????
     * @param publicKey ??????
     * @param sign      ????????????
     * @return ??????????????????true ????????????false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        // ?????????base64???????????????
        byte[] keyBytes = decryptBASE64(publicKey);
        // ??????X509EncodedKeySpec??????
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM ?????????????????????
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // ??????????????????
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        // ????????????????????????
        return signature.verify(decryptBASE64(sign));
    }

    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception{
        // ???????????????
        byte[] keyBytes = decryptBASE64(key);
        // ????????????
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // ???????????????
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * ??????<br>
     * ???????????????
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(String data, String key)
            throws Exception {
        return decryptByPrivateKey(decryptBASE64(data),key);
    }

    /**
     * ??????<br>
     * ???????????????
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key)
            throws Exception {
        // ???????????????
        byte[] keyBytes = decryptBASE64(key);
        // ????????????
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // ???????????????
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * ??????<br>
     * ???????????????
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(String data, String key)
            throws Exception {
        // ???????????????
        byte[] keyBytes = decryptBASE64(key);
        // ????????????
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // ???????????????
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes());
    }

    /**
     * ??????<br>
     * ???????????????
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key)
            throws Exception {
        // ???????????????
        byte[] keyBytes = decryptBASE64(key);
        // ????????????
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // ???????????????
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * ????????????
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * ????????????
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * ???????????????
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Key> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        Map<String, Key> keyMap = new HashMap(2);
        keyMap.put(PUBLIC_KEY, keyPair.getPublic());// ??????
        keyMap.put(PRIVATE_KEY, keyPair.getPrivate());// ??????
        return keyMap;
    }
}