# Tech Store E-Commerce Application

## Project Overview

A full-stack e-commerce web application built using Spring Boot, Thymeleaf, MySQL, and Bootstrap. The system allows customers to browse products, manage carts and wishlists, place orders, apply coupons, and track purchases. Administrators can manage products, users, orders, reviews, coupons, analytics, and reports through an admin dashboard.

## Features
Customer Features
User Registration & Login
Product Browsing
Product Search & Filtering
Shopping Cart
Wishlist
Checkout & Order Placement
Coupon Discounts
Order Confirmation Emails
Product Reviews & Ratings

Admin Features
Dashboard Analytics
Product Management
User Management
Order Management
Review Moderation
Coupon Management
Revenue Reports
PDF Export
Excel Export
Activity Logs
Low Stock Alerts
Email Notifications

## Tech Stack
Backend
Java 21
Spring Boot
Spring Data JPA
Spring Mail
Frontend
Thymeleaf
Bootstrap 5
Chart.js
Database
MySQL
Tools
Maven
Git
GitHub
IntelliJ IDEA

## Screenshots
...

## Installation Steps

### Clone Repository

```bash
git clone https://github.com/ChandanaThanakanti-57/techstore-ecommerce.git
```

### Configure Database

Create a MySQL database:

```sql
CREATE DATABASE ecommerce;
```

Update the `application.properties` file with your MySQL database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Run Application

Using Maven:

```bash
mvn spring-boot:run
```

The application will be available at:

http://localhost:8080

## Future Enhancements

* Razorpay Payment Gateway Integration
* Real-Time Order Tracking
* Notification Center
* Sales Forecasting
* Advanced Product Analytics
* Mobile App Integration
* AI-Based Product Recommendations
* Inventory Forecasting
* Multi-Vendor Marketplace Support
* Customer Support Chat System

