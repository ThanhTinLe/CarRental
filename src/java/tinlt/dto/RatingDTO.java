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
public class RatingDTO {
    private String ratingID;
    private int ratingValue;
    private boolean status;

    public RatingDTO(String ratingID, int ratingValue, boolean status) {
        this.ratingID = ratingID;
        this.ratingValue = ratingValue;
        this.status = status;
    }

    public RatingDTO() {
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
