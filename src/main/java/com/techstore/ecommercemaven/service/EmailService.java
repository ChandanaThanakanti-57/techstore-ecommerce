package com.techstore.ecommercemaven.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmation(
            String to,
            Long orderId,
            double amount) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);

        message.setSubject(
                "Order Confirmation");

        message.setText(
                "Hello,\n\n" +
                        "Your order #" + orderId +
                        " has been placed successfully.\n\n" +
                        "Total Amount: ₹" + amount +
                        "\n\nThank you for shopping with us.");

        mailSender.send(message);
    }
    public void sendSimpleEmail(
            String to,
            String subject,
            String body) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);

        message.setSubject(subject);

        message.setText(body);

        mailSender.send(message);
    }
    public void sendLowStockAlert(
            String productName,
            int stock) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo("vandanathanakanti123@gmail.com");

        message.setSubject(
                "Low Stock Alert");

        message.setText(
                "Product: " + productName +
                        "\nCurrent Stock: " + stock +
                        "\nPlease restock soon.");

        mailSender.send(message);
    }
}