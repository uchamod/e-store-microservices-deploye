package com.example.order.Feign;

import com.example.order.DTO.UserWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("USERS-SERVICE")
public interface UserFeignClient {

    @GetMapping("/api/user/getUserModel/{userId}")
    ResponseEntity<UserWrapper> getUserDTO(@PathVariable UUID userId);

}
