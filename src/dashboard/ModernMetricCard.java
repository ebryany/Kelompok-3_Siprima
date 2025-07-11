/*
 * Modern Interactive Metric Card untuk SIPRIMA Dashboard
 */
package dashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Modern metric card dengan animasi dan interaksi
 * @author febry
 */
public class ModernMetricCard extends JPanel {
    
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private static final Color WARNING_ORANGE = new Color(243, 156, 18);
    private static final Color ERROR_RED = new Color(231, 76, 60);
    
    private String title;
    private String value;
    private String trend;
    private String icon;
    private Color accentColor;
    private boolean isHovered = false;
    private float shadowOpacity = 0.1f;
    private Timer hoverTimer;
    
    public ModernMetricCard(String title, String value, String trend, String icon, Color accentColor) {
        this.title = title;
        this.value = value;
        this.trend = trend;
        this.icon = icon;
        this.accentColor = accentColor;
        
        setupCard();
        setupAnimations();
    }
    
    private void setupCard() {
        setPreferredSize(new Dimension(280, 140));
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Layout
        setLayout(new BorderLayout(15, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Icon panel
        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(60, 60));
        
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setForeground(accentColor);
        
        // Icon background circle
        JPanel iconBg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Circle background
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20));
                g2d.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
                
                // Circle border
                g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 50));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(5, 5, getWidth() - 10, getHeight() - 10);
                
                g2d.dispose();
            }
        };
        iconBg.setLayout(new BorderLayout());
        iconBg.setOpaque(false);
        iconBg.add(iconLabel, BorderLayout.CENTER);
        
        iconPanel.add(iconBg, BorderLayout.CENTER);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, 5));
        contentPanel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        // Value dengan animasi counter
        JLabel valueLabel = new JLabel(value) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Text shadow effect
                if (isHovered) {
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.drawString(getText(), 1, getFont().getSize() + 1);
                }
                
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(new Color(44, 62, 80));
        
        // Trend
        JLabel trendLabel = new JLabel(trend);
        trendLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        trendLabel.setForeground(new Color(150, 150, 150));
        
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(valueLabel, BorderLayout.CENTER);
        contentPanel.add(trendLabel, BorderLayout.SOUTH);
        
        add(iconPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void setupAnimations() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                animateHover(true);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                animateHover(false);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                animateClick();
            }
        });
    }
    
    private void animateHover(boolean enter) {
        if (hoverTimer != null) hoverTimer.stop();
        
        hoverTimer = new Timer(10, e -> {
            if (enter) {
                shadowOpacity = Math.min(0.3f, shadowOpacity + 0.02f);
            } else {
                shadowOpacity = Math.max(0.1f, shadowOpacity - 0.02f);
            }
            
            repaint();
            
            if ((enter && shadowOpacity >= 0.3f) || (!enter && shadowOpacity <= 0.1f)) {
                hoverTimer.stop();
            }
        });
        
        hoverTimer.start();
    }
    
    private void animateClick() {
        // Quick scale animation
        Timer clickTimer = new Timer(5, null);
        final int[] steps = {0};
        final int maxSteps = 10;
        
        clickTimer.addActionListener(e -> {
            steps[0]++;
            float progress = (float) steps[0] / maxSteps;
            
            // Scale down then up
            float scale = steps[0] <= maxSteps/2 ? 
                1.0f - (progress * 0.05f) : 
                0.95f + ((progress - 0.5f) * 0.1f);
            
            // Apply transform (simplified - just repaint with effect)
            if (steps[0] >= maxSteps) {
                clickTimer.stop();
            }
            
            repaint();
        });
        
        clickTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Drop shadow
        if (shadowOpacity > 0.1f) {
            g2d.setColor(new Color(0, 0, 0, (int)(shadowOpacity * 100)));
            g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 15, 15);
        }
        
        // Card background
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 15, 15);
        
        // Accent border (left side)
        g2d.setColor(accentColor);
        g2d.fillRoundRect(0, 0, 4, getHeight() - 3, 4, 4);
        
        // Subtle border
        g2d.setColor(new Color(230, 230, 230));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 15, 15);
        
        // Hover glow effect
        if (isHovered) {
            g2d.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 15, 15);
        }
        
        g2d.dispose();
        super.paintComponent(g);
    }
    
    // Update methods
    public void updateValue(String newValue) {
        this.value = newValue;
        // Find value label and update
        updateValueLabel(newValue);
    }
    
    public void updateTrend(String newTrend) {
        this.trend = newTrend;
        // Find trend label and update
        updateTrendLabel(newTrend);
    }
    
    private void updateValueLabel(String newValue) {
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                updateLabelsRecursively((JPanel) comp, "value", newValue);
            }
        }
    }
    
    private void updateTrendLabel(String newTrend) {
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                updateLabelsRecursively((JPanel) comp, "trend", newTrend);
            }
        }
    }
    
    private void updateLabelsRecursively(JPanel panel, String type, String newText) {
        Component[] components = panel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                Font font = label.getFont();
                
                if ("value".equals(type) && font.getSize() > 20) {
                    // Animate value change
                    animateValueChange(label, newText);
                } else if ("trend".equals(type) && font.getSize() < 15) {
                    label.setText(newText);
                }
            } else if (comp instanceof JPanel) {
                updateLabelsRecursively((JPanel) comp, type, newText);
            }
        }
    }
    
    private void animateValueChange(JLabel label, String newValue) {
        // Simple fade and update animation
        Timer fadeTimer = new Timer(10, null);
        final float[] opacity = {1.0f};
        final boolean[] fadingOut = {true};
        
        fadeTimer.addActionListener(e -> {
            if (fadingOut[0]) {
                opacity[0] -= 0.05f;
                if (opacity[0] <= 0.3f) {
                    fadingOut[0] = false;
                    label.setText(newValue);
                }
            } else {
                opacity[0] += 0.05f;
                if (opacity[0] >= 1.0f) {
                    opacity[0] = 1.0f;
                    fadeTimer.stop();
                }
            }
            
            // Apply opacity effect
            Color currentColor = label.getForeground();
            label.setForeground(new Color(
                currentColor.getRed(),
                currentColor.getGreen(), 
                currentColor.getBlue(),
                (int)(255 * opacity[0])
            ));
        });
        
        fadeTimer.start();
    }
}

