package com.example.order.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    private UUID customerId;
    private Double totalAmount;
    private String orderStatus="PENDING";
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
    private Date lastUpdate;
    @ElementCollection
    private List<OrderProductModel> orderProductModelList;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdate = new Date();
    }
}

