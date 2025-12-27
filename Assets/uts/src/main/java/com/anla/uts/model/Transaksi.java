package com.anla.uts.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transaksi")
public class Transaksi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kdTransaksi;
    
    @Column(nullable = false)
    private String pelanggan;
    
    @Column(nullable = false)
    private Integer meterBulanIni;
    
    @Column(nullable = false)
    private Integer meterBulanLalu;
    
    @Column(nullable = false)
    private Integer pemakaian;
    
    @Column(nullable = false)
    private BigDecimal tarifPerMeter;
    
    @Column(nullable = false)
    private BigDecimal total;
    
    // konstruk kosoang
    public Transaksi() {}
    
    // Constructor dengan parameter
    public Transaksi(String pelanggan, Integer meterBulanIni, Integer meterBulanLalu, BigDecimal tarifPerMeter) {
        this.pelanggan = pelanggan;
        this.meterBulanIni = meterBulanIni;
        this.meterBulanLalu = meterBulanLalu;
        this.tarifPerMeter = tarifPerMeter;
        hitungPemakaianDanTotal();
    }
    
    // logikanya
    public void hitungPemakaianDanTotal() {
        this.pemakaian = this.meterBulanIni - this.meterBulanLalu;
        this.total = this.tarifPerMeter.multiply(new BigDecimal(this.pemakaian));
    }
    
    // Getters dan Setters
    public Long getKdTransaksi() {
        return kdTransaksi;
    }
    
    public void setKdTransaksi(Long kdTransaksi) {
        this.kdTransaksi = kdTransaksi;
    }

    public String getPelanggan() {
        return pelanggan;
    }
    
    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }
    
    public Integer getMeterBulanIni() {
        return meterBulanIni;
    }
    
    public void setMeterBulanIni(Integer meterBulanIni) {
        this.meterBulanIni = meterBulanIni;
    }
    
    public Integer getMeterBulanLalu() {
        return meterBulanLalu;
    }
    
    public void setMeterBulanLalu(Integer meterBulanLalu) {
        this.meterBulanLalu = meterBulanLalu;
    }
    
    public Integer getPemakaian() {
        return pemakaian;
    }
    
    public void setPemakaian(Integer pemakaian) {
        this.pemakaian = pemakaian;
    }
    
    public BigDecimal getTarifPerMeter() {
        return tarifPerMeter;
    }
    
    public void setTarifPerMeter(BigDecimal tarifPerMeter) {
        this.tarifPerMeter = tarifPerMeter;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}