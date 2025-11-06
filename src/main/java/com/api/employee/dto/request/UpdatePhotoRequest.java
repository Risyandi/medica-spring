package com.api.employee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePhotoRequest {
    @NotBlank(message = "ID user tidak boleh kosong")
    private String idUser;
    
    @NotBlank(message = "Nama file tidak boleh kosong")
    private String namaFile;
}