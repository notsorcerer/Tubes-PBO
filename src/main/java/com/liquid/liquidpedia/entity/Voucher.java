package com.liquid.liquidpedia.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vouchers")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVoucher;

    @Column(name = "kode_voucher", unique = true, nullable = false)
    private String kodeVoucher;

    private Double discount;

    @Column(name = "min_purchase")
    private Integer minPurchase;

    @Column(name = "max_discount")
    private Double maxDiscount;

    private Integer stock;

    @Column(name = "expired_at")
    private LocalDate expiredAt;

    public Voucher() {}

    public Long getIdVoucher() { return idVoucher; }
    public void setIdVoucher(Long idVoucher) { this.idVoucher = idVoucher; }
    public String getKodeVoucher() { return kodeVoucher; }
    public void setKodeVoucher(String kodeVoucher) { this.kodeVoucher = kodeVoucher; }
    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }
    public Integer getMinPurchase() { return minPurchase; }
    public void setMinPurchase(Integer minPurchase) { this.minPurchase = minPurchase; }
    public Double getMaxDiscount() { return maxDiscount; }
    public void setMaxDiscount(Double maxDiscount) { this.maxDiscount = maxDiscount; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public LocalDate getExpiredAt() { return expiredAt; }
    public void setExpiredAt(LocalDate expiredAt) { this.expiredAt = expiredAt; }
}
