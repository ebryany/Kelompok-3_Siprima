/*
 * Custom TextField Component untuk Siprima
 * Text field dengan styling yang konsisten dengan tema aplikasi
 */
package Theme;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * @author febry
 * Custom text field untuk aplikasi Siprima
 */
public class CustomTextField extends JTextField {
    
    private String placeholderText;
    private Color placeholderColor;
    private Color focusColor;
    private Color borderColor;
    private boolean isRounded;
    private int borderRadius;
    private boolean showFocusEffect;
    private boolean hasError;
    private String errorMessage;
    
    /**
     * Constructor default
     */
    public CustomTextField() {
        this("");
    }
    
    /**
     * Constructor dengan placeholder
     */
    public CustomTextField(String placeholder) {
        super();
        this.placeholderText = placeholder;
        this.placeholderColor = SiprimaPalette.INPUT_PLACEHOLDER;
        this.focusColor = SiprimaPalette.INPUT_FOCUS;
        this.borderColor = SiprimaPalette.INPUT_BORDER;
        this.isRounded = true;
        this.borderRadius = 8;
        this.showFocusEffect = true;
        this.hasError = false;
        this.errorMessage = "";
        
        initializeTextField();
        setupFocusListeners();
    }
    
    /**
     * Inisialisasi text field
     */
    private void initializeTextField() {
        // Set background dan warna
        setBackground(SiprimaPalette.INPUT_BACKGROUND);
        setForeground(SiprimaPalette.TEXT_PRIMARY);
        
        // Set font
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Set padding
        setBorder(new RoundedBorder());
        
        // Set preferred size
        setPreferredSize(new Dimension(200, 40));
        
        // Set caret color
        setCaretColor(SiprimaPalette.TEXT_PRIMARY);
        setSelectionColor(SiprimaPalette.HOVER_PRIMARY);
        setSelectedTextColor(SiprimaPalette.TEXT_PRIMARY);
    }
    
    /**
     * Setup focus listeners
     */
    private void setupFocusListeners() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showFocusEffect) {
                    repaint();
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (showFocusEffect) {
                    repaint();
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Gambar background
        g2d.setColor(getBackground());
        if (isRounded) {
            g2d.fill(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, borderRadius, borderRadius));
        } else {
            g2d.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
        }
        
        g2d.dispose();
        
        super.paintComponent(g);
        
        // Gambar placeholder jika field kosong dan tidak focus
        if (getText().isEmpty() && !hasFocus() && placeholderText != null && !placeholderText.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(placeholderColor);
            g2.setFont(getFont());
            
            FontMetrics fm = g2.getFontMetrics();
            Insets insets = getInsets();
            int x = insets.left + 5;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            
            g2.drawString(placeholderText, x, y);
            g2.dispose();
        }
    }
    
    /**
     * Custom border untuk text field
     */
    private class RoundedBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color currentBorderColor;
            int borderWidth = 1;
            
            if (hasError) {
                currentBorderColor = SiprimaPalette.INPUT_ERROR;
                borderWidth = 2;
            } else if (hasFocus() && showFocusEffect) {
                currentBorderColor = focusColor;
                borderWidth = 2;
            } else {
                currentBorderColor = borderColor;
            }
            
            g2d.setColor(currentBorderColor);
            g2d.setStroke(new BasicStroke(borderWidth));
            
            if (isRounded) {
                g2d.draw(new RoundRectangle2D.Double(x + borderWidth/2.0, y + borderWidth/2.0, 
                    width - borderWidth, height - borderWidth, borderRadius, borderRadius));
            } else {
                g2d.drawRect(x + borderWidth/2, y + borderWidth/2, width - borderWidth, height - borderWidth);
            }
            
            g2d.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 12, 8, 12);
        }
        
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = 8;
            insets.left = 12;
            insets.bottom = 8;
            insets.right = 12;
            return insets;
        }
    }
    
    // ===================== GETTER & SETTER =====================
    
    public String getPlaceholderText() {
        return placeholderText;
    }
    
    public void setPlaceholderText(String placeholderText) {
        this.placeholderText = placeholderText;
        repaint();
    }
    
    public Color getPlaceholderColor() {
        return placeholderColor;
    }
    
    public void setPlaceholderColor(Color placeholderColor) {
        this.placeholderColor = placeholderColor;
        repaint();
    }
    
    public Color getFocusColor() {
        return focusColor;
    }
    
    public void setFocusColor(Color focusColor) {
        this.focusColor = focusColor;
        repaint();
    }
    
    public Color getBorderColor() {
        return borderColor;
    }
    
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
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
    
    public boolean isShowFocusEffect() {
        return showFocusEffect;
    }
    
    public void setShowFocusEffect(boolean showFocusEffect) {
        this.showFocusEffect = showFocusEffect;
    }
    
    public boolean isHasError() {
        return hasError;
    }
    
    public void setHasError(boolean hasError) {
        this.hasError = hasError;
        repaint();
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        setHasError(!errorMessage.isEmpty());
    }
    
    /**
     * Clear error state
     */
    public void clearError() {
        setHasError(false);
        setErrorMessage("");
    }
}

