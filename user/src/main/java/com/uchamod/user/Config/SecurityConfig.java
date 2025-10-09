package com.uchamod.user.Config;



import com.uchamod.user.Service.CustomOAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomOAuthService customOAuthService)throws Exception{
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.anyRequest().permitAll()
                      ).oauth2Login(oauth -> oauth.successHandler(customOAuthService)
                .failureHandler((request, response, exception) -> {
                    // Log error for debugging
                    System.err.println("OAuth failure: " + exception.getMessage());
                    response.sendRedirect("/error?message=" + exception.getMessage());  // Custom error redirect
                })
        );


        return http.build();
    }
}
