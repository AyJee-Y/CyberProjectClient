package com.example.cyberprojectclient.utils;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.cyberprojectclient.LoginActivity;
import com.example.cyberprojectclient.R;

import java.util.Random;

public class NetworkAdapter {
    //Database structure:
    //[0] -> username
    //[1] -> password
    //[2] -> first name
    //[3] -> last name
    //[4] -> bio
    private static String[][] currentDataTable = new String[20][5];
    private static int userCount = 0;

    /**
     * This function is used to initialize the admin users
     * this user is initializes for testing in order to save
     * time
     */
    public static void initializeUsers() {
        currentDataTable[userCount][0] = "Admin";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "FirstName";
        currentDataTable[userCount][3] = "LastName";
        currentDataTable[userCount][4] = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.";
        userCount++;

        currentDataTable[userCount][0] = "AyJee";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "Yony";
        currentDataTable[userCount][3] = "Halpern";
        currentDataTable[userCount][4] = "example for bio";
        userCount++;

        currentDataTable[userCount][0] = "Brenda";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "Nir";
        currentDataTable[userCount][3] = "Brandes";
        currentDataTable[userCount][4] = "example for bio";
        userCount++;

        currentDataTable[userCount][0] = "Moshe";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "Rani";
        currentDataTable[userCount][3] = "Ya'acobi";
        currentDataTable[userCount][4] = "example for bio";
        userCount++;

        currentDataTable[userCount][0] = "Benyumin";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "Beny";
        currentDataTable[userCount][3] = "Halpern";
        currentDataTable[userCount][4] = "example for bio";
        userCount++;

        currentDataTable[userCount][0] = "Totalinyo";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "Ron";
        currentDataTable[userCount][3] = "Katz";
        currentDataTable[userCount][4] = "example for bio";
        userCount++;

        currentDataTable[userCount][0] = "Stella";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "Tomer";
        currentDataTable[userCount][3] = "Borin";
        currentDataTable[userCount][4] = "example for bio";
        userCount++;

        currentDataTable[userCount][0] = "Sielor";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "Itay";
        currentDataTable[userCount][3] = "Zenking";
        currentDataTable[userCount][4] = "example for bio";
        userCount++;

        currentDataTable[userCount][0] = "Sielek";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "Lior";
        currentDataTable[userCount][3] = "Isakov";
        currentDataTable[userCount][4] = "example for bio";
        userCount++;

        currentDataTable[userCount][0] = "Yanuv";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "Yaniv";
        currentDataTable[userCount][3] = "Sha'asheta";
        currentDataTable[userCount][4] = "example for bio";
        userCount++;

        currentDataTable[userCount][0] = "Itzhak";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "Shlomi";
        currentDataTable[userCount][3] = "Rabinovich";
        currentDataTable[userCount][4] = "example for bio";
        userCount++;

        currentDataTable[userCount][0] = "a";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "b";
        currentDataTable[userCount][3] = "c";
        currentDataTable[userCount][4] = "example for bio";
        userCount++;
    }

    /**
     * This function will check whether the sign in
     * info is valid or not, if its valid it will
     * also save the users data in the preferences
     * from the assumption that the user is now signed in
     *
     * @param username username of the user's account
     * @param password password of the user's account
     * @param thisContext the context of the activity for logging in
     * @return whether the sign in was successful
     */
    public static boolean attemptSignIn(String username, String password, Context thisContext) {
        //TODO:
        //Insert here a check with the server where you send the hash of the password and
        //the username in order to see whether its valid sign in data, afterwards you should
        //set the user id of the user (if sign in is valid)
        for (int i = 0; i < userCount; i++) {
            if (currentDataTable[i][0].equals(username) && currentDataTable[i][1].equals(password))
            {
                setThisUserData(i + 1, thisContext);
                return true;
            }
        }
        return false;
    }

    /**
     * This function saves the data of the
     * user that signed in the preferences, it's
     * mainly to not have the user sign in each
     * time he used the app
     *
     * @param userid The id of the user's account
     * @param thisContext The context of the activity in which the user logged in
     */
    public static void setThisUserData(int userid, Context thisContext) {
        SharedPrefUtils.saveBoolean(thisContext,  thisContext.getString(R.string.prefLoggedStatus), true);
        SharedPrefUtils.saveInt(thisContext,  thisContext.getString(R.string.prefUserId), userid);
        SharedPrefUtils.saveString(thisContext,  thisContext.getString(R.string.prefUsername), currentDataTable[userid-1][0]);
        SharedPrefUtils.saveString(thisContext,  thisContext.getString(R.string.prefFirstName), currentDataTable[userid-1][2]);
        SharedPrefUtils.saveString(thisContext,  thisContext.getString(R.string.prefLastName), currentDataTable[userid-1][3]);
        SharedPrefUtils.saveString(thisContext,  thisContext.getString(R.string.prefBio), currentDataTable[userid-1][4]);
    }

    /**
     * This function will firstly check whether
     * the username is available, if so it assumes the
     * rest of the data is legal and so its returns true
     * and saves the data in its database
     *
     * @param username The username of the new account
     * @param password The password of the new account
     * @param firstName The first name of the new account
     * @param lastName The last name of the new account
     * @return Whether the new account is registered
     */
    public static boolean attemptRegister(String username, String password, String firstName, String lastName) {
        //TODO:
        //Insert here a check with the server where you send the hash of the password and
        //the username and the rest of the data, in order to see whether this username is
        //available, if it is the server will save the data and than you can sign in with it

        for (int i = 0; i < userCount; i++) {
            if (currentDataTable[i][0].equals(username))
            {
                //Here i need to also save the index of the user
                return false;
            }
        }
        currentDataTable[userCount][0] = username;
        currentDataTable[userCount][1] = password;
        currentDataTable[userCount][2] = firstName;
        currentDataTable[userCount][3] = lastName;
        userCount++;
        return true;
    }

    /**
     * This function will get the user data of the user with the
     * given id from the database.
     * They will be in the form of a string array with the indexes:
     * 0 - username
     * 1 - first name
     * 2 - last name
     * 3 - bio
     * 4 - userId
     *
     * @param userId The id of the user who's data is returned
     * @return current users data - for a profile
     */
    public static String[] getUserData(int userId) {
        if (userId <= userCount && userId > 0) {
            return (new String[] {currentDataTable[userId-1][0], currentDataTable[userId-1][2], currentDataTable[userId-1][3], currentDataTable[userId-1][4], String.valueOf(userId)});
        }
        else
            return null;
    }

    /**
     * Saves a users new profile data
     *
     * @param userId The user id of the user to sat data
     * @param firstname The new first name
     * @param lastname The new last name
     * @param bio The new biogrophy
     */
    public static void changeUserData(int userId, String firstname, String lastname, String bio) {
        currentDataTable[userId-1][2] = firstname;
        currentDataTable[userId-1][3] = lastname;
        currentDataTable[userId-1][4] = bio;
    }

    /**
     * This function will get the user data of a random user,
     * They will be in the form of a string array with the indexes:
     * 0 - username
     * 1 - first name
     * 2 - last name
     * 3 - bio
     * 4 - userId
     *
     * @param thisUserId the id of the user we don't want to receive
     * @return current users data - for a profile
     */
    public static String[] getRandomUserData(int thisUserId) {
        if (userCount == 1)
            return null;
        else {
            int randomUserId = thisUserId;
            while (randomUserId == thisUserId)
                randomUserId = (new Random()).nextInt(userCount) + 1;

            return getUserData(randomUserId);
        }
    }

    /**
     * This function returns the number of chats
     * @return the number of chats
     */
    public static int getNumOfChats() {
        return (new Random()).nextInt(userCount - 1) + 1;
    }

    /**
     * This function returns the user ids of all of the
     * users that talk with the given user
     * @param numOfChats the number of chat the user expects
     * @return the list of user id
     */
    public static int[] getUserIdChats(int numOfChats) {
        int[] users = new int[numOfChats];

        for (int i = 0; i < numOfChats; i++)
            users[i] = i + 2;

        return users;
    }
}