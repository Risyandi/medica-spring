package com.api.employee.repository;

import com.api.employee.entity.Perusahaan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerusahaanRepository extends JpaRepository<Perusahaan, Integer> {
    boolean existsByNamaPerusahaan(String namaPerusahaan);
}