package com.api.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "status_absen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusAbsen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kdStatus;
    
    @Column(nullable = false)
    private String namaStatus;
}