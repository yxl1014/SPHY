package com.easyarch.entity;

import java.io.Serializable;

public class UserFriend implements Serializable {

    private static final long serialVersionUID = -8574752374201003948L;

    private String userid;
    private String other;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
