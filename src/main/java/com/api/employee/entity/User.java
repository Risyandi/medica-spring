package com.api.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idUser;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "nik_user")
    private String nikUser;
    
    @Column(nullable = false)
    private String namaLengkap;
    
    @Column(name = "tempat_lahir")
    private String tempatLahir;
    
    @Column(name = "tanggal_lahir")
    private Integer tanggalLahir;
    
    @Column(name = "kd_jabatan")
    private Integer kdJabatan;
    
    @Column(name = "kd_departemen")
    private Integer kdDepartemen;
    
    @Column(name = "kd_unit_kerja")
    private Integer kdUnitKerja;
    
    @Column(name = "kd_jenis_kelamin")
    private Integer kdJenisKelamin;
    
    @Column(name = "kd_pendidikan")
    private Integer kdPendidikan;
    
    @Column(name = "profile")
    private String profile;
    
    @Column(name = "photo")
    private String photo;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}