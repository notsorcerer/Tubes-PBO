package com.liquid.liquidpedia.repository;

import com.liquid.liquidpedia.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findByKodeVoucher(String kodeVoucher);
}
