package com.liu.shiromybatis.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * 加密工具类
 */
public class EncryptUtil {
    // hash算法名字
    public final static String HASH_ALGORITHM_NAME="MD5";
    // 循环次数
    public final static int ITERATION = 12;

    /**
     * 使用MD5加密方法
     * @param password
     * @param salt
     * @return 加密后的数据
     */
    public static String encrypt(String password,String salt){
        SimpleHash simpleHash = new SimpleHash(HASH_ALGORITHM_NAME, password, salt, ITERATION);

        return simpleHash.toString();
    }

    /**
     * 生成随机盐
     * @return 生成后的盐值
     */
    public static String generateSalt(){
        // 创建安全随机数生成器对象
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        return secureRandomNumberGenerator.nextBytes().toString();
    }



    public static void main(String[] args) {
        String salt = generateSalt();
        System.out.println(salt);
        System.out.println(encrypt("123456",salt));
        System.out.println(encrypt("123456",salt));
    }


}
