package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.entity.Cart;
import com.liquid.liquidpedia.entity.User;
import com.liquid.liquidpedia.entity.Voucher;
import com.liquid.liquidpedia.repository.UserRepository;
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
    private UserRepository userRepository;

    @GetMapping("/cart")
    public String viewCart(Authentication authentication, Model model) {
        User user = getUser(authentication);
        Cart cart = cartService.getCart(user);
        model.addAttribute("cart", cart);
        model.addAttribute("cartItemCount", cartService.getCartItemCount(user));
        return "cart/view";
    }

    @PostMapping("/cart/add/{produkId}")
    public String addToCart(@PathVariable Long produkId,
                            @RequestParam(defaultValue = "1") int qty,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        try {
            User user = getUser(authentication);
            cartService.addItem(user, produkId, qty);
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
            User user = getUser(authentication);
            cartService.updateQty(user, cartItemId, qty);
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
            User user = getUser(authentication);
            cartService.removeItem(user, cartItemId);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/apply-voucher")
    public String applyVoucher(@RequestParam String kodeVoucher,
                               Authentication authentication,
                               Model model) {
        User user = getUser(authentication);
        Cart cart = cartService.getCart(user);
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

    private User getUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }
}
