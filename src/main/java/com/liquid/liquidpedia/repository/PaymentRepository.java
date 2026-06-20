package com.liquid.liquidpedia.repository;

import com.liquid.liquidpedia.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
