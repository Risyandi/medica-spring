package com.api.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departemen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Departemen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kdDepartemen;
    
    @Column(nullable = false)
    private String namaDepartemen;
}