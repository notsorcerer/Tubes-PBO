package com.liquid.liquidpedia.service;

import com.liquid.liquidpedia.dto.CategoryDto;
import com.liquid.liquidpedia.dto.DashboardStats;
import com.liquid.liquidpedia.dto.DashboardStats.ProdukPerCategory;
import com.liquid.liquidpedia.dto.ProdukDto;
import com.liquid.liquidpedia.entity.Category;
import com.liquid.liquidpedia.entity.Produk;
import com.liquid.liquidpedia.repository.CategoryRepository;
import com.liquid.liquidpedia.repository.ProdukRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/products/";

    @Autowired
    private ProdukRepository produkRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();
        stats.setTotalProduk(produkRepository.count());
        stats.setTotalKategori(categoryRepository.count());
        stats.setTotalBestSeller(produkRepository.countByIsBestSellerTrue());
        stats.setTotalNewArrival(produkRepository.countByIsNewArrivalTrue());

        List<ProdukPerCategory> perCategory = categoryRepository.findAll()
            .stream()
            .map(cat -> new ProdukPerCategory(cat.getCategoryName(),
                cat.getProduks() != null ? cat.getProduks().size() : 0))
            .collect(Collectors.toList());
        stats.setProdukPerCategory(perCategory);

        return stats;
    }

    public List<Produk> getAllProduks() {
        return produkRepository.findAll();
    }

    @Transactional
    public Produk createProduk(ProdukDto dto, MultipartFile image) {
        Produk produk = new Produk();
        produk.setNameProduk(dto.getNameProduk());
        produk.setPrice(dto.getPrice());
        produk.setStock(dto.getStock());
        produk.setDeskripsi(dto.getDeskripsi());
        produk.setIsBestSeller(dto.getIsBestSeller() != null && dto.getIsBestSeller());
        produk.setIsNewArrival(dto.getIsNewArrival() != null && dto.getIsNewArrival());

        Category category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));
        produk.setCategory(category);

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            produk.setImageUrl(imageUrl);
        }

        return produkRepository.save(produk);
    }

    @Transactional
    public Produk updateProduk(Long id, ProdukDto dto, MultipartFile image) {
        Produk produk = produkRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        produk.setNameProduk(dto.getNameProduk());
        produk.setPrice(dto.getPrice());
        produk.setStock(dto.getStock());
        produk.setDeskripsi(dto.getDeskripsi());
        produk.setIsBestSeller(dto.getIsBestSeller() != null && dto.getIsBestSeller());
        produk.setIsNewArrival(dto.getIsNewArrival() != null && dto.getIsNewArrival());

        Category category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));
        produk.setCategory(category);

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            produk.setImageUrl(imageUrl);
        }

        return produkRepository.save(produk);
    }

    @Transactional
    public void deleteProduk(Long id) {
        Produk produk = produkRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        if (produk.getOrderItems() != null && !produk.getOrderItems().isEmpty()) {
            throw new RuntimeException("Produk tidak dapat dihapus karena masih memiliki pesanan aktif");
        }

        produkRepository.delete(produk);
    }

    public Produk findProdukById(Long id) {
        return produkRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));
    }

    @Transactional
    public Category createCategory(CategoryDto dto) {
        boolean exists = categoryRepository.findAll().stream()
            .anyMatch(c -> c.getCategoryName().equalsIgnoreCase(dto.getCategoryName()));
        if (exists) {
            throw new RuntimeException("Kategori dengan nama tersebut sudah ada");
        }

        Category category = new Category();
        category.setCategoryName(dto.getCategoryName());
        category.setDeskripsi(dto.getDeskripsi());
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));

        boolean exists = categoryRepository.findAll().stream()
            .anyMatch(c -> c.getCategoryName().equalsIgnoreCase(dto.getCategoryName())
                && !c.getIdCategory().equals(id));
        if (exists) {
            throw new RuntimeException("Kategori dengan nama tersebut sudah ada");
        }

        category.setCategoryName(dto.getCategoryName());
        category.setDeskripsi(dto.getDeskripsi());
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));

        if (category.getProduks() != null && !category.getProduks().isEmpty()) {
            throw new RuntimeException("Kategori tidak dapat dihapus karena masih memiliki produk");
        }

        categoryRepository.delete(category);
    }

    public String saveImage(MultipartFile file) {
        if (file.isEmpty()) return null;
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/products/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Gagal upload gambar: " + e.getMessage());
        }
    }
}
