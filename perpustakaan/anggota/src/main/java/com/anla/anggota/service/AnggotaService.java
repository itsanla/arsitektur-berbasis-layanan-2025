package com.anla.anggota.service;

import com.anla.anggota.model.Anggota;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class AnggotaService {
    
    private final CqrsClientService cqrsClient;
    private final AtomicLong idCounter = new AtomicLong(1);
    
    public Anggota createAnggota(Anggota anggota) {
        anggota.setId(idCounter.getAndIncrement());
        cqrsClient.save(anggota, anggota.getId().toString());
        return anggota;
    }
    
    public Anggota updateAnggota(Long id, Anggota anggota) {
        anggota.setId(id);
        cqrsClient.update(anggota, id.toString());
        return anggota;
    }
    
    public void deleteAnggota(Long id) {
        cqrsClient.delete(id.toString());
    }
    
    public Object getAnggotaById(Long id) {
        return cqrsClient.findById(id.toString());
    }
    
    public List<Object> getAllAnggota() {
        return cqrsClient.findAll();
    }
}