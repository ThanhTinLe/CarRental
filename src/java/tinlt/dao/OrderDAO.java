/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinlt.dao;

import dto.DiscountDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.RatingDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.naming.NamingException;
import tinlt.util.DBUtils;

/**
 *
 * @author tuanv
 */
public class OrderDAO {

    public DiscountDTO getDiscount(String disName, String date) throws SQLException, NamingException {
        DiscountDTO disdto = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT discountID,discountName,Disvalue,status FROM Discount WHERE discountName=? AND ? between [beginlDate] and [endDate]";
            pst = conn.prepareStatement(url);
            pst.setString(1, disName);
            pst.setString(2, date);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getBoolean("status")) {
                    disdto = new DiscountDTO(rs.getString("discountID"), disName, rs.getFloat("Disvalue"), rs.getBoolean("status"));
                }
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
        return disdto;
    }

    public int getMaxOrderID(String userID) throws SQLException, NamingException {
        int result = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT max(a.orderID)as id\n"
                    + "FROM tblOrder a JOIN Users b ON a.userID=b.userID\n"
                    + "WHERE a.userID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, userID);
            rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt("id");
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
        return result;
    }

    public void createOrder(OrderDTO cart) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBUtils.getConnection();
            String url = "INSERT INTO tblOrder([orderDate],[totalPrice],[userID],[discountID],[status]) \n"
                    + "VALUES(?,?,?,?,1)";
            pst = conn.prepareStatement(url);
            pst.setString(1, cart.getOrderDate());
            pst.setFloat(2, cart.getTotalPrice());
            pst.setString(3, cart.getUserID());
            pst.setString(4, cart.getDiscount().getDisID());
            HashMap<String, OrderDetailDTO> hash = cart.getHash();
            Set<String> key = hash.keySet();
            pst.executeUpdate();
            for (String cid : key) {
                int id = getMaxOrderID(cart.getUserID());
                createOrderDetail(hash.get(cid), getMaxOrderID(cart.getUserID()) + "");
            }
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void createOrderDetail(OrderDetailDTO detail, String orderID) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBUtils.getConnection();
            String url = "INSERT INTO OrderDetail([carID],[rentalDate],[returnDate],[quantity],[price],[orderID],[status])\n"
                    + "VALUES(?,?,?,?,?,?,1)";
            pst = conn.prepareStatement(url);
            pst.setString(1, detail.getCar().getCarID());
            pst.setString(2, detail.getRentalDate());
            pst.setString(3, detail.getReturnDate());
            pst.setString(4, detail.getQuantity() + "");
            pst.setFloat(5, detail.getPrice());
            pst.setString(6, orderID);
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

    public DiscountDTO getDiscountToHis(String disID) throws SQLException, NamingException {
        DiscountDTO disdto = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT discountID,discountName,Disvalue,status FROM Discount WHERE discountID=? ";
            pst = conn.prepareStatement(url);
            pst.setString(1, disID);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getBoolean("status")) {
                    disdto = new DiscountDTO(rs.getString("discountID"), rs.getString("discountName"), rs.getFloat("Disvalue"), rs.getBoolean("status"));
                }
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
        return disdto;
    }

    public ArrayList<OrderDTO> getOrder(String userID, String carName) throws SQLException, NamingException, ParseException {
        ArrayList<OrderDTO> list = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "Select a.orderID,orderDate,totalPrice,discountID,a.status,userID \n"
                    + "FROM tblOrder a JOIN OrderDetail b ON a.orderID=b.orderID JOIN car c ON b.carID=c.carID\n"
                    + "where userID=? AND c.carName LIKE ?\n"
                    + "order by orderDate desc";
            pst = conn.prepareStatement(url);
            pst.setString(1, userID);
            pst.setString(2, "%" + carName + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                OrderDTO odto = new OrderDTO();
                odto.setOrderID(rs.getString("orderID"));
                odto.setOrderDate(rs.getString("orderDate"));
                odto.setTotalPrice(rs.getFloat("totalPrice"));
                odto.setUserID(rs.getString("userID"));
                odto.setDiscount(getDiscountToHis(rs.getString("discountID")));
                odto.setStatus(rs.getBoolean("status"));
                odto.setHash(getOrderDetail(odto.getOrderID()));
                if (list == null) {
                    list = new ArrayList<>();
                }
                boolean check = true;
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getOrderID().equals(odto.getOrderID())) {
                            check = false;
                        }
                    }
                }
                if (check) {
                    list.add(odto);
                }
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
        return list;
    }

    public HashMap<String, OrderDetailDTO> getOrderDetail(String orderID) throws SQLException, NamingException, ParseException {
        HashMap<String, OrderDetailDTO> hash = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT ordDetailID,carID,rentalDate,returnDate,quantity,price,status\n"
                    + "FROM OrderDetail\n"
                    + "WHERE orderID =?";
            pst = conn.prepareStatement(url);
            pst.setString(1, orderID);
            rs = pst.executeQuery();
            CarDAO cdao = new CarDAO();
            while (rs.next()) {
                String key = rs.getString("ordDetailID");
                OrderDetailDTO detaildto = new OrderDetailDTO();
                detaildto.setOrderDetailID(key);
                detaildto.setOrderID(orderID);
                detaildto.setPrice(rs.getFloat("price"));
                detaildto.setCar(cdao.getCar(rs.getString("carID")));
                detaildto.setQuantity(rs.getInt("quantity"));
                detaildto.setRentalDate(rs.getString("rentalDate"));
                detaildto.setReturnDate(rs.getString("returnDate"));
                detaildto.setStatus(rs.getBoolean("status"));
                detaildto.setCheckRating();
                detaildto.setRating(getRating(key));
                if (hash == null) {
                    hash = new HashMap<>();
                }
                hash.put(key, detaildto);
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
        return hash;
    }

    public ArrayList<OrderDTO> getOrderByDate(String userID, String date) throws SQLException, NamingException, ParseException {
        ArrayList<OrderDTO> list = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "Select orderID,orderDate,totalPrice,discountID,status,userID\n"
                    + "From tblOrder\n"
                    + "Where orderDate =? AND userID=?\n"
                    + "order by orderDate desc";
            pst = conn.prepareStatement(url);
            pst.setString(2, userID);
            pst.setString(1, date);
            rs = pst.executeQuery();
            while (rs.next()) {
                OrderDTO odto = new OrderDTO();
                odto.setOrderID(rs.getString("orderID"));
                odto.setOrderDate(rs.getString("orderDate"));
                odto.setTotalPrice(rs.getFloat("totalPrice"));
                odto.setUserID(rs.getString("userID"));
                odto.setDiscount(getDiscountToHis(rs.getString("discountID")));
                odto.setStatus(rs.getBoolean("status"));
                odto.setHash(getOrderDetail(odto.getOrderID()));
                if (list == null) {
                    list = new ArrayList<>();
                }
                boolean check = true;
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getOrderID().equals(odto.getOrderID())) {
                            check = false;
                        }
                    }
                }
                if (check) {
                    list.add(odto);
                }
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
        return list;
    }

    public void removeOrder(String orderID) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBUtils.getConnection();
            String url = "UPDATE tblOrder\n"
                    + "SET status=0\n"
                    + "WHERE orderID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, orderID);
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

    public RatingDTO getRating(String detailID) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        RatingDTO rating = null;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT b.ratingID,b.ratingValue,b.status\n"
                    + "FROM OrderDetail a JOIN Rating b ON a.ratingID=b.ratingID\n"
                    + "WHERE ordDetailID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, detailID);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rating == null) {
                    rating = new RatingDTO(rs.getString("ratingID"), rs.getInt("ratingValue"), rs.getBoolean("status"));
                }
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
        return rating;
    }

    public void createRating(String rating, String userID) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBUtils.getConnection();
            String url = "INSERT INTO Rating(ratingValue,status,[userID])\n"
                    + "VALUES(?,1,?)";
            pst = conn.prepareStatement(url);
            pst.setString(1, rating);
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

    public int getRatingID(String userID) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int ratingid = -1;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT max(ratingID) as count\n"
                    + "FROM Rating\n"
                    + "Where userID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, userID);
            rs = pst.executeQuery();
            if (rs.next()) {
                ratingid = rs.getInt("count");
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
        return ratingid;
    }

    public void updateRating(String detailID, String ratingid) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBUtils.getConnection();
            String url = "Update OrderDetail\n"
                    + "SET [ratingID]=?\n"
                    + "Where ordDetailID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, ratingid);
            pst.setString(2, detailID);
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

    public void deleteOrder(String orderID) throws SQLException, NamingException {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = DBUtils.getConnection();
            String url = "DELETE FROM OrderDetail\n"
                    + "WHERE orderID=?\n"
                    + "DELETE FROM tblOrder\n"
                    + "WHERE orderID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, orderID);
            pst.setString(2, orderID);
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
