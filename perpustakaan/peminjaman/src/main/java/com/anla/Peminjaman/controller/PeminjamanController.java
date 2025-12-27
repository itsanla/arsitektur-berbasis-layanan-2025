package com.anla.Peminjaman.controller;

import com.anla.Peminjaman.VO.ResponseTemplateVO;
import com.anla.Peminjaman.dto.PeminjamanDto;
import com.anla.Peminjaman.model.Peminjaman;
import com.anla.Peminjaman.service.PeminjamanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/peminjaman")
@RequiredArgsConstructor
public class PeminjamanController {

    private final PeminjamanService peminjamanService;

    @GetMapping
    public List<Object> getAllPeminjaman() {
        return peminjamanService.getAllPeminjaman();
    }

    @GetMapping("/{id}")
    public Object getPeminjamanById(@PathVariable Long id) {
        return peminjamanService.getPeminjamanById(id);
    }

    @GetMapping("/denda/{id}")
    public ResponseEntity<PeminjamanDto> getPeminjamanWithDenda(@PathVariable Long id) {
        PeminjamanDto peminjamanDto = peminjamanService.getPeminjamanWithDenda(id);
        return peminjamanDto != null ? ResponseEntity.ok(peminjamanDto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Peminjaman createPeminjaman(@RequestBody Peminjaman peminjaman) {
        return peminjamanService.createPeminjaman(peminjaman);
    }

    @PutMapping("/{id}")
    public Peminjaman updatePeminjaman(@PathVariable Long id, @RequestBody Peminjaman peminjaman) {
        return peminjamanService.updatePeminjaman(id, peminjaman);
    }

    @DeleteMapping("/{id}")
    public void deletePeminjaman(@PathVariable Long id) {
        peminjamanService.deletePeminjaman(id);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseTemplateVO> getPeminjamanWithDetailById(@PathVariable Long id) {
        List<ResponseTemplateVO> results = peminjamanService.getPeminjamanWithDetailById(id);
        ResponseEntity<ResponseTemplateVO> response;
        if (results.isEmpty()) {
            response = ResponseEntity.notFound().build();
        } else {
            response = ResponseEntity.ok(results.get(0));
        }
        return response;
    }
}
