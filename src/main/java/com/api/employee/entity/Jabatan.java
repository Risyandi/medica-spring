package com.api.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jabatan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jabatan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kdJabatan;
    
    @Column(nullable = false)
    private String namaJabatan;
}