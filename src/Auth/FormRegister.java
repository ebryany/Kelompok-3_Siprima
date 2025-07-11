/*
 * SIPRIMA Desa Tarabbi - Registration Form
 * Sistem Pengaduan Masyarakat Desa Tarabbi
 */
package Auth;

import Utils.DatabaseConfig;
import Utils.SessionManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author febry
 */
public class FormRegister extends javax.swing.JFrame {
    
    // SIPRIMA Official Colors
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);         // #2980B9
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);           // #2C3E50
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);      // #7F8C8D
    private static final Color PLACEHOLDER_COLOR = new Color(149, 165, 166);   // #95A5A6
    private static final Color BORDER_COLOR = new Color(220, 221, 225);        // #DCDDE1
    private static final Color BG_PRIMARY = new Color(236, 240, 241);          // #ECF0F1
    
    private boolean[] isPlaceholder = new boolean[5]; // For tracking placeholder state

    /**
     * Creates new form FormRegister
     */
    public FormRegister() {
        initComponents();
        setupEventHandlers();
        setupWindowProperties();
        customizeModernComponents();
        setupPlaceholders();
    }
    
    /**
     * Setup window properties
     */
    private void setupWindowProperties() {
        setTitle("SIPRIMA Desa Tarabbi - Register Sistem");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 400));
        setPreferredSize(new Dimension(900, 650));
        
        // Set app icon (simplified)
        try {
            setIconImage(createAppIcon());
        } catch (Exception e) {
            System.out.println("⚠️ Could not set app icon: " + e.getMessage());
        }
        
        pack();
    }
    
    private Image createAppIcon() {
        // Create simple icon for app
        try {
            BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = icon.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw SIPRIMA icon
            g2.setColor(PRIMARY_BLUE);
            g2.fillRoundRect(2, 2, 28, 28, 8, 8);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
            FontMetrics fm = g2.getFontMetrics();
            int x = (32 - fm.stringWidth("S")) / 2;
            int y = (32 - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString("S", x, y);
            
            g2.dispose();
            return icon;
        } catch (Exception e) {
            System.out.println("⚠️ Error creating app icon: " + e.getMessage());
            // Return default icon if creation fails
            return new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        }
    }
    
    /**
     * Customize components untuk modern look sesuai SIPRIMA_UI_DESIGN.md
     */
    private void customizeModernComponents() {
        // Modern header styling
        headerPanel.setBackground(new Color(41, 128, 185));
        
        // Back button styling
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(PRIMARY_BLUE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 200), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setOpaque(true);
        
        // Home button styling
        btnHome.setBackground(Color.WHITE);
        btnHome.setForeground(PRIMARY_BLUE);
        btnHome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 200), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.setOpaque(true);
        
        // Title styling
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // Main content styling
        mainContentPanel.setBackground(BG_PRIMARY);
        
        // Register card styling
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        // Form title styling
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitleLabel.setForeground(PRIMARY_BLUE);
        
        // Register button styling
        customizeModernButton(btnRegister, PRIMARY_BLUE, "📤 DAFTAR AKUN", "Daftar akun baru");
        
        // Role selection styling
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(TEXT_SECONDARY);
        
        customizeRadioButton(rbPetugas, "Petugas");
        customizeRadioButton(rbSupervisor, "Supervisor");
        customizeRadioButton(rbAdmin, "Admin");
        
        // Checkbox styling
        chkAgreeTerms.setBackground(Color.WHITE);
        chkAgreeTerms.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkAgreeTerms.setForeground(TEXT_SECONDARY);
        chkAgreeTerms.setFocusPainted(false);
        chkAgreeTerms.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Login link styling
        loginLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginLink.setForeground(PRIMARY_BLUE);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Customize radio button dengan modern styling
     */
    private void customizeRadioButton(JRadioButton radio, String text) {
        radio.setBackground(Color.WHITE);
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        radio.setForeground(TEXT_SECONDARY);
        radio.setFocusPainted(false);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Customize button untuk modern appearance
     */
    private void customizeModernButton(JButton button, Color bgColor, String text, String tooltip) {
        button.setText(text);
        button.setToolTipText(tooltip);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(brighter(bgColor, 0.1f), 1),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(350, 50));
        
        // Add modern hover effect
        Color hoverColor = brighter(bgColor, 0.2f);
        Color normalColor = bgColor;
        
        button.addMouseListener(new ButtonHoverListener(button, normalColor, hoverColor));
    }
    
    /**
     * Utility method to create brighter color
     */
    private Color brighter(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() * (1 + factor)));
        int g = Math.min(255, (int)(color.getGreen() * (1 + factor)));
        int b = Math.min(255, (int)(color.getBlue() * (1 + factor)));
        return new Color(r, g, b);
    }
    
    /**
     * Utility method to create darker color
     */
    private Color darker(Color color, float factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
    
    private void setupPlaceholders() {
        // Setup custom placeholders without interfering with NetBeans
        customizeTextField(txtFullName, "👤 Nama Lengkap", 0);
        customizeTextField(txtUsername, "👤 Username", 1);
        customizeTextField(txtEmail, "📧 Email", 2);
        customizePasswordField(txtPassword, "🔒 Password", 3);
        customizePasswordField(txtConfirmPassword, "🔒 Konfirmasi Password", 4);
    }
    
    /**
     * Customize text field dengan modern styling
     */
    private void customizeTextField(JTextField field, String placeholder, int index) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        field.setPreferredSize(new Dimension(350, 44));
        
        // Add placeholder behavior
        field.setText(placeholder);
        field.setForeground(PLACEHOLDER_COLOR);
        isPlaceholder[index] = true;
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isPlaceholder[index]) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                    isPlaceholder[index] = false;
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_BLUE, 2),
                    BorderFactory.createEmptyBorder(11, 15, 11, 15)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(PLACEHOLDER_COLOR);
                    isPlaceholder[index] = true;
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
        });
    }
    
    /**
     * Customize password field dengan modern styling
     */
    private void customizePasswordField(JPasswordField field, String placeholder, int index) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        field.setPreferredSize(new Dimension(350, 44));
        
        // Add placeholder behavior
        field.setText(placeholder);
        field.setForeground(PLACEHOLDER_COLOR);
        field.setEchoChar((char) 0); // Show placeholder text
        isPlaceholder[index] = true;
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isPlaceholder[index]) {
                    field.setText("");
                    field.setForeground(TEXT_PRIMARY);
                    field.setEchoChar('•'); // Set echo character for password
                    isPlaceholder[index] = false;
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_BLUE, 2),
                    BorderFactory.createEmptyBorder(11, 15, 11, 15)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setText(placeholder);
                    field.setForeground(PLACEHOLDER_COLOR);
                    field.setEchoChar((char) 0); // Show placeholder text
                    isPlaceholder[index] = true;
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
        });
    }
    
    /**
     * Setup event handlers untuk komponen
     */
    private void setupEventHandlers() {
        // Back button
        btnBack.addActionListener(e -> {
            new FormLogin().setVisible(true);
            dispose();
        });
        
        // Home button
        btnHome.addActionListener(e -> {
            new FormLogin().setVisible(true);
            dispose();
        });
        
        // Login link
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new FormLogin().setVisible(true);
                dispose();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                loginLink.setText("<html><u>Sudah punya akun? Masuk di sini</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginLink.setText("Sudah punya akun? Masuk di sini");
            }
        });
        
        // Register button
        btnRegister.addActionListener(e -> handleRegistration());
        
        // Enter key support
        KeyListener enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleRegistration();
                }
            }
        };
        
        txtFullName.addKeyListener(enterKeyListener);
        txtUsername.addKeyListener(enterKeyListener);
        txtEmail.addKeyListener(enterKeyListener);
        txtPassword.addKeyListener(enterKeyListener);
        txtConfirmPassword.addKeyListener(enterKeyListener);
    }
    
    
    private void handleRegistration() {
        // Show loading state
        btnRegister.setEnabled(false);
        btnRegister.setText("⏳ MENDAFTAR...");
        
        // Validate input
        if (!validateInput()) {
            resetRegisterButton();
            return;
        }
        
        // Get values
        String fullName = isPlaceholder[0] ? "" : txtFullName.getText().trim();
        String username = isPlaceholder[1] ? "" : txtUsername.getText().trim();
        String email = isPlaceholder[2] ? "" : txtEmail.getText().trim();
        String password = isPlaceholder[3] ? "" : new String(txtPassword.getPassword());
        String confirmPassword = isPlaceholder[4] ? "" : new String(txtConfirmPassword.getPassword());
        String role = getSelectedRole();
        
        // Register user in background thread
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return registerUser(fullName, username, email, password, role);
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    resetRegisterButton();
                    
                    if (success) {
                        // Get user data for splash screen
                        String username = isPlaceholder[1] ? "" : txtUsername.getText().trim();
                        String role = getSelectedRole();
                        
                        // Show splash screen with user info and auto-redirect to dashboard
                        new FormSplashScreen(username, role).setVisible(true);
                        dispose();
                    }
                } catch (Exception e) {
                    resetRegisterButton();
                    JOptionPane.showMessageDialog(FormRegister.this,
                        "❌ Terjadi kesalahan saat registrasi: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    private void resetRegisterButton() {
        btnRegister.setEnabled(true);
        btnRegister.setText("📤 DAFTAR AKUN");
    }
    
    private void showSuccessDialog() {
        String message = "✅ Registrasi berhasil!\n\n" +
                        "Akun Anda telah dibuat dengan detail berikut:\n" +
                        "• Username: " + txtUsername.getText().trim() + "\n" +
                        "• Email: " + txtEmail.getText().trim() + "\n" +
                        "• Role: " + getSelectedRole().toUpperCase() + "\n\n" +
                        "Silakan login dengan akun Anda.";
        
        JOptionPane.showMessageDialog(this,
            message,
            "🎉 Registrasi Sukses",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private boolean validateInput() {
        // Check if terms are agreed
        if (!chkAgreeTerms.isSelected()) {
            JOptionPane.showMessageDialog(this, 
                "Anda harus menyetujui syarat dan ketentuan.", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Check required fields
        if (isPlaceholder[0] || txtFullName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nama lengkap harus diisi.", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtFullName.requestFocus();
            return false;
        }
        
        if (isPlaceholder[1] || txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username harus diisi.", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return false;
        }
        
        if (isPlaceholder[2] || txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Email harus diisi.", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        if (isPlaceholder[3] || txtPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, 
                "Password harus diisi.", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return false;
        }
        
        if (isPlaceholder[4] || txtConfirmPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, 
                "Konfirmasi password harus diisi.", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtConfirmPassword.requestFocus();
            return false;
        }
        
        // Validate email format
        String email = txtEmail.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, 
                "Format email tidak valid.", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        // Validate password length
        String password = new String(txtPassword.getPassword());
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "Password minimal 6 karakter.", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return false;
        }
        
        // Validate password confirmation
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Konfirmasi password tidak cocok.", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtConfirmPassword.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private String getSelectedRole() {
        if (rbPetugas.isSelected()) return "petugas";
        if (rbSupervisor.isSelected()) return "supervisor";
        if (rbAdmin.isSelected()) return "admin";
        return "petugas"; // default
    }
    
    private boolean registerUser(String fullName, String username, String email, String password, String role) {
        try {
            Connection conn = DatabaseConfig.getConnection();
            
            // Check if username already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                checkStmt.setString(2, email);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) > 0) {
                    // Check which one exists
                    String duplicateCheckQuery = "SELECT username, email FROM users WHERE username = ? OR email = ?";
                    try (PreparedStatement dupStmt = conn.prepareStatement(duplicateCheckQuery)) {
                        dupStmt.setString(1, username);
                        dupStmt.setString(2, email);
                        ResultSet dupRs = dupStmt.executeQuery();
                        
                        if (dupRs.next()) {
                            String existingUsername = dupRs.getString("username");
                            String existingEmail = dupRs.getString("email");
                            
                            String errorMsg = "❌ Registrasi gagal!\n\n";
                            if (username.equals(existingUsername)) {
                                errorMsg += "Username '" + username + "' sudah digunakan.";
                            } else if (email.equals(existingEmail)) {
                                errorMsg += "Email '" + email + "' sudah terdaftar.";
                            }
                            errorMsg += "\n\nSilakan gunakan username/email yang berbeda.";
                            
                            JOptionPane.showMessageDialog(this,
                                errorMsg,
                                "Registrasi Gagal",
                                JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                }
            }
            
            // Hash password securely
            String hashedPassword = hashPassword(password);
            
            // Insert new user with proper database structure
String insertQuery = "INSERT INTO users (nama_lengkap, username, email, password_hash, role, is_active, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, fullName);
                insertStmt.setString(2, username);
                insertStmt.setString(3, email);
                insertStmt.setString(4, hashedPassword);
                insertStmt.setString(5, role);
                insertStmt.setBoolean(6, true); // is_active
                
                int rowsAffected = insertStmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("✅ User registered successfully: " + username + " (" + role + ")");
                    return true;
                } else {
                    System.err.println("❌ Failed to insert user");
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Database error during registration: " + e.getMessage());
            e.printStackTrace();
            
            String userMessage = "❌ Terjadi kesalahan database saat registrasi.\n\n";
            if (e.getMessage().contains("Duplicate entry")) {
                userMessage += "Username atau email sudah digunakan.";
            } else if (e.getMessage().contains("Data too long")) {
                userMessage += "Data yang dimasukkan terlalu panjang.";
            } else {
                userMessage += "Periksa koneksi database dan coba lagi.";
            }
            userMessage += "\n\nError: " + e.getMessage();
            
            JOptionPane.showMessageDialog(this,
                userMessage,
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Hash password using SHA-256 with salt
     * For production, consider using BCrypt or similar
     */
    
    /**
     * Hash password using SHA-256 with salt
     * For production, consider using BCrypt or similar
     */
    private String hashPassword(String password) {
        try {
            // Generate salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            
            // Hash password with salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes("UTF-8"));
            
            // Combine salt and hash
            byte[] saltedHash = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, saltedHash, 0, salt.length);
            System.arraycopy(hashedPassword, 0, saltedHash, salt.length, hashedPassword.length);
            
            // Return Base64 encoded result
            return Base64.getEncoder().encodeToString(saltedHash);
            
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            System.err.println("❌ Error hashing password: " + e.getMessage());
            // Fallback: return original password (NOT SECURE - for development only)
            return password;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roleButtonGroup = new javax.swing.ButtonGroup();
        headerPanel = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();
        btnHome = new javax.swing.JButton();
        mainContentPanel = new javax.swing.JPanel();
        registerPanel = new javax.swing.JPanel();
        formTitleLabel = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        txtConfirmPassword = new javax.swing.JPasswordField();
        roleLabel = new javax.swing.JLabel();
        rolePanel = new javax.swing.JPanel();
        rbPetugas = new javax.swing.JRadioButton();
        rbSupervisor = new javax.swing.JRadioButton();
        rbAdmin = new javax.swing.JRadioButton();
        chkAgreeTerms = new javax.swing.JCheckBox();
        btnRegister = new javax.swing.JButton();
        loginLink = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIPRIMA Register");

        headerPanel.setBackground(new java.awt.Color(41, 128, 185));
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setPreferredSize(new java.awt.Dimension(900, 80));

        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBack.setForeground(new java.awt.Color(41, 128, 185));
        btnBack.setText("< Kembali");
        btnBack.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.setFocusPainted(false);

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("DAFTAR AKUN");

        btnHome.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHome.setForeground(new java.awt.Color(41, 128, 185));
        btnHome.setText("Beranda");
        btnHome.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome.setFocusPainted(false);

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHome))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(titleLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        mainContentPanel.setBackground(new java.awt.Color(236, 240, 241));
        mainContentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(50, 50, 50, 50));

        registerPanel.setBackground(new java.awt.Color(255, 255, 255));
        registerPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40)));
        registerPanel.setPreferredSize(new java.awt.Dimension(450, 600));

        formTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        formTitleLabel.setForeground(new java.awt.Color(41, 128, 185));
        formTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        formTitleLabel.setText("DAFTAR AKUN BARU");

        txtFullName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtFullName.setText("Nama Lengkap");
        txtFullName.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        txtFullName.setPreferredSize(new java.awt.Dimension(350, 44));

        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtUsername.setText("Username");
        txtUsername.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        txtUsername.setPreferredSize(new java.awt.Dimension(350, 44));

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtEmail.setText("Email");
        txtEmail.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        txtEmail.setPreferredSize(new java.awt.Dimension(350, 44));

        txtPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPassword.setText("password");
        txtPassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        txtPassword.setPreferredSize(new java.awt.Dimension(350, 44));

        txtConfirmPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtConfirmPassword.setText("confirmpassword");
        txtConfirmPassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        txtConfirmPassword.setPreferredSize(new java.awt.Dimension(350, 44));

        roleLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        roleLabel.setForeground(new java.awt.Color(127, 140, 141));
        roleLabel.setText("Role:");

        rolePanel.setBackground(new java.awt.Color(255, 255, 255));

        rbPetugas.setBackground(new java.awt.Color(255, 255, 255));
        roleButtonGroup.add(rbPetugas);
        rbPetugas.setForeground(new java.awt.Color(127, 140, 141));
        rbPetugas.setSelected(true);
        rbPetugas.setText("Petugas");
        rbPetugas.setFocusPainted(false);

        rbSupervisor.setBackground(new java.awt.Color(255, 255, 255));
        roleButtonGroup.add(rbSupervisor);
        rbSupervisor.setForeground(new java.awt.Color(127, 140, 141));
        rbSupervisor.setText("Supervisor");
        rbSupervisor.setFocusPainted(false);

        rbAdmin.setBackground(new java.awt.Color(255, 255, 255));
        roleButtonGroup.add(rbAdmin);
        rbAdmin.setForeground(new java.awt.Color(127, 140, 141));
        rbAdmin.setText("Admin");
        rbAdmin.setFocusPainted(false);

        javax.swing.GroupLayout rolePanelLayout = new javax.swing.GroupLayout(rolePanel);
        rolePanel.setLayout(rolePanelLayout);
        rolePanelLayout.setHorizontalGroup(
            rolePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rolePanelLayout.createSequentialGroup()
                .addComponent(rbPetugas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbSupervisor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbAdmin)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        rolePanelLayout.setVerticalGroup(
            rolePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rolePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(rbPetugas)
                .addComponent(rbSupervisor)
                .addComponent(rbAdmin))
        );

        chkAgreeTerms.setBackground(new java.awt.Color(255, 255, 255));
        chkAgreeTerms.setForeground(new java.awt.Color(127, 140, 141));
        chkAgreeTerms.setText("Saya setuju dengan syarat dan ketentuan");
        chkAgreeTerms.setFocusPainted(false);

        btnRegister.setBackground(new java.awt.Color(41, 128, 185));
        btnRegister.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnRegister.setForeground(new java.awt.Color(255, 255, 255));
        btnRegister.setText("DAFTAR AKUN");
        btnRegister.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 25, 15, 25));
        btnRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegister.setFocusPainted(false);
        btnRegister.setPreferredSize(new java.awt.Dimension(350, 50));

        loginLink.setForeground(new java.awt.Color(41, 128, 185));
        loginLink.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginLink.setText("Sudah punya akun? Masuk di sini");
        loginLink.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout registerPanelLayout = new javax.swing.GroupLayout(registerPanel);
        registerPanel.setLayout(registerPanelLayout);
        registerPanelLayout.setHorizontalGroup(
            registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(formTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtFullName, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
            .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtConfirmPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(registerPanelLayout.createSequentialGroup()
                .addGroup(registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roleLabel)
                    .addComponent(rolePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(chkAgreeTerms, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(loginLink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        registerPanelLayout.setVerticalGroup(
            registerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registerPanelLayout.createSequentialGroup()
                .addComponent(formTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(roleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rolePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(chkAgreeTerms)
                .addGap(25, 25, 25)
                .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(loginLink)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainContentPanelLayout = new javax.swing.GroupLayout(mainContentPanel);
        mainContentPanel.setLayout(mainContentPanelLayout);
        mainContentPanelLayout.setHorizontalGroup(
            mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContentPanelLayout.createSequentialGroup()
                .addContainerGap(175, Short.MAX_VALUE)
                .addComponent(registerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(184, Short.MAX_VALUE))
        );
        mainContentPanelLayout.setVerticalGroup(
            mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContentPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(registerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 909, Short.MAX_VALUE)
            .addComponent(mainContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormRegister.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormRegister().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnRegister;
    private javax.swing.JCheckBox chkAgreeTerms;
    private javax.swing.JLabel formTitleLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel loginLink;
    private javax.swing.JPanel mainContentPanel;
    private javax.swing.JRadioButton rbAdmin;
    private javax.swing.JRadioButton rbPetugas;
    private javax.swing.JRadioButton rbSupervisor;
    private javax.swing.JPanel registerPanel;
    private javax.swing.ButtonGroup roleButtonGroup;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JPanel rolePanel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Button hover listener untuk menghindari anonymous class yang membuat file $class
     */
    private class ButtonHoverListener extends MouseAdapter {
        private JButton button;
        private Color normalColor;
        private Color hoverColor;
        
        public ButtonHoverListener(JButton button, Color normalColor, Color hoverColor) {
            this.button = button;
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(hoverColor);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(brighter(hoverColor, 0.1f), 2),
                BorderFactory.createEmptyBorder(14, 24, 14, 24)
            ));
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(normalColor);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(brighter(normalColor, 0.1f), 1),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)
            ));
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            button.setBackground(darker(normalColor, 0.1f));
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            button.setBackground(hoverColor);
        }
    }
}
