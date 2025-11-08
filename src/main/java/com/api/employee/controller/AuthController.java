package com.api.employee.controller;

import com.api.employee.dto.request.ChangePasswordRequest;
import com.api.employee.dto.request.InitDataRequest;
import com.api.employee.dto.request.LoginRequest;
import com.api.employee.dto.response.ApiResponse;
import com.api.employee.dto.response.InitDataResponse;
import com.api.employee.dto.response.LoginResponse;
import com.api.employee.service.AuthService;
import com.api.employee.util.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    /**
     * Initialize data - Create admin account for new company
     * POST /api/auth/init-data
     */
    @PostMapping("/init-data")
    public ResponseEntity<ApiResponse<InitDataResponse>> initData(
            @Valid @RequestBody InitDataRequest request) {
        try {
            InitDataResponse response = authService.initData(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<InitDataResponse>builder()
                            .success(true)
                            .message("Data inisial berhasil dibuat")
                            .hasil(response)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<InitDataResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Login endpoint
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        try {
            ApiResponse<LoginResponse> response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<LoginResponse>builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        }
    }
    
    /**
     * Change password endpoint
     * POST /api/auth/ubah-password-sendiri
     * Requires Authorization header with Bearer token
     */
    @PostMapping("/ubah-password-sendiri")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ChangePasswordRequest request) {
        try {
        // Validate and extract token from Authorization header
        if (authHeader == null || authHeader.trim().isEmpty() || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.<String>builder()
                .success(false)
                .message("Authorization header must be provided and start with 'Bearer '")
                .build());
        }

        String token = authHeader.substring("Bearer ".length()).trim();

        if (!jwtTokenProvider.validateToken(token)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.<String>builder()
                .success(false)
                .message("Token tidak valid")
                .build());
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
            ApiResponse<String> response = authService.changePassword(email, request);
            
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