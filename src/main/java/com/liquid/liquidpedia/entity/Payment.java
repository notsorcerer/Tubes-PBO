package com.liquid.liquidpedia.entity;

import com.liquid.liquidpedia.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPayment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private LocalDate tanggal;

    @Column(name = "payment_type", insertable = false, updatable = false)
    private String paymentType;

    public Payment() {}

    @PrePersist
    protected void onCreate() {
        if (tanggal == null) tanggal = LocalDate.now();
        if (status == null) status = PaymentStatus.PENDING;
    }

    public Long getIdPayment() { return idPayment; }
    public void setIdPayment(Long idPayment) { this.idPayment = idPayment; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }
    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
}
