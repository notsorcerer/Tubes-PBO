package com.liquid.liquidpedia.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategory;

    @Column(name = "category_name")
    private String categoryName;

    private String deskripsi;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Produk> produks;

    public Category() {}

    public Long getIdCategory() { return idCategory; }
    public void setIdCategory(Long idCategory) { this.idCategory = idCategory; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public List<Produk> getProduks() { return produks; }
    public void setProduks(List<Produk> produks) { this.produks = produks; }
}
