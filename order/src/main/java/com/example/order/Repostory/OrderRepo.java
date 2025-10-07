package com.example.order.Repostory;

import com.example.order.DTO.SellerDTO;
import com.example.order.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {

    List<Order> findOrdersByCustomerId(UUID uuid);

    //get seller dto data
    @Query("""
            SELECT new com.example.order.DTO.SellerDTO(
                opm.productId, 
                opm.productCount, 
                opm.productItemPrice, 
                o.customerId, 
                o.orderStatus
            )
            FROM Order o 
            JOIN o.orderProductModelList opm 
            WHERE opm.sellerId = :sellerId
            ORDER BY o.lastUpdate DESC
            """)
    List<SellerDTO> findSellerOrdersBySellerId(UUID sellerId);

}
