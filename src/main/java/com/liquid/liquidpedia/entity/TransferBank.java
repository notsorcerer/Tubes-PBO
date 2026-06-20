package com.liquid.liquidpedia.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TRANSFER_BANK")
public class TransferBank extends Payment {

    @Column(name = "rek_number")
    private String rekNumber;

    @Column(name = "bank_name")
    private String bankName;

    public TransferBank() {}

    public String getRekNumber() { return rekNumber; }
    public void setRekNumber(String rekNumber) { this.rekNumber = rekNumber; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
}
