package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.dto.ReviewDto;
import com.liquid.liquidpedia.entity.Produk;
import com.liquid.liquidpedia.entity.Review;
import com.liquid.liquidpedia.entity.User;
import com.liquid.liquidpedia.repository.UserRepository;
import com.liquid.liquidpedia.service.CartService;
import com.liquid.liquidpedia.service.ProdukService;
import com.liquid.liquidpedia.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProdukController {

    @Autowired
    private ProdukService produkService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    private boolean isCustomer(Authentication auth) {
        return auth != null && auth.isAuthenticated()
                && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) Long category,
                               @RequestParam(required = false) String search,
                               @RequestParam(defaultValue = "0") int page,
                               Authentication authentication,
                               Model model) {
        Page<Produk> produkPage;
        boolean hasSearch = search != null && !search.trim().isEmpty();

        if (hasSearch && category != null) {
            produkPage = produkService.searchByCategory(search.trim(), category, page, 12);
            model.addAttribute("search", search.trim());
            model.addAttribute("selectedCategory", category);
        } else if (hasSearch) {
            produkPage = produkService.search(search.trim(), page, 12);
            model.addAttribute("search", search.trim());
        } else if (category != null) {
            produkPage = produkService.findByCategory(category, page, 12);
            model.addAttribute("selectedCategory", category);
        } else {
            produkPage = produkService.findAll(page, 12);
        }

        model.addAttribute("produks", produkPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", produkPage.getTotalPages());
        model.addAttribute("categories", produkService.findAllCategories());
        addCartCount(authentication, model);
        return "products/list";
    }

    @GetMapping("/products/{id}")
    public String detailProduct(@PathVariable Long id,
                                Authentication authentication,
                                Model model) {
        Produk produk = produkService.findById(id);
        List<Review> reviews = reviewService.getReviewsByProduk(id);
        Double avgRating = reviewService.getAverageRating(id);

        model.addAttribute("produk", produk);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("reviewDto", new ReviewDto());

        if (isCustomer(authentication)) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                boolean hasPurchased = reviewService.hasPurchased(user.getId(), id);
                model.addAttribute("hasPurchased", hasPurchased);
                boolean alreadyReviewed = reviews.stream()
                        .anyMatch(r -> r.getCustomer().getId().equals(user.getId()));
                model.addAttribute("alreadyReviewed", alreadyReviewed);
            }
        }

        addCartCount(authentication, model);
        return "products/detail";
    }

    @PostMapping("/products/{id}/review")
    public String submitReview(@PathVariable Long id,
                               @Valid @ModelAttribute("reviewDto") ReviewDto dto,
                               BindingResult result,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Rating minimal 1 dan komentar harus diisi");
            return "redirect:/products/" + id;
        }
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
            reviewService.submitReview(user, id, dto);
            redirectAttributes.addFlashAttribute("success", "Review berhasil dikirim!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/products/" + id;
    }

    private void addCartCount(Authentication auth, Model model) {
        if (isCustomer(auth)) {
            User user = userRepository.findByEmail(auth.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("cartItemCount", cartService.getCartItemCount(user));
            }
        }
    }
}
