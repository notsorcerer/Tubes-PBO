package com.liquid.liquidpedia.service;

import com.liquid.liquidpedia.dto.ReviewDto;
import com.liquid.liquidpedia.entity.Produk;
import com.liquid.liquidpedia.entity.Review;
import com.liquid.liquidpedia.entity.User;
import com.liquid.liquidpedia.repository.OrderRepository;
import com.liquid.liquidpedia.repository.ProdukRepository;
import com.liquid.liquidpedia.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProdukRepository produkRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Review> getReviewsByProduk(Long produkId) {
        return reviewRepository.findByProdukIdProdukOrderByCreatedAtDesc(produkId);
    }

    public Double getAverageRating(Long produkId) {
        Double avg = reviewRepository.averageRatingByProdukId(produkId);
        return avg != null ? avg : 0.0;
    }

    public boolean hasPurchased(Long customerId, Long produkId) {
        return orderRepository.hasCustomerPurchasedProduct(customerId, produkId);
    }

    public Review submitReview(User customer, Long produkId, ReviewDto dto) {
        if (!hasPurchased(customer.getId(), produkId)) {
            throw new RuntimeException("Anda harus membeli produk ini terlebih dahulu untuk memberikan review");
        }
        Produk produk = produkRepository.findById(produkId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        Review review = new Review();
        review.setCustomer(customer);
        review.setProduk(produk);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        return reviewRepository.save(review);
    }
}
