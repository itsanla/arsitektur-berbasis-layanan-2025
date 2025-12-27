package com.anla.Order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long pelangganId;
    private Long produkId;
    private Integer quantity;
    private Double totalHarga;
    private String status;
    private LocalDate orderDate;
    
    @Transient
    private Object pelanggan;
    
    @Transient
    private Object produk;
}