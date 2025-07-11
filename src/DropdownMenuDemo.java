import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Demo untuk menunjukkan dropdown menu yang berfungsi
 * Seperti yang diminta - header dengan menu yang ketika diklik menampilkan submenu
 */
public class DropdownMenuDemo extends JFrame {
    
    // SIPRIMA Color Palette
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color HOVER_BLUE = new Color(52, 152, 219);
    private static final Color WHITE = new Color(255, 255, 255);
    
    public DropdownMenuDemo() {
        initializeFrame();
        createHeaderWithDropdownMenus();
        createMainContent();
    }
    
    private void initializeFrame() {
        setTitle("SIPRIMA - Demo Dropdown Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(236, 240, 241));
    }
    
    private void createHeaderWithDropdownMenus() {
        // Create header panel dengan background biru
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Left menu navigation
        JPanel leftMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftMenu.setOpaque(false);
        
        // Create menu buttons dengan dropdown functionality
        JButton berandaButton = createMenuButton("🏠 Beranda", true);
        JButton aduanButton = createMenuButton("📋 Aduan", false);
        JButton dataButton = createMenuButton("📈 Data", false);
        JButton laporanButton = createMenuButton("📊 Laporan", false);
        JButton toolsButton = createMenuButton("⚙️ Tools", false);
        
        // Add action listeners untuk dropdown menus
        berandaButton.addActionListener(e -> showBerandaMenu(berandaButton));
        aduanButton.addActionListener(e -> showAduanMenu(aduanButton));
        dataButton.addActionListener(e -> showDataMenu(dataButton));
        laporanButton.addActionListener(e -> showLaporanMenu(laporanButton));
        toolsButton.addActionListener(e -> showToolsMenu(toolsButton));
        
        // Add buttons to left menu
        leftMenu.add(berandaButton);
        leftMenu.add(aduanButton);
        leftMenu.add(dataButton);
        leftMenu.add(laporanButton);
        leftMenu.add(toolsButton);
        
        // Right menu - User menu
        JPanel rightMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightMenu.setOpaque(false);
        
        JButton userButton = createMenuButton("👤 User ▼", false);
        userButton.addActionListener(e -> showUserMenu(userButton));
        rightMenu.add(userButton);
        
        // Add menus to header
        headerPanel.add(leftMenu, BorderLayout.WEST);
        headerPanel.add(rightMenu, BorderLayout.EAST);
        
        // Add header to frame
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private JButton createMenuButton(String text, boolean isActive) {
        JButton button = new JButton(text);
        
        // Basic styling
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(WHITE);
        button.setBackground(PRIMARY_BLUE);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        // Active button styling
        if (isActive) {
            button.setOpaque(true);
            button.setBackground(HOVER_BLUE);
            button.setContentAreaFilled(true);
        }
        
        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isActive) {
                    button.setOpaque(true);
                    button.setBackground(HOVER_BLUE);
                    button.setContentAreaFilled(true);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive) {
                    button.setOpaque(false);
                    button.setContentAreaFilled(false);
                }
            }
        });
        
        return button;
    }
    
    // =============================================
    // DROPDOWN MENU IMPLEMENTATIONS
    // =============================================
    
    private void showBerandaMenu(JButton sourceButton) {
        JPopupMenu berandaMenu = createStyledPopupMenu();
        
        berandaMenu.add(createMenuItem("🏠 Dashboard", "Kembali ke dashboard utama"));
        berandaMenu.add(createMenuItem("🔄 Refresh Data", "Muat ulang data terbaru"));
        berandaMenu.addSeparator();
        berandaMenu.add(createMenuItem("📋 Panduan Cepat", "Bantuan menggunakan sistem"));
        
        showDropdownMenu(berandaMenu, sourceButton);
    }
    
    private void showAduanMenu(JButton sourceButton) {
        JPopupMenu aduanMenu = createStyledPopupMenu();
        
        aduanMenu.add(createMenuItem("📋 Semua Aduan", "Lihat daftar semua aduan"));
        aduanMenu.add(createMenuItem("➕ Aduan Baru", "Tambah aduan baru"));
        aduanMenu.addSeparator();
        aduanMenu.add(createMenuItem("🆕 Status Baru", "Aduan dengan status baru"));
        aduanMenu.add(createMenuItem("⚙️ Sedang Diproses", "Aduan yang sedang ditangani"));
        aduanMenu.add(createMenuItem("✅ Selesai", "Aduan yang telah selesai"));
        aduanMenu.addSeparator();
        aduanMenu.add(createMenuItem("🔴 Darurat", "Aduan dengan prioritas darurat"));
        
        showDropdownMenu(aduanMenu, sourceButton);
    }
    
    private void showDataMenu(JButton sourceButton) {
        JPopupMenu dataMenu = createStyledPopupMenu();
        
        dataMenu.add(createMenuItem("👥 Data Masyarakat", "Kelola data masyarakat"));
        dataMenu.add(createMenuItem("👨‍💼 Data Petugas", "Kelola data petugas"));
        dataMenu.addSeparator();
        dataMenu.add(createMenuItem("📂 Kategori Aduan", "Kelola kategori aduan"));
        dataMenu.add(createMenuItem("🏢 Data Desa", "Informasi desa dan wilayah"));
        
        showDropdownMenu(dataMenu, sourceButton);
    }
    
    private void showLaporanMenu(JButton sourceButton) {
        JPopupMenu laporanMenu = createStyledPopupMenu();
        
        laporanMenu.add(createMenuItem("📈 Dashboard Analytics", "Analisis dashboard"));
        laporanMenu.add(createMenuItem("📋 Laporan Aduan", "Laporan aduan per periode"));
        laporanMenu.addSeparator();
        laporanMenu.add(createMenuItem("📄 Laporan Harian", "Laporan aktivitas harian"));
        laporanMenu.add(createMenuItem("📅 Laporan Bulanan", "Laporan bulanan lengkap"));
        laporanMenu.add(createMenuItem("📆 Laporan Tahunan", "Laporan tahunan"));
        laporanMenu.addSeparator();
        laporanMenu.add(createMenuItem("📤 Export Excel", "Export data ke Excel"));
        laporanMenu.add(createMenuItem("📄 Export PDF", "Export laporan ke PDF"));
        
        showDropdownMenu(laporanMenu, sourceButton);
    }
    
    private void showToolsMenu(JButton sourceButton) {
        JPopupMenu toolsMenu = createStyledPopupMenu();
        
        toolsMenu.add(createMenuItem("🔧 Pengaturan Sistem", "Konfigurasi sistem"));
        toolsMenu.add(createMenuItem("🎨 Tema Aplikasi", "Ubah tema tampilan"));
        toolsMenu.addSeparator();
        toolsMenu.add(createMenuItem("📊 Database Status", "Status koneksi database"));
        toolsMenu.add(createMenuItem("🧠 Memory Usage", "Informasi penggunaan memori"));
        toolsMenu.addSeparator();
        toolsMenu.add(createMenuItem("📋 Log Sistem", "Lihat log sistem"));
        toolsMenu.add(createMenuItem("🔄 Backup Data", "Backup database"));
        
        showDropdownMenu(toolsMenu, sourceButton);
    }
    
    private void showUserMenu(JButton sourceButton) {
        JPopupMenu userMenu = createStyledPopupMenu();
        
        userMenu.add(createMenuItem("👤 Profil Saya", "Lihat dan edit profil"));
        userMenu.add(createMenuItem("🔑 Ubah Password", "Ganti password"));
        userMenu.addSeparator();
        userMenu.add(createMenuItem("ℹ️ Info Session", "Informasi sesi login"));
        userMenu.add(createMenuItem("🔔 Notifikasi", "Lihat notifikasi"));
        userMenu.addSeparator();
        
        JMenuItem logoutItem = createMenuItem("🚪 Logout", "Keluar dari sistem");
        logoutItem.setForeground(new Color(231, 76, 60)); // Red color for logout
        userMenu.add(logoutItem);
        
        showDropdownMenu(userMenu, sourceButton);
    }
    
    // =============================================
    // HELPER METHODS
    // =============================================
    
    private JPopupMenu createStyledPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(WHITE);
        popupMenu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 221, 225), 1),
            BorderFactory.createEmptyBorder(4, 0, 4, 0)
        ));
        return popupMenu;
    }
    
    private JMenuItem createMenuItem(String text, String tooltip) {
        JMenuItem item = new JMenuItem(text);
        item.setToolTipText(tooltip);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        item.setBackground(WHITE);
        item.setForeground(new Color(44, 62, 80));
        
        // Add action listener untuk demo
        item.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Menu item dipilih:\n\n" +
                "📋 " + text + "\n" +
                "💡 " + tooltip + "\n\n" +
                "Fitur ini akan diimplementasikan sesuai kebutuhan.",
                "Menu Action",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Hover effects untuk menu items
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(248, 249, 250));
                item.setOpaque(true);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(WHITE);
                item.setOpaque(true);
            }
        });
        
        return item;
    }
    
    private void showDropdownMenu(JPopupMenu menu, JButton sourceButton) {
        // Show dropdown tepat di bawah button
        menu.show(sourceButton, 0, sourceButton.getHeight());
        System.out.println("✅ Dropdown menu shown for: " + sourceButton.getText());
    }
    
    private void createMainContent() {
        // Create main content area
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(236, 240, 241));
        mainContent.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Welcome message
        JLabel welcomeLabel = new JLabel(
            "<html><center>" +
            "<h1>🎉 SIPRIMA Dropdown Menu Demo</h1>" +
            "<p style='font-size: 16px; color: #7f8c8d;'>Klik menu di header untuk melihat dropdown submenu</p>" +
            "<br>" +
            "<p style='font-size: 14px;'>" +
            "✅ Header dengan background biru<br>" +
            "✅ Menu dengan hover effects<br>" +
            "✅ Dropdown submenu yang muncul saat diklik<br>" +
            "✅ Styling modern dan responsif" +
            "</p>" +
            "</center></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setOpaque(true);
        welcomeLabel.setBackground(WHITE);
        welcomeLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 221, 225), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        mainContent.add(welcomeLabel, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);
    }
    
    public static void main(String[] args) {
        // Create and show demo
        
        // Create and show demo
        SwingUtilities.invokeLater(() -> {
            new DropdownMenuDemo().setVisible(true);
        });
    }
}

