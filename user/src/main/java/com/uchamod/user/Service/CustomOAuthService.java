package com.uchamod.user.Service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.uchamod.user.Model.User;
import com.uchamod.user.Model.UserAuthResponse;
import com.uchamod.user.Model.UserWrapper;
import com.uchamod.user.Reposotory.UserRepo;
import com.uchamod.user.Util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class CustomOAuthService implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try{
            OAuth2AuthenticationToken authenticationToken= (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User=authenticationToken.getPrincipal();

            String email=oAuth2User.getAttribute("email");
            String name=oAuth2User.getAttribute("name");

            //check if user in db
            User user=userRepo.findByUserEmail(email);
            if(user == null){
                user=new User();
                user.setUserEmail(email);
                user.setUserName(name);
                user.setUserRole("CUSTOMER");
                user.setUserStatus("ACTIVE");
                userRepo.save(user);
            }
            String token= jwtUtil.generateToken(
                    user.getUserEmail(),
                    user.getUserRole(),
                    user.getUserId().toString()
            );
            System.out.println(token);
            // Create response object
            UserWrapper userWrapper = new UserWrapper(
                    user.getUserEmail(),
                    user.getUserName(),
                    user.getUserRole(),
                    null, null, null
            );
            UserAuthResponse authResponse = new UserAuthResponse(token, userWrapper);
            // Write JSON response directly to browser
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(authResponse));
            response.getWriter().flush();
           /* response.sendRedirect("/api/oauth/google?token=" + token+
                    "&email=" + URLEncoder.encode(user.getUserEmail(), StandardCharsets.UTF_8) +
                    "&name=" + URLEncoder.encode(user.getUserName(), StandardCharsets.UTF_8) +
                    "&role=" + user.getUserRole());*/


        } catch (Exception e){
            System.err.println("OAuth Error: " + e.getMessage());
            e.printStackTrace();
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
