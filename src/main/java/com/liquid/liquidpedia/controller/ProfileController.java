package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.dto.AddressDto;
import com.liquid.liquidpedia.entity.Address;
import com.liquid.liquidpedia.entity.Customer;
import com.liquid.liquidpedia.repository.CustomerRepository;
import com.liquid.liquidpedia.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        Customer customer = getCustomer(authentication);
        List<Address> addresses = addressService.getAddressesByCustomer(customer);
        model.addAttribute("customer", customer);
        model.addAttribute("addresses", addresses);
        model.addAttribute("addressDto", new AddressDto());
        return "profile/index";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Authentication authentication,
                                @RequestParam String nameUser,
                                @RequestParam String phone,
                                RedirectAttributes redirectAttributes) {
        try {
            Customer customer = getCustomer(authentication);
            customer.setNameUser(nameUser);
            customer.setPhone(phone);
            customerRepository.save(customer);
            redirectAttributes.addFlashAttribute("success", "Profil berhasil diperbarui");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(Authentication authentication,
                                  @RequestParam String currentPassword,
                                  @RequestParam String newPassword,
                                  @RequestParam String confirmPassword,
                                  RedirectAttributes redirectAttributes) {
        try {
            Customer customer = getCustomer(authentication);
            if (!passwordEncoder.matches(currentPassword, customer.getPassword())) {
                throw new RuntimeException("Password saat ini salah");
            }
            if (!newPassword.equals(confirmPassword)) {
                throw new RuntimeException("Konfirmasi password tidak cocok");
            }
            customer.setPassword(passwordEncoder.encode(newPassword));
            customerRepository.save(customer);
            redirectAttributes.addFlashAttribute("success", "Password berhasil diubah");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/address")
    public String addAddress(Authentication authentication,
                              @Valid @ModelAttribute("addressDto") AddressDto dto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .findFirst().orElse("Data alamat tidak valid");
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/profile";
        }
        try {
            Customer customer = getCustomer(authentication);
            addressService.save(customer, dto);
            redirectAttributes.addFlashAttribute("success", "Alamat berhasil ditambahkan");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/address/update")
    public String updateAddress(Authentication authentication,
                                 @Valid @ModelAttribute("addressDto") AddressDto dto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .findFirst().orElse("Data alamat tidak valid");
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/profile";
        }
        try {
            Customer customer = getCustomer(authentication);
            addressService.update(customer, dto);
            redirectAttributes.addFlashAttribute("success", "Alamat berhasil diperbarui");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/address/delete/{id}")
    public String deleteAddress(@PathVariable Long id,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            Customer customer = getCustomer(authentication);
            addressService.delete(customer, id);
            redirectAttributes.addFlashAttribute("success", "Alamat berhasil dihapus");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/address/default/{id}")
    public String setDefaultAddress(@PathVariable Long id,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        try {
            Customer customer = getCustomer(authentication);
            addressService.setDefault(customer, id);
            redirectAttributes.addFlashAttribute("success", "Alamat default berhasil diubah");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    private Customer getCustomer(Authentication authentication) {
        String email = authentication.getName();
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer tidak ditemukan"));
    }
}
