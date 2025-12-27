package com.anla.anggota.controller;

import com.anla.anggota.model.Anggota;
import com.anla.anggota.service.AnggotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anggota")
@RequiredArgsConstructor
public class AnggotaController {
    
    private final AnggotaService anggotaService;
    
    @GetMapping
    public List<Object> getAllAnggota() {
        return anggotaService.getAllAnggota();
    }
    
    @GetMapping("/{id}")
    public Object getAnggotaById(@PathVariable Long id) {
        return anggotaService.getAnggotaById(id);
    }
    
    @PostMapping
    public Anggota createAnggota(@RequestBody Anggota anggota) {
        return anggotaService.createAnggota(anggota);
    }
    
    @PutMapping("/{id}")
    public Anggota updateAnggota(@PathVariable Long id, @RequestBody Anggota anggota) {
        return anggotaService.updateAnggota(id, anggota);
    }
    
    @DeleteMapping("/{id}")
    public void deleteAnggota(@PathVariable Long id) {
        anggotaService.deleteAnggota(id);
    }
}
