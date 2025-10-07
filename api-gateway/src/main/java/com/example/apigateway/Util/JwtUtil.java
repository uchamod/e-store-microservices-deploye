package com.example.apigateway.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret:nchdenedjeyeneg364858939$%^&#7374HWNKFSDHEGYJKSDFJIOHDAG#NJDHIEDD}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expireTime;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


    //extract email from token
    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }
    //extract role from token
    public String extractRole(String token){
        return extractClaim(token, claims -> claims.get("role",String.class));
    }
    //extract id from token
    public String extractId(String token){
        return extractClaim(token, claims -> claims.get("userId",String.class));
    }
    //extract expire time from token
    public Date extractExpireTime(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    //check expireation
    public Boolean isTokenExpired(String token){
        return extractExpireTime(token).before(new Date());
    }
    //check validation
    public Boolean validateToken(String token){
          try{
              if(isTokenExpired(token)){
                  return false;
              }
              Jwts.parser()
                      .verifyWith(getSigningKey())
                      .build()
                      .parseSignedClaims(token);

              return true;
          }catch (JwtException | IllegalArgumentException e){
              return false;
          }
//        final String email=extractEmail(token);
//        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //extract claims(email,role,id)
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
