package com.hassan.auth.controlller;

import com.hassan.auth.model.EmailAuth;
import com.hassan.auth.repository.EmailAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${hashing-salt-text}")
    private static String saltText;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @PostMapping("/signup")
    public HashMap<String, String> createAuthDetails(@RequestBody EmailAuth emailAuth) {
        try {
            String hashedPassword = generateHash(emailAuth.getPassword());
            emailAuth.setPassword(hashedPassword);
            emailAuthRepository.save(emailAuth);
        } catch (DataIntegrityViolationException e) {
            return new HashMap<String, String>() {{
                put("message", "Existing User");
            }};
        }
        return new HashMap<String, String>() {{
            put("message", "Successfully signup user");
        }};

    }

    @PostMapping("/log")
    public HashMap<String, String> logWithEmailAuth(@RequestBody EmailAuth emailAuth) {
        String password = emailAuthRepository.findByEmail(emailAuth.getEmail()).get(0).getPassword();
        String hashedPassword = generateHash(emailAuth.getPassword());
        if(hashedPassword.equals(password))
            return new HashMap<String, String>() {{
                put("message", "Logged in");
            }};
        else
            return new HashMap<String, String>() {{
                put("message", "Invalid Password");
            }};
    }



    private static String generateHash(String input) {
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
