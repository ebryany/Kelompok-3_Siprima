/*
 * Database Fix Utility untuk SIPRIMA
 * Script untuk memperbaiki struktur tabel database
 */
package Utils;

import java.sql.*;

/**
 * Utility class untuk memperbaiki database
 * @author febry
 */
public class DatabaseFix {
    
    public static void main(String[] args) {
        System.out.println("🔧 SIPRIMA Database Fix Utility");
        System.out.println("=================================");
        
        try {
            Connection conn = DatabaseConfig.getConnection();
            
            // 1. Drop existing complaints table if exists (dengan typo)
            dropOldTable(conn);
            
            // 2. Create new complaints table dengan struktur yang benar
            createCorrectTable(conn);
            
            // 3. Insert sample data
            insertSampleData(conn);
            
            System.out.println("✅ Database berhasil diperbaiki!");
            
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void dropOldTable(Connection conn) throws SQLException {
        System.out.println("🗑️ Menghapus tabel lama...");
        
        String dropQuery = "DROP TABLE IF EXISTS complaints";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(dropQuery);
            System.out.println("✅ Tabel lama berhasil dihapus");
        }
    }
    
    private static void createCorrectTable(Connection conn) throws SQLException {
        System.out.println("🔨 Membuat tabel baru dengan struktur yang benar...");
        
        String createTableQuery = """
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
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableQuery);
            System.out.println("✅ Tabel complaints berhasil dibuat dengan struktur yang benar");
        }
    }
    
    private static void insertSampleData(Connection conn) throws SQLException {
        System.out.println("📊 Menambahkan data sample...");
        
        String insertQuery = """
            INSERT INTO complaints (
                complaint_number, reporter_name, reporter_nik, reporter_email, 
                reporter_phone, reporter_address, rt_rw, category, priority, 
                location_address, title, description, status
            ) VALUES 
            ('ADU20250618001', 'Siti Aminah', '1234567890123456', 'siti@email.com', 
             '081234567890', 'Jl. Mawar Raya No. 123', '02/01', 'Infrastruktur', 'darurat',
             'Jl. Mawar Raya', 'Jalan Rusak Parah', 'Jalan rusak dengan lubang besar yang sangat berbahaya untuk kendaraan', 'baru'),
            
            ('ADU20250618002', 'Budi Santoso', '1234567890123457', 'budi@email.com', 
             '081234567891', 'Jl. Melati No. 45', '03/02', 'Utilitas', 'sedang',
             'Jl. Melati', 'Lampu Jalan Mati', 'Lampu jalan sudah mati sejak 3 hari yang lalu', 'baru'),
            
            ('ADU20250618003', 'Ratna Sari', '1234567890123458', 'ratna@email.com', 
             '081234567892', 'Jl. Anggrek No. 67', '01/01', 'Kebersihan', 'rendah',
             'Jl. Anggrek', 'Sampah Tidak Diangkut', 'Sampah sudah menumpuk 2 minggu tidak diangkut', 'selesai')
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(insertQuery);
            System.out.println("✅ Data sample berhasil ditambahkan");
        }
    }
    
    public static void checkTableStructure() {
        System.out.println("🔍 Mengecek struktur tabel...");
        
        try {
            Connection conn = DatabaseConfig.getConnection();
            
            String query = "DESCRIBE complaints";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                System.out.println("\n📋 STRUKTUR TABEL COMPLAINTS:");
                System.out.println("================================");
                while (rs.next()) {
                    System.out.printf("%-20s %-20s %-10s %-10s\n",
                        rs.getString("Field"),
                        rs.getString("Type"),
                        rs.getString("Null"),
                        rs.getString("Key")
                    );
                }
                
            }
            
            // Check data count
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM complaints")) {
                
                if (rs.next()) {
                    System.out.println("\n📊 Total data: " + rs.getInt("total") + " aduan");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error checking table: " + e.getMessage());
        }
    }
}

