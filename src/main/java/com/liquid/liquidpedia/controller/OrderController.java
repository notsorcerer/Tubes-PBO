package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.entity.Customer;
import com.liquid.liquidpedia.entity.Order;
import com.liquid.liquidpedia.entity.enums.OrderStatus;
import com.liquid.liquidpedia.repository.CustomerRepository;
import com.liquid.liquidpedia.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/orders")
    public String orderHistory(Authentication authentication, Model model) {
        Customer customer = getCustomer(authentication);
        List<Order> orders = orderService.getOrderHistory(customer);
        model.addAttribute("orders", orders);
        return "orders/history";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id,
                              Authentication authentication,
                              Model model) {
        Customer customer = getCustomer(authentication);
        Order order = orderService.findById(id);

        if (!order.getCustomer().getId().equals(customer.getId())) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        model.addAttribute("canCancel", order.getStatus() == OrderStatus.PENDING);
        return "orders/detail";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            Customer customer = getCustomer(authentication);
            orderService.cancelOrder(customer, id);
            redirectAttributes.addFlashAttribute("success", "Pesanan berhasil dibatalkan");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    private Customer getCustomer(Authentication authentication) {
        String email = authentication.getName();
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer tidak ditemukan"));
    }
}
