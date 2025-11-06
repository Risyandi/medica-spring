package com.api.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pendidikan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pendidikan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kdPendidikan;
    
    @Column(nullable = false)
    private String namaPendidikan;
}