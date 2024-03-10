package com.example.lorby.controller;

import com.example.lorby.dto.UserLoginDTO;
import com.example.lorby.dto.UserRegisterDTO;
import com.example.lorby.dto.VerificationDTO;
import com.example.lorby.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDTO userRegisterDTO){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.registerUser(userRegisterDTO));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.loginUser(userLoginDTO));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerificationDTO verificationDTO){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.verifyUser(verificationDTO));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
