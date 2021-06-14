/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinlt.dao;

import dto.UserDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import tinlt.util.DBUtils;

/**
 *
 * @author tuanv
 */
public class UserDAO {

    public UserDTO checkLogin(String userID, String password) throws SQLException, NamingException {
        Connection conn = null;
        UserDTO user = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT a.address,a.createdate,a.phone,a.userName,a.verifyCode,a.email,b.roleName\n"
                    + "FROM Users a JOIN Roles b on a.roleID=b.roleID\n"
                    + "WHERE a.userID=? AND a.password=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, userID);
            pst.setString(2, password);
            rs = pst.executeQuery();
            if (rs.next()) {
                user = new UserDTO(userID, rs.getString("userName"), password, rs.getString("phone"), rs.getString("address"), rs.getString("roleName"), rs.getString("verifyCode"), rs.getString("createdate"), rs.getString("email"));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return user;
    }
    
    public boolean checkUSer(String userID) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT userName\n"
                    + "FROM Users\n"
                    + "WHERE userID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, userID);
            rs = pst.executeQuery();
            if (rs.next()) {
                return false;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return true;
    }

    public void createAcount(UserDTO user) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBUtils.getConnection();
            String url = "INSERT INTO Users([userID],[userName],[password],[phone],[address],[createdate],[verifyCode],[email],[roleID])\n"
                    + "VALUES(?,?,?,?,?,?,?,'','1')";
            pst = conn.prepareStatement(url);
            pst.setString(1, user.getUserID());
            pst.setString(2, user.getName());
            pst.setString(3, user.getPassword());
            pst.setString(4, user.getPhone());
            pst.setString(5, user.getAddress());
            pst.setString(6, user.getCreateDate());
            pst.setString(7, user.getVerifyCode());
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public String getVerifyCode(String userID) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String verifyCode = "";
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT verifyCode\n"
                    + "FROM users\n"
                    + "WHERE userID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, userID);
            rs = pst.executeQuery();
            if (rs.next()) {
                verifyCode = rs.getString("verifyCode");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return verifyCode;
    }

    public boolean checkEmail(String email, String userID) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT userID\n"
                    + "FROM Users\n"
                    + "WHERE email=? AND userID!=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, email);
            pst.setString(2, userID);
            rs = pst.executeQuery();
            if (rs.next()) {
                return false;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return true;
    }

    public void updateEmail(String userID, String email) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBUtils.getConnection();
            String url = "UPDATE Users\n"
                    + "SET	email=?\n"
                    + "WHERE userID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, email);
            pst.setString(2, userID);
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void updateRole(String userID) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBUtils.getConnection();
            String url = "UPDATE Users\n"
                    + "SET	roleID='2'\n"
                    + "WHERE userID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, userID);
            pst.executeUpdate();
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
