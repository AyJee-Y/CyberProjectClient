package com.example.cyberprojectclient.network;

public class CurrentUserData {
    private static int userId;
    private static String username;
    private static String firstName;
    private static String lastName;
    private static String bio;

    public static int getUserId() { return userId; }
    public static String getUsername() { return username; }
    public static String getFirstName() { return firstName; }
    public static String getLastName() { return lastName; }
    public static String getBio() { return bio; }

    public static String[] getFullData() { return new String[] {username, firstName, lastName, bio}; }

    public static void setUser(int newUserId) {
        userId = newUserId;
    }
    public static void updateFullData() {
        String[] newData = NetworkAdapter.getUserData(userId);
        username = newData[0];
        firstName = newData[1];
        lastName = newData[2];
        bio = newData[3];
    }
}
