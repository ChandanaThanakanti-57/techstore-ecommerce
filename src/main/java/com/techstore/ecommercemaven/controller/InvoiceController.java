package com.techstore.ecommercemaven.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.model.OrderItem;

import com.techstore.ecommercemaven.repository.OrderRepository;
import com.techstore.ecommercemaven.repository.OrderItemRepository;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class InvoiceController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public InvoiceController(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping("/invoice/{orderId}")
    public void downloadInvoice(
            @PathVariable Long orderId,
            HttpServletResponse response)
            throws Exception {

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=invoice-" + orderId + ".pdf");

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow();

        List<OrderItem> items =
                orderItemRepository.findByOrderId(orderId);

        Document document = new Document();

        PdfWriter.getInstance(
                document,
                response.getOutputStream());

        document.open();

        document.add(new Paragraph("TECH STORE"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Customer Invoice"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Order ID : " + order.getId()));
        document.add(new Paragraph("Customer : " + order.getCustomerName()));
        document.add(new Paragraph("Phone : " + order.getPhoneNumber()));
        document.add(new Paragraph("Email : " + order.getUserEmail()));
        document.add(new Paragraph("Address : " + order.getAddress()));
        document.add(new Paragraph("Status : " + order.getStatus()));
        document.add(new Paragraph("Order Date : " + order.getOrderDate()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);

        table.addCell("Product");
        table.addCell("Price");
        table.addCell("Quantity");
        table.addCell("Total");

        for (OrderItem item : items) {

            table.addCell(item.getProductName());

            table.addCell("₹" + item.getPrice());

            table.addCell(String.valueOf(item.getQuantity()));

            table.addCell(
                    "₹" + (item.getPrice() * item.getQuantity()));
        }

        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(
                "Grand Total : ₹" + order.getTotalAmount()));

        document.close();
    }
}