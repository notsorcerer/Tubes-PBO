package com.liquid.liquidpedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CheckoutDto {

    @NotNull(message = "Pilih alamat pengiriman")
    private Long addressId;

    @NotBlank(message = "Pilih metode pembayaran")
    private String paymentMethod;

    private String rekNumber;
    private String bankName;
    private String kodeQR;
    private String receiverName;
    private String shippingAddress;

    @NotBlank(message = "Pilih kurir pengiriman")
    private String kurir;

    public CheckoutDto() {}

    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getRekNumber() { return rekNumber; }
    public void setRekNumber(String rekNumber) { this.rekNumber = rekNumber; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getKodeQR() { return kodeQR; }
    public void setKodeQR(String kodeQR) { this.kodeQR = kodeQR; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public String getKurir() { return kurir; }
    public void setKurir(String kurir) { this.kurir = kurir; }
}
