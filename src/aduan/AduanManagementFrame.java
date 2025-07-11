/*
 * SIPRIMA Desa Tarabbi - Aduan Management Frame
 * Sistem Pengaduan Masyarakat Desa Tarabbi
 * This class now delegates to FormAduanManajemen for GUI components
 */
package aduan;

import models.User;

/**
 * @author febry
 * @deprecated Use FormAduanManajemen instead for NetBeans GUI editing capability
 */
public class AduanManagementFrame {
    
    private FormAduanManajemen form;
    
    /**
     * Creates new AduanManagementFrame
     */
    public AduanManagementFrame(User user) {
        // Delegate to the NetBeans form
        form = new FormAduanManajemen(user);
    }
    
    /**
     * Show the form
     */
    public void setVisible(boolean visible) {
        form.setVisible(visible);
    }
    
    /**
     * Dispose the form
     */
    public void dispose() {
        form.dispose();
    }
}

