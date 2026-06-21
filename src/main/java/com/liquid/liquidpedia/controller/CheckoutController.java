package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.dto.CheckoutDto;
import com.liquid.liquidpedia.entity.Address;
import com.liquid.liquidpedia.entity.Cart;
import com.liquid.liquidpedia.entity.User;
import com.liquid.liquidpedia.repository.UserRepository;
import com.liquid.liquidpedia.service.AddressService;
import com.liquid.liquidpedia.service.CartService;
import com.liquid.liquidpedia.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/checkout")
    public String checkoutForm(Authentication authentication,
                               @RequestParam(required = false) String voucherCode,
                               Model model) {
        User user = getUser(authentication);
        Cart cart = cartService.getCart(user);

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }

        List<Address> addresses = addressService.getAddressesByCustomer(user);
        model.addAttribute("cart", cart);
        model.addAttribute("addresses", addresses);
        model.addAttribute("checkoutDto", new CheckoutDto());
        model.addAttribute("voucherCode", voucherCode);
        model.addAttribute("cartItemCount", cartService.getCartItemCount(user));
        return "checkout/form";
    }

    @PostMapping("/checkout")
    public String processCheckout(Authentication authentication,
                                  @Valid @ModelAttribute CheckoutDto checkoutDto,
                                  BindingResult bindingResult,
                                  @RequestParam(required = false) String voucherCode,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/checkout";
        }
        try {
            User user = getUser(authentication);
            orderService.createOrder(user, checkoutDto, voucherCode);
            redirectAttributes.addFlashAttribute("success", "Pesanan berhasil dibuat!");
            return "redirect:/orders";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }

    private User getUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }
}
