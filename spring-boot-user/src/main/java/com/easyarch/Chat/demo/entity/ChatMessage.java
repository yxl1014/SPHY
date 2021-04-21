package com.easyarch.Chat.demo.entity;

import java.util.Date;

public class ChatMessage {
    private String forUsername;
    private String toUsername;
    private Date chatDate;
    private Message message;

    public ChatMessage() {

    }

    public ChatMessage(String forUsername, String toUsername, Date chatDate, Message message) {
        this.forUsername = forUsername;
        this.toUsername = toUsername;
        this.chatDate = chatDate;
        this.message = message;
    }

    public String getForUsername() {
        return forUsername;
    }

    public void setForUsername(String forUsername) {
        this.forUsername = forUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public Date getChatDate() {
        return chatDate;
    }

    public void setChatDate(Date chatDate) {
        this.chatDate = chatDate;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
