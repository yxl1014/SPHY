package com.easyarch.error.entity;

public class Erormessage {
    private int eid;
    private String etime;
    private String userid;
    private String context;

    public Erormessage(String etime, String userid, String context) {
        this.etime = etime;
        this.userid = userid;
        this.context = context;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
