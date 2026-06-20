package com.liquid.liquidpedia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produk_id")
    private Produk produk;

    private Integer jumlah;

    @Column(name = "sub_total")
    private Double subTotal;

    public OrderItem() {}

    public Long getIdOrderItem() { return idOrderItem; }
    public void setIdOrderItem(Long idOrderItem) { this.idOrderItem = idOrderItem; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Produk getProduk() { return produk; }
    public void setProduk(Produk produk) { this.produk = produk; }
    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }
    public Double getSubTotal() { return subTotal; }
    public void setSubTotal(Double subTotal) { this.subTotal = subTotal; }
}
