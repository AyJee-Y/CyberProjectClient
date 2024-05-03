package com.example.cyberprojectclient.network;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;


import com.example.cyberprojectclient.R;
import com.macasaet.fernet.Key;
import com.macasaet.fernet.Token;

import org.json.JSONObject;

public class Writer extends Thread {
    private BufferedWriter out;
    private final Key key;
    private SecureRandom random;
    private static boolean shouldSentPacket = false;
    private static ReentrantLock lock = new ReentrantLock();
    private static JSONObject packet;

    public Writer(BufferedWriter out, Key key) {
        this.out = out;
        this.key = key;
        random = new SecureRandom();

        start();
    }

    @Override
    public void run() {
        while (true) {
            if (shouldSentPacket()) {
                try {
                    lock.lock();
                    JSONObject packForSending = packet;
                    shouldSentPacket = false;
                    lock.unlock();

                    String jsonString = packForSending.toString();
                    String encryptedJsonString = encryptMessageAES(jsonString, key, random);
                    out.write(encryptedJsonString);
                    out.flush();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * @return Whether the writer thread should send a packet
     */
    public static boolean shouldSentPacket() {
        lock.lock();
        boolean answer = shouldSentPacket;
        lock.unlock();
        return answer;
    }

    /**
     * Saves the packet that the writer will send
     * @param packetForSending The packet that will be sent
     */
    public static void setPacketForSending(JSONObject packetForSending) {
        lock.lock();
        packet = packetForSending;
        shouldSentPacket = true;
        lock.unlock();
    }


    /**
     * This method encrypts a message using the symmetric key
     * given
     *
     * @param message The message to be encrypted
     * @param key The key of the symmetric encryption
     * @param thisRandom A secure random for the encryption
     * @return The encrypted message
     */
    private String encryptMessageAES(String message, Key key, SecureRandom thisRandom) {
        final Token token = Token.generate(thisRandom, key, message);

        return token.serialise();
    }
}
