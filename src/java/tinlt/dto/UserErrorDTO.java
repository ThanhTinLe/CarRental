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
public class UserErrorDTO {
    private String userE;
    private String nameE;
    private String passwordE;
    private String confrimE;
    private String phoneE;
    private String addressE;

    public UserErrorDTO() {
    }

    public UserErrorDTO(String userE, String nameE, String passwordE, String confrimE, String phoneE, String addressE) {
        this.userE = userE;
        this.nameE = nameE;
        this.passwordE = passwordE;
        this.confrimE = confrimE;
        this.phoneE = phoneE;
        this.addressE = addressE;
    }


    public String getNameE() {
        return nameE;
    }

    public void setNameE(String nameE) {
        this.nameE = nameE;
    }

    public String getUserE() {
        return userE;
    }

    public void setUserE(String userE) {
        this.userE = userE;
    }

    public String getPasswordE() {
        return passwordE;
    }

    public void setPasswordE(String passwordE) {
        this.passwordE = passwordE;
    }

    public String getConfrimE() {
        return confrimE;
    }

    public void setConfrimE(String confrimE) {
        this.confrimE = confrimE;
    }

    public String getPhoneE() {
        return phoneE;
    }

    public void setPhoneE(String PhoneE) {
        this.phoneE = PhoneE;
    }

    public String getAddressE() {
        return addressE;
    }

    public void setAddressE(String addressE) {
        this.addressE = addressE;
    }
    
}
