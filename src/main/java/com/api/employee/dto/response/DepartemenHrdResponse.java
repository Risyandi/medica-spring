package com.api.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartemenHrdResponse {
    private Object kd;
    private String nama;
    private String namaLengkap;
    private Integer kdJabatan;
    private String namaJabatan;
}