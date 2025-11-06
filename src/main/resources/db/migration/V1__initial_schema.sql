-- Create tables for Employee Management System

-- Jabatan table
CREATE TABLE IF NOT EXISTS jabatan (
    kd_jabatan INT AUTO_INCREMENT PRIMARY KEY,
    nama_jabatan VARCHAR(100) NOT NULL UNIQUE
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
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (kd_jabatan) REFERENCES jabatan(kd_jabatan) ON DELETE SET NULL,
    FOREIGN KEY (kd_departemen) REFERENCES departemen(kd_departemen) ON DELETE SET NULL,
    FOREIGN KEY (kd_unit_kerja) REFERENCES unit_kerja(kd_unit_kerja) ON DELETE SET NULL,
    FOREIGN KEY (kd_jenis_kelamin) REFERENCES jenis_kelamin(kd_jenis_kelamin) ON DELETE SET NULL,
    FOREIGN KEY (kd_pendidikan) REFERENCES pendidikan(kd_pendidikan) ON DELETE SET NULL
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