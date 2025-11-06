package com.api.employee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitDataRequest {
    @NotBlank(message = "Nama admin tidak boleh kosong")
    private String namaAdmin;
    
    @NotBlank(message = "Perusahaan tidak boleh kosong")
    private String perusahaan;
}