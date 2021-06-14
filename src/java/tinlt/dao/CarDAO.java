/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tinlt.dao;

import dto.CarDTO;
import dto.CategoryDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.NamingException;
import tinlt.util.DBUtils;

/**
 *
 * @author tuanv
 */
public class CarDAO {

    public ArrayList<CategoryDTO> getCategory() throws SQLException, NamingException {
        ArrayList<CategoryDTO> list = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT CategoryID,CategoryName,status FROM Category";
            pst = conn.prepareStatement(url);
            rs = pst.executeQuery();
            while (rs.next()) {
                CategoryDTO catedto = new CategoryDTO(rs.getString("CategoryID"), rs.getString("CategoryName"), rs.getBoolean("status"));
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (catedto.isStatus()) {
                    list.add(catedto);
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

    public CategoryDTO getCategoryByID(String cateID) throws SQLException, NamingException {
        CategoryDTO cate = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT CategoryID,CategoryName,status FROM Category WHERE CategoryID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, cateID);
            rs = pst.executeQuery();
            while (rs.next()) {
                cate = new CategoryDTO(rs.getString("CategoryID"), rs.getString("CategoryName"), rs.getBoolean("status"));
                if (!cate.isStatus()) {
                    return null;
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
        return cate;
    }

    public ArrayList<CarDTO> getCarName(String name, String amount, String rentalDate, String returnDate, int index) throws NamingException, SQLException {
        ArrayList<CarDTO> list = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "with x as (select ROW_NUMBER() over (order by a.CategoryID asc,a.year desc) as row,a.carID,a.CategoryID,a.carName,a.color,a.quantity-COALESCE(b.quantity, 0) as quantity,year,a.price,a.image,a.description,a.status\n"
                    + "from [dbo].[car] a LEFT OUTER join (select b.carID,SUM(b.quantity) as quantity\n"
                    + "from [dbo].[tblOrder] a join [dbo].[OrderDetail] b on a.orderID=b.orderID\n"
                    + "where a.status='1' and b.status='1' \n"
                    + "and( ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or b.[rentalDate]  between ? and ?\n"
                    + "or b.[returnDate] between ? and ?)\n"
                    + "group by b.carID)  b on a.carID=b.carID\n"
                    + "where a.status='1' and (a.quantity-COALESCE(b.quantity, 0))>=? and a.carName like ?)\n"
                    + "SELECT row,carID,CategoryID,carName,color,quantity,year,price,image,description,status FROM x where row between ? and ?";
            pst = conn.prepareStatement(url);
            pst.setString(1, rentalDate);
            pst.setString(2, returnDate);
            pst.setString(3, rentalDate);
            pst.setString(4, returnDate);
            pst.setString(5, rentalDate);
            pst.setString(6, returnDate);
            pst.setString(7, amount);
            pst.setString(8, "%" + name + "%");
            pst.setString(9, index * 5 - 4 + "");
            pst.setString(10, index * 5 + "");
            rs = pst.executeQuery();
            while (rs.next()) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                CarDTO cdto = new CarDTO();
                cdto.setCarID(rs.getString("carID"));
                cdto.setCarName(rs.getString("carName"));
                cdto.setCategory(rs.getString("CategoryID"));
                cdto.setColor(rs.getString("color"));
                cdto.setDescription(rs.getString("description"));
                cdto.setImage(rs.getString("image"));
                cdto.setPrice(rs.getFloat("price"));
                cdto.setQuantity(rs.getInt("quantity"));
                cdto.setYear(rs.getInt("year"));
                cdto.setStatus(rs.getBoolean("status"));
                if (cdto.isStatus()) {
                    cdto.setCategory(getCategoryByID(cdto.getCategory()).getCateName());
                    list.add(cdto);
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

    public int countCarName(String name, String amount, String rentalDate, String returnDate) throws NamingException, SQLException {
        int count = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "select count(a.carID) as count\n"
                    + "from [dbo].[car] a LEFT OUTER join (select b.carID,SUM(b.quantity) as quantity\n"
                    + "from [dbo].[tblOrder] a join [dbo].[OrderDetail] b on a.orderID=b.orderID\n"
                    + "where a.status='1' and b.status='1' \n"
                    + "and( ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or b.[rentalDate]  between ? and ?\n"
                    + "or b.[returnDate] between ? and ?)\n"
                    + "group by b.carID)  b on a.carID=b.carID\n"
                    + "where a.status='1' and (a.quantity-COALESCE(b.quantity, 0))>=? AND a.carName like ?";
            pst = conn.prepareStatement(url);
            pst.setString(1, rentalDate);
            pst.setString(2, returnDate);
            pst.setString(3, rentalDate);
            pst.setString(4, returnDate);
            pst.setString(5, rentalDate);
            pst.setString(6, returnDate);
            pst.setString(7, amount);
            pst.setString(8, "%" + name + "%");
            rs = pst.executeQuery();
            while (rs.next()) {
                count = (int) Math.ceil((double) rs.getInt("count") / 5);
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
        return count;
    }

    public ArrayList<CarDTO> getCarCategory(String categoryID, String amount, String rentalDate, String returnDate, int index) throws NamingException, SQLException {
        ArrayList<CarDTO> list = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "with x as (select ROW_NUMBER() over (order by a.year desc) as row,a.carID,a.CategoryID,a.carName,a.color,a.quantity-COALESCE(b.quantity, 0) as quantity,year,a.price,a.image,a.description,a.status\n"
                    + "from [dbo].[car] a LEFT OUTER join (select b.carID,SUM(b.quantity) as quantity\n"
                    + "from [dbo].[tblOrder] a join [dbo].[OrderDetail] b on a.orderID=b.orderID\n"
                    + "where a.status='1' and b.status='1' \n"
                    + "and( ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or b.[rentalDate]  between ? and ?\n"
                    + "or b.[returnDate] between ? and ?)\n"
                    + "group by b.carID)  b on a.carID=b.carID\n"
                    + "where a.status='1' and (a.quantity-COALESCE(b.quantity, 0))>=? and a.CategoryID=?)\n"
                    + "SELECT row,carID,CategoryID,carName,color,quantity,year,price,image,description,status FROM x where row between ? and ?";
            pst = conn.prepareStatement(url);
            pst.setString(1, rentalDate);
            pst.setString(2, returnDate);
            pst.setString(3, rentalDate);
            pst.setString(4, returnDate);
            pst.setString(5, rentalDate);
            pst.setString(6, returnDate);
            pst.setString(7, amount);
            pst.setString(8, categoryID);
            pst.setString(9, index * 5 - 4 + "");
            pst.setString(10, index * 5 + "");
            rs = pst.executeQuery();
            while (rs.next()) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                CarDTO cdto = new CarDTO();
                cdto.setCarID(rs.getString("carID"));
                cdto.setCarName(rs.getString("carName"));
                cdto.setCategory(rs.getString("CategoryID"));
                cdto.setColor(rs.getString("color"));
                cdto.setDescription(rs.getString("description"));
                cdto.setImage(rs.getString("image"));
                cdto.setPrice(rs.getFloat("price"));
                cdto.setQuantity(rs.getInt("quantity"));
                cdto.setYear(rs.getInt("year"));
                cdto.setStatus(rs.getBoolean("status"));
                if (cdto.isStatus()) {
                    cdto.setCategory(getCategoryByID(cdto.getCategory()).getCateName());
                    list.add(cdto);
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

    public int coutntCarCategory(String categoryID, String amount, String rentalDate, String returnDate) throws NamingException, SQLException {
        int count = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "select count(a.carID) as count\n"
                    + "from [dbo].[car] a LEFT OUTER join (select b.carID,SUM(b.quantity) as quantity\n"
                    + "from [dbo].[tblOrder] a join [dbo].[OrderDetail] b on a.orderID=b.orderID\n"
                    + "where a.status='1' and b.status='1' \n"
                    + "and( ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or b.[rentalDate]  between ? and ?\n"
                    + "or b.[returnDate] between ? and ?)\n"
                    + "group by b.carID)  b on a.carID=b.carID\n"
                    + "where a.status='1' and (a.quantity-COALESCE(b.quantity, 0))>=? AND a.CategoryID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, rentalDate);
            pst.setString(2, returnDate);
            pst.setString(3, rentalDate);
            pst.setString(4, returnDate);
            pst.setString(5, rentalDate);
            pst.setString(6, returnDate);
            pst.setString(7, amount);
            pst.setString(8, categoryID);
            rs = pst.executeQuery();
            while (rs.next()) {
                count = (int) Math.ceil((double) rs.getInt("count") / 5);
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
        return count;
    }

    public ArrayList<CarDTO> getAllCarCategory(String amount, String rentalDate, String returnDate, int index) throws NamingException, SQLException {
        ArrayList<CarDTO> list = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "with x as (select ROW_NUMBER() over (order by a.year desc) as row,a.carID,a.CategoryID,a.carName,a.color,a.quantity-COALESCE(b.quantity, 0) as quantity,year,a.price,a.image,a.description,a.status\n"
                    + "from [dbo].[car] a LEFT OUTER join (select b.carID,SUM(b.quantity) as quantity\n"
                    + "from [dbo].[tblOrder] a join [dbo].[OrderDetail] b on a.orderID=b.orderID\n"
                    + "where a.status='1' and b.status='1' \n"
                    + "and( ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or b.[rentalDate]  between ? and ?\n"
                    + "or b.[returnDate] between ? and ?)\n"
                    + "group by b.carID)  b on a.carID=b.carID\n"
                    + "where a.status='1' and (a.quantity-COALESCE(b.quantity, 0))>= ?)\n"
                    + "SELECT row,carID,CategoryID,carName,color,quantity,year,price,image,description,status FROM x where row between ? and ?";
            pst = conn.prepareStatement(url);
            pst.setString(1, rentalDate);
            pst.setString(2, returnDate);
            pst.setString(3, rentalDate);
            pst.setString(4, returnDate);
            pst.setString(5, rentalDate);
            pst.setString(6, returnDate);
            pst.setString(7, amount);
            pst.setString(8, index * 5 - 4 + "");
            pst.setString(9, index * 5 + "");
            rs = pst.executeQuery();
            while (rs.next()) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                CarDTO cdto = new CarDTO();
                cdto.setCarID(rs.getString("carID"));
                cdto.setCarName(rs.getString("carName"));
                cdto.setCategory(rs.getString("CategoryID"));
                cdto.setColor(rs.getString("color"));
                cdto.setDescription(rs.getString("description"));
                cdto.setImage(rs.getString("image"));
                cdto.setPrice(rs.getFloat("price"));
                cdto.setQuantity(rs.getInt("quantity"));
                cdto.setYear(rs.getInt("year"));
                cdto.setStatus(rs.getBoolean("status"));
                if (cdto.isStatus()) {
                    cdto.setCategory(getCategoryByID(cdto.getCategory()).getCateName());
                    list.add(cdto);
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

    public int coutntAllCarCategory(String amount, String rentalDate, String returnDate) throws NamingException, SQLException {
        int count = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "select count(a.carID) as count\n"
                    + "from [dbo].[car] a LEFT OUTER join (select b.carID,SUM(b.quantity) as quantity\n"
                    + "from [dbo].[tblOrder] a join [dbo].[OrderDetail] b on a.orderID=b.orderID\n"
                    + "where a.status='1' and b.status='1' \n"
                    + "and( ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or b.[rentalDate]  between ? and ?\n"
                    + "or b.[returnDate] between ? and ?)\n"
                    + "group by b.carID)  b on a.carID=b.carID\n"
                    + "where a.status='1' and (a.quantity-COALESCE(b.quantity, 0))>=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, rentalDate);
            pst.setString(2, returnDate);
            pst.setString(3, rentalDate);
            pst.setString(4, returnDate);
            pst.setString(5, rentalDate);
            pst.setString(6, returnDate);
            pst.setString(7, amount);
            rs = pst.executeQuery();
            while (rs.next()) {
                count = (int) Math.ceil((double) rs.getInt("count") / 5);
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
        return count;
    }

    public CarDTO getCar(String carID) throws NamingException, SQLException {
        CarDTO cardto = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "SELECT carID,carName,color,description,image,price,quantity,a.status,CategoryName,year\n"
                    + "FROM car a JOIN Category b ON a.CategoryID=b.CategoryID\n"
                    + "WHERE carID=?";
            pst = conn.prepareStatement(url);
            pst.setString(1, carID);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (cardto == null) {
                    cardto = new CarDTO();
                }
                cardto.setCarID(rs.getString("carID"));
                cardto.setCarName(rs.getString("carName"));
                cardto.setCategory(rs.getString("CategoryName"));
                cardto.setColor(rs.getString("color"));
                cardto.setDescription(rs.getString("description"));
                cardto.setImage(rs.getString("image"));
                cardto.setPrice(rs.getFloat("price"));
                cardto.setQuantity(rs.getInt("quantity"));
                cardto.setYear(rs.getInt("year"));
                cardto.setStatus(rs.getBoolean("status"));
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
        return cardto;
    }

    public ArrayList<CarDTO> getCarNameToUpdate(String amount, String rentalDate, String returnDate) throws NamingException, SQLException {
        ArrayList<CarDTO> list = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String url = "with x as (select ROW_NUMBER() over (order by a.year desc) as row,a.carID,a.CategoryID,a.carName,a.color,a.quantity-COALESCE(b.quantity, 0) as quantity,year,a.price,a.image,a.description,a.status\n"
                    + "from [dbo].[car] a LEFT OUTER join (select b.carID,SUM(b.quantity) as quantity\n"
                    + "from [dbo].[tblOrder] a join [dbo].[OrderDetail] b on a.orderID=b.orderID\n"
                    + "where a.status='1' and b.status='1' \n"
                    + "and( ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or ? between b.[rentalDate] and b.[returnDate]\n"
                    + "or b.[rentalDate]  between ? and ?\n"
                    + "or b.[returnDate] between ? and ?)\n"
                    + "group by b.carID)  b on a.carID=b.carID\n"
                    + "where a.status='1' and (a.quantity-COALESCE(b.quantity, 0))>=? and a.carName like ?)\n"
                    + "SELECT row,carID,CategoryID,carName,color,quantity,year,price,image,description,status FROM x";
            pst = conn.prepareStatement(url);
            pst.setString(1, rentalDate);
            pst.setString(2, returnDate);
            pst.setString(3, rentalDate);
            pst.setString(4, returnDate);
            pst.setString(5, rentalDate);
            pst.setString(6, returnDate);
            pst.setString(7, amount);
            pst.setString(8, "%%");
            rs = pst.executeQuery();
            while (rs.next()) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                CarDTO cdto = new CarDTO();
                cdto.setCarID(rs.getString("carID"));
                cdto.setCarName(rs.getString("carName"));
                cdto.setCategory(rs.getString("CategoryID"));
                cdto.setColor(rs.getString("color"));
                cdto.setDescription(rs.getString("description"));
                cdto.setImage(rs.getString("image"));
                cdto.setPrice(rs.getFloat("price"));
                cdto.setQuantity(rs.getInt("quantity"));
                cdto.setYear(rs.getInt("year"));
                cdto.setStatus(rs.getBoolean("status"));
                if (cdto.isStatus()) {
                    cdto.setCategory(getCategoryByID(cdto.getCategory()).getCateName());
                    list.add(cdto);
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

    
}
