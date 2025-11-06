package com.api.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "absensi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Absensi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_absensi")
    private Long idAbsensi;

    // References users.id_user (VARCHAR(36))
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    // Stored as integer YYYYMMDD (e.g., 20241106)
    @Column(name = "tgl_absensi", nullable = false)
    private Integer tglAbsensi;

    // Stored as "HH:mm:ss"
    @Column(name = "jam_masuk", length = 8)
    private String jamMasuk;

    // Stored as "HH:mm:ss"
    @Column(name = "jam_keluar", length = 8)
    private String jamKeluar;

    // References status_absen.kd_status
    @Column(name = "kd_status", nullable = false)
    private Integer kdStatus;

    // Set by DB default CURRENT_TIMESTAMP (do not insert/update from JPA)
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}