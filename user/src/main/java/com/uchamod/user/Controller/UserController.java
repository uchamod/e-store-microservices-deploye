package com.uchamod.user.Controller;

import com.uchamod.user.Model.UserWrapper;
import com.uchamod.user.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {

    private final UserService userServices;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        return userServices.getAllUsers();
    }

    @GetMapping("/getUserByEmail")
    public ResponseEntity<UserWrapper> getUserByEmail(@RequestHeader("X-Username") String email) {

        return userServices.getUserByEmail(email);
    }

    @GetMapping("/getUserById")
    public ResponseEntity<UserWrapper> getUserById(@RequestHeader("X-User-Id") String userId) {
        return userServices.getUserById(UUID.fromString(userId));
    }
    @GetMapping("/getAllUserByCategory/{userRole}")
    public ResponseEntity<List<UserWrapper>> getAllUserByCategory(@PathVariable String userRole){
        return userServices.getAllCustomers(userRole);
    }
   /* @GetMapping("/getAllSellers")
    public ResponseEntity<List<UserWrapper>> getAllSellers(){
        return userServices.getAllSellers();
    }*/
   @GetMapping("/getUserModel/{userId}")
   ResponseEntity<UserWrapper> getUserDTO(@PathVariable UUID userId){
       return userServices.getUserById(userId);
   }

}