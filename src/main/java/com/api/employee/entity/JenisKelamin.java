package com.api.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jenis_kelamin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JenisKelamin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kdJenisKelamin;
    
    @Column(nullable = false)
    private String namaJenisKelamin;
}