package com.anla.Peminjaman.service;

import com.anla.Peminjaman.dto.PeminjamanDto;
import com.anla.Peminjaman.dto.PeminjamanMessage;
import com.anla.Peminjaman.model.Peminjaman;
import com.anla.Peminjaman.VO.Anggota;
import com.anla.Peminjaman.VO.Buku;
import com.anla.Peminjaman.VO.ResponseTemplateVO;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import com.anla.Peminjaman.VO.Pengembalian;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class PeminjamanService {

    private final CqrsClientService cqrsClient;
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;
    private final PeminjamanProducerService peminjamanProducerService;
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<Object> getAllPeminjaman() {
        return cqrsClient.findAll();
    }

    public Object getPeminjamanById(Long id) {
        return cqrsClient.findById(id.toString());
    }

    public Peminjaman createPeminjaman(Peminjaman peminjaman) {
        peminjaman.setId(idCounter.getAndIncrement());
        cqrsClient.save(peminjaman, peminjaman.getId().toString());
        
        PeminjamanMessage message = new PeminjamanMessage(
            peminjaman.getId(),
            peminjaman.getAnggotaId(),
            peminjaman.getBukuId()
        );
        peminjamanProducerService.sendPeminjamanNotification(message);
        return peminjaman;
    }

    public Peminjaman updatePeminjaman(Long id, Peminjaman peminjaman) {
        peminjaman.setId(id);
        cqrsClient.update(peminjaman, id.toString());
        return peminjaman;
    }

    public void deletePeminjaman(Long id) {
        cqrsClient.delete(id.toString());
    }

    public List<ResponseTemplateVO> getPeminjamanWithDetailById(Long id) {
        List<ResponseTemplateVO> responseList = new ArrayList<>();
        Object peminjamanObj = getPeminjamanById(id);
        if (peminjamanObj != null) {
            Peminjaman peminjaman = convertToPeminjaman(peminjamanObj);
            
            Buku buku = restTemplate.getForObject("http://BUKU-SERVICE/api/buku/" 
                    + peminjaman.getBukuId(), Buku.class);

            Anggota anggota = restTemplate.getForObject("http://ANGGOTA-SERVICE/api/anggota/" 
                    + peminjaman.getAnggotaId(), Anggota.class);

            Pengembalian pengembalian = restTemplate.getForObject("http://PENGEMBALIAN-SERVICE/api/pengembalian/" 
                    + id, Pengembalian.class);

            processPengembalian(peminjaman, pengembalian);

            ResponseTemplateVO vo = new ResponseTemplateVO();
            vo.setPeminjaman(peminjaman);
            vo.setBuku(buku);
            vo.setAnggota(anggota);
            vo.setPengembalian(pengembalian);
            
            responseList.add(vo);
        }
        return responseList;
    }

    private Peminjaman convertToPeminjaman(Object obj) {
        if (obj instanceof Peminjaman) {
            return (Peminjaman) obj;
        }
        return new Peminjaman();
    }

    private void processPengembalian(Peminjaman peminjaman, Pengembalian pengembalian) {
        if (pengembalian == null) {
            return;
        }
        java.time.LocalDate tanggalDikembalikan = pengembalian.getTanggalDikembalikan();
        java.time.LocalDate tanggalBatas = peminjaman.getTanggal_batas();
        if (tanggalDikembalikan == null || tanggalBatas == null) {
            return;
        }
        peminjaman.setTanggalDikembalikan(tanggalDikembalikan);
        long daysDiff = ChronoUnit.DAYS.between(tanggalBatas, tanggalDikembalikan);
        if (daysDiff > 0) {
            pengembalian.setTerlambat((int) daysDiff);
            pengembalian.setDenda(daysDiff * 1000.0);
        } else {
            pengembalian.setTerlambat(0);
            pengembalian.setDenda(0.0);
        }
    }

    public PeminjamanDto getPeminjamanWithDenda(Long id) {
        Object peminjamanObj = getPeminjamanById(id);
        PeminjamanDto result = null;
        if (peminjamanObj != null) {
            Peminjaman peminjaman = convertToPeminjaman(peminjamanObj);
            result = new PeminjamanDto(peminjaman);
        }
        return result;
    }
}

