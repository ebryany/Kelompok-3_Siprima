/*
 * Custom Table Component untuk Siprima
 * Tabel dengan styling yang konsisten dengan tema aplikasi
 */
package Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author febry
 * Custom table untuk aplikasi Siprima
 */
public class CustomTable extends JTable {
    
    private boolean useAlternateRowColors;
    private boolean useHoverEffect;
    private Color hoverColor;
    private int hoveredRow;
    
    /**
     * Constructor default
     */
    public CustomTable() {
        super();
        this.useAlternateRowColors = true;
        this.useHoverEffect = true;
        this.hoverColor = SiprimaPalette.TABLE_HOVER;
        this.hoveredRow = -1;
        
        initializeTable();
        setupMouseListeners();
    }
    
    /**
     * Constructor dengan model
     */
    public CustomTable(DefaultTableModel model) {
        super(model);
        this.useAlternateRowColors = true;
        this.useHoverEffect = true;
        this.hoverColor = SiprimaPalette.TABLE_HOVER;
        this.hoveredRow = -1;
        
        initializeTable();
        setupMouseListeners();
    }
    
    /**
     * Inisialisasi tabel
     */
    private void initializeTable() {
        // Set basic properties
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setRowHeight(40);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 1));
        setSelectionBackground(SiprimaPalette.TABLE_SELECTED);
        setSelectionForeground(SiprimaPalette.TEXT_PRIMARY);
        setBackground(SiprimaPalette.TABLE_ROW_EVEN);
        
        // Set selection mode
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Customize header
        customizeHeader();
        
        // Set cell renderer
        setDefaultRenderer(Object.class, new CustomTableCellRenderer());
    }
    
    /**
     * Kustomisasi header tabel
     */
    private void customizeHeader() {
        JTableHeader header = getTableHeader();
        header.setBackground(SiprimaPalette.TABLE_HEADER);
        header.setForeground(SiprimaPalette.TEXT_WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setReorderingAllowed(false);
        
        // Custom header renderer
        header.setDefaultRenderer(new CustomHeaderRenderer());
    }
    
    /**
     * Setup mouse listeners untuk hover effect
     */
    private void setupMouseListeners() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (useHoverEffect) {
                    int row = rowAtPoint(e.getPoint());
                    if (row != hoveredRow) {
                        hoveredRow = row;
                        repaint();
                    }
                }
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (useHoverEffect) {
                    hoveredRow = -1;
                    repaint();
                }
            }
        });
    }
    
    /**
     * Custom cell renderer
     */
    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Set font
            setFont(table.getFont());
            
            // Set text color
            if (isSelected) {
                setForeground(SiprimaPalette.TEXT_PRIMARY);
                setBackground(SiprimaPalette.TABLE_SELECTED);
            } else {
                setForeground(SiprimaPalette.TEXT_PRIMARY);
                
                // Set background color
                if (useHoverEffect && row == hoveredRow) {
                    setBackground(hoverColor);
                } else if (useAlternateRowColors) {
                    if (row % 2 == 0) {
                        setBackground(SiprimaPalette.TABLE_ROW_EVEN);
                    } else {
                        setBackground(SiprimaPalette.TABLE_ROW_ODD);
                    }
                } else {
                    setBackground(SiprimaPalette.TABLE_ROW_EVEN);
                }
            }
            
            // Set border
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            
            // Set alignment berdasarkan tipe data
            if (value instanceof Number) {
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else if (value instanceof Boolean) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }
            
            return this;
        }
    }
    
    /**
     * Custom header renderer
     */
    private class CustomHeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setBackground(SiprimaPalette.TABLE_HEADER);
            setForeground(SiprimaPalette.TEXT_WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            
            return this;
        }
    }
    
    // ===================== UTILITY METHODS =====================
    
    /**
     * Tambah data ke tabel
     */
    public void addRow(Object[] rowData) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(rowData);
    }
    
    /**
     * Hapus baris terpilih
     */
    public void removeSelectedRow() {
        int selectedRow = getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) getModel();
            model.removeRow(selectedRow);
        }
    }
    
    /**
     * Hapus semua data
     */
    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(0);
    }
    
    /**
     * Get data dari baris terpilih
     */
    public Object[] getSelectedRowData() {
        int selectedRow = getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) getModel();
            int columnCount = model.getColumnCount();
            Object[] rowData = new Object[columnCount];
            
            for (int i = 0; i < columnCount; i++) {
                rowData[i] = model.getValueAt(selectedRow, i);
            }
            
            return rowData;
        }
        return null;
    }
    
    /**
     * Update data pada baris tertentu
     */
    public void updateRow(int row, Object[] newData) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        if (row >= 0 && row < model.getRowCount()) {
            for (int i = 0; i < newData.length && i < model.getColumnCount(); i++) {
                model.setValueAt(newData[i], row, i);
            }
        }
    }
    
    // ===================== GETTER & SETTER =====================
    
    public boolean isUseAlternateRowColors() {
        return useAlternateRowColors;
    }
    
    public void setUseAlternateRowColors(boolean useAlternateRowColors) {
        this.useAlternateRowColors = useAlternateRowColors;
        repaint();
    }
    
    public boolean isUseHoverEffect() {
        return useHoverEffect;
    }
    
    public void setUseHoverEffect(boolean useHoverEffect) {
        this.useHoverEffect = useHoverEffect;
        if (!useHoverEffect) {
            hoveredRow = -1;
            repaint();
        }
    }
    
    public Color getHoverColor() {
        return hoverColor;
    }
    
    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
        repaint();
    }
}

