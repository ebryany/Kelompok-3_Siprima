-- Fix SIPRIMA Database - Jalankan di phpMyAdmin Laragon
-- Copy-paste script ini ke phpMyAdmin dan jalankan

-- 1. Gunakan database
USE siprima_db;

-- 2. Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- 3. Drop semua tabel yang bermasalah
DROP TABLE IF EXISTS complaint_logs;
DROP TABLE IF EXISTS attachments;
DROP TABLE IF EXISTS complaints;

-- 4. Buat tabel complaints dengan struktur yang BENAR
CREATE TABLE complaints (
    id INT PRIMARY KEY AUTO_INCREMENT,
    complaint_number VARCHAR(50) UNIQUE NOT NULL,
    reporter_name VARCHAR(100) NOT NULL,
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

-- 5. Insert data sample untuk testing
INSERT INTO complaints (
    complaint_number, reporter_name, reporter_nik, reporter_email, 
    reporter_phone, reporter_address, rt_rw, category, priority, 
    location_address, title, description, status
) VALUES 
('ADU20250618001', 'Siti Aminah', '1234567890123456', 'siti@email.com', 
 '081234567890', 'Jl. Mawar Raya No. 123', '02/01', 'Infrastruktur', 'darurat',
 'Jl. Mawar Raya', 'Jalan Rusak Parah', 'Jalan rusak dengan lubang besar yang sangat berbahaya untuk kendaraan', 'baru');

-- 6. Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- 7. Verifikasi hasil
SELECT 'Database Fixed Successfully!' AS Status;
SELECT COUNT(*) AS total_complaints FROM complaints;
DESCRIBE complaints;

