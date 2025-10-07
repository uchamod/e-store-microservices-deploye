package com.example.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerDTO {

    private UUID productId;
    private Integer productCount;
    private Double ProductPrice;
    private UUID customerId;
    private String orderStatus;
}
