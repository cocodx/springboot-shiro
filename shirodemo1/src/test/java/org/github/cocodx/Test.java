package org.github.cocodx;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

/**
 * @author amazfit
 * @date 2022-10-14 上午12:56
 **/
public class Test {

    @org.junit.Test
    public void test1() {
        //1. 初始化SecurityManager
        //2. 获取Subject对象
        //3. 创建好token对象
        //4. 调用Subject的login方法，把对象传进去，完成登录

        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        //获取subject对象
        Subject subject = SecurityUtils.getSubject();
        // 创建token对象，web应用从页面传递
        AuthenticationToken token = new UsernamePasswordToken("zhangsan","123456");
        // 完成登录
        try{
            subject.login(token);
            System.out.println("login success");

            //判断角色
            boolean role1 = subject.hasRole("role1");
            System.out.println("是否拥有role1："+role1);

            boolean permitted = subject.isPermitted("user:insert");
            System.out.println("是否拥有用户插入权限："+permitted);

            //没有权限，会抛出异常
            subject.checkPermission("user:insert1");
        }catch (UnknownAccountException e){
            System.out.println("unknown account");
        }catch (IncorrectCredentialsException e){
            System.out.println("password error");
        }
    }
}
