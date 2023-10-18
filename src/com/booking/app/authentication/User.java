package com.booking.app.authentication;

import org.json.simple.JSONObject;

public class User {
    private boolean isLogged;
    private boolean admin;
    private String username;
    private String password;



    public User(String username, String password, boolean isLogged, boolean admin) {
        this.isLogged = isLogged;
        this.admin = admin;
        this.username = username;
        this.password = password;
    }

    public Object toJSON() {
        JSONObject userDetails = new JSONObject();
        userDetails.put("username", this.getUsername());
        userDetails.put("password", this.getPassword());
        userDetails.put("admin", this.isAdmin());
        return userDetails;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }

}






