package com.my.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbAccess {

    private Connection con = null;
    private Statement ps = null;
    private ResultSet rs = null;

    public void open() {
        String driverName = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://localhost/test_db?characterEncoding=UTF-8&serverTimezone=Japan&useSSL=false";
        String userId = "test_user";
        String userPass = "test_pass";

        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(jdbcUrl, userId, userPass);
            ps = con.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql) {
        if (ps != null) {
            try {
                rs = ps.executeQuery(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

    public int executeUpdate(String sql) throws SQLException {
        int num = 0;
        if (ps != null) {
            num = ps.executeUpdate(sql);
        }
        return num;

    }
}
