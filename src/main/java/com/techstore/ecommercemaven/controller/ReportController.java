package com.techstore.ecommercemaven.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.repository.OrderRepository;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

@Controller
public class ReportController {

    private final OrderRepository orderRepository;

    public ReportController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/admin/reports/pdf")
    public void exportPdf(HttpServletResponse response)
            throws Exception {

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=sales-report.pdf");

        Document document = new Document();

        PdfWriter.getInstance(
                document,
                response.getOutputStream());

        document.open();

        com.itextpdf.text.Font titleFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        18);

        Paragraph title =
                new Paragraph(
                        "Sales Report",
                        titleFont);

        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        document.add(new Paragraph(" "));

        PdfPTable table =
                new PdfPTable(5);

        table.addCell("Order ID");
        table.addCell("Customer");
        table.addCell("Amount");
        table.addCell("Status");
        table.addCell("Date");

        List<Order> orders =
                orderRepository.findAll();

        for (Order order : orders) {

            table.addCell(
                    String.valueOf(order.getId()));

            table.addCell(
                    order.getCustomerName());

            table.addCell(
                    String.valueOf(
                            order.getTotalAmount()));

            table.addCell(
                    order.getStatus());

            table.addCell(
                    String.valueOf(
                            order.getOrderDate()));
        }

        document.add(table);

        document.close();
    }
    @GetMapping("/admin/reports/excel")
    public void exportExcel(HttpServletResponse response)
            throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=sales-report.xlsx");

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet("Sales Report");

        Row header =
                sheet.createRow(0);

        header.createCell(0)
                .setCellValue("Order ID");

        header.createCell(1)
                .setCellValue("Customer");

        header.createCell(2)
                .setCellValue("Amount");

        header.createCell(3)
                .setCellValue("Status");

        header.createCell(4)
                .setCellValue("Date");

        int rowNum = 1;

        for (Order order : orderRepository.findAll()) {

            Row row =
                    sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(order.getId());

            row.createCell(1)
                    .setCellValue(order.getCustomerName());

            row.createCell(2)
                    .setCellValue(order.getTotalAmount());

            row.createCell(3)
                    .setCellValue(order.getStatus());

            row.createCell(4)
                    .setCellValue(
                            order.getOrderDate().toString());
        }

        workbook.write(
                response.getOutputStream());

        workbook.close();
    }
}