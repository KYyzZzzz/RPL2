package com.klinikpro.repository;

import com.klinikpro.model.Dokter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MODEL (akses data): mengambil data dokter dari database lewat Hibernate.
 * Hanya dipanggil oleh DokterController, tidak pernah langsung oleh view.
 */
public interface DokterRepository extends JpaRepository<Dokter, Integer> {
}
