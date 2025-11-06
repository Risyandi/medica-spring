package com.api.employee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Password asli tidak boleh kosong")
    private String passwordAsli;
    
    @NotBlank(message = "Password baru tidak boleh kosong")
    private String passwordBaru1;
    
    @NotBlank(message = "Konfirmasi password baru tidak boleh kosong")
    private String passwordBaru2;
}