package com.liquid.liquidpedia.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "produks")
public class Produk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduk;

    @Column(name = "name_produk")
    private String nameProduk;

    private Double price;

    private Integer stock;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_best_seller")
    private Boolean isBestSeller = false;

    @Column(name = "is_new_arrival")
    private Boolean isNewArrival = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "produk")
    private List<OrderItem> orderItems;

    public Produk() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getIdProduk() { return idProduk; }
    public void setIdProduk(Long idProduk) { this.idProduk = idProduk; }
    public String getNameProduk() { return nameProduk; }
    public void setNameProduk(String nameProduk) { this.nameProduk = nameProduk; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsBestSeller() { return isBestSeller; }
    public void setIsBestSeller(Boolean isBestSeller) { this.isBestSeller = isBestSeller; }
    public Boolean getIsNewArrival() { return isNewArrival; }
    public void setIsNewArrival(Boolean isNewArrival) { this.isNewArrival = isNewArrival; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}
