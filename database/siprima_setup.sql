-- SIPRIMA Database Setup Script untuk Laragon
-- Jalankan script ini di phpMyAdmin atau MySQL command line
-- Database: siprima_db

-- Buat database jika belum ada
CREATE DATABASE IF NOT EXISTS siprima_db;
USE siprima_db;

-- Drop table lama jika ada (untuk fix typo)
DROP TABLE IF EXISTS complaints;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- 1. Tabel Categories
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    icon VARCHAR(50),
    color VARCHAR(7) DEFAULT '#2980B9',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabel Users (untuk sistem lengkap)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('masyarakat', 'petugas', 'supervisor', 'admin') NOT NULL DEFAULT 'masyarakat',
    phone VARCHAR(20),
    address TEXT,
    rt_rw VARCHAR(10),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. Tabel Complaints (STRUKTUR YANG BENAR)
CREATE TABLE complaints (
    id INT PRIMARY KEY AUTO_INCREMENT,
    complaint_number VARCHAR(50) UNIQUE NOT NULL,
    reporter_name VARCHAR(100) NOT NULL,        -- ✅ BENAR: reporter_name 
    reporter_nik VARCHAR(20) NOT NULL,
    reporter_email VARCHAR(100) NOT NULL,
    reporter_phone VARCHAR(20),
    reporter_address TEXT,
    rt_rw VARCHAR(10),
    category VARCHAR(50) NOT NULL,
    priority ENUM('rendah', 'sedang', 'tinggi', 'darurat') DEFAULT 'sedang',
    status ENUM('baru', 'validasi', 'proses', 'selesai', 'ditolak') DEFAULT 'baru',
    location_address TEXT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 4. Tabel Attachments (untuk lampiran)
CREATE TABLE attachments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    complaint_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    uploaded_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (complaint_id) REFERENCES complaints(id) ON DELETE CASCADE
);

-- 5. Tabel Complaint Logs (riwayat status)
CREATE TABLE complaint_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    complaint_id INT NOT NULL,
    old_status VARCHAR(20),
    new_status VARCHAR(20) NOT NULL,
    notes TEXT,
    changed_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (complaint_id) REFERENCES complaints(id) ON DELETE CASCADE
);

-- INSERT DATA SAMPLE

-- Categories
INSERT INTO categories (name, description, icon, color) VALUES
('Infrastruktur', 'Jalan, jembatan, gedung publik', '🏗️', '#E67E22'),
('Kebersihan', 'Sampah, drainase, kebersihan lingkungan', '🗑️', '#27AE60'),
('Utilitas', 'Listrik, air, telekomunikasi', '⚡', '#F39C12'),
('Keamanan', 'Keamanan dan ketertiban', '🛡️', '#E74C3C'),
('Kesehatan', 'Fasilitas kesehatan, sanitasi', '🏥', '#9B59B6'),
('Pendidikan', 'Fasilitas pendidikan', '📚', '#3498DB'),
('Sosial', 'Masalah sosial kemasyarakatan', '👥', '#1ABC9C');

-- Users Sample
INSERT INTO users (username, email, password_hash, full_name, role, phone) VALUES
('admin', 'admin@desatarabbi.go.id', '$2a$10$dummy_hash', 'Administrator Desa', 'admin', '081234567890'),
('supervisor', 'supervisor@desatarabbi.go.id', '$2a$10$dummy_hash', 'Supervisor Desa Tarabbi', 'supervisor', '081234567891'),
('febry', 'febry@desatarabbi.go.id', '$2a$10$dummy_hash', 'Febriansyah', 'petugas', '081234567892'),
('budi', 'budi@email.com', '$2a$10$dummy_hash', 'Budi Santoso', 'masyarakat', '081234567893'),
('siti', 'siti@email.com', '$2a$10$dummy_hash', 'Siti Aminah', 'masyarakat', '081234567894');

-- Complaints Sample
INSERT INTO complaints (
    complaint_number, reporter_name, reporter_nik, reporter_email, 
    reporter_phone, reporter_address, rt_rw, category, priority, 
    location_address, title, description, status
) VALUES 
('ADU20250618001', 'Siti Aminah', '1234567890123456', 'siti@email.com', 
 '081234567890', 'Jl. Mawar Raya No. 123', '02/01', 'Infrastruktur', 'darurat',
 'Jl. Mawar Raya', 'Jalan Rusak Parah', 'Jalan rusak dengan lubang besar yang sangat berbahaya untuk kendaraan. Sudah ada beberapa motor yang jatuh. Mohon segera diperbaiki.', 'baru'),

('ADU20250618002', 'Budi Santoso', '1234567890123457', 'budi@email.com', 
 '081234567891', 'Jl. Melati No. 45', '03/02', 'Utilitas', 'sedang',
 'Jl. Melati', 'Lampu Jalan Mati', 'Lampu jalan sudah mati sejak 3 hari yang lalu. Jalan menjadi gelap dan berbahaya di malam hari.', 'baru'),

('ADU20250618003', 'Ratna Sari', '1234567890123458', 'ratna@email.com', 
 '081234567892', 'Jl. Anggrek No. 67', '01/01', 'Kebersihan', 'rendah',
 'Jl. Anggrek', 'Sampah Tidak Diangkut', 'Sampah sudah menumpuk 2 minggu tidak diangkut. Mulai menimbulkan bau dan lalat.', 'selesai'),

('ADU20250618004', 'Ahmad Yusuf', '1234567890123459', 'ahmad@email.com', 
 '081234567893', 'Jl. Kenanga No. 89', '04/03', 'Keamanan', 'tinggi',
 'Jl. Kenanga', 'Pencurian Motor', 'Motor saya dicuri di depan rumah tadi malam. Mohon ditingkatkan patroli keamanan.', 'proses'),

('ADU20250618005', 'Dewi Sartika', '1234567890123460', 'dewi@email.com', 
 '081234567894', 'Jl. Dahlia No. 12', '02/02', 'Kesehatan', 'sedang',
 'Jl. Dahlia', 'Saluran Air Kotor', 'Saluran air di depan rumah tersumbat dan menimbulkan genangan air kotor. Khawatir jadi sarang nyamuk.', 'validasi');

-- Complaint Logs Sample
INSERT INTO complaint_logs (complaint_id, old_status, new_status, notes, changed_by) VALUES
(1, NULL, 'baru', 'Aduan diterima sistem', 'system'),
(1, 'baru', 'validasi', 'Sedang divalidasi oleh admin', 'admin'),
(2, NULL, 'baru', 'Aduan diterima sistem', 'system'),
(3, NULL, 'baru', 'Aduan diterima sistem', 'system'),
(3, 'baru', 'proses', 'Sedang ditangani petugas kebersihan', 'febry'),
(3, 'proses', 'selesai', 'Sampah telah diangkut', 'febry'),
(4, NULL, 'baru', 'Aduan diterima sistem', 'system'),
(4, 'baru', 'proses', 'Laporan diteruskan ke polsek', 'supervisor'),
(5, NULL, 'baru', 'Aduan diterima sistem', 'system'),
(5, 'baru', 'validasi', 'Sedang diverifikasi lokasi', 'admin');

-- Verifikasi hasil
SELECT 'Database Setup Complete!' as Status;
SELECT COUNT(*) as total_categories FROM categories;
SELECT COUNT(*) as total_users FROM users;
SELECT COUNT(*) as total_complaints FROM complaints;
SELECT COUNT(*) as total_logs FROM complaint_logs;

-- Tampilkan struktur table complaints untuk verifikasi
DESCRIBE complaints;

