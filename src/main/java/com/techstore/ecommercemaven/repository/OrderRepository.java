package com.techstore.ecommercemaven.repository;

import com.techstore.ecommercemaven.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.techstore.ecommercemaven.dto.MonthlyRevenueDTO;
import com.techstore.ecommercemaven.dto.MonthlyOrdersDTO;
import com.techstore.ecommercemaven.dto.TopCustomerDTO;
import com.techstore.ecommercemaven.dto.CouponRevenueDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserEmail(String userEmail);
    List<Order> findByCustomerNameContainingIgnoreCase(
            String customerName);
    List<Order> findByStatus(String status);

    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    Double getTotalRevenue();
    long count();
    List<Order> findTop5ByOrderByIdDesc();
    @Query("""
SELECT new com.techstore.ecommercemaven.dto.MonthlyRevenueDTO(
MONTH(o.orderDate),
SUM(o.totalAmount)
)
FROM Order o
GROUP BY MONTH(o.orderDate)
ORDER BY MONTH(o.orderDate)
""")
    List<MonthlyRevenueDTO> getMonthlyRevenue();

    @Query("""
    SELECT new com.techstore.ecommercemaven.dto.MonthlyOrdersDTO(
        MONTH(o.orderDate),
        COUNT(o)
    )
    FROM Order o
    GROUP BY MONTH(o.orderDate)
    ORDER BY MONTH(o.orderDate)
""")
    List<MonthlyOrdersDTO> getMonthlyOrders();

    @Query("""
SELECT new com.techstore.ecommercemaven.dto.TopCustomerDTO(
    o.userEmail,
    COUNT(o),
    SUM(o.totalAmount)
)
FROM Order o
WHERE o.userEmail IS NOT NULL
AND o.userEmail <> ''
GROUP BY o.userEmail
ORDER BY SUM(o.totalAmount) DESC
""")
    List<TopCustomerDTO> getTopCustomers();

    @Query("""
SELECT new com.techstore.ecommercemaven.dto.CouponRevenueDTO(
    o.couponCode,
    COUNT(o.id),
    SUM(o.totalAmount)
)
FROM Order o
WHERE o.couponCode IS NOT NULL
GROUP BY o.couponCode
ORDER BY SUM(o.totalAmount) DESC
""")
    List<CouponRevenueDTO> getCouponRevenueAnalytics();

    @Query("""
SELECT o
FROM Order o
WHERE o.orderDate BETWEEN :startDate AND :endDate
""")
    List<Order> findOrdersBetweenDates(
            LocalDateTime startDate,
            LocalDateTime endDate);
}