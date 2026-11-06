package com.klinikpro.repository;

import com.klinikpro.model.Pengguna;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenggunaRepository extends JpaRepository<Pengguna, Integer> {

    Optional<Pengguna> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}