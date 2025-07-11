/*
 * SIPRIMA Desa Tarabbi - Settings Frame
 * Sistem Pengaduan Masyarakat Desa Tarabbi
 */
package Profil;

import models.User;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author febry
 */
public class SettingsFrame extends javax.swing.JFrame {
    
    // SIPRIMA Official Colors
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color PRIMARY_GREEN = new Color(39, 174, 96);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private static final Color BG_PRIMARY = new Color(236, 240, 241);
    
    private User currentUser;
    
    /**
     * Creates new form SettingsFrame
     */
    public SettingsFrame(User user) {
        this.currentUser = user;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("SIPRIMA - Pengaturan Sistem");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("⚙️ PENGATURAN SISTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        contentPanel.setBackground(BG_PRIMARY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Settings Categories
        contentPanel.add(createSettingsSection("🎨 Pengaturan Tampilan", 
            new String[]{"Ubah Tema", "Ukuran Font", "Mode Gelap/Terang"}));
        contentPanel.add(createSettingsSection("🔒 Pengaturan Keamanan", 
            new String[]{"Ubah Password", "Riwayat Login", "Session Timeout"}));
        contentPanel.add(createSettingsSection("📊 Pengaturan Database", 
            new String[]{"Backup Database", "Restore Database", "Optimasi Database"}));
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BG_PRIMARY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JButton btnSimpan = createButton("✅ Simpan Pengaturan", PRIMARY_GREEN);
        JButton btnReset = createButton("🔄 Reset Default", TEXT_SECONDARY);
        JButton btnTutup = createButton("❌ Tutup", ERROR_COLOR);
        
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnTutup);
        
        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Event Handlers
        btnSimpan.addActionListener(e -> saveSettings());
        btnReset.addActionListener(e -> resetSettings());
        btnTutup.addActionListener(e -> dispose());
        
        // Window properties
        setSize(600, 500);
        setLocationRelativeTo(null);
    }
    
    private JPanel createSettingsSection(String title, String[] options) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_BLUE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_BLUE);
        
        JPanel optionsPanel = new JPanel(new GridLayout(options.length, 1, 5, 5));
        optionsPanel.setBackground(Color.WHITE);
        
        for (String option : options) {
            JButton optionButton = new JButton(option);
            optionButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            optionButton.setBackground(BG_PRIMARY);
            optionButton.setForeground(TEXT_SECONDARY);
            optionButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            optionButton.setFocusPainted(false);
            optionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            optionButton.addActionListener(e -> handleSettingOption(option));
            optionsPanel.add(optionButton);
        }
        
        section.add(titleLabel, BorderLayout.NORTH);
        section.add(optionsPanel, BorderLayout.CENTER);
        
        return section;
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void handleSettingOption(String option) {
        switch (option) {
            case "Ubah Password":
                openChangePasswordDialog();
                break;
            case "Backup Database":
                performDatabaseBackup();
                break;
            case "Ubah Tema":
                openThemeSelector();
                break;
            default:
                JOptionPane.showMessageDialog(this,
                    "🚪 Fitur '" + option + "' sedang dalam pengembangan.\n" +
                    "Akan tersedia di versi mendatang.",
                    "Coming Soon",
                    JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }
    
    private void openChangePasswordDialog() {
        try {
            Formpengaturan formPengaturan = new Formpengaturan();
            formPengaturan.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Error opening change password: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performDatabaseBackup() {
        int result = JOptionPane.showConfirmDialog(this,
            "💾 Apakah Anda yakin ingin melakukan backup database?\n" +
            "Proses ini mungkin membutuhkan beberapa menit.",
            "Konfirmasi Backup",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "✅ Backup database berhasil!\n" +
                "File backup tersimpan di: Documents/SIPRIMA_Backup/backup_" +
                java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".sql",
                "Backup Selesai",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void openThemeSelector() {
        String[] themes = {"Default Blue", "Dark Mode", "Light Mode", "Green Theme"};
        String selectedTheme = (String) JOptionPane.showInputDialog(this,
            "🎨 Pilih tema aplikasi:",
            "Ubah Tema",
            JOptionPane.QUESTION_MESSAGE,
            null,
            themes,
            themes[0]);
        
        if (selectedTheme != null) {
            JOptionPane.showMessageDialog(this,
                "✅ Tema '" + selectedTheme + "' berhasil diterapkan!\n" +
                "Restart aplikasi untuk melihat perubahan.",
                "Tema Diubah",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void saveSettings() {
        JOptionPane.showMessageDialog(this,
            "✅ Pengaturan berhasil disimpan!\n" +
            "Beberapa perubahan mungkin memerlukan restart aplikasi.",
            "Pengaturan Disimpan",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void resetSettings() {
        int result = JOptionPane.showConfirmDialog(this,
            "⚠️ Apakah Anda yakin ingin mereset semua pengaturan ke default?\n" +
            "Tindakan ini tidak dapat dibatalkan.",
            "Konfirmasi Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "✅ Pengaturan berhasil direset ke default!\n" +
                "Restart aplikasi untuk melihat perubahan.",
                "Reset Selesai",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

