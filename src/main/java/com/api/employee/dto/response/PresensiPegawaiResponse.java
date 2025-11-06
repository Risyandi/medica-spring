package com.api.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresensiPegawaiResponse {
    private Integer tglAbsensi;
    private String jamMasuk;
    private String jamKeluar;
    private String namaStatus;
}