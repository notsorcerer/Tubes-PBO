package com.liquid.liquidpedia.service;

import com.liquid.liquidpedia.entity.Voucher;
import com.liquid.liquidpedia.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    public Voucher validateVoucher(String kode, Double totalBelanja) {
        Voucher voucher = voucherRepository.findByKodeVoucher(kode.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Voucher tidak ditemukan"));

        if (voucher.getStock() <= 0) {
            throw new RuntimeException("Voucher sudah habis");
        }

        if (voucher.getExpiredAt() != null && voucher.getExpiredAt().isBefore(LocalDate.now())) {
            throw new RuntimeException("Voucher sudah kadaluarsa");
        }

        if (voucher.getMinPurchase() != null && totalBelanja < voucher.getMinPurchase()) {
            throw new RuntimeException("Minimal belanja Rp" + voucher.getMinPurchase() + " untuk menggunakan voucher ini");
        }

        return voucher;
    }

    public Double applyDiscount(Voucher voucher, Double totalBelanja) {
        double discount = totalBelanja * voucher.getDiscount();
        if (voucher.getMaxDiscount() != null && discount > voucher.getMaxDiscount()) {
            discount = voucher.getMaxDiscount();
        }
        return discount;
    }
}
