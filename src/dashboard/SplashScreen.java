/*
 * SIPRIMA Modern Splash Screen
 * Loading screen dengan animasi sebelum dashboard muncul
 */
package dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Modern splash screen dengan loading animation
 * @author febry
 */
public class SplashScreen extends JWindow {
    
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color LIGHT_BLUE = new Color(52, 152, 219);
    private static final Color SUCCESS_GREEN = new Color(46, 204, 113);
    
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel logoLabel;
    private Timer animationTimer;
    private int progress = 0;
    
    public SplashScreen() {
        createSplashScreen();
    }
    
    private void createSplashScreen() {
        setSize(500, 350);
        setLocationRelativeTo(null);
        
        // Main panel dengan gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_BLUE,
                    0, getHeight(), LIGHT_BLUE
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Decorative circles
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                g2d.setColor(Color.WHITE);
                g2d.fillOval(-50, -50, 200, 200);
                g2d.fillOval(getWidth() - 150, getHeight() - 150, 200, 200);
                
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Logo dan title panel
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));
        
        // Logo SIPRIMA
        logoLabel = new JLabel("🏛️ SIPRIMA", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        logoLabel.setForeground(Color.WHITE);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Sistem Pengaduan Masyarakat", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        
        // Desa info
        JLabel desaLabel = new JLabel("Desa Tarabbi", SwingConstants.CENTER);
        desaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        desaLabel.setForeground(new Color(255, 255, 255, 180));
        
        logoPanel.add(logoLabel, BorderLayout.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(subtitleLabel);
        textPanel.add(desaLabel);
        logoPanel.add(textPanel, BorderLayout.SOUTH);
        
        // Loading panel
        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.setOpaque(false);
        loadingPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 50, 60));
        
        // Custom progress bar
        progressBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Progress fill
                int fillWidth = (int) ((getPercentComplete() * getWidth()));
                GradientPaint progressGradient = new GradientPaint(
                    0, 0, SUCCESS_GREEN,
                    fillWidth, 0, new Color(39, 174, 96)
                );
                g2d.setPaint(progressGradient);
                g2d.fillRoundRect(0, 0, fillWidth, getHeight(), 15, 15);
                
                // Progress text
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String text = getValue() + "%";
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(text)) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2;
                g2d.drawString(text, textX, textY);
                
                g2d.dispose();
            }
        };
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 25));
        
        // Status label
        statusLabel = new JLabel("Memulai aplikasi...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(255, 255, 255, 180));
        
        loadingPanel.add(progressBar, BorderLayout.CENTER);
        loadingPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Version info
        JLabel versionLabel = new JLabel("v1.0.0 - Build 2025.06", SwingConstants.CENTER);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(255, 255, 255, 120));
        versionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Layout
        mainPanel.add(logoPanel, BorderLayout.CENTER);
        mainPanel.add(loadingPanel, BorderLayout.SOUTH);
        mainPanel.add(versionLabel, BorderLayout.PAGE_END);
        
        add(mainPanel);
    }
    
    public void startLoading(Runnable onComplete) {
        setVisible(true);
        
        String[] loadingSteps = {
            "Memuat konfigurasi sistem...",
            "Menghubungkan ke database...", 
            "Memverifikasi sesi pengguna...",
            "Memuat data dashboard...",
            "Menyiapkan komponen UI...",
            "Mengaktifkan realtime updates...",
            "Finalisasi...",
            "Selesai! Selamat datang! 🎉"
        };
        
        animationTimer = new Timer(300, e -> {
            progress += 12;
            progressBar.setValue(Math.min(progress, 100));
            
            int step = Math.min(progress / 12, loadingSteps.length - 1);
            statusLabel.setText(loadingSteps[step]);
            
            // Logo animation
            if (progress % 24 == 0) {
                animateLogo();
            }
            
            if (progress >= 100) {
                animationTimer.stop();
                
                // Fade out animation
                Timer fadeTimer = new Timer(50, fadeEvent -> {
                    float opacity = getOpacity() - 0.05f;
                    if (opacity <= 0) {
                        setVisible(false);
                        dispose();
                        ((Timer)fadeEvent.getSource()).stop();
                        if (onComplete != null) {
                            SwingUtilities.invokeLater(onComplete);
                        }
                    } else {
                        setOpacity(opacity);
                    }
                });
                fadeTimer.start();
            }
        });
        
        animationTimer.start();
    }
    
    private void animateLogo() {
        // Simple scale animation
        Timer scaleTimer = new Timer(10, null);
        final int[] scale = {100};
        final boolean[] growing = {true};
        
        scaleTimer.addActionListener(e -> {
            if (growing[0]) {
                scale[0] += 2;
                if (scale[0] >= 110) growing[0] = false;
            } else {
                scale[0] -= 2;
                if (scale[0] <= 100) {
                    growing[0] = true;
                    scaleTimer.stop();
                }
            }
            
            float scaleF = scale[0] / 100.0f;
            Font currentFont = logoLabel.getFont();
            Font newFont = currentFont.deriveFont(36.0f * scaleF);
            logoLabel.setFont(newFont);
            logoLabel.repaint();
        });
        
        scaleTimer.start();
    }
}

