/*
 * SIPRIMA Modern Menu Bar
 * Custom menu bar dengan design modern dan interaktif
 */
package Theme;

import Utils.SessionManager;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

/**
 * Modern Menu Bar untuk aplikasi SIPRIMA
 * @author febry
 */
public class SiprimaMenuBar extends JMenuBar {
    
    // SIPRIMA Color Palette
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color HOVER_BLUE = new Color(52, 152, 219);
    private static final Color ACTIVE_BLUE = new Color(21, 67, 96);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color TEXT_MUTED = new Color(127, 140, 141);
    private static final Color SUCCESS = new Color(46, 204, 113);
    private static final Color WARNING = new Color(243, 156, 18);
    private static final Color ERROR = new Color(231, 76, 60);
    
    // Menu items
    private JMenu homeMenu;
    private JMenu aduanMenu;
    private JMenu dataMenu;
    private JMenu laporanMenu;
    private JMenu toolsMenu;
    private JMenu userMenu;
    
    // Action callbacks
    private Consumer<String> onMenuAction;
    
    // User info
    private User currentUser;
    private SessionManager sessionManager;
    
    /**
     * Constructor
     */
    public SiprimaMenuBar() {
        this.sessionManager = SessionManager.getInstance();
        this.currentUser = sessionManager.getCurrentUser();
        
        initializeMenuBar();
        createMenus();
        setupMenuStyling();
    }
    
    /**
     * Constructor dengan user parameter
     */
    public SiprimaMenuBar(User user) {
        this.currentUser = user;
        this.sessionManager = SessionManager.getInstance();
        
        initializeMenuBar();
        createMenus();
        setupMenuStyling();
    }
    
    /**
     * Set action callback untuk menu
     */
    public void setOnMenuAction(Consumer<String> callback) {
        this.onMenuAction = callback;
    }
    
    /**
     * Initialize menu bar properties
     */
    private void initializeMenuBar() {
        setBackground(PRIMARY_BLUE);
        setOpaque(true);  // Make sure background is visible
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACTIVE_BLUE));
        setPreferredSize(new Dimension(0, 45));
        
        // Force blue background for all L&F
        UIManager.put("MenuBar.background", PRIMARY_BLUE);
        UIManager.put("MenuBar.foreground", WHITE);
    }
    
    /**
     * Create all menus
     */
    private void createMenus() {
        // Home Menu
        homeMenu = createModernMenu("🏠 Beranda", "Kembali ke dashboard utama");
        homeMenu.add(createMenuItem("🏠 Dashboard", "dashboard", "Lihat ringkasan sistem"));
        homeMenu.add(createMenuItem("🔄 Refresh Data", "refresh", "Muat ulang data terbaru"));
        homeMenu.addSeparator();
        homeMenu.add(createMenuItem("📋 Panduan Cepat", "help", "Bantuan menggunakan sistem"));
        
        // Aduan Menu
        aduanMenu = createModernMenu("📋 Aduan", "Kelola aduan dan pengaduan");
        aduanMenu.add(createMenuItem("📋 Semua Aduan", "aduan-list", "Lihat daftar semua aduan"));
        aduanMenu.add(createMenuItem("➕ Aduan Baru", "aduan-new", "Tambah aduan baru"));
        aduanMenu.addSeparator();
        aduanMenu.add(createMenuItem("🆕 Status Baru", "aduan-baru", "Aduan dengan status baru"));
        aduanMenu.add(createMenuItem("⚙️ Sedang Diproses", "aduan-proses", "Aduan yang sedang ditangani"));
        aduanMenu.add(createMenuItem("✅ Selesai", "aduan-selesai", "Aduan yang telah selesai"));
        aduanMenu.addSeparator();
        aduanMenu.add(createMenuItem("🔴 Darurat", "aduan-darurat", "Aduan dengan prioritas darurat"));
        
        // Data Menu
        dataMenu = createModernMenu("📈 Data", "Kelola data sistem");
        dataMenu.add(createMenuItem("👥 Data Masyarakat", "data-masyarakat", "Kelola data masyarakat"));
        dataMenu.add(createMenuItem("👨‍💼 Data Petugas", "data-petugas", "Kelola data petugas"));
        dataMenu.addSeparator();
        dataMenu.add(createMenuItem("📂 Kategori Aduan", "data-kategori", "Kelola kategori aduan"));
        dataMenu.add(createMenuItem("🏢 Data Desa", "data-desa", "Informasi desa dan wilayah"));
        
        // Laporan Menu
        laporanMenu = createModernMenu("📉 Laporan", "Laporan dan statistik");
        laporanMenu.add(createMenuItem("📈 Dashboard Analytics", "laporan-dashboard", "Analisis dashboard"));
        laporanMenu.add(createMenuItem("📋 Laporan Aduan", "laporan-aduan", "Laporan aduan per periode"));
        laporanMenu.addSeparator();
        laporanMenu.add(createMenuItem("📄 Laporan Harian", "laporan-harian", "Laporan aktivitas harian"));
        laporanMenu.add(createMenuItem("📅 Laporan Bulanan", "laporan-bulanan", "Laporan bulanan lengkap"));
        laporanMenu.add(createMenuItem("📆 Laporan Tahunan", "laporan-tahunan", "Laporan tahunan"));
        laporanMenu.addSeparator();
        laporanMenu.add(createMenuItem("📤 Export Excel", "export-excel", "Export data ke Excel"));
        laporanMenu.add(createMenuItem("📟 Export PDF", "export-pdf", "Export laporan ke PDF"));
        
        // Tools Menu
        toolsMenu = createModernMenu("⚙️ Tools", "Alat dan utilitas");
        toolsMenu.add(createMenuItem("🔧 Pengaturan Sistem", "settings", "Konfigurasi sistem"));
        toolsMenu.add(createMenuItem("🎨 Tema Aplikasi", "theme", "Ubah tema tampilan"));
        toolsMenu.addSeparator();
        toolsMenu.add(createMenuItem("📊 Database Status", "db-status", "Status koneksi database"));
        toolsMenu.add(createMenuItem("🧠 Memory Usage", "memory", "Informasi penggunaan memori"));
        toolsMenu.addSeparator();
        toolsMenu.add(createMenuItem("📋 Log Sistem", "logs", "Lihat log sistem"));
        toolsMenu.add(createMenuItem("🔄 Backup Data", "backup", "Backup database"));
        
        // User Menu (dibuat dinamis berdasarkan user)
        createUserMenu();
        
        // Add menus to menu bar
        add(homeMenu);
        add(aduanMenu);
        add(dataMenu);
        add(laporanMenu);
        add(toolsMenu);
        
        // Add glue untuk push user menu ke kanan
        add(Box.createHorizontalGlue());
        add(userMenu);
    }
    
    /**
     * Create user menu dinamis
     */
    private void createUserMenu() {
        String userName = "User";
        if (currentUser != null) {
            userName = currentUser.getDisplayName();
        }
        
        userMenu = createModernMenu("👤 " + userName + " ▼", "Menu pengguna");
        userMenu.add(createMenuItem("👤 Profil Saya", "profile", "Lihat dan edit profil"));
        userMenu.add(createMenuItem("🔑 Ubah Password", "change-password", "Ganti password"));
        userMenu.addSeparator();
        userMenu.add(createMenuItem("ℹ️ Info Session", "session-info", "Informasi sesi login"));
        userMenu.add(createMenuItem("🔔 Notifikasi", "notifications", "Lihat notifikasi"));
        userMenu.addSeparator();
        
        // Logout dengan konfirmasi
        JMenuItem logoutItem = createMenuItem("🚺 Logout", "logout", "Keluar dari sistem");
        logoutItem.setForeground(ERROR);
        userMenu.add(logoutItem);
    }
    
    /**
     * Create modern menu dengan styling
     */
    private JMenu createModernMenu(String text, String tooltip) {
        JMenu menu = new JMenu(text);
        menu.setToolTipText(tooltip);
        
        // Styling
        menu.setForeground(WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menu.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        menu.setOpaque(false);
        
        // Hover effects
        menu.addMouseListener(new MenuHoverListener(menu));
        
        return menu;
    }
    
    /**
     * Create menu item dengan styling modern
     */
    private JMenuItem createMenuItem(String text, String action, String tooltip) {
        JMenuItem item = new JMenuItem(text);
        item.setToolTipText(tooltip);
        
        // Styling
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        item.setBackground(WHITE);
        item.setForeground(TEXT_DARK);
        
        // Action listener
        item.addActionListener(e -> {
            if (onMenuAction != null) {
                onMenuAction.accept(action);
            }
            handleMenuAction(action, text);
        });
        
        // Hover effects
        item.addMouseListener(new MenuItemHoverListener(item));
        
        return item;
    }
    
    /**
     * Setup menu styling
     */
    private void setupMenuStyling() {
        // Customize popup menu styling
        UIManager.put("PopupMenu.background", WHITE);
        UIManager.put("PopupMenu.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 221, 225), 1),
            BorderFactory.createEmptyBorder(4, 0, 4, 0)
        ));
        
        // Menu item styling
        UIManager.put("MenuItem.selectionBackground", LIGHT_GRAY);
        UIManager.put("MenuItem.selectionForeground", TEXT_DARK);
        UIManager.put("MenuItem.acceleratorForeground", TEXT_MUTED);
        UIManager.put("MenuItem.acceleratorSelectionForeground", TEXT_DARK);
    }
    
    /**
     * Handle menu actions
     */
    private void handleMenuAction(String action, String menuText) {
        switch (action) {
            case "logout":
                handleLogout();
                break;
            case "session-info":
                showSessionInfo();
                break;
            case "db-status":
                showDatabaseStatus();
                break;
            case "memory":
                showMemoryUsage();
                break;
            case "help":
                showQuickHelp();
                break;
            default:
                // Default action - show info
                JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "🟢 Menu '" + menuText + "' dipilih\n\n" +
                    "Action: " + action + "\n" +
                    "Fitur ini akan diimplementasikan sesuai kebutuhan.",
                    "MENU ACTION",
                    JOptionPane.INFORMATION_MESSAGE
                );
                break;
        }
    }
    
    /**
     * Handle logout dengan konfirmasi
     */
    private void handleLogout() {
        int response = JOptionPane.showConfirmDialog(
            SwingUtilities.getWindowAncestor(this),
            "🚺 Apakah Anda yakin ingin logout?\n\n" +
            "Semua data yang belum disimpan akan hilang.",
            "KONFIRMASI LOGOUT",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (response == JOptionPane.YES_OPTION) {
            // Logout logic
            if (sessionManager != null) {
                sessionManager.logout();
            }
            
            // Close current window and open login
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
            
            // Open login form
            SwingUtilities.invokeLater(() -> {
                try {
                    new Auth.FormLogin().setVisible(true);
                } catch (Exception e) {
                    System.err.println("Error opening login: " + e.getMessage());
                }
            });
        }
    }
    
    /**
     * Show session info
     */
    private void showSessionInfo() {
        String info = "Session info not available";
        if (sessionManager != null) {
            info = sessionManager.getSessionInfo();
        }
        
        JOptionPane.showMessageDialog(
            SwingUtilities.getWindowAncestor(this),
            info,
            "ℹ️ SESSION INFO",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Show database status
     */
    private void showDatabaseStatus() {
        String status;
        try {
            status = Utils.DatabaseConfig.getDatabaseInfo();
        } catch (Exception e) {
            status = "❌ Database connection failed: " + e.getMessage();
        }
        
        JTextArea textArea = new JTextArea(status, 15, 50);
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(
            SwingUtilities.getWindowAncestor(this),
            scrollPane,
            "📊 DATABASE STATUS",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Show memory usage
     */
    private void showMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        
        String memoryInfo = String.format(
            "🧠 MEMORY USAGE\n\n" +
            "Used Memory: %d MB\n" +
            "Free Memory: %d MB\n" +
            "Total Memory: %d MB\n" +
            "Max Memory: %d MB\n\n" +
            "Usage: %.1f%%",
            usedMemory, freeMemory, totalMemory, maxMemory,
            (double) usedMemory / totalMemory * 100
        );
        
        JOptionPane.showMessageDialog(
            SwingUtilities.getWindowAncestor(this),
            memoryInfo,
            "🧠 MEMORY USAGE",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Show quick help
     */
    private void showQuickHelp() {
        String help = 
            "📋 PANDUAN CEPAT SIPRIMA\n\n" +
            "🏠 BERANDA: Dashboard utama dengan ringkasan data\n" +
            "📋 ADUAN: Kelola aduan masyarakat\n" +
            "📈 DATA: Manajemen data sistem\n" +
            "📉 LAPORAN: Analisis dan laporan\n" +
            "⚙️ TOOLS: Pengaturan dan utilitas\n" +
            "👤 USER: Menu pengguna dan logout\n\n" +
            "📝 TIPS:\n" +
            "\u2022 Hover pada menu untuk melihat tooltip\n" +
            "\u2022 Gunakan shortcut keyboard jika tersedia\n" +
            "\u2022 Data refresh otomatis setiap 30 detik\n" +
            "\u2022 Backup data secara berkala";
            
        JTextArea textArea = new JTextArea(help, 15, 50);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JOptionPane.showMessageDialog(
            SwingUtilities.getWindowAncestor(this),
            scrollPane,
            "📋 PANDUAN CEPAT",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Update user info di menu
     */
    public void updateUserInfo(User user) {
        this.currentUser = user;
        
        // Remove old user menu
        remove(userMenu);
        
        // Create new user menu
        createUserMenu();
        
        // Add new user menu
        add(userMenu);
        
        // Refresh
        revalidate();
        repaint();
    }
    
    /**
     * Menu hover listener untuk efek hover
     */
    private class MenuHoverListener extends MouseAdapter {
        private final JMenu menu;
        
        public MenuHoverListener(JMenu menu) {
            this.menu = menu;
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            menu.setOpaque(true);
            menu.setBackground(HOVER_BLUE);
            menu.repaint();
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            menu.setOpaque(false);
            menu.setBackground(PRIMARY_BLUE);
            menu.repaint();
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            menu.setBackground(ACTIVE_BLUE);
            menu.repaint();
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            menu.setBackground(HOVER_BLUE);
            menu.repaint();
        }
    }
    
    /**
     * Menu item hover listener
     */
    private class MenuItemHoverListener extends MouseAdapter {
        private final JMenuItem item;
        
        public MenuItemHoverListener(JMenuItem item) {
            this.item = item;
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            item.setBackground(LIGHT_GRAY);
            item.setOpaque(true);
            item.repaint();
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            item.setBackground(WHITE);
            item.setOpaque(true);
            item.repaint();
        }
    }
}

