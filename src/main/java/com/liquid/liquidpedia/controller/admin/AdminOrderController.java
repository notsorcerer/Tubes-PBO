package com.liquid.liquidpedia.controller.admin;

import com.liquid.liquidpedia.entity.Order;
import com.liquid.liquidpedia.entity.enums.OrderStatus;
import com.liquid.liquidpedia.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String list(Model model) {
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "admin/orders";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam OrderStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order tidak ditemukan"));
            order.setStatus(status);
            orderRepository.save(order);
            redirectAttributes.addFlashAttribute("successMessage", "Status pesanan berhasil diperbarui");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/orders";
    }
}
