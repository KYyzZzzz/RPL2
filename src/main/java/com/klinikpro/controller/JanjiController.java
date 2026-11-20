package com.klinikpro.controller;

import com.klinikpro.model.Dokter;
import com.klinikpro.model.Janji;
import com.klinikpro.model.Pengguna;
import com.klinikpro.service.JanjiService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Controller;

/**
 * CONTROLLER: penghubung halaman Janji Temu dengan JanjiService.
 * Mengecek isian form, lalu meneruskan ke service. Pesan kesalahan dari
 * service diubah menjadi ValidasiException agar bisa ditampilkan di form.
 */
@Controller
public class JanjiController {

    private final JanjiService janjiService;

    public JanjiController(JanjiService janjiService) {
        this.janjiService = janjiService;
    }

    public List<LocalDate> tanggalPraktik(Dokter dokter) {
        return janjiService.tanggalPraktik(dokter);
    }

    public List<LocalTime> slotJam(Dokter dokter, LocalDate tanggal) {
        return janjiService.slotJam(dokter, tanggal);
    }

    public List<LocalTime> jamTerisi(Dokter dokter, LocalDate tanggal) {
        return janjiService.jamTerisi(dokter, tanggal);
    }

    /** Semua janji milik pasien yang sedang login. */
    public List<Janji> janjiSaya() {
        Pengguna p = Sesi.pengguna();
        return p == null ? List.of() : janjiService.janjiPasien(p);
    }

    /** Janji terdekat yang belum dilaksanakan, untuk kartu di Beranda. */
    public Janji janjiBerikutnya() {
        for (Janji j : janjiSaya()) {
            if (Janji.DIJADWALKAN.equals(j.getStatus()) && !j.getTanggal().isBefore(LocalDate.now())) {
                return j;
            }
        }
        return null;
    }

    public Janji buatJanji(Dokter dokter, LocalDate tanggal, LocalTime jam, String keluhan) throws ValidasiException {
        if (dokter == null) throw new ValidasiException("Pilih dokter terlebih dahulu.");
        if (tanggal == null) throw new ValidasiException("Dokter belum memiliki jadwal praktik.");
        if (jam == null) throw new ValidasiException("Pilih jam janji temu.");
        String isiKeluhan = keluhan == null ? "" : keluhan.trim();
        if (isiKeluhan.length() > 255) throw new ValidasiException("Keluhan maksimal 255 karakter.");
        try {
            return janjiService.buatJanji(Sesi.pengguna(), dokter, tanggal, jam,
                    isiKeluhan.isEmpty() ? null : isiKeluhan);
        } catch (IllegalArgumentException ex) {
            throw new ValidasiException(ex.getMessage());
        }
    }

    public void batalkan(Janji janji) throws ValidasiException {
        try {
            janjiService.batalkan(janji);
        } catch (IllegalArgumentException ex) {
            throw new ValidasiException(ex.getMessage());
        }
    }
}
