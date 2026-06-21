package com.liquid.liquidpedia.service;

import com.liquid.liquidpedia.entity.Category;
import com.liquid.liquidpedia.entity.Produk;
import com.liquid.liquidpedia.repository.CategoryRepository;
import com.liquid.liquidpedia.repository.ProdukRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ProdukService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/products/";

    @Autowired
    private ProdukRepository produkRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Produk> findAll() {
        return produkRepository.findAll();
    }

    public Page<Produk> findAll(int page, int size) {
        return produkRepository.findAll(PageRequest.of(page, size));
    }

    public Produk findById(Long id) {
        return produkRepository.findById(id).orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));
    }

    public List<Produk> findBestSellers() {
        return produkRepository.findByIsBestSellerTrue();
    }

    public List<Produk> findNewArrivals() {
        return produkRepository.findByIsNewArrivalTrue();
    }

    public Page<Produk> findByCategory(Long categoryId, int page, int size) {
        return produkRepository.findByCategoryIdCategory(categoryId, PageRequest.of(page, size));
    }

    public List<Produk> findByCategory(Long categoryId) {
        return produkRepository.findByCategoryIdCategory(categoryId);
    }

    public Page<Produk> search(String keyword, int page, int size) {
        return produkRepository.searchByKeyword(keyword, PageRequest.of(page, size));
    }

    public Page<Produk> searchByCategory(String keyword, Long categoryId, int page, int size) {
        return produkRepository.searchByKeywordAndCategory(keyword, categoryId, PageRequest.of(page, size));
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));
    }

    public Produk saveProduk(Produk produk) {
        return produkRepository.save(produk);
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
