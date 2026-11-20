package com.klinikpro.repository;

import com.klinikpro.model.Janji;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** MODEL (akses data): menyimpan dan mengambil data janji temu lewat Hibernate. */
public interface JanjiRepository extends JpaRepository<Janji, Integer> {

    /** Semua janji milik satu pasien, urut dari tanggal dan jam paling awal. */
    List<Janji> findByPenggunaIdOrderByTanggalAscJamAsc(Integer penggunaId);

    /** Apakah pasien sudah punya janji aktif dengan dokter ini di tanggal tersebut. */
    boolean existsByPenggunaIdAndDokterIdAndTanggalAndStatus(Integer penggunaId, Integer dokterId,
            LocalDate tanggal, String status);

    /** Jam yang sudah dipakai di tanggal tersebut (query HQL ditulis sendiri). */
    @Query("SELECT j.jam FROM Janji j WHERE j.dokter.id = :dokterId AND j.tanggal = :tanggal")
    List<LocalTime> cariJamTerisi(@Param("dokterId") Integer dokterId, @Param("tanggal") LocalDate tanggal);
}
