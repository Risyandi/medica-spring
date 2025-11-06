package com.api.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "presensi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Presensi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPresensi;
    
    @Column(name = "id_user", nullable = false)
    private String idUser;
    
    @Column(name = "tgl_absensi", nullable = false)
    private Integer tglAbsensi;  // Format: YYYYMMDD
    
    @Column(name = "jam_masuk")
    private String jamMasuk;  // Format: HH:mm:ss
    
    @Column(name = "jam_keluar")
    private String jamKeluar;  // Format: HH:mm:ss
    
    @Column(name = "kd_status")
    private Integer kdStatus;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "kd_status", insertable = false, updatable = false)
    private StatusAbsen statusAbsen;
    
    @ManyToOne
    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    private User user;
}