package com.techstore.ecommercemaven.repository;

import com.techstore.ecommercemaven.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository
        extends JpaRepository<CartItem, Long> {
}