/*
 * Custom Panel Component untuk Siprima
 * Panel dengan styling yang konsisten dengan tema aplikasi
 */
package Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * @author febry
 * Custom panel untuk aplikasi Siprima
 */
public class CustomPanel extends JPanel {
    
    // Enum untuk tipe panel
    public enum PanelType {
        CARD, SIDEBAR, HEADER, CONTENT, FORM
    }
    
    private PanelType panelType;
    private boolean isRounded;
    private int borderRadius;
    private boolean hasShadow;
    private boolean hasGradient;
    private Color gradientStartColor;
    private Color gradientEndColor;
    private Color borderColor;
    private int borderWidth;
    
    /**
     * Constructor default
     */
    public CustomPanel() {
        this(PanelType.CONTENT);
    }
    
    /**
     * Constructor dengan tipe panel
     */
    public CustomPanel(PanelType type) {
        super();
        this.panelType = type;
        this.isRounded = true;
        this.borderRadius = 12;
        this.hasShadow = false;
        this.hasGradient = false;
        this.borderWidth = 0;
        
        initializePanel();
    }
    
    /**
     * Inisialisasi panel
     */
    private void initializePanel() {
        setOpaque(false); // Untuk custom painting
        setPanelStyle();
    }
    
    /**
     * Set style panel berdasarkan type
     */
    private void setPanelStyle() {
        switch (panelType) {
            case CARD:
                setBackground(SiprimaPalette.CARD_BACKGROUND);
                setBorderColor(SiprimaPalette.CARD_BORDER);
                setBorderWidth(1);
                setHasShadow(true);
                setRounded(true);
                setBorderRadius(12);
                break;
                
            case SIDEBAR:
                setBackground(SiprimaPalette.BG_SIDEBAR);
                setRounded(false);
                setHasShadow(false);
                break;
                
            case HEADER:
                setBackground(SiprimaPalette.BG_HEADER);
                setHasGradient(true);
                setGradientStartColor(SiprimaPalette.GRADIENT_START);
                setGradientEndColor(SiprimaPalette.GRADIENT_END);
                setRounded(false);
                setHasShadow(true);
                break;
                
            case CONTENT:
                setBackground(SiprimaPalette.BG_PRIMARY);
                setRounded(false);
                setHasShadow(false);
                break;
                
            case FORM:
                setBackground(SiprimaPalette.BG_SECONDARY);
                setBorderColor(SiprimaPalette.BORDER_LIGHT);
                setBorderWidth(1);
                setRounded(true);
                setBorderRadius(8);
                setHasShadow(false);
                break;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Gambar shadow jika diaktifkan
        if (hasShadow) {
            g2d.setColor(SiprimaPalette.CARD_SHADOW);
            if (isRounded) {
                g2d.fill(new RoundRectangle2D.Double(3, 3, width - 3, height - 3, borderRadius, borderRadius));
            } else {
                g2d.fillRect(3, 3, width - 3, height - 3);
            }
        }
        
        // Gambar background
        if (hasGradient && gradientStartColor != null && gradientEndColor != null) {
            // Gradient background
            GradientPaint gradient = new GradientPaint(0, 0, gradientStartColor, width, 0, gradientEndColor);
            g2d.setPaint(gradient);
        } else {
            // Solid background
            g2d.setColor(getBackground());
        }
        
        if (isRounded) {
            g2d.fill(new RoundRectangle2D.Double(0, 0, width - (hasShadow ? 3 : 0), height - (hasShadow ? 3 : 0), borderRadius, borderRadius));
        } else {
            g2d.fillRect(0, 0, width - (hasShadow ? 3 : 0), height - (hasShadow ? 3 : 0));
        }
        
        // Gambar border jika ada
        if (borderWidth > 0 && borderColor != null) {
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(borderWidth));
            
            if (isRounded) {
                g2d.draw(new RoundRectangle2D.Double(borderWidth/2.0, borderWidth/2.0, 
                    width - borderWidth - (hasShadow ? 3 : 0), height - borderWidth - (hasShadow ? 3 : 0), 
                    borderRadius, borderRadius));
            } else {
                g2d.drawRect(borderWidth/2, borderWidth/2, 
                    width - borderWidth - (hasShadow ? 3 : 0), height - borderWidth - (hasShadow ? 3 : 0));
            }
        }
        
        g2d.dispose();
        super.paintComponent(g);
    }
    
    // ===================== GETTER & SETTER =====================
    
    public PanelType getPanelType() {
        return panelType;
    }
    
    public void setPanelType(PanelType panelType) {
        this.panelType = panelType;
        setPanelStyle();
        repaint();
    }
    
    public boolean isRounded() {
        return isRounded;
    }
    
    public void setRounded(boolean rounded) {
        isRounded = rounded;
        repaint();
    }
    
    public int getBorderRadius() {
        return borderRadius;
    }
    
    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint();
    }
    
    public boolean isHasShadow() {
        return hasShadow;
    }
    
    public void setHasShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
        repaint();
    }
    
    public boolean isHasGradient() {
        return hasGradient;
    }
    
    public void setHasGradient(boolean hasGradient) {
        this.hasGradient = hasGradient;
        repaint();
    }
    
    public Color getGradientStartColor() {
        return gradientStartColor;
    }
    
    public void setGradientStartColor(Color gradientStartColor) {
        this.gradientStartColor = gradientStartColor;
        repaint();
    }
    
    public Color getGradientEndColor() {
        return gradientEndColor;
    }
    
    public void setGradientEndColor(Color gradientEndColor) {
        this.gradientEndColor = gradientEndColor;
        repaint();
    }
    
    public Color getBorderColor() {
        return borderColor;
    }
    
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }
    
    public int getBorderWidth() {
        return borderWidth;
    }
    
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        repaint();
    }
    
    /**
     * Set gradient colors sekaligus
     */
    public void setGradient(Color startColor, Color endColor) {
        setGradientStartColor(startColor);
        setGradientEndColor(endColor);
        setHasGradient(true);
    }
    
    /**
     * Hapus gradient
     */
    public void removeGradient() {
        setHasGradient(false);
        setGradientStartColor(null);
        setGradientEndColor(null);
    }
}

