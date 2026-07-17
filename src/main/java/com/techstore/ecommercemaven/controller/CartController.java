package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.model.CartItem;
import com.techstore.ecommercemaven.model.Product;
import com.techstore.ecommercemaven.model.User;
import com.techstore.ecommercemaven.service.CartService;
import com.techstore.ecommercemaven.service.ProductService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {


    private final CartService cartService;
    private final ProductService productService;


    public CartController(
            CartService cartService,
            ProductService productService) {

        this.cartService = cartService;
        this.productService = productService;
    }



    @GetMapping("/add/{id}")
    public String addToCart(
            @PathVariable Long id,
            HttpSession session) {


        User user =
                (User) session.getAttribute("loggedInUser");


        if(user == null){
            return "redirect:/login";
        }


        Product product =
                productService.getProductById(id);



        CartItem item = new CartItem();


        item.setUser(user);

        item.setProductId(product.getId());

        item.setProductName(product.getName());

        item.setPrice(product.getPrice());

        item.setQuantity(1);



        cartService.save(item);



        return "redirect:/cart";
    }




    @GetMapping
    public String cart(
            HttpSession session,
            Model model) {


        User user =
                (User) session.getAttribute("loggedInUser");


        if(user == null){
            return "redirect:/login";
        }


        model.addAttribute(
                "cartItems",
                cartService.getUserCart(user));


        model.addAttribute(
                "total",
                cartService.getUserTotal(user));


        return "cart";
    }




    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id) {


        cartService.delete(id);


        return "redirect:/cart";
    }

}