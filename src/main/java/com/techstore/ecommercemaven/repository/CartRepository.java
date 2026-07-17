package com.techstore.ecommercemaven.repository;

import com.techstore.ecommercemaven.model.CartItem;
import com.techstore.ecommercemaven.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository
        extends JpaRepository<CartItem, Long> {


    List<CartItem> findByUser(User user);

}