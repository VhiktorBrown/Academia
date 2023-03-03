package com.theelitedevelopers.academia.modules.main.data.models;

public class Chat {
    String senderId;
    String message;
    String image;
    String name;
    String date;
    String lastMessage;

    public Chat(String name, String lastMessage) {
        this.name = name;
        this.lastMessage = lastMessage;
    }

    public Chat(String senderId, String message, String date) {
        this.senderId = senderId;
        this.message = message;
        this.date = date;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
