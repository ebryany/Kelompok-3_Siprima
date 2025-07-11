/*
 * SIPRIMA Session Manager
 * Menangani session dan authentication untuk aplikasi SIPRIMA
 */
package Utils;

import models.User;
import models.User.UserRole;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.swing.JOptionPane;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author febry
 * Session Manager untuk aplikasi SIPRIMA
 */
public class SessionManager {
    
    // Singleton instance
    private static SessionManager instance;
    
    // Current session
    private User currentUser;
    private String sessionToken;
    private Timestamp loginTime;
    
    // Session timeout (dalam menit)
    private static final int SESSION_TIMEOUT_MINUTES = 120; // 2 jam
    
    /**
     * Private constructor untuk singleton
     */
    private SessionManager() {
        // Private constructor
    }
    
    /**
     * Get singleton instance
     * @return SessionManager instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Authenticate user dengan username/email dan password
     * @param usernameOrEmail Username atau email
     * @param password Password (plain text)
     * @param selectedRole Role yang dipilih
     * @return User object jika berhasil, null jika gagal
     */
    public User authenticate(String usernameOrEmail, String password, String selectedRole) {
        try {
            Connection conn = DatabaseConfig.getConnection();
            
            // Query untuk mencari user berdasarkan username atau email
            String query = "SELECT * FROM users WHERE (username = ? OR email = ?) AND role = ? AND is_active = 1";
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, usernameOrEmail);
                stmt.setString(2, usernameOrEmail);
                stmt.setString(3, selectedRole);
                
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    // User ditemukan, verify password
                    String storedHash = rs.getString("password_hash");
                    
                    // Untuk sementara, gunakan simple password check
                    // Nanti bisa diganti dengan BCrypt atau password hashing lainnya
                    if (verifyPassword(password, storedHash)) {
                        // Password cocok, buat User object
                        User user = createUserFromResultSet(rs);
                        
                        // Update last login
                        updateLastLogin(user.getId());
                        
                        // Set current session
                        setCurrentSession(user);
                        
                        System.out.println("✅ Authentication berhasil untuk: " + user.getDisplayName());
                        return user;
                        
                    } else {
                        System.out.println("❌ Password salah untuk user: " + usernameOrEmail);
                    }
                } else {
                    System.out.println("❌ User tidak ditemukan: " + usernameOrEmail + " dengan role: " + selectedRole);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Database error saat authentication: " + e.getMessage());
            
            // Show user-friendly error
            JOptionPane.showMessageDialog(null,
                "❌ Terjadi kesalahan koneksi database.\n\n" +
                "Error: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        return null;
    }
    
    /**
     * Verify password dengan stored hash
     * Supports both SHA-256+salt (from FormRegister) and simple passwords
     */
    private boolean verifyPassword(String plainPassword, String storedHash) {
        try {
            // Check if it's a Base64 encoded hash (from FormRegister)
            if (storedHash.length() > 50 && isBase64(storedHash)) {
                return verifyHashedPassword(plainPassword, storedHash);
            }
            
            // For legacy/simple passwords (development mode)
            if ("password123".equals(plainPassword)) {
                return true;
            }
            
            if ("petugas123".equals(plainPassword)) {
                return true;
            }
            
            // Direct comparison for simple passwords
            return plainPassword.equals(storedHash);
            
        } catch (Exception e) {
            System.err.println("⚠️ Error verifying password: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify SHA-256 hashed password with salt (from FormRegister)
     */
    private boolean verifyHashedPassword(String plainPassword, String storedHashBase64) {
        try {
            // Decode the stored hash
            byte[] saltedHash = java.util.Base64.getDecoder().decode(storedHashBase64);
            
            // Extract salt (first 16 bytes) and hash (remaining bytes)
            byte[] salt = new byte[16];
            byte[] storedPasswordHash = new byte[saltedHash.length - 16];
            
            System.arraycopy(saltedHash, 0, salt, 0, 16);
            System.arraycopy(saltedHash, 16, storedPasswordHash, 0, storedPasswordHash.length);
            
            // Hash the input password with the same salt
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] inputPasswordHash = md.digest(plainPassword.getBytes("UTF-8"));
            
            // Compare the hashes
            return java.util.Arrays.equals(storedPasswordHash, inputPasswordHash);
            
        } catch (Exception e) {
            System.err.println("❌ Error verifying hashed password: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if string is Base64 encoded
     */
    private boolean isBase64(String str) {
        try {
            java.util.Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Create User object dari ResultSet dengan safe column access
     */
    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        
        // Core fields (always present)
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(UserRole.fromString(rs.getString("role")));
        user.setActive(rs.getBoolean("is_active"));
        
        // Optional timestamp fields (may not exist in older database versions)
        try {
            user.setLastLogin(rs.getTimestamp("last_login"));
        } catch (SQLException e) {
            if (e.getMessage().contains("Column 'last_login' not found")) {
                System.out.println("⚠️ Column 'last_login' not found in users table");
                user.setLastLogin(null);
            } else {
                throw e;
            }
        }
        
        try {
            user.setCreatedAt(rs.getTimestamp("created_at"));
        } catch (SQLException e) {
            if (e.getMessage().contains("Column 'created_at' not found")) {
                System.out.println("⚠️ Column 'created_at' not found in users table");
                user.setCreatedAt(null);
            } else {
                throw e;
            }
        }
        
        try {
            user.setUpdatedAt(rs.getTimestamp("updated_at"));
        } catch (SQLException e) {
            if (e.getMessage().contains("Column 'updated_at' not found")) {
                System.out.println("⚠️ Column 'updated_at' not found in users table");
                user.setUpdatedAt(null);
            } else {
                throw e;
            }
        }
        
        // Optional fields (may not exist in older database versions)
        try {
            user.setPhone(rs.getString("phone"));
        } catch (SQLException e) {
            if (e.getMessage().contains("Column 'phone' not found")) {
                System.out.println("⚠️ Column 'phone' not found in users table");
                user.setPhone(null);
            } else {
                throw e;
            }
        }
        
        try {
            user.setAddress(rs.getString("address"));
        } catch (SQLException e) {
            if (e.getMessage().contains("Column 'address' not found")) {
                System.out.println("⚠️ Column 'address' not found in users table");
                user.setAddress(null);
            } else {
                throw e;
            }
        }
        
        try {
            user.setRtRw(rs.getString("rt_rw"));
        } catch (SQLException e) {
            if (e.getMessage().contains("Column 'rt_rw' not found")) {
                System.out.println("⚠️ Column 'rt_rw' not found in users table");
                user.setRtRw(null);
            } else {
                throw e;
            }
        }
        
        try {
            user.setNik(rs.getString("nik"));
        } catch (SQLException e) {
            if (e.getMessage().contains("Column 'nik' not found")) {
                System.out.println("⚠️ Column 'nik' not found in users table");
                user.setNik(null);
            } else {
                throw e;
            }
        }
        
        try {
            user.setPhotoUrl(rs.getString("photo_url"));
        } catch (SQLException e) {
            if (e.getMessage().contains("Column 'photo_url' not found")) {
                System.out.println("⚠️ Column 'photo_url' not found in users table");
                user.setPhotoUrl(null);
            } else {
                throw e;
            }
        }
        
        return user;
    }
    
    /**
     * Update last login timestamp
     */
    private void updateLastLogin(int userId) {
        try {
            Connection conn = DatabaseConfig.getConnection();
            
            String query = "UPDATE users SET last_login = NOW() WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
                System.out.println("✅ Last login updated untuk user ID: " + userId);
            }
            
        } catch (SQLException e) {
            if (e.getMessage().contains("Column 'last_login' not found") || 
                e.getMessage().contains("Unknown column 'last_login'")) {
                System.out.println("⚠️ Column 'last_login' tidak ada - skip update last login");
            } else {
                System.err.println("⚠️ Error updating last login: " + e.getMessage());
            }
        }
    }
    
    /**
     * Set current session
     */
    private void setCurrentSession(User user) {
        this.currentUser = user;
        this.loginTime = new Timestamp(System.currentTimeMillis());
        this.sessionToken = generateSessionToken(user);
        
        System.out.println("🔐 Session dimulai untuk: " + user.getDisplayName() + " (" + user.getRoleDisplayName() + ")");
    }
    
    /**
     * Generate session token sederhana
     */
    private String generateSessionToken(User user) {
        return "SIPRIMA_" + user.getId() + "_" + System.currentTimeMillis();
    }
    
    /**
     * Get current logged in user
     * @return Current user atau null jika belum login
     */
    public User getCurrentUser() {
        if (isSessionValid()) {
            return currentUser;
        }
        return null;
    }
    
    /**
     * Check apakah user sudah login
     * @return true jika sudah login dan session masih valid
     */
    public boolean isLoggedIn() {
        return currentUser != null && isSessionValid();
    }
    
    /**
     * Check apakah session masih valid (belum timeout)
     * @return true jika session masih valid
     */
    public boolean isSessionValid() {
        if (currentUser == null || loginTime == null) {
            return false;
        }
        
        // Check timeout
        long sessionDuration = System.currentTimeMillis() - loginTime.getTime();
        long maxDuration = SESSION_TIMEOUT_MINUTES * 60 * 1000; // convert to milliseconds
        
        return sessionDuration < maxDuration;
    }
    
    /**
     * Logout current user
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("🚪 User logout: " + currentUser.getDisplayName());
        }
        
        this.currentUser = null;
        this.loginTime = null;
        this.sessionToken = null;
    }
    
    /**
     * Get current user role
     * @return UserRole atau null jika belum login
     */
    public UserRole getCurrentUserRole() {
        User user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }
    
    /**
     * Check apakah current user punya role tertentu
     * @param role Role yang dicek
     * @return true jika user punya role tersebut
     */
    public boolean hasRole(UserRole role) {
        UserRole currentRole = getCurrentUserRole();
        return currentRole != null && currentRole == role;
    }
    
    /**
     * Check apakah current user adalah staff (bukan masyarakat)
     * @return true jika user adalah staff
     */
    public boolean isStaff() {
        User user = getCurrentUser();
        return user != null && user.isStaff();
    }
    
    /**
     * Check apakah current user bisa manage users
     * @return true jika bisa manage users
     */
    public boolean canManageUsers() {
        User user = getCurrentUser();
        return user != null && user.canManageUsers();
    }
    
    /**
     * Check apakah current user bisa assign complaints
     * @return true jika bisa assign complaints
     */
    public boolean canAssignComplaints() {
        User user = getCurrentUser();
        return user != null && user.canAssignComplaints();
    }
    
    /**
     * Get session info untuk debugging
     * @return Session information string
     */
    public String getSessionInfo() {
        if (currentUser == null) {
            return "No active session";
        }
        
        long sessionDuration = System.currentTimeMillis() - loginTime.getTime();
        long remainingTime = (SESSION_TIMEOUT_MINUTES * 60 * 1000) - sessionDuration;
        
        return String.format(
            "User: %s (%s)\nLogin Time: %s\nSession Remaining: %d minutes",
            currentUser.getDisplayName(),
            currentUser.getRoleDisplayName(),
            loginTime.toString(),
            remainingTime / (60 * 1000)
        );
    }
    
    /**
     * Force refresh session (extend timeout)
     */
    public void refreshSession() {
        if (currentUser != null) {
            this.loginTime = new Timestamp(System.currentTimeMillis());
            System.out.println("🔄 Session refreshed untuk: " + currentUser.getDisplayName());
        }
    }
    
    /**
     * Test method untuk development - login dengan data dummy
     * @param role Role untuk testing
     * @return Test user
     */
    public User loginForTesting(String role) {
        User testUser = new User();
        testUser.setId(999);
        testUser.setUsername("test_" + role);
        testUser.setEmail("test@desatarabbi.go.id");
        testUser.setFullName("Test " + role.toUpperCase());
        testUser.setRole(UserRole.fromString(role));
        testUser.setPhone("081234567890");
        testUser.setActive(true);
        
        setCurrentSession(testUser);
        
        System.out.println("🧪 Test login sebagai: " + testUser.getDisplayName());
        return testUser;
    }
    
    /**
     * Debug method untuk test password verification
     */
    public void debugPasswordVerification(String plainPassword, String storedHash) {
        System.out.println("\n=== 🔍 DEBUG PASSWORD VERIFICATION ===");
        System.out.println("Plain Password: " + plainPassword);
        System.out.println("Stored Hash: " + storedHash);
        System.out.println("Hash Length: " + storedHash.length());
        System.out.println("Is Base64: " + isBase64(storedHash));
        
        boolean result = verifyPassword(plainPassword, storedHash);
        System.out.println("Verification Result: " + (result ? "✅ VALID" : "❌ INVALID"));
        System.out.println("======================================\n");
    }
}
