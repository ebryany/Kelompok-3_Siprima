/*
 * SIPRIMA Desa Tarabbi - Masyarakat Data Frame
 * Sistem Pengaduan Masyarakat Desa Tarabbi
 */
package Pengguna;

import Utils.DatabaseConfig;
import models.User;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author febry
 */
public class MasyarakatDataFrame extends javax.swing.JFrame {
    
    // SIPRIMA Official Colors
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color PRIMARY_GREEN = new Color(39, 174, 96);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private static final Color BG_PRIMARY = new Color(236, 240, 241);
    
    private User currentUser;
    private DefaultTableModel tableModel;
    private JTable dataTable;
    
    /**
     * Creates new form MasyarakatDataFrame
     */
    public MasyarakatDataFrame(User user) {
        this.currentUser = user;
        initComponents();
        loadMasyarakatData();
    }
    
    private void initComponents() {
        setTitle("SIPRIMA - Data Masyarakat");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("👥 DATA MASYARAKAT");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(BG_PRIMARY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JButton btnRefresh = createButton("🔄 Refresh", TEXT_SECONDARY);
        JButton btnTutup = createButton("❌ Tutup", ERROR_COLOR);
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnTutup);
        
        // Table
        String[] columns = {"Nama", "Email", "Role", "Status", "Tanggal Daftar"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        dataTable = new JTable(tableModel);
        dataTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dataTable.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Pengguna"));
        
        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Event Handlers
        btnRefresh.addActionListener(e -> loadMasyarakatData());
        btnTutup.addActionListener(e -> dispose());
        
        // Window properties
        setSize(800, 500);
        setLocationRelativeTo(null);
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
    
    private void loadMasyarakatData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> tableModel.setRowCount(0));
                
                try (Connection conn = DatabaseConfig.getConnection()) {
                    String query = "SELECT full_name, email, role, is_active, created_at " +
                                  "FROM users ORDER BY created_at DESC";
                    
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(query)) {
                        
                        while (rs.next()) {
                            Object[] row = {
                                rs.getString("full_name"),
                                rs.getString("email"),
                                getRoleDisplay(rs.getString("role")),
                                rs.getBoolean("is_active") ? "✅ Aktif" : "❌ Tidak Aktif",
                                rs.getTimestamp("created_at")
                            };
                            
                            SwingUtilities.invokeLater(() -> tableModel.addRow(row));
                        }
                    }
                } catch (SQLException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(MasyarakatDataFrame.this,
                            "❌ Error loading data: " + e.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                    });
                }
                
                return null;
            }
        };
        
        worker.execute();
    }
    
    private String getRoleDisplay(String role) {
        switch (role.toLowerCase()) {
            case "admin": return "🛡️ Admin";
            case "petugas": return "👨‍💼 Petugas";
            case "supervisor": return "👨‍💼 Supervisor";
            case "masyarakat": return "👥 Masyarakat";
            default: return "❓ " + role;
        }
    }
}

