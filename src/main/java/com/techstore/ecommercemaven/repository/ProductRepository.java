package com.techstore.ecommercemaven.repository;

import com.techstore.ecommercemaven.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techstore.ecommercemaven.dto.CategorySalesDTO;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory(String category);
    long count();
    long countByStockLessThan(Integer stock);
    long countByStock(int stock);
    List<Product> findByStockLessThan(Integer stock);

    List<Product> findByNameContainingIgnoreCaseAndCategory(
            String keyword,
            String category);
    List<Product> findByStock(int stock);

    @Query("""
SELECT new com.techstore.ecommercemaven.dto.CategorySalesDTO(
       p.category,
       COUNT(p)
)
FROM Product p
GROUP BY p.category
""")
    List<CategorySalesDTO> getCategorySales();

}