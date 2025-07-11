/*
 * SIPRIMA Desa Tarabbi - Modern Login Form
 * Sistem Pengaduan Masyarakat Desa Tarabbi
 */
package Auth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * @author febry
 */
public class FormLogin extends javax.swing.JFrame {
    
    // Login state
    private String selectedRole = "petugas";
    private boolean rememberMe = false;
    
    // Theme properties - warna biru langit yang lebih gelap untuk kontras yang lebih baik
    private Color currentThemeColor = new Color(41, 128, 185); // Biru langit gelap untuk kontras tulisan putih
    
    // Password visibility toggle
    private boolean isPasswordVisible = false;
    private JLabel lblPasswordToggle;
    private ImageIcon eyeOpenIcon;
    private ImageIcon eyeClosedIcon;
    private Timer animationTimer;
    
    // Responsive properties
    private boolean isCompactMode = false;
    private final int COMPACT_BREAKPOINT = 800; // Width threshold for compact mode
    private final int MOBILE_BREAKPOINT = 600;  // Width threshold for mobile mode
    
    /**
     * Creates new form FormLogin
     */
    public FormLogin() {
        initComponents();
        setupEventHandlers();
        setupWindowProperties();
        customizeModernComponents();
        setupPasswordToggle();
    }
    
    
    /**
     * Setup event handlers untuk komponen
     */
    private void setupEventHandlers() {
        // Role selection dengan dynamic title
        rbPetugas.addActionListener(e -> {
            selectedRole = "petugas";
            formTitleLabel.setText("LOGIN PETUGAS");
        });
        
        rbSupervisor.addActionListener(e -> {
            selectedRole = "supervisor";
            formTitleLabel.setText("LOGIN SUPERVISOR");
        });
        
        rbAdmin.addActionListener(e -> {
            selectedRole = "admin";
            formTitleLabel.setText("LOGIN ADMIN");
        });
        
        // Button actions  
        btnLogin.addActionListener(e -> btnLoginActionPerformed(e));
        
        // Forgot password link
        lblForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(FormLogin.this,
                    "Hubungi administrator untuk reset password:\n" +
                    "Telepon: (0411) 123-4567\n" +
                    "Email: admin@desatarabbi.go.id",
                    "LUPA PASSWORD",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Password toggle akan ditambahkan secara programatic di setupPasswordToggle()
        // Tidak menggunakan NetBeans form components untuk password toggle
    }
    
    /**
     * Setup fitur password toggle dengan transisi icon eye.png dan close-eye.png
     */
    private void setupPasswordToggle() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Load kedua icon dari folder icon
                loadPasswordToggleIcons();
                
                // Buat label untuk icon mata dengan icon awal (mata tertutup = password tersembunyi)
                if (eyeClosedIcon != null) {
                    lblPasswordToggle = new JLabel(eyeClosedIcon);
                    System.out.println("✅ Icon eye.png dan close-eye.png berhasil dimuat!");
                } else {
                    lblPasswordToggle = new JLabel("🙈");
                    lblPasswordToggle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    System.out.println("⚠️ Icon tidak ditemukan, menggunakan emoji fallback");
                }
                
                lblPasswordToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
                lblPasswordToggle.setToolTipText("Klik untuk menampilkan password");
                lblPasswordToggle.setSize(20, 20);
                
                // Tambahkan click listener dengan transisi keren
                lblPasswordToggle.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        togglePasswordWithAnimation();
                    }
                    
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        // Efek hover: sedikit scale up
                        addHoverEffect(true);
                        lblPasswordToggle.setToolTipText(
                            isPasswordVisible ? "Sembunyikan password" : "Tampilkan password"
                        );
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        // Remove hover effect
                        addHoverEffect(false);
                    }
                });
                
                // Tambahkan icon ke dalam password field sebagai overlay
                addPasswordToggleToField();
                
            } catch (Exception e) {
                System.out.println("❌ Error menambahkan password toggle: " + e.getMessage());
            }
        });
    }
    
    /**
     * Menambahkan icon toggle ke password field
     */
    private void addPasswordToggleToField() {
        Container parent = txtPassword.getParent();
        if (parent != null) {
            // Tambahkan icon ke parent container
            parent.add(lblPasswordToggle);
            parent.setComponentZOrder(lblPasswordToggle, 0); // Bawa ke depan
            
            // Posisikan icon di sebelah kanan password field
            positionPasswordToggleIcon();
            
            // Tambahkan listener untuk reposition saat resize
            parent.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    SwingUtilities.invokeLater(() -> positionPasswordToggleIcon());
                }
            });
            
            lblPasswordToggle.setVisible(true);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    /**
     * Posisikan icon mata di sebelah kanan password field
     */
    private void positionPasswordToggleIcon() {
        if (lblPasswordToggle != null && txtPassword != null && txtPassword.isDisplayable()) {
            // Hitung posisi icon di dalam area password field
            int x = txtPassword.getX() + txtPassword.getWidth() - 30; // 30px dari kanan
            int y = txtPassword.getY() + (txtPassword.getHeight() - 20) / 2; // center vertical
            
            lblPasswordToggle.setBounds(x, y, 20, 20);
        }
    }
    
    /**
     * Load kedua icon menggunakan IconLoader utility
     */
    private void loadPasswordToggleIcons() {
        try {
            // Gunakan IconLoader untuk loading icons
            eyeOpenIcon = Utils.IconLoader.Icons.EYE;
            eyeClosedIcon = Utils.IconLoader.Icons.EYE_CLOSED;
            
            System.out.println("✅ Icons loaded via IconLoader: " + 
                (eyeOpenIcon != null ? "✓" : "✗") + " eye.png, " +
                (eyeClosedIcon != null ? "✓" : "✗") + " close-eye.png");
                
        } catch (Exception e) {
            System.out.println("❌ Error loading password toggle icons: " + e.getMessage());
            
            // Fallback to manual loading if IconLoader fails
            loadPasswordToggleIconsManual();
        }
    }
    
    /**
     * Fallback manual loading (original implementation)
     */
    private void loadPasswordToggleIconsManual() {
        try {
            // Load icon mata terbuka (eye.png)
            java.net.URL eyeOpenURL = getClass().getResource("/icon/eye.png");
            if (eyeOpenURL != null) {
                ImageIcon originalEyeOpen = new ImageIcon(eyeOpenURL);
                Image eyeOpenImg = originalEyeOpen.getImage();
                Image resizedEyeOpen = eyeOpenImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                eyeOpenIcon = new ImageIcon(resizedEyeOpen);
            }
            
            // Load icon mata tertutup (close-eye.png)
            java.net.URL eyeClosedURL = getClass().getResource("/icon/close-eye.png");
            if (eyeClosedURL != null) {
                ImageIcon originalEyeClosed = new ImageIcon(eyeClosedURL);
                Image eyeClosedImg = originalEyeClosed.getImage();
                Image resizedEyeClosed = eyeClosedImg.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                eyeClosedIcon = new ImageIcon(resizedEyeClosed);
            }
            
            System.out.println("✅ Manual fallback loading completed: " + 
                (eyeOpenIcon != null ? "✓" : "✗") + " eye.png, " +
                (eyeClosedIcon != null ? "✓" : "✗") + " close-eye.png");
                
        } catch (Exception e) {
            System.out.println("❌ Error in manual icon loading: " + e.getMessage());
        }
    }
    
    /**
     * Toggle password dengan animasi transisi yang keren
     */
    private void togglePasswordWithAnimation() {
        // Stop animasi sebelumnya jika masih berjalan
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        
        // Toggle state
        isPasswordVisible = !isPasswordVisible;
        
        // Update password field
        if (isPasswordVisible) {
            txtPassword.setEchoChar((char) 0); // Show password
        } else {
            txtPassword.setEchoChar('•'); // Hide password
        }
        
        // Mulai animasi transisi icon
        if (eyeOpenIcon != null && eyeClosedIcon != null) {
            startIconTransitionAnimation();
        } else {
            // Fallback ke toggle emoji biasa
            togglePasswordVisibility();
        }
    }
    
    /**
     * Mulai animasi transisi antara icon mata terbuka dan tertutup
     */
    private void startIconTransitionAnimation() {
        final int ANIMATION_STEPS = 8;
        final int ANIMATION_DELAY = 30; // milliseconds
        final int[] currentStep = {0};
        
        // Icon target berdasarkan state yang benar:
        // isPasswordVisible = false → close-eye.png (password tersembunyi)
        // isPasswordVisible = true → eye.png (password terlihat)
        ImageIcon fromIcon = isPasswordVisible ? eyeClosedIcon : eyeOpenIcon;
        ImageIcon toIcon = isPasswordVisible ? eyeOpenIcon : eyeClosedIcon;
        
        animationTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentStep[0]++;
                
                if (currentStep[0] <= ANIMATION_STEPS / 2) {
                    // Fase 1: Scale down dengan fade out
                    float scale = 1.0f - (currentStep[0] * 0.3f / (ANIMATION_STEPS / 2));
                    float alpha = 1.0f - (currentStep[0] * 0.7f / (ANIMATION_STEPS / 2));
                    
                    // Create scaled icon dengan alpha
                    ImageIcon scaledIcon = createScaledAlphaIcon(fromIcon, scale, alpha);
                    lblPasswordToggle.setIcon(scaledIcon);
                    
                } else if (currentStep[0] == ANIMATION_STEPS / 2 + 1) {
                    // Fase 2: Switch icon di tengah animasi
                    lblPasswordToggle.setIcon(toIcon);
                    
                } else if (currentStep[0] <= ANIMATION_STEPS) {
                    // Fase 3: Scale up dengan fade in
                    int step = currentStep[0] - (ANIMATION_STEPS / 2 + 1);
                    float scale = 0.7f + (step * 0.3f / (ANIMATION_STEPS / 2));
                    float alpha = 0.3f + (step * 0.7f / (ANIMATION_STEPS / 2));
                    
                    // Create scaled icon dengan alpha
                    ImageIcon scaledIcon = createScaledAlphaIcon(toIcon, scale, alpha);
                    lblPasswordToggle.setIcon(scaledIcon);
                    
                } else {
                    // Animasi selesai - set icon final
                    lblPasswordToggle.setIcon(toIcon);
                    animationTimer.stop();
                    
                    // Update tooltip
                    lblPasswordToggle.setToolTipText(
                        isPasswordVisible ? "Sembunyikan password" : "Tampilkan password"
                    );
                }
                
                // Repaint untuk smooth animation
                lblPasswordToggle.repaint();
            }
        });
        
        animationTimer.start();
    }
    
    /**
     * Create icon dengan scale dan alpha untuk efek animasi
     */
    private ImageIcon createScaledAlphaIcon(ImageIcon originalIcon, float scale, float alpha) {
        if (originalIcon == null) return null;
        
        int newWidth = Math.max(1, (int)(16 * scale));
        int newHeight = Math.max(1, (int)(16 * scale));
        
        // Create buffered image untuk manipulasi alpha
        BufferedImage bufferedImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        
        // Set alpha composite
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        // Scale dan draw image di center
        int x = (16 - newWidth) / 2;
        int y = (16 - newHeight) / 2;
        
        g2d.drawImage(originalIcon.getImage(), x, y, newWidth, newHeight, null);
        g2d.dispose();
        
        return new ImageIcon(bufferedImage);
    }
    
    /**
     * Tambahkan efek hover pada icon mata
     */
    private void addHoverEffect(boolean isHovered) {
        if (lblPasswordToggle == null) return;
        
        if (isHovered) {
            // Efek hover: sedikit scale up dengan brightness
            // Logika yang benar: isPasswordVisible = true → eye.png, false → close-eye.png
            ImageIcon currentIcon = isPasswordVisible ? eyeOpenIcon : eyeClosedIcon;
            if (currentIcon != null) {
                ImageIcon hoverIcon = createScaledAlphaIcon(currentIcon, 1.1f, 1.0f);
                lblPasswordToggle.setIcon(hoverIcon);
            }
            
            // Background highlight
            lblPasswordToggle.setOpaque(true);
            lblPasswordToggle.setBackground(new Color(41, 128, 185, 30)); // Slight blue background
            
        } else {
            // Remove hover effect
            // Logika yang benar: isPasswordVisible = true → eye.png, false → close-eye.png
            ImageIcon normalIcon = isPasswordVisible ? eyeOpenIcon : eyeClosedIcon;
            if (normalIcon != null) {
                lblPasswordToggle.setIcon(normalIcon);
            }
            
            lblPasswordToggle.setOpaque(false);
        }
        
        lblPasswordToggle.repaint();
    }
    
    /**
     * Toggle visibility password (simple fallback method)
     */
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        
        if (isPasswordVisible) {
            // Tampilkan password sebagai text
            txtPassword.setEchoChar((char) 0); // Disable echo char
        } else {
            // Sembunyikan password dengan bintang
            txtPassword.setEchoChar('•'); // Enable echo char dengan bullet
        }
        
        // Refresh komponen
        txtPassword.repaint();
    }
    
    /**
     * Customize components untuk modern look sesuai SIPRIMA_UI_DESIGN.md
     */
    private void customizeModernComponents() {
        // Modern header styling
        headerPanel.setBackground(createGradientColor(currentThemeColor, 0.2f));
        
        // Navigation buttons removed for cleaner login interface
        
        // Title styling
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        // Main content styling
        mainContentPanel.setBackground(new Color(236, 240, 241));
        
        // Login card styling
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 221, 225), 1, true),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        // Form title styling
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitleLabel.setForeground(currentThemeColor);
        
        // Add users-alt icon to form title
        addIconToFormTitle();
        
        // Fingerprint icon removed - simplified interface
        
        // Input field styling
        customizeTextField(txtUsername, "Username atau Email");
        customizeTextField(txtPassword, "Password");
        
        // Checkbox styling
        chkRememberMe.setBackground(Color.WHITE);
        chkRememberMe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkRememberMe.setForeground(new Color(127, 140, 141));
        
        // Forgot password link styling
        lblForgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblForgotPassword.setForeground(currentThemeColor);
        lblForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Login button styling
        customizeModernButton(btnLogin, currentThemeColor, "MASUK SISTEM", "Login ke dashboard");
        
        // Add icon to login button
        addIconToLoginButton();
        
        // Role selection styling
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(new Color(127, 140, 141));
        
        customizeRadioButton(rbPetugas, "Petugas");
        customizeRadioButton(rbSupervisor, "Supervisor");
        customizeRadioButton(rbAdmin, "Admin");
        
        // Help text styling
        helpLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        helpLabel.setForeground(new Color(127, 140, 141));
        
        // Event listeners sudah diatur di setupEventHandlers()
        // Tidak perlu duplikasi di sini
    }
    
    /**
     * Update form title berdasarkan role yang dipilih
     */
    private void updateFormTitle() {
        String titleText = "";
        
        switch (selectedRole) {
            case "petugas":
                titleText = "LOGIN PETUGAS";
                break;
            case "supervisor":
                titleText = "LOGIN SUPERVISOR";
                break;
            case "admin":
                titleText = "LOGIN ADMIN";
                break;
            default:
                titleText = "LOGIN PETUGAS";
                break;
        }
        
        formTitleLabel.setText(titleText);
        
        // Optional: Update window title juga
        setTitle("SIPRIMA Desa Tarabbi - " + titleText);
    }
    
    /**
     * Customize text field dengan modern styling
     */
    private void customizeTextField(JTextField field, String placeholder) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 221, 225), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        field.setPreferredSize(new Dimension(300, 44));
        
        // Add placeholder behavior
        field.setText(placeholder);
        field.setForeground(new Color(149, 165, 166));
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(new Color(44, 62, 80));
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(149, 165, 166));
                }
            }
        });
    }
    
    /**
     * Customize radio button dengan modern styling
     */
    private void customizeRadioButton(JRadioButton radio, String text) {
        radio.setBackground(Color.WHITE);
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        radio.setForeground(new Color(127, 140, 141));
        radio.setFocusPainted(false);
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
        button.setPreferredSize(new Dimension(300, 50));
        
        // Add modern hover effect
        Color hoverColor = brighter(bgColor, 0.2f);
        Color normalColor = bgColor;
        
        button.addMouseListener(new ButtonHoverListener(button, normalColor, hoverColor));
    }
    
    /**
     * Create gradient-like color for modern effect
     */
    private Color createGradientColor(Color base, float brightness) {
        return brighter(base, brightness);
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
    
    /**
     * Add users-alt.png icon to form title label
     */
    private void addIconToFormTitle() {
        try {
            java.net.URL iconURL = getClass().getResource("/icon/users-alt.png");
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                // Resize icon to 24x24
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                formTitleLabel.setIcon(scaledIcon);
                formTitleLabel.setText(" LOGIN PETUGAS");
                formTitleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
                formTitleLabel.setIconTextGap(10);
                
                System.out.println("✅ Icon users-alt.png berhasil dimuat untuk form title!");
            } else {
                // fallback to emoji
                formTitleLabel.setText("👥 LOGIN PETUGAS");
                System.out.println("⚠️ File users-alt.png tidak ditemukan, menggunakan emoji fallback");
            }
        } catch (Exception e) {
            formTitleLabel.setText("👥 LOGIN PETUGAS");
            System.out.println("❌ Error loading users-alt icon: " + e.getMessage());
        }
    }
    
    /**
     * Add icon to login button with new sign.png icon
     */
    private void addIconToLoginButton() {
        try {
            // Load new sign icon from icon/login_btn/ folder
            java.net.URL iconURL = getClass().getResource("/icon/login_btn/sign.png");
            if (iconURL != null) {
                ImageIcon originalIcon = new ImageIcon(iconURL);
                
                // Resize icon to fit button (18x18 pixels for better proportion)
                Image img = originalIcon.getImage();
                Image resizedImg = img.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(resizedImg);
                
                // Set icon to button
                btnLogin.setIcon(resizedIcon);
                btnLogin.setHorizontalTextPosition(SwingConstants.RIGHT);
                btnLogin.setIconTextGap(10); // Slightly more gap for better spacing
                
                System.out.println("✅ Icon sign.png berhasil dimuat untuk tombol login!");
            } else {
                System.out.println("⚠️ File sign.png tidak ditemukan di /icon/login_btn/");
                
                // Fallback: try alternative path
                java.net.URL fallbackURL = getClass().getResource("/icon/sign.png");
                if (fallbackURL != null) {
                    ImageIcon originalIcon = new ImageIcon(fallbackURL);
                    Image img = originalIcon.getImage();
                    Image resizedImg = img.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                    ImageIcon resizedIcon = new ImageIcon(resizedImg);
                    
                    btnLogin.setIcon(resizedIcon);
                    btnLogin.setHorizontalTextPosition(SwingConstants.RIGHT);
                    btnLogin.setIconTextGap(10);
                    
                    System.out.println("✅ Icon sign.png berhasil dimuat dari path alternatif!");
                } else {
                    System.out.println("⚠️ File sign.png tidak ditemukan di kedua path");
                    // Use emoji fallback
                    btnLogin.setText("✍️ MASUK SISTEM");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error loading sign icon: " + e.getMessage());
            // Fallback: use text-based icon if image fails
            btnLogin.setText("✍️ MASUK SISTEM");
        }
    }
    
    
    
    // Fingerprint authentication methods removed for simplified interface
    
    /**
     * Creates a programmatic app icon with "S" letter
     * Same design as FormRegister for consistency
     */
    private Image createAppIcon() {
        // Create 32x32 buffered image
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        
        // Enable anti-aliasing for smooth edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Fill background with rounded rectangle (blue theme)
        g2d.setColor(currentThemeColor); // Use same blue as login theme
        g2d.fillRoundRect(2, 2, 28, 28, 8, 8);
        
        // Draw border for definition
        g2d.setColor(brighter(currentThemeColor, 0.2f));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(2, 2, 28, 28, 8, 8);
        
        // Draw "S" letter in center
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        // Calculate text position for centering
        FontMetrics fm = g2d.getFontMetrics();
        String letter = "S";
        int textWidth = fm.stringWidth(letter);
        int textHeight = fm.getAscent();
        
        int x = (32 - textWidth) / 2;
        int y = (32 + textHeight) / 2 - 2; // Slight adjustment for visual centering
        
        g2d.drawString(letter, x, y);
        
        // Cleanup
        g2d.dispose();
        
        return icon;
    }
    
    /**
     * Setup window properties
     */
    private void setupWindowProperties() {
        setTitle("SIPRIMA Desa Tarabbi - Login Sistem");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 400));
        setPreferredSize(new Dimension(900, 650));
        
        // Set app icon
        setIconImage(createAppIcon());
        
        // Set modern look and feel
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Use default look and feel
        }
        
        // Add responsive behavior
        addComponentListener(new ResponsiveListener());
        
        // Set full view (maximized window)
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        pack();
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
        titleLabel = new javax.swing.JLabel();
        mainContentPanel = new javax.swing.JPanel();
        loginPanel = new javax.swing.JPanel();
        formTitleLabel = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        checkboxPanel = new javax.swing.JPanel();
        chkRememberMe = new javax.swing.JCheckBox();
        lblForgotPassword = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        roleLabel = new javax.swing.JLabel();
        rolePanel = new javax.swing.JPanel();
        rbPetugas = new javax.swing.JRadioButton();
        rbSupervisor = new javax.swing.JRadioButton();
        rbAdmin = new javax.swing.JRadioButton();
        helpLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIPRIMA Login");

        headerPanel.setBackground(new java.awt.Color(41, 128, 185));
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setPreferredSize(new java.awt.Dimension(900, 80));


        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("MASUK SISTEM");


        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainContentPanel.setBackground(new java.awt.Color(236, 240, 241));
        mainContentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(50, 50, 50, 50));

        loginPanel.setBackground(new java.awt.Color(255, 255, 255));
        loginPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40)));
        loginPanel.setPreferredSize(new java.awt.Dimension(400, 500));


        formTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        formTitleLabel.setForeground(new java.awt.Color(41, 128, 185));
        formTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        formTitleLabel.setText("🏛️ LOGIN PETUGAS");

        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtUsername.setText("👤 Username atau Email");
        txtUsername.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        txtUsername.setPreferredSize(new java.awt.Dimension(300, 44));

        txtPassword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPassword.setText("🔒 password");
        txtPassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        txtPassword.setPreferredSize(new java.awt.Dimension(300, 44));

        checkboxPanel.setBackground(new java.awt.Color(255, 255, 255));

        chkRememberMe.setBackground(new java.awt.Color(255, 255, 255));
        chkRememberMe.setForeground(new java.awt.Color(127, 140, 141));
        chkRememberMe.setText("Ingat saya");
        chkRememberMe.setFocusPainted(false);

        lblForgotPassword.setForeground(new java.awt.Color(41, 128, 185));
        lblForgotPassword.setText("Lupa password?");
        lblForgotPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout checkboxPanelLayout = new javax.swing.GroupLayout(checkboxPanel);
        checkboxPanel.setLayout(checkboxPanelLayout);
        checkboxPanelLayout.setHorizontalGroup(
            checkboxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkboxPanelLayout.createSequentialGroup()
                .addComponent(chkRememberMe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblForgotPassword))
        );
        checkboxPanelLayout.setVerticalGroup(
            checkboxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkboxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(chkRememberMe)
                .addComponent(lblForgotPassword))
        );

        btnLogin.setBackground(new java.awt.Color(41, 128, 185));
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("MASUK SISTEM");
        btnLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 25, 15, 25));
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new java.awt.Dimension(300, 50));

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

        helpLabel.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        helpLabel.setForeground(new java.awt.Color(127, 140, 141));
        helpLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        helpLabel.setText("Hubungi admin untuk akun baru");


        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(formTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
            .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(checkboxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(helpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roleLabel)
                    .addComponent(rolePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 82, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addComponent(formTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(checkboxPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(roleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rolePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(helpLabel))
        );

        javax.swing.GroupLayout mainContentPanelLayout = new javax.swing.GroupLayout(mainContentPanel);
        mainContentPanel.setLayout(mainContentPanelLayout);
        mainContentPanelLayout.setHorizontalGroup(
            mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContentPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainContentPanelLayout.setVerticalGroup(
            mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContentPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    // Navigation button handlers removed - cleaner login interface
    
    /**
     * Handle login button action
     */
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        // Get input values
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        
        // Basic validation
        if (username.equals("👤 Username atau Email") || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Username tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
        
        if (password.equals("🔒 Password") || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Password tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }
        
        // Show loading state
        btnLogin.setText("⏳ Memproses...");
        btnLogin.setEnabled(false);
        
        // Create SwingWorker for background authentication
        SwingWorker<models.User, Void> loginWorker = new SwingWorker<models.User, Void>() {
            @Override
            protected models.User doInBackground() throws Exception {
                // Authenticate with SessionManager
                Utils.SessionManager sessionManager = Utils.SessionManager.getInstance();
                return sessionManager.authenticate(username, password, selectedRole);
            }
            
            @Override
            protected void done() {
                // Reset button state
                btnLogin.setText("🚪 MASUK SISTEM");
                btnLogin.setEnabled(true);
                
                try {
                    models.User authenticatedUser = get();
                    
                    if (authenticatedUser != null) {
                        // Authentication successful
                        JOptionPane.showMessageDialog(FormLogin.this,
                            "✅ Login berhasil!\n\n" +
                            "Selamat datang, " + authenticatedUser.getDisplayName() + "!\n" +
                            "Role: " + authenticatedUser.getRoleDisplayName() + "\n\n" +
                            "Membuka dashboard...",
                            "LOGIN BERHASIL",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Navigate to appropriate dashboard based on role
                        navigateToDashboard(authenticatedUser);
                        
                    } else {
                        // Authentication failed - show option to register
                        int choice = JOptionPane.showOptionDialog(FormLogin.this,
                            "❌ Login gagal!\n\n" +
                            "Username/email atau password salah.\n" +
                            "Pastikan role yang dipilih sesuai dengan akun Anda.\n\n" +
                            "Apakah Anda belum memiliki akun?",
                            "LOGIN GAGAL",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            null,
                            new Object[]{"📝 Daftar Akun", "🔄 Coba Lagi"},
                            "🔄 Coba Lagi");
                        
                        if (choice == 0) {
                            // User wants to register
                            FormRegister registerForm = new FormRegister();
                            registerForm.setVisible(true);
                            FormLogin.this.dispose();
                        } else {
                            // Clear password field and try again
                            txtPassword.setText("");
                            txtPassword.requestFocus();
                        }
                    }
                    
                } catch (Exception e) {
                    // Handle any errors
                    JOptionPane.showMessageDialog(FormLogin.this,
                        "❌ Terjadi kesalahan saat login:\n\n" +
                        e.getMessage(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                    
                    System.err.println("Login error: " + e.getMessage());
                }
            }
        };
        
        // Execute login in background
        loginWorker.execute();
    }
    
    /**
     * Navigate to appropriate dashboard based on user role
     * Optimized untuk auto close FormLogin dan buka dashboard dalam full view
     */
    private void navigateToDashboard(models.User user) {
        try {
            System.out.println("🚀 Navigating to dashboard for user: " + user.getDisplayName() + " (" + user.getRole() + ")");
            
            // Create dashboard instance before closing login form
            JFrame dashboardFrame = null;
            
            // Open appropriate dashboard based on role
            switch (user.getRole()) {
                case PETUGAS:
                case ADMIN:
                    // Both petugas and admin use DashboardPetugas
                    dashboardFrame = new dashboard.DashboardPetugas(user);
                    break;
                    
                case SUPERVISOR:
                    // Supervisor uses DashboardSuperVisor
                    dashboardFrame = new dashboard.DashboardSuperVisor(user);
                    break;
                    
                case MASYARAKAT:
                    // Masyarakat should not login through this form
                    // But if they do, redirect to public form
                    JOptionPane.showMessageDialog(this,
                        "⚠️ Akun masyarakat tidak dapat mengakses dashboard petugas.\n\n" +
                        "Silakan gunakan formulir pengaduan publik.",
                        "AKSES DITOLAK",
                        JOptionPane.WARNING_MESSAGE);
                    return; // Exit without closing login form
                    
                default:
                    JOptionPane.showMessageDialog(this,
                        "❌ Role tidak dikenali: " + user.getRole(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                    return; // Exit without closing login form
            }
            
            // Configure dashboard untuk full view
            if (dashboardFrame != null) {
                final JFrame finalDashboard = dashboardFrame;
                
                SwingUtilities.invokeLater(() -> {
                    // Set dashboard properties untuk full view experience
                    finalDashboard.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
                    finalDashboard.setLocationRelativeTo(null); // Center on screen
                    finalDashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    
                    // Ensure dashboard is brought to front
                    finalDashboard.setAlwaysOnTop(true);
                    finalDashboard.setVisible(true);
                    finalDashboard.toFront();
                    finalDashboard.requestFocus();
                    finalDashboard.setAlwaysOnTop(false); // Reset after bringing to front
                    
                    System.out.println("✅ Dashboard opened successfully in full view!");
                });
                
                // Close login form dengan smooth transition - FIX untuk decorated frame
                SwingUtilities.invokeLater(() -> {
                    try {
                        // Check if frame is undecorated before using opacity
                        if (!isDisplayable() || isUndecorated()) {
                            // Safe to use opacity
                            Timer fadeOut = new Timer(20, null);
                            fadeOut.addActionListener(e -> {
                                try {
                                    float opacity = getOpacity() - 0.1f;
                                    if (opacity <= 0.0f) {
                                        setOpacity(0.0f);
                                        fadeOut.stop();
                                        
                                        // Finally dispose the login form
                                        dispose();
                                        System.out.println("🔒 FormLogin closed successfully with fade effect!");
                                    } else {
                                        setOpacity(opacity);
                                    }
                                } catch (Exception opacityError) {
                                    // Opacity failed, just close normally
                                    fadeOut.stop();
                                    dispose();
                                    System.out.println("🔒 FormLogin closed successfully (no fade)!");
                                }
                            });
                            fadeOut.start();
                        } else {
                            // Frame is decorated, use simple close without opacity
                            Timer delayedClose = new Timer(500, e -> {
                                dispose();
                                System.out.println("🔒 FormLogin closed successfully (decorated frame)!");
                            });
                            delayedClose.setRepeats(false);
                            delayedClose.start();
                        }
                    } catch (Exception e) {
                        // Any error, just close immediately
                        dispose();
                        System.out.println("🔒 FormLogin closed successfully (fallback)!");
                    }
                });
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error navigating to dashboard: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this,
                "❌ Gagal membuka dashboard:\n\n" +
                e.getMessage() + "\n\n" +
                "Login form akan tetap terbuka untuk mencoba lagi.",
                "ERROR - DASHBOARD",
                JOptionPane.ERROR_MESSAGE);
        }
    }

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
            java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new FormLogin().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JCheckBox chkRememberMe;
    private javax.swing.JPanel checkboxPanel;
    private javax.swing.JLabel formTitleLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel helpLabel;
    private javax.swing.JLabel lblForgotPassword;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JPanel mainContentPanel;
    private javax.swing.JRadioButton rbAdmin;
    private javax.swing.JRadioButton rbPetugas;
    private javax.swing.JRadioButton rbSupervisor;
    private javax.swing.ButtonGroup roleButtonGroup;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JPanel rolePanel;
    private javax.swing.JLabel titleLabel;
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
    
    /**
     * Link mouse listener untuk forgot password
     */
    private class LinkMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JOptionPane.showMessageDialog(FormLogin.this,
                "🔐 Fitur Reset Password\n\n" +
                "Hubungi administrator untuk reset password:\n" +
                "📞 Telepon: (0411) 123-4567\n" +
                "📧 Email: admin@desatarabbi.go.id\n" +
                "🌐 Website: www.desatarabbi.go.id",
                "LUPA PASSWORD",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            lblForgotPassword.setText("<html><u>Lupa password? →</u></html>");
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            lblForgotPassword.setText("Lupa password? →");
        }
    }
    
    /**
     * Responsive listener untuk mengubah layout berdasarkan ukuran window
     */
    private class ResponsiveListener extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            adjustLayoutForScreenSize();
        }
    }
    
    /**
     * Adjust layout berdasarkan ukuran layar
     */
    private void adjustLayoutForScreenSize() {
        int width = getWidth();
        
        if (width < MOBILE_BREAKPOINT) {
            // Mobile layout
            setMobileLayout();
        } else if (width < COMPACT_BREAKPOINT) {
            // Compact tablet layout
            setCompactLayout();
        } else {
            // Desktop layout
            setDesktopLayout();
        }
    }
    
    /**
     * Set mobile layout untuk layar kecil
     */
    private void setMobileLayout() {
        // Smaller fonts dan components untuk mobile
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        txtUsername.setPreferredSize(new Dimension(250, 40));
        txtPassword.setPreferredSize(new Dimension(250, 40));
        btnLogin.setPreferredSize(new Dimension(250, 44));
        
        loginPanel.setPreferredSize(new Dimension(300, 450));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        isCompactMode = true;
    }
    
    /**
     * Set compact layout untuk tablet
     */
    private void setCompactLayout() {
        // Medium fonts dan components untuk tablet
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        txtUsername.setPreferredSize(new Dimension(280, 42));
        txtPassword.setPreferredSize(new Dimension(280, 42));
        btnLogin.setPreferredSize(new Dimension(280, 46));
        
        loginPanel.setPreferredSize(new Dimension(350, 480));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        isCompactMode = true;
    }
    
    /**
     * Set desktop layout untuk layar besar
     */
    private void setDesktopLayout() {
        // Full fonts dan components untuk desktop
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        txtUsername.setPreferredSize(new Dimension(300, 44));
        txtPassword.setPreferredSize(new Dimension(300, 44));
        btnLogin.setPreferredSize(new Dimension(300, 50));
        
        loginPanel.setPreferredSize(new Dimension(400, 500));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        isCompactMode = false;
    }
}
