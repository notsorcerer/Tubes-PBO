package com.liquid.liquidpedia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String label;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "phone_num")
    private String phoneNum;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String city;

    private String province;

    @Column(name = "pos_code")
    private String posCode;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    public Address() {}

    public Long getIdAddress() { return idAddress; }
    public void setIdAddress(Long idAddress) { this.idAddress = idAddress; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
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
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
}
