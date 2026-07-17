package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.model.Product;
import com.techstore.ecommercemaven.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.techstore.ecommercemaven.model.Review;
import com.techstore.ecommercemaven.repository.ReviewRepository;
import com.techstore.ecommercemaven.dto.ProductSuggestion;
import com.techstore.ecommercemaven.repository.OrderItemRepository;

import java.io.File;
import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;
    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;
    public HomeController(
            ProductService productService,
            ReviewRepository reviewRepository,
            OrderItemRepository orderItemRepository) {

        this.productService = productService;
        this.reviewRepository = reviewRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return "redirect:/admin/products";
    }

    @GetMapping("/edit-product/{id}")
    public String showEditProductForm(
            @PathVariable Long id,
            Model model) {

        Product product = productService.getProductById(id);

        model.addAttribute("product", product);

        return "edit-product";
    }

    @PostMapping("/update-product")
    public String updateProduct(Product product) {

        productService.saveProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/products/search")
    public String searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            Model model) {

        List<Product> products;

        if (keyword != null && !keyword.isEmpty()
                && category != null && !category.isEmpty()) {

            products =
                    productService.searchByKeywordAndCategory(
                            keyword,
                            category);

        } else if (keyword != null && !keyword.isEmpty()) {

            products =
                    productService.searchProducts(keyword);

        } else if (category != null && !category.isEmpty()) {

            products =
                    productService.getProductsByCategory(category);

        } else {

            products =
                    productService.getAllProducts();
        }

        model.addAttribute("products", products);

        return "products";
    }

    @GetMapping("/autocomplete")
    @ResponseBody
    public List<ProductSuggestion> autocomplete(
            @RequestParam String keyword) {

        return productService
                .searchProducts(keyword)
                .stream()
                .limit(5)
                .map(product -> new ProductSuggestion(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImageUrl()))
                .toList();
    }

    @GetMapping("/products")
    public String products(
            Model model,
            jakarta.servlet.http.HttpSession session) {

        model.addAttribute(
                "user",
                session.getAttribute("loggedInUser"));

        model.addAttribute(
                "products",
                productService.getAllProducts());

        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetails(
            @PathVariable Long id,
            Model model) {

        Product product =
                productService.getProductById(id);

        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);

        List<Review> reviews =
                reviewRepository.findByProduct(product);

        double averageRating = 0;

        if (!reviews.isEmpty()) {

            int total = 0;

            for (Review review : reviews) {
                total += review.getRating();
            }

            averageRating =
                    (double) total / reviews.size();
        }

        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);

        List<Product> relatedProducts =
                productService.getProductsByCategory(
                        product.getCategory());

        relatedProducts.removeIf(
                p -> p.getId().equals(product.getId()));

        model.addAttribute(
                "relatedProducts",
                relatedProducts);

        return "product-details";

    }


    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Home Controller Works";
    }

    @GetMapping("/test-image")
    @ResponseBody
    public String testImage() {

        File file = new File(
                "D:/EcommerceWebsite/IdeaProjects/ecommerce-Maven/uploads/laptop.png");

        return file.exists() + " : " + file.getAbsolutePath();
    }
}