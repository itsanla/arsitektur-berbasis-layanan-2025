package com.anla.Peminjaman.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Peminjaman {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("tanggal_pinjam")
    private LocalDate tanggal_pinjam;
    @JsonProperty("tanggal_dikembalikan")
    private LocalDate tanggalDikembalikan;
    @JsonProperty("tanggal_batas")
    private LocalDate tanggal_batas;
    @JsonProperty("anggota_id")
    private Long anggotaId;
    @JsonProperty("buku_id")
    private Long bukuId;
}
