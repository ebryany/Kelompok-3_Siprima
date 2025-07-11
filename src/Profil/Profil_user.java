/*
 * Sistem Informasi Primadi Desa Tarabbi
 * Profil User - Modul untuk mengelola profil pengguna
 * @author febry
 */
package Profil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.Pattern;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Form Profil User untuk mengelola data profil pengguna
 * Fitur: Lihat profil, edit profil, ganti password, upload foto
 */
public class Profil_user extends javax.swing.JFrame {
    
    // Komponen UI
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JPanel buttonPanel;
    private JPanel photoPanel;
    
    // Data fields
    private JTextField txtNama;
    private JTextField txtEmail;
    private JTextField txtTelepon;
    private JTextField txtAlamat;
    private JTextField txtNIK;
    private JTextField txtJabatan;
    private JComboBox<String> cbJenisKelamin;
    private JTextField txtTanggalLahir;
    private JLabel lblFoto;
    private JLabel lblLastUpdate;
    
    // Password fields
    private JPasswordField txtPasswordLama;
    private JPasswordField txtPasswordBaru;
    private JPasswordField txtKonfirmasiPassword;
    
    // Buttons
    private JButton btnEdit;
    private JButton btnSimpan;
    private JButton btnBatal;
    private JButton btnGantiPassword;
    private JButton btnUploadFoto;
    private JButton btnKembali;
    
    // Status tracking
    private boolean isEditing = false;
    private String currentUserId = "1"; // Default untuk testing
    private String currentPhotoPath = "";
    
    // Validasi patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10,15}$"
    );
    private static final Pattern NIK_PATTERN = Pattern.compile(
        "^[0-9]{16}$"
    );
    
    /**
     * Creates new form Profil_user
     */
    public Profil_user() {
        initComponents();
        setupUI();
        loadUserData();
        setLocationRelativeTo(null);
    }
    
    /**
     * Constructor dengan parameter userId
     */
    public Profil_user(String userId) {
        this.currentUserId = userId;
        initComponents();
        setupUI();
        loadUserData();
        setLocationRelativeTo(null);
    }
    
    /**
     * Setup UI components dan layout
     */
    private void setupUI() {
        setTitle("Profil Pengguna - Siprima Desa Tarabbi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        
        // Main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header panel
        createHeaderPanel();
        
        // Content panel
        createContentPanel();
        
        // Button panel
        createButtonPanel();
        
        // Assembly
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Initial state
        setEditMode(false);
        
        // Event listeners
        setupEventListeners();
    }
    
    /**
     * Create header panel dengan judul dan info
     */
    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel titleLabel = new JLabel("Profil Pengguna");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        
        lblLastUpdate = new JLabel("Terakhir diperbarui: -");
        lblLastUpdate.setFont(new Font("Arial", Font.PLAIN, 12));
        lblLastUpdate.setForeground(Color.GRAY);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(lblLastUpdate, BorderLayout.EAST);
    }
    
    /**
     * Create content panel dengan form fields
     */
    private void createContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        
        // Left panel - Photo
        createPhotoPanel();
        
        // Right panel - Form data
        createFormPanel();
        
        // Password panel
        createPasswordPanel();
        
        // Layout
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(photoPanel, BorderLayout.NORTH);
        leftPanel.add(Box.createVerticalGlue(), BorderLayout.CENTER);
        
        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(createFormPanel(), BorderLayout.CENTER);
    }
    
    /**
     * Create photo panel
     */
    private void createPhotoPanel() {
        photoPanel = new JPanel(new BorderLayout());
        photoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Foto Profil",
            TitledBorder.CENTER,
            TitledBorder.TOP
        ));
        photoPanel.setPreferredSize(new Dimension(200, 280));
        
        // Photo label
        lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(JLabel.CENTER);
        lblFoto.setVerticalAlignment(JLabel.CENTER);
        lblFoto.setPreferredSize(new Dimension(150, 200));
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.WHITE);
        
        // Default photo
        setDefaultPhoto();
        
        // Upload button
        btnUploadFoto = new JButton("Upload Foto");
        btnUploadFoto.setPreferredSize(new Dimension(150, 30));
        
        JPanel photoContainer = new JPanel(new BorderLayout());
        photoContainer.add(lblFoto, BorderLayout.CENTER);
        photoContainer.add(btnUploadFoto, BorderLayout.SOUTH);
        
        photoPanel.add(photoContainer, BorderLayout.CENTER);
    }
    
    /**
     * Create form panel dengan data fields
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "Informasi Pribadi",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Initialize fields
        txtNama = new JTextField(20);
        txtEmail = new JTextField(20);
        txtTelepon = new JTextField(20);
        txtAlamat = new JTextField(20);
        txtNIK = new JTextField(20);
        txtJabatan = new JTextField(20);
        cbJenisKelamin = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});
        txtTanggalLahir = new JTextField(20);
        
        // Add fields to form
        addFormField(formPanel, gbc, "Nama Lengkap:", txtNama, 0);
        addFormField(formPanel, gbc, "Email:", txtEmail, 1);
        addFormField(formPanel, gbc, "Telepon:", txtTelepon, 2);
        addFormField(formPanel, gbc, "Alamat:", txtAlamat, 3);
        addFormField(formPanel, gbc, "NIK:", txtNIK, 4);
        addFormField(formPanel, gbc, "Jabatan:", txtJabatan, 5);
        addFormField(formPanel, gbc, "Jenis Kelamin:", cbJenisKelamin, 6);
        addFormField(formPanel, gbc, "Tanggal Lahir:", txtTanggalLahir, 7);
        
        return formPanel;
    }
    
    /**
     * Add field to form panel
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
        gbc.weightx = 0;
    }
    
    /**
     * Create password panel
     */
    private void createPasswordPanel() {
        // Password panel implementation would go here
        // For brevity, creating a simple panel
    }
    
    /**
     * Create button panel
     */
    private void createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnEdit = new JButton("Edit Profil");
        btnSimpan = new JButton("Simpan");
        btnBatal = new JButton("Batal");
        btnGantiPassword = new JButton("Ganti Password");
        btnKembali = new JButton("Kembali");
        
        // Button styling
        styleButton(btnEdit, new Color(52, 152, 219), Color.WHITE);
        styleButton(btnSimpan, new Color(46, 204, 113), Color.WHITE);
        styleButton(btnBatal, new Color(231, 76, 60), Color.WHITE);
        styleButton(btnGantiPassword, new Color(155, 89, 182), Color.WHITE);
        styleButton(btnKembali, new Color(149, 165, 166), Color.WHITE);
        
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        buttonPanel.add(btnGantiPassword);
        buttonPanel.add(btnKembali);
    }
    
    /**
     * Style button dengan warna dan efek
     */
    private void styleButton(JButton button, Color bgColor, Color textColor) {
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 35));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setFocusPainted(false);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    /**
     * Setup event listeners untuk semua komponen
     */
    private void setupEventListeners() {
        btnEdit.addActionListener(e -> toggleEditMode());
        btnSimpan.addActionListener(e -> saveProfile());
        btnBatal.addActionListener(e -> cancelEdit());
        btnGantiPassword.addActionListener(e -> openPasswordDialog());
        btnUploadFoto.addActionListener(e -> uploadPhoto());
        btnKembali.addActionListener(e -> dispose());
        
        // Real-time validation
        txtEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateEmail();
            }
        });
        
        txtTelepon.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validatePhone();
            }
        });
        
        txtNIK.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateNIK();
            }
        });
    }
    
    /**
     * Load user data dari database
     */
    private void loadUserData() {
        try {
            // Simulasi load data dari database
            // Dalam implementasi sebenarnya, gunakan koneksi database
            txtNama.setText("John Doe");
            txtEmail.setText("john.doe@example.com");
            txtTelepon.setText("08123456789");
            txtAlamat.setText("Jl. Contoh No. 123, Tarabbi");
            txtNIK.setText("1234567890123456");
            txtJabatan.setText("Kepala Desa");
            cbJenisKelamin.setSelectedItem("Laki-laki");
            txtTanggalLahir.setText("01/01/1980");
            
            lblLastUpdate.setText("Terakhir diperbarui: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            
        } catch (Exception e) {
            showError("Gagal memuat data profil: " + e.getMessage());
        }
    }
    
    /**
     * Toggle edit mode
     */
    private void toggleEditMode() {
        setEditMode(!isEditing);
    }
    
    /**
     * Set edit mode
     */
    private void setEditMode(boolean editing) {
        isEditing = editing;
        
        // Enable/disable fields
        txtNama.setEditable(editing);
        txtEmail.setEditable(editing);
        txtTelepon.setEditable(editing);
        txtAlamat.setEditable(editing);
        txtNIK.setEditable(editing);
        txtJabatan.setEditable(editing);
        cbJenisKelamin.setEnabled(editing);
        txtTanggalLahir.setEditable(editing);
        btnUploadFoto.setEnabled(editing);
        
        // Show/hide buttons
        btnEdit.setVisible(!editing);
        btnSimpan.setVisible(editing);
        btnBatal.setVisible(editing);
        
        // Field appearance
        Color bgColor = editing ? Color.WHITE : new Color(245, 245, 245);
        txtNama.setBackground(bgColor);
        txtEmail.setBackground(bgColor);
        txtTelepon.setBackground(bgColor);
        txtAlamat.setBackground(bgColor);
        txtNIK.setBackground(bgColor);
        txtJabatan.setBackground(bgColor);
        txtTanggalLahir.setBackground(bgColor);
    }
    
    /**
     * Save profile data
     */
    private void saveProfile() {
        if (!validateAllFields()) {
            return;
        }
        
        try {
            // Simulasi save ke database
            // Dalam implementasi sebenarnya, gunakan koneksi database
            
            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Profil berhasil diperbarui!", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Update last modified
            lblLastUpdate.setText("Terakhir diperbarui: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            
            // Exit edit mode
            setEditMode(false);
            
        } catch (Exception e) {
            showError("Gagal menyimpan profil: " + e.getMessage());
        }
    }
    
    /**
     * Cancel edit
     */
    private void cancelEdit() {
        int result = JOptionPane.showConfirmDialog(this,
            "Batalkan perubahan?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            loadUserData(); // Reload original data
            setEditMode(false);
        }
    }
    
    /**
     * Open password change dialog
     */
    private void openPasswordDialog() {
        PasswordChangeDialog dialog = new PasswordChangeDialog(this);
        dialog.setVisible(true);
    }
    
    /**
     * Upload photo
     */
    private void uploadPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png", "gif"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Copy file to application directory
                String fileName = "profile_" + currentUserId + "_" + 
                    System.currentTimeMillis() + ".jpg";
                File destFile = new File("photos/" + fileName);
                
                // Create directory if not exists
                destFile.getParentFile().mkdirs();
                
                // Copy file
                Files.copy(selectedFile.toPath(), destFile.toPath(), 
                    StandardCopyOption.REPLACE_EXISTING);
                
                // Update photo display
                ImageIcon icon = new ImageIcon(destFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
                
                currentPhotoPath = destFile.getAbsolutePath();
                
                JOptionPane.showMessageDialog(this, 
                    "Foto berhasil diupload!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException e) {
                showError("Gagal mengupload foto: " + e.getMessage());
            }
        }
    }
    
    /**
     * Set default photo
     */
    private void setDefaultPhoto() {
        lblFoto.setText("<html><center>Tidak ada foto<br>Klik 'Upload Foto'<br>untuk menambahkan</center></html>");
        lblFoto.setIcon(null);
    }
    
    /**
     * Validate all fields
     */
    private boolean validateAllFields() {
        if (txtNama.getText().trim().isEmpty()) {
            showError("Nama harus diisi!");
            txtNama.requestFocus();
            return false;
        }
        
        if (!validateEmail()) {
            return false;
        }
        
        if (!validatePhone()) {
            return false;
        }
        
        if (!validateNIK()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Validate email
     */
    private boolean validateEmail() {
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            showFieldError(txtEmail, "Email harus diisi!");
            return false;
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showFieldError(txtEmail, "Format email tidak valid!");
            return false;
        }
        
        clearFieldError(txtEmail);
        return true;
    }
    
    /**
     * Validate phone
     */
    private boolean validatePhone() {
        String phone = txtTelepon.getText().trim();
        if (phone.isEmpty()) {
            showFieldError(txtTelepon, "Telepon harus diisi!");
            return false;
        }
        
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            showFieldError(txtTelepon, "Format telepon tidak valid! (10-15 digit)");
            return false;
        }
        
        clearFieldError(txtTelepon);
        return true;
    }
    
    /**
     * Validate NIK
     */
    private boolean validateNIK() {
        String nik = txtNIK.getText().trim();
        if (nik.isEmpty()) {
            showFieldError(txtNIK, "NIK harus diisi!");
            return false;
        }
        
        if (!NIK_PATTERN.matcher(nik).matches()) {
            showFieldError(txtNIK, "NIK harus 16 digit!");
            return false;
        }
        
        clearFieldError(txtNIK);
        return true;
    }
    
    /**
     * Show field error
     */
    private void showFieldError(JTextField field, String message) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        field.setToolTipText(message);
        field.requestFocus();
    }
    
    /**
     * Clear field error
     */
    private void clearFieldError(JTextField field) {
        field.setBorder(UIManager.getBorder("TextField.border"));
        field.setToolTipText(null);
    }
    
    /**
     * Show error message
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Password change dialog
     */
    private class PasswordChangeDialog extends JDialog {
        private JPasswordField txtOldPassword;
        private JPasswordField txtNewPassword;
        private JPasswordField txtConfirmPassword;
        private JButton btnSave;
        private JButton btnCancel;
        
        public PasswordChangeDialog(JFrame parent) {
            super(parent, "Ganti Password", true);
            initDialog();
        }
        
        private void initDialog() {
            setSize(400, 250);
            setLocationRelativeTo(getParent());
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            txtOldPassword = new JPasswordField(20);
            txtNewPassword = new JPasswordField(20);
            txtConfirmPassword = new JPasswordField(20);
            
            gbc.gridx = 0; gbc.gridy = 0;
            panel.add(new JLabel("Password Lama:"), gbc);
            gbc.gridx = 1;
            panel.add(txtOldPassword, gbc);
            
            gbc.gridx = 0; gbc.gridy = 1;
            panel.add(new JLabel("Password Baru:"), gbc);
            gbc.gridx = 1;
            panel.add(txtNewPassword, gbc);
            
            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(new JLabel("Konfirmasi Password:"), gbc);
            gbc.gridx = 1;
            panel.add(txtConfirmPassword, gbc);
            
            btnSave = new JButton("Simpan");
            btnCancel = new JButton("Batal");
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);
            
            gbc.gridx = 0; gbc.gridy = 3;
            gbc.gridwidth = 2;
            panel.add(buttonPanel, gbc);
            
            add(panel);
            
            // Event listeners
            btnSave.addActionListener(e -> savePassword());
            btnCancel.addActionListener(e -> dispose());
        }
        
        private void savePassword() {
            String oldPassword = new String(txtOldPassword.getPassword());
            String newPassword = new String(txtNewPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Password baru tidak cocok!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password minimal 6 karakter!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Simulasi validasi password lama dan save password baru
            JOptionPane.showMessageDialog(this, "Password berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
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
        // Generated code akan dihandle oleh NetBeans Form Editor
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Profil Pengguna");
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Profil_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Profil_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Profil_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Profil_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Profil_user().setVisible(true);
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
