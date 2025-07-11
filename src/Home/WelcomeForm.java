/*
 * SIPRIMA Desa Tarabbi - Modern Welcome Screen  
 * Sistem Pengaduan Masyarakat Desa Tarabbi
 * 
 * Design berdasarkan: 
 * - ../../SIPRIMA_UI_DESIGN.md
 * - ../../SIPRIMA_UI_FLOW.md
 */
package Home;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;

/**
 * Modern Welcome Form - Landing page untuk aplikasi SIPRIMA
 * Responsive design dengan modern UI elements
 * 
 * @author febry
 */
public class WelcomeForm extends javax.swing.JFrame {
    
    // Modern UI properties - Fixed Blue Theme
    private final Color PERMANENT_BLUE_THEME = new Color(41, 128, 185); // Professional blue
    
    // Responsive properties
    private boolean isCompactMode = false;
    private final int COMPACT_BREAKPOINT = 800; // Width threshold for compact mode
    private final int MOBILE_BREAKPOINT = 600;  // Width threshold for mobile mode
    
    /**
     * Creates new modern WelcomeForm
     */
    public WelcomeForm() {
        initComponents();
        customizeModernComponents();
        setupWindowProperties();
    }
    
    /**
     * Customize components untuk modern look with permanent blue theme
     */
    private void customizeModernComponents() {
        // Modern header styling - permanent blue
        headerPanel.setBackground(createGradientColor(PERMANENT_BLUE_THEME, 0.2f));
        
        // Modern title styling
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        
        subtitleLabel.setForeground(new Color(255, 255, 255, 220));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setText("Sistem Pengaduan Masyarakat");
        
        // Modern main content
        mainContentPanel.setBackground(new Color(236, 240, 241));
        
        // Modern logo panel with subtle styling
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 221, 225), 2, true),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Enhanced logo - permanent blue
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        logoLabel.setForeground(PERMANENT_BLUE_THEME);
        
        // Modern motto
        mottoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        mottoLabel.setForeground(new Color(127, 140, 141));
        mottoLabel.setText("<html><center><i>\"Melayani Dengan Hati,<br>Mendengar Setiap Aspirasi\"</i></center></html>");
        
        // Modern button styling - permanent blue for main button
        customizeModernButton(btnMasukSistem, PERMANENT_BLUE_THEME, "MASUK SISTEM", "Masuk sebagai Petugas, Supervisor, atau Admin");
        customizeModernButton(btnDaftarAduan, new Color(39, 174, 96), "DAFTAR ADUAN", "Laporkan keluhan atau masalah Anda");
        
        // Add ActionListeners (using lambda to avoid anonymous classes)
        btnMasukSistem.addActionListener(evt -> btnMasukSistemActionPerformed(evt));
        btnDaftarAduan.addActionListener(evt -> btnDaftarAduanActionPerformed(evt));
        
        // Modern footer
        footerPanel.setBackground(Color.WHITE);
        contactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contactLabel.setForeground(new Color(127, 140, 141));
        contactLabel.setText("<html><center>📞 <b>Kontak:</b> (belum_ada) &nbsp;|&nbsp; 📧 <b>Email:</b> belum_ada &nbsp;|&nbsp; 🌐 <b>Web:</b> belum_ada</center></html>");
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
        
        // Add modern hover effect
        Color hoverColor = brighter(bgColor, 0.2f);
        Color normalColor = bgColor;
        
        // Simple hover effect without anonymous classes
        button.addMouseListener(new ButtonHoverListener(button, normalColor, hoverColor));
    }
    
    /**
     * Setup window properties
     */
    private void setupWindowProperties() {
        setTitle("SIPRIMA Desa Tarabbi - Sistem Pengaduan Masyarakat Modern");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 500));
        setPreferredSize(new Dimension(1100, 800));
        
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
        
        pack();
        
        // ===============================
        // FULL VIEW CONFIGURATION untuk DEBUG
        // ===============================
        
        // Untuk debugging dan development - tampilkan dalam full view
        SwingUtilities.invokeLater(() -> {
            // Set extended state untuk maximize window
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            
            // Alternative: Set manual full screen size
            // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            // setSize(screenSize.width, screenSize.height);
            // setLocation(0, 0);
            
            // Bring to front dan focus
            setAlwaysOnTop(true);
            toFront();
            requestFocus();
            setAlwaysOnTop(false); // Reset after bringing to front
            
            System.out.println("✅ WelcomeForm opened in FULL VIEW mode!");
            System.out.println("📐 Window size: " + getWidth() + "x" + getHeight());
        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        subtitleLabel = new javax.swing.JLabel();
        mainContentPanel = new javax.swing.JPanel();
        logoPanel = new javax.swing.JPanel();
        logoLabel = new javax.swing.JLabel();
        mottoLabel = new javax.swing.JLabel();
        btnMasukSistem = new javax.swing.JButton();
        btnDaftarAduan = new javax.swing.JButton();
        footerPanel = new javax.swing.JPanel();
        contactLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIPRIMA Desa Tarabbi - Modern Responsive");
        setBackground(new java.awt.Color(245, 245, 245));
        setMinimumSize(new java.awt.Dimension(600, 500));
        setPreferredSize(new java.awt.Dimension(1200, 850));

        headerPanel.setBackground(new java.awt.Color(41, 128, 185));
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setPreferredSize(new java.awt.Dimension(900, 80));

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("SIPRIMA DESA TARABBI");

        subtitleLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        subtitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        subtitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        subtitleLabel.setText("Sistem Pengaduan Masyarakat Desa Tarabbi");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subtitleLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subtitleLabel)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        mainContentPanel.setBackground(new java.awt.Color(236, 240, 241));
        mainContentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 50, 40, 50));

        logoPanel.setBackground(new java.awt.Color(255, 255, 255));
        logoPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225), 2), javax.swing.BorderFactory.createEmptyBorder(30, 30, 30, 30)));
        logoPanel.setPreferredSize(new java.awt.Dimension(400, 200));

        logoLabel.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        logoLabel.setForeground(new java.awt.Color(41, 128, 185));
        logoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoLabel.setText("🏛️");
        logoLabel.setToolTipText("Logo Desa Tarabbi");

        mottoLabel.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        mottoLabel.setForeground(new java.awt.Color(127, 140, 141));
        mottoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mottoLabel.setText("\"Melayani Dengan Hati, Mendengar Setiap Aspirasi\"");

        javax.swing.GroupLayout logoPanelLayout = new javax.swing.GroupLayout(logoPanel);
        logoPanel.setLayout(logoPanelLayout);
        logoPanelLayout.setHorizontalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mottoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
        );
        logoPanelLayout.setVerticalGroup(
            logoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoPanelLayout.createSequentialGroup()
                .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(mottoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnMasukSistem.setBackground(new java.awt.Color(41, 128, 185));
        btnMasukSistem.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnMasukSistem.setForeground(new java.awt.Color(255, 255, 255));
        btnMasukSistem.setText("🔐 MASUK SISTEM");
        btnMasukSistem.setToolTipText("Masuk sebagai petugas/admin");
        btnMasukSistem.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btnMasukSistem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMasukSistem.setFocusPainted(false);
        btnMasukSistem.setPreferredSize(new java.awt.Dimension(200, 50));
        btnMasukSistem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMasukSistemActionPerformed(evt);
            }
        });

        btnDaftarAduan.setBackground(new java.awt.Color(39, 174, 96));
        btnDaftarAduan.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnDaftarAduan.setForeground(new java.awt.Color(255, 255, 255));
        btnDaftarAduan.setText("📋 DAFTAR ADUAN");
        btnDaftarAduan.setToolTipText("Laporkan masalah Anda");
        btnDaftarAduan.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btnDaftarAduan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDaftarAduan.setFocusPainted(false);
        btnDaftarAduan.setPreferredSize(new java.awt.Dimension(200, 50));

        javax.swing.GroupLayout mainContentPanelLayout = new javax.swing.GroupLayout(mainContentPanel);
        mainContentPanel.setLayout(mainContentPanelLayout);
        mainContentPanelLayout.setHorizontalGroup(
            mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContentPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainContentPanelLayout.createSequentialGroup()
                        .addComponent(btnMasukSistem, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(btnDaftarAduan, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(logoPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainContentPanelLayout.setVerticalGroup(
            mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContentPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(logoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addGroup(mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMasukSistem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDaftarAduan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(98, 98, 98))
        );

        footerPanel.setBackground(new java.awt.Color(255, 255, 255));
        footerPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(15, 30, 15, 30)));
        footerPanel.setPreferredSize(new java.awt.Dimension(900, 50));

        contactLabel.setForeground(new java.awt.Color(127, 140, 141));
        contactLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        contactLabel.setText("📞 Kontak: (belum_ada)  | 📧 (belum_ada) | 🌐 (belum_ada)");

        javax.swing.GroupLayout footerPanelLayout = new javax.swing.GroupLayout(footerPanel);
        footerPanel.setLayout(footerPanelLayout);
        footerPanelLayout.setHorizontalGroup(
            footerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contactLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 1140, Short.MAX_VALUE)
        );
        footerPanelLayout.setVerticalGroup(
            footerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contactLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
            .addComponent(footerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
            .addComponent(mainContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainContentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(footerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDaftarAduanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDaftarAduanActionPerformed
        // Open Form Register for public user registration
        try {
            // Create and show registration form
            Auth.FormRegister registerForm = new Auth.FormRegister();
            registerForm.setVisible(true);
            
            // Close current welcome form
            this.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error membuka form registrasi: " + e.getMessage() + "\n\n" +
                "Pastikan:\n" +
                "• Database sudah terkoneksi\n" +
                "• Semua komponen sudah terinstall",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDaftarAduanActionPerformed

    private void btnMasukSistemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasukSistemActionPerformed
        // Navigate to LoginForm
        try {
            // Create and show login form
            Auth.FormLogin loginForm = new Auth.FormLogin();
            
            // Ensure login form opens maximized
            SwingUtilities.invokeLater(() -> {
                loginForm.setExtendedState(JFrame.MAXIMIZED_BOTH);
                loginForm.setVisible(true);
                loginForm.toFront();
                loginForm.requestFocus();
                System.out.println("✅ FormLogin opened in FULL VIEW mode from WelcomeForm!");
            });
            
            // Close current welcome form
            this.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnMasukSistemActionPerformed

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
            // Set system look and feel untuk NetBeans compatibility
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WelcomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WelcomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WelcomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WelcomeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new WelcomeForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDaftarAduan;
    private javax.swing.JButton btnMasukSistem;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JPanel logoPanel;
    private javax.swing.JPanel mainContentPanel;
    private javax.swing.JLabel mottoLabel;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JLabel titleLabel;
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
        // Smaller fonts untuk mobile - tetap center aligned
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mottoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        mottoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Smaller button fonts
        btnMasukSistem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDaftarAduan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Compact padding
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 221, 225), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Smaller contact font - tetap center
        contactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        contactLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        isCompactMode = true;
    }
    
    /**
     * Set compact layout untuk tablet
     */
    private void setCompactLayout() {
        // Medium fonts untuk tablet - tetap center aligned
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 44));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mottoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        mottoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Medium button fonts
        btnMasukSistem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDaftarAduan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Medium padding
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 221, 225), 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Medium contact font - tetap center
        contactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        contactLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        isCompactMode = true;
    }
    
    /**
     * Set desktop layout untuk layar besar
     */
    private void setDesktopLayout() {
        // Full fonts untuk desktop - tetap center aligned
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mottoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        mottoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Full button fonts
        btnMasukSistem.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDaftarAduan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Full padding
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        logoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 221, 225), 2, true),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Full contact font - tetap center
        contactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contactLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        isCompactMode = false;
    }
}
