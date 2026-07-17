package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.CartItem;
import com.techstore.ecommercemaven.model.User;
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



    public List<CartItem> getUserCart(User user) {

        return cartRepository.findByUser(user);

    }



    public double getUserTotal(User user) {

        return cartRepository.findByUser(user)
                .stream()
                .mapToDouble(item ->
                        item.getPrice()
                                * item.getQuantity())
                .sum();

    }



    public void delete(Long id) {

        cartRepository.deleteById(id);

    }



    public void clearCart(User user) {

        List<CartItem> items =
                cartRepository.findByUser(user);

        cartRepository.deleteAll(items);

    }

}