package com.api.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "unit_kerja")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitKerja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kdUnitKerja;
    
    @Column(nullable = false)
    private String namaUnitKerja;
}