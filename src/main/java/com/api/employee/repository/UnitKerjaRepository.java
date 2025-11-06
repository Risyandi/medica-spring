package com.api.employee.repository;

import com.api.employee.entity.UnitKerja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitKerjaRepository extends JpaRepository<UnitKerja, Integer> {
}