package com.example.cyberprojectclient.network;

public class NetworkAdapter {
    //Database structure:
    //[0] -> username
    //[1] -> password
    //[2] -> first name
    //[3] -> last name
    //[4] -> bio
    private static String[][] currentDataTable = new String[10][5];
    private static int userCount = 0;

    public static void initializeUsers() {
        currentDataTable[userCount][0] = "Admin";
        currentDataTable[userCount][1] = "12345";
        currentDataTable[userCount][2] = "Yony";
        currentDataTable[userCount][3] = "Halpern";
        currentDataTable[userCount][4] = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur consectetur tristique mi, in tempus odio ultricies vitae. Ut vitae ornare.";
        userCount++;
    }

    public static boolean attemptSignIn(String username, String password) {
        //TODO:
        //Insert here a check with the server where you send the hash of the password and
        //the username in order to see whether its valid sign in data, afterwards you should
        //set the user id of the user (if sign in is valid)
        for (int i = 0; i < userCount; i++) {
            if (currentDataTable[i][0].equals(username) && currentDataTable[i][1].equals(password))
            {
                //Here i need to also save the index of the user
                return true;
            }
        }
        return false;
    }

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

    public static String[] getUserData(int userId) {
        if (userId <= userCount && userId > 0)
            return currentDataTable[userId-1];
        else
            return null;
    }
}