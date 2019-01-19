package com.hassan.auth.controlller;

import com.hassan.auth.Utils.HashUtil;
import com.hassan.auth.model.EmailAuth;
import com.hassan.auth.repository.EmailAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @PostMapping("/signup")
    public ResponseEntity<HashMap<String, String>> createAuthDetails(@RequestBody EmailAuth emailAuth) {
        HashMap<String, String> result = new HashMap<>();
        try {

            String hashedPassword = HashUtil.generateHash(emailAuth.getPassword());
            emailAuth.setPassword(hashedPassword);
            emailAuthRepository.save(emailAuth);
            result.put("message", "Successfully signup user");

        } catch (DataIntegrityViolationException e) {
            result.put("message", "Existing User");
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/log")
    public ResponseEntity<HashMap<String, String>> logWithEmailAuth(@RequestBody EmailAuth emailAuth) {

        String password;
        HashMap<String, String> result = new HashMap<>();
        List<EmailAuth> emailAuths = emailAuthRepository.findByEmail(emailAuth.getEmail());

        if(emailAuths.size() > 0) {
            password = emailAuths.get(0).getPassword();
            String hashedPassword = HashUtil.generateHash(emailAuth.getPassword());
            if(hashedPassword.equals(password)) {
                result.put("message", "Logged in");
            }
            else {
                result.put("message", "Invalid Password");
            }
        }
        else {
            result.put("message", "User is not signup");
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}
