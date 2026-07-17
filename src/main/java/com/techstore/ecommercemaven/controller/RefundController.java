package com.techstore.ecommercemaven.service;

import com.techstore.ecommercemaven.model.Refund;
import com.techstore.ecommercemaven.repository.RefundRepository;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.techstore.ecommercemaven.model.Order;
import com.techstore.ecommercemaven.service.OrderService;

@Controller
@RequestMapping("/admin")
public class RefundController {

    private final RefundService refundService;
    private final OrderService orderService;

    public RefundController(RefundService refundService,
                            OrderService orderService) {
        this.refundService = refundService;
        this.orderService = orderService;
    }



    // Mark Refund Completed
    @GetMapping("/refund-complete/{id}")
    public String completeRefund(
            @PathVariable Long id
    ) {

        refundService.completeRefund(id);

        return "redirect:/admin/refunds";
    }
    @GetMapping("/refund/create/{id}")
    public String createRefund(@PathVariable Long id) {

        Order order = orderService.getOrderById(id);

        if (order != null) {

            Refund refund = new Refund();

            refund.setOrderId(order.getId());
            refund.setCustomerName(order.getCustomerName());
            refund.setUserEmail(order.getUserEmail());
            refund.setRefundAmount(order.getTotalAmount());
            refund.setRefundMethod("Pending");
            refund.setComments("Refund created for cancelled order");

            refundService.createRefund(refund);
        }

        return "redirect:/admin/refunds";
    }

}