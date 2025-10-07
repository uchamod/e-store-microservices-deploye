package com.example.order.Model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderProductModel {

    private UUID productId;
    private UUID sellerId;
    private Integer productCount;
    private Double productItemPrice;

}
