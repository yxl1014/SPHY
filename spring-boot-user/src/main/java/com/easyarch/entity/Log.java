package com.easyarch.entity;


import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class Log implements Serializable {

    private static final long serialVersionUID = -3246199064036684772L;

    private int id;

    private String logOp;

    private String logType;

    private int userId;

    private long createTime;

    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogOp() {
        return logOp;
    }

    public void setLogOp(String logOp) {
        this.logOp = logOp;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
