package com.klinikpro.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

/** MODEL: janji temu pasien dengan dokter, dipetakan ke tabel "janji". */
@Entity
@Table(name = "janji")
public class Janji {

    public static final String DIJADWALKAN = "dijadwalkan";
    public static final String SELESAI = "selesai";
    public static final String DIBATALKAN = "dibatalkan";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pengguna_id")
    private Pengguna pengguna;

    @ManyToOne
    @JoinColumn(name = "dokter_id")
    private Dokter dokter;

    private LocalDate tanggal;
    private LocalTime jam;

    @Column(name = "no_antrean")
    private Integer noAntrean;

    private String keluhan;
    private String status = DIJADWALKAN;

    /** Dibutuhkan Hibernate untuk membuat objek dari data di tabel. */
    protected Janji() {
    }

    public Janji(Pengguna pengguna, Dokter dokter, LocalDate tanggal, LocalTime jam,
            int noAntrean, String keluhan) {
        this.pengguna = pengguna;
        this.dokter = dokter;
        this.tanggal = tanggal;
        this.jam = jam;
        this.noAntrean = noAntrean;
        this.keluhan = keluhan;
    }

    public Integer getId() { return id; }
    public Pengguna getPengguna() { return pengguna; }
    public Dokter getDokter() { return dokter; }
    public LocalDate getTanggal() { return tanggal; }
    public LocalTime getJam() { return jam; }
    public Integer getNoAntrean() { return noAntrean; }
    public String getKeluhan() { return keluhan == null ? "" : keluhan; }
    public String getStatus() { return status; }

    public void batalkan() {
        this.status = DIBATALKAN;
    }

    /** Kode antrean, misalnya "A-03": huruf menandai dokter, angka menandai urutan. */
    public String getKodeAntrean() {
        char huruf = (char) ('A' + (dokter.getId() - 1) % 26);
        return String.format("%c-%02d", huruf, noAntrean);
    }
}
