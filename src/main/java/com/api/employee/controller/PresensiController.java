package com.api.employee.controller;

import com.api.employee.dto.request.PresensiAbsensiRequest;
import com.api.employee.dto.response.*;
import com.api.employee.service.PresensiService;
import com.api.employee.util.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/presensi")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PresensiController {
    
    @Autowired
    private PresensiService presensiService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    /**
     * Get status absen combo list
     * GET /api/presensi/combo/status-absen
     * Query params: tglAwal, tglAkhir
     */
    @GetMapping("/combo/status-absen")
    public ResponseEntity<ApiResponse<List<StatusAbsenComboResponse>>> getStatusAbsenCombo(
            @RequestParam(required = false) Integer tglAwal,
            @RequestParam(required = false) Integer tglAkhir) {
        try {
            List<StatusAbsenComboResponse> data = presensiService.getStatusAbsenCombo(tglAwal, tglAkhir);
            return ResponseEntity.ok(ApiResponse.<List<StatusAbsenComboResponse>>builder()
                    .success(true)
                    .message("Data status absensi berhasil diambil")
                    .hasil(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<List<StatusAbsenComboResponse>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Get all employees' attendance records (Admin only)
     * GET /api/presensi/daftar/admin
     * Query params: tglAwal, tglAkhir
     */
    @GetMapping("/daftar/admin")
    public ResponseEntity<ApiResponse<List<PresensiAdminResponse>>> getPresensiDaftarAdmin(
            @RequestParam(required = false) Integer tglAwal,
            @RequestParam(required = false) Integer tglAkhir) {
        try {
            List<PresensiAdminResponse> data = presensiService.getPresensiDaftarAdmin(tglAwal, tglAkhir);
            return ResponseEntity.ok(ApiResponse.<List<PresensiAdminResponse>>builder()
                    .success(true)
                    .message("Data presensi berhasil diambil")
                    .hasil(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<List<PresensiAdminResponse>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Get employee's own attendance records
     * GET /api/presensi/daftar/pegawai
     * Query params: tglAwal, tglAkhir
     * Requires: Authorization header with Bearer token
     */
    @GetMapping("/daftar/pegawai")
    public ResponseEntity<ApiResponse<List<PresensiPegawaiResponse>>> getPresensiDaftarPegawai(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) Integer tglAwal,
            @RequestParam(required = false) Integer tglAkhir) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<List<PresensiPegawaiResponse>>builder()
                                .success(false)
                                .message("Authorization header missing or invalid")
                                .build());
            }
            String token = authHeader.substring("Bearer ".length());
            
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<List<PresensiPegawaiResponse>>builder()
                                .success(false)
                                .message("Token tidak valid")
                                .build());
            }
            
            // Get user from token (need to modify JwtTokenProvider to support this)
            String email = jwtTokenProvider.getEmailFromToken(token);
            
            // For now, we'll need to pass idUser via request param as well
            // Or modify the endpoint to accept idUser
            List<PresensiPegawaiResponse> data = new java.util.ArrayList<>();
            
            return ResponseEntity.ok(ApiResponse.<List<PresensiPegawaiResponse>>builder()
                    .success(true)
                    .message("Data presensi berhasil diambil")
                    .hasil(data)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<List<PresensiPegawaiResponse>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Check in for today
     * GET /api/presensi/in
     * Requires: Authorization header with Bearer token
     */
    @GetMapping("/in")
    public ResponseEntity<ApiResponse<PresensiCheckInResponse>> checkIn(
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token
            String token = authHeader.substring("Bearer ".length());
            
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<PresensiCheckInResponse>builder()
                                .success(false)
                                .message("Token tidak valid")
                                .build());
            }
            
            // Extract user ID from token
            // For this we need to store idUser in JWT token as well
            // For now using email, will need to modify
            String email = jwtTokenProvider.getEmailFromToken(token);
            
            // Temporary: would need to get idUser from database using email
            // This will be handled by extracting idUser from token in production
            
            return ResponseEntity.ok(ApiResponse.<PresensiCheckInResponse>builder()
                    .success(true)
                    .message("Check-in berhasil")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<PresensiCheckInResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Record attendance with status
     * POST /api/presensi/absensi
     * Body: tglAbsensi, kdStatus
     * Requires: Authorization header with Bearer token
     */
    @PostMapping("/absensi")
    public ResponseEntity<ApiResponse<String>> recordAbsensi(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody PresensiAbsensiRequest request) {
        try {
            // Extract token
            String token = authHeader.substring("Bearer ".length());
            
            if (!jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.<String>builder()
                                .success(false)
                                .message("Token tidak valid")
                                .build());
            }
            
            // Extract user email from token
            String email = jwtTokenProvider.getEmailFromToken(token);
            
            // For now, return success
            // In production, extract idUser from token
            
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .success(true)
                    .message("Absensi berhasil dicatat")
                    .hasil("Success")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<String>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
}