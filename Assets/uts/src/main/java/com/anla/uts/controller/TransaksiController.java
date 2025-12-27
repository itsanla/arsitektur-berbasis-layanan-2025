package com.anla.uts.controller;

import com.anla.uts.model.Transaksi;
import com.anla.uts.service.TransaksiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaksi")
@CrossOrigin(origins = "*")
public class TransaksiController {
    
    @Autowired
    private TransaksiService transaksiService;
    
    // CREATE - Tambah transaksi baru
    @PostMapping
    public ResponseEntity<Transaksi> createTransaksi(@RequestBody Transaksi transaksi) {
        Transaksi savedTransaksi = transaksiService.tambahTransaksi(transaksi);
        return new ResponseEntity<>(savedTransaksi, HttpStatus.CREATED);
    }
    
    // READ - Ambil semua transaksi
    @GetMapping
    public ResponseEntity<List<Transaksi>> getAllTransaksi() {
        List<Transaksi> transaksiList = transaksiService.getAllTransaksi();
        return new ResponseEntity<>(transaksiList, HttpStatus.OK);
    }
    
    // READ - Ambil transaksi berdasarkan ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaksi> getTransaksiById(@PathVariable Long id) {
        return transaksiService.getTransaksiById(id)
            .map(transaksi -> new ResponseEntity<>(transaksi, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // UPDATE - Update transaksi
    @PutMapping("/{id}")
    public ResponseEntity<Transaksi> updateTransaksi(@PathVariable Long id, @RequestBody Transaksi transaksi) {
        try {
            Transaksi updatedTransaksi = transaksiService.updateTransaksi(id, transaksi);
            return new ResponseEntity<>(updatedTransaksi, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // DELETE - Hapus transaksi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaksi(@PathVariable Long id) {
        transaksiService.deleteTransaksi(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}