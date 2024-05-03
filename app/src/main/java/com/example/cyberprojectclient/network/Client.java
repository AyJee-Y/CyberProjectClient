package com.example.cyberprojectclient.network;

import android.util.Log;

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
            packet.put("password", password);
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
}