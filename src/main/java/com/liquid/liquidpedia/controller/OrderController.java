package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.entity.Order;
import com.liquid.liquidpedia.entity.User;
import com.liquid.liquidpedia.entity.enums.OrderStatus;
import com.liquid.liquidpedia.repository.UserRepository;
import com.liquid.liquidpedia.service.CartService;
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
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/orders")
    public String orderHistory(Authentication authentication, Model model) {
        User user = getUser(authentication);
        List<Order> orders = orderService.getOrderHistory(user);
        model.addAttribute("orders", orders);
        model.addAttribute("cartItemCount", cartService.getCartItemCount(user));
        return "orders/history";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id,
                              Authentication authentication,
                              Model model) {
        User user = getUser(authentication);
        Order order = orderService.findById(id);

        if (!order.getCustomer().getId().equals(user.getId())) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        model.addAttribute("canCancel", order.getStatus() == OrderStatus.PENDING);
        model.addAttribute("cartItemCount", cartService.getCartItemCount(user));
        return "orders/detail";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            User user = getUser(authentication);
            orderService.cancelOrder(user, id);
            redirectAttributes.addFlashAttribute("success", "Pesanan berhasil dibatalkan");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    private User getUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }
}
