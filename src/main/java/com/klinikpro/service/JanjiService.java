package com.klinikpro.service;

import com.klinikpro.model.Dokter;
import com.klinikpro.model.JadwalDokter;
import com.klinikpro.model.Janji;
import com.klinikpro.model.Pengguna;
import com.klinikpro.repository.JanjiRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SERVICE: aturan bisnis janji temu.
 * Objek kelas ini dibuat oleh Spring (@Service), lalu JanjiRepository
 * disuntikkan lewat konstruktor (Dependency Injection).
 */
@Service
public class JanjiService {

    public static final int MENIT_PER_PASIEN = 30;
    public static final int HARI_KE_DEPAN = 14;
    private static final String[] NAMA_HARI = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};

    private final JanjiRepository janjiRepository;

    public JanjiService(JanjiRepository janjiRepository) {
        this.janjiRepository = janjiRepository;
    }

    /** Nama hari dalam bahasa Indonesia, sama seperti kolom hari di tabel jadwal_dokter. */
    public static String namaHari(LocalDate tanggal) {
        return NAMA_HARI[tanggal.getDayOfWeek().getValue() - 1];
    }

    /** Jadwal dokter pada hari tersebut, atau null jika dokter tidak praktik. */
    public JadwalDokter jadwalPada(Dokter dokter, LocalDate tanggal) {
        for (JadwalDokter j : dokter.getJadwal()) {
            if (j.getHari().equals(namaHari(tanggal))) {
                return j;
            }
        }
        return null;
    }

    /** Tanggal praktik dokter mulai besok sampai 14 hari ke depan. */
    public List<LocalDate> tanggalPraktik(Dokter dokter) {
        List<LocalDate> hasil = new ArrayList<>();
        for (int i = 1; i <= HARI_KE_DEPAN; i++) {
            LocalDate tanggal = LocalDate.now().plusDays(i);
            if (jadwalPada(dokter, tanggal) != null) {
                hasil.add(tanggal);
            }
        }
        return hasil;
    }

    /** Pilihan jam setiap 30 menit selama jam praktik, misalnya 08:00, 08:30, ... 11:30. */
    public List<LocalTime> slotJam(Dokter dokter, LocalDate tanggal) {
        List<LocalTime> slot = new ArrayList<>();
        JadwalDokter jadwal = jadwalPada(dokter, tanggal);
        if (jadwal != null) {
            for (LocalTime t = jadwal.getJamMulai(); t.isBefore(jadwal.getJamSelesai());
                    t = t.plusMinutes(MENIT_PER_PASIEN)) {
                slot.add(t);
            }
        }
        return slot;
    }

    @Transactional(readOnly = true)
    public List<LocalTime> jamTerisi(Dokter dokter, LocalDate tanggal) {
        return janjiRepository.cariJamTerisi(dokter.getId(), tanggal);
    }

    @Transactional(readOnly = true)
    public List<Janji> janjiPasien(Pengguna pengguna) {
        return janjiRepository.findByPenggunaIdOrderByTanggalAscJamAsc(pengguna.getId());
    }

    /**
     * Declarative Transaction: semua pengecekan dan penyimpanan berjalan dalam satu transaksi.
     * Jika ada IllegalArgumentException, transaksi otomatis di-rollback.
     */
    @Transactional
    public Janji buatJanji(Pengguna pengguna, Dokter dokter, LocalDate tanggal, LocalTime jam, String keluhan) {
        if (tanggal.isBefore(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException("Janji temu paling cepat untuk besok.");
        }
        List<LocalTime> slot = slotJam(dokter, tanggal);
        if (slot.isEmpty()) {
            throw new IllegalArgumentException(dokter.getNama() + " tidak praktik pada hari " + namaHari(tanggal) + ".");
        }
        if (!slot.contains(jam)) {
            throw new IllegalArgumentException("Jam yang dipilih di luar jadwal praktik dokter.");
        }
        if (janjiRepository.cariJamTerisi(dokter.getId(), tanggal).contains(jam)) {
            throw new IllegalArgumentException("Jam " + jam + " sudah diambil pasien lain. Silakan pilih jam lain.");
        }
        if (janjiRepository.existsByPenggunaIdAndDokterIdAndTanggalAndStatus(
                pengguna.getId(), dokter.getId(), tanggal, Janji.DIJADWALKAN)) {
            throw new IllegalArgumentException("Anda sudah punya janji dengan dokter ini pada tanggal tersebut.");
        }

        int noAntrean = slot.indexOf(jam) + 1;   // jam pertama = antrean 1, jam kedua = antrean 2, dst.
        Janji janji = new Janji(pengguna, dokter, tanggal, jam, noAntrean, keluhan);
        return janjiRepository.save(janji);      // ORM: INSERT ke tabel janji dibuat oleh Hibernate
    }

    @Transactional
    public void batalkan(Janji janji) {
        if (!Janji.DIJADWALKAN.equals(janji.getStatus())) {
            throw new IllegalArgumentException("Janji ini sudah tidak bisa dibatalkan.");
        }
        janji.batalkan();
        janjiRepository.save(janji);             // ORM: UPDATE status menjadi "dibatalkan"
    }
}
