package com.easyarch.using.entity;


import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 3886376643037180111L;

    private String user_id;
    private String name;
    private String username;
    private String password;
    private String user_tel;
    private String user_email;
    public User(){}

    public User(String name,String username,String password){
        this.name=name;
        this.username=username;
        this.password=password;
    }

    public String getUser_id() {
        return user_id;
    }

    public User(String user_id, String name, String username, String password) {
        this.user_id = user_id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
