package com.api.employee.repository;

import com.api.employee.entity.JenisKelamin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JenisKelaminRepository extends JpaRepository<JenisKelamin, Integer> {
}