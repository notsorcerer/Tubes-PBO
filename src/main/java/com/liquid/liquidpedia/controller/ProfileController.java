package com.liquid.liquidpedia.controller;

import com.liquid.liquidpedia.dto.AddressDto;
import com.liquid.liquidpedia.entity.Address;
import com.liquid.liquidpedia.entity.User;
import com.liquid.liquidpedia.repository.UserRepository;
import com.liquid.liquidpedia.service.AddressService;
import com.liquid.liquidpedia.service.CartService;
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
    private UserRepository userRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = getUser(authentication);
        List<Address> addresses = addressService.getAddressesByCustomer(user);
        model.addAttribute("customer", user);
        model.addAttribute("addresses", addresses);
        model.addAttribute("addressDto", new AddressDto());
        model.addAttribute("cartItemCount", cartService.getCartItemCount(user));
        return "profile/index";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Authentication authentication,
                                @RequestParam String nameUser,
                                @RequestParam String phone,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = getUser(authentication);
            user.setNameUser(nameUser);
            user.setPhone(phone);
            userRepository.save(user);
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
            User user = getUser(authentication);
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new RuntimeException("Password saat ini salah");
            }
            if (!newPassword.equals(confirmPassword)) {
                throw new RuntimeException("Konfirmasi password tidak cocok");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
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
            User user = getUser(authentication);
            addressService.save(user, dto);
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
            User user = getUser(authentication);
            addressService.update(user, dto);
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
            User user = getUser(authentication);
            addressService.delete(user, id);
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
            User user = getUser(authentication);
            addressService.setDefault(user, id);
            redirectAttributes.addFlashAttribute("success", "Alamat default berhasil diubah");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    private User getUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
    }
}
