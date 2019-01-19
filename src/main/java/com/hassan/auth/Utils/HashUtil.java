package com.hassan.auth.Utils;

import org.springframework.beans.factory.annotation.Value;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    @Value("${hashing-salt-text}")
    private static String saltText;

    public static String generateHash(String input) {
        StringBuilder hash = new StringBuilder();
        String saltedInput = saltText + ":" + input;

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = sha.digest(saltedInput.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length;   idx++) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash.toString();
    }

}
