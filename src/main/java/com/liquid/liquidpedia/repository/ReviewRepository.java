package com.liquid.liquidpedia.repository;

import com.liquid.liquidpedia.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProdukIdProdukOrderByCreatedAtDesc(Long produkId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.produk.idProduk = :produkId")
    Double averageRatingByProdukId(@Param("produkId") Long produkId);

    boolean existsByCustomerIdAndProdukIdProduk(Long customerId, Long produkId);
}
