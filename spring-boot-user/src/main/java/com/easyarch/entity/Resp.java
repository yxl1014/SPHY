package com.easyarch.entity;

import java.io.Serializable;

public class Resp<T> implements Serializable {

    private static final long serialVersionUID = 4961735540198562221L;

    private String code;
    private String message;
    private T data;
    private String userid;
    private String token;

    public Resp(String code, String message, T data, String userid, String token) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.userid = userid;
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
