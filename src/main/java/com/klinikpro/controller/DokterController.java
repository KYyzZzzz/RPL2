package com.klinikpro.controller;

import com.klinikpro.model.Dokter;
import com.klinikpro.repository.DokterRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import org.springframework.stereotype.Controller;

/**
 * CONTROLLER: penghubung antara view (tampilan) dan model (data).
 * View meminta data ke controller, controller mengolah permintaan itu
 * (mencari, memfilter, mengurutkan), lalu mengambil data dari database
 * lewat repository. View tidak pernah mengakses repository secara langsung.
 */
@Controller
public class DokterController {

    private final DokterRepository dokterRepository;

    public DokterController(DokterRepository dokterRepository) {
        this.dokterRepository = dokterRepository;
    }

    /** Semua dokter, diurutkan berdasarkan nama. */
    public List<Dokter> semua() {
        List<Dokter> hasil = new ArrayList<>(dokterRepository.findAll());
        hasil.sort(Comparator.comparing(Dokter::getNama));
        return hasil;
    }

    /**
     * Cari dokter berdasarkan kata kunci (nama atau spesialis), spesialis, dan hari praktik.
     * Parameter yang kosong atau null tidak dipakai sebagai filter.
     */
    public List<Dokter> cari(String kataKunci, String spesialis, String hari) {
        String kunci = kataKunci == null ? "" : kataKunci.trim().toLowerCase();
        List<Dokter> hasil = new ArrayList<>();
        for (Dokter d : semua()) {
            boolean cocokKunci = kunci.isEmpty()
                    || d.getNama().toLowerCase().contains(kunci)
                    || d.getSpesialis().toLowerCase().contains(kunci);
            boolean cocokSpesialis = spesialis == null || d.getSpesialis().equalsIgnoreCase(spesialis);
            boolean cocokHari = hari == null || d.getHariPraktik().contains(hari);
            if (cocokKunci && cocokSpesialis && cocokHari) {
                hasil.add(d);
            }
        }
        return hasil;
    }

    /** Daftar spesialis yang ada di database, untuk tombol filter. */
    public List<String> daftarSpesialis() {
        TreeSet<String> set = new TreeSet<>();
        for (Dokter d : semua()) {
            set.add(d.getSpesialis());
        }
        return new ArrayList<>(set);
    }
}
