package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.model.CartItem;
import com.techstore.ecommercemaven.model.Product;
import com.techstore.ecommercemaven.service.CartService;
import com.techstore.ecommercemaven.service.ProductService;
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
    public String addToCart(@PathVariable Long id) {

        Product product =
                productService.getProductById(id);

        CartItem item = new CartItem();

        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setPrice(product.getPrice());
        item.setQuantity(1);

        cartService.save(item);

        return "redirect:/cart";
    }

    @GetMapping
    public String cart(Model model) {

        model.addAttribute(
                "cartItems",
                cartService.getAllItems());

        model.addAttribute(
                "total",
                cartService.getTotal());

        System.out.println(
                "Cart Total = " +
                        cartService.getTotal());

        return "cart";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        cartService.delete(id);

        return "redirect:/cart";
    }

}
