package com.api.employee.controller;

import com.api.employee.dto.request.AddPegawaiRequest;
import com.api.employee.dto.request.UpdatePegawaiRequest;
import com.api.employee.dto.response.ApiResponse;
import com.api.employee.dto.response.ComboResponse;
import com.api.employee.dto.response.DepartemenHrdResponse;
import com.api.employee.dto.response.UserInfoResponse;
import com.api.employee.service.PegawaiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/pegawai")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PegawaiController {
    
    @Autowired
    private PegawaiService pegawaiService;
    
    /**
     * Get Jabatan combo list
     * GET /api/pegawai/combo/jabatan
     */
    @GetMapping("/combo/jabatan")
    public ResponseEntity<ApiResponse<List<ComboResponse>>> getJabatanCombo() {
        try {
            List<ComboResponse> data = pegawaiService.getJabatanCombo();
            return ResponseEntity.ok(ApiResponse.<List<ComboResponse>>builder()
                    .success(true)
                    .message("Data jabatan berhasil diambil")
                    .hasil(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<ComboResponse>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Get Departemen combo list
     * GET /api/pegawai/combo/departemen
     */
    @GetMapping("/combo/departemen")
    public ResponseEntity<ApiResponse<List<ComboResponse>>> getDepartemenCombo() {
        try {
            List<ComboResponse> data = pegawaiService.getDepartemenCombo();
            return ResponseEntity.ok(ApiResponse.<List<ComboResponse>>builder()
                    .success(true)
                    .message("Data departemen berhasil diambil")
                    .hasil(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<ComboResponse>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Get Pendidikan combo list
     * GET /api/pegawai/combo/pendidikan
     */
    @GetMapping("/combo/pendidikan")
    public ResponseEntity<ApiResponse<List<ComboResponse>>> getPendidikanCombo() {
        try {
            List<ComboResponse> data = pegawaiService.getPendidikanCombo();
            return ResponseEntity.ok(ApiResponse.<List<ComboResponse>>builder()
                    .success(true)
                    .message("Data pendidikan berhasil diambil")
                    .hasil(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<ComboResponse>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Get Jenis Kelamin combo list
     * GET /api/pegawai/combo/jenis-kelamin
     */
    @GetMapping("/combo/jenis-kelamin")
    public ResponseEntity<ApiResponse<List<ComboResponse>>> getJenisKelaminCombo() {
        try {
            List<ComboResponse> data = pegawaiService.getJenisKelaminCombo();
            return ResponseEntity.ok(ApiResponse.<List<ComboResponse>>builder()
                    .success(true)
                    .message("Data jenis kelamin berhasil diambil")
                    .hasil(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<ComboResponse>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Get Departemen HRD - All HRD staff
     * GET /api/pegawai/combo/departemen-hrd
     */
    @GetMapping("/combo/departemen-hrd")
    public ResponseEntity<ApiResponse<List<DepartemenHrdResponse>>> getDepartemenHrd() {
        try {
            List<DepartemenHrdResponse> data = pegawaiService.getDepartemenHrd();
            return ResponseEntity.ok(ApiResponse.<List<DepartemenHrdResponse>>builder()
                    .success(true)
                    .message("Data departemen HRD berhasil diambil")
                    .hasil(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<DepartemenHrdResponse>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Get all employees list
     * GET /api/pegawai/daftar
     */
    @GetMapping("/daftar")
    public ResponseEntity<ApiResponse<List<UserInfoResponse>>> getAllPegawai() {
        try {
            List<UserInfoResponse> data = pegawaiService.getAllPegawai();
            return ResponseEntity.ok(ApiResponse.<List<UserInfoResponse>>builder()
                    .success(true)
                    .message("Data pegawai berhasil diambil")
                    .hasil(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<UserInfoResponse>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Admin add employee
     * POST /api/pegawai/admin-tambah-pegawai
     */
    @PostMapping("/admin-tambah-pegawai")
    public ResponseEntity<ApiResponse<String>> addPegawai(
            @Valid @RequestBody AddPegawaiRequest request) {
        try {
            ApiResponse<String> response = pegawaiService.addPegawai(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Admin update employee
     * POST /api/pegawai/admin-ubah-pegawai
     */
    @PostMapping("/admin-ubah-pegawai")
    public ResponseEntity<ApiResponse<String>> updatePegawai(
            @Valid @RequestBody UpdatePegawaiRequest request) {
        try {
            ApiResponse<String> response = pegawaiService.updatePegawai(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Admin update employee photo
     * POST /api/pegawai/admin-ubah-photo
     */
    @PostMapping("/admin-ubah-photo")
    public ResponseEntity<ApiResponse<String>> adminUpdatePhoto(
            @RequestParam("idUser") String idUser,
            @RequestParam("namaFile") String namaFile,
            @RequestParam("files") MultipartFile file) {
        try {
            ApiResponse<String> response = pegawaiService.adminUpdatePhoto(idUser, namaFile, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * User update own photo
     * POST /api/pegawai/ubah-photo
     */
    @PostMapping("/ubah-photo")
    public ResponseEntity<ApiResponse<String>> updateOwnPhoto(
            @RequestParam("idUser") String idUser,
            @RequestParam("namaFile") String namaFile,
            @RequestParam("files") MultipartFile file) {
        try {
            ApiResponse<String> response = pegawaiService.updateOwnPhoto(idUser, namaFile, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
}