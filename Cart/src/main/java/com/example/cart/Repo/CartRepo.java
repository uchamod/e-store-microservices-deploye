package com.example.cart.Repo;

import com.example.cart.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface CartRepo extends JpaRepository<Cart, UUID> {

     Cart findCartByCustomerId(UUID userId);
     Cart deleteByCustomerId(UUID userId);

}
