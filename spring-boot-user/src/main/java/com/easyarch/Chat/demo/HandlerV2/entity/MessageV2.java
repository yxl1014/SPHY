package com.easyarch.Chat.demo.HandlerV2.entity;

public class MessageV2 {
    private String mid;
    private String uid;
    private String toUsername;
    private String fromUsername;
    private String content;
    private String messageType;
    private String dataType;
    private boolean reading;
    private boolean canSend;
    private long timestamp;


    public MessageV2(String mid, String uid, String toUsername, String fromUsername, String content, String messageType, String dataType, boolean reading, boolean canSend, long timestamp) {
        this.mid = mid;
        this.uid = uid;
        this.toUsername = toUsername;
        this.fromUsername = fromUsername;
        this.content = content;
        this.messageType = messageType;
        this.dataType = dataType;
        this.reading = reading;
        this.canSend = canSend;
        this.timestamp = timestamp;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isReading() {
        return reading;
    }

    public void setReading(boolean reading) {
        this.reading = reading;
    }

    public boolean isCanSend() {
        return canSend;
    }

    public void setCanSend(boolean canSend) {
        this.canSend = canSend;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
