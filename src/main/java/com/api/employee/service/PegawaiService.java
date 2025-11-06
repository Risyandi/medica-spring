package com.api.employee.service;

import com.api.employee.dto.request.AddPegawaiRequest;
import com.api.employee.dto.request.UpdatePegawaiRequest;
import com.api.employee.dto.response.*;
import com.api.employee.entity.*;
import com.api.employee.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.api.employee.exception.ValidationException;

@Service
public class PegawaiService {
    
    @Autowired
    private UserRepository userRepository;
    
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
    
    @Value("${app.upload-dir}")
    private String uploadDir;
    
    // Get Jabatan Combo
    public List<ComboResponse> getJabatanCombo() {
        return jabatanRepository.findAll()
                .stream()
                .map(j -> ComboResponse.builder()
                        .kd(j.getKdJabatan())
                        .nama(j.getNamaJabatan())
                        .build())
                .collect(Collectors.toList());
    }
    
    // Get Departemen Combo
    public List<ComboResponse> getDepartemenCombo() {
        return departemenRepository.findAll()
                .stream()
                .map(d -> ComboResponse.builder()
                        .kd(d.getKdDepartemen())
                        .nama(d.getNamaDepartemen())
                        .build())
                .collect(Collectors.toList());
    }
    
    // Get Pendidikan Combo
    public List<ComboResponse> getPendidikanCombo() {
        return pendidikanRepository.findAll()
                .stream()
                .map(p -> ComboResponse.builder()
                        .kd(p.getKdPendidikan())
                        .nama(p.getNamaPendidikan())
                        .build())
                .collect(Collectors.toList());
    }
    
    // Get Jenis Kelamin Combo
    public List<ComboResponse> getJenisKelaminCombo() {
        return jenisKelaminRepository.findAll()
                .stream()
                .map(jk -> ComboResponse.builder()
                        .kd(jk.getKdJenisKelamin())
                        .nama(jk.getNamaJenisKelamin())
                        .build())
                .collect(Collectors.toList());
    }
    
    // Get Unit Kerja Combo
    public List<ComboResponse> getUnitKerjaCombo() {
        return unitKerjaRepository.findAll()
                .stream()
                .map(uk -> ComboResponse.builder()
                        .kd(uk.getKdUnitKerja())
                        .nama(uk.getNamaUnitKerja())
                        .build())
                .collect(Collectors.toList());
    }
    
    // Get Departemen HRD - All users in HRD department with jabatan info
    public List<DepartemenHrdResponse> getDepartemenHrd() throws Exception {
        // Assuming HRD department ID is 1, adjust as needed
        Integer hrdDepartemenId = 1;
        
        List<User> hrdUsers = userRepository.findAll()
                .stream()
                .filter(u -> hrdDepartemenId.equals(u.getKdDepartemen()))
                .collect(Collectors.toList());
        
        return hrdUsers.stream()
                .map(user -> {
                    String namaJabatan = "";
                    if (user.getKdJabatan() != null) {
                        Optional<Jabatan> jabatan = jabatanRepository.findById(user.getKdJabatan());
                        namaJabatan = jabatan.map(Jabatan::getNamaJabatan).orElse("");
                    }
                    
                    return DepartemenHrdResponse.builder()
                            .namaLengkap(user.getNamaLengkap())
                            .kdJabatan(user.getKdJabatan())
                            .namaJabatan(namaJabatan)
                            .build();
                })
                .collect(Collectors.toList());
    }
    
    // Get All Employees
    public List<UserInfoResponse> getAllPegawai() {
        return userRepository.findAll()
                .stream()
                .map(this::buildUserInfoResponse)
                .collect(Collectors.toList());
    }
    
    // Add Employee
    public ApiResponse<String> addPegawai(AddPegawaiRequest request) {
        if (request.getPassword() == null || request.getPasswordC() == null) {
            throw new ValidationException("Password dan konfirmasi password harus diisi");
        }
        if (!request.getPassword().equals(request.getPasswordC())) {
            throw new ValidationException("Password dan konfirmasi password tidak sesuai");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email sudah terdaftar");
        }
        
        // Validate minimum password length
        if (request.getPassword().length() < 6) {
            throw new RuntimeException("Password minimal 6 karakter");
        }
        
        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNamaLengkap(request.getNamaLengkap());
        user.setTempatLahir(request.getTempatLahir());
        user.setTanggalLahir(request.getTanggalLahir());
        user.setKdJenisKelamin(request.getKdJenisKelamin());
        user.setKdPendidikan(request.getKdPendidikan());
        user.setKdJabatan(request.getKdJabatan());
        user.setKdDepartemen(request.getKdDepartemen());
        user.setKdUnitKerja(request.getKdUnitKerja());
        user.setProfile("PEGAWAI");
        user.setIsActive(true);
        
        userRepository.save(user);
        
        return ApiResponse.<String>builder()
                .success(true)
                .message("Pegawai berhasil ditambahkan")
                .hasil("Success")
                .build();
    }
    
    // Update Employee
    public ApiResponse<String> updatePegawai(UpdatePegawaiRequest request) {
        Optional<User> userOpt = userRepository.findById(request.getIdUser());
    
        if (userOpt.isEmpty()) {
            throw new ValidationException("Pegawai tidak ditemukan");
        }
    
        User user = userOpt.get();
    
        // Validate passwords match
        if (!request.getPassword().equals(request.getPasswordC())) {
            throw new ValidationException("Password dan konfirmasi password tidak sesuai");
        }
    
        // Validate minimum password length
        if (request.getPassword().length() < 6) {
            throw new ValidationException("Password minimal 6 karakter");
        }
    
        // Check if new email already exists (and it's different from current)
        if (!user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email sudah terdaftar");
        }
    
        // Update user data
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNamaLengkap(request.getNamaLengkap());
        user.setTempatLahir(request.getTempatLahir());
        user.setTanggalLahir(request.getTanggalLahir());
        user.setKdJenisKelamin(request.getKdJenisKelamin());
        user.setKdPendidikan(request.getKdPendidikan());
        user.setKdJabatan(request.getKdJabatan());
        user.setKdDepartemen(request.getKdDepartemen());
        user.setKdUnitKerja(request.getKdUnitKerja());
    
        userRepository.save(user);
    
        return ApiResponse.<String>builder()
                .success(true)
                .message("Data pegawai berhasil diubah")
                .hasil("Success")
                .build();
    }
    
    // Update Photo by Admin
    public ApiResponse<String> adminUpdatePhoto(String idUser, String namaFile, MultipartFile file) throws IOException {
        Optional<User> userOpt = userRepository.findById(idUser);
    
        if (userOpt.isEmpty()) {
            throw new ValidationException("Pegawai tidak ditemukan");
        }
    
        User user = userOpt.get();
    
        // Save file
        String photoPath = saveFile(namaFile, file);
        user.setPhoto(photoPath);
    
        userRepository.save(user);
    
        return ApiResponse.<String>builder()
                .success(true)
                .message("Foto pegawai berhasil diubah")
                .hasil("Success")
                .build();
    }
    
    // Update Photo by User
    public ApiResponse<String> updateOwnPhoto(String idUser, String namaFile, MultipartFile file) throws IOException {
        Optional<User> userOpt = userRepository.findById(idUser);
    
        if (userOpt.isEmpty()) {
            throw new ValidationException("User tidak ditemukan");
        }
    
        User user = userOpt.get();
    
        // Save file
        String photoPath = saveFile(namaFile, file);
        user.setPhoto(photoPath);
    
        userRepository.save(user);
    
        return ApiResponse.<String>builder()
                .success(true)
                .message("Foto profil berhasil diubah")
                .hasil("Success")
                .build();
    }
    
    // Save file to disk
    private String saveFile(String fileName, MultipartFile file) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
        Path filePath = uploadPath.resolve(uniqueFileName);
        
        // Save file
        Files.copy(file.getInputStream(), filePath);
        
        return "/uploads/" + uniqueFileName;
    }
    
    // Build UserInfoResponse from User entity
    private UserInfoResponse buildUserInfoResponse(User user) {
        String namaJabatan = "";
        String namaDepartemen = "";
        String namaUnitKerja = "";
        String namaJenisKelamin = "";
        String namaPendidikan = "";
    
        if (user.getKdJabatan() != null) {
            Optional<Jabatan> jabatan = jabatanRepository.findById(user.getKdJabatan());
            namaJabatan = jabatan.map(Jabatan::getNamaJabatan).orElse("");
        }
    
        if (user.getKdDepartemen() != null) {
            Optional<Departemen> departemen = departemenRepository.findById(user.getKdDepartemen());
            namaDepartemen = departemen.map(Departemen::getNamaDepartemen).orElse("");
        }
    
        if (user.getKdUnitKerja() != null) {
            Optional<UnitKerja> unitKerja = unitKerjaRepository.findById(user.getKdUnitKerja());
            namaUnitKerja = unitKerja.map(UnitKerja::getNamaUnitKerja).orElse("");
        }
    
        if (user.getKdJenisKelamin() != null) {
            Optional<JenisKelamin> jenisKelamin = jenisKelaminRepository.findById(user.getKdJenisKelamin());
            namaJenisKelamin = jenisKelamin.map(JenisKelamin::getNamaJenisKelamin).orElse("");
        }
    
        if (user.getKdPendidikan() != null) {
            Optional<Pendidikan> pendidikan = pendidikanRepository.findById(user.getKdPendidikan());
            namaPendidikan = pendidikan.map(Pendidikan::getNamaPendidikan).orElse("");
        }
    
        return UserInfoResponse.builder()
                .profile(user.getProfile())
                .idUser(user.getIdUser())
                .namaLengkap(user.getNamaLengkap())
                .tempatLahir(user.getTempatLahir())
                .tanggalLahir(user.getTanggalLahir())
                .email(user.getEmail())
                // .password(user.getPassword()) // REMOVE THIS LINE
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
}