package com.klinikpro;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "jadwal_dokter")
public class JadwalDokter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "dokter_id")
    private Dokter dokter;

    private String hari;

    @Column(name = "jam_mulai")
    private LocalTime jamMulai;

    @Column(name = "jam_selesai")
    private LocalTime jamSelesai;

    public String getHari() { return hari; }
    public LocalTime getJamMulai() { return jamMulai; }
    public LocalTime getJamSelesai() { return jamSelesai; }
}