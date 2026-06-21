package com.liquid.liquidpedia.service;

import com.liquid.liquidpedia.dto.RegisterDto;
import com.liquid.liquidpedia.entity.*;
import com.liquid.liquidpedia.entity.enums.Role;
import com.liquid.liquidpedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User register(RegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email sudah terdaftar");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Konfirmasi password tidak cocok");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRoles(Set.of(Role.CUSTOMER));
        user.setNameUser(dto.getName());
        user.setAddresses(new ArrayList<>());
        user.setOrders(new ArrayList<>());
        user.setReviews(new ArrayList<>());

        Cart cart = new Cart();
        cart.setCustomer(user);
        cart.setTotalPrice(0.0);
        cart.setCartItems(new ArrayList<>());
        user.setCart(cart);

        return userRepository.save(user);
    }
}
