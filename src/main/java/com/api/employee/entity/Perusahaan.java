package com.api.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "perusahaan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Perusahaan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kdPerusahaan;
    
    @Column(nullable = false, unique = true)
    private String namaPerusahaan;
    
    @Column(name = "admin_email", unique = true)
    private String adminEmail;
    
    @Column(name = "admin_id")
    private String adminId;
}