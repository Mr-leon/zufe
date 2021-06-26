package org.smartjq.test;

import java.security.Key;
import java.util.Map;

import org.smartjq.plugin.util.RSACoder;

/**
 * Created by lake on 17-4-12.
 */
public class RSACoderTest {
    private String publicKey;
    private String privateKey;

    public void setUp() throws Exception {
        Map<String, Key> keyMap = RSACoder.initKey();
        publicKey = RSACoder.getPublicKey(keyMap);
        privateKey = RSACoder.getPrivateKey(keyMap);
        System.err.println("公钥：" + publicKey);
        System.err.println("私钥：" + privateKey);
    }

    public void test() throws Exception {
        System.err.println("公钥加密——私钥解密");
        String inputStr = "abc";
        byte[] encodedData = RSACoder.encryptByPublicKey(inputStr, publicKey);
        byte[] decodedData = RSACoder.decryptByPrivateKey(encodedData,
                privateKey);
        String outputStr = new String(decodedData);
        String enoutputStr = new String(encodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "加密后: " + enoutputStr);
    }

    public void testSign() throws Exception {
        System.err.println("私钥加密——公钥解密");
        String inputStr = "sign";
        byte[] data = inputStr.getBytes();
        byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);
        byte[] decodedData = RSACoder.decryptByPublicKey(encodedData, publicKey);
        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
        System.err.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = RSACoder.sign(encodedData, privateKey);
        System.err.println("签名:" + sign);
        // 验证签名
        boolean status = RSACoder.verify(encodedData, publicKey, sign);
        System.err.println("状态:" + status);
    }
    
    public static void main(String[] args) throws Exception {
    	RSACoderTest test = new RSACoderTest();
    	test.setUp();
    	test.testSign();
    }
}