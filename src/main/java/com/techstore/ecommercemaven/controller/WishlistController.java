package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.model.Product;
import com.techstore.ecommercemaven.model.User;
import com.techstore.ecommercemaven.model.Wishlist;
import com.techstore.ecommercemaven.repository.ProductRepository;
import com.techstore.ecommercemaven.repository.WishlistRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WishlistController {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistController(
            WishlistRepository wishlistRepository,
            ProductRepository productRepository) {

        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/wishlist")
    public String wishlist(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute(
                        "loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "wishlistItems",
                wishlistRepository.findByUserEmail(
                        user.getEmail()));

        return "wishlist";
    }

    @GetMapping("/wishlist/add/{id}")
    public String addToWishlist(
            @PathVariable Long id,
            HttpSession session) {

        User user =
                (User) session.getAttribute(
                        "loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Wishlist existing =
                wishlistRepository
                        .findByUserEmailAndProduct_Id(
                                user.getEmail(),
                                id);

        if (existing == null) {

            Product product =
                    productRepository.findById(id)
                            .orElse(null);

            if (product != null) {

                Wishlist wishlist =
                        new Wishlist();

                wishlist.setUserEmail(
                        user.getEmail());

                wishlist.setProduct(product);

                wishlistRepository.save(
                        wishlist);
            }
        }

        return "redirect:/products/" + id;
    }

    @GetMapping("/wishlist/remove/{id}")
    public String removeWishlist(
            @PathVariable Long id) {

        wishlistRepository.deleteById(id);

        return "redirect:/wishlist";
    }
}