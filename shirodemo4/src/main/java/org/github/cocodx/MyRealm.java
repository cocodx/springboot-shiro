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

    /**
     * 登录之后，用户具有哪些权限，在这里执行
     * PrincipalCollection
     * @param principals 认证方法中返回的AuthenticationInfo中的第一个参数
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取到当前账号
        String userName = principals.getPrimaryPrincipal().toString();
        System.out.println("当前登录的账号："+userName);
        //根据登录的账号去查询。用户所拥有的信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Connection connect = MysqlConnection.connect();
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;
        String sql = "select r.* from sys_user u \n" +
                "LEFT JOIN sys_user_role ur on u.id=ur.user_id\n" +
                "LEFT JOIN sys_role r on r.id=ur.role_id\n" +
                "where u.user_name=?";

        String sql1 = "select p.* from sys_role r\n" +
                "LEFT JOIN sys_role_permission rp on r.id=rp.role_id\n" +
                "LEFT JOIN sys_permission p on rp.permission_id=p.id\n" +
                "where r.role_name in (\n" +
                "\tselect r.role_name from sys_user u \n" +
                "LEFT JOIN sys_user_role ur on u.id=ur.user_id\n" +
                "LEFT JOIN sys_role r on r.id=ur.role_id\n" +
                "where u.user_name=?\n" +
                ");";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1,userName);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                long roleId = resultSet.getLong("id");
                String roleName = resultSet.getString("role_name");
                info.addRole(roleName);
            }
            PreparedStatement preparedStatement2 = connect.prepareStatement(sql1);
            preparedStatement2.setString(1,userName);
            resultSet1 = preparedStatement2.executeQuery();
            while (resultSet1.next()){
                long permissionId = resultSet1.getLong("id");
                String permission = resultSet1.getString("permission");
                String remark = resultSet1.getString("remark");
                info.addStringPermission(permission);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return info;
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
