package com.techstore.ecommercemaven.repository;

import com.techstore.ecommercemaven.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.techstore.ecommercemaven.dto.WishlistAnalyticsDTO;
import java.util.List;

public interface WishlistRepository
        extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUserEmail(
            String userEmail);

    Wishlist findByUserEmailAndProduct_Id(
            String userEmail,
            Long productId);

    @Query("""
SELECT new com.techstore.ecommercemaven.dto.WishlistAnalyticsDTO(
    w.product.name,
    COUNT(w.id)
)
FROM Wishlist w
GROUP BY w.product.name
ORDER BY COUNT(w.id) DESC
""")
    List<WishlistAnalyticsDTO> getMostWishlistedProducts();
}