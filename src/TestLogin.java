/*
 * Test Login dan Dashboard untuk SIPRIMA
 * Test class untuk mencoba sistem login dan navigasi ke dashboard
 */

import Utils.DatabaseConfig;
import Utils.SessionManager;
import models.User;
import dashboard.DashboardPetugas;
import dashboard.DashboardSuperVisor;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * @author febry
 * Test class untuk login dan dashboard system
 */
public class TestLogin {
    
    public static void main(String[] args) {
        System.out.println("=== SIPRIMA LOGIN & DASHBOARD TEST ===");
        
        // Test database connection
        if (testDatabaseConnection()) {
            // Show test menu
            showTestMenu();
        } else {
            System.out.println("❌ Database connection failed. Please check your MySQL setup.");
        }
    }
    
    /**
     * Test database connection
     */
    private static boolean testDatabaseConnection() {
        System.out.println("\n🔧 Testing database connection...");
        
        try {
            if (DatabaseConfig.testConnection()) {
                if (DatabaseConfig.isDatabaseSetup()) {
                    System.out.println("✅ Database connection and schema verified!");
                    System.out.println(DatabaseConfig.getDatabaseInfo());
                    return true;
                } else {
                    System.out.println("❌ Database schema not found. Please run database/siprima_db.sql");
                    return false;
                }
            } else {
                System.out.println("❌ Database connection failed.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Database error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Show test menu
     */
    private static void showTestMenu() {
        SwingUtilities.invokeLater(() -> {
            String[] options = {
                "1. Test Login Real (Database)",
                "2. Test Login Dummy Petugas", 
                "3. Test Login Dummy Supervisor",
                "4. Open Login Form",
                "5. Exit"
            };
            
            String choice = (String) JOptionPane.showInputDialog(
                null,
                "🧪 SIPRIMA Test Menu\n\n" +
                "Pilih opsi test yang ingin dijalankan:\n" +
                "• Option 1: Test login dengan database real\n" +
                "• Option 2-3: Test dengan data dummy\n" +
                "• Option 4: Buka form login normal\n",
                "SIPRIMA TEST MENU",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            if (choice != null) {
                switch (choice) {
                    case "1. Test Login Real (Database)":
                        testRealLogin();
                        break;
                    case "2. Test Login Dummy Petugas":
                        testDummyLogin("petugas");
                        break;
                    case "3. Test Login Dummy Supervisor":
                        testDummyLogin("supervisor");
                        break;
                    case "4. Open Login Form":
                        openLoginForm();
                        break;
                    case "5. Exit":
                        System.exit(0);
                        break;
                }
            }
        });
    }
    
    /**
     * Test real login dengan database
     */
    private static void testRealLogin() {
        SwingUtilities.invokeLater(() -> {
            String username = JOptionPane.showInputDialog(
                null,
                "🔐 Test Real Login\n\n" +
                "Masukkan username/email:\n" +
                "(Default users: admin, supervisor, febriansyah)",
                "TEST REAL LOGIN",
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (username != null && !username.trim().isEmpty()) {
                String password = JOptionPane.showInputDialog(
                    null,
                    "🔒 Password untuk " + username + ":\n\n" +
                    "(Default password: password123)",
                    "TEST REAL LOGIN",
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (password != null && !password.trim().isEmpty()) {
                    String[] roles = {"petugas", "supervisor", "admin"};
                    String role = (String) JOptionPane.showInputDialog(
                        null,
                        "👤 Pilih role untuk login:",
                        "SELECT ROLE",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        roles,
                        roles[0]
                    );
                    
                    if (role != null) {
                        performLogin(username, password, role);
                    }
                }
            }
        });
    }
    
    /**
     * Test dummy login tanpa database
     */
    private static void testDummyLogin(String role) {
        SwingUtilities.invokeLater(() -> {
            SessionManager sessionManager = SessionManager.getInstance();
            User dummyUser = sessionManager.loginForTesting(role);
            
            if (dummyUser != null) {
                JOptionPane.showMessageDialog(
                    null,
                    "✅ Dummy login berhasil!\n\n" +
                    "User: " + dummyUser.getDisplayName() + "\n" +
                    "Role: " + dummyUser.getRoleDisplayName() + "\n\n" +
                    "Membuka dashboard...",
                    "DUMMY LOGIN SUCCESS",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                openDashboard(dummyUser);
            }
        });
    }
    
    /**
     * Perform real login
     */
    private static void performLogin(String username, String password, String role) {
        // Show loading
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                null,
                "⏳ Melakukan authentication...\n\n" +
                "Username: " + username + "\n" +
                "Role: " + role,
                "AUTHENTICATING",
                JOptionPane.INFORMATION_MESSAGE
            );
        });
        
        // Perform authentication in background
        new Thread(() -> {
            try {
                SessionManager sessionManager = SessionManager.getInstance();
                User authenticatedUser = sessionManager.authenticate(username, password, role);
                
                SwingUtilities.invokeLater(() -> {
                    if (authenticatedUser != null) {
                        JOptionPane.showMessageDialog(
                            null,
                            "✅ Login berhasil!\n\n" +
                            "Welcome, " + authenticatedUser.getDisplayName() + "!\n" +
                            "Role: " + authenticatedUser.getRoleDisplayName() + "\n" +
                            "User ID: " + authenticatedUser.getId() + "\n\n" +
                            "Membuka dashboard...",
                            "LOGIN SUCCESS",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        
                        openDashboard(authenticatedUser);
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "❌ Login gagal!\n\n" +
                            "Username/password salah atau role tidak sesuai.\n" +
                            "Pastikan:\n" +
                            "• Username/email benar\n" +
                            "• Password benar (default: password123)\n" +
                            "• Role sesuai dengan akun",
                            "LOGIN FAILED",
                            JOptionPane.ERROR_MESSAGE
                        );
                        
                        // Show menu again
                        showTestMenu();
                    }
                });
                
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                        null,
                        "❌ Error saat login:\n\n" +
                        e.getMessage(),
                        "LOGIN ERROR",
                        JOptionPane.ERROR_MESSAGE
                    );
                    
                    showTestMenu();
                });
            }
        }).start();
    }
    
    /**
     * Open dashboard based on user role
     */
    private static void openDashboard(User user) {
        SwingUtilities.invokeLater(() -> {
            try {
                switch (user.getRole()) {
                    case PETUGAS:
                    case ADMIN:
                        DashboardPetugas dashboardPetugas = new DashboardPetugas(user);
                        dashboardPetugas.setVisible(true);
                        break;
                        
                    case SUPERVISOR:
                        DashboardSuperVisor dashboardSupervisor = new DashboardSuperVisor(user);
                        dashboardSupervisor.setVisible(true);
                        break;
                        
                    default:
                        JOptionPane.showMessageDialog(
                            null,
                            "❌ Role tidak dikenali: " + user.getRole(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE
                        );
                        break;
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    null,
                    "❌ Error membuka dashboard:\n\n" +
                    e.getMessage(),
                    "DASHBOARD ERROR",
                    JOptionPane.ERROR_MESSAGE
                );
                
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Open normal login form
     */
    private static void openLoginForm() {
        SwingUtilities.invokeLater(() -> {
            try {
                Auth.FormLogin loginForm = new Auth.FormLogin();
                loginForm.setVisible(true);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    null,
                    "❌ Error membuka login form:\n\n" +
                    e.getMessage(),
                    "FORM ERROR",
                    JOptionPane.ERROR_MESSAGE
                );
                
                e.printStackTrace();
            }
        });
    }
}

