package com.example.cyberprojectclient.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataVerification {
    private static final String passwordPatternString = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    private static final Pattern passwordPattern = Pattern.compile(passwordPatternString);
    private static final String usernamePatternString = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
    private static final Pattern usernamePattern = Pattern.compile(usernamePatternString);
    private static final String namePatternString = "(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$";
    private static final Pattern namePattern = Pattern.compile(namePatternString);
    private static final String bioPatternString = "(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,249}$";
    private static final Pattern bioPattern = Pattern.compile(bioPatternString);

    /**
     * This func verifies the password's strength and validity based on the following characteristics:
     * - contains at least 8 digits
     * - At least 1 upper case and 1 lower case letter
     * - At least 1 digit and 1 special character
     * @param password The password to be verified
     * @return Whether the password is strong enough
     */
    public static boolean verifyPassword(String password) {
        return passwordPattern.matcher(password).find();
    }

    /**
     * This func verifies the password's strength and validity based on the following characteristics:
     * - contains between 5 and 20 characters
     * - consists on alphanumeric characters and dot, underscore and hyphen
     * - the special characters are neither at the start and end nor consecutive
     * @param username The username to be verified
     * @return Whether the ysername is strong enough
     */
    public static boolean verifyUsername(String username) {
        return usernamePattern.matcher(username).find();
    }

    /**
     * This func verifies the password's strength and validity based on the following characteristics:
     * - contains between 1 and 25 characters
     * - consists on alphanumeric characters and ' - , .
     * @param name The name to be verified
     * @return Whether the name is strong enough
     */
    public static boolean verifyName(String name) {
        return namePattern.matcher(name).find();
    }

    /**
     * This func verifies the password's strength and validity based on the following characteristics:
     * - contains between 1 and 250 characters
     * - consists on alphanumeric characters and ' - , .
     * @param bio The bio to be verified
     * @return Whether the bio is strong enough
     */
    public static boolean verifyBio(String bio) {
        return bioPattern.matcher(bio).find();
    }
}
