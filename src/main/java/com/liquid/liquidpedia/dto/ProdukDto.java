package com.liquid.liquidpedia.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProdukDto {

    private Long id;

    @NotBlank(message = "Nama produk wajib diisi")
    @Size(min = 3, message = "Nama produk minimal 3 karakter")
    private String nameProduk;

    @NotNull(message = "Harga wajib diisi")
    @Min(value = 1000, message = "Harga minimal Rp1.000")
    private Double price;

    @NotNull(message = "Stok wajib diisi")
    @Min(value = 0, message = "Stok tidak boleh negatif")
    private Integer stock;

    @NotBlank(message = "Deskripsi wajib diisi")
    @Size(min = 10, message = "Deskripsi minimal 10 karakter")
    private String deskripsi;

    @NotNull(message = "Kategori wajib diisi")
    private Long categoryId;

    private Boolean isBestSeller = false;
    private Boolean isNewArrival = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNameProduk() { return nameProduk; }
    public void setNameProduk(String nameProduk) { this.nameProduk = nameProduk; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Boolean getIsBestSeller() { return isBestSeller; }
    public void setIsBestSeller(Boolean isBestSeller) { this.isBestSeller = isBestSeller; }
    public Boolean getIsNewArrival() { return isNewArrival; }
    public void setIsNewArrival(Boolean isNewArrival) { this.isNewArrival = isNewArrival; }
}
