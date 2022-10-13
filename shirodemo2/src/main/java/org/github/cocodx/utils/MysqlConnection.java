package org.github.cocodx.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author amazfit
 * @date 2022-07-28 上午1:16
 **/
public class MysqlConnection {

    public static Connection connect(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shiro?useUnicode=true&amp;characterEncoding=utf8&serverTimeZone=GMT%2B8","root","password");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
