package com.example.cyberprojectclient.utils;

public class User {
    private String firstName, lastName, username;
    private int userId;

    public User(String firstName, String lastName, String username, int userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.userId = userId;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public int getUserId() { return userId; }
}
