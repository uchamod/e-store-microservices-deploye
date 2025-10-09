//package com.uchamod.user.Service;
//
//import com.uchamod.user.Model.User;
//import com.uchamod.user.Reposotory.UserRepo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//@RequiredArgsConstructor
//public class UserDetailService implements UserDetailsService {
//
//    private final UserRepo userRepo;
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//      User user = userRepo.findByUserEmail(email);
//      if(user == null){
//          throw new UsernameNotFoundException("User not found"+email);
//
//      }
//      return org.springframework.security.core.userdetails.User.builder()
//              .username(user.getUserEmail())
//              .password(user.getUserPassword())
//              .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE "+user.getUserRole())))
//              .build();
//
//    }
//}
