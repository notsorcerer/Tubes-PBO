package com.liquid.liquidpedia.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDelivery;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    private String kurir;

    @Column(name = "no_resi")
    private String noResi;

    @Column(name = "status_delivery")
    private String statusDelivery;

    @Column(name = "estimated_time")
    private LocalDateTime estimatedTime;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Delivery() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getIdDelivery() { return idDelivery; }
    public void setIdDelivery(Long idDelivery) { this.idDelivery = idDelivery; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public String getKurir() { return kurir; }
    public void setKurir(String kurir) { this.kurir = kurir; }
    public String getNoResi() { return noResi; }
    public void setNoResi(String noResi) { this.noResi = noResi; }
    public String getStatusDelivery() { return statusDelivery; }
    public void setStatusDelivery(String statusDelivery) { this.statusDelivery = statusDelivery; }
    public LocalDateTime getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(LocalDateTime estimatedTime) { this.estimatedTime = estimatedTime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
