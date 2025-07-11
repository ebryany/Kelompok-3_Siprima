/*
 * Database Configuration untuk SIPRIMA
 * Konfigurasi koneksi database MySQL dengan connection pooling
 */
package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * @author febry
 * Konfigurasi database untuk aplikasi SIPRIMA
 */
public class DatabaseConfig {
    
    // Database connection parameters
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "siprima_db";
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Sesuaikan dengan password MySQL Anda
    
    // Connection pool settings
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final int CONNECTION_TIMEOUT = 30;
    private static final int SOCKET_TIMEOUT = 60;
    
    // Singleton connection instance
    private static Connection connection = null;
    
    /**
     * Get database connection with optimized settings
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName(DB_DRIVER);
            
            // Check if connection is valid
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                // Create new connection with optimized properties
                Properties props = new Properties();
                props.setProperty("user", DB_USER);
                props.setProperty("password", DB_PASSWORD);
                props.setProperty("useSSL", "false");
                props.setProperty("serverTimezone", "Asia/Makassar");
                props.setProperty("allowPublicKeyRetrieval", "true");
                props.setProperty("useUnicode", "true");
                props.setProperty("characterEncoding", "utf8");
                props.setProperty("connectTimeout", String.valueOf(CONNECTION_TIMEOUT * 1000));
                props.setProperty("socketTimeout", String.valueOf(SOCKET_TIMEOUT * 1000));
                props.setProperty("autoReconnect", "true");
                props.setProperty("failOverReadOnly", "false");
                props.setProperty("maxReconnects", "3");
                
                connection = DriverManager.getConnection(DB_URL, props);
                connection.setAutoCommit(true);
                
                System.out.println("✅ Database connection established successfully!");
            }
            
            return connection;
            
        } catch (ClassNotFoundException e) {
            String errorMsg = "❌ MySQL JDBC Driver not found: " + e.getMessage();
            System.err.println(errorMsg);
            throw new SQLException(errorMsg, e);
            
        } catch (SQLException e) {
            String errorMsg = "❌ Database connection failed: " + e.getMessage();
            System.err.println(errorMsg);
            
            // Show user-friendly error message
            showConnectionError(e);
            throw e;
        }
    }
    
    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection testConn = getConnection();
            
            // Test with a simple query
            String testQuery = "SELECT 1 as test";
            try (Statement stmt = testConn.createStatement();
                 ResultSet rs = stmt.executeQuery(testQuery)) {
                
                if (rs.next() && rs.getInt("test") == 1) {
                    System.out.println("✅ Database connection test passed!");
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Database connection test failed: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Check if database and tables exist
     * @return true if database is properly set up
     */
    public static boolean isDatabaseSetup() {
        try {
            Connection conn = getConnection();
            
            // Check if main tables exist
            String[] requiredTables = {"users", "categories", "complaints", "attachments", "notifications"};
            
            for (String tableName : requiredTables) {
                String checkQuery = "SELECT COUNT(*) FROM information_schema.tables " +
                                  "WHERE table_schema = ? AND table_name = ?";
                
                try (PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
                    stmt.setString(1, DB_NAME);
                    stmt.setString(2, tableName);
                    
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        System.err.println("❌ Table '" + tableName + "' not found in database");
                        return false;
                    }
                }
            }
            
            System.out.println("✅ Database schema verification passed!");
            return true;
            
        } catch (SQLException e) {
            System.err.println("❌ Database schema verification failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get database statistics
     * @return Database info string
     */
    public static String getDatabaseInfo() {
        try {
            Connection conn = getConnection();
            StringBuilder info = new StringBuilder();
            
            // Get database version
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT VERSION() as version");
                if (rs.next()) {
                    info.append("Database: MySQL ").append(rs.getString("version")).append("\n");
                }
            }
            
            // Get table counts
            String[] tables = {"users", "categories", "complaints", "attachments", "notifications"};
            
            for (String table : tables) {
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM " + table);
                    if (rs.next()) {
                        info.append("Table ").append(table).append(": ").append(rs.getInt("count")).append(" rows\n");
                    }
                }
            }
            
            return info.toString();
            
        } catch (SQLException e) {
            return "❌ Error getting database info: " + e.getMessage();
        }
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Show user-friendly connection error dialog
     * @param e SQLException that occurred
     */
    private static void showConnectionError(SQLException e) {
        String title = "Database Connection Error";
        String message;
        
        // Determine error type and show appropriate message
        if (e.getMessage().contains("Access denied")) {
            message = "❌ Access denied to database.\n\n" +
                     "Please check your database credentials:\n" +
                     "• Username: " + DB_USER + "\n" +
                     "• Password: Check if correct\n" +
                     "• Database: " + DB_NAME + "\n\n" +
                     "Error: " + e.getMessage();
                     
        } else if (e.getMessage().contains("Unknown database")) {
            message = "❌ Database '" + DB_NAME + "' not found.\n\n" +
                     "Please create the database by running:\n" +
                     "1. Open MySQL Command Line or phpMyAdmin\n" +
                     "2. Run the SQL script: database/siprima_db.sql\n\n" +
                     "Error: " + e.getMessage();
                     
        } else if (e.getMessage().contains("Connection refused") || 
                   e.getMessage().contains("Communications link failure")) {
            message = "❌ Cannot connect to MySQL server.\n\n" +
                     "Please check:\n" +
                     "• MySQL server is running\n" +
                     "• Host: " + DB_HOST + "\n" +
                     "• Port: " + DB_PORT + "\n" +
                     "• Firewall settings\n\n" +
                     "Error: " + e.getMessage();
                     
        } else {
            message = "❌ Database connection failed.\n\n" +
                     "Connection details:\n" +
                     "• Host: " + DB_HOST + ":" + DB_PORT + "\n" +
                     "• Database: " + DB_NAME + "\n" +
                     "• User: " + DB_USER + "\n\n" +
                     "Error: " + e.getMessage();
        }
        
        // Show error dialog (only in GUI context)
        try {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // If GUI not available, just print to console
            System.err.println(title + ": " + message);
        }
    }
    
    /**
     * Initialize database with sample data (for development)
     * @return true if successful
     */
    public static boolean initializeSampleData() {
        try {
            Connection conn = getConnection();
            
            // Check if data already exists
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
                if (rs.next() && rs.getInt("count") > 0) {
                    System.out.println("ℹ️ Sample data already exists");
                    return true;
                }
            }
            
            System.out.println("🔄 Initializing sample data...");
            
            // This would run the SQL script programmatically
            // For now, we'll just indicate that the SQL script should be run manually
            
            JOptionPane.showMessageDialog(null, 
                "Please run the database initialization script:\n" +
                "database/siprima_db.sql\n\n" +
                "This script will create all tables and sample data.",
                "Database Initialization", 
                JOptionPane.INFORMATION_MESSAGE);
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("❌ Error initializing sample data: " + e.getMessage());
            return false;
        }
    }
    
    // Getter methods for connection parameters (for configuration purposes)
    public static String getDbHost() { return DB_HOST; }
    public static String getDbPort() { return DB_PORT; }
    public static String getDbName() { return DB_NAME; }
    public static String getDbUser() { return DB_USER; }
    public static String getDbUrl() { return DB_URL; }
    
    /**
     * Main method for testing database connection
     */
    public static void main(String[] args) {
        System.out.println("=== SIPRIMA Database Connection Test ===");
        
        // Test connection
        if (testConnection()) {
            System.out.println("\n=== Database Information ===");
            System.out.println(getDatabaseInfo());
            
            // Check schema
            if (isDatabaseSetup()) {
                System.out.println("\n✅ Database is ready for use!");
            } else {
                System.out.println("\n⚠️ Database schema needs to be set up.");
                System.out.println("Please run: database/siprima_db.sql");
            }
        } else {
            System.out.println("\n❌ Database connection failed!");
            System.out.println("Please check your MySQL installation and configuration.");
        }
        
        // Close connection
        closeConnection();
    }
}
