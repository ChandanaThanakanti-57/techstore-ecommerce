package com.techstore.ecommercemaven.repository;

import com.techstore.ecommercemaven.model.Review;
import com.techstore.ecommercemaven.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);

    Review findByProductAndUserName(
            Product product,
            String userName);

    List<Review> findByProductIdAndApprovedTrue(Long productId);

    long countByApprovedFalse();
}