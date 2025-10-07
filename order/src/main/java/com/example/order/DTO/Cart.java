package com.example.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private UUID cartId;
    private UUID customerId;
    private Double totalAmount;
    private List<CartProduct> cartProductList;
}
