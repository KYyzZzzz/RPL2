package com.klinikpro.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** MODEL: data dokter, dipetakan ke tabel "dokter" di MySQL. */
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
    public String getDeskripsi() { return deskripsi == null ? "" : deskripsi; }
    public BigDecimal getTarif() { return tarif == null ? BigDecimal.ZERO : tarif; }
    public List<JadwalDokter> getJadwal() { return jadwal; }

    /** Inisial nama untuk avatar, misalnya "dr. Andi Pratama" menjadi "AP". */
    public String getInisial() {
        StringBuilder sb = new StringBuilder();
        for (String kata : nama.replace("dr.", "").split("[ ,]+")) {
            if (!kata.isBlank() && !kata.contains(".") && sb.length() < 2) {
                sb.append(Character.toUpperCase(kata.charAt(0)));
            }
        }
        return sb.toString();
    }

    /** Daftar hari praktik, misalnya [Senin, Rabu]. */
    public List<String> getHariPraktik() {
        List<String> hari = new ArrayList<>();
        for (JadwalDokter j : jadwal) {
            hari.add(j.getHari());
        }
        return hari;
    }

    /** Jadwal ringkas untuk ditampilkan, misalnya "Senin 08:00-12:00, Rabu 08:00-12:00". */
    public String getJadwalTeks() {
        StringBuilder sb = new StringBuilder();
        for (JadwalDokter j : jadwal) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(j.getHari()).append(" ").append(j.getJamTeks());
        }
        return sb.toString();
    }
}
