package com.uchamod.estore.Model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;
    private UUID sellerId;
    private String productName;
    private Double productPrice;
    private Integer productCount;
    private Boolean isAvailable;
    private String productBrand;
    private String productDescription;
    private String productCategory;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
    private Date productReleaseDate;

    //image Data
    private String productImageType;
    private String productImageName;
    private String productImage;

}
