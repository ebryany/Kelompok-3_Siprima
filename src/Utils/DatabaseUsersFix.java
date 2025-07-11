/*
 * Database Users Fix Utility untuk SIPRIMA
 * Script untuk memperbaiki struktur tabel users
 */
package Utils;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 * Utility class untuk memperbaiki tabel users
 * @author febry
 */
public class DatabaseUsersFix {
    
    public static void main(String[] args) {
        System.out.println("🔧 SIPRIMA Database Users Fix Utility");
        System.out.println("======================================");
        
        try {
            Connection conn = DatabaseConfig.getConnection();
            
            // 1. Check current table structure
            checkCurrentStructure(conn);
            
            // 2. Fix users table structure
            fixUsersTable(conn);
            
            // 3. Verify the fix
            verifyFix(conn);
            
            System.out.println("✅ Database users table berhasil diperbaiki!");
            
        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void checkCurrentStructure(Connection conn) throws SQLException {
        System.out.println("🔍 Checking current users table structure...");
        
        try {
            String query = "DESCRIBE users";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                System.out.println("\n📋 CURRENT USERS TABLE STRUCTURE:");
                System.out.println("=====================================");
                boolean hasPhone = false;
                boolean hasAddress = false;
                boolean hasRtRw = false;
                boolean hasNik = false;
                
                while (rs.next()) {
                    String field = rs.getString("Field");
                    String type = rs.getString("Type");
                    String nullVal = rs.getString("Null");
                    String key = rs.getString("Key");
                    
                    System.out.printf("%-20s %-20s %-10s %-10s\n", field, type, nullVal, key);
                    
                    if ("phone".equals(field)) hasPhone = true;
                    if ("address".equals(field)) hasAddress = true;
                    if ("rt_rw".equals(field)) hasRtRw = true;
                    if ("nik".equals(field)) hasNik = true;
                }
                
                System.out.println("\n🔍 Missing columns check:");
                System.out.println("• phone: " + (hasPhone ? "✅ EXISTS" : "❌ MISSING"));
                System.out.println("• address: " + (hasAddress ? "✅ EXISTS" : "❌ MISSING"));
                System.out.println("• rt_rw: " + (hasRtRw ? "✅ EXISTS" : "❌ MISSING"));
                System.out.println("• nik: " + (hasNik ? "✅ EXISTS" : "❌ MISSING"));
                
            }
        } catch (SQLException e) {
            System.err.println("❌ Error checking table structure: " + e.getMessage());
            throw e;
        }
    }
    
    private static void fixUsersTable(Connection conn) throws SQLException {
        System.out.println("\n🔨 Fixing users table structure...");
        
        // Add missing columns if they don't exist
        String[] alterQueries = {
            "ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20) AFTER email",
            "ALTER TABLE users ADD COLUMN IF NOT EXISTS address TEXT AFTER phone",
            "ALTER TABLE users ADD COLUMN IF NOT EXISTS rt_rw VARCHAR(10) AFTER address",
            "ALTER TABLE users ADD COLUMN IF NOT EXISTS nik VARCHAR(20) AFTER rt_rw",
            "ALTER TABLE users ADD COLUMN IF NOT EXISTS photo_url VARCHAR(255) AFTER nik",
            "ALTER TABLE users ADD COLUMN IF NOT EXISTS last_login TIMESTAMP NULL AFTER photo_url",
            "ALTER TABLE users ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP AFTER last_login",
            "ALTER TABLE users ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_at"
        };
        
        try (Statement stmt = conn.createStatement()) {
            for (String query : alterQueries) {
                try {
                    stmt.executeUpdate(query);
                    System.out.println("✅ Executed: " + query);
                } catch (SQLException e) {
                    // Check if error is about column already exists
                    if (e.getMessage().contains("Duplicate column name") || 
                        e.getMessage().contains("already exists")) {
                        System.out.println("ℹ️ Column already exists, skipping: " + query);
                    } else {
                        // For MySQL versions that don't support "IF NOT EXISTS"
                        // Try alternative approach
                        String column = extractColumnName(query);
                        if (column != null && !columnExists(conn, "users", column)) {
                            String simpleQuery = query.replace(" IF NOT EXISTS", "");
                            stmt.executeUpdate(simpleQuery);
                            System.out.println("✅ Executed (alternative): " + simpleQuery);
                        } else {
                            System.out.println("ℹ️ Column " + column + " already exists");
                        }
                    }
                }
            }
        }
    }
    
    private static String extractColumnName(String alterQuery) {
        // Extract column name from ALTER TABLE query
        String[] parts = alterQuery.split(" ");
        for (int i = 0; i < parts.length; i++) {
            if ("COLUMN".equalsIgnoreCase(parts[i]) && i + 1 < parts.length) {
                String columnPart = parts[i + 1];
                if (columnPart.contains("IF")) {
                    // Handle "IF NOT EXISTS" case
                    if (i + 4 < parts.length) {
                        return parts[i + 4];
                    }
                } else {
                    return columnPart;
                }
            }
        }
        return null;
    }
    
    private static boolean columnExists(Connection conn, String tableName, String columnName) {
        try {
            String query = "SELECT * FROM information_schema.columns WHERE table_name = ? AND column_name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, tableName);
                pstmt.setString(2, columnName);
                ResultSet rs = pstmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error checking column existence: " + e.getMessage());
            return false;
        }
    }
    
    private static void verifyFix(Connection conn) throws SQLException {
        System.out.println("\n🔍 Verifying the fix...");
        
        String query = "DESCRIBE users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("\n📋 UPDATED USERS TABLE STRUCTURE:");
            System.out.println("===================================");
            
            boolean hasPhone = false;
            boolean hasAddress = false;
            boolean hasRtRw = false;
            boolean hasNik = false;
            boolean hasPhotoUrl = false;
            
            while (rs.next()) {
                String field = rs.getString("Field");
                String type = rs.getString("Type");
                String nullVal = rs.getString("Null");
                String key = rs.getString("Key");
                
                System.out.printf("%-20s %-20s %-10s %-10s\n", field, type, nullVal, key);
                
                if ("phone".equals(field)) hasPhone = true;
                if ("address".equals(field)) hasAddress = true;
                if ("rt_rw".equals(field)) hasRtRw = true;
                if ("nik".equals(field)) hasNik = true;
                if ("photo_url".equals(field)) hasPhotoUrl = true;
            }
            
            System.out.println("\n✅ Verification results:");
            System.out.println("• phone: " + (hasPhone ? "✅ FIXED" : "❌ STILL MISSING"));
            System.out.println("• address: " + (hasAddress ? "✅ FIXED" : "❌ STILL MISSING"));
            System.out.println("• rt_rw: " + (hasRtRw ? "✅ FIXED" : "❌ STILL MISSING"));
            System.out.println("• nik: " + (hasNik ? "✅ FIXED" : "❌ STILL MISSING"));
            System.out.println("• photo_url: " + (hasPhotoUrl ? "✅ FIXED" : "❌ STILL MISSING"));
            
            if (hasPhone && hasAddress && hasRtRw && hasNik && hasPhotoUrl) {
                System.out.println("\n🎉 ALL COLUMNS SUCCESSFULLY ADDED!");
                
                // Show success message
                try {
                    JOptionPane.showMessageDialog(null,
                        "✅ Database users table berhasil diperbaiki!\n\n" +
                        "Kolom yang ditambahkan:\n" +
                        "• phone (VARCHAR(20))\n" +
                        "• address (TEXT)\n" +
                        "• rt_rw (VARCHAR(10))\n" +
                        "• nik (VARCHAR(20))\n" +
                        "• photo_url (VARCHAR(255))\n\n" +
                        "Sekarang Anda dapat login dengan normal!",
                        "Database Fix Berhasil",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    // GUI not available, just print
                }
            } else {
                System.out.println("\n❌ SOME COLUMNS STILL MISSING!");
            }
        }
    }
    
    /**
     * Method untuk dipanggil dari aplikasi lain
     */
    public static boolean fixUsersTableStructure() {
        try {
            Connection conn = DatabaseConfig.getConnection();
            fixUsersTable(conn);
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error fixing users table: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Quick check if users table has required columns
     */
    public static boolean checkUsersTableStructure() {
        try {
            Connection conn = DatabaseConfig.getConnection();
            
            String query = "SELECT * FROM information_schema.columns WHERE table_name = 'users' AND column_name IN ('phone', 'address', 'rt_rw', 'nik', 'photo_url')";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                int count = 0;
                while (rs.next()) {
                    count++;
                }
                
                return count >= 5; // All 5 columns should exist
            }
        } catch (SQLException e) {
            System.err.println("❌ Error checking users table structure: " + e.getMessage());
            return false;
        }
    }
}

