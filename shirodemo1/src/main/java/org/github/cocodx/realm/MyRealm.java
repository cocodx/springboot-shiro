package org.github.cocodx.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

/**
 * @author amazfit
 * @date 2022-10-14 上午1:49
 **/
public class MyRealm extends AuthenticatingRealm {

    /**
     * 创建自定义的登录认证方法
     *
     * 配置自定义的realm生效，1.在ini文件中可以配置 2.在springboot框架中进行配置
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1. 获取身份信息
        String principal = token.getPrincipal().toString();

        //2. 获取凭证信息
        String password = new String((char[]) token.getCredentials());

        //3. 如果是真正做真正登录获取数据库登录信息
        if (principal.equals("zhangsan")){
            AuthenticationInfo info = new SimpleAuthenticationInfo(principal,"6cd90c6dd4c2123410dd9a90fba5e932", ByteSource.Util.bytes("1234567890"),"userName");
            return info;
        }

        //4. 创建封装逻辑对象，封装数据返回

        System.out.println("principal = " + principal);
        System.out.println("password = " + password);
        return null;
    }
}
