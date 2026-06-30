package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.Product;
import com.techstore.ecommercemaven.repository.ProductRepository;
import com.techstore.ecommercemaven.service.AdminLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final EmailService emailService;
    private final AdminLogService adminLogService;

    public ProductService(ProductRepository productRepository, EmailService emailService, AdminLogService adminLogService) {
        this.productRepository = productRepository;
        this.emailService = emailService;
        this.adminLogService = adminLogService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public long getProductCount() {
        return productRepository.count();
    }
    public List<Product> searchProducts(String keyword) {
        return productRepository
                .findByNameContainingIgnoreCase(keyword);
    }
    public long getTotalProducts() {
        return productRepository.count();
    }
    public List<Product> getProductsByCategory(
            String category) {

        return productRepository.findByCategory(category);
    }
    public List<Product> searchByKeywordAndCategory(
            String keyword,
            String category) {

        return productRepository
                .findByNameContainingIgnoreCaseAndCategory(
                        keyword,
                        category);
    }
    public Product saveProduct(Product product) {

        if(product.getStock() < 5 &&
                !product.isLowStockAlertSent()) {

            emailService.sendLowStockAlert(
                    product.getName(),
                    product.getStock());

            adminLogService.log(
                    "Low stock alert sent for " +
                            product.getName());

            product.setLowStockAlertSent(true);
        }

        if(product.getStock() >= 5) {
            product.setLowStockAlertSent(false);
        }

        return productRepository.save(product);
    }
}