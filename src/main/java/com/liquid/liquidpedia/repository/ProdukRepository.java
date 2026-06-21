package com.liquid.liquidpedia.repository;

import com.liquid.liquidpedia.entity.Produk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProdukRepository extends JpaRepository<Produk, Long> {
    List<Produk> findByIsBestSellerTrue();
    List<Produk> findByIsNewArrivalTrue();
    long countByIsBestSellerTrue();
    long countByIsNewArrivalTrue();
    Page<Produk> findByCategoryIdCategory(Long categoryId, Pageable pageable);
    Page<Produk> findAll(Pageable pageable);
    List<Produk> findByCategoryIdCategory(Long categoryId);

    Page<Produk> findByNameProdukContainingIgnoreCase(String name, Pageable pageable);

    Page<Produk> findByNameProdukContainingIgnoreCaseAndCategoryIdCategory(String name, Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Produk p WHERE LOWER(p.nameProduk) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.deskripsi) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Produk> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Produk p WHERE (LOWER(p.nameProduk) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.deskripsi) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND p.category.idCategory = :categoryId")
    Page<Produk> searchByKeywordAndCategory(@Param("keyword") String keyword, @Param("categoryId") Long categoryId, Pageable pageable);
}
