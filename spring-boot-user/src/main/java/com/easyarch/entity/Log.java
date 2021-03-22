package com.easyarch.entity;



import java.io.Serializable;

public class Log implements Serializable {

    private static final long serialVersionUID = -3246199064036684772L;

    private int id;

    private String logOp;

    private String logType;

    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
