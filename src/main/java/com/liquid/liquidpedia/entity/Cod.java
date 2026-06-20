package com.liquid.liquidpedia.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("COD")
public class Cod extends Payment {

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    public Cod() {}

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}
