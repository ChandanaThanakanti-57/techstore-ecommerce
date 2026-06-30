package com.techstore.ecommercemaven.controller;
import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.repository.OrderRepository;
import com.techstore.ecommercemaven.repository.ProductRepository;
import com.techstore.ecommercemaven.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.techstore.ecommercemaven.model.Product;
import org.springframework.web.bind.annotation.PathVariable;
import com.techstore.ecommercemaven.repository.OrderItemRepository;
import com.techstore.ecommercemaven.repository.WishlistRepository;
import com.techstore.ecommercemaven.repository.CouponRepository;
import com.techstore.ecommercemaven.service.EmailService;
import com.techstore.ecommercemaven.repository.ReviewRepository;
import com.techstore.ecommercemaven.model.Review;
import com.techstore.ecommercemaven.model.Coupon;
import com.techstore.ecommercemaven.dto.MonthlyRevenueDTO;
import com.techstore.ecommercemaven.service.AdminLogService;
import com.techstore.ecommercemaven.repository.AdminLogRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AdminController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final WishlistRepository wishlistRepository;
    private final CouponRepository couponRepository;
    private final ReviewRepository reviewRepository;
    private final EmailService emailService;
    private final AdminLogService adminLogService;
    private final AdminLogRepository adminLogRepository;


    public AdminController(
            ProductRepository productRepository,
            OrderRepository orderRepository,
            UserRepository userRepository,
            OrderItemRepository orderItemRepository,
            WishlistRepository wishlistRepository,
            CouponRepository couponRepository,
            ReviewRepository reviewRepository,
            EmailService emailService,
            AdminLogService adminLogService,
            AdminLogRepository adminLogRepository) {

        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.wishlistRepository = wishlistRepository;
        this.couponRepository = couponRepository;
        this.reviewRepository = reviewRepository;
        this.emailService = emailService;
        this.adminLogService = adminLogService;
        this.adminLogRepository = adminLogRepository;
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {

        model.addAttribute(
                "totalProducts",
                productRepository.count());

        model.addAttribute(
                "totalOrders",
                orderRepository.count());

        model.addAttribute(
                "totalUsers",
                userRepository.count());

        model.addAttribute(
                "totalRevenue",
                orderRepository.getTotalRevenue());

        model.addAttribute(
                "lowStockProducts",
                productRepository.countByStockLessThan(5));

        model.addAttribute(
                "lowStockList",
                productRepository.findByStockLessThan(5));

        model.addAttribute(
                "recentOrders",
                orderRepository.findTop5ByOrderByIdDesc());
        model.addAttribute(
                "topProducts",
                orderItemRepository.getTopSellingProducts());
        model.addAttribute(
                "monthlyRevenue",
                orderRepository.getMonthlyRevenue());
        model.addAttribute(
                "monthlyOrders",
                orderRepository.getMonthlyOrders());
        model.addAttribute(
                "topCustomers",
                orderRepository.getTopCustomers());
        model.addAttribute(
                "categorySales",
                productRepository.getCategorySales());
        model.addAttribute(
                "wishlistAnalytics",
                wishlistRepository.getMostWishlistedProducts());
        model.addAttribute(
                "couponAnalytics",
                orderRepository.getCouponRevenueAnalytics());
        model.addAttribute(
                "outOfStockCount",
                productRepository.countByStock(0));

        model.addAttribute(
                "outOfStockProducts",
                productRepository.findByStock(0));
        model.addAttribute(
                "pendingReviews",
                reviewRepository.countByApprovedFalse());
        model.addAttribute(
                "customerGrowth",
                userRepository.getCustomerGrowth());
        model.addAttribute(
                "newUsersThisMonth",
                userRepository.getNewUsersThisMonth());
        Double totalRevenue =
                orderRepository.getTotalRevenue();

        double forecastRevenue = 0;

        if (totalRevenue != null) {

            forecastRevenue =
                    totalRevenue * 1.10; // assume 10% growth
        }

        model.addAttribute(
                "forecastRevenue",
                forecastRevenue);
        model.addAttribute(
                "recentLogs",
                adminLogRepository
                        .findTop20ByOrderByCreatedAtDesc());

        return "admin-dashboard";
    }
    @GetMapping("/admin/orders")
    public String adminOrders(
            @RequestParam(required = false)
            String search,

            Model model) {

        if (search != null &&
                !search.isBlank()) {

            model.addAttribute(
                    "orders",

                    orderRepository
                            .findByCustomerNameContainingIgnoreCase(
                                    search));
        }
        else {

            model.addAttribute(
                    "orders",
                    orderRepository.findAll());
        }

        return "admin-orders";
    }
    @GetMapping("/admin/orders/status")
    public String filterOrders(

            @RequestParam String status,

            Model model) {

        model.addAttribute(
                "orders",
                orderRepository.findByStatus(status));

        return "admin-orders";
    }
    @PostMapping("/admin/update-status")
    public String updateStatus(
            @RequestParam Long orderId,
            @RequestParam String status) {

        Order order =
                orderRepository.findById(orderId)
                        .orElse(null);

        if (order != null) {

            order.setStatus(status);

            orderRepository.save(order);

            adminLogService.log(
                    "Order #" +
                            order.getId() +
                            " updated to " +
                            status);

            if ("Shipped".equals(status)
                    && order.getUserEmail() != null
                    && !order.getUserEmail().isEmpty()) {

                emailService.sendSimpleEmail(
                        order.getUserEmail(),
                        "Order Shipped",
                        "Your order #" + order.getId()
                                + " has been shipped."
                );
            }

            if ("Delivered".equals(status)
                    && order.getUserEmail() != null
                    && !order.getUserEmail().isEmpty()) {

                emailService.sendSimpleEmail(
                        order.getUserEmail(),
                        "Order Delivered",
                        "Your order #" + order.getId()
                                + " has been delivered."
                );
            }
        }

        return "redirect:/admin/orders";
    }
    @GetMapping("/admin/products")
    public String adminProducts(Model model) {

        model.addAttribute(
                "products",
                productRepository.findAll());

        return "admin-products";
    }
    @GetMapping("/admin/users")
    public String adminUsers(Model model) {

        model.addAttribute(
                "users",
                userRepository.findAll());

        return "admin-users";
    }
    @GetMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable Integer id) {

        userRepository.deleteById(id);

        adminLogService.log(
                "Deleted user ID " + id);

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/coupons")
    public String adminCoupons(Model model) {

        model.addAttribute(
                "coupons",
                couponRepository.findAll());

        return "admin-coupons";
    }

    @PostMapping("/admin/coupons/add")
    public String addCoupon(
            @RequestParam String code,
            @RequestParam double discountPercent) {

        Coupon coupon = new Coupon();

        coupon.setCode(code);
        coupon.setDiscountPercent(discountPercent);
        coupon.setActive(true);

        couponRepository.save(coupon);

        adminLogService.log(
                "Created coupon: " +
                        coupon.getCode());

        return "redirect:/admin/coupons";
    }

    @GetMapping("/admin/revenue-report")
    public String revenueReport(
            @RequestParam(required = false)
            String startDate,

            @RequestParam(required = false)
            String endDate,

            Model model) {

        if (startDate != null &&
                endDate != null &&
                !startDate.isBlank() &&
                !endDate.isBlank()) {

            LocalDateTime start =
                    LocalDate.parse(startDate)
                            .atStartOfDay();

            LocalDateTime end =
                    LocalDate.parse(endDate)
                            .atTime(23,59,59);

            List<Order> orders =
                    orderRepository.findOrdersBetweenDates(
                            start,
                            end);

            double revenue =
                    orders.stream()
                            .mapToDouble(Order::getTotalAmount)
                            .sum();

            model.addAttribute(
                    "orders",
                    orders);

            model.addAttribute(
                    "revenue",
                    revenue);
        }

        return "revenue-report";
    }

    @GetMapping("/admin/reviews")
    public String adminReviews(Model model) {

        model.addAttribute(
                "reviews",
                reviewRepository.findAll());

        return "admin-reviews";
    }

    @GetMapping("/admin/reviews/approve/{id}")
    public String approveReview(
            @PathVariable Long id) {

        Review review =
                reviewRepository.findById(id)
                        .orElse(null);

        if(review != null){

            review.setApproved(true);

            reviewRepository.save(review);

            adminLogService.log(
                    "Approved review #" +
                            review.getId());
        }

        return "redirect:/admin/reviews";
    }

    @GetMapping("/admin/reviews/delete/{id}")
    public String deleteReview(
            @PathVariable Long id){

        reviewRepository.deleteById(id);

        adminLogService.log(
                "Deleted review #" + id);

        return "redirect:/admin/reviews";
    }

}