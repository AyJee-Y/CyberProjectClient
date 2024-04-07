package com.example.cyberprojectclient;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class networkAdapter {
    private static int currentUserId;
    private static Map<String, String> usernameToPassword = new HashMap<>();

    public static int getCurrentUserId() { return currentUserId; }
    public static void setCurrentUserId(int userId) { currentUserId = userId; }

    public static void initializeUsers() {
        usernameToPassword.put("Admin", "12345");
    }

    public static boolean attemptSignIn(String username, String password) {
        //TODO:
        //Insert here a check with the server where you send the hash of the password and
        //the username in order to see whether its valid sign in data, afterwards you should
        //set the user id of the user (if sign in is valid)
        boolean result = (usernameToPassword.containsKey(username)) && (usernameToPassword.get(username).equals(password));
        if (result)
            setCurrentUserId(10); //This should be a random number, but should be received from the server
        return (result);
    }

    public static boolean attemptRegister(String username, String password) {
        //TODO:
        //Insert here a check with the server where you send the hash of the password and
        //the username and the rest of the data, in order to see whether this username is
        //available, if it is the server will save the data and than you can sign in with it

        if (usernameToPassword.containsKey(username))
            return false;
        else {
            usernameToPassword.put(username, password);
            return true;
        }
    }
}