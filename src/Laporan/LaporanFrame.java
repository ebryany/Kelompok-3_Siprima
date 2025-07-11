/*
 * SIPRIMA Desa Tarabbi - Laporan Frame
 * Sistem Pengaduan Masyarakat Desa Tarabbi
 */
package Laporan;

import Utils.DatabaseConfig;
import models.User;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * @author febry
 */
public class LaporanFrame extends javax.swing.JFrame {
    
    // SIPRIMA Official Colors
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color PRIMARY_GREEN = new Color(39, 174, 96);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private static final Color BG_PRIMARY = new Color(236, 240, 241);
    
    private User currentUser;
    
    /**
     * Creates new form LaporanFrame
     */
    public LaporanFrame(User user) {
        this.currentUser = user;
        initComponents();
        loadLaporanData();
    }
    
    /**
     * Default constructor for testing
     */
    public LaporanFrame() {
        this.currentUser = null;
        initComponents();
        loadLaporanData();
    }
    
    private void initComponents() {
        setTitle("SIPRIMA - Laporan & Analytics");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("📉 LAPORAN & ANALYTICS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(BG_PRIMARY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Statistik Cards
        contentPanel.add(createStatsCard("📋 Total Aduan", "Loading...", PRIMARY_BLUE));
        contentPanel.add(createStatsCard("⚙️ Sedang Diproses", "Loading...", WARNING_COLOR));
        contentPanel.add(createStatsCard("✅ Selesai", "Loading...", PRIMARY_GREEN));
        contentPanel.add(createStatsCard("👥 Total Pengguna", "Loading...", TEXT_SECONDARY));
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BG_PRIMARY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JButton btnGenerateReport = createButton("📄 Generate Report", PRIMARY_BLUE);
        JButton btnExportExcel = createButton("📈 Export Excel", PRIMARY_GREEN);
        JButton btnExportPDF = createButton("📄 Export PDF", ERROR_COLOR);
        JButton btnTutup = createButton("❌ Tutup", TEXT_SECONDARY);
        
        buttonPanel.add(btnGenerateReport);
        buttonPanel.add(btnExportExcel);
        buttonPanel.add(btnExportPDF);
        buttonPanel.add(btnTutup);
        
        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Event Handlers
        btnGenerateReport.addActionListener(e -> generateReport());
        btnExportExcel.addActionListener(e -> exportToExcel());
        btnExportPDF.addActionListener(e -> exportToPDF());
        btnTutup.addActionListener(e -> dispose());
        
        // Window properties
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    private JPanel createStatsCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(color);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
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
    
    private void loadLaporanData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (Connection conn = DatabaseConfig.getConnection()) {
                    // Load statistics
                    updateStatistic(0, getTotalAduan(conn));
                    updateStatistic(1, getAduanByStatus(conn, "proses"));
                    updateStatistic(2, getAduanByStatus(conn, "selesai"));
                    updateStatistic(3, getTotalUsers(conn));
                } catch (SQLException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(LaporanFrame.this,
                            "❌ Error loading statistics: " + e.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                    });
                }
                return null;
            }
        };
        
        worker.execute();
    }
    
    private void updateStatistic(int cardIndex, String value) {
        SwingUtilities.invokeLater(() -> {
            JPanel contentPanel = (JPanel) ((BorderLayout) getContentPane().getLayout())
                .getLayoutComponent(BorderLayout.CENTER);
            JPanel card = (JPanel) contentPanel.getComponent(cardIndex);
            JLabel valueLabel = (JLabel) card.getComponent(1);
            valueLabel.setText(value);
        });
    }
    
    private String getTotalAduan(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) FROM complaints";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }
        }
        return "0";
    }
    
    private String getAduanByStatus(Connection conn, String status) throws SQLException {
        String query = "SELECT COUNT(*) FROM complaints WHERE status = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return String.valueOf(rs.getInt(1));
                }
            }
        }
        return "0";
    }
    
    private String getTotalUsers(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }
        }
        return "0";
    }
    
    private void generateReport() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                GeneratorLaporan generator = new GeneratorLaporan();
                generator.generateComprehensiveReport();
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions
                    // Success message already shown by GeneratorLaporan
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LaporanFrame.this,
                        "❌ Error generating report: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    private void exportToExcel() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                GeneratorLaporan generator = new GeneratorLaporan();
                generator.exportToExcel();
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions
                    // Success message already shown by GeneratorLaporan
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LaporanFrame.this,
                        "❌ Error exporting to Excel: " + e.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    private void exportToPDF() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                GeneratorLaporan generator = new GeneratorLaporan();
                generator.printReport();
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions
                    JOptionPane.showMessageDialog(LaporanFrame.this,
                        "✅ Print report berhasil dieksekusi!\nDialog print telah dibuka.",
                        "Print Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LaporanFrame.this,
                        "❌ Error printing report: " + e.getCause().getMessage(),
                        "Print Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        try {
            // Set look and feel
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set look and feel: " + e.getMessage());
        }
        
        // Run on EDT
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LaporanFrame().setVisible(true);
        });
    }
}

