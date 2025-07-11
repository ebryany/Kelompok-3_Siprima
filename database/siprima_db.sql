portfolio_adminportfolio_adminsiprima_dbusers-- ==============================================
-- SIPRIMA DESA TARABBI - DATABASE SCHEMA
-- Sistem Pengaduan Masyarakat
-- Version: 1.0
-- Author: Febriansyah
-- Date: 16 Juni 2025
-- ==============================================

-- Buat database
CREATE DATABASE IF NOT EXISTS siprima_db;
USE siprima_db;

-- Set charset
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ==============================================
-- TABEL USERS (Pengguna Sistem)
-- ==============================================
DROP TABLE IF EXISTS users;
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
    nik VARCHAR(16),
    photo_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================
-- TABEL CATEGORIES (Kategori Aduan)
-- ==============================================
DROP TABLE IF EXISTS categories;
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    icon VARCHAR(50),
    color VARCHAR(7) DEFAULT '#2980B9',
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_active (is_active),
    INDEX idx_sort (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================
-- TABEL COMPLAINTS (Aduan)
-- ==============================================
DROP TABLE IF EXISTS complaints;
CREATE TABLE complaints (
    id INT PRIMARY KEY AUTO_INCREMENT,
    complaint_number VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    category_id INT NOT NULL,
    reporter_id INT NOT NULL,
    assigned_to INT NULL,
    priority ENUM('rendah', 'sedang', 'tinggi', 'darurat') DEFAULT 'sedang',
    status ENUM('baru', 'validasi', 'proses', 'selesai', 'ditolak') DEFAULT 'baru',
    location_address TEXT NOT NULL,
    rt_rw VARCHAR(10),
    latitude DECIMAL(10, 8) NULL,
    longitude DECIMAL(11, 8) NULL,
    expected_completion DATE NULL,
    actual_completion DATE NULL,
    completion_notes TEXT NULL,
    rating INT NULL CHECK (rating >= 1 AND rating <= 5),
    feedback TEXT NULL,
    rejection_reason TEXT NULL,
    is_public BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT,
    FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (assigned_to) REFERENCES users(id) ON DELETE SET NULL,
    
    INDEX idx_complaint_number (complaint_number),
    INDEX idx_category (category_id),
    INDEX idx_reporter (reporter_id),
    INDEX idx_assigned (assigned_to),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_created (created_at),
    INDEX idx_public (is_public)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================
-- TABEL ATTACHMENTS (Lampiran)
-- ==============================================
DROP TABLE IF EXISTS attachments;
CREATE TABLE attachments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    complaint_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_category ENUM('photo', 'document', 'video', 'other') DEFAULT 'photo',
    uploaded_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (complaint_id) REFERENCES complaints(id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE RESTRICT,
    
    INDEX idx_complaint (complaint_id),
    INDEX idx_type (file_type),
    INDEX idx_category (file_category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================
-- TABEL COMPLAINT_LOGS (Riwayat Status)
-- ==============================================
DROP TABLE IF EXISTS complaint_logs;
CREATE TABLE complaint_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    complaint_id INT NOT NULL,
    old_status VARCHAR(20),
    new_status VARCHAR(20) NOT NULL,
    notes TEXT,
    changed_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (complaint_id) REFERENCES complaints(id) ON DELETE CASCADE,
    FOREIGN KEY (changed_by) REFERENCES users(id) ON DELETE RESTRICT,
    
    INDEX idx_complaint (complaint_id),
    INDEX idx_status (new_status),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================
-- TABEL NOTIFICATIONS (Notifikasi)
-- ==============================================
DROP TABLE IF EXISTS notifications;
CREATE TABLE notifications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    complaint_id INT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type ENUM('info', 'success', 'warning', 'error') DEFAULT 'info',
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (complaint_id) REFERENCES complaints(id) ON DELETE SET NULL,
    
    INDEX idx_user (user_id),
    INDEX idx_read (is_read),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================
-- TABEL SETTINGS (Pengaturan Sistem)
-- ==============================================
DROP TABLE IF EXISTS settings;
CREATE TABLE settings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    setting_type ENUM('string', 'number', 'boolean', 'json') DEFAULT 'string',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_key (setting_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================
-- INSERT DATA AWAL
-- ==============================================

-- Kategori Aduan
INSERT INTO categories (name, description, icon, color, sort_order) VALUES
('Infrastruktur', 'Jalan, jembatan, gedung publik, fasilitas umum', '🏗️', '#E67E22', 1),
('Kebersihan', 'Sampah, drainase, kebersihan lingkungan', '🗑️', '#27AE60', 2),
('Utilitas', 'Listrik, air, telekomunikasi, internet', '⚡', '#F39C12', 3),
('Keamanan', 'Keamanan dan ketertiban masyarakat', '🛡️', '#E74C3C', 4),
('Kesehatan', 'Fasilitas kesehatan, sanitasi, lingkungan', '🏥', '#9B59B6', 5),
('Pendidikan', 'Fasilitas pendidikan, sekolah', '📚', '#3498DB', 6),
('Sosial', 'Masalah sosial kemasyarakatan, konflik', '👥', '#1ABC9C', 7),
('Ekonomi', 'Pasar, UMKM, ekonomi desa', '💰', '#34495E', 8);

-- Users default
INSERT INTO users (username, email, password_hash, full_name, role, phone, address, rt_rw) VALUES
('admin', 'admin@desatarabbi.go.id', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Administrator Sistem', 'admin', '081234567890', 'Kantor Desa Tarabbi', '00/00'),
('supervisor', 'supervisor@desatarabbi.go.id', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Supervisor Desa Tarabbi', 'supervisor', '081234567891', 'Kantor Desa Tarabbi', '00/00'),
('febriansyah', 'febry@desatarabbi.go.id', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Febriansyah', 'petugas', '081234567892', 'Jl. Petugas No. 1', '01/01'),
('petugas2', 'petugas2@desatarabbi.go.id', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Ahmad Petugas', 'petugas', '081234567893', 'Jl. Petugas No. 2', '02/01'),
('masyarakat1', 'siti@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Siti Aminah', 'masyarakat', '081234567894', 'Jl. Mawar Raya No. 15', '02/01'),
('masyarakat2', 'budi@email.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Budi Santoso', 'masyarakat', '081234567895', 'Jl. Melati No. 8', '03/02');

-- Sample Complaints untuk testing
INSERT INTO complaints (complaint_number, title, description, category_id, reporter_id, priority, status, location_address, rt_rw) VALUES
('ADU001/VI/2025', 'Jalan Rusak Parah di Jl. Mawar Raya', 'Jalan di depan rumah saya sudah rusak parah sejak seminggu yang lalu. Banyak lubang besar yang berbahaya untuk kendaraan. Sudah ada beberapa motor yang jatuh. Mohon segera diperbaiki karena ini jalur utama menuju pasar desa.', 1, 5, 'darurat', 'baru', 'Jl. Mawar Raya No. 15-20', '02/01'),
('ADU002/VI/2025', 'Lampu Jalan Mati di Jl. Melati', 'Lampu jalan di Jl. Melati sudah mati sejak 3 hari yang lalu. Pada malam hari jalan menjadi sangat gelap dan rawan kejahatan. Mohon segera diperbaiki.', 3, 6, 'sedang', 'validasi', 'Jl. Melati (depan warung Pak Haji)', '03/02'),
('ADU003/VI/2025', 'Sampah Tidak Diangkut Seminggu', 'Sampah di TPS RT 01 sudah menumpuk dan tidak diangkut selama seminggu. Mulai berbau dan mengundang lalat. Warga sudah mengeluh.', 2, 5, 'rendah', 'selesai', 'TPS RT 01 RW 01', '01/01');

-- Sample Complaint Logs
INSERT INTO complaint_logs (complaint_id, old_status, new_status, notes, changed_by) VALUES
(1, NULL, 'baru', 'Aduan baru diterima dari sistem', 1),
(2, NULL, 'baru', 'Aduan baru diterima dari sistem', 1),
(2, 'baru', 'validasi', 'Sedang divalidasi oleh admin', 1),
(3, NULL, 'baru', 'Aduan baru diterima dari sistem', 1),
(3, 'baru', 'proses', 'Ditugaskan kepada petugas kebersihan', 3),
(3, 'proses', 'selesai', 'Sampah sudah diangkut, TPS sudah bersih', 3);

-- Sample Notifications
INSERT INTO notifications (user_id, complaint_id, title, message, type) VALUES
(5, 1, 'Aduan Anda Diterima', 'Aduan #ADU001/VI/2025 telah diterima dan akan segera diproses', 'info'),
(6, 2, 'Aduan Anda Diterima', 'Aduan #ADU002/VI/2025 telah diterima dan sedang divalidasi', 'info'),
(5, 3, 'Aduan Selesai', 'Aduan #ADU003/VI/2025 telah diselesaikan. Mohon berikan rating dan feedback', 'success'),
(3, 1, 'Aduan Baru Masuk', 'Ada aduan darurat baru yang perlu ditangani segera', 'warning'),
(3, 2, 'Tugas Baru', 'Anda mendapat tugas baru untuk menangani aduan lampu jalan', 'info');

-- System Settings
INSERT INTO settings (setting_key, setting_value, setting_type, description) VALUES
('app_name', 'SIPRIMA Desa Tarabbi', 'string', 'Nama aplikasi'),
('app_version', '1.0.0', 'string', 'Versi aplikasi'),
('max_file_size', '5242880', 'number', 'Maksimum ukuran file upload (bytes)'),
('allowed_file_types', '["jpg","jpeg","png","pdf","doc","docx"]', 'json', 'Tipe file yang diizinkan'),
('response_time_target', '2', 'number', 'Target response time dalam jam'),
('completion_time_target', '7', 'number', 'Target waktu penyelesaian dalam hari'),
('auto_assign', 'false', 'boolean', 'Otomatis assign aduan ke petugas'),
('email_notifications', 'true', 'boolean', 'Aktifkan notifikasi email'),
('sms_notifications', 'false', 'boolean', 'Aktifkan notifikasi SMS'),
('public_view', 'true', 'boolean', 'Izinkan masyarakat melihat status aduan'),
('desa_name', 'Desa Tarabbi', 'string', 'Nama desa'),
('desa_address', 'Jl. Raya Tarabbi, Kec. Gowa, Kab. Gowa', 'string', 'Alamat desa'),
('desa_phone', '(0411) 123-4567', 'string', 'Telepon desa'),
('desa_email', 'admin@desatarabbi.go.id', 'string', 'Email desa');

-- Set foreign key checks back
SET FOREIGN_KEY_CHECKS = 1;

-- ==============================================
-- VIEWS UNTUK REPORTING
-- ==============================================

-- View untuk dashboard statistik
CREATE OR REPLACE VIEW dashboard_stats AS
SELECT 
    COUNT(*) as total_complaints,
    COUNT(CASE WHEN status = 'baru' THEN 1 END) as new_complaints,
    COUNT(CASE WHEN status = 'proses' THEN 1 END) as in_progress,
    COUNT(CASE WHEN status = 'selesai' THEN 1 END) as completed,
    COUNT(CASE WHEN status = 'ditolak' THEN 1 END) as rejected,
    COUNT(CASE WHEN priority = 'darurat' THEN 1 END) as urgent_complaints,
    COUNT(CASE WHEN DATE(created_at) = CURDATE() THEN 1 END) as today_complaints
FROM complaints;

-- View untuk complaints dengan detail lengkap
CREATE OR REPLACE VIEW complaints_detail AS
SELECT 
    c.id,
    c.complaint_number,
    c.title,
    c.description,
    c.priority,
    c.status,
    c.location_address,
    c.rt_rw,
    c.rating,
    c.created_at,
    c.updated_at,
    cat.name as category_name,
    cat.color as category_color,
    cat.icon as category_icon,
    reporter.full_name as reporter_name,
    reporter.phone as reporter_phone,
    reporter.email as reporter_email,
    assigned.full_name as assigned_name,
    assigned.phone as assigned_phone
FROM complaints c
LEFT JOIN categories cat ON c.category_id = cat.id
LEFT JOIN users reporter ON c.reporter_id = reporter.id
LEFT JOIN users assigned ON c.assigned_to = assigned.id;

-- ==============================================
-- PROCEDURES UNTUK OPERASI UMUM
-- ==============================================

DELIMITER //

-- Procedure untuk generate complaint number
CREATE OR REPLACE PROCEDURE GenerateComplaintNumber(
    OUT complaint_num VARCHAR(20)
)
BEGIN
    DECLARE next_id INT;
    DECLARE month_roman VARCHAR(4);
    DECLARE current_year INT;
    
    -- Get next auto increment value
    SELECT AUTO_INCREMENT INTO next_id 
    FROM information_schema.TABLES 
    WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'complaints';
    
    -- Get current year
    SET current_year = YEAR(NOW());
    
    -- Convert month to Roman numerals
    SET month_roman = CASE MONTH(NOW())
        WHEN 1 THEN 'I' WHEN 2 THEN 'II' WHEN 3 THEN 'III'
        WHEN 4 THEN 'IV' WHEN 5 THEN 'V' WHEN 6 THEN 'VI'
        WHEN 7 THEN 'VII' WHEN 8 THEN 'VIII' WHEN 9 THEN 'IX'
        WHEN 10 THEN 'X' WHEN 11 THEN 'XI' WHEN 12 THEN 'XII'
    END;
    
    -- Generate format: ADU001/VI/2025
    SET complaint_num = CONCAT('ADU', LPAD(next_id, 3, '0'), '/', month_roman, '/', current_year);
END//

-- Procedure untuk update complaint status
CREATE OR REPLACE PROCEDURE UpdateComplaintStatus(
    IN p_complaint_id INT,
    IN p_new_status VARCHAR(20),
    IN p_notes TEXT,
    IN p_changed_by INT
)
BEGIN
    DECLARE v_old_status VARCHAR(20);
    
    -- Get current status
    SELECT status INTO v_old_status FROM complaints WHERE id = p_complaint_id;
    
    -- Update complaint status
    UPDATE complaints 
    SET status = p_new_status, 
        updated_at = NOW(),
        actual_completion = CASE WHEN p_new_status = 'selesai' THEN NOW() ELSE actual_completion END
    WHERE id = p_complaint_id;
    
    -- Log the status change
    INSERT INTO complaint_logs (complaint_id, old_status, new_status, notes, changed_by)
    VALUES (p_complaint_id, v_old_status, p_new_status, p_notes, p_changed_by);
    
    -- Create notification for reporter
    INSERT INTO notifications (user_id, complaint_id, title, message, type)
    SELECT 
        reporter_id,
        p_complaint_id,
        CONCAT('Status Aduan Update: ', p_new_status),
        CONCAT('Status aduan Anda telah diupdate menjadi: ', p_new_status, 
               CASE WHEN p_notes IS NOT NULL THEN CONCAT('. Catatan: ', p_notes) ELSE '' END),
        CASE p_new_status 
            WHEN 'selesai' THEN 'success'
            WHEN 'ditolak' THEN 'error'
            ELSE 'info'
        END
    FROM complaints WHERE id = p_complaint_id;
END//

DELIMITER ;

-- ==============================================
-- SAMPLE QUERIES UNTUK TESTING
-- ==============================================

/*
-- Test dashboard stats
SELECT * FROM dashboard_stats;

-- Test complaints detail
SELECT * FROM complaints_detail ORDER BY created_at DESC;

-- Test generate complaint number
CALL GenerateComplaintNumber(@new_number);
SELECT @new_number;

-- Test update status
CALL UpdateComplaintStatus(1, 'proses', 'Sedang ditangani petugas', 3);

-- Query untuk dashboard petugas
SELECT 
    priority,
    COUNT(*) as count,
    AVG(TIMESTAMPDIFF(HOUR, created_at, NOW())) as avg_age_hours
FROM complaints 
WHERE status IN ('baru', 'proses')
GROUP BY priority;

-- Query untuk laporan bulanan
SELECT 
    DATE_FORMAT(created_at, '%Y-%m') as month,
    COUNT(*) as total,
    COUNT(CASE WHEN status = 'selesai' THEN 1 END) as completed,
    AVG(CASE WHEN status = 'selesai' 
        THEN TIMESTAMPDIFF(DAY, created_at, actual_completion) 
        END) as avg_completion_days
FROM complaints 
GROUP BY DATE_FORMAT(created_at, '%Y-%m')
ORDER BY month DESC;
*/

-- ==============================================
-- DATABASE SETUP COMPLETED
-- ==============================================

SELECT 'SIPRIMA Database Setup Completed Successfully!' as message;
SELECT COUNT(*) as total_tables FROM information_schema.tables WHERE table_schema = 'siprima_db';
SELECT COUNT(*) as total_users FROM users;
SELECT COUNT(*) as total_categories FROM categories;
SELECT COUNT(*) as sample_complaints FROM complaints;

