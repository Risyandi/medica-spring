package com.api.employee.service;

import com.api.employee.dto.request.ChangePasswordRequest;
import com.api.employee.dto.request.InitDataRequest;
import com.api.employee.dto.request.LoginRequest;
import com.api.employee.dto.response.*;
import com.api.employee.entity.*;
import com.api.employee.repository.*;
import com.api.employee.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PerusahaanRepository perusahaanRepository;
    
    @Autowired
    private JabatanRepository jabatanRepository;
    
    @Autowired
    private DepartemenRepository departemenRepository;
    
    @Autowired
    private UnitKerjaRepository unitKerjaRepository;
    
    @Autowired
    private JenisKelaminRepository jenisKelaminRepository;
    
    @Autowired
    private PendidikanRepository pendidikanRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    /**
     * Initialize data - Create admin account for new company
     * 
     * @param request InitDataRequest containing namaAdmin and perusahaan
     * @return InitDataResponse with email, password, and profile
     * @throws Exception if company already exists or email is taken
     */
    public InitDataResponse initData(InitDataRequest request) throws Exception {
        // Validate input
        if (request.getNamaAdmin() == null || request.getNamaAdmin().trim().isEmpty()) {
            throw new Exception("Nama admin tidak boleh kosong");
        }
        
        if (request.getPerusahaan() == null || request.getPerusahaan().trim().isEmpty()) {
            throw new Exception("Nama perusahaan tidak boleh kosong");
        }
        
        // Check if company already exists
        if (perusahaanRepository.existsByNamaPerusahaan(request.getPerusahaan())) {
            throw new Exception("Perusahaan dengan nama '" + request.getPerusahaan() + "' sudah terdaftar");
        }
        
        // Generate credentials
        String baseEmail = "admin." + request.getPerusahaan()
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", "_")
                .replaceAll("[^a-z0-9_]", "");
        
        String email = baseEmail + "@company.com";
        String password = generateSecurePassword();
        String profile = "ADMIN";
        
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new Exception("Email sudah terdaftar");
        }
        
        // Create admin user
        User admin = new User();
        admin.setIdUser(UUID.randomUUID().toString());
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setNamaLengkap(request.getNamaAdmin());
        admin.setProfile(profile);
        admin.setIsActive(true);
        
        User savedAdmin = userRepository.save(admin);
        
        // Create company record
        Perusahaan perusahaan = new Perusahaan();
        perusahaan.setNamaPerusahaan(request.getPerusahaan());
        perusahaan.setAdminEmail(email);
        perusahaan.setAdminId(savedAdmin.getIdUser());
        perusahaanRepository.save(perusahaan);
        
        return new InitDataResponse(email, password, profile);
    }
    
    /**
     * User login endpoint
     * 
     * @param request LoginRequest with email, password, and profile
     * @return ApiResponse containing JWT token and user info
     * @throws Exception if credentials are invalid
     */
    public ApiResponse<LoginResponse> login(LoginRequest request) throws Exception {
        // Validate input
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new Exception("Email tidak boleh kosong");
        }
        
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new Exception("Password tidak boleh kosong");
        }
        
        if (request.getProfile() == null || request.getProfile().trim().isEmpty()) {
            throw new Exception("Profile tidak boleh kosong");
        }
        
        // Find user by email
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        
        if (user.isEmpty()) {
            throw new Exception("Email tidak ditemukan");
        }
        
        User userData = user.get();
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), userData.getPassword())) {
            throw new Exception("Password salah");
        }
        
        // Verify profile matches
        if (!request.getProfile().equalsIgnoreCase(userData.getProfile())) {
            throw new Exception("Profile tidak sesuai");
        }
        
        // Check if user is active
        if (!userData.getIsActive()) {
            throw new Exception("User tidak aktif");
        }
        
        // Generate JWT token
        String token = jwtTokenProvider.generateToken(userData.getEmail(), userData.getProfile());
        
        // Build user info response with all relationships
        UserInfoResponse info = buildUserInfoResponse(userData);
        
        // Create login response
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setInfo(info);
        
        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login berhasil")
                .hasil(loginResponse)
                .build();
    }
    
    /**
     * Change password for authenticated user
     * 
     * @param email User email address (extracted from JWT token)
     * @param request ChangePasswordRequest with old and new passwords
     * @return ApiResponse indicating success
     * @throws Exception if current password is wrong or new passwords don't match
     */
    public ApiResponse<String> changePassword(String email, ChangePasswordRequest request) throws Exception {
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Email tidak valid");
        }
        
        if (request.getPasswordAsli() == null || request.getPasswordAsli().trim().isEmpty()) {
            throw new Exception("Password asli tidak boleh kosong");
        }
        
        if (request.getPasswordBaru1() == null || request.getPasswordBaru1().trim().isEmpty()) {
            throw new Exception("Password baru tidak boleh kosong");
        }
        
        if (request.getPasswordBaru2() == null || request.getPasswordBaru2().trim().isEmpty()) {
            throw new Exception("Konfirmasi password baru tidak boleh kosong");
        }
        
        // Find user
        Optional<User> user = userRepository.findByEmail(email);
        
        if (user.isEmpty()) {
            throw new Exception("User tidak ditemukan");
        }
        
        User userData = user.get();
        
        // Verify old password
        if (!passwordEncoder.matches(request.getPasswordAsli(), userData.getPassword())) {
            throw new Exception("Password asli tidak sesuai");
        }
        
        // Verify new passwords match
        if (!request.getPasswordBaru1().equals(request.getPasswordBaru2())) {
            throw new Exception("Password baru dan konfirmasi password baru tidak sesuai");
        }
        
        // Validate password strength
        if (request.getPasswordBaru1().length() < 6) {
            throw new Exception("Password minimal 6 karakter");
        }
        
        // Prevent using same password as before
        if (passwordEncoder.matches(request.getPasswordBaru1(), userData.getPassword())) {
            throw new Exception("Password baru tidak boleh sama dengan password lama");
        }
        
        // Update password
        userData.setPassword(passwordEncoder.encode(request.getPasswordBaru1()));
        userRepository.save(userData);
        
        return ApiResponse.<String>builder()
                .success(true)
                .message("Password berhasil diubah")
                .hasil("Success")
                .build();
    }
    
    /**
     * Build UserInfoResponse from User entity
     * This method joins all related tables to get complete user information
     * 
     * @param user User entity
     * @return UserInfoResponse with all joined data
     */
    private UserInfoResponse buildUserInfoResponse(User user) {
        // Initialize all related data as empty strings
        String namaJabatan = "";
        String namaDepartemen = "";
        String namaUnitKerja = "";
        String namaJenisKelamin = "";
        String namaPendidikan = "";
        
        // Get Jabatan information
        if (user.getKdJabatan() != null) {
            Optional<Jabatan> jabatan = jabatanRepository.findById(user.getKdJabatan());
            if (jabatan.isPresent()) {
                namaJabatan = jabatan.get().getNamaJabatan();
            }
        }
        
        // Get Departemen information
        if (user.getKdDepartemen() != null) {
            Optional<Departemen> departemen = departemenRepository.findById(user.getKdDepartemen());
            if (departemen.isPresent()) {
                namaDepartemen = departemen.get().getNamaDepartemen();
            }
        }
        
        // Get Unit Kerja information
        if (user.getKdUnitKerja() != null) {
            Optional<UnitKerja> unitKerja = unitKerjaRepository.findById(user.getKdUnitKerja());
            if (unitKerja.isPresent()) {
                namaUnitKerja = unitKerja.get().getNamaUnitKerja();
            }
        }
        
        // Get Jenis Kelamin information
        if (user.getKdJenisKelamin() != null) {
            Optional<JenisKelamin> jenisKelamin = jenisKelaminRepository.findById(user.getKdJenisKelamin());
            if (jenisKelamin.isPresent()) {
                namaJenisKelamin = jenisKelamin.get().getNamaJenisKelamin();
            }
        }
        
        // Get Pendidikan information
        if (user.getKdPendidikan() != null) {
            Optional<Pendidikan> pendidikan = pendidikanRepository.findById(user.getKdPendidikan());
            if (pendidikan.isPresent()) {
                namaPendidikan = pendidikan.get().getNamaPendidikan();
            }
        }
        
        // Build and return the response object
        return UserInfoResponse.builder()
                .profile(user.getProfile())
                .idUser(user.getIdUser())
                .namaLengkap(user.getNamaLengkap())
                .tempatLahir(user.getTempatLahir())
                .tanggalLahir(user.getTanggalLahir())
                .email(user.getEmail())
                .password(user.getPassword())
                .nikUser(user.getNikUser())
                .kdJabatan(user.getKdJabatan())
                .namaJabatan(namaJabatan)
                .kdDepartemen(user.getKdDepartemen())
                .namaDepartemen(namaDepartemen)
                .kdUnitKerja(user.getKdUnitKerja())
                .namaUnitKerja(namaUnitKerja)
                .kdJenisKelamin(user.getKdJenisKelamin())
                .namaJenisKelamin(namaJenisKelamin)
                .kdPendidikan(user.getKdPendidikan())
                .namaPendidikan(namaPendidikan)
                .photo(user.getPhoto())
                .build();
    }
    
    /**
     * Generate secure random password
     * 
     * @return Random password with 12 characters combining uppercase, lowercase, numbers
     */
    private String generateSecurePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < 12; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }
        
        return password.toString();
    }
}