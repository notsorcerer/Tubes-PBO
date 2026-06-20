package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.dto.ReviewDto;
import com.liquid.liquidpedia.entity.Customer;
import com.liquid.liquidpedia.entity.Produk;
import com.liquid.liquidpedia.entity.Review;
import com.liquid.liquidpedia.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;

    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) Long category,
                               @RequestParam(defaultValue = "0") int page,
                               Model model) {
        Page<Produk> produkPage;
        if (category != null) {
            produkPage = produkService.findByCategory(category, page, 12);
            model.addAttribute("selectedCategory", category);
        } else {
            produkPage = produkService.findAll(page, 12);
        }
        model.addAttribute("produks", produkPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", produkPage.getTotalPages());
        model.addAttribute("categories", produkService.findAllCategories());
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

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            String email = authentication.getName();
            Customer customer = customerRepository.findByEmail(email).orElse(null);
            if (customer != null) {
                boolean hasPurchased = reviewService.hasPurchased(customer.getId(), id);
                model.addAttribute("hasPurchased", hasPurchased);
                boolean alreadyReviewed = reviews.stream()
                        .anyMatch(r -> r.getCustomer().getId().equals(customer.getId()));
                model.addAttribute("alreadyReviewed", alreadyReviewed);
            }
        }

        return "products/detail";
    }

    @PostMapping("/products/{id}/review")
    public String submitReview(@PathVariable Long id,
                               @Valid @ModelAttribute("reviewDto") ReviewDto dto,
                               BindingResult result,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "redirect:/products/" + id;
        }
        try {
            String email = authentication.getName();
            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer tidak ditemukan"));
            reviewService.submitReview(customer, id, dto);
            redirectAttributes.addFlashAttribute("success", "Review berhasil dikirim!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/products/" + id;
    }
}
