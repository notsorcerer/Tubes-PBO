package com.liquid.liquidpedia.service;

import com.liquid.liquidpedia.dto.RegisterDto;
import com.liquid.liquidpedia.entity.*;
import com.liquid.liquidpedia.entity.enums.Role;
import com.liquid.liquidpedia.repository.CustomerRepository;
import com.liquid.liquidpedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Customer register(RegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email sudah terdaftar");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Konfirmasi password tidak cocok");
        }

        Customer customer = new Customer();
        customer.setEmail(dto.getEmail());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        customer.setPhone(dto.getPhone());
        customer.setRole(Role.CUSTOMER);
        customer.setNameUser(dto.getName());
        customer.setAddresses(new ArrayList<>());
        customer.setOrders(new ArrayList<>());
        customer.setReviews(new ArrayList<>());

        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setTotalPrice(0.0);
        cart.setCartItems(new ArrayList<>());
        customer.setCart(cart);

        return customerRepository.save(customer);
    }
}
