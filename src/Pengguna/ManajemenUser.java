/*
 * Sistem Informasi Kependudukan Desa Tarabbi
 * Form Manajemen User - Kelola data pengguna sistem
 * @author febry
 */
package Pengguna;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import javax.swing.border.TitledBorder;

/**
 * Form untuk mengelola data pengguna sistem
 * Fitur: Tambah, Edit, Hapus, Lihat data user
 * @author febry
 */
public class ManajemenUser extends javax.swing.JFrame {
    
    // Database connection
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    
    // GUI Components
    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel buttonPanel;
    private JPanel tablePanel;
    
    // Form fields
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtNamaLengkap;
    private JTextField txtEmail;
    private JTextField txtTelepon;
    private JComboBox<String> cbRole;
    private JComboBox<String> cbStatus;
    
    // Table
    private JTable tableUser;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    
    // Buttons
    private JButton btnTambah;
    private JButton btnEdit;
    private JButton btnHapus;
    private JButton btnBatal;
    private JButton btnSimpan;
    private JButton btnRefresh;
    private JButton btnKembali;
    
    // Labels
    private JLabel lblTitle;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JLabel lblNamaLengkap;
    private JLabel lblEmail;
    private JLabel lblTelepon;
    private JLabel lblRole;
    private JLabel lblStatus;
    
    // Mode operasi
    private String mode = "VIEW"; // VIEW, ADD, EDIT
    private int selectedUserId = -1;
    
    /**
     * Creates new form ManajemenUser
     */
    public ManajemenUser() {
        initComponents();
        initDatabase();
        loadTableData();
        setFormMode("VIEW");
        
        // Set window properties
        setTitle("Manajemen User - Siprima Desa Tarabbi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Set minimum size
        setMinimumSize(new Dimension(900, 600));
        
        // Set icon
        try {
            setIconImage(new ImageIcon(getClass().getResource("/icon/user-management.png")).getImage());
        } catch (Exception e) {
            // Icon tidak ditemukan, gunakan default
        }
    }
    
    /**
     * Initialize database connection
     */
    private void initDatabase() {
        try {
            // Koneksi ke database SQLite
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:siprima.db");
            stmt = conn.createStatement();
            
            // Buat tabel users jika belum ada
            createUserTable();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error koneksi database: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Create user table if not exists
     */
    private void createUserTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username TEXT UNIQUE NOT NULL," +
                        "password TEXT NOT NULL," +
                        "nama_lengkap TEXT NOT NULL," +
                        "email TEXT," +
                        "telepon TEXT," +
                        "role TEXT NOT NULL DEFAULT 'USER'," +
                        "status TEXT NOT NULL DEFAULT 'AKTIF'," +
                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                        ")";
            stmt.execute(sql);
            
            // Insert default admin user if not exists
            insertDefaultUser();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error creating table: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Insert default admin user
     */
    private void insertDefaultUser() {
        try {
            String checkSql = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
            rs = stmt.executeQuery(checkSql);
            
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO users (username, password, nama_lengkap, email, role, status) " +
                                  "VALUES ('admin', 'admin123', 'Administrator', 'admin@desatarabbi.id', 'ADMIN', 'AKTIF')";
                stmt.execute(insertSql);
            }
        } catch (SQLException e) {
            System.err.println("Error inserting default user: " + e.getMessage());
        }
    }
    
    /**
     * Initialize GUI components
     */
    private void initComponents() {
        // Main panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // Title
        lblTitle = new JLabel("MANAJEMEN USER");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Form panel
        createFormPanel();
        
        // Button panel
        createButtonPanel();
        
        // Table panel
        createTablePanel();
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    /**
     * Create form panel
     */
    private void createFormPanel() {
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            "Form Data User",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(41, 128, 185)
        ));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(lblUsername, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtUsername, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(lblPassword, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtPassword, gbc);
        
        // Nama Lengkap
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        lblNamaLengkap = new JLabel("Nama Lengkap:");
        lblNamaLengkap.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(lblNamaLengkap, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtNamaLengkap = new JTextField(20);
        txtNamaLengkap.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNamaLengkap.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtNamaLengkap, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(lblEmail, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtEmail, gbc);
        
        // Telepon
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        lblTelepon = new JLabel("Telepon:");
        lblTelepon.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(lblTelepon, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTelepon = new JTextField(20);
        txtTelepon.setFont(new Font("Arial", Font.PLAIN, 12));
        txtTelepon.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtTelepon, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        lblRole = new JLabel("Role:");
        lblRole.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(lblRole, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbRole = new JComboBox<>(new String[]{"USER", "ADMIN", "PETUGAS"});
        cbRole.setFont(new Font("Arial", Font.PLAIN, 12));
        cbRole.setBackground(Color.WHITE);
        formPanel.add(cbRole, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(lblStatus, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbStatus = new JComboBox<>(new String[]{"AKTIF", "NONAKTIF"});
        cbStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        cbStatus.setBackground(Color.WHITE);
        formPanel.add(cbStatus, gbc);
    }
    
    /**
     * Create button panel
     */
    private void createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        // Tambah button
        btnTambah = new JButton("Tambah");
        btnTambah.setFont(new Font("Arial", Font.PLAIN, 12));
        btnTambah.setBackground(new Color(46, 204, 113));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setFocusPainted(false);
        btnTambah.setPreferredSize(new Dimension(90, 35));
        btnTambah.addActionListener(e -> setFormMode("ADD"));
        
        // Edit button
        btnEdit = new JButton("Edit");
        btnEdit.setFont(new Font("Arial", Font.PLAIN, 12));
        btnEdit.setBackground(new Color(52, 152, 219));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setPreferredSize(new Dimension(90, 35));
        btnEdit.addActionListener(e -> editUser());
        
        // Hapus button
        btnHapus = new JButton("Hapus");
        btnHapus.setFont(new Font("Arial", Font.PLAIN, 12));
        btnHapus.setBackground(new Color(231, 76, 60));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setFocusPainted(false);
        btnHapus.setPreferredSize(new Dimension(90, 35));
        btnHapus.addActionListener(e -> deleteUser());
        
        // Simpan button
        btnSimpan = new JButton("Simpan");
        btnSimpan.setFont(new Font("Arial", Font.PLAIN, 12));
        btnSimpan.setBackground(new Color(46, 204, 113));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setPreferredSize(new Dimension(90, 35));
        btnSimpan.addActionListener(e -> saveUser());
        
        // Batal button
        btnBatal = new JButton("Batal");
        btnBatal.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBatal.setBackground(new Color(149, 165, 166));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFocusPainted(false);
        btnBatal.setPreferredSize(new Dimension(90, 35));
        btnBatal.addActionListener(e -> setFormMode("VIEW"));
        
        // Refresh button
        btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRefresh.setBackground(new Color(155, 89, 182));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setPreferredSize(new Dimension(90, 35));
        btnRefresh.addActionListener(e -> {
            loadTableData();
            JOptionPane.showMessageDialog(this, "Data berhasil direfresh!", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Kembali button
        btnKembali = new JButton("Kembali");
        btnKembali.setFont(new Font("Arial", Font.PLAIN, 12));
        btnKembali.setBackground(new Color(52, 73, 94));
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setPreferredSize(new Dimension(90, 35));
        btnKembali.addActionListener(e -> {
            dispose();
        });
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnKembali);
    }
    
    /**
     * Create table panel
     */
    private void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            "Daftar User",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(41, 128, 185)
        ));
        tablePanel.setBackground(Color.WHITE);
        
        // Table model
        String[] columnNames = {"ID", "Username", "Nama Lengkap", "Email", "Telepon", "Role", "Status", "Dibuat"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Table
        tableUser = new JTable(tableModel);
        tableUser.setFont(new Font("Arial", Font.PLAIN, 11));
        tableUser.setRowHeight(25);
        tableUser.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tableUser.getTableHeader().setBackground(new Color(41, 128, 185));
        tableUser.getTableHeader().setForeground(Color.WHITE);
        tableUser.setSelectionBackground(new Color(184, 207, 229));
        tableUser.setSelectionForeground(Color.BLACK);
        tableUser.setGridColor(new Color(230, 230, 230));
        
        // Column widths
        tableUser.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableUser.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableUser.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableUser.getColumnModel().getColumn(3).setPreferredWidth(150);
        tableUser.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableUser.getColumnModel().getColumn(5).setPreferredWidth(80);
        tableUser.getColumnModel().getColumn(6).setPreferredWidth(80);
        tableUser.getColumnModel().getColumn(7).setPreferredWidth(120);
        
        // Selection listener
        tableUser.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableUser.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedUserId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    loadUserData(selectedUserId);
                }
            }
        });
        
        // Scroll pane
        scrollPane = new JScrollPane(tableUser);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Load table data from database
     */
    private void loadTableData() {
        try {
            // Clear table
            tableModel.setRowCount(0);
            
            // Load data
            String sql = "SELECT * FROM users ORDER BY created_at DESC";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("username"));
                row.add(rs.getString("nama_lengkap"));
                row.add(rs.getString("email"));
                row.add(rs.getString("telepon"));
                row.add(rs.getString("role"));
                row.add(rs.getString("status"));
                row.add(rs.getString("created_at"));
                
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Load user data to form
     */
    private void loadUserData(int userId) {
        try {
            String sql = "SELECT * FROM users WHERE id = " + userId;
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                txtUsername.setText(rs.getString("username"));
                txtPassword.setText(""); // Don't show password
                txtNamaLengkap.setText(rs.getString("nama_lengkap"));
                txtEmail.setText(rs.getString("email"));
                txtTelepon.setText(rs.getString("telepon"));
                cbRole.setSelectedItem(rs.getString("role"));
                cbStatus.setSelectedItem(rs.getString("status"));
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading user data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Set form mode
     */
    private void setFormMode(String newMode) {
        mode = newMode;
        
        switch (mode) {
            case "VIEW":
                setFieldsEnabled(false);
                btnTambah.setVisible(true);
                btnEdit.setVisible(true);
                btnHapus.setVisible(true);
                btnSimpan.setVisible(false);
                btnBatal.setVisible(false);
                clearForm();
                break;
                
            case "ADD":
                setFieldsEnabled(true);
                btnTambah.setVisible(false);
                btnEdit.setVisible(false);
                btnHapus.setVisible(false);
                btnSimpan.setVisible(true);
                btnBatal.setVisible(true);
                clearForm();
                txtUsername.requestFocus();
                break;
                
            case "EDIT":
                setFieldsEnabled(true);
                txtUsername.setEnabled(false); // Username tidak bisa diubah
                btnTambah.setVisible(false);
                btnEdit.setVisible(false);
                btnHapus.setVisible(false);
                btnSimpan.setVisible(true);
                btnBatal.setVisible(true);
                txtPassword.requestFocus();
                break;
        }
    }
    
    /**
     * Set fields enabled/disabled
     */
    private void setFieldsEnabled(boolean enabled) {
        txtUsername.setEnabled(enabled);
        txtPassword.setEnabled(enabled);
        txtNamaLengkap.setEnabled(enabled);
        txtEmail.setEnabled(enabled);
        txtTelepon.setEnabled(enabled);
        cbRole.setEnabled(enabled);
        cbStatus.setEnabled(enabled);
    }
    
    /**
     * Clear form
     */
    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtNamaLengkap.setText("");
        txtEmail.setText("");
        txtTelepon.setText("");
        cbRole.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        selectedUserId = -1;
    }
    
    /**
     * Edit user
     */
    private void editUser() {
        if (selectedUserId == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih user yang akan diedit!", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        setFormMode("EDIT");
    }
    
    /**
     * Delete user
     */
    private void deleteUser() {
        if (selectedUserId == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih user yang akan dihapus!", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus user ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM users WHERE id = " + selectedUserId;
                stmt.execute(sql);
                
                JOptionPane.showMessageDialog(this, 
                    "User berhasil dihapus!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                loadTableData();
                clearForm();
                selectedUserId = -1;
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error menghapus user: " + e.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Save user (add or edit)
     */
    private void saveUser() {
        // Validasi input
        if (txtUsername.getText().trim().isEmpty() || 
            txtPassword.getPassword().length == 0 || 
            txtNamaLengkap.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Username, Password, dan Nama Lengkap harus diisi!", 
                "Validasi Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validasi email format
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, 
                "Format email tidak valid!", 
                "Validasi Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String sql;
            
            if (mode.equals("ADD")) {
                // Check username exists
                String checkSql = "SELECT COUNT(*) FROM users WHERE username = '" + txtUsername.getText().trim() + "'";
                rs = stmt.executeQuery(checkSql);
                
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Username sudah digunakan!", 
                        "Validasi Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                sql = "INSERT INTO users (username, password, nama_lengkap, email, telepon, role, status) VALUES (" +
                      "'" + txtUsername.getText().trim() + "', " +
                      "'" + new String(txtPassword.getPassword()) + "', " +
                      "'" + txtNamaLengkap.getText().trim() + "', " +
                      "'" + email + "', " +
                      "'" + txtTelepon.getText().trim() + "', " +
                      "'" + cbRole.getSelectedItem() + "', " +
                      "'" + cbStatus.getSelectedItem() + "')";
            } else {
                sql = "UPDATE users SET " +
                      "password = '" + new String(txtPassword.getPassword()) + "', " +
                      "nama_lengkap = '" + txtNamaLengkap.getText().trim() + "', " +
                      "email = '" + email + "', " +
                      "telepon = '" + txtTelepon.getText().trim() + "', " +
                      "role = '" + cbRole.getSelectedItem() + "', " +
                      "status = '" + cbStatus.getSelectedItem() + "', " +
                      "updated_at = CURRENT_TIMESTAMP " +
                      "WHERE id = " + selectedUserId;
            }
            
            stmt.execute(sql);
            
            JOptionPane.showMessageDialog(this, 
                "User berhasil disimpan!", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTableData();
            setFormMode("VIEW");
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error menyimpan user: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$");
    }
    
    /**
     * Main method for testing
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManajemenUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManajemenUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManajemenUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManajemenUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManajemenUser().setVisible(true);
            }
        });
    }
}
