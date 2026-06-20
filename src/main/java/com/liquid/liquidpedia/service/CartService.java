package com.liquid.liquidpedia.service;

import com.liquid.liquidpedia.entity.*;
import com.liquid.liquidpedia.repository.CartItemRepository;
import com.liquid.liquidpedia.repository.CartRepository;
import com.liquid.liquidpedia.repository.ProdukRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProdukRepository produkRepository;

    public Cart getCart(Customer customer) {
        return cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setCustomer(customer);
                    cart.setTotalPrice(0.0);
                    cart.setCartItems(new ArrayList<>());
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public Cart addItem(Customer customer, Long produkId, int qty) {
        Cart cart = getCart(customer);
        Produk produk = produkRepository.findById(produkId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        if (produk.getStock() < qty) {
            throw new RuntimeException("Stok tidak mencukupi");
        }

        CartItem existingItem = cartItemRepository
                .findByCartIdCartAndProdukIdProduk(cart.getIdCart(), produkId)
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQty(existingItem.getQty() + qty);
            existingItem.calculateSubTotal();
            cartItemRepository.save(existingItem);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduk(produk);
            item.setQty(qty);
            item.calculateSubTotal();
            if (cart.getCartItems() == null) {
                cart.setCartItems(new ArrayList<>());
            }
            cart.getCartItems().add(item);
        }

        calculateTotal(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(Customer customer, Long cartItemId) {
        Cart cart = getCart(customer);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Item tidak ditemukan"));

        if (!item.getCart().getIdCart().equals(cart.getIdCart())) {
            throw new RuntimeException("Item tidak ditemukan di cart anda");
        }

        cart.getCartItems().remove(item);
        cartItemRepository.delete(item);
        calculateTotal(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateQty(Customer customer, Long cartItemId, int qty) {
        Cart cart = getCart(customer);
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Item tidak ditemukan"));

        if (!item.getCart().getIdCart().equals(cart.getIdCart())) {
            throw new RuntimeException("Item tidak ditemukan di cart anda");
        }

        if (qty <= 0) {
            return removeItem(customer, cartItemId);
        }

        if (item.getProduk().getStock() < qty) {
            throw new RuntimeException("Stok tidak mencukupi");
        }

        item.setQty(qty);
        item.calculateSubTotal();
        cartItemRepository.save(item);
        calculateTotal(cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Customer customer) {
        Cart cart = getCart(customer);
        if (cart.getCartItems() != null) {
            cart.getCartItems().clear();
        }
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }

    public void calculateTotal(Cart cart) {
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            cart.setTotalPrice(0.0);
            return;
        }
        double total = cart.getCartItems().stream()
                .mapToDouble(CartItem::getSubTotal)
                .sum();
        cart.setTotalPrice(total);
    }

    public int getCartItemCount(Customer customer) {
        Cart cart = getCart(customer);
        return cart.getCartItems() == null ? 0 : cart.getCartItems().size();
    }
}
