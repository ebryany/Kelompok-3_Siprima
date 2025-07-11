/*
 * SIPRIMA Icon Loader Utility
 * Utility class untuk loading dan managing icons dalam aplikasi
 */
package Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * @author febry
 * Utility class untuk loading icons dengan fallback dan error handling
 */
public class IconLoader {
    
    private static final String ICON_PATH = "/icon/";
    
    /**
     * Load icon dengan size default (24x24)
     * @param fileName nama file icon
     * @return ImageIcon atau placeholder jika gagal
     */
    public static ImageIcon loadIcon(String fileName) {
        return loadIcon(fileName, 24, 24);
    }
    
    /**
     * Load icon dengan size custom
     * @param fileName nama file icon
     * @param width lebar icon
     * @param height tinggi icon
     * @return ImageIcon yang sudah di-resize
     */
    public static ImageIcon loadIcon(String fileName, int width, int height) {
        try {
            // Coba load dari resource path
            URL iconURL = IconLoader.class.getResource(ICON_PATH + fileName);
            if (iconURL != null) {
                ImageIcon originalIcon = new ImageIcon(iconURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                    width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } else {
                System.err.println("⚠️ Icon not found: " + fileName);
                return createPlaceholderIcon(width, height, fileName);
            }
        } catch (Exception e) {
            System.err.println("❌ Error loading icon: " + fileName + " - " + e.getMessage());
            return createPlaceholderIcon(width, height, fileName);
        }
    }
    
    /**
     * Load icon dengan fallback ke alternative filename
     * @param primaryFile file utama
     * @param fallbackFile file alternatif
     * @param width lebar
     * @param height tinggi
     * @return ImageIcon
     */
    public static ImageIcon loadIconWithFallback(String primaryFile, String fallbackFile, int width, int height) {
        // Coba load primary file
        URL iconURL = IconLoader.class.getResource(ICON_PATH + primaryFile);
        if (iconURL != null) {
            return loadIcon(primaryFile, width, height);
        }
        
        // Fallback ke alternative
        iconURL = IconLoader.class.getResource(ICON_PATH + fallbackFile);
        if (iconURL != null) {
            return loadIcon(fallbackFile, width, height);
        }
        
        // Jika semua gagal, buat placeholder
        return createPlaceholderIcon(width, height, primaryFile);
    }
    
    /**
     * Create placeholder icon jika file tidak ditemukan
     * @param width lebar placeholder
     * @param height tinggi placeholder
     * @param fileName nama file untuk debugging
     * @return ImageIcon placeholder
     */
    private static ImageIcon createPlaceholderIcon(int width, int height, String fileName) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, width, height);
        
        // Border
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawRect(0, 0, width-1, height-1);
        
        // Question mark or icon identifier
        g2d.setColor(new Color(150, 150, 150));
        Font font = new Font("SansSerif", Font.BOLD, Math.max(8, width/3));
        g2d.setFont(font);
        
        String text = "?";
        if (fileName != null) {
            if (fileName.contains("eye")) text = "👁";
            else if (fileName.contains("home")) text = "🏠";
            else if (fileName.contains("add")) text = "+";
            else if (fileName.contains("edit")) text = "✏";
            else if (fileName.contains("delete")) text = "🗑";
        }
        
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        
        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + fm.getAscent();
        
        g2d.drawString(text, x, y);
        g2d.dispose();
        
        return new ImageIcon(placeholder);
    }
    
    /**
     * Check apakah icon file exists
     * @param fileName nama file
     * @return true jika exists
     */
    public static boolean iconExists(String fileName) {
        URL iconURL = IconLoader.class.getResource(ICON_PATH + fileName);
        return iconURL != null;
    }
    
    /**
     * Create colored version dari existing icon
     * @param fileName nama file icon
     * @param color warna yang diinginkan
     * @param width lebar
     * @param height tinggi
     * @return ImageIcon dengan warna custom
     */
    public static ImageIcon createColoredIcon(String fileName, Color color, int width, int height) {
        ImageIcon originalIcon = loadIcon(fileName, width, height);
        
        BufferedImage coloredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = coloredImage.createGraphics();
        
        // Draw original icon
        g2d.drawImage(originalIcon.getImage(), 0, 0, null);
        
        // Apply color overlay
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, height);
        
        g2d.dispose();
        return new ImageIcon(coloredImage);
    }
    
    /**
     * Predefined icon constants untuk semua form SIPRIMA
     * Organized berdasarkan kategori untuk kemudahan maintenance
     */
    public static class Icons {
        
        // ============================= AUTHENTICATION =============================
        // Icons untuk auth forms: FormLogin, FormRegister, FormSplashScreen
        
        /** Password toggle icons (existing) */
        public static final ImageIcon EYE = loadIconWithFallback("eye.png", "auth/eye.png", 16, 16);
        public static final ImageIcon EYE_CLOSED = loadIconWithFallback("close-eye.png", "auth/close-eye.png", 16, 16);
        
        /** Authentication action icons */
        public static final ImageIcon LOGIN = loadIconWithFallback("login.png", "auth/login.png", 20, 20);
        public static final ImageIcon LOGOUT = loadIconWithFallback("auth/logout.png", "login.png", 20, 20);
        public static final ImageIcon USER_PLUS = loadIconWithFallback("auth/user-plus.png", "login.png", 20, 20);
        
        /** Field indicator icons */
        public static final ImageIcon USER = loadIconWithFallback("auth/user.png", "login.png", 18, 18);
        public static final ImageIcon LOCK = loadIconWithFallback("auth/lock.png", "login.png", 18, 18);
        
        /** Splash screen icons */
        public static final ImageIcon LOGO = loadIconWithFallback("auth/logo.png", "login.png", 32, 32);
        public static final ImageIcon LOADING = loadIconWithFallback("auth/loading.png", "login.png", 20, 20);
        
        // ============================= NAVIGATION =============================
        // Icons untuk navigasi umum: home, back, dashboard, menu
        
        public static final ImageIcon HOME = loadIconWithFallback("home.png", "navigation/home.png", 20, 20);
        public static final ImageIcon HOME_LARGE = loadIconWithFallback("home.png", "navigation/home.png", 24, 24);
        public static final ImageIcon BACK = loadIconWithFallback("navigation/back.png", "home.png", 20, 20);
        public static final ImageIcon DASHBOARD = loadIconWithFallback("navigation/dashboard.png", "home.png", 24, 24);
        public static final ImageIcon MENU = loadIconWithFallback("navigation/menu.png", "home.png", 20, 20);
        public static final ImageIcon BREADCRUMB = loadIconWithFallback("navigation/breadcrumb.png", "home.png", 16, 16);
        
        // ============================= ACTIONS =============================
        // Icons untuk action buttons: add, edit, delete, view, search, dll
        
        public static final ImageIcon EDIT = loadIconWithFallback("edit.png", "actions/edit.png", 20, 20);
        public static final ImageIcon ADD = loadIconWithFallback("actions/add.png", "edit.png", 20, 20);
        public static final ImageIcon DELETE = loadIconWithFallback("actions/delete.png", "edit.png", 20, 20);
        public static final ImageIcon VIEW = loadIconWithFallback("actions/view.png", "eye.png", 20, 20);
        public static final ImageIcon SEARCH = loadIconWithFallback("actions/search.png", "edit.png", 18, 18);
        public static final ImageIcon FILTER = loadIconWithFallback("actions/filter.png", "edit.png", 18, 18);
        public static final ImageIcon SAVE = loadIconWithFallback("actions/save.png", "edit.png", 20, 20);
        public static final ImageIcon SEND = loadIconWithFallback("actions/send.png", "edit.png", 20, 20);
        public static final ImageIcon DRAFT = loadIconWithFallback("actions/draft.png", "edit.png", 20, 20);
        public static final ImageIcon REFRESH = loadIconWithFallback("actions/refresh.png", "edit.png", 18, 18);
        public static final ImageIcon SORT = loadIconWithFallback("actions/sort.png", "edit.png", 18, 18);
        public static final ImageIcon EXPORT = loadIconWithFallback("actions/export.png", "edit.png", 20, 20);
        
        // Bulk actions
        public static final ImageIcon SELECT_ALL = loadIconWithFallback("actions/select-all.png", "edit.png", 18, 18);
        public static final ImageIcon BULK_EDIT = loadIconWithFallback("actions/bulk-edit.png", "edit.png", 20, 20);
        public static final ImageIcon BULK_DELETE = loadIconWithFallback("actions/bulk-delete.png", "edit.png", 20, 20);
        
        // View options
        public static final ImageIcon LIST_VIEW = loadIconWithFallback("actions/list.png", "edit.png", 18, 18);
        public static final ImageIcon GRID_VIEW = loadIconWithFallback("actions/grid.png", "edit.png", 18, 18);
        
        // ============================= STATUS =============================
        // Icons untuk status aduan berdasarkan enum di Aduan.java
        
        public static final ImageIcon STATUS_NEW = loadIconWithFallback("status/status-new.png", "edit.png", 24, 24);
        public static final ImageIcon STATUS_VALIDATION = loadIconWithFallback("status/status-validation.png", "edit.png", 24, 24);
        public static final ImageIcon STATUS_PROCESS = loadIconWithFallback("status/status-process.png", "edit.png", 24, 24);
        public static final ImageIcon STATUS_COMPLETED = loadIconWithFallback("status/status-completed.png", "edit.png", 24, 24);
        public static final ImageIcon STATUS_REJECTED = loadIconWithFallback("status/status-rejected.png", "edit.png", 24, 24);
        
        // Status actions
        public static final ImageIcon APPROVE = loadIconWithFallback("status/approve.png", "edit.png", 20, 20);
        public static final ImageIcon REJECT = loadIconWithFallback("status/reject.png", "edit.png", 20, 20);
        public static final ImageIcon ASSIGN = loadIconWithFallback("status/assign.png", "edit.png", 20, 20);
        public static final ImageIcon STATUS_CHANGE = loadIconWithFallback("status/status-change.png", "edit.png", 20, 20);
        
        // ============================= PRIORITY =============================
        // Icons untuk priority levels berdasarkan enum di Aduan.java
        
        public static final ImageIcon PRIORITY_LOW = loadIconWithFallback("priority/priority-low.png", "edit.png", 16, 16);
        public static final ImageIcon PRIORITY_MEDIUM = loadIconWithFallback("priority/priority-medium.png", "edit.png", 16, 16);
        public static final ImageIcon PRIORITY_HIGH = loadIconWithFallback("priority/priority-high.png", "edit.png", 16, 16);
        public static final ImageIcon PRIORITY_URGENT = loadIconWithFallback("priority/priority-urgent.png", "edit.png", 16, 16);
        
        // ============================= USERS =============================
        // Icons untuk user management: ManajemenUser, FormProfil, dll
        
        public static final ImageIcon USERS = loadIconWithFallback("users/users.png", "login.png", 24, 24);
        public static final ImageIcon ADD_USER = loadIconWithFallback("users/add-user.png", "login.png", 20, 20);
        public static final ImageIcon EDIT_USER = loadIconWithFallback("users/edit-user.png", "edit.png", 20, 20);
        public static final ImageIcon DELETE_USER = loadIconWithFallback("users/delete-user.png", "edit.png", 20, 20);
        public static final ImageIcon USER_PROFILE = loadIconWithFallback("users/user-profile.png", "login.png", 24, 24);
        public static final ImageIcon USER_PERMISSIONS = loadIconWithFallback("users/user-permissions.png", "edit.png", 20, 20);
        
        // Role icons
        public static final ImageIcon ROLE_ADMIN = loadIconWithFallback("users/role-admin.png", "login.png", 16, 16);
        public static final ImageIcon ROLE_SUPERVISOR = loadIconWithFallback("users/role-supervisor.png", "login.png", 16, 16);
        public static final ImageIcon ROLE_PETUGAS = loadIconWithFallback("users/role-petugas.png", "login.png", 16, 16);
        public static final ImageIcon ROLE_MASYARAKAT = loadIconWithFallback("users/role-masyarakat.png", "login.png", 16, 16);
        
        // Profile actions
        public static final ImageIcon EDIT_PROFILE = loadIconWithFallback("users/edit-profile.png", "edit.png", 20, 20);
        public static final ImageIcon CHANGE_PASSWORD = loadIconWithFallback("users/change-password.png", "login.png", 20, 20);
        public static final ImageIcon UPLOAD_PHOTO = loadIconWithFallback("users/upload-photo.png", "edit.png", 20, 20);
        public static final ImageIcon SAVE_PROFILE = loadIconWithFallback("users/save-profile.png", "edit.png", 20, 20);
        
        // Public data
        public static final ImageIcon PUBLIC_USERS = loadIconWithFallback("users/public-users.png", "login.png", 24, 24);
        public static final ImageIcon CITIZEN = loadIconWithFallback("users/citizen.png", "login.png", 20, 20);
        public static final ImageIcon DEMOGRAPHICS = loadIconWithFallback("users/demographics.png", "edit.png", 20, 20);
        
        // ============================= REPORTS =============================
        // Icons untuk reporting: GeneratorLaporan, LaporanFrame
        
        public static final ImageIcon REPORT = loadIconWithFallback("reports/report.png", "edit.png", 24, 24);
        public static final ImageIcon GENERATE = loadIconWithFallback("reports/generate.png", "edit.png", 20, 20);
        public static final ImageIcon PREVIEW = loadIconWithFallback("reports/preview.png", "edit.png", 20, 20);
        public static final ImageIcon DOWNLOAD = loadIconWithFallback("reports/download.png", "edit.png", 20, 20);
        
        // Report types
        public static final ImageIcon REPORT_SUMMARY = loadIconWithFallback("reports/report-summary.png", "edit.png", 20, 20);
        public static final ImageIcon REPORT_DETAILED = loadIconWithFallback("reports/report-detailed.png", "edit.png", 20, 20);
        public static final ImageIcon REPORT_CHART = loadIconWithFallback("reports/report-chart.png", "edit.png", 20, 20);
        public static final ImageIcon REPORT_TABLE = loadIconWithFallback("reports/report-table.png", "edit.png", 20, 20);
        
        // Export formats
        public static final ImageIcon PDF = loadIconWithFallback("reports/pdf.png", "edit.png", 18, 18);
        public static final ImageIcon EXCEL = loadIconWithFallback("reports/excel.png", "edit.png", 18, 18);
        public static final ImageIcon CSV = loadIconWithFallback("reports/csv.png", "edit.png", 18, 18);
        public static final ImageIcon PRINT = loadIconWithFallback("reports/print.png", "edit.png", 18, 18);
        public static final ImageIcon EXPORT_CSV = loadIconWithFallback("reports/export-csv.png", "edit.png", 18, 18);
        public static final ImageIcon EXPORT_PDF = loadIconWithFallback("reports/export-pdf.png", "edit.png", 18, 18);
        
        // Report viewer
        public static final ImageIcon REPORT_VIEW = loadIconWithFallback("reports/report-view.png", "edit.png", 24, 24);
        public static final ImageIcon ZOOM_IN = loadIconWithFallback("reports/zoom-in.png", "edit.png", 18, 18);
        public static final ImageIcon ZOOM_OUT = loadIconWithFallback("reports/zoom-out.png", "edit.png", 18, 18);
        public static final ImageIcon FULLSCREEN = loadIconWithFallback("reports/fullscreen.png", "edit.png", 18, 18);
        
        // ============================= FORMS =============================
        // Icons untuk form inputs: FormInputanAduan, FormDetailAduan
        
        public static final ImageIcon DOCUMENT = loadIconWithFallback("forms/document.png", "edit.png", 24, 24);
        public static final ImageIcon ATTACHMENT = loadIconWithFallback("forms/attachment.png", "edit.png", 18, 18);
        public static final ImageIcon CAMERA = loadIconWithFallback("forms/camera.png", "edit.png", 18, 18);
        public static final ImageIcon LOCATION = loadIconWithFallback("forms/location.png", "edit.png", 18, 18);
        public static final ImageIcon CATEGORY = loadIconWithFallback("forms/category.png", "edit.png", 18, 18);
        public static final ImageIcon COMMENT = loadIconWithFallback("forms/comment.png", "edit.png", 18, 18);
        public static final ImageIcon HISTORY = loadIconWithFallback("forms/history.png", "edit.png", 18, 18);
        
        // Form management
        public static final ImageIcon MANAGE = loadIconWithFallback("forms/manage.png", "edit.png", 24, 24);
        public static final ImageIcon WORKFLOW = loadIconWithFallback("forms/workflow.png", "edit.png", 20, 20);
        
        // ============================= CATEGORIES =============================
        // Icons untuk kategori aduan berdasarkan enum di Aduan.java
        
        public static final ImageIcon CATEGORY_INFRASTRUCTURE = loadIconWithFallback("categories/infrastructure.png", "edit.png", 20, 20);
        public static final ImageIcon CATEGORY_CLEANLINESS = loadIconWithFallback("categories/cleanliness.png", "edit.png", 20, 20);
        public static final ImageIcon CATEGORY_UTILITIES = loadIconWithFallback("categories/utilities.png", "edit.png", 20, 20);
        public static final ImageIcon CATEGORY_SECURITY = loadIconWithFallback("categories/security.png", "edit.png", 20, 20);
        public static final ImageIcon CATEGORY_HEALTH = loadIconWithFallback("categories/health.png", "edit.png", 20, 20);
        public static final ImageIcon CATEGORY_EDUCATION = loadIconWithFallback("categories/education.png", "edit.png", 20, 20);
        public static final ImageIcon CATEGORY_SOCIAL = loadIconWithFallback("categories/social.png", "edit.png", 20, 20);
        
        // ============================= SYSTEM =============================
        // Icons untuk system settings dan administration
        
        public static final ImageIcon SETTINGS = loadIconWithFallback("system/settings.png", "edit.png", 20, 20);
        public static final ImageIcon NOTIFICATION = loadIconWithFallback("system/notification.png", "edit.png", 18, 18);
        public static final ImageIcon SECURITY = loadIconWithFallback("system/security.png", "edit.png", 20, 20);
        public static final ImageIcon DATABASE = loadIconWithFallback("system/database.png", "edit.png", 20, 20);
        public static final ImageIcon NETWORK = loadIconWithFallback("system/network.png", "edit.png", 20, 20);
        public static final ImageIcon LOGS = loadIconWithFallback("system/logs.png", "edit.png", 18, 18);
        public static final ImageIcon HELP = loadIconWithFallback("system/help.png", "edit.png", 18, 18);
        
        // Profile & settings
        public static final ImageIcon PROFILE = loadIconWithFallback("system/profile.png", "login.png", 24, 24);
        public static final ImageIcon PERSONAL_INFO = loadIconWithFallback("system/personal-info.png", "login.png", 20, 20);
        public static final ImageIcon CONTACT_INFO = loadIconWithFallback("system/contact-info.png", "edit.png", 20, 20);
        public static final ImageIcon APPEARANCE = loadIconWithFallback("system/appearance.png", "edit.png", 20, 20);
        public static final ImageIcon PRIVACY = loadIconWithFallback("system/privacy.png", "edit.png", 20, 20);
        public static final ImageIcon BACKUP = loadIconWithFallback("system/backup.png", "edit.png", 20, 20);
        
        // Session info
        public static final ImageIcon SESSION = loadIconWithFallback("system/session.png", "login.png", 20, 20);
        public static final ImageIcon TIME = loadIconWithFallback("system/time.png", "edit.png", 18, 18);
        public static final ImageIcon ACTIVITY = loadIconWithFallback("system/activity.png", "edit.png", 18, 18);
        public static final ImageIcon IP_ADDRESS = loadIconWithFallback("system/ip-address.png", "edit.png", 18, 18);
        
        // System admin
        public static final ImageIcon SYSTEM = loadIconWithFallback("system/system.png", "edit.png", 24, 24);
        
        // ============================= WELCOME & MISC =============================
        // Icons untuk welcome forms dan miscellaneous
        
        public static final ImageIcon WELCOME = loadIconWithFallback("welcome/welcome.png", "home.png", 32, 32);
        public static final ImageIcon GETTING_STARTED = loadIconWithFallback("welcome/getting-started.png", "home.png", 24, 24);
        public static final ImageIcon FEATURES = loadIconWithFallback("welcome/features.png", "edit.png", 24, 24);
        
        // Dashboard metrics (legacy compatibility)
        public static final ImageIcon METRICS = loadIconWithFallback("dashboard/metrics.png", "home.png", 32, 32);
        public static final ImageIcon ANALYTICS = loadIconWithFallback("dashboard/analytics.png", "edit.png", 24, 24);
        public static final ImageIcon APPROVAL = loadIconWithFallback("dashboard/approval.png", "edit.png", 20, 20);
        
        // =========================== UTILITY METHODS ===========================
        
        /**
         * Get icon untuk status berdasarkan string value
         * @param status string status dari enum Aduan.Status
         * @return ImageIcon yang sesuai
         */
        public static ImageIcon getStatusIcon(String status) {
            if (status == null) return STATUS_NEW;
            
            switch (status.toLowerCase()) {
                case "baru": return STATUS_NEW;
                case "validasi": return STATUS_VALIDATION;
                case "proses": return STATUS_PROCESS;
                case "selesai": return STATUS_COMPLETED;
                case "ditolak": return STATUS_REJECTED;
                default: return STATUS_NEW;
            }
        }
        
        /**
         * Get icon untuk priority berdasarkan string value
         * @param priority string priority dari enum Aduan.Priority
         * @return ImageIcon yang sesuai
         */
        public static ImageIcon getPriorityIcon(String priority) {
            if (priority == null) return PRIORITY_MEDIUM;
            
            switch (priority.toLowerCase()) {
                case "rendah": return PRIORITY_LOW;
                case "sedang": return PRIORITY_MEDIUM;
                case "tinggi": return PRIORITY_HIGH;
                case "darurat": return PRIORITY_URGENT;
                default: return PRIORITY_MEDIUM;
            }
        }
        
        /**
         * Get icon untuk category berdasarkan string value
         * @param category string category dari enum Aduan.Category
         * @return ImageIcon yang sesuai
         */
        public static ImageIcon getCategoryIcon(String category) {
            if (category == null) return CATEGORY_INFRASTRUCTURE;
            
            switch (category.toLowerCase()) {
                case "infrastruktur": return CATEGORY_INFRASTRUCTURE;
                case "kebersihan": return CATEGORY_CLEANLINESS;
                case "utilitas": return CATEGORY_UTILITIES;
                case "keamanan": return CATEGORY_SECURITY;
                case "kesehatan": return CATEGORY_HEALTH;
                case "pendidikan": return CATEGORY_EDUCATION;
                case "sosial": return CATEGORY_SOCIAL;
                default: return CATEGORY_INFRASTRUCTURE;
            }
        }
    }
    
    /**
     * Test method untuk verify icon loading
     */
    public static void testIconLoading() {
        System.out.println("\n=== 🎨 SIPRIMA ICON LOADING TEST ===");
        
        // Test existing icons
        System.out.println("📁 Existing Icons:");
        System.out.println("  Eye icon: " + (Icons.EYE != null ? "✅" : "❌"));
        System.out.println("  Eye closed: " + (Icons.EYE_CLOSED != null ? "✅" : "❌"));
        System.out.println("  Home icon: " + (Icons.HOME != null ? "✅" : "❌"));
        System.out.println("  Edit icon: " + (Icons.EDIT != null ? "✅" : "❌"));
        System.out.println("  Login icon: " + (Icons.LOGIN != null ? "✅" : "❌"));
        
        // Test placeholder icons
        System.out.println("\n🔄 Placeholder Icons:");
        System.out.println("  Add icon: " + (Icons.ADD != null ? "✅" : "❌"));
        System.out.println("  Delete icon: " + (Icons.DELETE != null ? "✅" : "❌"));
        System.out.println("  Status new: " + (Icons.STATUS_NEW != null ? "✅" : "❌"));
        
        // Test utility methods
        System.out.println("\n⚙️ Utility Methods:");
        System.out.println("  Status icon (baru): " + (Icons.getStatusIcon("baru") != null ? "✅" : "❌"));
        System.out.println("  Priority icon (darurat): " + (Icons.getPriorityIcon("darurat") != null ? "✅" : "❌"));
        System.out.println("  Category icon (infrastruktur): " + (Icons.getCategoryIcon("infrastruktur") != null ? "✅" : "❌"));
        
        System.out.println("\n🎯 Icon loading test completed!");
        System.out.println("=======================================\n");
    }
    
    /**
     * Main method untuk testing
     */
    public static void main(String[] args) {
        testIconLoading();
    }
}

