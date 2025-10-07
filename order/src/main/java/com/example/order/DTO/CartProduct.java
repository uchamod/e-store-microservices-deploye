package com.example.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {

    private UUID productId;
    private UUID sellerId;
    private Integer productCount;
    private Double productItemPrice;
    private Boolean isCheckout;
}
