package org.github.cocodx;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

/**
 *
 * 使用shiro的内置加密
 *
 * @author amazfit
 * @date 2022-10-14 上午1:37
 **/
public class TestCrypto {

    @Test
    public void testMD5(){
        //密码明文
        String password = "z3";
        //使用md5加密
        Md5Hash md5Hash = new Md5Hash(password);
        System.out.println("md5Hash = " + md5Hash);

        //加点盐
        Md5Hash md5Hash1 = new Md5Hash(password, "1234567890");
        System.out.println("md5Hash1 = " + md5Hash1);

        //添加盐，迭代100次加密
        Md5Hash md5Hash2 = new Md5Hash(password, "1234567890", 100);
        System.out.println("md5Hash2 = " + md5Hash2);
    }

    /**
     * 使用父类进行加密
     */
    @Test
    public void testParentCrypto(){
        String password = "123456";
        //使用父类进行加密
        SimpleHash simpleHash = new SimpleHash("MD5", password, "salt", 3);
        System.out.println("父类带盐三次加密 = " + simpleHash.toHex());
    }
}
