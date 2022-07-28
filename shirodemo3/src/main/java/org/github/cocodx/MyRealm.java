package org.github.cocodx;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author amazfit
 * @date 2022-07-28 上午1:08
 **/
public class MyRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Set<String> permissions = new HashSet<>();
        permissions.add("admin");
        SimpleAuthorizationInfo simpleAuth = new SimpleAuthorizationInfo();
        simpleAuth.addStringPermissions(permissions);
        return simpleAuth;
    }

    /**
     * 认证操作
     * @param token the authentication token containing the user's principal and credentials.
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken token1 = (UsernamePasswordToken) token;
        String userName = token1.getUsername();
        char[] password = token1.getPassword();
        System.out.println("登录的账号密码是："+userName+"   "+String.valueOf(password));
        Connection connect = MysqlConnection.connect();
        ResultSet resultSet = null;
        try {
            //防止sql注入
            String sql = "select * from sys_user where user_name=? ";
            PreparedStatement preparedStatement = connect.prepareStatement(sql);

            preparedStatement.setString(1,userName);
//            preparedStatement.setString(2, String.valueOf(password));

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Long id = resultSet.getLong("id");
                String userName1 = resultSet.getString("user_name");
//                String password1 = resultSet.getString("password");
                String password1 = "b2793335f43645fd8e00c7d18e14e05f";
                String salt = "123";
                AuthenticationInfo info = new SimpleAuthenticationInfo(userName1,password1,new SimpleByteSource(salt),"myRealm");
                return info;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet!=null){
                    resultSet.close();
                }
                if (connect!=null){
                    connect.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
