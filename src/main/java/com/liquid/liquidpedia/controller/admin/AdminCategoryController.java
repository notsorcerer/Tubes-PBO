package com.liquid.liquidpedia.controller.admin;

import com.liquid.liquidpedia.dto.CategoryDto;
import com.liquid.liquidpedia.entity.Category;
import com.liquid.liquidpedia.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public String list(Model model) {
        List<Category> categories = adminService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/categories";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("categoryDto", new CategoryDto());
        return "admin/category-form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("categoryDto") CategoryDto dto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/category-form";
        }

        try {
            adminService.createCategory(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Kategori berhasil ditambahkan");
            return "redirect:/admin/categories";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/categories/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = adminService.findCategoryById(id);
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getIdCategory());
        dto.setCategoryName(category.getCategoryName());
        dto.setDeskripsi(category.getDeskripsi());

        model.addAttribute("categoryDto", dto);
        return "admin/category-form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("categoryDto") CategoryDto dto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/category-form";
        }

        try {
            adminService.updateCategory(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Kategori berhasil diperbarui");
            return "redirect:/admin/categories";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/categories/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            adminService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "Kategori berhasil dihapus");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/categories";
    }
}
