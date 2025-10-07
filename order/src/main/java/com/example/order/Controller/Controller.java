package com.example.order.Controller;

import com.example.order.DTO.SellerDTO;
import com.example.order.Model.Order;
import com.example.order.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class Controller {

    private final OrderService orderService;
    @PostMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(
                                            @RequestHeader("X-User-Id") String sellerId
                                           ){
        return orderService.placeOrder(UUID.fromString(sellerId));
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<List<Order>> getAllOrders(){
        return orderService.getAllOrders();
    }
    @GetMapping("/getOrdersBySellerId")
    public ResponseEntity<List<SellerDTO>> getOrdersBySellerId(@RequestHeader("X-User-Id") String sellerId,
                                                               @RequestHeader("X-User-Role") String role){
        return orderService.getOrdersBySellerId(UUID.fromString(sellerId),role);
    }

    @GetMapping("/getOrdersByCustomerId")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@RequestHeader("X-User-Id") String sellerId,
                                                             @RequestHeader("X-User-Role") String role){
        return orderService.getOrdersByCustomerId(UUID.fromString(sellerId),role);
    }
    @PutMapping("/purcheForOrder/{orderId}/status/{status}")
    public ResponseEntity<String> purcheForOrder(@PathVariable UUID orderId,@PathVariable String status){
        return orderService.purcheForOrder(orderId,status);
    }
}
