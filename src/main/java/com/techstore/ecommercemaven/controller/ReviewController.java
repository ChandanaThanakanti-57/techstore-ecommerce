package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.model.Product;
import com.techstore.ecommercemaven.model.Review;
import com.techstore.ecommercemaven.model.User;
import com.techstore.ecommercemaven.repository.ProductRepository;
import com.techstore.ecommercemaven.repository.ReviewRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewController(
            ReviewRepository reviewRepository,
            ProductRepository productRepository) {

        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/add-review")
    public String addReview(

            @RequestParam Long productId,
            @RequestParam int rating,
            @RequestParam String comment,
            HttpSession session) {

        User user =
                (User) session.getAttribute(
                        "loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        Product product =
                productRepository.findById(productId)
                        .orElse(null);

        if (product == null) {
            return "redirect:/products";
        }

        Review review =
                reviewRepository.findByProductAndUserName(
                        product,
                        user.getName());

        if (review == null) {

            review = new Review();

            review.setProduct(product);

            review.setUserName(user.getName());

        }

        review.setRating(rating);

        review.setComment(comment);

        review.setReviewDate(
                LocalDateTime.now());

        reviewRepository.save(review);

        System.out.println("REVIEW SAVED SUCCESSFULLY");

        return "redirect:/products/" + productId;
    }

    @GetMapping("/admin/review/approve/{id}")
    public String approveReview(@PathVariable Long id) {

        Review review = reviewRepository.findById(id).orElse(null);

        if (review != null) {
            review.setApproved(true);
            reviewRepository.save(review);
        }

        return "redirect:/admin/reviews";
    }

    @GetMapping("/admin/review/delete/{id}")
    public String deleteReview(@PathVariable Long id) {

        reviewRepository.deleteById(id);

        return "redirect:/admin/reviews";
    }

}
