package com.example.order.Feign;

import com.example.order.DTO.Cart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("CART")
public interface OrderFeignClient {
    @DeleteMapping("/api/cart/checkoutFromCart/{userId}")
    ResponseEntity<String> checkoutFromCart(@PathVariable UUID userId);

    @GetMapping("/api/cart/getCartByUserId/{userId}")
    ResponseEntity<Cart> getCartByUserId(@PathVariable UUID userId);
}
