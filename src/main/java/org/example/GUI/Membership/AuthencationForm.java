package org.example.GUI.Membership;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class AuthencationForm {

    public abstract boolean createForm();

    public String encrypt(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

