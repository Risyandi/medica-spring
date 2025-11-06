package com.api.employee.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePegawaiRequest {
    @NotBlank(message = "ID user tidak boleh kosong")
    private String idUser;
    
    @NotBlank(message = "Nama lengkap tidak boleh kosong")
    private String namaLengkap;
    
    @Email(message = "Email harus valid")
    @NotBlank(message = "Email tidak boleh kosong")
    private String email;
    
    @NotBlank(message = "Tempat lahir tidak boleh kosong")
    private String tempatLahir;
    
    @NotNull(message = "Tanggal lahir tidak boleh kosong")
    private Integer tanggalLahir;
    
    @NotNull(message = "Jenis kelamin harus dipilih")
    private Integer kdJenisKelamin;
    
    @NotNull(message = "Pendidikan harus dipilih")
    private Integer kdPendidikan;
    
    @NotNull(message = "Jabatan harus dipilih")
    private Integer kdJabatan;
    
    @NotNull(message = "Departemen harus dipilih")
    private Integer kdDepartemen;
    
    @NotNull(message = "Unit kerja harus dipilih")
    private Integer kdUnitKerja;
    
    @NotBlank(message = "Password tidak boleh kosong")
    private String password;
    
    @NotBlank(message = "Konfirmasi password tidak boleh kosong")
    private String passwordC;
}