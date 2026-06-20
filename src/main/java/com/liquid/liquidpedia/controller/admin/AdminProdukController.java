package com.liquid.liquidpedia.controller.admin;

import com.liquid.liquidpedia.dto.ProdukDto;
import com.liquid.liquidpedia.entity.Category;
import com.liquid.liquidpedia.entity.Produk;
import com.liquid.liquidpedia.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProdukController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public String list(Model model) {
        List<Produk> produks = adminService.getAllProduks();
        model.addAttribute("produks", produks);
        return "admin/products";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("produkDto", new ProdukDto());
        model.addAttribute("categories", adminService.getAllCategories());
        return "admin/product-form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("produkDto") ProdukDto dto,
                         BindingResult bindingResult,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", adminService.getAllCategories());
            return "admin/product-form";
        }

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                validateImage(imageFile);
            }
            adminService.createProduk(dto, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil ditambahkan");
            return "redirect:/admin/products";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/products/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Produk produk = adminService.findProdukById(id);
        ProdukDto dto = new ProdukDto();
        dto.setId(produk.getIdProduk());
        dto.setNameProduk(produk.getNameProduk());
        dto.setPrice(produk.getPrice());
        dto.setStock(produk.getStock());
        dto.setDeskripsi(produk.getDeskripsi());
        dto.setCategoryId(produk.getCategory() != null ? produk.getCategory().getIdCategory() : null);
        dto.setIsBestSeller(produk.getIsBestSeller());
        dto.setIsNewArrival(produk.getIsNewArrival());

        model.addAttribute("produkDto", dto);
        model.addAttribute("categories", adminService.getAllCategories());
        model.addAttribute("existingImage", produk.getImageUrl());
        return "admin/product-form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("produkDto") ProdukDto dto,
                         BindingResult bindingResult,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", adminService.getAllCategories());
            Produk existing = adminService.findProdukById(id);
            model.addAttribute("existingImage", existing.getImageUrl());
            return "admin/product-form";
        }

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                validateImage(imageFile);
            }
            adminService.updateProduk(id, dto, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil diperbarui");
            return "redirect:/admin/products";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/products/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            adminService.deleteProduk(id);
            redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil dihapus");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    private void validateImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            throw new RuntimeException("Format gambar harus JPG/JPEG atau PNG");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Ukuran gambar maksimal 5MB");
        }
    }
}
