package com.techstore.ecommercemaven.controller;

import com.techstore.ecommercemaven.model.ReturnRequest;
import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.service.ReturnRequestService;
import com.techstore.ecommercemaven.service.OrderService;
import com.techstore.ecommercemaven.model.Refund;
import com.techstore.ecommercemaven.service.RefundService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminReturnController {

    private final ReturnRequestService returnRequestService;
    private final OrderService orderService;
    private final RefundService refundService;

    public AdminReturnController(OrderService orderService,
                                 ReturnRequestService returnRequestService,
                                 RefundService refundService) {

        this.orderService = orderService;
        this.returnRequestService = returnRequestService;
        this.refundService = refundService;
    }


    @GetMapping("/admin/returns")
    public String viewReturns(Model model) {

        model.addAttribute(
                "returns",
                returnRequestService.getAllRequests());

        return "admin/return-requests";
    }


    @GetMapping("/admin/returns/approve/{id}")
    public String approveReturn(
            @PathVariable Long id) {


        ReturnRequest request =
                returnRequestService.getById(id);


        if (request != null) {

            Order order =
                    request.getOrder();

            orderService.restoreStock(order);

            order.setStatus("Returned");

            orderService.saveOrder(order);

            Refund refund = new Refund();

            refund.setOrderId(order.getId());
            refund.setCustomerName(order.getCustomerName());
            refund.setUserEmail(order.getUserEmail());
            refund.setRefundAmount(order.getTotalAmount());
            refund.setRefundMethod("Pending");
            refund.setComments("Refund created after return approval");

            refundService.createRefund(refund);

            returnRequestService.updateStatus(
                    id,
                    "APPROVED");
        }


        return "redirect:/admin/returns";
    }


    @GetMapping("/admin/returns/reject/{id}")
    public String rejectReturn(
            @PathVariable Long id) {

        returnRequestService.updateStatus(
                id,
                "REJECTED");

        return "redirect:/admin/returns";
    }

}