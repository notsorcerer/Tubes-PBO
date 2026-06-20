package com.liquid.liquidpedia.repository;

import com.liquid.liquidpedia.entity.Produk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdukRepository extends JpaRepository<Produk, Long> {
    List<Produk> findByIsBestSellerTrue();
    List<Produk> findByIsNewArrivalTrue();
    long countByIsBestSellerTrue();
    long countByIsNewArrivalTrue();
    Page<Produk> findByCategoryIdCategory(Long categoryId, Pageable pageable);
    Page<Produk> findAll(Pageable pageable);
    List<Produk> findByCategoryIdCategory(Long categoryId);
}
