package com.example.messenger.common;

public class Message {

    private String text;
    private String senderId;
    private String receiverId;

    Message() {}

    public Message(String text, String senderId, String receiverId) {
        this.text = text;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getText() {
        return this.text;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public String getReceiverId() {
        return this.receiverId;
    }
}
