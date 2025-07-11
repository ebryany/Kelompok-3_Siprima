/*
 * Custom Button Component untuk Siprima
 * Komponen tombol yang dapat dikustomisasi dengan berbagai style
 */
package Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * @author febry
 * Custom button dengan berbagai style untuk aplikasi Siprima
 */
public class CustomButton extends JButton {
    
    // Enum untuk tipe tombol
    public enum ButtonType {
        PRIMARY, SUCCESS, WARNING, DANGER, SECONDARY, INFO
    }
    
    // Enum untuk ukuran tombol
    public enum ButtonSize {
        SMALL, MEDIUM, LARGE
    }
    
    private ButtonType buttonType;
    private ButtonSize buttonSize;
    private int borderRadius;
    private boolean isRoundedCorners;
    private boolean hasHoverEffect;
    private boolean hasShadow;
    private Color currentBackgroundColor;
    private Color currentTextColor;
    private Color hoverColor;
    private Color pressedColor;
    
    /**
     * Constructor default
     */
    public CustomButton() {
        this("Button", ButtonType.PRIMARY, ButtonSize.MEDIUM);
    }
    
    /**
     * Constructor dengan text
     */
    public CustomButton(String text) {
        this(text, ButtonType.PRIMARY, ButtonSize.MEDIUM);
    }
    
    /**
     * Constructor dengan text dan type
     */
    public CustomButton(String text, ButtonType type) {
        this(text, type, ButtonSize.MEDIUM);
    }
    
    /**
     * Constructor lengkap
     */
    public CustomButton(String text, ButtonType type, ButtonSize size) {
        super(text);
        this.buttonType = type;
        this.buttonSize = size;
        this.borderRadius = 8;
        this.isRoundedCorners = true;
        this.hasHoverEffect = true;
        this.hasShadow = true;
        
        initializeButton();
        setupMouseListeners();
    }
    
    /**
     * Inisialisasi tombol
     */
    private void initializeButton() {
        // Hapus border default
        setBorder(null);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        
        // Set cursor
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Set warna berdasarkan type
        updateButtonColors();
        
        // Set ukuran berdasarkan size
        updateButtonDimensions();
        
        // Set font
        updateButtonFont();
    }
    
    /**
     * Set warna tombol berdasarkan type
     */
    private void updateButtonColors() {
        switch (buttonType) {
            case PRIMARY:
                currentBackgroundColor = SiprimaPalette.BTN_PRIMARY;
                hoverColor = SiprimaPalette.BTN_PRIMARY_HOVER;
                pressedColor = SiprimaPalette.darker(SiprimaPalette.BTN_PRIMARY, 0.2f);
                currentTextColor = SiprimaPalette.TEXT_WHITE;
                break;
            case SUCCESS:
                currentBackgroundColor = SiprimaPalette.BTN_SUCCESS;
                hoverColor = SiprimaPalette.BTN_SUCCESS_HOVER;
                pressedColor = SiprimaPalette.darker(SiprimaPalette.BTN_SUCCESS, 0.2f);
                currentTextColor = SiprimaPalette.TEXT_WHITE;
                break;
            case WARNING:
                currentBackgroundColor = SiprimaPalette.BTN_WARNING;
                hoverColor = SiprimaPalette.BTN_WARNING_HOVER;
                pressedColor = SiprimaPalette.darker(SiprimaPalette.BTN_WARNING, 0.2f);
                currentTextColor = SiprimaPalette.TEXT_WHITE;
                break;
            case DANGER:
                currentBackgroundColor = SiprimaPalette.BTN_DANGER;
                hoverColor = SiprimaPalette.BTN_DANGER_HOVER;
                pressedColor = SiprimaPalette.darker(SiprimaPalette.BTN_DANGER, 0.2f);
                currentTextColor = SiprimaPalette.TEXT_WHITE;
                break;
            case SECONDARY:
                currentBackgroundColor = SiprimaPalette.BTN_SECONDARY;
                hoverColor = SiprimaPalette.BTN_SECONDARY_HOVER;
                pressedColor = SiprimaPalette.darker(SiprimaPalette.BTN_SECONDARY, 0.2f);
                currentTextColor = SiprimaPalette.TEXT_WHITE;
                break;
            case INFO:
                currentBackgroundColor = SiprimaPalette.INFO;
                hoverColor = SiprimaPalette.darker(SiprimaPalette.INFO, 0.1f);
                pressedColor = SiprimaPalette.darker(SiprimaPalette.INFO, 0.2f);
                currentTextColor = SiprimaPalette.TEXT_WHITE;
                break;
        }
        
        setForeground(currentTextColor);
    }
    
    /**
     * Set ukuran tombol berdasarkan size
     */
    private void updateButtonDimensions() {
        Dimension size;
        switch (buttonSize) {
            case SMALL:
                size = new Dimension(80, 30);
                break;
            case MEDIUM:
                size = new Dimension(120, 40);
                break;
            case LARGE:
                size = new Dimension(160, 50);
                break;
            default:
                size = new Dimension(120, 40);
        }
        
        setPreferredSize(size);
        setMinimumSize(size);
    }
    
    /**
     * Set font tombol
     */
    private void updateButtonFont() {
        Font font;
        switch (buttonSize) {
            case SMALL:
                font = new Font("SansSerif", Font.BOLD, 12);
                break;
            case MEDIUM:
                font = new Font("SansSerif", Font.BOLD, 14);
                break;
            case LARGE:
                font = new Font("SansSerif", Font.BOLD, 16);
                break;
            default:
                font = new Font("SansSerif", Font.BOLD, 14);
        }
        
        setFont(font);
    }
    
    /**
     * Setup mouse listeners untuk efek hover
     */
    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (hasHoverEffect && isEnabled()) {
                    repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (hasHoverEffect && isEnabled()) {
                    repaint();
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    repaint();
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Tentukan warna background berdasarkan state
        Color bgColor = currentBackgroundColor;
        
        if (!isEnabled()) {
            bgColor = SiprimaPalette.withAlpha(currentBackgroundColor, 100);
        } else if (getModel().isPressed()) {
            bgColor = pressedColor;
        } else if (getModel().isRollover() && hasHoverEffect) {
            bgColor = hoverColor;
        }
        
        // Gambar shadow jika diaktifkan
        if (hasShadow && isEnabled()) {
            g2d.setColor(SiprimaPalette.CARD_SHADOW);
            if (isRoundedCorners) {
                g2d.fill(new RoundRectangle2D.Double(2, 2, getWidth() - 2, getHeight() - 2, borderRadius, borderRadius));
            } else {
                g2d.fillRect(2, 2, getWidth() - 2, getHeight() - 2);
            }
        }
        
        // Gambar background
        g2d.setColor(bgColor);
        if (isRoundedCorners) {
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - (hasShadow ? 2 : 0), getHeight() - (hasShadow ? 2 : 0), borderRadius, borderRadius));
        } else {
            g2d.fillRect(0, 0, getWidth() - (hasShadow ? 2 : 0), getHeight() - (hasShadow ? 2 : 0));
        }
        
        // Gambar text
        g2d.setColor(isEnabled() ? currentTextColor : SiprimaPalette.withAlpha(currentTextColor, 150));
        g2d.setFont(getFont());
        
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getHeight();
        
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() - textHeight) / 2 + fm.getAscent();
        
        g2d.drawString(getText(), x, y);
        
        g2d.dispose();
    }
    
    // ===================== GETTER & SETTER =====================
    
    public ButtonType getButtonType() {
        return buttonType;
    }
    
    public void setButtonType(ButtonType buttonType) {
        this.buttonType = buttonType;
        updateButtonColors();
        repaint();
    }
    
    public ButtonSize getButtonSize() {
        return buttonSize;
    }
    
    public void setButtonSize(ButtonSize buttonSize) {
        this.buttonSize = buttonSize;
        updateButtonDimensions();
        updateButtonFont();
        repaint();
    }
    
    public int getBorderRadius() {
        return borderRadius;
    }
    
    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint();
    }
    
    public boolean isRoundedCorners() {
        return isRoundedCorners;
    }
    
    public void setRoundedCorners(boolean roundedCorners) {
        isRoundedCorners = roundedCorners;
        repaint();
    }
    
    public boolean isHasHoverEffect() {
        return hasHoverEffect;
    }
    
    public void setHasHoverEffect(boolean hasHoverEffect) {
        this.hasHoverEffect = hasHoverEffect;
    }
    
    public boolean isHasShadow() {
        return hasShadow;
    }
    
    public void setHasShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
        repaint();
    }
    
    /**
     * Set custom background color
     */
    public void setCustomBackgroundColor(Color color) {
        this.currentBackgroundColor = color;
        this.hoverColor = SiprimaPalette.brighter(color, 0.1f);
        this.pressedColor = SiprimaPalette.darker(color, 0.1f);
        repaint();
    }
    
    /**
     * Set custom text color
     */
    public void setCustomTextColor(Color color) {
        this.currentTextColor = color;
        setForeground(color);
        repaint();
    }
}

