package com.example.cart.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWrapper {

    private UUID productId;
    private String productName;
    private Double productPrice;
    private Integer productCount;
    private Boolean isAvailable;
    private String productBrand;
    private String productDescription;
    private String productCategory;
    private String productImage;
    private Integer purchaseCount;
}
