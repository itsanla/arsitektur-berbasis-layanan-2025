package com.anla.Order.service;

import com.anla.Order.model.Order;
import com.anla.Order.repository.OrderRepository;
import com.anla.Order.VO.Pelanggan;
import com.anla.Order.VO.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    public Order getOrderWithDetails(Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            
            // Fetch pelanggan details
            try {
                Pelanggan pelanggan = restTemplate.getForObject(
                    "http://PELANGGAN-SERVICE/api/pelanggan/" + order.getPelangganId(), 
                    Pelanggan.class
                );
                order.setPelanggan(pelanggan);
            } catch (Exception e) {
                // Handle service unavailable
            }
            
            // Fetch produk details
            try {
                Product produk = restTemplate.getForObject(
                    "http://PRODUK-SERVICE/api/products/" + order.getProdukId(), 
                    Product.class
                );
                order.setProduk(produk);
            } catch (Exception e) {
                // Handle service unavailable
            }
            
            return order;
        }
        return null;
    }
    
    public Order createOrder(Order order) {
        order.setOrderDate(LocalDate.now());
        order.setStatus("PENDING");
        return orderRepository.save(order);
    }
    
    public Order updateOrder(Long id, Order orderDetails) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setQuantity(orderDetails.getQuantity());
            order.setTotalHarga(orderDetails.getTotalHarga());
            order.setStatus(orderDetails.getStatus());
            return orderRepository.save(order);
        }
        return null;
    }
    
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}