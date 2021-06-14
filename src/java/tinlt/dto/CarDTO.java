/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author tuanv
 */
public class CarDTO implements Serializable{
    private String carID;
    private String carName;
    private String color;
    private int year;
    private int quantity;
    private float price;
    private String description;
    private String category;
    private String image;
    private boolean status;

    public CarDTO() {
    }

    public CarDTO(String carID, String carName, String color, int year, int quantity, float price, String description, String category, String image, boolean status) {
        this.carID = carID;
        this.carName = carName;
        this.color = color;
        this.year = year;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
        this.status = status;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
