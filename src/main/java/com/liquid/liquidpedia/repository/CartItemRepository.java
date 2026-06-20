package com.liquid.liquidpedia.repository;

import com.liquid.liquidpedia.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdCartAndProdukIdProduk(Long cartId, Long produkId);
}
