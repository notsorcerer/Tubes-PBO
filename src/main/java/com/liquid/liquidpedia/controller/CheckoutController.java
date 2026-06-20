package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.dto.CheckoutDto;
import com.liquid.liquidpedia.entity.Address;
import com.liquid.liquidpedia.entity.Cart;
import com.liquid.liquidpedia.entity.Customer;
import com.liquid.liquidpedia.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;

    @GetMapping("/checkout")
    public String checkoutForm(Authentication authentication,
                               @RequestParam(required = false) String voucherCode,
                               Model model) {
        Customer customer = getCustomer(authentication);
        Cart cart = cartService.getCart(customer);

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }

        List<Address> addresses = addressService.getAddressesByCustomer(customer);
        model.addAttribute("cart", cart);
        model.addAttribute("addresses", addresses);
        model.addAttribute("checkoutDto", new CheckoutDto());
        model.addAttribute("voucherCode", voucherCode);
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
            Customer customer = getCustomer(authentication);
            orderService.createOrder(customer, checkoutDto, voucherCode);
            redirectAttributes.addFlashAttribute("success", "Pesanan berhasil dibuat!");
            return "redirect:/orders";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }

    private Customer getCustomer(Authentication authentication) {
        String email = authentication.getName();
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer tidak ditemukan"));
    }
}
