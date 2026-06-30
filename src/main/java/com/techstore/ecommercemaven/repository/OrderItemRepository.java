package com.techstore.ecommercemaven.repository;

import com.techstore.ecommercemaven.dto.TopProductDTO;
import com.techstore.ecommercemaven.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    @Query("""
    SELECT new com.techstore.ecommercemaven.dto.TopProductDTO(
        oi.productName,
        SUM(oi.quantity)
    )
    FROM OrderItem oi
    GROUP BY oi.productName
    ORDER BY SUM(oi.quantity) DESC
""")
    List<TopProductDTO> getTopSellingProducts();

}
