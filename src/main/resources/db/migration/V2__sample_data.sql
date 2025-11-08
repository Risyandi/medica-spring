-- Insert sample data for Status Absen
INSERT INTO status_absen (nama_status) VALUES 
('Hadir'),
('Sakit'),
('Izin'),
('Cuti'),
('Tanpa Keterangan'),
('Work From Home'),
('Dinas Luar');

-- Insert sample data for Perusahaan
INSERT INTO perusahaan (nama_perusahaan, admin_email) VALUES 
('PT Medica Health Indonesia', 'admin@medicahealth.com');

-- Insert sample users with bcrypt encoded passwords (password: 'password123')
INSERT INTO users (
    id_user, 
    email, 
    password, 
    nik_user, 
    nama_lengkap, 
    tempat_lahir, 
    tanggal_lahir, 
    kd_jabatan, 
    kd_departemen, 
    kd_unit_kerja, 
    kd_jenis_kelamin, 
    kd_pendidikan, 
    profile, 
    is_active
) VALUES 
-- Admin user
('1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p', 'admin@medicahealth.com', '$2a$10$lIpXeZy1STGvKmB.xJKY0.F.JuV.kT1H5vYUEHMB1YJbEbyO8PJlG', 'ADM001', 'Administrator', 'Jakarta', 19900101, 5, 1, 1, 1, 5, 'ADMIN', true),
-- HR Manager
('2b3c4d5e-6f7g-8h9i-0j1k-2l3m4n5o6p7q', 'hr.manager@medicahealth.com', '$2a$10$lIpXeZy1STGvKmB.xJKY0.F.JuV.kT1H5vYUEHMB1YJbEbyO8PJlG', 'HR001', 'Sarah Williams', 'Bandung', 19910315, 1, 1, 1, 2, 5, 'HR_MANAGER', true),
-- IT Staff
('3c4d5e6f-7g8h-9i0j-1k2l-3m4n5o6p7q8r', 'it.staff@medicahealth.com', '$2a$10$lIpXeZy1STGvKmB.xJKY0.F.JuV.kT1H5vYUEHMB1YJbEbyO8PJlG', 'IT001', 'John Tech', 'Surabaya', 19920520, 3, 3, 1, 1, 5, 'STAFF', true),
-- Finance Manager
('4d5e6f7g-8h9i-0j1k-2l3m-4n5o6p7q8r9s', 'finance.manager@medicahealth.com', '$2a$10$lIpXeZy1STGvKmB.xJKY0.F.JuV.kT1H5vYUEHMB1YJbEbyO8PJlG', 'FIN001', 'Michael Finance', 'Jakarta', 19880725, 1, 2, 1, 1, 6, 'MANAGER', true);

-- Update Perusahaan admin_id
UPDATE perusahaan 
SET admin_id = '1a2b3c4d-5e6f-7g8h-9i0j-1k2l3m4n5o6p' 
WHERE admin_email = 'admin@medicahealth.com';

-- Insert sample Absensi data
INSERT INTO absensi (id_user, tanggal, jam_masuk, jam_keluar, status, keterangan) VALUES 
('2b3c4d5e-6f7g-8h9i-0j1k-2l3m4n5o6p7q', CURDATE(), '08:00:00', '17:00:00', 'Hadir', 'Tepat waktu'),
('3c4d5e6f-7g8h-9i0j-1k2l-3m4n5o6p7q8r', CURDATE(), '08:15:00', '17:30:00', 'Hadir', 'Sedikit terlambat'),
('4d5e6f7g-8h9i-0j1k-2l3m-4n5o6p7q8r9s', CURDATE(), '07:45:00', '17:00:00', 'Hadir', 'Datang lebih awal');

-- Insert sample Cuti data
-- Note: V1 schema uses `id_jenis_cuti` (FK) instead of a string `jenis_cuti`. Use the correct id values inserted in V1 (1=Tahunan,2=Melahirkan,3=Sakit,4=Penting).
INSERT INTO cuti (id_user, id_jenis_cuti, tanggal_pengajuan, tanggal_mulai, tanggal_selesai, status, keterangan) VALUES 
('2b3c4d5e-6f7g-8h9i-0j1k-2l3m4n5o6p7q', 1, DATE_SUB(CURDATE(), INTERVAL 7 DAY), DATE_ADD(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'Disetujui', 'Cuti tahunan'),
('3c4d5e6f-7g8h-9i0j-1k2l-3m4n5o6p7q8r', 3, DATE_SUB(CURDATE(), INTERVAL 14 DAY), DATE_ADD(CURDATE(), INTERVAL 7 DAY), DATE_ADD(CURDATE(), INTERVAL 8 DAY), 'Disetujui', 'Cuti sakit dengan surat dokter');

-- Insert sample Gaji data
-- V1 `gaji` table expects columns like gaji_pokok, total_tunjangan, total_potongan, total_lembur, jumlah_bersih. Populate those accordingly.
INSERT INTO gaji (id_user, bulan, tahun, gaji_pokok, total_tunjangan, total_potongan, total_lembur, jumlah_bersih, tanggal_pembayaran, keterangan) VALUES 
('2b3c4d5e-6f7g-8h9i-0j1k-2l3m4n5o6p7q', MONTH(CURDATE()), YEAR(CURDATE()), 15000000.00, 0.00, 0.00, 0.00, 15000000.00, CURDATE(), 'Gaji bulan ini'),
('3c4d5e6f-7g8h-9i0j-1k2l-3m4n5o6p7q8r', MONTH(CURDATE()), YEAR(CURDATE()), 8000000.00, 0.00, 0.00, 0.00, 8000000.00, CURDATE(), 'Gaji bulan ini'),
('4d5e6f7g-8h9i-0j1k-2l3m-4n5o6p7q8r9s', MONTH(CURDATE()), YEAR(CURDATE()), 20000000.00, 0.00, 0.00, 0.00, 20000000.00, CURDATE(), 'Gaji bulan ini');

-- Insert sample Riwayat Jabatan data
INSERT INTO riwayat_jabatan (id_user, kd_jabatan, tanggal_mulai, tanggal_selesai, keterangan) VALUES 
('2b3c4d5e-6f7g-8h9i-0j1k-2l3m4n5o6p7q', 3, DATE_SUB(CURDATE(), INTERVAL 2 YEAR), DATE_SUB(CURDATE(), INTERVAL 1 YEAR), 'Posisi awal'),
('2b3c4d5e-6f7g-8h9i-0j1k-2l3m4n5o6p7q', 1, DATE_SUB(CURDATE(), INTERVAL 1 YEAR), NULL, 'Promosi ke Manager'),
('3c4d5e6f-7g8h-9i0j-1k2l-3m4n5o6p7q8r', 3, DATE_SUB(CURDATE(), INTERVAL 1 YEAR), NULL, 'Posisi awal');

-- Insert sample Presensi data
INSERT INTO presensi (id_user, tgl_absensi, jam_masuk, jam_keluar, kd_status) VALUES 
('2b3c4d5e-6f7g-8h9i-0j1k-2l3m4n5o6p7q', CAST(DATE_FORMAT(CURDATE(), '%Y%m%d') AS UNSIGNED), '08:00', '17:00', 1),
('3c4d5e6f-7g8h-9i0j-1k2l-3m4n5o6p7q8r', CAST(DATE_FORMAT(CURDATE(), '%Y%m%d') AS UNSIGNED), '08:15', '17:30', 1),
('4d5e6f7g-8h9i-0j1k-2l3m-4n5o6p7q8r9s', CAST(DATE_FORMAT(CURDATE(), '%Y%m%d') AS UNSIGNED), '07:45', '17:00', 1);