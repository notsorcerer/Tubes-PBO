package com.liquid.liquidpedia.repository;

import com.liquid.liquidpedia.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerIdOrderByOrderDateDesc(Long customerId);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.idOrder = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);

    @Query("SELECT COUNT(o) > 0 FROM Order o JOIN o.orderItems oi WHERE o.customer.id = :customerId AND oi.produk.idProduk = :produkId AND o.status <> 'CANCELLED'")
    boolean hasCustomerPurchasedProduct(@Param("customerId") Long customerId, @Param("produkId") Long produkId);
}
