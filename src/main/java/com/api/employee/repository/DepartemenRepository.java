package com.api.employee.repository;

import com.api.employee.entity.Departemen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartemenRepository extends JpaRepository<Departemen, Integer> {
}