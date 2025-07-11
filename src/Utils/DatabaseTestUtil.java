/*
 * Database Test Utility untuk SIPRIMA
 * Utility untuk testing koneksi dan struktur database
 */
package Utils;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 * Utility class untuk testing database SIPRIMA
 */
public class DatabaseTestUtil {
    
    /**
     * Test koneksi database dan tampilkan hasilnya
     */
    public static void testDatabaseConnection() {
        StringBuilder result = new StringBuilder();
        result.append("=== DATABASE CONNECTION TEST ===").append("\n\n");
        
        try {
            // Test basic connection
            boolean connected = DatabaseConfig.testConnection();
            if (connected) {
                result.append("✅ Database connection: SUCCESS\n");
                
                // Test database info
                String dbInfo = DatabaseConfig.getDatabaseInfo();
                result.append("\n📊 Database Information:\n");
                result.append(dbInfo).append("\n");
                
                // Test schema
                boolean schemaOK = DatabaseConfig.isDatabaseSetup();
                if (schemaOK) {
                    result.append("✅ Database schema: VALID\n");
                } else {
                    result.append("❌ Database schema: INCOMPLETE\n");
                    result.append("   Please run database setup script first.\n");
                }
                
            } else {
                result.append("❌ Database connection: FAILED\n");
                result.append("   Please check database configuration.\n");
            }
            
        } catch (Exception e) {
            result.append("❌ Database test error: ").append(e.getMessage()).append("\n");
            result.append("\n🔧 Troubleshooting tips:\n");
            result.append("   1. Check if MySQL server is running\n");
            result.append("   2. Verify database credentials\n");
            result.append("   3. Ensure database 'siprima_db' exists\n");
            result.append("   4. Check MySQL JDBC driver is in classpath\n");
        }
        
        // Show result in dialog
        JOptionPane.showMessageDialog(null, 
            result.toString(), 
            "SIPRIMA Database Test", 
            JOptionPane.INFORMATION_MESSAGE);
            
        // Also print to console
        System.out.println(result.toString());
    }
    
    /**
     * Create sample data for testing
     */
    public static void createSampleData() {
        try {
            Connection conn = DatabaseConfig.getConnection();
            
            // Insert sample complaint if not exists
            String checkQuery = "SELECT COUNT(*) FROM complaints WHERE complaint_number = 'ADU-001'";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(checkQuery)) {
                
                if (rs.next() && rs.getInt(1) == 0) {
                    // Insert sample data
                    String insertQuery = "INSERT INTO complaints " +
                        "(complaint_number, title, reporter_name, reporter_email, " +
                        "category, status, priority, location_address, description, created_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
                        
                    try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                        pstmt.setString(1, "ADU-001");
                        pstmt.setString(2, "Jalan Rusak Parah");
                        pstmt.setString(3, "Siti Aminah");
                        pstmt.setString(4, "siti@email.com");
                        pstmt.setString(5, "Infrastruktur");
                        pstmt.setString(6, "baru");
                        pstmt.setString(7, "tinggi");
                        pstmt.setString(8, "Jl. Mawar Raya RT 02/01");
                        pstmt.setString(9, "Jalan rusak dengan lubang besar yang sangat berbahaya untuk kendaraan");
                        
                        int rows = pstmt.executeUpdate();
                        if (rows > 0) {
                            JOptionPane.showMessageDialog(null,
                                "✅ Sample data berhasil dibuat\n" +
                                "ID: ADU-001 - Jalan Rusak Parah",
                                "Sample Data Created",
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                        "ℹ️ Sample data sudah ada\n" +
                        "ADU-001 sudah tersedia untuk testing",
                        "Sample Data Exists",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "❌ Error creating sample data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Main method untuk standalone testing
     */
    public static void main(String[] args) {
        System.out.println("SIPRIMA Database Test Utility");
        System.out.println("==============================");
        
        // Test connection
        testDatabaseConnection();
        
        // Ask to create sample data
        int option = JOptionPane.showConfirmDialog(null,
            "Apakah Anda ingin membuat sample data untuk testing?",
            "Create Sample Data",
            JOptionPane.YES_NO_OPTION);
            
        if (option == JOptionPane.YES_OPTION) {
            createSampleData();
        }
    }
}

