package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.CartItem;
import com.techstore.ecommercemaven.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartItem save(CartItem item) {
        return cartRepository.save(item);
    }

    public List<CartItem> getAllItems() {
        return cartRepository.findAll();
    }

    public void delete(Long id) {
        cartRepository.deleteById(id);
    }

    public double getTotal() {

        return cartRepository.findAll()
                .stream()
                .mapToDouble(item ->
                        item.getPrice() * item.getQuantity())
                .sum();
    }

    public void clearCart() {
        cartRepository.deleteAll();
    }
}