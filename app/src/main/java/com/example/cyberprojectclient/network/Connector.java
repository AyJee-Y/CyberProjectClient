package com.example.cyberprojectclient.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.locks.ReentrantLock;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import com.macasaet.fernet.Key;

import org.json.JSONObject;

public class Connector extends Thread {
    private Socket socket;
    public static BufferedWriter out;
    public static BufferedReader in;
    public static Key key;
    public static boolean finished = false;
    public static ReentrantLock lock = new ReentrantLock();

    /**
     * Creates an instance of the connector class,
     * it's goal is to preform the TLS protocol
     * wtih the server.
     */
    public Connector() { }

    /**
     * This method will cause the client to preform a
     * TLS protocol with the server, it will save the key and
     * the reader and writer for later use.
     */
    public void run() {
        try {
            SecureRandom random = new SecureRandom();
            InetAddress serverIp = InetAddress.getByName("192.168.50.209");
            this.socket = new Socket(serverIp, 8512);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PublicKey publicKey = createPublicKey(in);
            key = createAndSendSymmetricKey(out, random, publicKey);

            lock.lock();
            finished = true;
            lock.unlock();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This function will return whether the
     * TLS protocol with the server is finished
     *
     * @return Whether the connection is finished
     */
    public static boolean connectionFinished() {
        lock.lock();
        boolean result = finished;
        lock.unlock();
        return result;
    }

    /**
     * This method will receive from the server the string
     * of the public key and it will create the public key
     * and it will return it
     * @param in The bufferedReader of the client
     * @return The server's public key
     */
    private PublicKey createPublicKey(BufferedReader in) throws Exception {
        String jsonObjectString = in.readLine();
        jsonObjectString = jsonObjectString.replaceAll("\\n", "");
        JSONObject received = new JSONObject(jsonObjectString);

        String publicKeyString = (String) received.get("publicKey");
        String publicKeyContent = publicKeyString.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        byte[] publicBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            publicBytes = Base64.getDecoder().decode(publicKeyContent);
        }
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(publicBytes);
        PublicKey pubKey = keyFactory.generatePublic(keySpecX509);

        return pubKey;
    }

    /**
     * This function creates a symmetric key, it
     * returns it and also sends it to the server
     *
     * @param out The BufferedWriter from the client socket
     * @param random A secure random for key generation
     * @param publicKey The public key for encryption
     * @return The created symmetric key
     */
    private Key createAndSendSymmetricKey(BufferedWriter out, SecureRandom random, PublicKey publicKey) throws Exception {
        final Key key = createSymmetricKey(random);

        String input = key.serialise();
        JSONObject packet = new JSONObject();

        packet.put("id", "0");
        packet.put("key", input);

        String encryptedMessage = encryptAsymetricly(packet.toString().getBytes(), publicKey);

        out.write(encryptedMessage);
        out.flush();

        return key;
    }

    /**
     * This method creates a symmetric key and returns it
     *
     * @param thisRandom A secureRandom for the key generation
     * @return  The generated symmetric key
     */
    private Key createSymmetricKey(SecureRandom thisRandom) {
        Key key = Key.generateKey(thisRandom);

        return key;
    }

    /**
     * This method encrypts a message using a public key
     *
     * @param toEncrypt A message that will be encrypted using the public key
     * @param pubKey The public key
     * @return Encrypted message
     */
    private String encryptAsymetricly(byte[] toEncrypt, PublicKey pubKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPwithSHA-256andMGF1Padding");

        cipher.init(Cipher.ENCRYPT_MODE, pubKey, new OAEPParameterSpec("SHA-256",
                "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        byte[] cipherText = cipher.doFinal(toEncrypt);

        String encryptedMessage = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encryptedMessage = Base64.getEncoder().encodeToString(cipherText);
        }

        return encryptedMessage;
    }
}