/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.naming.NamingException;

/**
 *
 * @author tuanv
 */
public class OrderDetailDTO {

    private String orderDetailID;
    private CarDTO car;
    private String rentalDate;
    private String returnDate;
    private String orderID;
    private int quantity;
    private float price;
    private boolean status;
    private RatingDTO rating;
    private boolean checkRating;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(String orderDetailID, CarDTO car, String rentalDate, String returnDate, String orderID, int quantity, float price, boolean status) {
        this.orderDetailID = orderDetailID;
        this.car = car;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.orderID = orderID;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    public OrderDetailDTO(String orderDetailID, CarDTO car, String rentalDate, String returnDate, String orderID, int quantity, float price, boolean status, RatingDTO rating) {
        this.orderDetailID = orderDetailID;
        this.car = car;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.orderID = orderID;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.rating = rating;
    }

    public OrderDetailDTO(String orderDetailID, CarDTO car, String rentalDate, String returnDate, String orderID, int quantity, float price, boolean status, RatingDTO rating, boolean checkRating) {
        this.orderDetailID = orderDetailID;
        this.car = car;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.orderID = orderID;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.rating = rating;
        this.checkRating = checkRating;
    }

    public boolean isCheckRating() {
        return checkRating;
    }

    public void setCheckRating() throws NamingException, SQLException, ParseException {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Date retDate = date.parse(returnDate);
        Date curenDate = new Date();
        if(curenDate.after(retDate)){
            this.checkRating =true;
        }else
        this.checkRating =false;
    }

    public RatingDTO getRating() {
        return rating;
    }

    public void setRating(RatingDTO rating) {
        this.rating = rating;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(String orderDetailID) {
        this.orderDetailID = orderDetailID;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
