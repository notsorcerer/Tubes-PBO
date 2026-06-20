package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.entity.Cart;
import com.liquid.liquidpedia.entity.Customer;
import com.liquid.liquidpedia.entity.Voucher;
import com.liquid.liquidpedia.repository.CustomerRepository;
import com.liquid.liquidpedia.service.CartService;
import com.liquid.liquidpedia.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/cart")
    public String viewCart(Authentication authentication, Model model) {
        Customer customer = getCustomer(authentication);
        Cart cart = cartService.getCart(customer);
        model.addAttribute("cart", cart);
        return "cart/view";
    }

    @PostMapping("/cart/add/{produkId}")
    public String addToCart(@PathVariable Long produkId,
                            @RequestParam(defaultValue = "1") int qty,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        try {
            Customer customer = getCustomer(authentication);
            cartService.addItem(customer, produkId, qty);
            redirectAttributes.addFlashAttribute("success", "Produk berhasil ditambahkan ke cart!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/update/{cartItemId}")
    public String updateQty(@PathVariable Long cartItemId,
                            @RequestParam int qty,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        try {
            Customer customer = getCustomer(authentication);
            cartService.updateQty(customer, cartItemId, qty);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{cartItemId}")
    public String removeItem(@PathVariable Long cartItemId,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            Customer customer = getCustomer(authentication);
            cartService.removeItem(customer, cartItemId);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/apply-voucher")
    public String applyVoucher(@RequestParam String kodeVoucher,
                               Authentication authentication,
                               Model model) {
        Customer customer = getCustomer(authentication);
        Cart cart = cartService.getCart(customer);
        try {
            Voucher voucher = voucherService.validateVoucher(kodeVoucher, cart.getTotalPrice());
            double discount = voucherService.applyDiscount(voucher, cart.getTotalPrice());
            model.addAttribute("voucher", voucher);
            model.addAttribute("discount", discount);
            model.addAttribute("totalAfterDiscount", cart.getTotalPrice() - discount);
        } catch (RuntimeException e) {
            model.addAttribute("voucherError", e.getMessage());
        }
        model.addAttribute("cart", cart);
        return "cart/view";
    }

    private Customer getCustomer(Authentication authentication) {
        String email = authentication.getName();
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer tidak ditemukan"));
    }
}
