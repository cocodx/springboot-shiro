package org.github.cocodx;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;

/**
 * shiro入门案例
 * @author amazfit
 * @date 2022-07-28 上午12:32
 **/
public class Test01 {

    public static void main(String[] args) {
        // 其中 shiro.ini 在 resources 的根目录下 老版本写法
        /**
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();**/

        //1、获取一个SecurityManager对象
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        IniRealm iniRealm = new IniRealm("classpath:shiro.ini");
        defaultSecurityManager.setRealm(iniRealm);

        //将securityManger对象加入到当前的运行环境中
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //使用subject就可以去进行认证
        Subject subject = SecurityUtils.getSubject();

        //获取用户提交的要认证的账号密码
        String userName = "root1";
        String password = "1234567";
        //将用户提交的账号密码封装成一个token
        AuthenticationToken token = new UsernamePasswordToken(userName, password);
        //完成认证操作
        subject.login(token);

        //获取认证状态
        System.out.println(subject.isAuthenticated());
    }
}
