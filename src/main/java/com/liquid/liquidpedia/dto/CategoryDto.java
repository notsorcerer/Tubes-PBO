package com.liquid.liquidpedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDto {

    private Long id;

    @NotBlank(message = "Nama kategori wajib diisi")
    @Size(min = 2, message = "Nama kategori minimal 2 karakter")
    private String categoryName;

    private String deskripsi;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
}
