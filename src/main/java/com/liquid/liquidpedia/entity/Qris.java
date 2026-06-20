package com.liquid.liquidpedia.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("QRIS")
public class Qris extends Payment {

    @Column(name = "kode_qr")
    private String kodeQR;

    public Qris() {}

    public String getKodeQR() { return kodeQR; }
    public void setKodeQR(String kodeQR) { this.kodeQR = kodeQR; }
}
