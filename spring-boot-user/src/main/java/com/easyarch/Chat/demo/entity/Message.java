package com.easyarch.Chat.demo.entity;

public class Message {

    private String id;
    private String toUsername;
    private String forUsername;
    private String content;
    private String messageType;
    private long timestamp;


    public Message(String id, String content, long timestamp, String messageType) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.messageType = messageType;
    }


    public Message(String id, String toUsername, String forUsername, String content, String messageType, long timestamp) {
        this.id = id;
        this.toUsername = toUsername;
        this.forUsername = forUsername;
        this.content = content;
        this.messageType = messageType;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getForUsername() {
        return forUsername;
    }

    public void setForUsername(String forUsername) {
        this.forUsername = forUsername;
    }
}
