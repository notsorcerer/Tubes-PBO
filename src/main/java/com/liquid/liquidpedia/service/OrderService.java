package com.liquid.liquidpedia.service;

import com.liquid.liquidpedia.dto.CheckoutDto;
import com.liquid.liquidpedia.entity.*;
import com.liquid.liquidpedia.entity.enums.OrderStatus;
import com.liquid.liquidpedia.entity.enums.PaymentStatus;
import com.liquid.liquidpedia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private ProdukRepository produkRepository;

    public List<Order> getOrderHistory(User customer) {
        return orderRepository.findByCustomerIdOrderByOrderDateDesc(customer.getId());
    }

    public Order findById(Long id) {
        return orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new RuntimeException("Order tidak ditemukan"));
    }

    @Transactional
    public Order createOrder(User customer, CheckoutDto dto, String voucherCode) {
        Cart cart = cartService.getCart(customer);

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart kosong");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderItems(new ArrayList<>());

        double discountAmount = 0.0;

        if (voucherCode != null && !voucherCode.trim().isEmpty()) {
            try {
                Voucher voucher = voucherService.validateVoucher(voucherCode.trim(), cart.getTotalPrice());
                discountAmount = voucherService.applyDiscount(voucher, cart.getTotalPrice());
                voucher.setStock(voucher.getStock() - 1);
                order.setVoucher(voucher);
            } catch (RuntimeException e) {
                throw new RuntimeException("Voucher: " + e.getMessage());
            }
        }

        for (CartItem cartItem : cart.getCartItems()) {
            Produk produk = cartItem.getProduk();
            if (produk.getStock() < cartItem.getQty()) {
                throw new RuntimeException("Stok " + produk.getNameProduk() + " tidak mencukupi");
            }
            produk.setStock(produk.getStock() - cartItem.getQty());
            produkRepository.save(produk);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduk(produk);
            orderItem.setJumlah(cartItem.getQty());
            orderItem.setSubTotal(cartItem.getSubTotal());
            order.getOrderItems().add(orderItem);
        }

        double finalTotal = cart.getTotalPrice() - discountAmount;
        if (finalTotal < 0) finalTotal = 0.0;
        order.setTotalPrice(finalTotal);

        order = orderRepository.save(order);

        Payment payment = createPayment(dto, order);
        paymentRepository.save(payment);

        if (dto.getAddressId() != null) {
            Address address = addressService.findById(dto.getAddressId());
            Delivery delivery = new Delivery();
            delivery.setOrder(order);
            delivery.setAddress(address);
            delivery.setKurir(dto.getKurir());
            delivery.setStatusDelivery("PENDING");
            deliveryRepository.save(delivery);
        }

        cartService.clearCart(customer);

        return order;
    }

    private Payment createPayment(CheckoutDto dto, Order order) {
        String method = dto.getPaymentMethod();
        if ("TRANSFER_BANK".equals(method)) {
            TransferBank transferBank = new TransferBank();
            transferBank.setOrder(order);
            transferBank.setStatus(PaymentStatus.PENDING);
            transferBank.setRekNumber(dto.getRekNumber());
            transferBank.setBankName(dto.getBankName());
            return transferBank;
        } else if ("QRIS".equals(method)) {
            Qris qris = new Qris();
            qris.setOrder(order);
            qris.setStatus(PaymentStatus.PENDING);
            qris.setKodeQR(dto.getKodeQR());
            return qris;
        } else if ("COD".equals(method)) {
            Cod cod = new Cod();
            cod.setOrder(order);
            cod.setStatus(PaymentStatus.PENDING);
            cod.setReceiverName(dto.getReceiverName());
            cod.setShippingAddress(dto.getShippingAddress());
            return cod;
        } else {
            throw new RuntimeException("Metode pembayaran tidak valid");
        }
    }

    @Transactional
    public void cancelOrder(User customer, Long orderId) {
        Order order = findById(orderId);
        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Order tidak ditemukan");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order tidak dapat dibatalkan");
        }
        order.setStatus(OrderStatus.CANCELLED);
        for (OrderItem item : order.getOrderItems()) {
            Produk produk = item.getProduk();
            produk.setStock(produk.getStock() + item.getJumlah());
            produkRepository.save(produk);
        }
        orderRepository.save(order);
    }
}
