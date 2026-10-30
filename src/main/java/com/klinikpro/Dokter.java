package com.klinikpro;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dokter")
public class Dokter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nama;
    private String spesialis;
    private String deskripsi;
    private BigDecimal tarif;

    @OneToMany(mappedBy = "dokter", fetch = FetchType.EAGER)
    private List<JadwalDokter> jadwal = new ArrayList<>();

    public Integer getId() { return id; }
    public String getNama() { return nama; }
    public String getSpesialis() { return spesialis; }
    public String getDeskripsi() { return deskripsi; }
    public BigDecimal getTarif() { return tarif; }

    public String getJadwalTeks() {
        StringBuilder sb = new StringBuilder();
        for (JadwalDokter j : jadwal) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(j.getHari()).append(" ")
              .append(j.getJamMulai()).append("-").append(j.getJamSelesai());
        }
        return sb.toString();
    }
}