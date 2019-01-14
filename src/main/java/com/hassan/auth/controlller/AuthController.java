package com.hassan.auth.controlller;

import com.hassan.auth.model.EmailAuth;
import com.hassan.auth.repository.EmailAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
public class AuthController {

    @Value("${hashing-salt-text}")
    private String saltText;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @PostMapping("/signup")
    public String createAuthDetails(@RequestBody EmailAuth emailAuth) {
        String saltedPassword = saltText + ":" + emailAuth.getPassword();
        String hashedPassword = generateHash(saltedPassword);
        emailAuth.setPassword(hashedPassword);
        emailAuthRepository.save(emailAuth);
        return "Successfully signup user";
    }

    @PostMapping("/log")
    public String logWithEmailAuth(@RequestBody EmailAuth emailAuth) {
        String password = emailAuthRepository.findByEmail(emailAuth.getEmail()).get(0).getPassword();
        String saltedPassword = saltText + ":" + emailAuth.getPassword();
        String hashedPassword = generateHash(saltedPassword);
        System.out.println(hashedPassword);
        System.out.println(emailAuth.getPassword());
        if(hashedPassword.equals(password))
            return "Logged in";
        else
            return "Invalid Password";
    }



    private static String generateHash(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = sha.digest(input.getBytes());
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
