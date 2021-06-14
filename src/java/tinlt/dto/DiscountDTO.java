/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author tuanv
 */
public class DiscountDTO {
    private String disID;
    private String disName;
    private float disValue;
    private boolean status;

    public DiscountDTO() {
    }

    public DiscountDTO(String disID, String disName, float disValue, boolean status) {
        this.disID = disID;
        this.disName = disName;
        this.disValue = disValue;
        this.status = status;
    }

    public String getDisID() {
        return disID;
    }

    public void setDisID(String disID) {
        this.disID = disID;
    }

    public String getDisName() {
        return disName;
    }

    public void setDisName(String disName) {
        this.disName = disName;
    }

    public float getDisValue() {
        return disValue;
    }

    public void setDisValue(float disValue) {
        this.disValue = disValue;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
