package com.liquid.liquidpedia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produk_id")
    private Produk produk;

    private Integer qty;

    @Column(name = "sub_total")
    private Double subTotal;

    public CartItem() {}

    public void calculateSubTotal() {
        if (produk != null && qty != null) {
            this.subTotal = produk.getPrice() * qty;
        }
    }

    public Long getIdCartItem() { return idCartItem; }
    public void setIdCartItem(Long idCartItem) { this.idCartItem = idCartItem; }
    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
    public Produk getProduk() { return produk; }
    public void setProduk(Produk produk) { this.produk = produk; }
    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
    public Double getSubTotal() { return subTotal; }
    public void setSubTotal(Double subTotal) { this.subTotal = subTotal; }
}
