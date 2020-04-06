package com.example.notebook.Models;


public class Users {
    private String userName , email , password , confirmPassword , photo;
    public Users(String userName, String email, String photo , String password  , String confirm) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirm ;
        this.photo = photo ;
    }

    public Users() {
    }

    public Users(String userName, String email , String photo) {
        this.userName = userName;
        this.email = email;
        this.photo = photo ;
    }
    public Users(String userName, String email ) {
        this.userName = userName;
        this.email = email;
    }
    //setter and getter

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
