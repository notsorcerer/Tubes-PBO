package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.entity.User;
import com.liquid.liquidpedia.repository.UserRepository;
import com.liquid.liquidpedia.service.CartService;
import com.liquid.liquidpedia.service.ProdukService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProdukService produkService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        model.addAttribute("bestSellers", produkService.findBestSellers());
        model.addAttribute("newArrivals", produkService.findNewArrivals());
        model.addAttribute("categories", produkService.findAllCategories());
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                model.addAttribute("cartItemCount", cartService.getCartItemCount(user));
            }
        }
        return "index";
    }
}
