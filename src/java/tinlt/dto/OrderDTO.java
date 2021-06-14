/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author tuanv
 */
public class OrderDTO implements Serializable{

    private String orderID;
    private String orderDate;
    private float totalPrice;
    private String userID;
    private DiscountDTO discount;
    HashMap<String, OrderDetailDTO> hash;
    private boolean status;

    public OrderDTO(String orderID, String orderDate, float totalPrice, String userID, DiscountDTO discount, HashMap<String, OrderDetailDTO> hash) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.userID = userID;
        this.discount = discount;
        this.hash = hash;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public OrderDTO(String orderID, String orderDate, float totalPrice, String userID, DiscountDTO discount, HashMap<String, OrderDetailDTO> hash, boolean status) {
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.userID = userID;
        this.discount = discount;
        this.hash = hash;
        this.status = status;
    }

    public OrderDTO() {
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public DiscountDTO getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountDTO discount) {
        this.discount = discount;
    }

    public HashMap<String, OrderDetailDTO> getHash() {
        return hash;
    }

    public void setHash(HashMap<String, OrderDetailDTO> hash) {
        this.hash = hash;
    }

    public void add(OrderDetailDTO orderDetail) {
        if (hash == null) {
            this.hash = new HashMap<>();
        }
        LocalDateTime now = LocalDateTime.now();
        Set<String> key = this.hash.keySet();
        boolean check= true;
        for (String detaiid : key) {
            if (this.hash.get(detaiid).getCar().getCarID().equals(orderDetail.getCar().getCarID()) && this.hash.get(detaiid).getRentalDate().equals(orderDetail.getRentalDate()) && this.hash.get(detaiid).getReturnDate().equals(orderDetail.getReturnDate())) {
                int quantity = this.hash.get(detaiid).getQuantity();
                orderDetail.setQuantity(quantity + 1);
                float price = this.hash.get(detaiid).getPrice() + orderDetail.getPrice();
                orderDetail.setPrice(price);
                hash.replace(detaiid, orderDetail);
                check=false;
            }
            
        }
        if(check){
            hash.put(orderDetail.getCar().getCarID() + "-" + now.toString(), orderDetail);
        }
        
    }

    public void remove(OrderDetailDTO orderDetail) {
        if (hash == null) {
            return;
        }
        if (this.hash.containsKey(orderDetail.getOrderDetailID())) {
            this.hash.remove(orderDetail.getOrderDetailID());
        }
    }

    public void update(OrderDetailDTO orderDetail) {
        LocalDateTime now = LocalDateTime.now();
        if (hash == null) {
            return;
        }
        if (hash.containsKey(orderDetail.getOrderDetailID())) {
            hash.replace(orderDetail.getOrderDetailID(), orderDetail);
        }
    }
}
