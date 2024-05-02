package com.example.cyberprojectclient.utils;

public class Message {
    private String message;
    private User sender;

    public Message(String message, User sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() { return message; }
    public User getUser() { return sender; }
}
