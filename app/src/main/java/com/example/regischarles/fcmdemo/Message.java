package com.example.regischarles.fcmdemo;

public class Message {
    private int userId;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Message(int userId, String message,String name ){
        this.userId = userId;
        this.message = message;
        this.name=name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
