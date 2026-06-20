package com.liquid.liquidpedia.dto;

import jakarta.validation.constraints.NotBlank;

public class AddressDto {

    private Long idAddress;

    @NotBlank(message = "Label tidak boleh kosong")
    private String label;

    @NotBlank(message = "Nama penerima tidak boleh kosong")
    private String receiverName;

    @NotBlank(message = "No. telepon tidak boleh kosong")
    private String phoneNum;

    @NotBlank(message = "Alamat tidak boleh kosong")
    private String address;

    @NotBlank(message = "Kota tidak boleh kosong")
    private String city;

    @NotBlank(message = "Provinsi tidak boleh kosong")
    private String province;

    @NotBlank(message = "Kode pos tidak boleh kosong")
    private String posCode;

    private boolean isDefault;

    public AddressDto() {}

    public Long getIdAddress() { return idAddress; }
    public void setIdAddress(Long idAddress) { this.idAddress = idAddress; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getPhoneNum() { return phoneNum; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getPosCode() { return posCode; }
    public void setPosCode(String posCode) { this.posCode = posCode; }
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
}
