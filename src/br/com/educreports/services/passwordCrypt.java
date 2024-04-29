/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.educreports.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Nick1
 */
public class passwordCrypt {

    public static String passCrypt(String password) throws NoSuchAlgorithmException {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        byte[] messageDigest = algorithm.digest(password.getBytes(StandardCharsets.UTF_8));
        String encrypted_password = bytesToHex(messageDigest);
        return encrypted_password;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

}
