/*
 * Test Authentication untuk SIPRIMA
 * Testing authentication flow antara FormRegister dan FormLogin
 */
package Utils;

import models.User;

public class TestAuthentication {
    
    public static void main(String[] args) {
        System.out.println("=== 🧪 SIPRIMA AUTHENTICATION TEST ===");
        
        // Test 1: Database connection
        System.out.println("\n1. Testing database connection...");
        if (DatabaseConfig.testConnection()) {
            System.out.println("✅ Database connection OK");
        } else {
            System.out.println("❌ Database connection FAILED");
            return;
        }
        
        // Test 2: SessionManager
        System.out.println("\n2. Testing SessionManager...");
        SessionManager sessionManager = SessionManager.getInstance();
        
        // Test with registered user (assume user registered with FormRegister)
        String testUsername = "febry_test";
        String testPassword = "password123";
        String testRole = "petugas";
        
        System.out.println("\n3. Testing authentication with registered user...");
        System.out.println("Username: " + testUsername);
        System.out.println("Password: " + testPassword);
        System.out.println("Role: " + testRole);
        
        User authenticatedUser = sessionManager.authenticate(testUsername, testPassword, testRole);
        
        if (authenticatedUser != null) {
            System.out.println("\n✅ AUTHENTICATION SUCCESS!");
            System.out.println("User ID: " + authenticatedUser.getId());
            System.out.println("Username: " + authenticatedUser.getUsername());
            System.out.println("Full Name: " + authenticatedUser.getFullName());
            System.out.println("Email: " + authenticatedUser.getEmail());
            System.out.println("Role: " + authenticatedUser.getRoleDisplayName());
            
            // Test session info
            System.out.println("\n📊 Session Info:");
            System.out.println(sessionManager.getSessionInfo());
            
            // Test logout
            System.out.println("\n🚪 Testing logout...");
            sessionManager.logout();
            System.out.println("Logged out successfully");
            
        } else {
            System.out.println("\n❌ AUTHENTICATION FAILED!");
            System.out.println("Possible reasons:");
            System.out.println("1. User not found in database");
            System.out.println("2. Password doesn't match");
            System.out.println("3. Role doesn't match");
            System.out.println("4. User is not active");
            
            // Try to find user without password check
            System.out.println("\n🔍 Debugging: Checking if user exists...");
            try {
                java.sql.Connection conn = DatabaseConfig.getConnection();
                String query = "SELECT username, email, role, is_active, password_hash FROM users WHERE username = ? OR email = ?";
                java.sql.PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, testUsername);
                stmt.setString(2, testUsername);
                
                java.sql.ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println("✅ User found in database:");
                    System.out.println("  Username: " + rs.getString("username"));
                    System.out.println("  Email: " + rs.getString("email"));
                    System.out.println("  Role: " + rs.getString("role"));
                    System.out.println("  Is Active: " + rs.getBoolean("is_active"));
                    System.out.println("  Password Hash Length: " + rs.getString("password_hash").length());
                    
                    // Test password verification
                    String storedHash = rs.getString("password_hash");
                    sessionManager.debugPasswordVerification(testPassword, storedHash);
                    
                } else {
                    System.out.println("❌ User not found in database");
                    System.out.println("Please register first using FormRegister");
                }
                
            } catch (Exception e) {
                System.out.println("❌ Error checking user: " + e.getMessage());
            }
        }
        
        System.out.println("\n=== 🏁 TEST COMPLETED ===");
    }
}

