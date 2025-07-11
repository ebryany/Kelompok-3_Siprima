/*
 * Theme Manager untuk Siprima
 * Kelas untuk mengelola tema dan styling aplikasi secara terpusat
 */
package Theme;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

/**
 * @author febry
 * Manager untuk mengatur tema aplikasi Siprima
 */
public class ThemeManager {
    
    private static boolean isDarkMode = false;
    
    /**
     * Inisialisasi tema aplikasi
     */
    public static void initializeTheme() {
        try {
            // Set Look and Feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Apply custom colors to UIManager
            applyCustomUIDefaults();
            
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback ke Nimbus jika system L&F gagal
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Apply custom UI defaults
     */
    private static void applyCustomUIDefaults() {
        // Button defaults
        UIManager.put("Button.background", new ColorUIResource(SiprimaPalette.BTN_PRIMARY));
        UIManager.put("Button.foreground", new ColorUIResource(SiprimaPalette.TEXT_WHITE));
        UIManager.put("Button.select", new ColorUIResource(SiprimaPalette.BTN_PRIMARY_HOVER));
        UIManager.put("Button.focus", new ColorUIResource(SiprimaPalette.FOCUS_PRIMARY));
        
        // TextField defaults
        UIManager.put("TextField.background", new ColorUIResource(SiprimaPalette.INPUT_BACKGROUND));
        UIManager.put("TextField.foreground", new ColorUIResource(SiprimaPalette.TEXT_PRIMARY));
        UIManager.put("TextField.caretForeground", new ColorUIResource(SiprimaPalette.TEXT_PRIMARY));
        UIManager.put("TextField.selectionBackground", new ColorUIResource(SiprimaPalette.HOVER_PRIMARY));
        UIManager.put("TextField.selectionForeground", new ColorUIResource(SiprimaPalette.TEXT_PRIMARY));
        
        // Panel defaults
        UIManager.put("Panel.background", new ColorUIResource(SiprimaPalette.BG_PRIMARY));
        UIManager.put("Panel.foreground", new ColorUIResource(SiprimaPalette.TEXT_PRIMARY));
        
        // Table defaults
        UIManager.put("Table.background", new ColorUIResource(SiprimaPalette.TABLE_ROW_EVEN));
        UIManager.put("Table.foreground", new ColorUIResource(SiprimaPalette.TEXT_PRIMARY));
        UIManager.put("Table.selectionBackground", new ColorUIResource(SiprimaPalette.TABLE_SELECTED));
        UIManager.put("Table.selectionForeground", new ColorUIResource(SiprimaPalette.TEXT_PRIMARY));
        UIManager.put("TableHeader.background", new ColorUIResource(SiprimaPalette.TABLE_HEADER));
        UIManager.put("TableHeader.foreground", new ColorUIResource(SiprimaPalette.TEXT_WHITE));
        
        // ScrollPane defaults
        UIManager.put("ScrollPane.background", new ColorUIResource(SiprimaPalette.BG_PRIMARY));
        UIManager.put("ScrollBar.background", new ColorUIResource(SiprimaPalette.LIGHT_GRAY));
        UIManager.put("ScrollBar.thumb", new ColorUIResource(SiprimaPalette.MEDIUM_GRAY));
        UIManager.put("ScrollBar.thumbHighlight", new ColorUIResource(SiprimaPalette.PRIMARY_BLUE));
        
        // ComboBox defaults
        UIManager.put("ComboBox.background", new ColorUIResource(SiprimaPalette.INPUT_BACKGROUND));
        UIManager.put("ComboBox.foreground", new ColorUIResource(SiprimaPalette.TEXT_PRIMARY));
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(SiprimaPalette.HOVER_PRIMARY));
        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(SiprimaPalette.TEXT_PRIMARY));
        
        // Menu defaults
        UIManager.put("MenuBar.background", new ColorUIResource(SiprimaPalette.BG_HEADER));
        UIManager.put("MenuBar.foreground", new ColorUIResource(SiprimaPalette.TEXT_WHITE));
        UIManager.put("Menu.background", new ColorUIResource(SiprimaPalette.BG_HEADER));
        UIManager.put("Menu.foreground", new ColorUIResource(SiprimaPalette.TEXT_WHITE));
        UIManager.put("MenuItem.background", new ColorUIResource(SiprimaPalette.BG_SECONDARY));
        UIManager.put("MenuItem.foreground", new ColorUIResource(SiprimaPalette.TEXT_PRIMARY));
        UIManager.put("MenuItem.selectionBackground", new ColorUIResource(SiprimaPalette.HOVER_PRIMARY));
        
        // Label defaults
        UIManager.put("Label.foreground", new ColorUIResource(SiprimaPalette.TEXT_PRIMARY));
        
        // Border defaults
        UIManager.put("TextField.border", BorderFactory.createLineBorder(SiprimaPalette.INPUT_BORDER));
        UIManager.put("ComboBox.border", BorderFactory.createLineBorder(SiprimaPalette.INPUT_BORDER));
    }
    
    /**
     * Toggle dark mode (untuk implementasi future)
     */
    public static void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            applyDarkTheme();
        } else {
            applyLightTheme();
        }
        
        // Update semua window yang terbuka
        SwingUtilities.invokeLater(() -> {
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
        });
    }
    
    /**
     * Apply dark theme
     */
    private static void applyDarkTheme() {
        // Dark theme implementation - bisa dikembangkan di masa depan
        UIManager.put("Panel.background", new ColorUIResource(SiprimaPalette.CHARCOAL));
        UIManager.put("TextField.background", new ColorUIResource(SiprimaPalette.DARK_GRAY));
        UIManager.put("Label.foreground", new ColorUIResource(SiprimaPalette.TEXT_WHITE));
    }
    
    /**
     * Apply light theme
     */
    private static void applyLightTheme() {
        applyCustomUIDefaults();
    }
    
    /**
     * Buat JFrame dengan styling Siprima
     */
    public static JFrame createStyledFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(SiprimaPalette.BG_PRIMARY);
        
        // Set icon jika ada
        // frame.setIconImage(loadIcon());
        
        return frame;
    }
    
    /**
     * Buat JDialog dengan styling Siprima
     */
    public static JDialog createStyledDialog(Frame parent, String title) {
        JDialog dialog = new JDialog(parent, title, true);
        dialog.setBackground(SiprimaPalette.BG_PRIMARY);
        return dialog;
    }
    
    /**
     * Style komponen secara batch
     */
    public static void styleComponent(Component component) {
        if (component instanceof JButton) {
            styleButton((JButton) component);
        } else if (component instanceof JTextField) {
            styleTextField((JTextField) component);
        } else if (component instanceof JLabel) {
            styleLabel((JLabel) component);
        } else if (component instanceof JPanel) {
            stylePanel((JPanel) component);
        }
        
        // Style children components
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                styleComponent(child);
            }
        }
    }
    
    /**
     * Style button dengan tema Siprima
     */
    public static void styleButton(JButton button) {
        button.setBackground(SiprimaPalette.BTN_PRIMARY);
        button.setForeground(SiprimaPalette.TEXT_WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Style text field dengan tema Siprima
     */
    public static void styleTextField(JTextField textField) {
        textField.setBackground(SiprimaPalette.INPUT_BACKGROUND);
        textField.setForeground(SiprimaPalette.TEXT_PRIMARY);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SiprimaPalette.INPUT_BORDER),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        textField.setCaretColor(SiprimaPalette.TEXT_PRIMARY);
        textField.setSelectionColor(SiprimaPalette.HOVER_PRIMARY);
    }
    
    /**
     * Style label dengan tema Siprima
     */
    public static void styleLabel(JLabel label) {
        label.setForeground(SiprimaPalette.TEXT_PRIMARY);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }
    
    /**
     * Style panel dengan tema Siprima
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(SiprimaPalette.BG_PRIMARY);
    }
    
    /**
     * Buat header panel untuk aplikasi
     */
    public static JPanel createHeaderPanel(String title) {
        CustomPanel headerPanel = new CustomPanel(CustomPanel.PanelType.HEADER);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(SiprimaPalette.TEXT_WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    /**
     * Buat sidebar panel untuk navigasi
     */
    public static JPanel createSidebarPanel() {
        CustomPanel sidebarPanel = new CustomPanel(CustomPanel.PanelType.SIDEBAR);
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        
        return sidebarPanel;
    }
    
    /**
     * Buat content panel utama
     */
    public static JPanel createContentPanel() {
        CustomPanel contentPanel = new CustomPanel(CustomPanel.PanelType.CONTENT);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        return contentPanel;
    }
    
    /**
     * Buat card panel untuk konten
     */
    public static JPanel createCardPanel() {
        CustomPanel cardPanel = new CustomPanel(CustomPanel.PanelType.CARD);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        return cardPanel;
    }
    
    // ===================== GETTER & SETTER =====================
    
    public static boolean isDarkMode() {
        return isDarkMode;
    }
    
    /**
     * Get primary color
     */
    public static Color getPrimaryColor() {
        return SiprimaPalette.PRIMARY_BLUE;
    }
    
    /**
     * Get secondary color
     */
    public static Color getSecondaryColor() {
        return SiprimaPalette.PRIMARY_GREEN;
    }
    
    /**
     * Get background color
     */
    public static Color getBackgroundColor() {
        return SiprimaPalette.BG_PRIMARY;
    }
    
    /**
     * Get text color
     */
    public static Color getTextColor() {
        return SiprimaPalette.TEXT_PRIMARY;
    }
    
    /**
     * Get system look and feel
     */
    public static String getSystemLookAndFeel() {
        return UIManager.getSystemLookAndFeelClassName();
    }
}

