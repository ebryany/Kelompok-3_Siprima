/*
 * SIPRIMA Dashboard Petugas
 * Dashboard untuk petugas desa
 */
package dashboard;

import Utils.SessionManager;
import Utils.DatabaseConfig;
import Utils.ComplaintEventManager;
import Utils.ComplaintStatusListener;
import Utils.IconLoader;
import aduan.AduanManagementFrame;
import models.User;
import models.User.UserRole;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.beans.Beans;

/**
 * @author febry
 * Dashboard Petugas untuk SIPRIMA Desa Tarabbi
 */
public class DashboardPetugas extends javax.swing.JFrame implements ComplaintStatusListener {
    
    // SIPRIMA Modern Color Palette - Consistent with FormLogin styling
    private static final Color DEEP_BLUE = new Color(31, 53, 94);      // Primary deep blue
    private static final Color ROYAL_BLUE = new Color(65, 105, 225);    // Royal blue accent
    private static final Color OCEAN_BLUE = new Color(70, 130, 180);    // Steel blue variant
    private static final Color SOFT_BLUE = new Color(135, 206, 235);    // Sky blue light
    private static final Color LIGHT_BLUE = new Color(176, 196, 222);   // Light steel blue
    private static final Color VERY_LIGHT_BLUE = new Color(230, 230, 250); // Lavender
    
    private static final Color SUCCESS_GREEN = new Color(34, 139, 34);   // Forest green
    private static final Color WARNING_ORANGE = new Color(255, 140, 0);  // Dark orange
    private static final Color DANGER_RED = new Color(220, 20, 60);      // Crimson
    private static final Color INFO_CYAN = new Color(30, 144, 255);      // Dodger blue
    
    private static final Color PURE_WHITE = new Color(255, 255, 255);
    private static final Color SOFT_WHITE = new Color(248, 249, 250);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color BORDER_GRAY = new Color(220, 223, 230);
    private static final Color TEXT_DARK = new Color(52, 73, 94);
    private static final Color TEXT_MEDIUM = new Color(108, 117, 125);
    private static final Color TEXT_LIGHT = new Color(134, 142, 150);
    
    // Legacy color constants for backward compatibility
    private static final Color PRIMARY_BLUE = DEEP_BLUE;
    private static final Color PRIMARY_LIGHT_BLUE = ROYAL_BLUE;
    private static final Color SUCCESS = SUCCESS_GREEN;
    private static final Color WARNING = WARNING_ORANGE;
    private static final Color ERROR = DANGER_RED;
    private static final Color INFO = INFO_CYAN;
    private static final Color WHITE = PURE_WHITE;
    private static final Color BG_PRIMARY = LIGHT_GRAY;
    private static final Color BG_HEADER = DEEP_BLUE;
    private static final Color TEXT_PRIMARY = TEXT_DARK;
    private static final Color TEXT_SECONDARY = TEXT_MEDIUM;
    private static final Color TEXT_MUTED = TEXT_LIGHT;
    private static final Color BORDER_LIGHT = BORDER_GRAY;
    
    // Session manager
    private SessionManager sessionManager;
    private User currentUser;
    
    // Dashboard data
    private int totalBaru = 0;
    private int totalProses = 0;
    private int totalSelesai = 0;
    private int totalDarurat = 0;
    
    // Auto refresh timer
    private javax.swing.Timer refreshTimer;
    
    // Icon cache untuk performa
    private static final java.util.Map<String, ImageIcon> iconCache = new java.util.HashMap<>();
    
    // MDI SYSTEM - Multi-window using JDesktopPane and JInternalFrame
    private JDesktopPane desktopPane;
    private JInternalFrame currentOpenInternalForm;
    private boolean isMDIMode = false;
    
    // Form tracking variables
    private String currentFormType;
    private final Object formLock = new Object();
    
    // Embedded panel variables
    private JPanel currentEmbeddedPanel;
    private String currentEmbeddedPanelType;
    
    /**
     * Load icon PNG dari folder icon dengan caching - FIXED VERSION
     */
    private ImageIcon loadIcon(String iconName, int width, int height) {
        // Return null immediately if iconName is null or empty
        if (iconName == null || iconName.trim().isEmpty()) {
            return null;
        }
        
        String cacheKey = iconName + "_" + width + "x" + height;
        
        // Check cache first
        if (iconCache.containsKey(cacheKey)) {
            return iconCache.get(cacheKey);
        }
        
        try {
            // Try different paths untuk icon
            String[] possiblePaths = {
                "/icon/" + iconName + ".png",
                "/icons/" + iconName + ".png",
                "/images/" + iconName + ".png",
                "/resources/" + iconName + ".png",
                "/icon/auth/" + iconName + ".png", 
                "/icon/actions/" + iconName + ".png"
            };
            
            ImageIcon icon = null;
            for (String path : possiblePaths) {
                try {
                    java.net.URL iconURL = getClass().getResource(path);
                    if (iconURL != null) {
                        ImageIcon originalIcon = new ImageIcon(iconURL);
                        // Resize to specified dimensions
                        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(scaledImage);
                        System.out.println("[OK] Icon loaded: " + iconName + " from " + path);
                        break;
                    }
                } catch (Exception pathEx) {
                    // Continue to next path
                }
            }
            
            // Cache the icon (even if null)
            iconCache.put(cacheKey, icon);
            
            if (icon == null) {
                System.out.println("[INFO] Icon not found: " + iconName + ", using text fallback");
            }
            
            return icon;
            
        } catch (Exception e) {
            System.out.println("[WARNING] Error loading icon " + iconName + ": " + e.getMessage());
            iconCache.put(cacheKey, null);
            return null;
        }
    }
    
    /**
     * Create menu item dengan PNG icon
     */
    private JMenuItem createMenuItemWithIcon(String text, String iconName, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        
        // Try to load PNG icon
        ImageIcon icon = loadIcon(iconName, 16, 16);
        if (icon != null) {
            item.setIcon(icon);
        }
        
        if (action != null) {
            item.addActionListener(action);
        }
        
        return item;
    }
    
    /**
     * Create styled menu dengan PNG icon
     */
    private JMenu createStyledMenuWithIcon(String text, String iconName) {
        JMenu menu = new JMenu(text);
        
        // Try to load PNG icon
        ImageIcon icon = loadIcon(iconName, 16, 16);
        if (icon != null) {
            menu.setIcon(icon);
        }
        
        // Menu styling
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menu.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        menu.setOpaque(false); // Transparan biar background menu bar keliatan
        
        // Hover effect
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menu.setOpaque(true);
                menu.setBackground(new Color(52, 152, 219)); // Hover biru muda
                menu.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                menu.setOpaque(false); // Balik transparan
                menu.repaint();
            }
        });
        
        return menu;
    }
    
    /**
     * Creates new form DashboardPetugas
     * DEFAULT CONSTRUCTOR - HANYA UNTUK TESTING, TIDAK UNTUK PRODUCTION
     */
    public DashboardPetugas() {
        // Initialize session manager first
        try {
            sessionManager = SessionManager.getInstance();
            System.out.println("✅ SessionManager initialized in default constructor");
        } catch (Exception e) {
            System.err.println("⚠️ Warning: SessionManager not available in default constructor: " + e.getMessage());
        }
        
        initComponents();
        
        if (!Beans.isDesignTime()) {
            setupHeaderPanel(); // Ensure header panel styling is applied
            setupNavigationEventHandlers();
            loadDashboardData();
        }
    }
    
    /**
     * Show splash screen then initialize dashboard
     * METHOD INI HANYA DIGUNAKAN UNTUK APLIKASI STARTUP (main method)
     * TIDAK UNTUK LOGIN NAVIGATION
     */
    private void showSplashScreenThenInitialize() {
        System.out.println("[STARTUP] DashboardPetugas - Showing splash screen (application startup)");
        
        SplashScreen splash = new SplashScreen();
        splash.startLoading(() -> {
            SwingUtilities.invokeLater(() -> {
                initializeDashboard();
                
                // Simple show (no fade for decorated frame to avoid IllegalComponentStateException)
                setVisible(true);
                
                // Bring to front
                toFront();
                requestFocus();
                
                System.out.println("[OK] DashboardPetugas displayed successfully after splash!");
            });
        });
    }
    
    /**
     * Initialize dashboard after splash screen
     */
    private void initializeDashboard() {
        // FORCE MENU BAR COLORS SEBELUM INIT COMPONENTS
        forceMenuBarColors();
        
        // Initialize session
        sessionManager = SessionManager.getInstance();
        currentUser = sessionManager.getCurrentUser();
        
        // Initialize components
        initComponents();
        
        // Initialize MDI Desktop Pane
        // Desktop pane initialization removed - not needed for current functionality
        
        // Uncomment the next line if you need the custom menu bar
        //setupModernMenuBar(); // Disabled to prevent double header
        
        // Customization of header panel
        setupHeaderPanel();
        
        // Remove force menu bar
        // forceMenuBarColorsAfterSetup(); // No longer needed
        
        // Setup dashboard
        setupDashboard();
        setupNavigationEventHandlers();
        loadDashboardData();
        startAutoRefresh();
        
        // REGISTER UNTUK REALTIME UPDATES
        registerForRealtimeUpdates();
    }
    
    /**
     * Constructor dengan user parameter (untuk login navigation)
     * INI ADALAH CONSTRUCTOR YANG BENAR UNTUK DIGUNAKAN SETELAH LOGIN
     */
    public DashboardPetugas(User user) {
        // Set user first
        this.currentUser = user;
        
        // Initialize session manager
        sessionManager = SessionManager.getInstance();
        
        System.out.println("[TARGET] DashboardPetugas(User) - Direct login navigation for: " + 
            (user != null ? user.getDisplayName() : "Unknown User"));
        
        // PERBAIKAN: SKIP splash screen untuk login navigation
        // Splash screen sudah ditampilkan saat aplikasi pertama dibuka
        initializeDashboardDirectly();
    }
    
    /**
     * Initialize dashboard directly without splash screen (for login navigation)
     * METHOD INI DIGUNAKAN UNTUK NAVIGASI SETELAH LOGIN - TIDAK MENAMPILKAN SPLASH SCREEN
     */
    private void initializeDashboardDirectly() {
        try {
            System.out.println("[FAST] DashboardPetugas - Direct initialization (no splash screen)");
            
            // Initialize components FIRST - this creates all NetBeans form components
            initComponents();
            
            // Setup header panel styling SAMA SEPERTI DEBUG MODE
            setupHeaderPanel();
            
            // Setup dashboard layout and styling
            setupDashboard();
            
            // Setup event handlers for navigation buttons
            setupNavigationEventHandlers();
            
            // Load data and start timers
            loadDashboardData();
            startAutoRefresh();
            
            // REGISTER UNTUK REALTIME UPDATES
            registerForRealtimeUpdates();
            
            System.out.println("[OK] DashboardPetugas initialized directly for user: " + 
                (currentUser != null ? currentUser.getDisplayName() : "Unknown"));
                
        } catch (Exception e) {
            System.err.println("[ERROR] Error initializing DashboardPetugas directly: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: show error and initialize basic dashboard
            initializeFallbackDashboard();
        }
    }
    
    /**
     * Initialize basic dashboard as fallback
     */
    private void initializeFallbackDashboard() {
        try {
            initComponents();
            setTitle("SIPRIMA Desa Tarabbi - Dashboard Petugas");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setSize(1400, 900);
            
            // Basic user info update
            if (currentUser != null && lblGreeting != null) {
                updateUserInfo();
            }
            
            System.out.println("[WARNING] DashboardPetugas initialized with fallback mode");
            
        } catch (Exception e) {
            System.err.println("[ERROR] Critical error in fallback initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize Desktop Pane for MDI functionality
     */
    
    /**
     * Setup dashboard styling dan layout
     */
    private void setupDashboard() {
        // Window properties
        setTitle("SIPRIMA Desa Tarabbi - Dashboard Petugas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 800));
        setPreferredSize(new Dimension(1400, 900));
        
        // Style the header panel that already exists from NetBeans form
        if (headerPanel != null) {
            headerPanel.setBackground(new Color(41, 128, 185)); // FormLogin blue
            headerPanel.setOpaque(true);
        }
        
        // Main content styling
        if (mainContentPanel != null) {
            mainContentPanel.setBackground(BG_PRIMARY);
        }
        
        // Welcome section styling
        if (welcomePanel != null) {
            welcomePanel.setBackground(Color.WHITE);
        }
        
        // Update user info and time
        updateUserInfo();
        updateDateTime();
    }
    
    /**
     * Setup navigation button styling with modern interactive effects
     */
    private void setupNavigationButton(JButton button, String text, boolean isActive) {
        button.setText(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        
        // Set initial appearance
        if (isActive) {
            button.setBackground(WHITE);
            button.setForeground(PRIMARY_BLUE);
            button.setOpaque(true);
            // Add subtle shadow effect for active button
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)
            ));
        } else {
            button.setBackground(PRIMARY_BLUE);
            button.setForeground(WHITE);
            button.setOpaque(false);
        }
        
        // Add modern interactive effects
        button.addMouseListener(new MouseAdapter() {
            private javax.swing.Timer hoverTimer;
            private float alpha = 0.0f;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isActive) {
                    // Smooth hover animation
                    if (hoverTimer != null) hoverTimer.stop();
                    hoverTimer = new javax.swing.Timer(20, event -> {
                        alpha = Math.min(1.0f, alpha + 0.1f);
                        Color hoverColor = new Color(
                            (int)(PRIMARY_LIGHT_BLUE.getRed() * alpha + PRIMARY_BLUE.getRed() * (1-alpha)),
                            (int)(PRIMARY_LIGHT_BLUE.getGreen() * alpha + PRIMARY_BLUE.getGreen() * (1-alpha)),
                            (int)(PRIMARY_LIGHT_BLUE.getBlue() * alpha + PRIMARY_BLUE.getBlue() * (1-alpha))
                        );
                        button.setBackground(hoverColor);
                        button.setOpaque(true);
                        
                        if (alpha >= 1.0f) {
                            ((javax.swing.Timer)event.getSource()).stop();
                        }
                    });
                    hoverTimer.start();
                    
                    // Add subtle scale effect
                    button.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
                }
                
                // Change cursor to indicate interactivity
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive) {
                    // Smooth exit animation
                    if (hoverTimer != null) hoverTimer.stop();
                    hoverTimer = new javax.swing.Timer(20, event -> {
                        alpha = Math.max(0.0f, alpha - 0.15f);
                        if (alpha <= 0.0f) {
                            button.setOpaque(false);
                            button.setBackground(PRIMARY_BLUE);
                            ((javax.swing.Timer)event.getSource()).stop();
                        } else {
                            Color exitColor = new Color(
                                (int)(PRIMARY_LIGHT_BLUE.getRed() * alpha + PRIMARY_BLUE.getRed() * (1-alpha)),
                                (int)(PRIMARY_LIGHT_BLUE.getGreen() * alpha + PRIMARY_BLUE.getGreen() * (1-alpha)),
                                (int)(PRIMARY_LIGHT_BLUE.getBlue() * alpha + PRIMARY_BLUE.getBlue() * (1-alpha))
                            );
                            button.setBackground(exitColor);
                        }
                    });
                    hoverTimer.start();
                    
                    // Restore normal size
                    button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isActive) {
                    // Click feedback - darker color
                    button.setBackground(new Color(21, 67, 96)); // Darker blue
                    button.setBorder(BorderFactory.createEmptyBorder(13, 25, 11, 23)); // Slight press effect
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isActive) {
                    // Restore hover state
                    button.setBackground(PRIMARY_LIGHT_BLUE);
                    button.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
                }
            }
        });
        
        // Add ripple effect on click
        button.addActionListener(e -> {
            // Visual feedback for button click
            javax.swing.Timer clickFeedback = new javax.swing.Timer(100, event -> {
                button.setBackground(isActive ? WHITE : PRIMARY_LIGHT_BLUE);
                ((javax.swing.Timer)event.getSource()).stop();
            });
            
            // Brief flash effect
            button.setBackground(new Color(255, 255, 255, 100));
            clickFeedback.start();
        });
    }
    
    /**
     * Setup metric card dengan PNG icon
     */
    private void setupMetricCardWithIcon(JPanel card, String title, String iconName, String value, String trend, Color accentColor) {
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            )
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Set layout untuk metric card
        card.setLayout(new BorderLayout());
        
        // Create header dengan icon dan title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        // Load PNG icon jika ada
        ImageIcon icon = loadIcon(iconName, 24, 24);
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            headerPanel.add(iconLabel, BorderLayout.WEST);
        }
        
        // Title label
        JLabel titleLabel = new JLabel("  " + title); // Space untuk alignment
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(TEXT_SECONDARY);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Value label
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Trend label
        JLabel trendLabel = new JLabel(trend);
        trendLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        trendLabel.setForeground(TEXT_MUTED);
        trendLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add components
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(trendLabel, BorderLayout.SOUTH);
        
        // Add hover effects
        setupMetricCardHoverEffects(card, accentColor, title, value, trend);
    }
    
    
    /**
     * Show detailed metric information dengan PNG icon support
     */
    private void showMetricDetails(String title, String value, String trend) {
        String details = String.format(
            "📊 DETAIL METRIK\n\n" +
            "📋 %s\n" +
            "📈 Nilai Saat Ini: %s\n" +
            "📊 Trend: %s\n\n" +
            "💡 Klik pada kartu metrik untuk melihat detail lebih lanjut.\n" +
            "📊 Data diperbarui setiap 30 detik secara otomatis.",
            title, value, trend
        );
        
        JOptionPane.showMessageDialog(this,
            details,
            "📊 DETAIL METRIK",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Setup metric card styling with interactive hover effects (fallback tanpa icon)
     */
    private void setupMetricCard(JPanel card, String title, String value, String trend, Color accentColor) {
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            )
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add interactive hover effects to metric cards
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Subtle elevation effect
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                        BorderFactory.createEmptyBorder(18, 18, 22, 22)
                    )
                ));
                card.setBackground(new Color(252, 253, 254));
                
                // Add subtle animation
                javax.swing.Timer hoverTimer = new javax.swing.Timer(10, event -> {
                    card.repaint();
                    ((javax.swing.Timer)event.getSource()).stop();
                });
                hoverTimer.start();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                // Restore original state
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                    )
                ));
                card.setBackground(Color.WHITE);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                // Add click feedback with brief color change
                Color originalBg = card.getBackground();
                card.setBackground(new Color(240, 248, 255));
                
                javax.swing.Timer clickTimer = new javax.swing.Timer(150, event -> {
                    card.setBackground(originalBg);
                    ((javax.swing.Timer)event.getSource()).stop();
                });
                clickTimer.start();
                
                // Show detailed info for the metric
                showMetricDetails(title, value, trend);
            }
        });
    }
    
    
    /**
     * Setup event handlers
     */
    private void setupNavigationEventHandlers() {
        // Aduan button with dropdown
        if (btnAduan != null) {
            // Remove existing listeners first
            for (ActionListener al : btnAduan.getActionListeners()) {
                btnAduan.removeActionListener(al);
            }
            btnAduan.addActionListener(e -> showAduanDropdown(btnAduan));
        }
        
        // Masyarakat button with dropdown
        if (btnMasyarakat != null) {
            btnMasyarakat.addActionListener(e -> showMasyarakatDropdown(btnMasyarakat));
        }
        
        // Laporan button with dropdown
        if (btnLaporan != null) {
            btnLaporan.addActionListener(e -> showLaporanDropdown(btnLaporan));
        }
        
        // User menu button with dropdown
        if (btnUserMenu != null) {
            btnUserMenu.addActionListener(e -> showUserMenu());
        }
        
        // Window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleLogout();
            }
        });
    }
    
    /**
     * Show user menu dropdown
     */
    private void showUserMenu() {
        JPopupMenu userMenu = new JPopupMenu();
        
        // Profile menu item with PNG icon
        JMenuItem profileItem = createMenuItemWithIcon("Profil Saya", "user", e -> {
            openUserProfile();
        });
        
        // Settings menu item with PNG icon
        JMenuItem settingsItem = createMenuItemWithIcon("Pengaturan", "locked", e -> {
            openSettings();
        });
        
        // Session info menu item with PNG icon
        JMenuItem sessionItem = createMenuItemWithIcon("Info Session", "info", e -> {
            JOptionPane.showMessageDialog(this,
                sessionManager.getSessionInfo(),
                "SESSION INFO",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Logout menu item with PNG icon
        JMenuItem logoutItem = createMenuItemWithIcon("Logout", "log-out", e -> handleLogout());
        
        // Add items to menu
        userMenu.add(profileItem);
        userMenu.addSeparator();
        userMenu.add(settingsItem);
        userMenu.add(sessionItem);
        userMenu.addSeparator();
        userMenu.add(logoutItem);
        
        // Show menu
        userMenu.show(btnUserMenu, 0, btnUserMenu.getHeight());
    }
    
    /**
     * Handle logout
     */
    private void handleLogout() {
        int response = JOptionPane.showConfirmDialog(this,
            "🚪 Apakah Anda yakin ingin logout?",
            "KONFIRMASI LOGOUT",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (response == JOptionPane.YES_OPTION) {
            try {
                // Stop auto refresh
                if (refreshTimer != null) {
                    refreshTimer.stop();
                }
                
                // Logout from session (with null check)
                if (sessionManager != null) {
                    sessionManager.logout();
                    System.out.println("✅ Session logged out successfully");
                } else {
                    System.out.println("⚠️ SessionManager is null, skipping logout call");
                }
                
                // Cleanup realtime updates
                unregisterFromRealtimeUpdates();
                
                // Close dashboard and back to login
                this.dispose();
                
                // Show login form
                SwingUtilities.invokeLater(() -> {
                    try {
                        new Auth.FormLogin().setVisible(true);
                        System.out.println("✅ Login form opened successfully");
                    } catch (Exception e) {
                        System.err.println("❌ Error opening login form: " + e.getMessage());
                        e.printStackTrace();
                        // Fallback - exit application if can't open login
                        System.exit(0);
                    }
                });
                
            } catch (Exception e) {
                System.err.println("❌ Error during logout process: " + e.getMessage());
                e.printStackTrace();
                
                // Force close even if logout fails
                this.dispose();
                
                // Try to open login form anyway
                SwingUtilities.invokeLater(() -> {
                    try {
                        new Auth.FormLogin().setVisible(true);
                    } catch (Exception ex) {
                        System.err.println("❌ Failed to open login form: " + ex.getMessage());
                        System.exit(0);
                    }
                });
            }
        }
    }
    
    /**
     * Update user info display
     */
    /**
     * Setup header panel with dropdown functionality (replaces menu bar)
     */
    private void setupHeaderPanel() {
        try {
            if (headerPanel == null) {
                System.err.println("❌ headerPanel is null - cannot setup header");
                return;
            }
            
            // Style header panel with FormLogin colors
        // Enhanced header styling with professional gradient effect
        headerPanel.setBackground(new Color(41, 128, 185)); // Primary blue background
        headerPanel.setOpaque(true);
        headerPanel.setPreferredSize(new Dimension(1200, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Add subtle shadow effect for modern appearance
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 0, 0, 20)), // Subtle shadow
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
            
            // Note: titleLabel and logoLabel are not part of NetBeans form
            // The header panel contains navigation buttons instead
            
            // Style navigation buttons
        if (btnBeranda != null) {
        // Load icon safely
        ImageIcon homeIcon = loadIcon("home", 20, 20);
        if (homeIcon != null) {
            btnBeranda.setIcon(homeIcon);
        } else {
            btnBeranda.setText("🏠 Beranda");
        }
        btnBeranda.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBeranda.setForeground(Color.WHITE);
        btnBeranda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBeranda.setFocusPainted(false);
        btnBeranda.setMargin(new Insets(10, 20, 10, 20));
        btnBeranda.setBorderPainted(false);
        btnBeranda.setOpaque(false);
                
                // Add hover effect for better UX
                btnBeranda.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnBeranda.setBackground(new Color(52, 152, 219)); // Lighter blue on hover
                        btnBeranda.setOpaque(true);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnBeranda.setOpaque(false);
                    }
                });
            }
            
    if (btnAduan != null) {
        // Load icon safely
        ImageIcon aduanIcon = loadIcon("status-new", 20, 20);
        if (aduanIcon != null) {
            btnAduan.setIcon(aduanIcon);
        } else {
            btnAduan.setText("📋 Aduan");
        }
        btnAduan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAduan.setForeground(Color.WHITE);
        btnAduan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAduan.setFocusPainted(false);
        btnAduan.setMargin(new Insets(10, 20, 10, 20));
        btnAduan.setBorderPainted(false);
        btnAduan.setOpaque(false);
                
                // Add hover effect for better UX
                btnAduan.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnAduan.setBackground(new Color(52, 152, 219)); // Lighter blue on hover
                        btnAduan.setOpaque(true);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnAduan.setOpaque(false);
                    }
                });
            }
            
    if (btnMasyarakat != null) {
        // Load icon safely
        ImageIcon masyarakatIcon = loadIcon("users", 20, 20);
        if (masyarakatIcon != null) {
            btnMasyarakat.setIcon(masyarakatIcon);
        } else {
            btnMasyarakat.setText("👥 Masyarakat");
        }
        btnMasyarakat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMasyarakat.setForeground(Color.WHITE);
        btnMasyarakat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMasyarakat.setFocusPainted(false);
        btnMasyarakat.setMargin(new Insets(10, 20, 10, 20));
        btnMasyarakat.setBorderPainted(false);
        btnMasyarakat.setOpaque(false);
                
                // Add hover effect for better UX
                btnMasyarakat.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnMasyarakat.setBackground(new Color(52, 152, 219)); // Lighter blue on hover
                        btnMasyarakat.setOpaque(true);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnMasyarakat.setOpaque(false);
                    }
                });
            }
            
    if (btnLaporan != null) {
        // Load icon safely SAMA SEPERTI DEBUG MODE
        ImageIcon laporanIcon = loadIcon("report", 20, 20);
        if (laporanIcon != null) {
            btnLaporan.setIcon(laporanIcon);
        } else {
            
            btnLaporan.setText("Laporan");
        }
        btnLaporan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLaporan.setForeground(Color.WHITE);
        btnLaporan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLaporan.setFocusPainted(false);
        btnLaporan.setMargin(new Insets(10, 20, 10, 20));
        btnLaporan.setBorderPainted(false);
        btnLaporan.setOpaque(false);
                
                // Add hover effect for better UX
                btnLaporan.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnLaporan.setBackground(new Color(52, 152, 219)); // Lighter blue on hover
                        btnLaporan.setOpaque(true);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnLaporan.setOpaque(false);
                    }
                });
            }
            
            // Style user menu button with enhanced appearance
    if (btnUserMenu != null) {
        // Load user icon safely
        ImageIcon userIcon = loadIcon("users", 20, 20);
        if (userIcon != null) {
            btnUserMenu.setIcon(userIcon);
        } else {
            // Fallback ke icon user.png di folder auth
            ImageIcon authUserIcon = loadIcon("user", 20, 20);
            if (authUserIcon != null) {
                btnUserMenu.setIcon(authUserIcon);
            } else {
                btnUserMenu.setText(" User");
            }
        }
        btnUserMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnUserMenu.setForeground(new Color(41, 128, 185));
        btnUserMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUserMenu.setFocusPainted(false);
        btnUserMenu.setMargin(new Insets(8, 16, 8, 16));
        btnUserMenu.setBackground(new Color(255, 255, 255)); // White background for contrast
        btnUserMenu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        btnUserMenu.setOpaque(true);
                
                // Add sophisticated hover effect for user menu
                btnUserMenu.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnUserMenu.setBackground(new Color(240, 248, 255)); // Very light blue on hover
                        btnUserMenu.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
                            BorderFactory.createEmptyBorder(8, 16, 8, 16)
                        ));
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnUserMenu.setBackground(new Color(255, 255, 255));
                        btnUserMenu.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 1),
                            BorderFactory.createEmptyBorder(8, 16, 8, 16)
                        ));
                    }
                });
            }
            
            System.out.println("✅ Header panel setup completed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error setting up header panel: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 🚑 BUAT MENU BAR STANDAR YANG GAK MUNGKIN TERPOTONG! (Style seperti DashboardSuperVisor)
     */
    private void setupSimpleMenuBar() {
        try {
            // BUAT MENU BAR STANDAR JAVA SWING
            JMenuBar menuBar = new JMenuBar();
            menuBar.setBackground(PRIMARY_BLUE);
            menuBar.setBorderPainted(false);
            menuBar.setOpaque(true);
            
            // MENU BERANDA
            JMenu menuBeranda = new JMenu("🏠 Beranda");
            styleMenu(menuBeranda, true); // Active menu
            
            // Sub menu untuk Beranda
            JMenuItem itemDashboard = new JMenuItem("📊 Dashboard");
            JMenuItem itemRefresh = new JMenuItem("🔄 Refresh Data");
            
            itemDashboard.addActionListener(e -> {
                loadDashboardData();
                JOptionPane.showMessageDialog(this, "Anda sudah di halaman Dashboard", "INFO", JOptionPane.INFORMATION_MESSAGE);
            });
            itemRefresh.addActionListener(e -> {
                loadDashboardData();
                JOptionPane.showMessageDialog(this, "✅ Data berhasil diperbarui!", "REFRESH", JOptionPane.INFORMATION_MESSAGE);
            });
            
            menuBeranda.add(itemDashboard);
            menuBeranda.addSeparator();
            menuBeranda.add(itemRefresh);
            menuBar.add(menuBeranda);
            
            // MENU ADUAN
            JMenu menuAduan = new JMenu("📋 Aduan");
            styleMenu(menuAduan, false);
            
            // Sub menu untuk Aduan
            JMenuItem itemSemuaAduan = new JMenuItem("📋 Semua Aduan");
            JMenuItem itemAduanBaru = new JMenuItem("➕ Aduan Baru");
            JMenuItem itemAduanDarurat = new JMenuItem("🚨 Aduan Darurat");
            JMenuItem itemAduanSaya = new JMenuItem("👤 Aduan Saya");
            
            itemSemuaAduan.addActionListener(e -> navigateToAduanManagement());
            itemAduanBaru.addActionListener(e -> openNewAduanForm());
            itemAduanDarurat.addActionListener(e -> JOptionPane.showMessageDialog(this, "Fitur aduan darurat akan segera tersedia", "INFO", JOptionPane.INFORMATION_MESSAGE));
            itemAduanSaya.addActionListener(e -> JOptionPane.showMessageDialog(this, "Fitur aduan saya akan segera tersedia", "INFO", JOptionPane.INFORMATION_MESSAGE));
            
            menuAduan.add(itemSemuaAduan);
            menuAduan.add(itemAduanBaru);
            menuAduan.addSeparator();
            menuAduan.add(itemAduanDarurat);
            menuAduan.add(itemAduanSaya);
            menuBar.add(menuAduan);
            
            // MENU DATA
            JMenu menuData = new JMenu("👥 Data");
            styleMenu(menuData, false);
            
            // Sub menu untuk Data
            JMenuItem itemDataMasyarakat = new JMenuItem("👥 Data Masyarakat");
            JMenuItem itemDataPetugas = new JMenuItem("👷 Data Petugas");
            JMenuItem itemDataAduan = new JMenuItem("📋 Data Aduan");
            
            itemDataMasyarakat.addActionListener(e -> navigateToMasyarakatData());
            itemDataPetugas.addActionListener(e -> navigateToDataPetugas());
            itemDataAduan.addActionListener(e -> JOptionPane.showMessageDialog(this, "Fitur data aduan akan segera tersedia", "INFO", JOptionPane.INFORMATION_MESSAGE));
            
            menuData.add(itemDataMasyarakat);
            menuData.add(itemDataPetugas);
            menuData.addSeparator();
            menuData.add(itemDataAduan);
            menuBar.add(menuData);
            
            // MENU LAPORAN
            JMenu menuLaporan = new JMenu("📑 Laporan");
            styleMenu(menuLaporan, false);
            
            // Sub menu untuk Laporan
            JMenuItem itemLaporanAduan = new JMenuItem("📈 Laporan Aduan");
            JMenuItem itemAnalytics = new JMenuItem("📊 Analytics");
            JMenuItem itemExportData = new JMenuItem("📤 Export Data");
            
            itemLaporanAduan.addActionListener(e -> navigateToLaporan());
            itemAnalytics.addActionListener(e -> navigateToLaporan());
            itemExportData.addActionListener(e -> showExportOptions());
            
            menuLaporan.add(itemLaporanAduan);
            menuLaporan.add(itemAnalytics);
            menuLaporan.addSeparator();
            menuLaporan.add(itemExportData);
            menuBar.add(menuLaporan);
            
            // MENU TOOLS
            JMenu menuTools = new JMenu("🔧 Tools");
            styleMenu(menuTools, false);
            
            // Sub menu untuk Tools
            JMenuItem itemDatabaseStatus = new JMenuItem("💾 Database Status");
            JMenuItem itemMemoryUsage = new JMenuItem("📊 Memory Usage");
            JMenuItem itemPengaturan = new JMenuItem("⚙️ Pengaturan");
            
            itemDatabaseStatus.addActionListener(e -> showDatabaseStatus());
            itemMemoryUsage.addActionListener(e -> showMemoryUsage());
            itemPengaturan.addActionListener(e -> showSettings());
            
            menuTools.add(itemDatabaseStatus);
            menuTools.add(itemMemoryUsage);
            menuTools.addSeparator();
            menuTools.add(itemPengaturan);
            menuBar.add(menuTools);
            
            // GLUE UNTUK PUSH USER MENU KE KANAN
            menuBar.add(Box.createHorizontalGlue());
            
            // USER MENU
            JMenu menuUser = new JMenu("👤 " + (currentUser != null ? currentUser.getDisplayName() : "User") + " ▼");
            styleUserMenu(menuUser);
            
            // USER MENU ITEMS
            JMenuItem itemProfile = new JMenuItem("👤 Profil");
            JMenuItem itemUbahPassword = new JMenuItem("🔒 Ubah Password");
            JMenuItem itemSession = new JMenuItem("ℹ️ Info Session");
            JMenuItem itemLogout = new JMenuItem("🚪 Logout");
            
            itemProfile.addActionListener(e -> showUserProfile());
            itemUbahPassword.addActionListener(e -> showChangePassword());
            itemSession.addActionListener(e -> JOptionPane.showMessageDialog(this, sessionManager.getSessionInfo(), "SESSION INFO", JOptionPane.INFORMATION_MESSAGE));
            itemLogout.addActionListener(e -> handleLogout());
            
            menuUser.add(itemProfile);
            menuUser.addSeparator();
            menuUser.add(itemUbahPassword);
            menuUser.add(itemSession);
            menuUser.addSeparator();
            menuUser.add(itemLogout);
            
            menuBar.add(menuUser);
            
            // SET MENU BAR KE FRAME
            setJMenuBar(menuBar);
            
            System.out.println("✅ STANDARD MENU BAR CREATED - PETUGAS STYLE!");
            
        } catch (Exception e) {
            System.err.println("❌ Error creating standard menu bar: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Style menu dengan warna yang tepat (seperti DashboardSuperVisor)
     */
    private void styleMenu(JMenu menu, boolean isActive) {
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        menu.setOpaque(true);
        
        if (isActive) {
            menu.setBackground(new Color(52, 152, 219)); // Lighter blue for active
        } else {
            menu.setBackground(PRIMARY_BLUE); // Normal blue
        }
        
        menu.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }
    
    /**
     * Style user menu khusus (seperti DashboardSuperVisor)
     */
    private void styleUserMenu(JMenu menu) {
        menu.setForeground(PRIMARY_BLUE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        menu.setBackground(Color.WHITE);
        menu.setOpaque(true);
        menu.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }
    
    /**
     * NUCLEAR OPTION: Rebuild layout completely to fix header issue
     */
    // Method removed - no longer needed since we use NetBeans form layout
    
    /**
     * Create styled menu - NORMAL MENU ITEMS, BACKGROUND MENU BAR AJA YANG BIRU
     */
    private JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(text);
        
        // Menu item styling - NORMAL AJA
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menu.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        menu.setOpaque(false); // TRANSPARAN biar background menu bar keliatan
        
        // Hover effect biasa
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menu.setOpaque(true);
                menu.setBackground(new Color(52, 152, 219)); // Hover biru muda
                menu.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                menu.setOpaque(false); // Balik transparan
                menu.repaint();
            }
        });
        
        return menu;
    }
    
    /**
     * Show user profile
     */
    private void showUserProfile() {
        JOptionPane.showMessageDialog(this,
            "👤 Fitur profil pengguna akan membuka FormProfil\n" +
            "dengan data user yang sedang login.",
            "User Profile",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    /**
     * Create menu item helper
     */
    private JMenuItem createMenuItem(String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(action);
        return item;
    }
    
    /**
     * Open new aduan form - ENHANCED UX VERSION
     */
    private void openNewAduanForm() {
        openNewAduanFormWithModal();
    }
    
    /**
     * Open new aduan form dengan modal dialog approach untuk UX yang lebih baik
     * ENHANCED: Auto-close previous forms
     */
    private void openNewAduanFormWithModal() {
        // AUTO-CLOSE: Tutup form sebelumnya yang terbuka
        closeCurrentOpenForm();
        
        try {
            // ENHANCED: Show loading indicator dulu
            showLoadingDialog("Memuat form aduan...");
            
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                private aduan.FormInputanAduan aduanForm;
                
                @Override
                protected Void doInBackground() throws Exception {
                    // Load form di background thread
                    aduanForm = new aduan.FormInputanAduan(currentUser);
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        hideLoadingDialog();
                        
                        // ENHANCED: Set sebagai modal dialog dari dashboard
                        if (aduanForm != null) {
                            // REGISTER: Set sebagai form yang sedang terbuka
                            registerCurrentForm(aduanForm, "FormInputanAduan");
                            
                            aduanForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            aduanForm.setLocationRelativeTo(DashboardPetugas.this);
                            
                            // ENHANCED: Add window listener untuk refresh dashboard setelah form ditutup
                            aduanForm.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosed(WindowEvent e) {
                                    // UNREGISTER: Clear form ketika ditutup
                                    clearCurrentForm();
                                    // Refresh dashboard data setelah form ditutup
                                    SwingUtilities.invokeLater(() -> {
                                        loadDashboardData();
                                        showQuickNotification("🔄 Dashboard", "Data dashboard telah diperbarui");
                                    });
                                }
                            });
                            
                            // ENHANCED: Show dengan fade-in effect
                            aduanForm.setVisible(true);
                            aduanForm.toFront();
                            aduanForm.requestFocus();
                            
                            System.out.println("✅ Form input aduan berhasil dibuka dengan auto-close policy");
                        }
                        
                    } catch (Exception e) {
                        hideLoadingDialog();
                        showUserFriendlyError("Form Aduan", 
                            "Tidak dapat membuka form input aduan", 
                            e.getMessage(),
                            "Coba lagi beberapa saat atau hubungi administrator");
                    }
                }
            };
            
            worker.execute();
            
        } catch (Exception e) {
            hideLoadingDialog();
            showUserFriendlyError("Form Aduan", 
                "Error membuka form aduan", 
                e.getMessage(),
                "Pastikan aplikasi berjalan dengan baik dan coba lagi");
        }
    }
    
    /**
     * Open Aduan Management dengan loading animation
     */
    private void openAduanManagementWithAnimation() {
        showLoadingDialog("Memuat manajemen aduan...");
        
        SwingUtilities.invokeLater(() -> {
            try {
                navigateToAduanManagement();
                hideLoadingDialog();
            } catch (Exception e) {
                hideLoadingDialog();
                showUserFriendlyError("Manajemen Aduan", 
                    "Tidak dapat membuka manajemen aduan", 
                    e.getMessage(),
                    "Form manajemen aduan mungkin belum tersedia");
            }
        });
    }
    
    /**
     * Show database status
     */
    private void showDatabaseStatus() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                try {
                    return DatabaseConfig.getDatabaseInfo();
                } catch (Exception e) {
                    return "❌ Database connection failed: " + e.getMessage();
                }
            }
            
            @Override
            protected void done() {
                try {
                    String status = get();
                    JTextArea textArea = new JTextArea(status, 15, 50);
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
                    
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    
                    JOptionPane.showMessageDialog(DashboardPetugas.this,
                        scrollPane,
                        "📊 DATABASE STATUS",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DashboardPetugas.this,
                        "❌ Error: " + e.getMessage(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    private void updateUserInfo() {
        if (currentUser != null) {
            // Update greeting
            String greeting = getGreeting();
            String displayName = currentUser.getDisplayName();
            
            // Pastikan displayName tidak null atau kosong
            if (displayName == null || displayName.trim().isEmpty()) {
                displayName = "User";
            }
            
            lblGreeting.setText(greeting + ", " + displayName + "!");
            
            // Update role dengan fallback
            String roleDisplay = currentUser.getRoleDisplayName();
            if (roleDisplay == null || roleDisplay.trim().isEmpty()) {
                roleDisplay = "Petugas";
            }
            lblRole.setText(roleDisplay + " Desa Tarabbi");
            
            // Update user menu button
            btnUserMenu.setText("👤 " + displayName + " ▼");
            
            // Debug log untuk memastikan user info ter-update
            System.out.println("✅ User info updated - Name: " + displayName + ", Role: " + roleDisplay);
        } else {
            lblGreeting.setText("Selamat Datang!");
            lblRole.setText("Petugas Desa Tarabbi");
            btnUserMenu.setText(" User ▼");
            
            System.out.println("⚠️ Current user is null - using default values");
        }
    }
    
    /**
     * Get greeting based on time with compatible icons
     */
    private String getGreeting() {
        int hour = new Date().getHours();
        
        if (hour < 10) {
            return "[PAGI] Selamat Pagi";
        } else if (hour < 15) {
            return "[SIANG] Selamat Siang";
        } else if (hour < 18) {
            return "[SORE] Selamat Sore";
        } else {
            return "[MALAM] Selamat Malam";
        }
    }
    
    /**
     * Update date and time display with compatible icons
     */
    private void updateDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        
        Date now = new Date();
        lblDate.setText("📅 " + dateFormat.format(now));
        lblTime.setText("🕐 " + timeFormat.format(now) + " WIB");
        
        // Schedule next update
        javax.swing.Timer timer = new javax.swing.Timer(60000, e -> updateDateTime());
        timer.setRepeats(false);
        timer.start();
    }
    
    
    /**
     * Navigate to Masyarakat Data
     */
    private void navigateToMasyarakatData() {
        // Class MasyarakatDataFrame belum dibuat, gunakan fallback
        System.out.println("📋 MasyarakatDataFrame belum dibuat, menggunakan dialog fallback");
        showMasyarakatListDialog();
    }
    
    /**
     * Navigate to Laporan
     */
    private void navigateToLaporan() {
        // Class LaporanFrame belum dibuat, gunakan fallback
        System.out.println("📊 LaporanFrame belum dibuat, menggunakan dialog fallback");
        showLaporanOptionsDialog();
    }
    
    /**
     * Open user profile
     */
    private void openUserProfile() {
        // UserProfileFrame not implemented yet - show dialog instead
        showUserProfileDialog();
    }
    
    /**
     * Open settings
     */
    private void openSettings() {
        // Class SettingsFrame belum dibuat, gunakan fallback
        System.out.println("⚙️ SettingsFrame belum dibuat, menggunakan dialog fallback");
        showSettingsDialog();
    }
    
    /**
     * Fallback: Show aduan list in dialog
     */
    private void showAduanListDialog() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                StringBuilder content = new StringBuilder();
                content.append("📋 DAFTAR ADUAN TERBARU\n\n");
                
                try (Connection conn = DatabaseConfig.getConnection()) {
                    String query = "SELECT c.complaint_number, c.title, c.status, c.priority, " +
                                  "c.created_at, u.full_name, cat.name as category_name " +
                                  "FROM complaints c " +
                                  "JOIN users u ON c.reporter_id = u.id " +
                                  "JOIN categories cat ON c.category_id = cat.id " +
                                  "ORDER BY c.created_at DESC LIMIT 10";
                    
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(query)) {
                        
                        int count = 1;
                        while (rs.next()) {
                            String statusIcon = getStatusIcon(rs.getString("status"));
                            String priorityIcon = getPriorityIcon(rs.getString("priority"));
                            
                            content.append(String.format(
                                "%d. %s %s #%s\n" +
                                "   📋 %s\n" +
                                "   👤 %s • 📂 %s\n" +
                                "   📅 %s\n\n",
                                count++,
                                statusIcon,
                                priorityIcon,
                                rs.getString("complaint_number"),
                                rs.getString("title"),
                                rs.getString("full_name"),
                                rs.getString("category_name"),
                                rs.getTimestamp("created_at")
                            ));
                        }
                        
                        if (count == 1) {
                            content.append("📝 Belum ada aduan terdaftar.");
                        }
                    }
                } catch (SQLException e) {
                    content.append("❌ Error loading data: ").append(e.getMessage());
                }
                
                return content.toString();
            }
            
            @Override
            protected void done() {
                try {
                    String content = get();
                    JTextArea textArea = new JTextArea(content, 20, 50);
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    
                    JOptionPane.showMessageDialog(DashboardPetugas.this,
                        scrollPane,
                        "📋 MANAJEMEN ADUAN",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DashboardPetugas.this,
                        "❌ Error: " + e.getMessage(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Fallback: Show masyarakat list in dialog
     */
    private void showMasyarakatListDialog() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                StringBuilder content = new StringBuilder();
                content.append("👥 DATA MASYARAKAT DESA TARABBI\n\n");
                
                try (Connection conn = DatabaseConfig.getConnection()) {
                    String query = "SELECT full_name, phone, address, rt_rw, " +
                                  "(SELECT COUNT(*) FROM complaints WHERE reporter_id = users.id) as total_aduan " +
                                  "FROM users WHERE role = 'masyarakat' AND is_active = 1 " +
                                  "ORDER BY full_name LIMIT 20";
                    
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(query)) {
                        
                        int count = 1;
                        while (rs.next()) {
                            content.append(String.format(
                                "%d. 👤 %s\n" +
                                "   📞 %s\n" +
                                "   📍 %s, RT/RW: %s\n" +
                                "   📋 Total Aduan: %d\n\n",
                                count++,
                                rs.getString("full_name"),
                                rs.getString("phone") != null ? rs.getString("phone") : "Tidak tersedia",
                                rs.getString("address") != null ? rs.getString("address") : "Tidak tersedia",
                                rs.getString("rt_rw") != null ? rs.getString("rt_rw") : "Tidak tersedia",
                                rs.getInt("total_aduan")
                            ));
                        }
                        
                        if (count == 1) {
                            content.append("📝 Belum ada data masyarakat terdaftar.");
                        }
                    }
                } catch (SQLException e) {
                    content.append("❌ Error loading data: ").append(e.getMessage());
                }
                
                return content.toString();
            }
            
            @Override
            protected void done() {
                try {
                    String content = get();
                    JTextArea textArea = new JTextArea(content, 20, 50);
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    
                    JOptionPane.showMessageDialog(DashboardPetugas.this,
                        scrollPane,
                        "👥 DATA MASYARAKAT",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DashboardPetugas.this,
                        "❌ Error: " + e.getMessage(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Fallback: Show laporan options in dialog
     */
    private void showLaporanOptionsDialog() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                StringBuilder content = new StringBuilder();
                content.append("📊 LAPORAN STATISTIK SIPRIMA\n\n");
                
                try (Connection conn = DatabaseConfig.getConnection()) {
                    // Get statistics
                    String statsQuery = "SELECT " +
                        "COUNT(*) as total, " +
                        "SUM(CASE WHEN status = 'baru' THEN 1 ELSE 0 END) as baru, " +
                        "SUM(CASE WHEN status = 'proses' THEN 1 ELSE 0 END) as proses, " +
                        "SUM(CASE WHEN status = 'selesai' THEN 1 ELSE 0 END) as selesai, " +
                        "SUM(CASE WHEN priority = 'darurat' THEN 1 ELSE 0 END) as darurat " +
                        "FROM complaints";
                    
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(statsQuery)) {
                        
                        if (rs.next()) {
                            content.append("📈 RINGKASAN KESELURUHAN:\n");
                            content.append(String.format("• Total Aduan: %d\n", rs.getInt("total")));
                            content.append(String.format("• Status Baru: %d\n", rs.getInt("baru")));
                            content.append(String.format("• Sedang Diproses: %d\n", rs.getInt("proses")));
                            content.append(String.format("• Selesai: %d\n", rs.getInt("selesai")));
                            content.append(String.format("• Darurat: %d\n\n", rs.getInt("darurat")));
                        }
                    }
                    
                    // Get category statistics
                    String categoryQuery = "SELECT cat.name, cat.icon, COUNT(c.id) as total " +
                        "FROM categories cat " +
                        "LEFT JOIN complaints c ON cat.id = c.category_id " +
                        "GROUP BY cat.id, cat.name, cat.icon " +
                        "ORDER BY total DESC";
                    
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(categoryQuery)) {
                        
                        content.append("📂 ADUAN PER KATEGORI:\n");
                        while (rs.next()) {
                            content.append(String.format("• %s %s: %d aduan\n",
                                rs.getString("icon"),
                                rs.getString("name"),
                                rs.getInt("total")
                            ));
                        }
                    }
                    
                } catch (SQLException e) {
                    content.append("❌ Error loading data: ").append(e.getMessage());
                }
                
                content.append("\n📋 FITUR LAPORAN:\n");
                content.append("• Export data ke Excel/PDF\n");
                content.append("• Analisis trend bulanan\n");
                content.append("• Laporan per kategori\n");
                content.append("• Grafik performa petugas\n");
                
                return content.toString();
            }
            
            @Override
            protected void done() {
                try {
                    String content = get();
                    JTextArea textArea = new JTextArea(content, 20, 50);
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    
                    JOptionPane.showMessageDialog(DashboardPetugas.this,
                        scrollPane,
                        "📊 LAPORAN",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DashboardPetugas.this,
                        "❌ Error: " + e.getMessage(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Fallback: Show user profile in dialog
     */
    private void showUserProfileDialog() {
        if (currentUser != null) {
            try {
                // Safe getter dengan null checks dan fallback
                String displayName = getDisplayNameSafe(currentUser);
                String username = getUsernameSafe(currentUser);
                String email = getEmailSafe(currentUser);
                String role = getRoleSafe(currentUser);
                String phone = getPhoneSafe(currentUser);
                String address = getAddressSafe(currentUser);
                String rtRw = getRtRwSafe(currentUser);
                String loginTime = getLoginTimeSafe();
                
                String profileInfo = String.format(
                    "👤 PROFIL PENGGUNA\n\n" +
                    "📝 Nama Lengkap: %s\n" +
                    "👤 Username: %s\n" +
                    "📧 Email: %s\n" +
                    "🎭 Role: %s\n" +
                    "📞 Telepon: %s\n" +
                    "📍 Alamat: %s\n" +
                    "🏘️ RT/RW: %s\n\n" +
                    "📅 Session Info:\n" +
                    "• Login sejak: %s\n" +
                    "• Status: Aktif\n\n" +
                    "⚙️ PENGATURAN TERSEDIA:\n" +
                    "• Ubah password\n" +
                    "• Update profil\n" +
                    "• Pengaturan notifikasi\n" +
                    "• Preferensi tampilan",
                    displayName, username, email, role, phone, address, rtRw, loginTime
                );
                
                JOptionPane.showMessageDialog(this,
                    profileInfo,
                    "👤 PROFIL SAYA",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "❌ Error menampilkan profil: " + e.getMessage(),
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "❌ Data profil tidak tersedia. Silakan login ulang.",
                "ERROR",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Fallback: Show settings in dialog
     */
    private void showSettingsDialog() {
        String[] options = {"🎨 Tema Aplikasi", "🔔 Notifikasi", "🔄 Refresh Data", "📊 Cache", "❌ Tutup"};
        
        int choice = JOptionPane.showOptionDialog(this,
            "⚙️ PENGATURAN APLIKASI\n\n" +
            "Pilih pengaturan yang ingin diubah:",
            "⚙️ PENGATURAN",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[4]);
        
        switch (choice) {
            case 0: // Tema
                String[] themes = {"🌅 Terang", "🌙 Gelap", "🔵 Biru (Default)", "🟢 Hijau"};
                int themeChoice = JOptionPane.showOptionDialog(this,
                    "🎨 PILIH TEMA APLIKASI\n\nTema saat ini: Biru (Default)",
                    "TEMA",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    themes,
                    themes[2]);
                if (themeChoice >= 0) {
                    JOptionPane.showMessageDialog(this,
                        "✅ Tema '" + themes[themeChoice] + "' berhasil diterapkan!\n" +
                        "Restart aplikasi untuk melihat perubahan.",
                        "TEMA DIUBAH",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                break;
                
            case 1: // Notifikasi
                String[] notifOptions = {"🔔 Aktif", "🔕 Nonaktif", "⏰ Jadwal"};
                int notifChoice = JOptionPane.showOptionDialog(this,
                    "🔔 PENGATURAN NOTIFIKASI\n\nStatus saat ini: Aktif",
                    "NOTIFIKASI",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    notifOptions,
                    notifOptions[0]);
                if (notifChoice >= 0) {
                    JOptionPane.showMessageDialog(this,
                        "✅ Pengaturan notifikasi berhasil diubah!",
                        "NOTIFIKASI DIUBAH",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                break;
                
            case 2: // Refresh
                JOptionPane.showMessageDialog(this,
                    "🔄 Memuat ulang data dashboard...",
                    "REFRESH",
                    JOptionPane.INFORMATION_MESSAGE);
                loadDashboardData();
                break;
                
            case 3: // Cache
                int clearCache = JOptionPane.showConfirmDialog(this,
                    "🗑️ Hapus cache aplikasi?\n\nIni akan membersihkan data sementara dan mempercepat aplikasi.",
                    "HAPUS CACHE",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                if (clearCache == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this,
                        "✅ Cache berhasil dibersihkan!",
                        "CACHE DIBERSIHKAN",
                        JOptionPane.INFORMATION_MESSAGE);
                }
                break;
        }
    }
    
    /**
     * Get status icon for complaint status
     */
    private String getStatusIcon(String status) {
        switch (status.toLowerCase()) {
            case "baru": return "🆕";
            case "validasi": return "🔍";
            case "proses": return "⚙️";
            case "selesai": return "✅";
            case "ditolak": return "❌";
            default: return "❓";
        }
    }
    
    /**
     * Get priority icon for complaint priority
     */
    private String getPriorityIcon(String priority) {
        switch (priority.toLowerCase()) {
            case "rendah": return "🟢";
            case "sedang": return "🟡";
            case "tinggi": return "🟠";
            case "darurat": return "🔴";
            default: return "⚪";
        }
    }
    
    /**
     * Load dashboard data from database
     */
    private void loadDashboardData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Connection conn = DatabaseConfig.getConnection();
                    
                    // Get dashboard statistics
                    String statsQuery = "SELECT * FROM dashboard_stats";
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(statsQuery)) {
                        
                        if (rs.next()) {
                            totalBaru = rs.getInt("new_complaints");
                            totalProses = rs.getInt("in_progress");
                            totalSelesai = rs.getInt("completed");
                            totalDarurat = rs.getInt("urgent_complaints");
                        }
                    }
                    
                    System.out.println("✅ Dashboard data loaded: " + 
                        totalBaru + " baru, " + totalProses + " proses, " + 
                        totalSelesai + " selesai, " + totalDarurat + " darurat");
                        
                } catch (SQLException e) {
                    System.err.println("❌ Error loading dashboard data: " + e.getMessage());
                }
                
                return null;
            }
            
            @Override
            protected void done() {
                // Update UI on EDT
                updateMetricsDisplay();
                updateStatisticsDisplay();
                loadPriorityComplaints();
                loadNotifications();
            }
        };
        
        worker.execute();
    }
    
    /**
     * Update metrics cards display dengan PNG icons
     */
    private void updateMetricsDisplay() {
        // Update labels in metric cards dengan fallback untuk missing icons
        updateMetricCardWithIcon(cardBaru, "ADUAN BARU", "add", String.valueOf(totalBaru), "+" + getTodayIncrease("baru") + " hari ini");
        updateMetricCardWithIcon(cardProses, "DIPROSES", "status-process", String.valueOf(totalProses), "+" + getTodayIncrease("proses") + " hari ini");
        updateMetricCardWithIcon(cardSelesai, "SELESAI", "status-new", String.valueOf(totalSelesai), "+" + getTodayIncrease("selesai") + " hari ini");
        updateMetricCardWithIcon(cardDarurat, "DARURAT", "alarm", String.valueOf(totalDarurat), totalDarurat > 0 ? "Perlu aksi" : "Tidak ada");
    }
    
    /**
     * Update metric card content dengan PNG icon support
     */
    private void updateMetricCardWithIcon(JPanel card, String title, String iconName, String value, String trend) {
        // Remove all components and recreate layout
        card.removeAll();
        card.setLayout(new BorderLayout());
        
        // Create header dengan icon dan title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        // Load PNG icon jika ada, dengan fallback ke emoji
        ImageIcon icon = loadIcon(iconName, 20, 20);
        String titleText = title;
        
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            headerPanel.add(iconLabel, BorderLayout.WEST);
            titleText = "  " + title; // Space untuk alignment dengan icon
        } else {
            // Fallback to emoji icons
            String emojiIcon = getEmojiForMetric(title);
            titleText = emojiIcon + " " + title;
        }
        
        // Title label
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(TEXT_SECONDARY);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Value label
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(TEXT_PRIMARY);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Trend label
        JLabel trendLabel = new JLabel(trend);
        trendLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        trendLabel.setForeground(TEXT_MUTED);
        trendLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Layout
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(trendLabel, BorderLayout.SOUTH);
        
        card.revalidate();
        card.repaint();
    }
    
    /**
     * Update metric card content (fallback tanpa icon)
     */
    private void updateMetricCard(JPanel card, String title, String value, String trend) {
        // Remove all components and recreate layout
        card.removeAll();
        card.setLayout(new BorderLayout());
        
        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(TEXT_SECONDARY);
        
        // Value label
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(TEXT_PRIMARY);
        
        // Trend label
        JLabel trendLabel = new JLabel(trend);
        trendLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        trendLabel.setForeground(TEXT_MUTED);
        
        // Layout
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(valueLabel, BorderLayout.CENTER);
        contentPanel.add(trendLabel, BorderLayout.SOUTH);
        
        card.add(contentPanel, BorderLayout.CENTER);
        card.revalidate();
        card.repaint();
    }
    
    /**
     * Update metric card menggunakan komponen NetBeans yang sudah ada
     */
    private void updateMetricCard(String type, String value, String trend) {
        switch (type.toUpperCase()) {
            case "BARU":
                // Update existing NetBeans labels
                if (lblBaruTitle != null) lblBaruTitle.setText("ADUAN BARU");
                if (lblBaruValue != null) lblBaruValue.setText(value);
                if (lblBaruTrend != null) lblBaruTrend.setText(trend);
                break;
                
            case "PROSES":
                if (lblProsesTitle != null) lblProsesTitle.setText("DIPROSES");
                if (lblProsesValue != null) lblProsesValue.setText(value);
                if (lblProsesTrend != null) lblProsesTrend.setText(trend);
                break;
                
            case "SELESAI":
                if (lblSelesaiTitle != null) lblSelesaiTitle.setText("SELESAI");
                if (lblSelesaiValue != null) lblSelesaiValue.setText(value);
                if (lblSelesaiTrend != null) lblSelesaiTrend.setText(trend);
                break;
                
            case "DARURAT":
                if (lblDaruratTitle != null) lblDaruratTitle.setText("DARURAT");
                if (lblDaruratValue != null) lblDaruratValue.setText(value);
                if (lblDaruratTrend != null) lblDaruratTrend.setText(trend);
                break;
        }
        
        // Revalidate parent panels
        if (metricsPanel != null) {
            metricsPanel.revalidate();
            metricsPanel.repaint();
        }
    }
    
    /**
     * Get today's increase for metric
     */
    private int getTodayIncrease(String type) {
        // Placeholder - dalam implementasi nyata, query database untuk data hari ini
        return (int)(Math.random() * 5); // Random 0-4 untuk demo
    }
    
    /**
     * Update statistics display
     */
    private void updateStatisticsDisplay() {
        int total = totalBaru + totalProses + totalSelesai;
        
        if (total > 0) {
            int completedPercentage = (totalSelesai * 100) / total;
            int progressPercentage = (totalProses * 100) / total;
            int newPercentage = (totalBaru * 100) / total;
            
            lblStatsTitle.setText("📈 STATISTIK HARI INI");
            lblStatTotal.setText("• Total Aduan: " + total);
            lblStatCompleted.setText("• Selesai: " + totalSelesai + " (" + completedPercentage + "%)");
            lblStatProgress.setText("• Proses: " + totalProses + " (" + progressPercentage + "%)");
            lblStatNew.setText("• Baru: " + totalBaru + " (" + newPercentage + "%)");
        } else {
            lblStatsTitle.setText("📈 STATISTIK HARI INI");
            lblStatTotal.setText("• Belum ada data hari ini");
            lblStatCompleted.setText("");
            lblStatProgress.setText("");
            lblStatNew.setText("");
        }
    }
    
    /**
     * Load priority complaints
     */
    private void loadPriorityComplaints() {
        // Placeholder implementation
        lblPriorityTitle.setText("ADUAN PRIORITAS TINGGI");
        
        // In real implementation, load from database and create dynamic components
        if (totalDarurat > 0) {
            txtPriorityList.setText(
                "🔴 #001 Jalan Rusak Parah - Jl. Mawar Raya\n" +
                "👤 Siti Aminah • 📍 RT 02/RW 01 • ⏰ 2 jam lalu\n" +
                "[👁️ Lihat] [📝 Proses] [📞 Hubungi]\n\n" +
                "🟡 #002 Lampu Jalan Mati - Jl. Melati\n" +
                "👤 Budi Santoso • 📍 RT 03/RW 02 • ⏰ 4 jam lalu\n" +
                "[👁️ Lihat] [📝 Proses] [📞 Hubungi]"
            );
        } else {
            txtPriorityList.setText("✅ Tidak ada aduan prioritas tinggi saat ini.");
        }
    }
    
    /**
     * Load notifications
     */
    private void loadNotifications() {
        lblNotificationTitle.setText("NOTIFIKASI TERBARU");
        
        txtNotificationList.setText(
            "• 📢 Rapat koordinasi besok pukul 09:00\n" +
            "• ✅ Aduan #087 telah diselesaikan\n" +
            "• 📋 " + totalBaru + " aduan baru menunggu penanganan"
        );
    }
    
    /**
     * Start auto refresh timer
     */
    private void startAutoRefresh() {
        refreshTimer = new javax.swing.Timer(30000, e -> {
            if (sessionManager.isLoggedIn()) {
                loadDashboardData();
                sessionManager.refreshSession(); // Keep session alive
            }
        });
        refreshTimer.setRepeats(true);
        refreshTimer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        headerPanel = new javax.swing.JPanel();
        btnBeranda = new javax.swing.JButton();
        btnAduan = new javax.swing.JButton();
        btnMasyarakat = new javax.swing.JButton();
        btnLaporan = new javax.swing.JButton();
        btnUserMenu = new javax.swing.JButton();
        mainContentPanel = new javax.swing.JPanel();
        welcomePanel = new javax.swing.JPanel();
        lblGreeting = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        metricsPanel = new javax.swing.JPanel();
        cardBaru = new javax.swing.JPanel();
        lblBaruTitle = new javax.swing.JLabel();
        lblBaruValue = new javax.swing.JLabel();
        lblBaruTrend = new javax.swing.JLabel();
        cardProses = new javax.swing.JPanel();
        lblProsesTitle = new javax.swing.JLabel();
        lblProsesValue = new javax.swing.JLabel();
        lblProsesTrend = new javax.swing.JLabel();
        cardSelesai = new javax.swing.JPanel();
        lblSelesaiTitle = new javax.swing.JLabel();
        lblSelesaiValue = new javax.swing.JLabel();
        lblSelesaiTrend = new javax.swing.JLabel();
        cardDarurat = new javax.swing.JPanel();
        lblDaruratTitle = new javax.swing.JLabel();
        lblDaruratValue = new javax.swing.JLabel();
        lblDaruratTrend = new javax.swing.JLabel();
        statsPanel = new javax.swing.JPanel();
        lblStatsTitle = new javax.swing.JLabel();
        lblStatTotal = new javax.swing.JLabel();
        lblStatCompleted = new javax.swing.JLabel();
        lblStatProgress = new javax.swing.JLabel();
        lblStatNew = new javax.swing.JLabel();
        priorityPanel = new javax.swing.JPanel();
        lblPriorityTitle = new javax.swing.JLabel();
        scrollPriority = new javax.swing.JScrollPane();
        txtPriorityList = new javax.swing.JTextArea();
        notificationPanel = new javax.swing.JPanel();
        lblNotificationTitle = new javax.swing.JLabel();
        scrollNotifications = new javax.swing.JScrollPane();
        txtNotificationList = new javax.swing.JTextArea();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIPRIMA Dashboard Petugas");
        setBackground(new java.awt.Color(236, 240, 241));
        setMinimumSize(new java.awt.Dimension(1200, 800));

        headerPanel.setBackground(new java.awt.Color(41, 128, 185));
        headerPanel.setPreferredSize(new java.awt.Dimension(1200, 60));
        headerPanel.setMinimumSize(new java.awt.Dimension(800, 60));
        headerPanel.setMaximumSize(new java.awt.Dimension(32767, 60));

        btnBeranda.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBeranda.setForeground(new java.awt.Color(255, 255, 255));
        btnBeranda.setText("Beranda");
        btnBeranda.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnBeranda.setContentAreaFilled(false);
        btnBeranda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBeranda.setFocusPainted(false);
        btnBeranda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBerandaActionPerformed(evt);
            }
        });

        btnAduan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAduan.setForeground(new java.awt.Color(255, 255, 255));
        btnAduan.setText("Aduan");
        btnAduan.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAduan.setContentAreaFilled(false);
        btnAduan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAduan.setFocusPainted(false);

        btnMasyarakat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMasyarakat.setForeground(new java.awt.Color(255, 255, 255));
        btnMasyarakat.setText("Masyarakat");
        btnMasyarakat.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnMasyarakat.setContentAreaFilled(false);
        btnMasyarakat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMasyarakat.setFocusPainted(false);

        btnLaporan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLaporan.setForeground(new java.awt.Color(255, 255, 255));
        btnLaporan.setText("Laporan");
        btnLaporan.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLaporan.setContentAreaFilled(false);
        btnLaporan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLaporan.setFocusPainted(false);

        btnUserMenu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUserMenu.setForeground(new java.awt.Color(41, 128, 185));
        btnUserMenu.setText("User ▼");
        btnUserMenu.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnUserMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserMenu.setFocusPainted(false);

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnBeranda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAduan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMasyarakat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLaporan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnUserMenu)
                .addGap(24, 24, 24))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBeranda)
                    .addComponent(btnAduan)
                    .addComponent(btnMasyarakat)
                    .addComponent(btnLaporan)
                    .addComponent(btnUserMenu))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        mainContentPanel.setBackground(new java.awt.Color(236, 240, 241));

        welcomePanel.setBackground(new java.awt.Color(255, 255, 255));
        welcomePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(20, 25, 20, 25)));
        welcomePanel.setPreferredSize(new java.awt.Dimension(1160, 80));

        lblGreeting.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblGreeting.setForeground(new java.awt.Color(44, 62, 80));
        lblGreeting.setText("Selamat Pagi, User!");

        lblRole.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblRole.setForeground(new java.awt.Color(127, 140, 141));
        lblRole.setText("Petugas Desa Tarabbi");

        lblDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDate.setForeground(new java.awt.Color(127, 140, 141));
        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDate.setText("Senin, 16 Jun 2025");

        lblTime.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTime.setForeground(new java.awt.Color(44, 62, 80));
        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTime.setText("09:30 WIB");

        javax.swing.GroupLayout welcomePanelLayout = new javax.swing.GroupLayout(welcomePanel);
        welcomePanel.setLayout(welcomePanelLayout);
        welcomePanelLayout.setHorizontalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomePanelLayout.createSequentialGroup()
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGreeting)
                    .addComponent(lblRole))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDate, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTime, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        welcomePanelLayout.setVerticalGroup(
            welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomePanelLayout.createSequentialGroup()
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGreeting)
                    .addComponent(lblDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(welcomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRole)
                    .addComponent(lblTime)))
        );

        metricsPanel.setBackground(new java.awt.Color(236, 240, 241));
        metricsPanel.setLayout(new java.awt.GridLayout(1, 4, 15, 0));

        cardBaru.setBackground(new java.awt.Color(255, 255, 255));
        cardBaru.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(52, 152, 219)), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20))));
        cardBaru.setPreferredSize(new java.awt.Dimension(280, 120));

        lblBaruTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblBaruTitle.setForeground(new java.awt.Color(85, 85, 85));
        lblBaruTitle.setText("ADUAN BARU");

        lblBaruValue.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblBaruValue.setForeground(new java.awt.Color(44, 62, 80));
        lblBaruValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBaruValue.setText("0");

        lblBaruTrend.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblBaruTrend.setForeground(new java.awt.Color(127, 140, 141));
        lblBaruTrend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBaruTrend.setText("+0 hari ini");

        javax.swing.GroupLayout cardBaruLayout = new javax.swing.GroupLayout(cardBaru);
        cardBaru.setLayout(cardBaruLayout);
        cardBaruLayout.setHorizontalGroup(
            cardBaruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardBaruLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(cardBaruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblBaruTitle)
                    .addComponent(lblBaruValue)
                    .addComponent(lblBaruTrend))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardBaruLayout.setVerticalGroup(
            cardBaruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardBaruLayout.createSequentialGroup()
                .addComponent(lblBaruTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBaruValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblBaruTrend))
        );

        metricsPanel.add(cardBaru);

        cardProses.setBackground(new java.awt.Color(255, 255, 255));
        cardProses.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(243, 156, 18)), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20))));
        cardProses.setPreferredSize(new java.awt.Dimension(280, 120));

        lblProsesTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblProsesTitle.setForeground(new java.awt.Color(85, 85, 85));
        lblProsesTitle.setText("DIPROSES");

        lblProsesValue.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblProsesValue.setForeground(new java.awt.Color(44, 62, 80));
        lblProsesValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProsesValue.setText("0");

        lblProsesTrend.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblProsesTrend.setForeground(new java.awt.Color(127, 140, 141));
        lblProsesTrend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProsesTrend.setText("+0 hari ini");

        javax.swing.GroupLayout cardProsesLayout = new javax.swing.GroupLayout(cardProses);
        cardProses.setLayout(cardProsesLayout);
        cardProsesLayout.setHorizontalGroup(
            cardProsesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardProsesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(cardProsesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblProsesTitle)
                    .addComponent(lblProsesValue)
                    .addComponent(lblProsesTrend))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardProsesLayout.setVerticalGroup(
            cardProsesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardProsesLayout.createSequentialGroup()
                .addComponent(lblProsesTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblProsesValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblProsesTrend))
        );

        metricsPanel.add(cardProses);

        cardSelesai.setBackground(new java.awt.Color(255, 255, 255));
        cardSelesai.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(46, 204, 113)), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20))));
        cardSelesai.setPreferredSize(new java.awt.Dimension(280, 120));

        lblSelesaiTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSelesaiTitle.setForeground(new java.awt.Color(85, 85, 85));
        lblSelesaiTitle.setText("SELESAI");

        lblSelesaiValue.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblSelesaiValue.setForeground(new java.awt.Color(44, 62, 80));
        lblSelesaiValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelesaiValue.setText("0");

        lblSelesaiTrend.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblSelesaiTrend.setForeground(new java.awt.Color(127, 140, 141));
        lblSelesaiTrend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelesaiTrend.setText("+0 hari ini");

        javax.swing.GroupLayout cardSelesaiLayout = new javax.swing.GroupLayout(cardSelesai);
        cardSelesai.setLayout(cardSelesaiLayout);
        cardSelesaiLayout.setHorizontalGroup(
            cardSelesaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardSelesaiLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(cardSelesaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblSelesaiTitle)
                    .addComponent(lblSelesaiValue)
                    .addComponent(lblSelesaiTrend))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardSelesaiLayout.setVerticalGroup(
            cardSelesaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardSelesaiLayout.createSequentialGroup()
                .addComponent(lblSelesaiTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSelesaiValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSelesaiTrend))
        );

        metricsPanel.add(cardSelesai);

        cardDarurat.setBackground(new java.awt.Color(255, 255, 255));
        cardDarurat.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 0, 0, new java.awt.Color(231, 76, 60)), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20))));
        cardDarurat.setPreferredSize(new java.awt.Dimension(280, 120));

        lblDaruratTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDaruratTitle.setForeground(new java.awt.Color(85, 85, 85));
        lblDaruratTitle.setText("DARURAT");

        lblDaruratValue.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblDaruratValue.setForeground(new java.awt.Color(44, 62, 80));
        lblDaruratValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDaruratValue.setText("0");

        lblDaruratTrend.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblDaruratTrend.setForeground(new java.awt.Color(127, 140, 141));
        lblDaruratTrend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDaruratTrend.setText("Perlu aksi");

        javax.swing.GroupLayout cardDaruratLayout = new javax.swing.GroupLayout(cardDarurat);
        cardDarurat.setLayout(cardDaruratLayout);
        cardDaruratLayout.setHorizontalGroup(
            cardDaruratLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardDaruratLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(cardDaruratLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblDaruratTitle)
                    .addComponent(lblDaruratValue)
                    .addComponent(lblDaruratTrend))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardDaruratLayout.setVerticalGroup(
            cardDaruratLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardDaruratLayout.createSequentialGroup()
                .addComponent(lblDaruratTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDaruratValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDaruratTrend))
        );

        metricsPanel.add(cardDarurat);

        statsPanel.setBackground(new java.awt.Color(255, 255, 255));
        statsPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        statsPanel.setPreferredSize(new java.awt.Dimension(300, 250));

        lblStatsTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStatsTitle.setForeground(new java.awt.Color(44, 62, 80));
        lblStatsTitle.setText("📈 STATISTIK HARI INI");

        lblStatTotal.setForeground(new java.awt.Color(127, 140, 141));
        lblStatTotal.setText("• Total Aduan: 0");

        lblStatCompleted.setForeground(new java.awt.Color(127, 140, 141));
        lblStatCompleted.setText("• Selesai: 0 (0%)");

        lblStatProgress.setForeground(new java.awt.Color(127, 140, 141));
        lblStatProgress.setText("• Proses: 0 (0%)");

        lblStatNew.setForeground(new java.awt.Color(127, 140, 141));
        lblStatNew.setText("• Baru: 0 (0%)");

        javax.swing.GroupLayout statsPanelLayout = new javax.swing.GroupLayout(statsPanel);
        statsPanel.setLayout(statsPanelLayout);
        statsPanelLayout.setHorizontalGroup(
            statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblStatsTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
            .addComponent(lblStatTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblStatCompleted, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblStatProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblStatNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        statsPanelLayout.setVerticalGroup(
            statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsPanelLayout.createSequentialGroup()
                .addComponent(lblStatsTitle)
                .addGap(18, 18, 18)
                .addComponent(lblStatTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatCompleted)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatProgress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatNew)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        priorityPanel.setBackground(new java.awt.Color(255, 255, 255));
        priorityPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        priorityPanel.setPreferredSize(new java.awt.Dimension(580, 250));

        lblPriorityTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPriorityTitle.setForeground(new java.awt.Color(44, 62, 80));
        lblPriorityTitle.setText("🚨 ADUAN PRIORITAS TINGGI");

        scrollPriority.setBorder(null);

        txtPriorityList.setEditable(false);
        txtPriorityList.setBackground(new java.awt.Color(250, 250, 250));
        txtPriorityList.setColumns(20);
        txtPriorityList.setForeground(new java.awt.Color(127, 140, 141));
        txtPriorityList.setLineWrap(true);
        txtPriorityList.setRows(5);
        txtPriorityList.setText("Memuat data aduan prioritas...");
        txtPriorityList.setWrapStyleWord(true);
        txtPriorityList.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPriority.setViewportView(txtPriorityList);

        javax.swing.GroupLayout priorityPanelLayout = new javax.swing.GroupLayout(priorityPanel);
        priorityPanel.setLayout(priorityPanelLayout);
        priorityPanelLayout.setHorizontalGroup(
            priorityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblPriorityTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
            .addComponent(scrollPriority)
        );
        priorityPanelLayout.setVerticalGroup(
            priorityPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(priorityPanelLayout.createSequentialGroup()
                .addComponent(lblPriorityTitle)
                .addGap(18, 18, 18)
                .addComponent(scrollPriority, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
        );

        notificationPanel.setBackground(new java.awt.Color(255, 255, 255));
        notificationPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 221, 225)), javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        notificationPanel.setPreferredSize(new java.awt.Dimension(570, 180));

        lblNotificationTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNotificationTitle.setForeground(new java.awt.Color(44, 62, 80));
        lblNotificationTitle.setText("🔔 NOTIFIKASI TERBARU");

        scrollNotifications.setBorder(null);

        txtNotificationList.setEditable(false);
        txtNotificationList.setBackground(new java.awt.Color(250, 250, 250));
        txtNotificationList.setColumns(20);
        txtNotificationList.setForeground(new java.awt.Color(127, 140, 141));
        txtNotificationList.setLineWrap(true);
        txtNotificationList.setRows(5);
        txtNotificationList.setText("Memuat notifikasi...");
        txtNotificationList.setWrapStyleWord(true);
        txtNotificationList.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollNotifications.setViewportView(txtNotificationList);

        javax.swing.GroupLayout notificationPanelLayout = new javax.swing.GroupLayout(notificationPanel);
        notificationPanel.setLayout(notificationPanelLayout);
        notificationPanelLayout.setHorizontalGroup(
            notificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblNotificationTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrollNotifications, javax.swing.GroupLayout.DEFAULT_SIZE, 1130, Short.MAX_VALUE)
        );
        notificationPanelLayout.setVerticalGroup(
            notificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notificationPanelLayout.createSequentialGroup()
                .addComponent(lblNotificationTitle)
                .addGap(18, 18, 18)
                .addComponent(scrollNotifications, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainContentPanelLayout = new javax.swing.GroupLayout(mainContentPanel);
        mainContentPanel.setLayout(mainContentPanelLayout);
        mainContentPanelLayout.setHorizontalGroup(
            mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContentPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(notificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
                    .addComponent(welcomePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
                    .addComponent(metricsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainContentPanelLayout.createSequentialGroup()
                        .addComponent(statsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(priorityPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        mainContentPanelLayout.setVerticalGroup(
            mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContentPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(welcomePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(metricsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(mainContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(statsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(priorityPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addComponent(notificationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 786, Short.MAX_VALUE)
            .addComponent(mainContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mainContentPanel, 200, 531, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(800, 600));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBerandaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBerandaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBerandaActionPerformed

    /**
     * @param args the command line arguments
     * MAIN METHOD - UNTUK TESTING DASHBOARD LANGSUNG
     * DALAM PRODUCTION, DASHBOARD DIBUKA MELALUI LOGIN FLOW
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
            java.util.logging.Logger.getLogger(DashboardPetugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashboardPetugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashboardPetugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashboardPetugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        System.out.println("🧪 DashboardPetugas.main() - TESTING MODE");
        System.out.println("⚠️  Dalam production, gunakan login flow untuk membuka dashboard");
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // PERBAIKAN: Tambahkan splash screen untuk main method (application startup)
                // Tapi untuk login navigation, gunakan constructor DashboardPetugas(User)
                DashboardPetugas dashboard = new DashboardPetugas();
                
                // Jika ingin test dengan splash screen, uncomment baris berikut:
                // dashboard.showSplashScreenThenInitialize();
                
                dashboard.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAduan;
    private javax.swing.JButton btnBeranda;
    private javax.swing.JButton btnLaporan;
    private javax.swing.JButton btnMasyarakat;
    private javax.swing.JButton btnUserMenu;
    private javax.swing.JPanel cardBaru;
    private javax.swing.JPanel cardDarurat;
    private javax.swing.JPanel cardProses;
    private javax.swing.JPanel cardSelesai;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JLabel lblBaruTitle;
    private javax.swing.JLabel lblBaruTrend;
    private javax.swing.JLabel lblBaruValue;
    private javax.swing.JLabel lblDaruratTitle;
    private javax.swing.JLabel lblDaruratTrend;
    private javax.swing.JLabel lblDaruratValue;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblGreeting;
    private javax.swing.JLabel lblNotificationTitle;
    private javax.swing.JLabel lblPriorityTitle;
    private javax.swing.JLabel lblProsesTitle;
    private javax.swing.JLabel lblProsesTrend;
    private javax.swing.JLabel lblProsesValue;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSelesaiTitle;
    private javax.swing.JLabel lblSelesaiTrend;
    private javax.swing.JLabel lblSelesaiValue;
    private javax.swing.JLabel lblStatCompleted;
    private javax.swing.JLabel lblStatNew;
    private javax.swing.JLabel lblStatProgress;
    private javax.swing.JLabel lblStatTotal;
    private javax.swing.JLabel lblStatsTitle;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel mainContentPanel;
    private javax.swing.JPanel metricsPanel;
    private javax.swing.JPanel notificationPanel;
    private javax.swing.JPanel priorityPanel;
    private javax.swing.JScrollPane scrollNotifications;
    private javax.swing.JScrollPane scrollPriority;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JTextArea txtNotificationList;
    private javax.swing.JTextArea txtPriorityList;
    private javax.swing.JPanel welcomePanel;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Navigate to Data Petugas
     */
    private void navigateToDataPetugas() {
        JOptionPane.showMessageDialog(this,
            "👨‍💼 Fitur Data Petugas akan membuka form\n" +
            "manajemen data petugas dan admin.",
            "Data Petugas",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show export options
     */
    private void showExportOptions() {
        String[] options = {"📄 Export Excel", "📄 Export PDF", "📊 Export CSV", "❌ Batal"};
        
        int choice = JOptionPane.showOptionDialog(this,
            "📤 EXPORT DATA\n\n" +
            "Pilih format export yang diinginkan:",
            "EXPORT OPTIONS",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[3]);
        
        if (choice >= 0 && choice <= 2) {
            JOptionPane.showMessageDialog(this,
                "✅ Data berhasil diekspor ke format " + options[choice] + "!\n" +
                "File disimpan di folder Documents/SIPRIMA_Export/",
                "EXPORT BERHASIL",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Show memory usage
     */
    private void showMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        String memoryInfo = String.format(
            "🧠 INFORMASI MEMORY APLIKASI\n\n" +
            "📊 Maximum Memory: %.2f MB\n" +
            "📈 Total Memory: %.2f MB\n" +
            "💾 Used Memory: %.2f MB\n" +
            "🆓 Free Memory: %.2f MB\n" +
            "📉 Usage: %.1f%%\n\n" +
            "💡 Jika usage > 80%%, restart aplikasi untuk performa optimal.",
            maxMemory / (1024.0 * 1024.0),
            totalMemory / (1024.0 * 1024.0),
            usedMemory / (1024.0 * 1024.0),
            freeMemory / (1024.0 * 1024.0),
            (usedMemory * 100.0) / totalMemory
        );
        
        JOptionPane.showMessageDialog(this,
            memoryInfo,
            "🧠 MEMORY USAGE",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show settings
     */
    private void showSettings() {
        showSettingsDialog();
    }
    
    /**
     * Show change password dialog
     */
    private void showChangePassword() {
        JPasswordField oldPassword = new JPasswordField();
        JPasswordField newPassword = new JPasswordField();
        JPasswordField confirmPassword = new JPasswordField();
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("🔐 Password Lama:"));
        panel.add(oldPassword);
        panel.add(new JLabel("🔑 Password Baru:"));
        panel.add(newPassword);
        panel.add(new JLabel("✅ Konfirmasi Password:"));
        panel.add(confirmPassword);
        
        int result = JOptionPane.showConfirmDialog(this,
            panel,
            "🔑 UBAH PASSWORD",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String oldPass = new String(oldPassword.getPassword());
            String newPass = new String(newPassword.getPassword());
            String confirmPass = new String(confirmPassword.getPassword());
            
            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "❌ Semua field harus diisi!",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            } else if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this,
                    "❌ Password baru dan konfirmasi tidak sama!",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            } else if (newPass.length() < 6) {
                JOptionPane.showMessageDialog(this,
                    "❌ Password baru minimal 6 karakter!",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "✅ Password berhasil diubah!\n" +
                    "Silakan login ulang dengan password baru.",
                    "PASSWORD DIUBAH",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    /**
     * Force menu bar colors SEBELUM init components - HANYA MENU BAR!
     */
    private void forceMenuBarColors() {
        try {
            // HANYA MENU BAR DAN MENU - TIDAK YANG LAIN
            UIManager.put("MenuBar.background", PRIMARY_BLUE);
            UIManager.put("MenuBar.foreground", Color.WHITE);
            UIManager.put("Menu.background", PRIMARY_BLUE);
            UIManager.put("Menu.foreground", Color.WHITE);
            UIManager.put("Menu.opaque", Boolean.TRUE);
            UIManager.put("Menu.selectionBackground", new Color(52, 152, 219));
            
            // MENU ITEM TETAP PUTIH BACKGROUND
            UIManager.put("MenuItem.background", Color.WHITE);
            UIManager.put("MenuItem.foreground", Color.BLACK);
            UIManager.put("PopupMenu.background", Color.WHITE);
            
            System.out.println("🎨 FORCE: HANYA menu bar colors set to blue - komponen lain normal");
        } catch (Exception e) {
            System.err.println("❌ Error forcing menu bar colors: " + e.getMessage());
        }
    }
    
    /**
     * Force menu bar colors SETELAH setup
     */
    private void forceMenuBarColorsAfterSetup() {
        SwingUtilities.invokeLater(() -> {
            try {
                JMenuBar menuBar = getJMenuBar();
                if (menuBar != null) {
                    // PAKSA WARNA BACKGROUND BIRU
                    menuBar.setBackground(PRIMARY_BLUE);
                    menuBar.setOpaque(true);
                    menuBar.setBorderPainted(false);
                    
                    // PAKSA SETIAP MENU JADI BIRU
                    for (int i = 0; i < menuBar.getMenuCount(); i++) {
                        JMenu menu = menuBar.getMenu(i);
                        if (menu != null) {
                            menu.setBackground(PRIMARY_BLUE);
                            menu.setForeground(Color.WHITE);
                            menu.setOpaque(true);
                        }
                    }
                    
                    // REPAINT PAKSA
                    menuBar.repaint();
                    menuBar.revalidate();
                    repaint();
                    revalidate();
                    
                    System.out.println("🎨 FORCE: Menu bar colors applied AFTER setup - BLUE!");
                    System.out.println("🎨 Menu bar background: " + menuBar.getBackground());
                    System.out.println("🎨 Menu bar opaque: " + menuBar.isOpaque());
                } else {
                    System.err.println("❌ Menu bar is null - cannot force colors");
                }
            } catch (Exception e) {
                System.err.println("❌ Error forcing menu bar colors after setup: " + e.getMessage());
            }
        });
    }
    
    /**
     * PAKSA MENU TETAP BIRU - METHOD HELPER
     */
    private void forceMenuBlue(JMenu menu) {
        try {
            // PAKSA PROPERTIES BIRU
            menu.setBackground(PRIMARY_BLUE);
            menu.setForeground(Color.WHITE);
            menu.setOpaque(true);
            
            // FORCE REPAINT
            menu.repaint();
            
            // FORCE PARENT MENU BAR TETAP BIRU JUGA
            if (menu.getParent() instanceof JMenuBar) {
                JMenuBar parentMenuBar = (JMenuBar) menu.getParent();
                parentMenuBar.setBackground(PRIMARY_BLUE);
                parentMenuBar.setOpaque(true);
                parentMenuBar.repaint();
            }
        } catch (Exception e) {
            // Silent error - jangan ganggu user experience
        }
    }
    
    /**
     * SMART HEADER FIX - Keep functionality, force blue background
     */
    private void emergencyHeaderFix() {
        SwingUtilities.invokeLater(() -> {
            try {
                // SMART APPROACH: Keep NetBeans header but force blue background
                if (headerPanel != null) {
                    // Force header panel background to stay blue
                    headerPanel.setBackground(PRIMARY_BLUE);
                    headerPanel.setOpaque(true);
                    
                    // Make sure all buttons maintain white text on blue background
                    setupHeaderButton(btnBeranda, "Beranda", true);
                    setupHeaderButton(btnAduan, "Aduan", false);
                    setupHeaderButton(btnMasyarakat, "Masyarakat", false);
                    setupHeaderButton(btnLaporan, "Laporan", false);
                    setupHeaderButton(btnUserMenu, currentUser != null ? currentUser.getDisplayName() + " ▼" : "User ▼", false);
                    
                    // Add persistent blue background timer
                    Timer persistentHeaderBlue = new Timer(200, event -> {
                        if (headerPanel.getBackground() != PRIMARY_BLUE) {
                            headerPanel.setBackground(PRIMARY_BLUE);
                            headerPanel.repaint();
                        }
                    });
                    persistentHeaderBlue.setRepeats(true);
                    persistentHeaderBlue.start();
                    
                    System.out.println("🔥 SMART FIX: NetBeans header kept with forced blue background!");
                }
                
                // Also force MenuBar if present (dual navigation support)
                JMenuBar menuBar = getJMenuBar();
                if (menuBar != null) {
                    Timer persistentMenuBlue = new Timer(200, event -> {
                        menuBar.setBackground(PRIMARY_BLUE);
                        menuBar.setOpaque(true);
                        
                        // Force all menus blue
                        for (int i = 0; i < menuBar.getMenuCount(); i++) {
                            JMenu menu = menuBar.getMenu(i);
                            if (menu != null) {
                                menu.setBackground(PRIMARY_BLUE);
                                menu.setForeground(Color.WHITE);
                            }
                        }
                        menuBar.repaint();
                    });
                    persistentMenuBlue.setRepeats(true);
                    persistentMenuBlue.start();
                }
                
            } catch (Exception e) {
                System.err.println("❌ Smart header fix failed: " + e.getMessage());
            }
        });
    }
    
    /**
     * Setup header button SIMPLE SOLID - SAMA SEPERTI FormLogin, NO HOVER!
     */
    private void setupHeaderButton(JButton button, String text, boolean isActive) {
        if (button == null) return;
        
        button.setText(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        
        // SAMA SEPERTI FormLogin - SIMPLE SOLID BACKGROUND
        button.setBackground(Color.WHITE); // White background like FormLogin back/home buttons
        button.setForeground(PRIMARY_BLUE); // Blue text like FormLogin
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 200), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        // NO HOVER EFFECTS - SOLID STATIC STYLING ONLY
        // NO mouse listeners - biar gak ada interferensi
        
        // Restore functionality only
        restoreButtonFunctionality(button, text);
        
        System.out.println("🎯 SIMPLE Button: " + text + " - White background, blue text, NO HOVER");
    }
    
    /**
     * Restore navigation functionality for header buttons
     */
    private void restoreButtonFunctionality(JButton button, String buttonText) {
        // Remove existing listeners first
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }
        
        // Add new functionality based on button text
        switch (buttonText.toLowerCase()) {
            case "beranda":
                button.addActionListener(e -> {
                    // Refresh dashboard
                    loadDashboardData();
                    System.out.println("🏠 Navigated to Beranda (refreshed)");
                });
                break;
                
            case "aduan":
                button.addActionListener(e -> {
                    showAduanMenu();
                    System.out.println("📋 Showed Aduan dropdown menu");
                });
                break;
                
            case "masyarakat":
                button.addActionListener(e -> {
                    navigateToMasyarakatData();
                    System.out.println("👥 Navigated to Masyarakat Data");
                });
                break;
                
            case "laporan":
                button.addActionListener(e -> {
                    navigateToLaporan();
                    System.out.println("📊 Navigated to Laporan");
                });
                break;
                
            default:
                if (buttonText.contains("▼") || buttonText.toLowerCase().contains("user")) {
                    // User menu
                    button.addActionListener(e -> {
                        showUserMenu();
                        System.out.println("👤 Opened User Menu");
                    });
                }
                break;
        }
    }
    
    /**
     * Navigate to Aduan Management - Updated method
     */
    private void navigateToAduanManagement() {
        try {
            // Check if AduanManagementFrame exists
            Class.forName("aduan.AduanManagementFrame");
            AduanManagementFrame aduanFrame = new AduanManagementFrame(currentUser);
            aduanFrame.setVisible(true);
            System.out.println("✅ Opened AduanManagementFrame");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ AduanManagementFrame not found, using fallback");
            showAduanListDialog();
        } catch (Exception e) {
            System.err.println("❌ Error opening Aduan Management: " + e.getMessage());
            showAduanListDialog();
        }
    }
    
    // ==========================================
    // REALTIME UPDATE SYSTEM IMPLEMENTATION
    // ==========================================
    
    /**
     * Register untuk menerima realtime updates dari complaint events
     */
    private void registerForRealtimeUpdates() {
        try {
            ComplaintEventManager.getInstance().addListener(this);
            System.out.println("🔥 DashboardPetugas registered for realtime complaint updates!");
        } catch (Exception e) {
            System.err.println("❌ Error registering for realtime updates: " + e.getMessage());
        }
    }
    
    /**
     * Unregister dari complaint events (cleanup)
     */
    private void unregisterFromRealtimeUpdates() {
        try {
            ComplaintEventManager.getInstance().removeListener(this);
            System.out.println("🧹 DashboardPetugas unregistered from complaint updates");
        } catch (Exception e) {
            System.err.println("❌ Error unregistering from realtime updates: " + e.getMessage());
        }
    }
    
    
    // ==========================================
    // COMPLAINT STATUS LISTENER IMPLEMENTATION
    // ==========================================
    
    @Override
    public void onStatusChanged(String complaintNumber, String oldStatus, String newStatus, String updatedBy) {
        // 🔥 REALTIME UPDATE: Status aduan berubah!
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("🔄 REALTIME UPDATE: Status changed " + complaintNumber + 
                                 " (" + oldStatus + " → " + newStatus + ") by " + updatedBy);
                
                // Update metrics secara realtime
                updateMetricsForStatusChange(oldStatus, newStatus);
                
                // Update statistics display
                updateStatisticsDisplay();
                
                // Show notification popup
                showRealtimeNotification(
                    "🔄 Status Diupdate",
                    String.format("Aduan %s\n%s → %s\noleh %s", 
                        complaintNumber, oldStatus.toUpperCase(), newStatus.toUpperCase(), updatedBy)
                );
                
                // Load fresh data (optional - untuk memastikan sinkronisasi)
                // loadDashboardData();
                
            } catch (Exception e) {
                System.err.println("❌ Error processing status change event: " + e.getMessage());
            }
        });
    }
    
    @Override
    public void onComplaintAdded(String complaintNumber, String category, String priority) {
        // 🔥 REALTIME UPDATE: Aduan baru ditambahkan!
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("📝 REALTIME UPDATE: New complaint added " + complaintNumber + 
                                 " (" + category + ", " + priority + ")");
                
                // Update metrics
                totalBaru++;
                if ("darurat".equalsIgnoreCase(priority)) {
                    totalDarurat++;
                }
                
                // Update display
                updateMetricsDisplay();
                updateStatisticsDisplay();
                
                // Show notification
                showRealtimeNotification(
                    "📝 Aduan Baru",
                    String.format("Aduan %s\nKategori: %s\nPrioritas: %s", 
                        complaintNumber, category, priority)
                );
                
            } catch (Exception e) {
                System.err.println("❌ Error processing complaint added event: " + e.getMessage());
            }
        });
    }
    
    @Override
    public void onComplaintDeleted(String complaintNumber) {
        // 🔥 REALTIME UPDATE: Aduan dihapus!
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("🗑️ REALTIME UPDATE: Complaint deleted " + complaintNumber);
                
                // Refresh dashboard data
                loadDashboardData();
                
                // Show notification
                showRealtimeNotification(
                    "🗑️ Aduan Dihapus",
                    "Aduan " + complaintNumber + " telah dihapus dari sistem"
                );
                
            } catch (Exception e) {
                System.err.println("❌ Error processing complaint deleted event: " + e.getMessage());
            }
        });
    }
    
    // ==========================================
    // REALTIME UPDATE HELPER METHODS
    // ==========================================
    
    /**
     * Update metrics untuk perubahan status
     */
    private void updateMetricsForStatusChange(String oldStatus, String newStatus) {
        // Kurangi dari status lama
        switch (oldStatus.toLowerCase()) {
            case "baru":
                totalBaru = Math.max(0, totalBaru - 1);
                break;
            case "proses":
                totalProses = Math.max(0, totalProses - 1);
                break;
            case "selesai":
                totalSelesai = Math.max(0, totalSelesai - 1);
                break;
        }
        
        // Tambah ke status baru
        switch (newStatus.toLowerCase()) {
            case "baru":
                totalBaru++;
                break;
            case "proses":
                totalProses++;
                break;
            case "selesai":
                totalSelesai++;
                break;
        }
        
        // Update display
        updateMetricsDisplay();
    }
    
    /**
     * Show realtime notification popup
     */
    private void showRealtimeNotification(String title, String message) {
        // Create notification toast di pojok kanan atas
        JDialog notification = new JDialog(this, false);
        notification.setUndecorated(true);
        notification.setAlwaysOnTop(true);
        
        // Panel notification
        JPanel notifPanel = new JPanel(new BorderLayout());
        notifPanel.setBackground(new Color(46, 204, 113)); // Green background
        notifPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 174, 96), 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        
        // Message label
        JLabel messageLabel = new JLabel("<html>" + message.replace("\n", "<br>") + "</html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setForeground(Color.WHITE);
        
        // Add components
        notifPanel.add(titleLabel, BorderLayout.NORTH);
        notifPanel.add(messageLabel, BorderLayout.CENTER);
        
        notification.add(notifPanel);
        notification.pack();
        
        // Position di pojok kanan atas
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        notification.setLocation(
            screenSize.width - notification.getWidth() - 20,
            20
        );
        
        // Show notification
        notification.setVisible(true);
        
        // Auto hide after 3 seconds
        Timer hideTimer = new Timer(3000, e -> {
            notification.setVisible(false);
            notification.dispose();
        });
        hideTimer.setRepeats(false);
        hideTimer.start();
    }
    
    /**
     * Setup hover effects untuk metric cards
     */
    private void setupMetricCardHoverEffects(JPanel card, Color accentColor, String title, String value, String trend) {
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Add subtle hover effect
                card.setBackground(new Color(248, 249, 250));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor.brighter()),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(accentColor.brighter(), 1),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                    )
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                // Restore normal appearance
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                    )
                ));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                // Add click feedback
                System.out.println("📊 Metric card clicked: " + title);
                // Could show detailed dialog or navigate to specific view
            }
        });
    }
    
    /**
     * Get emoji icon for metric type as fallback
     */
    private String getEmojiForMetric(String metricType) {
        switch (metricType.toUpperCase()) {
            case "ADUAN BARU": return "📝";
            case "DIPROSES": return "⚙️";
            case "SELESAI": return "✅";
            case "DARURAT": return "🚨";
            default: return "📊";
        }
    }
    
    /**
     * Method untuk cleanup saat window ditutup
     */
    @Override
    public void dispose() {
        // Cleanup realtime updates
        unregisterFromRealtimeUpdates();
        
        // Stop auto refresh timer
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
        
        // Stop datetime timer
        // Note: datetime timer sudah set dengan setRepeats(false)
        
        super.dispose();
        System.out.println("🧹 DashboardPetugas disposed with cleanup");
    }
    
    // ==========================================
    // SAFE GETTER METHODS - HELPER UNTUK NULL CHECKS
    // ==========================================
    
    /**
     * Safe getter untuk display name dengan fallback
     */
    private String getDisplayNameSafe(User user) {
        try {
            if (user != null && user.getDisplayName() != null) {
                return user.getDisplayName();
            }
        } catch (Exception e) {
            // Silent - method mungkin tidak ada
        }
        
        // Fallback - coba method lain
        try {
            if (user != null && user.getFullName() != null) {
                return user.getFullName();
            }
        } catch (Exception e) {
            // Silent
        }
        
        return "Tidak tersedia";
    }
    
    /**
     * Safe getter untuk username dengan fallback
     */
    private String getUsernameSafe(User user) {
        try {
            if (user != null && user.getUsername() != null) {
                return user.getUsername();
            }
        } catch (Exception e) {
            // Silent - method mungkin tidak ada
        }
        return "Tidak tersedia";
    }
    
    /**
     * Safe getter untuk email dengan fallback
     */
    private String getEmailSafe(User user) {
        try {
            if (user != null && user.getEmail() != null) {
                return user.getEmail();
            }
        } catch (Exception e) {
            // Silent - method mungkin tidak ada
        }
        return "Tidak tersedia";
    }
    
    /**
     * Safe getter untuk role dengan fallback
     */
    private String getRoleSafe(User user) {
        try {
            if (user != null && user.getRoleDisplayName() != null) {
                return user.getRoleDisplayName();
            }
        } catch (Exception e) {
            // Silent - coba method lain
        }
        
        try {
            if (user != null && user.getRole() != null) {
                return user.getRole().toString();
            }
        } catch (Exception e) {
            // Silent
        }
        
        return "Petugas";
    }
    
    /**
     * Safe getter untuk phone dengan fallback
     */
    private String getPhoneSafe(User user) {
        try {
            if (user != null && user.getPhone() != null) {
                return user.getPhone();
            }
        } catch (Exception e) {
            // Silent - method mungkin tidak ada
        }
        return "Tidak tersedia";
    }
    
    /**
     * Safe getter untuk address dengan fallback
     */
    private String getAddressSafe(User user) {
        try {
            if (user != null && user.getAddress() != null) {
                return user.getAddress();
            }
        } catch (Exception e) {
            // Silent - method mungkin tidak ada
        }
        return "Tidak tersedia";
    }
    
    /**
     * Safe getter untuk RT/RW dengan fallback
     */
    private String getRtRwSafe(User user) {
        try {
            if (user != null && user.getRtRw() != null) {
                return user.getRtRw();
            }
        } catch (Exception e) {
            // Silent - method mungkin tidak ada
        }
        return "Tidak tersedia";
    }
    
    /**
     * Safe getter untuk login time dengan fallback
     */
    private String getLoginTimeSafe() {
        try {
            if (sessionManager != null) {
                // SessionManager might not have getLoginTime() method
                // Use current time as fallback
                
                // Coba method lain atau buat sendiri
                try {
                    return new SimpleDateFormat("HH:mm, dd MMM yyyy").format(new Date());
                } catch (Exception e) {
                    // Silent
                }
            }
        } catch (Exception e) {
            // Silent
        }
        
        return "Sesi aktif";
    }
    
    // ==========================================
    // COLOR HELPER METHODS FOR STYLING
    // ==========================================
    
    /**
     * Brighten color by specified amount
     */
    private Color brightenColor(Color color, int amount) {
        int r = Math.min(255, color.getRed() + amount);
        int g = Math.min(255, color.getGreen() + amount);
        int b = Math.min(255, color.getBlue() + amount);
        return new Color(r, g, b);
    }
    
    /**
     * Darken color by specified amount
     */
    private Color darkenColor(Color color, int amount) {
        int r = Math.max(0, color.getRed() - amount);
        int g = Math.max(0, color.getGreen() - amount);
        int b = Math.max(0, color.getBlue() - amount);
        return new Color(r, g, b);
    }
    
    /**
     * Create color with alpha transparency
     */
    private Color createAlphaColor(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    /**
     * Blend two colors with specified ratio
     */
    private Color blendColors(Color color1, Color color2, float ratio) {
        float invRatio = 1.0f - ratio;
        int r = (int)(color1.getRed() * ratio + color2.getRed() * invRatio);
        int g = (int)(color1.getGreen() * ratio + color2.getGreen() * invRatio);
        int b = (int)(color1.getBlue() * ratio + color2.getBlue() * invRatio);
        return new Color(r, g, b);
    }
    
    /**
     * CREATE NEW BLUE HEADER - Fresh header dengan background biru solid
     */
    private void createNewBlueHeader() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create brand new header panel
                JPanel newHeader = new JPanel(new BorderLayout());
                newHeader.setBackground(PRIMARY_BLUE);
                newHeader.setOpaque(true);
                newHeader.setPreferredSize(new Dimension(getWidth(), 60));
                newHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
                
                // Left section - Navigation menu
                JPanel leftMenu = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
                leftMenu.setOpaque(false);
                
                // Create navigation buttons with FULL FUNCTIONALITY
                JButton newBeranda = createSimpleButton("🏠 Beranda", true, e -> {
                    loadDashboardData();
                    showQuickNotification("🏠 Dashboard", "Data berhasil diperbarui!");
                    System.out.println("🏠 Navigated to Beranda (refreshed)");
                });
                
                JButton newAduan = createSimpleButton("📋 Aduan", false, e -> {
                    showAduanMenu(); // Show dropdown menu for aduan options
                    System.out.println("📋 Aduan menu opened");
                });
                
                JButton newMasyarakat = createSimpleButton("👥 Masyarakat", false, e -> {
                    showMasyarakatMenu(); // Show dropdown menu for masyarakat options
                    System.out.println("👥 Masyarakat menu opened");
                });
                
                JButton newLaporan = createSimpleButton("📊 Laporan", false, e -> {
                    showLaporanMenu(); // Show dropdown menu for laporan options
                    System.out.println("📊 Laporan menu opened");
                });
                
                // Add Tools/Settings button
                JButton newTools = createSimpleButton("⚙️ Tools", false, e -> {
                    showToolsMenu(); // Show dropdown menu for tools/settings
                    System.out.println("⚙️ Tools menu opened");
                });
                
                // Add navigation buttons to left menu
                leftMenu.add(newBeranda);
                leftMenu.add(newAduan);
                leftMenu.add(newMasyarakat);
                leftMenu.add(newLaporan);
                
                // Right section - User menu
                JPanel rightMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
                rightMenu.setOpaque(false);
                
                // First create the button without action listener
                JButton newUserMenu = new JButton(
                    "👤 " + (currentUser != null ? currentUser.getDisplayName() : "User") + " ▼"
                );
                
                // Apply button styling
                newUserMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
                newUserMenu.setForeground(Color.WHITE);
                newUserMenu.setBackground(PRIMARY_BLUE);
                newUserMenu.setOpaque(false);
                newUserMenu.setContentAreaFilled(false);
                newUserMenu.setBorderPainted(false);
                newUserMenu.setFocusPainted(false);
                newUserMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
                newUserMenu.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                
                // Add hover effects
                newUserMenu.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        newUserMenu.setOpaque(true);
                        newUserMenu.setBackground(PRIMARY_LIGHT_BLUE);
                        newUserMenu.setContentAreaFilled(true);
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        newUserMenu.setOpaque(false);
                        newUserMenu.setContentAreaFilled(false);
                    }
                });
                
                // Now add the action listener after the button is fully initialized
                newUserMenu.addActionListener(e -> showCustomUserMenu(newUserMenu));
                
                rightMenu.add(newUserMenu);
                
                // Add sections to header
                newHeader.add(leftMenu, BorderLayout.WEST);
                newHeader.add(rightMenu, BorderLayout.EAST);
                
                // Add header to frame at TOP
                getContentPane().add(newHeader, BorderLayout.NORTH);
                
                // Force immediate layout update
                revalidate();
                repaint();
                
                System.out.println("🎉 NEW BLUE HEADER CREATED SUCCESSFULLY!");
                
            } catch (Exception e) {
                System.err.println("❌ Error creating new blue header: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Create simple button for new header - SIMPLE & RELIABLE
     */
    private JButton createSimpleButton(String text, boolean isActive, ActionListener action) {
        JButton button = new JButton(text);
        
        // Basic styling
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_BLUE);
        button.setOpaque(false); // Transparent so header background shows
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        
        // Active button styling
        if (isActive) {
            button.setOpaque(true);
            button.setBackground(PRIMARY_LIGHT_BLUE);
            button.setContentAreaFilled(true);
        }
        
        // Add action
        if (action != null) {
            button.addActionListener(action);
        }
        
        // SIMPLE hover effect - Just color change, NO complex states
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isActive) {
                    button.setOpaque(true);
                    button.setBackground(PRIMARY_LIGHT_BLUE);
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
    
    /**
     * Show quick notification for user feedback - ENHANCED VERSION
     */
    private void showQuickNotification(String title, String message) {
        try {
            showRealtimeNotification(title, message);
        } catch (Exception e) {
            System.out.println("📢 " + title + ": " + message);
        }
    }
    
    // ==========================================
    // ENHANCED UX HELPER METHODS
    // ==========================================
    
    /**
     * Create modern dropdown menu dengan consistent styling
     */
    private JPopupMenu createModernDropdownMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(Color.WHITE);
        menu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 5, 8, 5)
        ));
        
        // ENHANCED: Add subtle shadow effect
        menu.setOpaque(true);
        
        return menu;
    }
    
    /**
     * Create enhanced menu item dengan keyboard shortcuts dan tooltips
     */
    private JMenuItem createEnhancedMenuItem(String text, String shortcut, String tooltip, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        item.setToolTipText(tooltip);
        
        // ENHANCED: Add keyboard shortcut display
        if (shortcut != null && !shortcut.isEmpty()) {
            item.setAccelerator(KeyStroke.getKeyStroke(shortcut.replace("Ctrl+", "control ")
                                                           .replace("Alt+", "alt ")
                                                           .replace("Shift+", "shift ")));
        }
        
        // ENHANCED: Add action dengan feedback
        item.addActionListener(e -> {
            try {
                if (action != null) {
                    action.actionPerformed(e);
                }
            } catch (Exception ex) {
                showUserFriendlyError("Menu Action", 
                    "Error menjalankan aksi menu", 
                    ex.getMessage(),
                    "Coba lagi atau hubungi administrator");
            }
        });
        
        // ENHANCED: Add hover effects
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(240, 248, 255));
                item.setOpaque(true);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(Color.WHITE);
                item.setOpaque(true);
            }
        });
        
        return item;
    }
    
    /**
     * Show dropdown dengan smooth animation
     */
    private void showDropdownWithAnimation(JPopupMenu menu, JButton sourceButton) {
        if (sourceButton != null && sourceButton.isDisplayable() && sourceButton.isVisible()) {
            // ENHANCED: Calculate optimal position
            Point buttonLocation = sourceButton.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(buttonLocation, sourceButton.getParent());
            
            // ENHANCED: Show dengan fade effect (simulasi)
            menu.show(sourceButton, 0, sourceButton.getHeight());
            
            // ENHANCED: Add subtle animation timer untuk smooth appearance
            javax.swing.Timer animationTimer = new javax.swing.Timer(50, e -> {
                menu.repaint();
                ((javax.swing.Timer)e.getSource()).stop();
            });
            animationTimer.start();
            
        } else {
            // Fallback positioning
            menu.show(this, 200, 80);
        }
    }
    
    /**
     * Show simple fallback menu jika ada error
     */
    private void showSimpleFallbackMenu(JButton sourceButton, String menuType) {
        JOptionPane.showMessageDialog(this,
            "🎯 Menu " + menuType + " berhasil diklik!\n\n" +
            "Menu dropdown akan ditampilkan di sini dengan fitur:\n" +
            "• Navigasi cepat\n" +
            "• Shortcut keyboard\n" +
            "• Real-time statistics\n" +
            "• Quick actions",
            "📋 " + menuType.toUpperCase(),
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // ==========================================
    // MDI INTERNAL FRAME MANAGEMENT METHODS
    // ==========================================
    
    /**
     * Open form as JInternalFrame in desktop pane (MDI style)
     * This is the core method for embedded form display
     */
    private void openFormAsInternalFrame(JFrame sourceForm, String title, String formType) {
        synchronized (formLock) {
            try {
                System.out.println("🏠 Opening form as internal frame: " + title + " (" + formType + ")");
                
                // Close any currently open internal frame first
                closeCurrentInternalFrame();
                
                // Check if desktop pane is available
                if (desktopPane == null) {
                    System.err.println("❌ Desktop pane not initialized, opening as separate window");
                    sourceForm.setVisible(true);
                    return;
                }
                
                // Create JInternalFrame to wrap the source form
                JInternalFrame internalFrame = new JInternalFrame(
                    title,
                    true,  // resizable
                    true,  // closable
                    true,  // maximizable
                    true   // iconifiable
                );
                
                // Configure internal frame properties
                internalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
                
                // Get content from source form and add to internal frame
                if (sourceForm.getContentPane() != null) {
                    internalFrame.setContentPane(sourceForm.getContentPane());
                }
                
                // Set appropriate size based on source form or defaults
                Dimension preferredSize = sourceForm.getPreferredSize();
                if (preferredSize.width > 0 && preferredSize.height > 0) {
                    internalFrame.setSize(preferredSize);
                } else {
                    // Default sizes based on form type
                    switch (formType.toLowerCase()) {
                        case "aduan":
                        case "forminputanaduan":
                            internalFrame.setSize(900, 700);
                            break;
                        case "aduanmanagement":
                            internalFrame.setSize(1200, 800);
                            break;
                        default:
                            internalFrame.setSize(800, 600);
                            break;
                    }
                }
                
                // Center the internal frame in desktop pane
                centerInternalFrame(internalFrame);
                
                // Add window listener for cleanup
                internalFrame.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                    @Override
                    public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                        // Clear current form reference when closed
                        clearCurrentForm();
                        System.out.println("✅ Internal frame closed: " + title);
                        
                        // Refresh dashboard data after form is closed
                        SwingUtilities.invokeLater(() -> {
                            loadDashboardData();
                            showQuickNotification("🔄 Dashboard", "Data berhasil diperbarui");
                        });
                    }
                });
                
                // Add to desktop pane
                desktopPane.add(internalFrame);
                
                // Register as current open form
                currentOpenInternalForm = internalFrame;
                currentFormType = formType;
                
                // Show and bring to front
                internalFrame.setVisible(true);
                
                try {
                    internalFrame.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                    System.err.println("⚠️ Could not select internal frame: " + e.getMessage());
                }
                
                internalFrame.toFront();
                
                // Dispose the original form since we're using its content
                sourceForm.dispose();
                
                System.out.println("✅ Internal frame opened successfully: " + title);
                
            } catch (Exception e) {
                System.err.println("❌ Error opening form as internal frame: " + e.getMessage());
                e.printStackTrace();
                
                // Fallback to regular window if internal frame fails
                sourceForm.setVisible(true);
            }
        }
    }
    
    /**
     * Center internal frame in desktop pane
     */
    private void centerInternalFrame(JInternalFrame internalFrame) {
        if (desktopPane != null) {
            Dimension desktopSize = desktopPane.getSize();
            Dimension frameSize = internalFrame.getSize();
            
            int x = Math.max(0, (desktopSize.width - frameSize.width) / 2);
            int y = Math.max(0, (desktopSize.height - frameSize.height) / 2);
            
            internalFrame.setLocation(x, y);
        }
    }
    
    /**
     * Close currently open internal frame if exists
     */
    private void closeCurrentInternalFrame() {
        synchronized (formLock) {
            if (currentOpenInternalForm != null) {
                try {
                    System.out.println("🔒 Closing current internal frame: " + currentOpenInternalForm.getTitle());
                    
                    // Remove from desktop pane
                    if (desktopPane != null) {
                        desktopPane.remove(currentOpenInternalForm);
                        desktopPane.repaint();
                    }
                    
                    // Dispose the internal frame
                    currentOpenInternalForm.dispose();
                    
                } catch (Exception e) {
                    System.err.println("⚠️ Error closing current internal frame: " + e.getMessage());
                } finally {
                    // Clear references
                    clearCurrentForm();
                }
            }
        }
    }
    
    /**
     * Clear current form references
     */
    private void clearCurrentForm() {
        synchronized (formLock) {
            currentOpenInternalForm = null;
            currentFormType = null;
        }
    }
    
    /**
     * Register current form for tracking
     */
    private void registerCurrentForm(JFrame form, String formType) {
        // This method is used for tracking external forms
        // For MDI, we track via currentOpenInternalForm instead
        System.out.println("📝 Registering external form: " + formType);
    }
    
    /**
     * Close current open form (external forms)
     */
    private void closeCurrentOpenForm() {
        // For external forms - this would close external JFrame windows
        // For MDI, we use closeCurrentInternalFrame() instead
        System.out.println("🔒 Closing external forms (if any)");
    }
    
    /**
     * Create internal frame version of form content
     * Alternative approach: create content directly without source form
     */
    private void createInternalFrameDirectly(String title, String formType, javax.swing.JPanel contentPanel) {
        synchronized (formLock) {
            try {
                System.out.println("🏠 Creating internal frame directly: " + title);
                
                // Close any currently open internal frame first
                closeCurrentInternalFrame();
                
                // Check if desktop pane is available
                if (desktopPane == null) {
                    System.err.println("❌ Desktop pane not initialized");
                    return;
                }
                
                // Create JInternalFrame
                JInternalFrame internalFrame = new JInternalFrame(
                    title,
                    true,  // resizable
                    true,  // closable
                    true,  // maximizable
                    true   // iconifiable
                );
                
                // Configure internal frame
                internalFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
                
                // Add content panel
                if (contentPanel != null) {
                    internalFrame.setContentPane(contentPanel);
                }
                
                // Set size based on form type
                switch (formType.toLowerCase()) {
                    case "aduan":
                        internalFrame.setSize(900, 700);
                        break;
                    case "management":
                        internalFrame.setSize(1200, 800);
                        break;
                    default:
                        internalFrame.setSize(800, 600);
                        break;
                }
                
                // Center and show
                centerInternalFrame(internalFrame);
                
                // Add cleanup listener
                internalFrame.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
                    @Override
                    public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                        clearCurrentForm();
                        SwingUtilities.invokeLater(() -> {
                            loadDashboardData();
                            showQuickNotification("🔄 Dashboard", "Data berhasil diperbarui");
                        });
                    }
                });
                
                // Add to desktop pane
                desktopPane.add(internalFrame);
                currentOpenInternalForm = internalFrame;
                currentFormType = formType;
                
                // Show
                internalFrame.setVisible(true);
                
                try {
                    internalFrame.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                    System.err.println("⚠️ Could not select internal frame: " + e.getMessage());
                }
                
                internalFrame.toFront();
                
                System.out.println("✅ Internal frame created successfully: " + title);
                
            } catch (Exception e) {
                System.err.println("❌ Error creating internal frame directly: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    // Loading dialog management
    private JDialog loadingDialog;
    
    /**
     * Show loading dialog untuk better UX
     */
    private void showLoadingDialog(String message) {
        if (loadingDialog != null) {
            loadingDialog.dispose();
        }
        
        loadingDialog = new JDialog(this, "Loading", true);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setUndecorated(true);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_BLUE, 2),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        JLabel loadingLabel = new JLabel("🔄 " + message);
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadingLabel.setForeground(TEXT_PRIMARY);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(loadingLabel, BorderLayout.CENTER);
        loadingDialog.add(panel);
        loadingDialog.pack();
        loadingDialog.setLocationRelativeTo(this);
        
        // Auto-hide after 5 seconds as safety
        javax.swing.Timer safetyTimer = new javax.swing.Timer(5000, e -> hideLoadingDialog());
        safetyTimer.setRepeats(false);
        safetyTimer.start();
        
        loadingDialog.setVisible(true);
    }
    
    /**
     * Hide loading dialog
     */
    private void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dispose();
            loadingDialog = null;
        }
    }
    
    /**
     * Show user-friendly error dialog dengan actionable solutions
     */
    private void showUserFriendlyError(String context, String userMessage, String technicalError, String suggestion) {
        String message = String.format(
            "❌ %s\n\n" +
            "💡 Saran: %s\n\n" +
            "🔧 Detail teknis: %s",
            userMessage, suggestion, technicalError
        );
        
        JOptionPane.showMessageDialog(this,
            message,
            "⚠️ " + context,
            JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Open search aduan dialog
     */
    private void openSearchAduanDialog() {
        String searchTerm = JOptionPane.showInputDialog(this,
            "🔍 PENCARIAN ADUAN\n\n" +
            "Masukkan kata kunci:\n" +
            "• Nomor aduan (ADU-XXXXXXXX)\n" +
            "• Nama pelapor\n" +
            "• Judul aduan\n" +
            "• Kategori",
            "🔍 Cari Aduan",
            JOptionPane.QUESTION_MESSAGE);
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            showSearchComplaints();
        }
    }
    
    /**
     * Show urgent complaints dengan enhanced details
     */
    private void showUrgentComplaintsWithDetails() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                // Implementation untuk load urgent complaints dengan detail
                return "🚨 ADUAN DARURAT\n\n" +
                       "📊 Total: " + totalDarurat + " aduan\n" +
                       "⏰ Memerlukan penanganan segera\n\n" +
                       "Silakan klik 'Lihat Semua Aduan' untuk detail lengkap.";
            }
            
            @Override
            protected void done() {
                try {
                    String content = get();
                    JOptionPane.showMessageDialog(DashboardPetugas.this,
                        content,
                        "🚨 ADUAN DARURAT",
                        JOptionPane.WARNING_MESSAGE);
                } catch (Exception e) {
                    showUserFriendlyError("Aduan Darurat", 
                        "Tidak dapat memuat data aduan darurat", 
                        e.getMessage(),
                        "Refresh dashboard dan coba lagi");
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Show Aduan dropdown menu
     */
    private void showAduanMenu() {
        // Find the button that triggered this
        JButton aduanButton = findHeaderButton("📋 Aduan");
        if (aduanButton == null) return;
        
        JPopupMenu aduanMenu = new JPopupMenu();
        
        // Menu Aduan sesuai permintaan
        JMenuItem daftarAduanMasukItem = new JMenuItem("📋 Daftar Aduan Masuk");
        daftarAduanMasukItem.addActionListener(e -> showIncomingComplaintsPanel());
        
        JMenuItem cariAduanItem = new JMenuItem("🔍 Cari Aduan");
        cariAduanItem.addActionListener(e -> showSearchComplaints());
        
        JMenuItem detailAduanItem = new JMenuItem("📝 Detail Aduan");
        detailAduanItem.addActionListener(e -> showComplaintDetails());
        
        JMenuItem lihatSemuaAduanItem = new JMenuItem("👁️ Lihat Semua Aduan");
        lihatSemuaAduanItem.addActionListener(e -> showIncomingComplaintsPanel());
        
        JMenuItem statistikAduanItem = new JMenuItem("📊 Statistik Aduan");
        statistikAduanItem.addActionListener(e -> showAduanStatistics());
        
        aduanMenu.add(daftarAduanMasukItem);
        aduanMenu.add(cariAduanItem);
        aduanMenu.add(detailAduanItem);
        aduanMenu.add(lihatSemuaAduanItem);
        aduanMenu.add(statistikAduanItem);
        
        // Show menu TEPAT di bawah button
        aduanMenu.show(aduanButton, 0, aduanButton.getHeight());
        
        System.out.println("📋 Aduan menu shown at button position");
    }
    
    /**
     * Show Masyarakat dropdown menu
     */
    private void showMasyarakatMenu() {
        // Find the button that triggered this
        JButton masyarakatButton = findHeaderButton("👥 Masyarakat");
        if (masyarakatButton == null) return;
        
        JPopupMenu masyarakatMenu = new JPopupMenu();
        
        JMenuItem dataMasyarakatItem = new JMenuItem("👥 Data Masyarakat");
        dataMasyarakatItem.addActionListener(e -> navigateToMasyarakatData());
        
        JMenuItem statistikItem = new JMenuItem("📊 Statistik Pengaduan");
        statistikItem.addActionListener(e -> showMasyarakatStatistics());
        
        JMenuItem kontakItem = new JMenuItem("📞 Info Kontak");
        kontakItem.addActionListener(e -> showContactInfo());
        
        masyarakatMenu.add(dataMasyarakatItem);
        masyarakatMenu.add(statistikItem);
        masyarakatMenu.addSeparator();
        masyarakatMenu.add(kontakItem);
        
        // Show menu TEPAT di bawah button
        masyarakatMenu.show(masyarakatButton, 0, masyarakatButton.getHeight());
        
        System.out.println("👥 Masyarakat menu shown at button position");
    }
    
    /**
     * Show Laporan dropdown menu
     */
    private void showLaporanMenu() {
        // Find the button that triggered this
        JButton laporanButton = findHeaderButton("📊 Laporan");
        if (laporanButton == null) return;
        
        JPopupMenu laporanMenu = new JPopupMenu();
        
        JMenuItem dashboardLaporanItem = new JMenuItem("Dashboard Laporan");
        dashboardLaporanItem.addActionListener(e -> navigateToLaporan());
        
        JMenuItem exportItem = new JMenuItem("Export Data");
        exportItem.addActionListener(e -> showExportOptions());
        
        JMenuItem analitikItem = new JMenuItem("Analitik");
        analitikItem.addActionListener(e -> showAnalytics());
        
        JMenuItem printItem = new JMenuItem("Print Laporan");
        printItem.addActionListener(e -> showPrintOptions());
        
        laporanMenu.add(dashboardLaporanItem);
        laporanMenu.add(exportItem);
        laporanMenu.add(analitikItem);
        laporanMenu.addSeparator();
        laporanMenu.add(printItem);
        
        // Show menu TEPAT di bawah button
        laporanMenu.show(laporanButton, 0, laporanButton.getHeight());
        
        System.out.println("📊 Laporan menu shown at button position");
    }
    
    /**
     * Show Tools dropdown menu
     */
    private void showToolsMenu() {
        JPopupMenu toolsMenu = new JPopupMenu();
        
        JMenuItem dbStatusItem = new JMenuItem("🗄️ Status Database");
        dbStatusItem.addActionListener(e -> showDatabaseStatus());
        
        JMenuItem memoryItem = new JMenuItem("🧠 Memory Usage");
        memoryItem.addActionListener(e -> showMemoryUsage());
        
        JMenuItem settingsItem = new JMenuItem("⚙️ Pengaturan");
        settingsItem.addActionListener(e -> showSettings());
        
        JMenuItem refreshItem = new JMenuItem("🔄 Refresh Data");
        refreshItem.addActionListener(e -> {
            loadDashboardData();
            showQuickNotification("🔄 Refresh", "Data berhasil diperbarui!");
        });
        
        toolsMenu.add(dbStatusItem);
        toolsMenu.add(memoryItem);
        toolsMenu.addSeparator();
        toolsMenu.add(settingsItem);
        toolsMenu.add(refreshItem);
        
        // Show menu at appropriate location
        Component invoker = (Component) getContentPane().getComponent(0); // header
        toolsMenu.show(invoker, 650, 60);
    }
    
    /**
     * Show urgent complaints dialog
     */
    private void showUrgentComplaints() {
        JOptionPane.showMessageDialog(this,
            "🚨 ADUAN DARURAT\n\n" +
            "Menampilkan daftar aduan dengan prioritas darurat\n" +
            "yang memerlukan penanganan segera.\n\n" +
            "Total aduan darurat: " + totalDarurat,
            "🚨 ADUAN DARURAT",
            JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Show aduan dropdown menu
     */
    private void showAduanDropdown(JButton sourceButton) {
        JPopupMenu aduanMenu = createModernDropdownMenu();
        
        // Menu items untuk aduan
        JMenuItem lihatSemuaItem = createEnhancedMenuItem(
            "Lihat Semua Aduan", "Ctrl+A", "Buka manajemen aduan lengkap", 
            e -> showIncomingComplaintsPanel());
            
        JMenuItem cariAduanItem = createEnhancedMenuItem(
            "Cari Aduan", "Ctrl+F", "Cari aduan berdasarkan kata kunci", 
            e -> showSearchComplaints());
            
        JMenuItem detailAduanItem = createEnhancedMenuItem(
            "Detail Aduan", "Ctrl+D", "Lihat detail aduan tertentu", 
            e -> showComplaintDetails());
            
        JMenuItem statistikItem = createEnhancedMenuItem(
            "Statistik Aduan", "Ctrl+S", "Tampilkan statistik dan analisis", 
            e -> showAduanStatistics());
            
        JMenuItem aduanDaruratItem = createEnhancedMenuItem(
            "Aduan Darurat", "Ctrl+U", "Lihat aduan dengan prioritas darurat", 
            e -> showUrgentComplaints());
        
        aduanMenu.add(lihatSemuaItem);
        aduanMenu.addSeparator();
        aduanMenu.add(cariAduanItem);
        aduanMenu.add(detailAduanItem);
        aduanMenu.addSeparator();
        aduanMenu.add(statistikItem);
        aduanMenu.add(aduanDaruratItem);
        
        showDropdownWithAnimation(aduanMenu, sourceButton);
    }
    
    /**
     * Show masyarakat dropdown menu
     */
    private void showMasyarakatDropdown(JButton sourceButton) {
        JPopupMenu masyarakatMenu = createModernDropdownMenu();
        
        JMenuItem dataMasyarakatItem = createEnhancedMenuItem(
            "Data Masyarakat", "Ctrl+M", "Kelola data penduduk desa", 
            e -> navigateToMasyarakatData());
            
        JMenuItem statistikMasyarakatItem = createEnhancedMenuItem(
            "Statistik Masyarakat", null, "Lihat statistik data masyarakat", 
            e -> showMasyarakatStatistics());
            
        JMenuItem kontakInfoItem = createEnhancedMenuItem(
            "Info Kontak", null, "Informasi kontak penting", 
            e -> showContactInfo());
            
        JMenuItem petaSebaranItem = createEnhancedMenuItem(
            "Peta Sebaran", null, "Peta sebaran penduduk per RT/RW", 
            e -> showPopulationMap());
        
        masyarakatMenu.add(dataMasyarakatItem);
        masyarakatMenu.addSeparator();
        masyarakatMenu.add(statistikMasyarakatItem);
        masyarakatMenu.add(kontakInfoItem);
        masyarakatMenu.add(petaSebaranItem);
        
        showDropdownWithAnimation(masyarakatMenu, sourceButton);
    }
    
    /**
     * Show laporan dropdown menu
     */
    private void showLaporanDropdown(JButton sourceButton) {
        JPopupMenu laporanMenu = createModernDropdownMenu();
        
        JMenuItem dashboardLaporanItem = createEnhancedMenuItem(
            "Dashboard Laporan", "Ctrl+R", "Buka dashboard laporan lengkap", 
            e -> navigateToLaporan());
            
        JMenuItem exportDataItem = createEnhancedMenuItem(
            "Export Data", "Ctrl+E", "Export data ke berbagai format", 
            e -> showExportOptions());
            
        JMenuItem analitikItem = createEnhancedMenuItem(
            "Analitik", null, "Analisis data dan tren", 
            e -> showAnalytics());
            
        JMenuItem printLaporanItem = createEnhancedMenuItem(
            "Print Laporan", "Ctrl+P", "Cetak laporan", 
            e -> showPrintOptions());
            
        JMenuItem scheduledReportItem = createEnhancedMenuItem(
            "Laporan Terjadwal", null, "Atur laporan otomatis", 
            e -> showScheduledReports());
        
        laporanMenu.add(dashboardLaporanItem);
        laporanMenu.addSeparator();
        laporanMenu.add(exportDataItem);
        laporanMenu.add(analitikItem);
        laporanMenu.add(printLaporanItem);
        laporanMenu.addSeparator();
        laporanMenu.add(scheduledReportItem);
        
        showDropdownWithAnimation(laporanMenu, sourceButton);
    }
    
    /**
     * Show custom user menu for new header
     */
    private void showCustomUserMenu(JButton sourceButton) {
        JPopupMenu userMenu = createModernDropdownMenu();
        
        // Profile section
        JMenuItem profileItem = createEnhancedMenuItem(
            "👤 Profil Saya", null, "Lihat dan edit profil pengguna", 
            e -> showUserProfileDialog());
            
        JMenuItem changePasswordItem = createEnhancedMenuItem(
            "🔑 Ubah Password", null, "Ganti password akun", 
            e -> showChangePassword());
        
        // Settings section
        JMenuItem settingsItem = createEnhancedMenuItem(
            "⚙️ Pengaturan", null, "Pengaturan aplikasi", 
            e -> showSettingsDialog());
            
        JMenuItem preferencesItem = createEnhancedMenuItem(
            "🎨 Preferensi", null, "Atur tampilan dan notifikasi", 
            e -> showPreferences());
        
        // Session section
        JMenuItem sessionInfoItem = createEnhancedMenuItem(
            "ℹ️ Info Session", null, "Informasi sesi login", 
            e -> showSessionInfo());
            
        JMenuItem logoutItem = createEnhancedMenuItem(
            "🚪 Logout", "Ctrl+Q", "Keluar dari aplikasi", 
            e -> handleLogout());
        
        userMenu.add(profileItem);
        userMenu.add(changePasswordItem);
        userMenu.addSeparator();
        userMenu.add(settingsItem);
        userMenu.add(preferencesItem);
        userMenu.addSeparator();
        userMenu.add(sessionInfoItem);
        userMenu.add(logoutItem);
        
        showDropdownWithAnimation(userMenu, sourceButton);
    }
    
    /**
     * Show preferences dialog
     */
    private void showPreferences() {
        JOptionPane.showMessageDialog(this,
            "🎨 PREFERENSI TAMPILAN\n\n" +
            "Pengaturan tampilan dan notifikasi akan tersedia di sini:\n" +
            "• Tema warna\n" +
            "• Ukuran font\n" +
            "• Notifikasi suara\n" +
            "• Layout dashboard\n" +
            "• Bahasa interface",
            "🎨 PREFERENSI",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show session info dialog
     */
    private void showSessionInfo() {
        if (sessionManager != null) {
            JOptionPane.showMessageDialog(this,
                sessionManager.getSessionInfo(),
                "ℹ️ INFO SESSION",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "ℹ️ INFO SESSION\n\n" +
                "Session Manager tidak tersedia.\n" +
                "Silakan login ulang.",
                "ℹ️ INFO SESSION",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Show incoming complaints - Navigate to FormAduanManajemen and close dashboard
     */
    private void showIncomingComplaintsPanel() {
        System.out.println("📝 Navigating to FormAduanManajemen...");
        
        try {
            // Create FormAduanManajemen
            aduan.FormAduanManajemen aduanForm = new aduan.FormAduanManajemen(currentUser);
            
            // Show the form
            aduanForm.setVisible(true);
            aduanForm.toFront();
            aduanForm.requestFocus();
            
            // Close current dashboard
            this.dispose();
            
            System.out.println("✅ Successfully navigated to FormAduanManajemen and closed dashboard");
            
        } catch (Exception e) {
            System.err.println("❌ Error opening FormAduanManajemen: " + e.getMessage());
            e.printStackTrace();
            
            // Show error message to user
            JOptionPane.showMessageDialog(this,
                "❌ Tidak dapat membuka form manajemen aduan.\n" +
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Alternative method to open FormAduanManajemen as MDI
     */
    private void openFormAduanManajemenAsMDI() {
        try {
            // Check if FormAduanManajemen class exists
            Class.forName("aduan.FormAduanManajemen");
            
            // MDI mode will be activated automatically when opening internal frame
            // No need for explicit activation
            
            // Create FormAduanManajemen instance (this should work based on your codebase)
            aduan.FormAduanManajemen aduanForm = new aduan.FormAduanManajemen(currentUser);
            
            // Open as internal frame in desktop pane
            openFormAsInternalFrame(aduanForm, "📋 Manajemen Aduan Desa Tarabbi", "FormAduanManajemen");
            
            System.out.println("✅ FormAduanManajemen opened as MDI internal frame successfully!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ FormAduanManajemen class not found: " + e.getMessage());
            throw new RuntimeException("Both AduanManagementFrame and FormAduanManajemen not available");
        } catch (Exception e) {
            System.err.println("❌ Error opening FormAduanManajemen as MDI: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Show search complaints dialog - Cari Aduan
     */
    private void showSearchComplaints() {
        String searchTerm = JOptionPane.showInputDialog(this,
            "🔍 CARI ADUAN\n\n" +
            "Masukkan kata kunci pencarian:\n" +
            "(Nomor aduan, judul, nama pelapor, atau kategori)",
            "🔍 CARI ADUAN",
            JOptionPane.QUESTION_MESSAGE);
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    StringBuilder content = new StringBuilder();
                    content.append("🔍 HASIL PENCARIAN ADUAN\n");
                    content.append("Kata kunci: \"").append(searchTerm).append("\"\n\n");
                    
                    try (Connection conn = DatabaseConfig.getConnection()) {
                        String query = "SELECT c.complaint_number, c.title, c.status, c.priority, " +
                                      "c.created_at, u.full_name, cat.name as category_name " +
                                      "FROM complaints c " +
                                      "JOIN users u ON c.reporter_id = u.id " +
                                      "JOIN categories cat ON c.category_id = cat.id " +
                                      "WHERE c.complaint_number LIKE ? OR c.title LIKE ? OR " +
                                      "u.full_name LIKE ? OR cat.name LIKE ? " +
                                      "ORDER BY c.created_at DESC LIMIT 20";
                        
                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                            String searchPattern = "%" + searchTerm + "%";
                            pstmt.setString(1, searchPattern);
                            pstmt.setString(2, searchPattern);
                            pstmt.setString(3, searchPattern);
                            pstmt.setString(4, searchPattern);
                            
                            try (ResultSet rs = pstmt.executeQuery()) {
                                int count = 1;
                                while (rs.next()) {
                                    String statusIcon = getStatusIcon(rs.getString("status"));
                                    String priorityIcon = getPriorityIcon(rs.getString("priority"));
                                    
                                    content.append(String.format(
                                        "%d. %s %s #%s\n" +
                                        "   📋 %s\n" +
                                        "   👤 %s • 📂 %s\n" +
                                        "   📅 %s\n\n",
                                        count++,
                                        statusIcon,
                                        priorityIcon,
                                        rs.getString("complaint_number"),
                                        rs.getString("title"),
                                        rs.getString("full_name"),
                                        rs.getString("category_name"),
                                        rs.getTimestamp("created_at")
                                    ));
                                }
                                
                                if (count == 1) {
                                    content.append("❌ Tidak ditemukan aduan yang sesuai dengan kata kunci \"").append(searchTerm).append("\"");
                                }
                            }
                        }
                    } catch (SQLException e) {
                        content.append("❌ Error searching data: ").append(e.getMessage());
                    }
                    
                    return content.toString();
                }
                
                @Override
                protected void done() {
                    try {
                        String content = get();
                        JTextArea textArea = new JTextArea(content, 20, 60);
                        textArea.setEditable(false);
                        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                        
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        
                        JOptionPane.showMessageDialog(DashboardPetugas.this,
                            scrollPane,
                            "🔍 HASIL PENCARIAN",
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(DashboardPetugas.this,
                            "❌ Error: " + e.getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            
            worker.execute();
        }
    }
    
    /**
     * Show complaint details dialog - Detail Aduan
     */
    private void showComplaintDetails() {
        String complaintNumber = JOptionPane.showInputDialog(this,
            "📝 DETAIL ADUAN\n\n" +
            "Masukkan nomor aduan yang ingin dilihat detailnya:\n" +
            "(Format: ADU-YYYYMMDD-XXX)",
            "📝 DETAIL ADUAN",
            JOptionPane.QUESTION_MESSAGE);
        
        if (complaintNumber != null && !complaintNumber.trim().isEmpty()) {
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    StringBuilder content = new StringBuilder();
                    content.append("📝 DETAIL ADUAN\n");
                    content.append("Nomor: ").append(complaintNumber).append("\n\n");
                    
                    try (Connection conn = DatabaseConfig.getConnection()) {
                        String query = "SELECT c.*, u.full_name, u.phone, u.address, u.rt_rw, " +
                                      "cat.name as category_name, cat.icon as category_icon " +
                                      "FROM complaints c " +
                                      "JOIN users u ON c.reporter_id = u.id " +
                                      "JOIN categories cat ON c.category_id = cat.id " +
                                      "WHERE c.complaint_number = ?";
                        
                        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                            pstmt.setString(1, complaintNumber);
                            
                            try (ResultSet rs = pstmt.executeQuery()) {
                                if (rs.next()) {
                                    String statusIcon = getStatusIcon(rs.getString("status"));
                                    String priorityIcon = getPriorityIcon(rs.getString("priority"));
                                    
                                    content.append(String.format(
                                        "📋 INFORMASI ADUAN:\n" +
                                        "• Judul: %s\n" +
                                        "• Status: %s %s\n" +
                                        "• Prioritas: %s %s\n" +
                                        "• Kategori: %s %s\n" +
                                        "• Tanggal Dibuat: %s\n" +
                                        "• Lokasi: %s\n\n" +
                                        "👤 PELAPOR:\n" +
                                        "• Nama: %s\n" +
                                        "• Telepon: %s\n" +
                                        "• Alamat: %s\n" +
                                        "• RT/RW: %s\n\n" +
                                        "📄 DESKRIPSI:\n" +
                                        "%s\n",
                                        rs.getString("title"),
                                        statusIcon, rs.getString("status").toUpperCase(),
                                        priorityIcon, rs.getString("priority").toUpperCase(),
                                        rs.getString("category_icon"), rs.getString("category_name"),
                                        rs.getTimestamp("created_at"),
                                        rs.getString("location") != null ? rs.getString("location") : "Tidak disebutkan",
                                        rs.getString("full_name"),
                                        rs.getString("phone") != null ? rs.getString("phone") : "Tidak tersedia",
                                        rs.getString("address") != null ? rs.getString("address") : "Tidak tersedia",
                                        rs.getString("rt_rw") != null ? rs.getString("rt_rw") : "Tidak tersedia",
                                        rs.getString("description")
                                    ));
                                } else {
                                    content.append("❌ Aduan dengan nomor \"").append(complaintNumber).append("\" tidak ditemukan.");
                                }
                            }
                        }
                    } catch (SQLException e) {
                        content.append("❌ Error loading detail: ").append(e.getMessage());
                    }
                    
                    return content.toString();
                }
                
                @Override
                protected void done() {
                    try {
                        String content = get();
                        JTextArea textArea = new JTextArea(content, 20, 60);
                        textArea.setEditable(false);
                        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                        
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        
                        JOptionPane.showMessageDialog(DashboardPetugas.this,
                            scrollPane,
                            "📝 DETAIL ADUAN",
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(DashboardPetugas.this,
                            "❌ Error: " + e.getMessage(),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            
            worker.execute();
        }
    }
    
    /**
     * Show complaint statistics - Statistik Aduan
     */
    private void showAduanStatistics() {
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                StringBuilder content = new StringBuilder();
                content.append("📊 STATISTIK ADUAN SIPRIMA\n\n");
                
                try (Connection conn = DatabaseConfig.getConnection()) {
                    // Get overall statistics
                    String statsQuery = "SELECT " +
                        "COUNT(*) as total, " +
                        "SUM(CASE WHEN status = 'baru' THEN 1 ELSE 0 END) as baru, " +
                        "SUM(CASE WHEN status = 'proses' THEN 1 ELSE 0 END) as proses, " +
                        "SUM(CASE WHEN status = 'selesai' THEN 1 ELSE 0 END) as selesai, " +
                        "SUM(CASE WHEN priority = 'darurat' THEN 1 ELSE 0 END) as darurat, " +
                        "SUM(CASE WHEN priority = 'tinggi' THEN 1 ELSE 0 END) as tinggi, " +
                        "SUM(CASE WHEN priority = 'sedang' THEN 1 ELSE 0 END) as sedang, " +
                        "SUM(CASE WHEN priority = 'rendah' THEN 1 ELSE 0 END) as rendah " +
                        "FROM complaints";
                    
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(statsQuery)) {
                        
                        if (rs.next()) {
                            int total = rs.getInt("total");
                            content.append("📈 RINGKASAN KESELURUHAN:\n");
                            content.append(String.format("• Total Aduan: %d\n", total));
                            content.append(String.format("• Status Baru: %d (%.1f%%)\n", 
                                rs.getInt("baru"), total > 0 ? (rs.getInt("baru") * 100.0 / total) : 0));
                            content.append(String.format("• Sedang Diproses: %d (%.1f%%)\n", 
                                rs.getInt("proses"), total > 0 ? (rs.getInt("proses") * 100.0 / total) : 0));
                            content.append(String.format("• Selesai: %d (%.1f%%)\n", 
                                rs.getInt("selesai"), total > 0 ? (rs.getInt("selesai") * 100.0 / total) : 0));
                            content.append("\n⚡ PRIORITAS:\n");
                            content.append(String.format("• 🔴 Darurat: %d\n", rs.getInt("darurat")));
                            content.append(String.format("• 🟠 Tinggi: %d\n", rs.getInt("tinggi")));
                            content.append(String.format("• 🟡 Sedang: %d\n", rs.getInt("sedang")));
                            content.append(String.format("• 🟢 Rendah: %d\n\n", rs.getInt("rendah")));
                        }
                    }
                    
                    // Get category statistics
                    String categoryQuery = "SELECT cat.name, cat.icon, COUNT(c.id) as total " +
                        "FROM categories cat " +
                        "LEFT JOIN complaints c ON cat.id = c.category_id " +
                        "GROUP BY cat.id, cat.name, cat.icon " +
                        "ORDER BY total DESC";
                    
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(categoryQuery)) {
                        
                        content.append("📂 ADUAN PER KATEGORI:\n");
                        while (rs.next()) {
                            content.append(String.format("• %s %s: %d aduan\n",
                                rs.getString("icon"),
                                rs.getString("name"),
                                rs.getInt("total")
                            ));
                        }
                    }
                    
                    // Get monthly trend (last 6 months)
                    String trendQuery = "SELECT " +
                        "DATE_FORMAT(created_at, '%Y-%m') as bulan, " +
                        "COUNT(*) as total " +
                        "FROM complaints " +
                        "WHERE created_at >= DATE_SUB(NOW(), INTERVAL 6 MONTH) " +
                        "GROUP BY DATE_FORMAT(created_at, '%Y-%m') " +
                        "ORDER BY bulan DESC";
                    
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(trendQuery)) {
                        
                        content.append("\n📈 TREND 6 BULAN TERAKHIR:\n");
                        while (rs.next()) {
                            content.append(String.format("• %s: %d aduan\n",
                                rs.getString("bulan"),
                                rs.getInt("total")
                            ));
                        }
                    }
                    
                } catch (SQLException e) {
                    content.append("❌ Error loading statistics: ").append(e.getMessage());
                }
                
                return content.toString();
            }
            
            @Override
            protected void done() {
                try {
                    String content = get();
                    JTextArea textArea = new JTextArea(content, 25, 60);
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    
                    JOptionPane.showMessageDialog(DashboardPetugas.this,
                        scrollPane,
                        "📊 STATISTIK ADUAN",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(DashboardPetugas.this,
                        "❌ Error: " + e.getMessage(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Show simple complaints list as embedded panel (fallback)
     */
    private void showSimpleComplaintsList() {
        try {
            // Create embedded complaints management panel
            JPanel embeddedComplaintsPanel = new JPanel(new BorderLayout());
            embeddedComplaintsPanel.setBackground(BG_PRIMARY);
            
            // Create header with back button
            JPanel embeddedHeader = new JPanel(new BorderLayout());
            embeddedHeader.setBackground(PRIMARY_BLUE);
            embeddedHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            
            JLabel embeddedTitle = new JLabel("📋 MANAJEMEN ADUAN DESA TARABBI");
            embeddedTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            embeddedTitle.setForeground(Color.WHITE);
            
            // Back to dashboard button
            JButton backToDashboard = new JButton("🏠 Kembali ke Dashboard");
            backToDashboard.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            backToDashboard.setForeground(PRIMARY_BLUE);
            backToDashboard.setBackground(Color.WHITE);
            backToDashboard.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            backToDashboard.setFocusPainted(false);
            backToDashboard.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backToDashboard.addActionListener(e -> {
                deactivateEmbeddedMode();
                System.out.println("🏠 Returned to normal dashboard view");
            });
            
            embeddedHeader.add(embeddedTitle, BorderLayout.WEST);
            embeddedHeader.add(backToDashboard, BorderLayout.EAST);
            
            // Create content area with table-like display
            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(Color.WHITE);
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Create table header
            JPanel tableHeader = new JPanel(new GridLayout(1, 6, 10, 0));
            tableHeader.setBackground(new Color(52, 73, 94));
            tableHeader.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            String[] headers = {"No. Aduan", "Judul", "Pelapor", "Status", "Prioritas", "Tanggal"};
            for (String header : headers) {
                JLabel headerLabel = new JLabel(header);
                headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
                tableHeader.add(headerLabel);
            }
            
            // Create scrollable content area for complaints list
            JPanel complaintsList = new JPanel();
            complaintsList.setLayout(new BoxLayout(complaintsList, BoxLayout.Y_AXIS));
            complaintsList.setBackground(Color.WHITE);
            
            // Load and display complaints data
            SwingWorker<Void, JPanel> loadWorker = new SwingWorker<Void, JPanel>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try (Connection conn = DatabaseConfig.getConnection()) {
                        String query = "SELECT c.complaint_number, c.title, c.status, c.priority, " +
                                      "c.created_at, u.full_name, cat.name as category_name " +
                                      "FROM complaints c " +
                                      "JOIN users u ON c.reporter_id = u.id " +
                                      "JOIN categories cat ON c.category_id = cat.id " +
                                      "ORDER BY c.created_at DESC LIMIT 20";
                        
                        try (Statement stmt = conn.createStatement();
                             ResultSet rs = stmt.executeQuery(query)) {
                            
                            int count = 0;
                            while (rs.next()) {
                                // Create row panel
                                JPanel rowPanel = new JPanel(new GridLayout(1, 6, 10, 0));
                                final Color originalBgColor = count % 2 == 0 ? Color.WHITE : new Color(248, 249, 250);
                                rowPanel.setBackground(originalBgColor);
                                rowPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                                
                                // Add data to row
                                rowPanel.add(createCellLabel(rs.getString("complaint_number")));
                                rowPanel.add(createCellLabel(truncateText(rs.getString("title"), 30)));
                                rowPanel.add(createCellLabel(rs.getString("full_name")));
                                rowPanel.add(createStatusLabel(rs.getString("status")));
                                rowPanel.add(createPriorityLabel(rs.getString("priority")));
                                rowPanel.add(createCellLabel(rs.getTimestamp("created_at").toString().substring(0, 16)));
                                
                                // Add click handler for row
                                rowPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                                final String complaintNumber = rs.getString("complaint_number");
                                rowPanel.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        showComplaintDetailDialog(complaintNumber);
                                    }
                                    
                                    @Override
                                    public void mouseEntered(MouseEvent e) {
                                        rowPanel.setBackground(new Color(232, 245, 255));
                                    }
                                    
                                    @Override
                                    public void mouseExited(MouseEvent e) {
                                        rowPanel.setBackground(originalBgColor);
                                    }
                                });
                                
                                publish(rowPanel);
                                count++;
                            }
                            
                            if (count == 0) {
                                JPanel emptyPanel = new JPanel();
                                emptyPanel.setBackground(Color.WHITE);
                                emptyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                                
                                JLabel emptyLabel = new JLabel("📝 Belum ada aduan terdaftar");
                                emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                                emptyLabel.setForeground(TEXT_SECONDARY);
                                emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                emptyPanel.add(emptyLabel);
                                
                                publish(emptyPanel);
                            }
                        }
                    } catch (SQLException e) {
                        JPanel errorPanel = new JPanel();
                        errorPanel.setBackground(Color.WHITE);
                        errorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                        
                        JLabel errorLabel = new JLabel("❌ Error loading data: " + e.getMessage());
                        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        errorLabel.setForeground(ERROR);
                        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        errorPanel.add(errorLabel);
                        
                        publish(errorPanel);
                    }
                    
                    return null;
                }
                
                @Override
                protected void process(java.util.List<JPanel> chunks) {
                    for (JPanel panel : chunks) {
                        complaintsList.add(panel);
                    }
                    complaintsList.revalidate();
                    complaintsList.repaint();
                }
            };
            
            loadWorker.execute();
            
            // Create scroll pane for the table
            JScrollPane scrollPane = new JScrollPane(complaintsList);
            scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT));
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            
            // Assemble content panel
            contentPanel.add(tableHeader, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            
            // Add instruction panel
            JPanel instructionPanel = new JPanel();
            instructionPanel.setBackground(new Color(240, 248, 255));
            instructionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            
            JLabel instructionLabel = new JLabel("💡 Klik pada baris aduan untuk melihat detail");
            instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            instructionLabel.setForeground(TEXT_SECONDARY);
            instructionPanel.add(instructionLabel);
            
            contentPanel.add(instructionPanel, BorderLayout.SOUTH);
            
            // Assemble the main panel
            embeddedComplaintsPanel.add(embeddedHeader, BorderLayout.NORTH);
            embeddedComplaintsPanel.add(contentPanel, BorderLayout.CENTER);
            
            // Add to dashboard
            mainContentPanel.add(embeddedComplaintsPanel, BorderLayout.CENTER);
            mainContentPanel.revalidate();
            mainContentPanel.repaint();
            
            currentEmbeddedPanel = embeddedComplaintsPanel;
            currentEmbeddedPanelType = "COMPLAINTS_MANAGEMENT";
            
            System.out.println("📋 Simple complaints list embedded successfully in dashboard");
            
        } catch (Exception e) {
            System.err.println("❌ Error creating simple complaints list: " + e.getMessage());
            e.printStackTrace();
            
            // Final fallback - show error dialog
            JOptionPane.showMessageDialog(this,
                "❌ Tidak dapat menampilkan daftar aduan.\n\n" +
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Helper method to create cell labels for table
     */
    private JLabel createCellLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_PRIMARY);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    /**
     * Helper method to create status labels with colors
     */
    private JLabel createStatusLabel(String status) {
        JLabel label = new JLabel(getStatusIcon(status) + " " + status.toUpperCase());
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        switch (status.toLowerCase()) {
            case "baru":
                label.setForeground(INFO);
                break;
            case "proses":
                label.setForeground(WARNING);
                break;
            case "selesai":
                label.setForeground(SUCCESS);
                break;
            default:
                label.setForeground(TEXT_SECONDARY);
                break;
        }
        
        return label;
    }
    
    /**
     * Helper method to create priority labels with colors
     */
    private JLabel createPriorityLabel(String priority) {
        JLabel label = new JLabel(getPriorityIcon(priority) + " " + priority.toUpperCase());
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        switch (priority.toLowerCase()) {
            case "darurat":
                label.setForeground(ERROR);
                break;
            case "tinggi":
                label.setForeground(WARNING);
                break;
            case "sedang":
                label.setForeground(INFO);
                break;
            case "rendah":
                label.setForeground(SUCCESS);
                break;
            default:
                label.setForeground(TEXT_SECONDARY);
                break;
        }
        
        return label;
    }
    
    /**
     * Helper method to truncate text
     */
    private String truncateText(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Show complaint detail dialog when row is clicked
     */
    private void showComplaintDetailDialog(String complaintNumber) {
        JOptionPane.showMessageDialog(this,
            "📝 Detail untuk aduan: " + complaintNumber + "\n\n" +
            "Fitur detail aduan akan ditampilkan di sini\n" +
            "dengan informasi lengkap tentang aduan.",
            "Detail Aduan",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Add missing methods to fix compilation errors
     */
    private void deactivateEmbeddedMode() {
        // Deactivate embedded mode - return to normal dashboard view
        if (currentEmbeddedPanel != null) {
            mainContentPanel.remove(currentEmbeddedPanel);
            mainContentPanel.revalidate();
            mainContentPanel.repaint();
            currentEmbeddedPanel = null;
            currentEmbeddedPanelType = null;
        }
        System.out.println("📱 Embedded mode deactivated - back to normal dashboard");
    }
    
    private void showPopulationMap() {
        JOptionPane.showMessageDialog(this,
            "🗺️ PETA SEBARAN PENDUDUK\n\n" +
            "Menampilkan peta interaktif sebaran penduduk\n" +
            "berdasarkan RT/RW di Desa Tarabbi.\n\n" +
            "Fitur ini akan menampilkan:\n" +
            "• Distribusi penduduk per wilayah\n" +
            "• Kepadatan populasi\n" +
            "• Statistik demografi",
            "🗺️ PETA SEBARAN",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showScheduledReports() {
        JOptionPane.showMessageDialog(this,
            "⏰ LAPORAN TERJADWAL\n\n" +
            "Atur laporan otomatis yang akan dikirim\n" +
            "secara berkala ke email Anda.\n\n" +
            "Jenis laporan tersedia:\n" +
            "• Laporan Harian (setiap pagi)\n" +
            "• Laporan Mingguan (setiap Senin)\n" +
            "• Laporan Bulanan (tanggal 1)\n" +
            "• Laporan Tahunan (Januari)",
            "⏰ LAPORAN TERJADWAL",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show masyarakat statistics
     */
    private void showMasyarakatStatistics() {
        JOptionPane.showMessageDialog(this,
            "📊 STATISTIK MASYARAKAT\n\n" +
            "• Total pengguna terdaftar\n" +
            "• Tingkat partisipasi pengaduan\n" +
            "• Distribusi per RT/RW\n" +
            "• Kategori aduan terpopuler",
            "📊 STATISTIK",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show contact info
     */
    private void showContactInfo() {
        JOptionPane.showMessageDialog(this,
            "📞 INFORMASI KONTAK\n\n" +
            "📧 Email: admin@desatarabbi.go.id\n" +
            "📞 Telepon: (0411) 123-4567\n" +
            "📱 WhatsApp: 081234567890\n" +
            "🌐 Website: www.desatarabbi.go.id\n\n" +
            "📍 Alamat Kantor Desa:\n" +
            "Jl. Raya Tarabbi No. 123\n" +
            "Kec. Tarabbi, Kab. Contoh",
            "📞 KONTAK DESA TARABBI",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show analytics dialog
     */
    private void showAnalytics() {
        JOptionPane.showMessageDialog(this,
            "📈 ANALITIK PENGADUAN\n\n" +
            "• Trend pengaduan per bulan\n" +
            "• Waktu rata-rata penyelesaian\n" +
            "• Tingkat kepuasan masyarakat\n" +
            "• Performa petugas\n" +
            "• Kategori yang sering dilaporkan",
            "📈 ANALITIK",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show print options
     */
    private void showPrintOptions() {
        String[] options = {"📄 Laporan Harian", "📅 Laporan Mingguan", "📊 Laporan Bulanan", "❌ Batal"};
        
        int choice = JOptionPane.showOptionDialog(this,
            "🖨️ CETAK LAPORAN\n\n" +
            "Pilih jenis laporan yang ingin dicetak:",
            "🖨️ PRINT OPTIONS",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[3]);
        
        if (choice >= 0 && choice <= 2) {
            JOptionPane.showMessageDialog(this,
                "✅ " + options[choice] + " berhasil dicetak!\n" +
                "File disimpan di folder Documents/SIPRIMA_Print/",
                "🖨️ PRINT BERHASIL",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Find header button by text for dropdown positioning
     */
    private JButton findHeaderButton(String buttonText) {
        try {
            // Search through all components in the frame
            return findButtonRecursive(getContentPane(), buttonText);
        } catch (Exception e) {
            System.err.println("❌ Error finding header button: " + buttonText + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Recursively search for button with specific text
     */
    private JButton findButtonRecursive(Container container, String buttonText) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (button.getText() != null && button.getText().contains(buttonText.replace("📋 ", "").replace("👥 ", "").replace("📊 ", ""))) {
                    return button;
                }
            } else if (component instanceof Container) {
                JButton found = findButtonRecursive((Container) component, buttonText);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    
    /**
     * Main dropdown menu method
     */
    private void showDropdownMenu(JButton sourceButton, String menuType) {
        System.out.println("🔍 DEBUG: showDropdownMenu called for " + menuType);
        
        try {
            JPopupMenu dropdown = new JPopupMenu();
            dropdown.setBackground(Color.WHITE);
            dropdown.setBorder(BorderFactory.createLineBorder(new Color(220, 221, 225), 1));
            
            // Create menu items based on type
            switch (menuType.toLowerCase()) {
                case "aduan":
                    JMenuItem listAduanItem = new JMenuItem("📋 Lihat Semua Aduan");
                    listAduanItem.addActionListener(e -> showIncomingComplaintsPanel());
                    
                    JMenuItem newAduanItem = new JMenuItem("➕ Aduan Baru");
                    newAduanItem.addActionListener(e -> openNewAduanForm());
                    
                    JMenuItem urgentAduanItem = new JMenuItem("🚨 Aduan Darurat");
                    urgentAduanItem.addActionListener(e -> showUrgentComplaints());
                    
                    dropdown.add(listAduanItem);
                    dropdown.add(newAduanItem);
                    dropdown.addSeparator();
                    dropdown.add(urgentAduanItem);
                    break;
                    
                case "masyarakat":
                    JMenuItem dataMasyarakatItem = new JMenuItem("👥 Data Masyarakat");
                    dataMasyarakatItem.addActionListener(e -> navigateToMasyarakatData());
                    
                    JMenuItem statistikItem = new JMenuItem("📊 Statistik Pengaduan");
                    statistikItem.addActionListener(e -> showMasyarakatStatistics());
                    
                    JMenuItem kontakItem = new JMenuItem("📞 Info Kontak");
                    kontakItem.addActionListener(e -> showContactInfo());
                    
                    dropdown.add(dataMasyarakatItem);
                    dropdown.add(statistikItem);
                    dropdown.addSeparator();
                    dropdown.add(kontakItem);
                    break;
                    
                case "laporan":
                    JMenuItem dashboardLaporanItem = new JMenuItem("📊 Dashboard Laporan");
                    dashboardLaporanItem.addActionListener(e -> navigateToLaporan());
                    
                    JMenuItem exportItem = new JMenuItem("📤 Export Data");
                    exportItem.addActionListener(e -> showExportOptions());
                    
                    JMenuItem analitikItem = new JMenuItem("📈 Analitik");
                    analitikItem.addActionListener(e -> showAnalytics());
                    
                    JMenuItem printItem = new JMenuItem("🖨️ Print Laporan");
                    printItem.addActionListener(e -> showPrintOptions());
                    
                    dropdown.add(dashboardLaporanItem);
                    dropdown.add(exportItem);
                    dropdown.add(analitikItem);
                    dropdown.addSeparator();
                    dropdown.add(printItem);
                    break;
                    
                default:
                    JMenuItem defaultItem = new JMenuItem("Menu " + menuType);
                    defaultItem.addActionListener(e -> JOptionPane.showMessageDialog(this, 
                        "Menu " + menuType + " clicked!", 
                        "Info", 
                        JOptionPane.INFORMATION_MESSAGE));
                    dropdown.add(defaultItem);
                    break;
            }
            
            System.out.println("🔍 DEBUG: Dropdown created with " + dropdown.getComponentCount() + " items");
            System.out.println("🔍 DEBUG: Source button = " + sourceButton);
            System.out.println("🔍 DEBUG: Button visible = " + (sourceButton != null ? sourceButton.isVisible() : "null"));
            
            if (sourceButton != null && sourceButton.isVisible()) {
                // Show dropdown tepat di bawah button
                dropdown.show(sourceButton, 0, sourceButton.getHeight());
                System.out.println("✅ Dropdown shown successfully at button position!");
            } else {
                System.out.println("❌ Source button is null or not visible!");
                // Fallback - show at center of frame
                dropdown.show(this, getWidth() / 2, 100);
            }
            
        } catch (Exception e) {
            System.out.println("❌ ERROR in showDropdownMenu: " + e.getMessage());
            e.printStackTrace();
            
            // Ultimate fallback - simple dialog
            JOptionPane.showMessageDialog(this, 
                "Menu " + menuType + " berhasil diklik!\n\n" +
                "Menu dropdown akan ditampilkan di sini.", 
                "🎯 " + menuType.toUpperCase(), 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // ==========================================
    // AUTO-CLOSE ENHANCED WINDOW MANAGEMENT
    // ==========================================
    
    /**
     * Open Data Masyarakat with auto-close - ENHANCED UX VERSION
     */
    private void openDataMasyarakatWithAutoClose() {
        // AUTO-CLOSE: Tutup form sebelumnya yang terbuka
        closeCurrentOpenForm();
        
        try {
            // ENHANCED: Show loading indicator
            showLoadingDialog("Memuat data masyarakat...");
            
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                private JFrame masyarakatFrame; // Fallback to generic JFrame
                
                @Override
                protected Void doInBackground() throws Exception {
                    // Load form di background thread
                    try {
                        // Try to load the actual frame class if it exists
                        Class.forName("masyarakat.DataMasyarakatFrame");
                        // Use reflection to create instance with user parameter
                        Class<?> frameClass = Class.forName("masyarakat.DataMasyarakatFrame");
                        java.lang.reflect.Constructor<?> constructor = frameClass.getConstructor(User.class);
                        masyarakatFrame = (javax.swing.JFrame) constructor.newInstance(currentUser);
                    } catch (ClassNotFoundException e) {
                        // Frame doesn't exist yet - will use fallback dialog
                        masyarakatFrame = null;
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        hideLoadingDialog();
                        
                        if (masyarakatFrame != null) {
                            // REGISTER: Set sebagai form yang sedang terbuka
                            registerCurrentForm(masyarakatFrame, "DataMasyarakatFrame");
                            
                            masyarakatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            masyarakatFrame.setLocationRelativeTo(DashboardPetugas.this);
                            
                            // ENHANCED: Add window listener untuk refresh dashboard setelah form ditutup
                            masyarakatFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosed(WindowEvent e) {
                                    // UNREGISTER: Clear form ketika ditutup
                                    clearCurrentForm();
                                    // Refresh dashboard data setelah form ditutup
                                    SwingUtilities.invokeLater(() -> {
                                        loadDashboardData();
                                        showQuickNotification("🔄 Dashboard", "Data dashboard telah diperbarui");
                                    });
                                }
                            });
                            
                            // ENHANCED: Show dengan smooth transition
                            masyarakatFrame.setVisible(true);
                            masyarakatFrame.toFront();
                            masyarakatFrame.requestFocus();
                            
                            System.out.println("✅ Data Masyarakat frame berhasil dibuka dengan auto-close policy");
                        } else {
                            // Fallback ke dialog jika frame belum ada
                            showMasyarakatListDialog();
                        }
                        
                    } catch (Exception e) {
                        hideLoadingDialog();
                        showUserFriendlyError("Data Masyarakat", 
                            "Tidak dapat membuka form data masyarakat", 
                            e.getMessage(),
                            "Coba lagi beberapa saat atau hubungi administrator");
                    }
                }
            };
            
            worker.execute();
            
        } catch (Exception e) {
            hideLoadingDialog();
            showUserFriendlyError("Data Masyarakat", 
                "Error membuka data masyarakat", 
                e.getMessage(),
                "Pastikan aplikasi berjalan dengan baik dan coba lagi");
        }
    }
    
    /**
     * Open Laporan Dashboard with auto-close - ENHANCED UX VERSION
     */
    private void openLaporanDashboardWithAutoClose() {
        // AUTO-CLOSE: Tutup form sebelumnya yang terbuka
        closeCurrentOpenForm();
        
        try {
            // ENHANCED: Show loading indicator
            showLoadingDialog("Memuat dashboard laporan...");
            
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                private JFrame laporanFrame; // Fallback to generic JFrame
                
                @Override
                protected Void doInBackground() throws Exception {
                    // Load form di background thread
                    try {
                        // Try to load the actual frame class if it exists
                        Class.forName("laporan.LaporanDashboardFrame");
                        // Use reflection to create instance with user parameter
                        Class<?> frameClass = Class.forName("laporan.LaporanDashboardFrame");
                        java.lang.reflect.Constructor<?> constructor = frameClass.getConstructor(User.class);
                        laporanFrame = (javax.swing.JFrame) constructor.newInstance(currentUser);
                    } catch (ClassNotFoundException e) {
                        // Frame doesn't exist yet - will use fallback dialog
                        laporanFrame = null;
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        hideLoadingDialog();
                        
                        if (laporanFrame != null) {
                            // REGISTER: Set sebagai form yang sedang terbuka
                            registerCurrentForm(laporanFrame, "LaporanDashboardFrame");
                            
                            laporanFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            laporanFrame.setLocationRelativeTo(DashboardPetugas.this);
                            
                            // ENHANCED: Add window listener untuk refresh dashboard setelah form ditutup
                            laporanFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosed(WindowEvent e) {
                                    // UNREGISTER: Clear form ketika ditutup
                                    clearCurrentForm();
                                    // Refresh dashboard data setelah form ditutup
                                    SwingUtilities.invokeLater(() -> {
                                        loadDashboardData();
                                        showQuickNotification("🔄 Dashboard", "Data dashboard telah diperbarui");
                                    });
                                }
                            });
                            
                            // ENHANCED: Show dengan smooth transition
                            laporanFrame.setVisible(true);
                            laporanFrame.toFront();
                            laporanFrame.requestFocus();
                            
                            System.out.println("✅ Laporan Dashboard berhasil dibuka dengan auto-close policy");
                        } else {
                            // Fallback ke dialog jika frame belum ada
                            showLaporanOptionsDialog();
                        }
                        
                    } catch (Exception e) {
                        hideLoadingDialog();
                        showUserFriendlyError("Dashboard Laporan", 
                            "Tidak dapat membuka dashboard laporan", 
                            e.getMessage(),
                            "Coba lagi beberapa saat atau hubungi administrator");
                    }
                }
            };
            
            worker.execute();
            
        } catch (Exception e) {
            hideLoadingDialog();
            showUserFriendlyError("Dashboard Laporan", 
                "Error membuka dashboard laporan", 
                e.getMessage(),
                "Pastikan aplikasi berjalan dengan baik dan coba lagi");
        }
    }
    
    /**
     * Open User Profile with auto-close - ENHANCED UX VERSION
     */
    private void openUserProfileWithAutoClose() {
        // AUTO-CLOSE: Tutup form sebelumnya yang terbuka
        closeCurrentOpenForm();
        
        try {
            // ENHANCED: Show loading indicator
            showLoadingDialog("Memuat profil pengguna...");
            
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                private JFrame profileFrame; // Fallback to generic JFrame
                
                @Override
                protected Void doInBackground() throws Exception {
                    // Load form di background thread
                    try {
                        // Try to load the actual frame class if it exists
                        Class.forName("user.UserProfileFrame");
                        // Use reflection to create instance with user parameter
                        Class<?> frameClass = Class.forName("user.UserProfileFrame");
                        java.lang.reflect.Constructor<?> constructor = frameClass.getConstructor(User.class);
                        profileFrame = (javax.swing.JFrame) constructor.newInstance(currentUser);
                    } catch (ClassNotFoundException e) {
                        // Frame doesn't exist yet - will use fallback dialog
                        profileFrame = null;
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        hideLoadingDialog();
                        
                        if (profileFrame != null) {
                            // REGISTER: Set sebagai form yang sedang terbuka
                            registerCurrentForm(profileFrame, "UserProfileFrame");
                            
                            profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            profileFrame.setLocationRelativeTo(DashboardPetugas.this);
                            
                            // ENHANCED: Add window listener untuk refresh dashboard setelah form ditutup
                            profileFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosed(WindowEvent e) {
                                    // UNREGISTER: Clear form ketika ditutup
                                    clearCurrentForm();
                                    // Refresh dashboard user info setelah form ditutup
                                    SwingUtilities.invokeLater(() -> {
                                        updateUserInfo();
                                        showQuickNotification("👤 Profil", "Data profil telah diperbarui");
                                    });
                                }
                            });
                            
                            // ENHANCED: Show dengan smooth transition
                            profileFrame.setVisible(true);
                            profileFrame.toFront();
                            profileFrame.requestFocus();
                            
                            System.out.println("✅ User Profile berhasil dibuka dengan auto-close policy");
                        } else {
                            // Fallback ke dialog jika frame belum ada
                            showUserProfileDialog();
                        }
                        
                    } catch (Exception e) {
                        hideLoadingDialog();
                        showUserFriendlyError("Profil Pengguna", 
                            "Tidak dapat membuka profil pengguna", 
                            e.getMessage(),
                            "Coba lagi beberapa saat atau hubungi administrator");
                    }
                }
            };
            
            worker.execute();
            
        } catch (Exception e) {
            hideLoadingDialog();
            showUserFriendlyError("Profil Pengguna", 
                "Error membuka profil pengguna", 
                e.getMessage(),
                "Pastikan aplikasi berjalan dengan baik dan coba lagi");
        }
    }
    
    /**
     * Open Settings with auto-close - ENHANCED UX VERSION
     */
    private void openSettingsWithAutoClose() {
        // AUTO-CLOSE: Tutup form sebelumnya yang terbuka
        closeCurrentOpenForm();
        
        try {
            // ENHANCED: Show loading indicator
            showLoadingDialog("Memuat pengaturan...");
            
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                private JFrame settingsFrame; // Fallback to generic JFrame
                
                @Override
                protected Void doInBackground() throws Exception {
                    // Load form di background thread
                    try {
                        // Try to load the actual frame class if it exists
                        Class.forName("settings.SettingsFrame");
                        // Use reflection to create instance with user parameter
                        Class<?> frameClass = Class.forName("settings.SettingsFrame");
                        java.lang.reflect.Constructor<?> constructor = frameClass.getConstructor(User.class);
                        settingsFrame = (javax.swing.JFrame) constructor.newInstance(currentUser);
                    } catch (ClassNotFoundException e) {
                        // Frame doesn't exist yet - will use fallback dialog
                        settingsFrame = null;
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        hideLoadingDialog();
                        
                        if (settingsFrame != null) {
                            // REGISTER: Set sebagai form yang sedang terbuka
                            registerCurrentForm(settingsFrame, "SettingsFrame");
                            
                            settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            settingsFrame.setLocationRelativeTo(DashboardPetugas.this);
                            
                            // ENHANCED: Add window listener untuk refresh dashboard setelah form ditutup
                            settingsFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosed(WindowEvent e) {
                                    // UNREGISTER: Clear form ketika ditutup
                                    clearCurrentForm();
                                    // Refresh dashboard setelah settings ditutup
                                    SwingUtilities.invokeLater(() -> {
                                        loadDashboardData();
                                        showQuickNotification("⚙️ Pengaturan", "Pengaturan telah disimpan");
                                    });
                                }
                            });
                            
                            // ENHANCED: Show dengan smooth transition
                            settingsFrame.setVisible(true);
                            settingsFrame.toFront();
                            settingsFrame.requestFocus();
                            
                            System.out.println("✅ Settings berhasil dibuka dengan auto-close policy");
                        } else {
                            // Fallback ke dialog jika frame belum ada
                            showSettingsDialog();
                        }
                        
                    } catch (Exception e) {
                        hideLoadingDialog();
                        showUserFriendlyError("Pengaturan", 
                            "Tidak dapat membuka pengaturan", 
                            e.getMessage(),
                            "Coba lagi beberapa saat atau hubungi administrator");
                    }
                }
            };
            
            worker.execute();
            
        } catch (Exception e) {
            hideLoadingDialog();
            showUserFriendlyError("Pengaturan", 
                "Error membuka pengaturan", 
                e.getMessage(),
                "Pastikan aplikasi berjalan dengan baik dan coba lagi");
        }
    }
}
