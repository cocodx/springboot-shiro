package org.github.cocodx;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

import java.util.Arrays;

/**
 * shiro入门案例
 * @author amazfit
 * @date 2022-07-28 上午12:32
 **/
public class Test02 {

    public static void main(String[] args) {
        // 其中 shiro1.ini 在 resources 的根目录下 老版本写法
        /**
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro1.ini");
        SecurityManager securityManager = factory.getInstance();**/

        //1、获取一个SecurityManager对象
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
//        IniRealm iniRealm = new IniRealm("classpath:shiro1.ini");
        MyRealm myRealm = new MyRealm();

        //设置凭证匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置迭代次数1024
        hashedCredentialsMatcher.setHashIterations(1024);
        //设置散列的算法
        hashedCredentialsMatcher.setHashAlgorithmName("md5");

        myRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        defaultSecurityManager.setRealm(myRealm);

        //将securityManger对象加入到当前的运行环境中
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        //使用subject就可以去进行认证
        Subject subject = SecurityUtils.getSubject();

        //获取用户提交的要认证的账号密码
        String userName = "root";
        String password = "123456";
        //将用户提交的账号密码封装成一个token
        AuthenticationToken token = new UsernamePasswordToken(userName, password);
        //完成认证操作
        try{
            subject.login(token);
            System.out.println("登录成功。。。。。");
            //做权限的验证操作
            System.out.println("认证状态："+subject.isAuthenticated());
            System.out.println("是否具有role1角色："+subject.hasRole("role1"));
            System.out.println("是否具有role2角色："+subject.hasRole("role2"));
            System.out.println("是否具有role3角色："+subject.hasRole("role3"));
            boolean[] roles = subject.hasRoles(Arrays.asList("role1", "role2", "role3"));
            System.out.println(Arrays.toString(roles));

            System.out.println(subject.getPrincipal()
                    +"是有具有role1和role2两个角色："
                    +subject.hasAllRoles(Arrays.asList("role1","role2")));

            System.out.println(subject.getPrincipal()
                    +"是有具有role1和role3两个角色："
                    +subject.hasAllRoles(Arrays.asList("role1","role3")));
            //check开头的方法校验不通过会抛出异常
            subject.checkRole("role1");
            subject.checkRole("role3");

            // 做权限的验证
            System.out.println(subject.getPrincipal()
                    +"是否具有user:create权限"
                    +subject.isPermitted("user:create"));
            System.out.println(subject.getPrincipal()
                    +"是否具有user:delete权限"
                    +subject.isPermitted("user:delete"));

            //check 开头的校验方法不通过同样会抛出异常
            subject.checkPermission("user:query");

        }catch (UnknownAccountException exception){
            System.out.println("账号错误。。。。。");
        }catch (IncorrectCredentialsException e){
            System.out.println("密码错误。。。。。");
        }

        //获取认证状态
        System.out.println(subject.isAuthenticated());
    }
}
