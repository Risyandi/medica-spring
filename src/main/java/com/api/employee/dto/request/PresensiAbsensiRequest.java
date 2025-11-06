package com.api.employee.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresensiAbsensiRequest {
    @NotNull(message = "Tanggal absensi tidak boleh kosong")
    private Integer tglAbsensi;
    
    @NotNull(message = "Status absensi tidak boleh kosong")
    private Integer kdStatus;
}