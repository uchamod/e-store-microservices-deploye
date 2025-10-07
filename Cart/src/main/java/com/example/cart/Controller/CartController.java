package com.example.cart.Controller;

import com.example.cart.Model.Cart;
import com.example.cart.Model.CartProduct;
import com.example.cart.Model.ProductResponse;
import com.example.cart.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/cart")
public class CartController {


    private final  CartService cartService;

    @GetMapping("/getProductFromCart")
    public ResponseEntity<ProductResponse> getProductFromCart(@RequestHeader("X-User-Id") String userId){
        return cartService.getProductFromCart(UUID.fromString(userId));
    }

    @PostMapping("/addToCart/{productId}")
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-Id") String userId, @PathVariable UUID productId){
        return cartService.addToCart(UUID.fromString(userId),productId);
    }

    @PutMapping("/updateCart/{productId}/count/{count}")
    public ResponseEntity<List<CartProduct>> updateCartItem(@RequestHeader("X-User-Id") String userId,
                                                 @PathVariable UUID productId,
                                                 @PathVariable Integer count){
        return cartService.updateCartItem(UUID.fromString(userId),productId,count);
    }

    @DeleteMapping("/deleteCartItem/{productId}")
    public ResponseEntity<String> deleteCartItem(@RequestHeader("X-User-Id") String userId, @PathVariable UUID productId){
        return cartService.deleteCartItem(UUID.fromString(userId),productId);
    }

   @PutMapping("/toggleCheckOut")
    public ResponseEntity<String> toggleCheckOut(@RequestHeader("X-User-Id") String userId, @RequestBody List<UUID> productIds){
        return cartService.toggleCheckOut(UUID.fromString(userId),productIds);
   }

   @GetMapping("/getTotal")
    public ResponseEntity<Double> getTotal(@RequestHeader("X-User-Id") String userId){
        return cartService.getTotal(UUID.fromString(userId));
   }
    @DeleteMapping("/api/cart/checkoutFromCart/{userId}")
    ResponseEntity<String> checkoutFromCart(@PathVariable UUID userId){
        return cartService.checkoutFromCart(userId);
    }
    @GetMapping("/getCartByUserId/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable UUID userId){
        return cartService.getCartByUserId(userId);
    }

}
