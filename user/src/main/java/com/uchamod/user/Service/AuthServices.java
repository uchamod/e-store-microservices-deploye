package com.uchamod.user.Service;


import com.uchamod.user.Model.User;
import com.uchamod.user.Model.UserAuthResponse;
import com.uchamod.user.Model.UserLoginCredientials;
import com.uchamod.user.Model.UserWrapper;
import com.uchamod.user.Reposotory.UserRepo;
import com.uchamod.user.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthServices {

    private final UserRepo userRepo;

    private final JwtUtil jwtUtil;

    //private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    //register new user
    public ResponseEntity<UserAuthResponse> register(User user) {
        try{
            if(user==null){
                System.out.println("empty user");
                return ResponseEntity.noContent().build();
            }
            if(user.getUserName().isEmpty() || user.getUserEmail().isEmpty() || user.getUserPassword().isEmpty()){
                System.out.println("empty user data");
                return ResponseEntity.badRequest().build();
            }
            //verify unique user
            User existingUserByEmail = userRepo.findByUserEmail(user.getUserEmail());
            User existingUserByPhone = userRepo.findByUserPhone(user.getUserPhone());
            if(existingUserByEmail != null || existingUserByPhone != null){
                System.out.println("user already exist");
                return ResponseEntity.badRequest().build();
            }
            //hased user password
          String hasedPassword=  passwordEncoder.encode(user.getUserPassword());
            user.setUserPassword(hasedPassword);
            // Set default role if not provided
            if (user.getUserRole() == null || user.getUserRole().isEmpty()) {
                System.out.println("defult role assign");
                user.setUserRole("CUSTOMER");
            }
            User savedUser=  userRepo.save(user);
            //generate jwt token
            String token= jwtUtil.generateToken(
                user.getUserEmail(),
                user.getUserRole(),
                user.getUserId().toString()
            );

            UserWrapper userWrapper = new UserWrapper(savedUser.getUserName(),
                    savedUser.getUserEmail(), savedUser.getUserRole(),
                    savedUser.getUserStatus(),
                    savedUser.getUserPhone(), savedUser.getUserAddress());

            return ResponseEntity.ok(new UserAuthResponse(token,userWrapper));
        }catch (Exception e){
            System.out.println("server error"+e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
//login with email and password
    public ResponseEntity<UserAuthResponse> login(UserLoginCredientials userLoginCredientials) {
        try{
            if(userLoginCredientials.getUserEmail().isEmpty() || userLoginCredientials.getUserPassword().isEmpty()){
                System.out.println("empty email or password");
                return ResponseEntity.badRequest().build();
            }
            User user = userRepo.findByUserEmail(userLoginCredientials.getUserEmail());
            if(user == null){
                System.out.println("user not exist in db");
                return ResponseEntity.notFound().build();
            }
            if(passwordEncoder.matches(userLoginCredientials.getUserPassword(),user.getUserPassword())){



                //generate jwt token
                String token= jwtUtil.generateToken(
                        user.getUserEmail(),
                        user.getUserRole(),
                        user.getUserId().toString()
                );
                UserWrapper userWrapper = new UserWrapper(user.getUserName(),
                        user.getUserEmail(), user.getUserRole(),
                        user.getUserStatus(),
                        user.getUserPhone(), user.getUserAddress());
                return ResponseEntity.ok(new UserAuthResponse(token,userWrapper));
            }else{
                System.out.println("incorrect password");
                return ResponseEntity.badRequest().build();
            }
        }catch (Exception e){
            System.out.println("faile to login"+e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
