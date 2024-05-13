package com.example.cyberprojectclient.network;

import android.util.Log;

import com.example.cyberprojectclient.utils.Message;
import com.macasaet.fernet.Key;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

public class Client {
    private static Client instance = null;
    private Socket socket;
    private Listener listener;
    private Writer writer;

    /**
     * Singleton implementation
     * @return The instance of the class
     */
    public static Client getInstance() {
        if (instance == null)
            instance = new Client();
        return instance;
    }

    /**
     * Creates an object
     */
    private Client() {
        initializeClient();
    }

    /**
     * Initializes the clients connection with the server
     * as well performs a TLS protocol
     */
    private void initializeClient() {
        Connector connector = new Connector();
        connector.start();

        while (!Connector.connectionFinished()) {}
        writer = new Writer(Connector.out, Connector.key);
        listener = new Listener(Connector.in, Connector.key);
    }

    /**
     * This method using the SHA-256 algorithm
     * hashes a given string, this method is for ensuring
     * the password wont be saved in the server to
     * prevent data leak;
     * @param string The string to be hashed
     * @return The hashed string
     */
    private String hashString(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(string.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method will attempt to sign into an account, it will
     * receive the userId of the account it signed into if the signing
     * was successful, otherwise it will return -1
     *
     * @param username The username of the account
     * @param password The password of the account
     * @return UserId of the account (or -1 if failed)
     */
    public int attemptSignIn(String username, String password) {
        try {
            //Log.d("TEST", "1");
            JSONObject packet = new JSONObject();

            packet.put("id", "100");
            packet.put("username", username);
            packet.put("password", hashString(password));
            //Log.d("TEST", "1");
            Writer.setPacketForSending(packet);
            //Log.d("TEST", "2");
            while (!Listener.isAnswerReceived()) {}
            JSONObject answer = Listener.getLatestAnswer();

            if (answer.get("id").equals("100")) {
                Log.d("userId", (String) answer.get("user"));
                return Integer.parseInt((String)answer.get("user"));
            }
            return -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void announceUser(int userId) {
        try {
            JSONObject packet = new JSONObject();

            packet.put("id", "102");
            packet.put("userId", String.valueOf(userId));

            Writer.setPacketForSending(packet);

            while (!Listener.isAnswerReceived()) {}
            Listener.getLatestAnswer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getUserChats(int userId) {
        try {
            JSONObject packet = new JSONObject();

            packet.put("id", "300");
            packet.put("user", String.valueOf(userId));

            Writer.setPacketForSending(packet);

            while (!Listener.isAnswerReceived()) {}
            return Listener.getLatestAnswer();
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    public JSONObject getProfileData(int userId) {
        try {
            JSONObject packet = new JSONObject();

            packet.put("id", "200");
            packet.put("user", String.valueOf(userId));

            Writer.setPacketForSending(packet);

            while (!Listener.isAnswerReceived()) {}
            JSONObject answer = Listener.getLatestAnswer();

            if (answer.get("id").equals("200"))
                return answer;
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getRandomUser(int userId) {
        try {
            JSONObject packet = new JSONObject();
            packet.put("id", "203");
            packet.put("selfUserId", String.valueOf(userId));

            Writer.setPacketForSending(packet);
            while (!Listener.isAnswerReceived()) {}
            JSONObject answer = Listener.getLatestAnswer();
            if (answer.get("id").equals("200"))
                return answer;
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject createNewAccount(String username, String password, String firstName, String lastName, String bio) {
        try {
            JSONObject packet = new JSONObject();
            packet.put("id", "101");
            packet.put("username", username);
            packet.put("password", hashString(password));
            packet.put("firstName", firstName);
            packet.put("lastName", lastName);
            packet.put("bio", bio);

            Writer.setPacketForSending(packet);
            while (!Listener.isAnswerReceived()) {}
            return Listener.getLatestAnswer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changeProfileData(int userId, String newFirstName, String newLastName, String newBio) {
        try {
            JSONObject packet = new JSONObject();

            packet.put("id", "202");
            packet.put("userId", String.valueOf(userId));
            packet.put("newFirstName", newFirstName);
            packet.put("newLastName", newLastName);
            packet.put("newBio", newBio);

            Writer.setPacketForSending(packet);

            while (!Listener.isAnswerReceived()) {}
            Listener.getLatestAnswer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}