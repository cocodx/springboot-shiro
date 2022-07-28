package org.github.cocodx;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author amazfit
 * @date 2022-07-28 下午9:38
 **/
public class MD501 {

    public static void main(String[] args) {
        //单个信息加密
        Md5Hash md5Hash = new Md5Hash("123456");

        System.out.println(md5Hash);

        //加密添加盐值
        Md5Hash md5Hash1 = new Md5Hash("123456", "123");
        System.out.println(md5Hash1);

        //加密添加盐值，及增加迭代次数
        Md5Hash md5Hash2 = new Md5Hash("123456", "123", 1024);
        System.out.println(md5Hash2);
    }
}
