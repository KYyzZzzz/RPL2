package com.klinikpro;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DokterRepository extends JpaRepository<Dokter, Integer> {
    List<Dokter> findByNamaContainingIgnoreCaseAndSpesialisContainingIgnoreCase(
            String nama, String spesialis);
}