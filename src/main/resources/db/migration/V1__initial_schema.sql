-- Create tables for Employee Management System

-- Roles table for access control
CREATE TABLE IF NOT EXISTS roles (
    id_role INT AUTO_INCREMENT PRIMARY KEY,
    nama_role VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default roles
INSERT INTO roles (nama_role, description) VALUES 
('ADMIN', 'Administrator with full access'),
('HR_MANAGER', 'HR Manager with employee management access'),
('MANAGER', 'Department Manager'),
('STAFF', 'Regular staff member');

-- Jabatan table
CREATE TABLE IF NOT EXISTS jabatan (
    kd_jabatan INT AUTO_INCREMENT PRIMARY KEY,
    nama_jabatan VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36)
);

-- Departemen table
CREATE TABLE IF NOT EXISTS departemen (
    kd_departemen INT AUTO_INCREMENT PRIMARY KEY,
    nama_departemen VARCHAR(100) NOT NULL UNIQUE
);

-- Unit Kerja table
CREATE TABLE IF NOT EXISTS unit_kerja (
    kd_unit_kerja INT AUTO_INCREMENT PRIMARY KEY,
    nama_unit_kerja VARCHAR(100) NOT NULL UNIQUE
);

-- Jenis Kelamin table
CREATE TABLE IF NOT EXISTS jenis_kelamin (
    kd_jenis_kelamin INT AUTO_INCREMENT PRIMARY KEY,
    nama_jenis_kelamin VARCHAR(50) NOT NULL UNIQUE
);

-- Pendidikan table
CREATE TABLE IF NOT EXISTS pendidikan (
    kd_pendidikan INT AUTO_INCREMENT PRIMARY KEY,
    nama_pendidikan VARCHAR(100) NOT NULL UNIQUE
);

-- Perusahaan table
CREATE TABLE IF NOT EXISTS perusahaan (
    kd_perusahaan INT AUTO_INCREMENT PRIMARY KEY,
    nama_perusahaan VARCHAR(100) NOT NULL UNIQUE,
    admin_email VARCHAR(100) UNIQUE,
    admin_id VARCHAR(36)
);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id_user VARCHAR(36) PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nik_user VARCHAR(20),
    nama_lengkap VARCHAR(100) NOT NULL,
    tempat_lahir VARCHAR(100),
    tanggal_lahir INT,
    kd_jabatan INT,
    kd_departemen INT,
    kd_unit_kerja INT,
    kd_jenis_kelamin INT,
    kd_pendidikan INT,
    profile VARCHAR(50),
    photo VARCHAR(255),
    photo_uploaded_at TIMESTAMP,
    photo_content_type VARCHAR(100),
    photo_size BIGINT,
    last_login_at TIMESTAMP,
    last_login_ip VARCHAR(45),
    password_changed_at TIMESTAMP,
    failed_login_attempts INT DEFAULT 0,
    account_locked_until TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    FOREIGN KEY (kd_jabatan) REFERENCES jabatan(kd_jabatan) ON DELETE SET NULL,
    FOREIGN KEY (kd_departemen) REFERENCES departemen(kd_departemen) ON DELETE SET NULL,
    FOREIGN KEY (kd_unit_kerja) REFERENCES unit_kerja(kd_unit_kerja) ON DELETE SET NULL,
    FOREIGN KEY (kd_jenis_kelamin) REFERENCES jenis_kelamin(kd_jenis_kelamin) ON DELETE SET NULL,
    FOREIGN KEY (kd_pendidikan) REFERENCES pendidikan(kd_pendidikan) ON DELETE SET NULL
);

-- User Roles mapping table
CREATE TABLE IF NOT EXISTS user_roles (
    id_user VARCHAR(36),
    id_role INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    PRIMARY KEY (id_user, id_role),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_role) REFERENCES roles(id_role) ON DELETE CASCADE
);

-- User Status History table
CREATE TABLE IF NOT EXISTS user_status_history (
    id_history INT AUTO_INCREMENT PRIMARY KEY,
    id_user VARCHAR(36),
    status_before BOOLEAN,
    status_after BOOLEAN,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(36),
    keterangan VARCHAR(255),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (changed_by) REFERENCES users(id_user) ON DELETE SET NULL
);

-- Insert sample data for Jenis Kelamin
INSERT INTO jenis_kelamin (nama_jenis_kelamin) VALUES 
('Laki-laki'),
('Perempuan');

-- Insert sample data for Pendidikan
INSERT INTO pendidikan (nama_pendidikan) VALUES 
('SD'),
('SMP'),
('SMA'),
('D3'),
('S1'),
('S2'),
('S3');

-- Insert sample data for Jabatan
INSERT INTO jabatan (nama_jabatan) VALUES 
('Manager'),
('Supervisor'),
('Staff'),
('Koordinator'),
('Direktur'),
('Kepala Bagian');

-- Insert sample data for Departemen
INSERT INTO departemen (nama_departemen) VALUES 
('HR & Administrasi'),
('Finance'),
('IT'),
('Marketing'),
('Operations'),
('Customer Service');

-- Insert sample data for Unit Kerja
INSERT INTO unit_kerja (nama_unit_kerja) VALUES 
('Kantor Pusat'),
('Cabang Jakarta'),
('Cabang Surabaya'),
('Cabang Bandung'),
('Cabang Medan'),
('Kantor Regional');

-- Create indexes for better query performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_profile ON users(profile);
CREATE INDEX idx_users_is_active ON users(is_active);
CREATE INDEX idx_users_kd_departemen ON users(kd_departemen);
CREATE INDEX idx_users_kd_jabatan ON users(kd_jabatan);
CREATE INDEX idx_perusahaan_nama ON perusahaan(nama_perusahaan);


-- Absensi table
CREATE TABLE IF NOT EXISTS absensi (
    id_absensi INT AUTO_INCREMENT PRIMARY KEY,
    id_user VARCHAR(36) NOT NULL,
    tanggal DATE NOT NULL,
    jam_masuk TIME,
    jam_keluar TIME,
    status VARCHAR(20),
    keterangan VARCHAR(255),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    device_info VARCHAR(255),
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id_user) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id_user) ON DELETE SET NULL
);

-- Jenis Cuti table
CREATE TABLE IF NOT EXISTS jenis_cuti (
    id_jenis_cuti INT AUTO_INCREMENT PRIMARY KEY,
    nama_jenis_cuti VARCHAR(50) NOT NULL UNIQUE,
    max_hari INT NOT NULL,
    keterangan VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default jenis cuti
INSERT INTO jenis_cuti (nama_jenis_cuti, max_hari, keterangan) VALUES 
('Tahunan', 12, 'Cuti tahunan regular'),
('Melahirkan', 90, 'Cuti untuk melahirkan'),
('Sakit', 14, 'Cuti karena sakit dengan surat dokter'),
('Penting', 7, 'Cuti karena keperluan penting');

-- Cuti table
CREATE TABLE IF NOT EXISTS cuti (
    id_cuti INT AUTO_INCREMENT PRIMARY KEY,
    id_user VARCHAR(36) NOT NULL,
    id_jenis_cuti INT NOT NULL,
    tanggal_pengajuan DATE NOT NULL,
    tanggal_mulai DATE NOT NULL,
    tanggal_selesai DATE NOT NULL,
    status VARCHAR(20),
    keterangan VARCHAR(255),
    dokumen_pendukung VARCHAR(255),
    approved_by VARCHAR(36),
    approved_at TIMESTAMP,
    rejected_by VARCHAR(36),
    rejected_at TIMESTAMP,
    rejection_reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_jenis_cuti) REFERENCES jenis_cuti(id_jenis_cuti),
    FOREIGN KEY (approved_by) REFERENCES users(id_user) ON DELETE SET NULL,
    FOREIGN KEY (rejected_by) REFERENCES users(id_user) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id_user) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id_user) ON DELETE SET NULL
);

-- Komponen Gaji table
CREATE TABLE IF NOT EXISTS komponen_gaji (
    id_komponen INT AUTO_INCREMENT PRIMARY KEY,
    nama_komponen VARCHAR(100) NOT NULL,
    jenis ENUM('TUNJANGAN', 'POTONGAN') NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default komponen gaji
INSERT INTO komponen_gaji (nama_komponen, jenis) VALUES 
('Gaji Pokok', 'TUNJANGAN'),
('Tunjangan Transport', 'TUNJANGAN'),
('Tunjangan Makan', 'TUNJANGAN'),
('BPJS Kesehatan', 'POTONGAN'),
('BPJS Ketenagakerjaan', 'POTONGAN'),
('PPh 21', 'POTONGAN');

-- Gaji table
CREATE TABLE IF NOT EXISTS gaji (
    id_gaji INT AUTO_INCREMENT PRIMARY KEY,
    id_user VARCHAR(36) NOT NULL,
    bulan INT NOT NULL,
    tahun INT NOT NULL,
    gaji_pokok DECIMAL(15,2) NOT NULL,
    total_tunjangan DECIMAL(15,2) DEFAULT 0,
    total_potongan DECIMAL(15,2) DEFAULT 0,
    total_lembur DECIMAL(15,2) DEFAULT 0,
    jumlah_bersih DECIMAL(15,2) NOT NULL,
    tanggal_pembayaran DATE,
    status_pembayaran VARCHAR(20) DEFAULT 'PENDING',
    keterangan VARCHAR(255),
    slip_gaji_path VARCHAR(255),
    approved_by VARCHAR(36),
    approved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES users(id_user) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id_user) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id_user) ON DELETE SET NULL
);

-- Detail Gaji table for individual components
CREATE TABLE IF NOT EXISTS detail_gaji (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_gaji INT NOT NULL,
    id_komponen INT NOT NULL,
    jumlah DECIMAL(15,2) NOT NULL,
    keterangan VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_gaji) REFERENCES gaji(id_gaji) ON DELETE CASCADE,
    FOREIGN KEY (id_komponen) REFERENCES komponen_gaji(id_komponen)
);

-- Riwayat Jabatan table
CREATE TABLE IF NOT EXISTS riwayat_jabatan (
    id_riwayat INT AUTO_INCREMENT PRIMARY KEY,
    id_user VARCHAR(36) NOT NULL,
    kd_jabatan INT,
    kd_departemen INT,
    kd_unit_kerja INT,
    tanggal_mulai DATE NOT NULL,
    tanggal_selesai DATE,
    sk_number VARCHAR(100),
    gaji_pokok_lama DECIMAL(15,2),
    gaji_pokok_baru DECIMAL(15,2),
    keterangan VARCHAR(255),
    dokumen_sk VARCHAR(255),
    approved_by VARCHAR(36),
    approved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (kd_jabatan) REFERENCES jabatan(kd_jabatan) ON DELETE SET NULL,
    FOREIGN KEY (kd_departemen) REFERENCES departemen(kd_departemen) ON DELETE SET NULL,
    FOREIGN KEY (kd_unit_kerja) REFERENCES unit_kerja(kd_unit_kerja) ON DELETE SET NULL,
    FOREIGN KEY (approved_by) REFERENCES users(id_user) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id_user) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id_user) ON DELETE SET NULL
);

-- Status Absen table
CREATE TABLE IF NOT EXISTS status_absen (
    kd_status INT AUTO_INCREMENT PRIMARY KEY,
    nama_status VARCHAR(100) NOT NULL UNIQUE
);

-- Shift Kerja table
CREATE TABLE IF NOT EXISTS shift_kerja (
    id_shift INT AUTO_INCREMENT PRIMARY KEY,
    nama_shift VARCHAR(50) NOT NULL,
    jam_masuk TIME NOT NULL,
    jam_keluar TIME NOT NULL,
    toleransi_keterlambatan INT DEFAULT 15, -- in minutes
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default shifts
INSERT INTO shift_kerja (nama_shift, jam_masuk, jam_keluar) VALUES 
('Regular', '08:00:00', '17:00:00'),
('Pagi', '06:00:00', '14:00:00'),
('Siang', '14:00:00', '22:00:00'),
('Malam', '22:00:00', '06:00:00');

-- Presensi table
CREATE TABLE IF NOT EXISTS presensi (
    id_presensi INT AUTO_INCREMENT PRIMARY KEY,
    id_user VARCHAR(36) NOT NULL,
    id_shift INT,
    tgl_absensi INT NOT NULL, -- Format: YYYYMMDD
    jam_masuk VARCHAR(8),
    jam_keluar VARCHAR(8),
    kd_status INT,
    latitude_masuk DECIMAL(10,8),
    longitude_masuk DECIMAL(11,8),
    latitude_keluar DECIMAL(10,8),
    longitude_keluar DECIMAL(11,8),
    device_info_masuk VARCHAR(255),
    device_info_keluar VARCHAR(255),
    ip_address_masuk VARCHAR(45),
    ip_address_keluar VARCHAR(45),
    foto_masuk VARCHAR(255),
    foto_keluar VARCHAR(255),
    keterlambatan INT, -- in minutes
    pulang_cepat INT, -- in minutes
    total_jam_kerja INT, -- in minutes
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_shift) REFERENCES shift_kerja(id_shift) ON DELETE SET NULL,
    FOREIGN KEY (kd_status) REFERENCES status_absen(kd_status) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id_user) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id_user) ON DELETE SET NULL
);

-- Create additional indexes for performance
CREATE INDEX idx_presensi_tanggal ON presensi(tgl_absensi);
CREATE INDEX idx_presensi_user_tanggal ON presensi(id_user, tgl_absensi);
CREATE INDEX idx_absensi_tanggal ON absensi(tanggal);
CREATE INDEX idx_absensi_user_tanggal ON absensi(id_user, tanggal);
CREATE INDEX idx_cuti_tanggal ON cuti(tanggal_mulai, tanggal_selesai);
CREATE INDEX idx_cuti_user ON cuti(id_user);
CREATE INDEX idx_gaji_periode ON gaji(tahun, bulan);
CREATE INDEX idx_gaji_user ON gaji(id_user);
CREATE INDEX idx_riwayat_jabatan_user ON riwayat_jabatan(id_user);
CREATE INDEX idx_user_roles_user ON user_roles(id_user);