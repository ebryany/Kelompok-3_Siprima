/*
 * SIPRIMA Complaint Event Manager
 * Mengelola event listener untuk perubahan status aduan secara realtime
 */
package Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manager untuk mengelola event listener complaint status
 * @author febry
 */
public class ComplaintEventManager {
    
    // Singleton instance
    private static ComplaintEventManager instance;
    
    // Thread-safe list of listeners
    private final List<ComplaintStatusListener> listeners;
    
    /**
     * Private constructor untuk singleton
     */
    private ComplaintEventManager() {
        listeners = new CopyOnWriteArrayList<>();
    }
    
    /**
     * Get singleton instance
     * @return ComplaintEventManager instance
     */
    public static ComplaintEventManager getInstance() {
        if (instance == null) {
            synchronized (ComplaintEventManager.class) {
                if (instance == null) {
                    instance = new ComplaintEventManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Register listener untuk menerima notifikasi perubahan complaint
     * @param listener ComplaintStatusListener yang akan menerima notifikasi
     */
    public void addListener(ComplaintStatusListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            System.out.println("✅ ComplaintStatusListener registered: " + listener.getClass().getSimpleName());
        }
    }
    
    /**
     * Unregister listener
     * @param listener ComplaintStatusListener yang akan dihapus
     */
    public void removeListener(ComplaintStatusListener listener) {
        if (listener != null) {
            listeners.remove(listener);
            System.out.println("❌ ComplaintStatusListener unregistered: " + listener.getClass().getSimpleName());
        }
    }
    
    /**
     * Fire event ketika status complaint berubah
     * @param complaintNumber nomor aduan
     * @param oldStatus status lama
     * @param newStatus status baru
     * @param updatedBy user yang melakukan update
     */
    public void fireStatusChanged(String complaintNumber, String oldStatus, String newStatus, String updatedBy) {
        System.out.println("🔄 Firing status change event: " + complaintNumber + " (" + oldStatus + " → " + newStatus + ")");
        
        // Notify all listeners in separate thread untuk avoid blocking UI
        new Thread(() -> {
            for (ComplaintStatusListener listener : listeners) {
                try {
                    listener.onStatusChanged(complaintNumber, oldStatus, newStatus, updatedBy);
                } catch (Exception e) {
                    System.err.println("Error notifying listener: " + e.getMessage());
                }
            }
        }).start();
    }
    
    /**
     * Fire event ketika ada complaint baru
     * @param complaintNumber nomor aduan baru
     * @param category kategori aduan
     * @param priority prioritas aduan
     */
    public void fireComplaintAdded(String complaintNumber, String category, String priority) {
        System.out.println("📝 Firing complaint added event: " + complaintNumber);
        
        new Thread(() -> {
            for (ComplaintStatusListener listener : listeners) {
                try {
                    listener.onComplaintAdded(complaintNumber, category, priority);
                } catch (Exception e) {
                    System.err.println("Error notifying listener: " + e.getMessage());
                }
            }
        }).start();
    }
    
    /**
     * Fire event ketika complaint dihapus
     * @param complaintNumber nomor aduan yang dihapus
     */
    public void fireComplaintDeleted(String complaintNumber) {
        System.out.println("🗑️ Firing complaint deleted event: " + complaintNumber);
        
        new Thread(() -> {
            for (ComplaintStatusListener listener : listeners) {
                try {
                    listener.onComplaintDeleted(complaintNumber);
                } catch (Exception e) {
                    System.err.println("Error notifying listener: " + e.getMessage());
                }
            }
        }).start();
    }
    
    /**
     * Get jumlah listener yang terdaftar
     * @return jumlah listener
     */
    public int getListenerCount() {
        return listeners.size();
    }
    
    /**
     * Clear semua listener (untuk cleanup)
     */
    public void clearAllListeners() {
        listeners.clear();
        System.out.println("🧹 All ComplaintStatusListeners cleared");
    }
}

