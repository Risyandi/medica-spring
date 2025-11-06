package com.api.employee.service;

import com.api.employee.dto.request.PresensiAbsensiRequest;
import com.api.employee.dto.response.*;
import com.api.employee.entity.Presensi;
import com.api.employee.entity.StatusAbsen;
import com.api.employee.entity.User;
import com.api.employee.repository.PresensiRepository;
import com.api.employee.repository.StatusAbsenRepository;
import com.api.employee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PresensiService {
    
    @Autowired
    private PresensiRepository presensiRepository;
    
    @Autowired
    private StatusAbsenRepository statusAbsenRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    /**
     * Get status absen combo list
     * Between two dates (can be used to filter available statuses)
     */
    public List<StatusAbsenComboResponse> getStatusAbsenCombo(Integer tglAwal, Integer tglAkhir) throws Exception {
        // Validate date parameters
        if (tglAwal == null || tglAkhir == null) {
            throw new Exception("Tanggal awal dan tanggal akhir tidak boleh kosong");
        }
        
        if (tglAwal > tglAkhir) {
            throw new Exception("Tanggal awal tidak boleh lebih besar dari tanggal akhir");
        }
        
        return statusAbsenRepository.findAll()
                .stream()
                .map(status -> StatusAbsenComboResponse.builder()
                        .kdStatus(status.getKdStatus())
                        .namaStatus(status.getNamaStatus())
                        .build())
                .collect(Collectors.toList());
    }
    
    /**
     * Get attendance list for admin
     * Shows all employees' attendance between dates
     */
    public List<PresensiAdminResponse> getPresensiDaftarAdmin(Integer tglAwal, Integer tglAkhir) throws Exception {
        // Validate date parameters
        if (tglAwal == null || tglAkhir == null) {
            throw new Exception("Tanggal awal dan tanggal akhir tidak boleh kosong");
        }
        
        if (tglAwal > tglAkhir) {
            throw new Exception("Tanggal awal tidak boleh lebih besar dari tanggal akhir");
        }
        
        // Get all attendance records within date range
        List<Presensi> presensiList = presensiRepository.findByTglAbsensiBetween(tglAwal, tglAkhir);
        
        return presensiList.stream()
                .map(presensi -> {
                    String namaStatus = "";
                    if (presensi.getStatusAbsen() != null) {
                        namaStatus = presensi.getStatusAbsen().getNamaStatus();
                    }
                    
                    String namaLengkap = "";
                    if (presensi.getUser() != null) {
                        namaLengkap = presensi.getUser().getNamaLengkap();
                    }
                    
                    return PresensiAdminResponse.builder()
                            .idUser(presensi.getIdUser())
                            .namaLengkap(namaLengkap)
                            .tglAbsensi(presensi.getTglAbsensi())
                            .jamMasuk(presensi.getJamMasuk())
                            .jamKeluar(presensi.getJamKeluar())
                            .namaStatus(namaStatus)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Get attendance list for employee
     * Shows own attendance records between dates
     */
    public List<PresensiPegawaiResponse> getPresensiDaftarPegawai(String idUser, Integer tglAwal, Integer tglAkhir) throws Exception {
        // Validate parameters
        if (idUser == null || idUser.trim().isEmpty()) {
            throw new Exception("ID user tidak boleh kosong");
        }
        
        if (tglAwal == null || tglAkhir == null) {
            throw new Exception("Tanggal awal dan tanggal akhir tidak boleh kosong");
        }
        
        if (tglAwal > tglAkhir) {
            throw new Exception("Tanggal awal tidak boleh lebih besar dari tanggal akhir");
        }
        
        // Verify user exists
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new Exception("User tidak ditemukan");
        }
        
        // Get attendance records for this user
        List<Presensi> presensiList = presensiRepository.findByIdUserAndTglAbsensiBetween(
                idUser, tglAwal, tglAkhir);
        
        return presensiList.stream()
                .map(presensi -> {
                    String namaStatus = "";
                    if (presensi.getStatusAbsen() != null) {
                        namaStatus = presensi.getStatusAbsen().getNamaStatus();
                    }
                    
                    return PresensiPegawaiResponse.builder()
                            .tglAbsensi(presensi.getTglAbsensi())
                            .jamMasuk(presensi.getJamMasuk())
                            .jamKeluar(presensi.getJamKeluar())
                            .namaStatus(namaStatus)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Check in for today
     * Creates new attendance record with current time as jamMasuk
     */
    public ApiResponse<PresensiCheckInResponse> checkIn(String idUser) throws Exception {
        // Validate user
        if (idUser == null || idUser.trim().isEmpty()) {
            throw new Exception("ID user tidak boleh kosong");
        }
        
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new Exception("User tidak ditemukan");
        }
        
        // Get today's date in YYYYMMDD format
        Integer todayDate = getTodayDateInYYYYMMDD();
        
        // Check if already checked in today
        Optional<Presensi> existingCheckIn = presensiRepository.findTodayCheckIn(idUser, todayDate);
        if (existingCheckIn.isPresent()) {
            throw new Exception("Anda sudah melakukan check-in hari ini");
        }
        
        // Create new attendance record
        Presensi presensi = new Presensi();
        presensi.setIdUser(idUser);
        presensi.setTglAbsensi(todayDate);
        presensi.setJamMasuk(getCurrentTimeInHHmmss());
        presensi.setKdStatus(1);  // Default status: Hadir
        
        Presensi savedPresensi = presensiRepository.save(presensi);
        
        PresensiCheckInResponse response = PresensiCheckInResponse.builder()
                .jamMasuk(savedPresensi.getJamMasuk())
                .build();
        
        return ApiResponse.<PresensiCheckInResponse>builder()
                .success(true)
                .message("Check-in berhasil")
                .hasil(response)
                .build();
    }
    
    /**
     * Record attendance with custom status
     * Can be used for:
     * - Checkout (end of day)
     * - Mark as absent
     * - Mark as sick leave, etc.
     */
    public ApiResponse<String> recordAbsensi(String idUser, PresensiAbsensiRequest request) throws Exception {
        // Validate input
        if (idUser == null || idUser.trim().isEmpty()) {
            throw new Exception("ID user tidak boleh kosong");
        }
        
        if (request.getTglAbsensi() == null) {
            throw new Exception("Tanggal absensi tidak boleh kosong");
        }
        
        if (request.getKdStatus() == null) {
            throw new Exception("Status absensi tidak boleh kosong");
        }
        
        // Verify user exists
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new Exception("User tidak ditemukan");
        }
        
        // Verify status exists
        Optional<StatusAbsen> status = statusAbsenRepository.findById(request.getKdStatus());
        if (status.isEmpty()) {
            throw new Exception("Status absensi tidak ditemukan");
        }
        
        // Check if attendance record exists for this date
        Optional<Presensi> existingPresensi = presensiRepository
                .findByIdUserAndTglAbsensi(idUser, request.getTglAbsensi());
        
        Presensi presensi;
        if (existingPresensi.isPresent()) {
            // Update existing record
            presensi = existingPresensi.get();
            presensi.setKdStatus(request.getKdStatus());
            presensi.setUpdatedAt(LocalDateTime.now());
        } else {
            // Create new record
            presensi = new Presensi();
            presensi.setIdUser(idUser);
            presensi.setTglAbsensi(request.getTglAbsensi());
            presensi.setKdStatus(request.getKdStatus());
            
            // Set jamMasuk if not already set (for new records)
            presensi.setJamMasuk(getCurrentTimeInHHmmss());
        }
        
        presensiRepository.save(presensi);
        
        return ApiResponse.<String>builder()
                .success(true)
                .message("Absensi berhasil dicatat")
                .hasil("Success")
                .build();
    }
    
    /**
     * Check out for today
     * Updates existing attendance record with jamKeluar
     */
    public ApiResponse<String> checkOut(String idUser) throws Exception {
        if (idUser == null || idUser.trim().isEmpty()) {
            throw new Exception("ID user tidak boleh kosong");
        }
        
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new Exception("User tidak ditemukan");
        }
        
        Integer todayDate = getTodayDateInYYYYMMDD();
        
        // Find today's check-in record
        Optional<Presensi> presensiOpt = presensiRepository.findTodayCheckIn(idUser, todayDate);
        if (presensiOpt.isEmpty()) {
            throw new Exception("Anda belum melakukan check-in hari ini");
        }
        
        Presensi presensi = presensiOpt.get();
        
        // Check if already checked out
        if (presensi.getJamKeluar() != null && !presensi.getJamKeluar().isEmpty()) {
            throw new Exception("Anda sudah melakukan check-out hari ini");
        }
        
        // Update checkout time
        presensi.setJamKeluar(getCurrentTimeInHHmmss());
        presensi.setUpdatedAt(LocalDateTime.now());
        
        presensiRepository.save(presensi);
        
        return ApiResponse.<String>builder()
                .success(true)
                .message("Check-out berhasil")
                .hasil("Success")
                .build();
    }
    
    /**
     * Helper method to get today's date in YYYYMMDD format
     */
    private Integer getTodayDateInYYYYMMDD() {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return Integer.parseInt(dateStr);
    }
    
    /**
     * Helper method to get current time in HH:mm:ss format
     */
    private String getCurrentTimeInHHmmss() {
        return LocalTime.now().format(TIME_FORMATTER);
    }
    
    /**
     * Validate date format YYYYMMDD
     */
    private boolean isValidDateFormat(Integer date) {
        if (date == null) return false;
        
        String dateStr = String.valueOf(date);
        if (dateStr.length() != 8) return false;
        
        try {
            int year = Integer.parseInt(dateStr.substring(0, 4));
            int month = Integer.parseInt(dateStr.substring(4, 6));
            int day = Integer.parseInt(dateStr.substring(6, 8));
            
            return month >= 1 && month <= 12 && day >= 1 && day <= 31;
        } catch (Exception e) {
            return false;
        }
    }
}