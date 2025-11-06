package com.api.employee.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private String profile;
    private String idUser;
    private String namaLengkap;
    private String tempatLahir;
    private Integer tanggalLahir;
    private String email;
    private String password;
    private String nikUser;
    private Integer kdJabatan;
    private String namaJabatan;
    private Integer kdDepartemen;
    private String namaDepartemen;
    private Integer kdUnitKerja;
    private String namaUnitKerja;
    private Integer kdJenisKelamin;
    private String namaJenisKelamin;
    private Integer kdPendidikan;
    private String namaPendidikan;
    private String photo;
}