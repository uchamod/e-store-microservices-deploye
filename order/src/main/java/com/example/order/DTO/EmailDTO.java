package com.example.order.DTO;

import com.example.order.Model.OrderProductModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    private UUID sellerId;
    private Double totalAmount;
    private List<OrderProductModel> orderProductModelList;
}
