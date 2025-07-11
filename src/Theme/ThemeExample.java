/*
 * Contoh Penggunaan Theme Siprima
 * Demonstrasi cara menggunakan komponen-komponen theme yang telah dibuat
 */
package Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author febry
 * Contoh implementasi penggunaan theme Siprima
 */
public class ThemeExample {
    
    public static void main(String[] args) {
        // Inisialisasi tema
        ThemeManager.initializeTheme();
        
        SwingUtilities.invokeLater(() -> {
            createAndShowMainWindow();
        });
    }
    
    private static void createAndShowMainWindow() {
        // Buat frame utama
        JFrame frame = ThemeManager.createStyledFrame("Siprima - Contoh Penggunaan Theme");
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        
        // Buat komponen utama
        JPanel headerPanel = createHeaderPanel();
        JPanel sidebarPanel = createSidebarPanel();
        JPanel contentPanel = createContentPanel();
        
        // Tambahkan ke frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(sidebarPanel, BorderLayout.WEST);
        frame.add(contentPanel, BorderLayout.CENTER);
        
        frame.setVisible(true);
    }
    
    private static JPanel createHeaderPanel() {
        JPanel headerPanel = ThemeManager.createHeaderPanel("SIPRIMA - Sistem Informasi Pengaduan Masyarakat");
        
        // Tambahkan tombol dark mode toggle
        CustomButton darkModeBtn = new CustomButton("Toggle Dark Mode", CustomButton.ButtonType.SECONDARY, CustomButton.ButtonSize.SMALL);
        darkModeBtn.addActionListener(e -> {
            ThemeManager.toggleDarkMode();
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(darkModeBtn));
        });
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(darkModeBtn);
        
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private static JPanel createSidebarPanel() {
        JPanel sidebarPanel = ThemeManager.createSidebarPanel();
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Menu items
        String[] menuItems = {
            "Dashboard", "Input Aduan", "Data Aduan", "Laporan", 
            "Manajemen User", "Profil", "Pengaturan", "Logout"
        };
        
        CustomButton.ButtonType[] buttonTypes = {
            CustomButton.ButtonType.PRIMARY, CustomButton.ButtonType.INFO, CustomButton.ButtonType.SUCCESS,
            CustomButton.ButtonType.WARNING, CustomButton.ButtonType.SECONDARY, CustomButton.ButtonType.INFO,
            CustomButton.ButtonType.SECONDARY, CustomButton.ButtonType.DANGER
        };
        
        for (int i = 0; i < menuItems.length; i++) {
            CustomButton menuBtn = new CustomButton(menuItems[i], buttonTypes[i], CustomButton.ButtonSize.MEDIUM);
            menuBtn.setPreferredSize(new Dimension(200, 40));
            menuBtn.setMaximumSize(new Dimension(200, 40));
            menuBtn.setRoundedCorners(true);
            menuBtn.setBorderRadius(6);
            
            // Tambahkan margin
            menuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidebarPanel.add(Box.createVerticalStrut(5));
            sidebarPanel.add(menuBtn);
            
            // Action listener untuk demo
            final String itemName = menuItems[i];
            menuBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(sidebarPanel, "Menu \"" + itemName + "\" diklik!", 
                    "Demo", JOptionPane.INFORMATION_MESSAGE);
            });
        }
        
        sidebarPanel.add(Box.createVerticalGlue());
        
        return sidebarPanel;
    }
    
    private static JPanel createContentPanel() {
        JPanel contentPanel = ThemeManager.createContentPanel();
        
        // Buat tab pane untuk demo berbagai komponen
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        // Tab 1: Form Components
        tabbedPane.addTab("Form Components", createFormDemoPanel());
        
        // Tab 2: Table Components
        tabbedPane.addTab("Table Demo", createTableDemoPanel());
        
        // Tab 3: Button Variations
        tabbedPane.addTab("Button Variations", createButtonDemoPanel());
        
        // Tab 4: Color Palette
        tabbedPane.addTab("Color Palette", createColorPalettePanel());
        
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        
        return contentPanel;
    }
    
    private static JPanel createFormDemoPanel() {
        JPanel panel = ThemeManager.createCardPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        JLabel titleLabel = new JLabel("Form Components Demo");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(SiprimaPalette.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Custom Text Fields
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1;
        CustomTextField namaField = new CustomTextField("Masukkan nama Anda");
        namaField.setPreferredSize(new Dimension(250, 40));
        panel.add(namaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        CustomTextField emailField = new CustomTextField("contoh@email.com");
        emailField.setPreferredSize(new Dimension(250, 40));
        panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField();
        ThemeManager.styleTextField(passwordField);
        passwordField.setPreferredSize(new Dimension(250, 40));
        panel.add(passwordField, gbc);
        
        // Error demo
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Field dengan Error:"), gbc);
        gbc.gridx = 1;
        CustomTextField errorField = new CustomTextField("Field ini error");
        errorField.setPreferredSize(new Dimension(250, 40));
        errorField.setErrorMessage("Field ini wajib diisi!");
        panel.add(errorField, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        CustomButton submitBtn = new CustomButton("Submit", CustomButton.ButtonType.PRIMARY);
        CustomButton resetBtn = new CustomButton("Reset", CustomButton.ButtonType.SECONDARY);
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(resetBtn);
        panel.add(buttonPanel, gbc);
        
        // Action listeners
        submitBtn.addActionListener(e -> {
            String message = "Data yang disubmit:\n" +
                           "Nama: " + namaField.getText() + "\n" +
                           "Email: " + emailField.getText();
            JOptionPane.showMessageDialog(panel, message, "Form Submitted", JOptionPane.INFORMATION_MESSAGE);
        });
        
        resetBtn.addActionListener(e -> {
            namaField.setText("");
            emailField.setText("");
            passwordField.setText("");
            errorField.clearError();
        });
        
        return panel;
    }
    
    private static JPanel createTableDemoPanel() {
        JPanel panel = ThemeManager.createCardPanel();
        panel.setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Custom Table Demo");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Nama", "Email", "Status", "Tanggal"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        // Sample data
        Object[][] data = {
            {1, "Ahmad Febriansyah", "ahmad@email.com", "Aktif", "2024-01-15"},
            {2, "Siti Nurhaliza", "siti@email.com", "Aktif", "2024-01-16"},
            {3, "Budi Santoso", "budi@email.com", "Nonaktif", "2024-01-17"},
            {4, "Dewi Sartika", "dewi@email.com", "Aktif", "2024-01-18"},
            {5, "Ridwan Kamil", "ridwan@email.com", "Pending", "2024-01-19"}
        };
        
        for (Object[] row : data) {
            model.addRow(row);
        }
        
        CustomTable table = new CustomTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        
        CustomButton addBtn = new CustomButton("Tambah", CustomButton.ButtonType.SUCCESS, CustomButton.ButtonSize.SMALL);
        CustomButton editBtn = new CustomButton("Edit", CustomButton.ButtonType.WARNING, CustomButton.ButtonSize.SMALL);
        CustomButton deleteBtn = new CustomButton("Hapus", CustomButton.ButtonType.DANGER, CustomButton.ButtonSize.SMALL);
        
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Action listeners
        deleteBtn.addActionListener(e -> {
            Object[] selectedData = table.getSelectedRowData();
            if (selectedData != null) {
                int result = JOptionPane.showConfirmDialog(panel, 
                    "Hapus data \"" + selectedData[1] + "\"?", 
                    "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    table.removeSelectedRow();
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Pilih baris yang akan dihapus!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        return panel;
    }
    
    private static JPanel createButtonDemoPanel() {
        JPanel panel = ThemeManager.createCardPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("Button Variations Demo");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Button types
        CustomButton.ButtonType[] types = CustomButton.ButtonType.values();
        String[] typeNames = {"Primary", "Success", "Warning", "Danger", "Secondary", "Info"};
        
        int row = 1;
        for (int i = 0; i < types.length; i++) {
            // Small
            gbc.gridx = 0; gbc.gridy = row;
            CustomButton smallBtn = new CustomButton(typeNames[i] + " Small", types[i], CustomButton.ButtonSize.SMALL);
            panel.add(smallBtn, gbc);
            
            // Medium
            gbc.gridx = 1;
            CustomButton mediumBtn = new CustomButton(typeNames[i] + " Medium", types[i], CustomButton.ButtonSize.MEDIUM);
            panel.add(mediumBtn, gbc);
            
            // Large
            gbc.gridx = 2;
            CustomButton largeBtn = new CustomButton(typeNames[i] + " Large", types[i], CustomButton.ButtonSize.LARGE);
            panel.add(largeBtn, gbc);
            
            row++;
        }
        
        return panel;
    }
    
    private static JPanel createColorPalettePanel() {
        JPanel panel = ThemeManager.createCardPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        JLabel titleLabel = new JLabel("Siprima Color Palette");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Color samples
        Color[] colors = {
            SiprimaPalette.PRIMARY_BLUE, SiprimaPalette.PRIMARY_GREEN,
            SiprimaPalette.SECONDARY_ORANGE, SiprimaPalette.SECONDARY_RED,
            SiprimaPalette.SUCCESS, SiprimaPalette.WARNING,
            SiprimaPalette.ERROR, SiprimaPalette.INFO
        };
        
        String[] colorNames = {
            "Primary Blue", "Primary Green",
            "Secondary Orange", "Secondary Red",
            "Success", "Warning",
            "Error", "Info"
        };
        
        int row = 1;
        for (int i = 0; i < colors.length; i += 4) {
            for (int j = 0; j < 4 && (i + j) < colors.length; j++) {
                int index = i + j;
                
                gbc.gridx = j; gbc.gridy = row;
                JPanel colorPanel = createColorSample(colors[index], colorNames[index]);
                panel.add(colorPanel, gbc);
            }
            row++;
        }
        
        return panel;
    }
    
    private static JPanel createColorSample(Color color, String name) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(120, 80));
        panel.setBorder(BorderFactory.createLineBorder(SiprimaPalette.BORDER_LIGHT));
        
        JPanel colorDisplay = new JPanel();
        colorDisplay.setBackground(color);
        colorDisplay.setPreferredSize(new Dimension(120, 50));
        
        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel hexLabel = new JLabel(SiprimaPalette.toHex(color), SwingConstants.CENTER);
        hexLabel.setFont(new Font("Monospaced", Font.PLAIN, 9));
        hexLabel.setForeground(SiprimaPalette.TEXT_SECONDARY);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(SiprimaPalette.WHITE);
        textPanel.add(nameLabel);
        textPanel.add(hexLabel);
        
        panel.add(colorDisplay, BorderLayout.CENTER);
        panel.add(textPanel, BorderLayout.SOUTH);
        
        return panel;
    }
}

