package com.example.cyberprojectclient.network;

import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
//import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


import com.example.cyberprojectclient.R;
import com.macasaet.fernet.Key;
import com.macasaet.fernet.StringValidator;
import com.macasaet.fernet.Token;
import com.macasaet.fernet.Validator;

import org.json.JSONObject;

public class Listener extends Thread {
    private BufferedReader in;
    private final Key key;
    private static JSONObject latestAnswer;
    private static boolean receivedAnswer;
    private static ReentrantLock lock = new ReentrantLock();

    /**
     * Creates an instance of a Listener,
     * the listener will be run in a thread of its own
     * @param in The bufferedReader the instance will be listening to
     * @param key The symmetric key
     */
    public Listener(BufferedReader in, Key key) {
        this.in = in;
        this.key = key;

        latestAnswer = new JSONObject();
        receivedAnswer = false;
        start();
    }

    /**
     * This method will be the method
     * running in the listener's thread
     */
    public void run() {
        boolean serverOpen = true;
        while (serverOpen) {
            try {
                String encryptedReply = in.readLine();
                String decryptedReply = decryptMessageAES(encryptedReply, key);
                decryptedReply = decryptedReply.replace("'", "\"");
                JSONObject jo = new JSONObject(decryptedReply);

                if (!jo.get("id").equals("302")) {
                    lock.lock();

                    receivedAnswer = true;
                    latestAnswer = jo;

                    lock.unlock();
                }

            } catch (Exception e) {
                serverOpen = false;
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @return Whether an answer from the server was given
     */
    public static boolean isAnswerReceived() {
        lock.lock();
        boolean answer = receivedAnswer;
        lock.unlock();
        return answer;
    }

    /**
     * @return The latest answer from the server
     */
    public static JSONObject getLatestAnswer() {
        lock.lock();
        JSONObject answer = latestAnswer;
        receivedAnswer = false;
        lock.unlock();
        return answer;
    }

    /**
     * This methods will decrypt a given message using the symmetric
     * key given.
     * @param message The given message for decryption
     * @param key The symmetric key for decryption
     * @return Returns the decrypted message
     */
    public String decryptMessageAES(String message, Key key) {
        final Token token = Token.fromString(message);

        final Validator<String> validator = new StringValidator() {
            public TemporalAmount getTimeToLive() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return Duration.ofSeconds(Instant.MAX.getEpochSecond());
                }
                else { return null; }
            }
        };

        final String payload = token.validateAndDecrypt(key, validator);
        return payload;
    }
}