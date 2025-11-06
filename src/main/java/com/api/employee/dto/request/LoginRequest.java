package com.api.employee.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Email(message = "Email harus valid")
    @NotBlank(message = "Email tidak boleh kosong")
    private String email;
    
    @NotBlank(message = "Password tidak boleh kosong")
    private String password;
    
    @NotBlank(message = "Profile tidak boleh kosong")
    private String profile;
}