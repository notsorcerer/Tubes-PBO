package com.liquid.liquidpedia.repository;

import com.liquid.liquidpedia.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.cart WHERE c.id = :id")
    Optional<Customer> findByIdWithCart(@Param("id") Long id);
}
