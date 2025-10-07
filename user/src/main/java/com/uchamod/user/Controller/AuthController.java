package com.uchamod.user.Controller;

import com.uchamod.user.Model.User;
import com.uchamod.user.Model.UserAuthResponse;
import com.uchamod.user.Model.UserLoginCredientials;
import com.uchamod.user.Service.AuthServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServices userServices;
    @GetMapping("/")
    public String testUser(){
        return "Hello user";
    }


    @PostMapping("/register")
    public ResponseEntity<UserAuthResponse> register(@RequestBody User user){
        return userServices.register(user);
    }
    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@RequestBody UserLoginCredientials userLoginCredientials){
        return userServices.login(userLoginCredientials);
    }
}








