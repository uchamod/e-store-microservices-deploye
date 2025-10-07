package com.uchamod.user.Controller;


import com.uchamod.user.Model.UserAuthResponse;
import com.uchamod.user.Model.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/oauth")
public class OAuthController {


    @GetMapping("/google")
    public ResponseEntity<UserAuthResponse> googleAuthenticationFlow(
            @RequestParam("token") String token,
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("role") String role){
      UserWrapper userWrapper=new UserWrapper(email,name,role,null,null,null);
      return ResponseEntity.ok(new UserAuthResponse(token,userWrapper));
    }


}
