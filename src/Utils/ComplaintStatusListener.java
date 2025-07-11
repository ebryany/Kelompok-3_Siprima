/*
 * SIPRIMA Complaint Status Listener
 * Interface untuk mendengarkan perubahan status aduan secara realtime
 */
package Utils;

/**
 * Interface untuk menerima notifikasi perubahan status aduan
 * @author febry
 */
public interface ComplaintStatusListener {
    
    /**
     * Dipanggil ketika status aduan berubah
     * @param complaintNumber nomor aduan yang berubah
     * @param oldStatus status lama
     * @param newStatus status baru
     * @param updatedBy user yang melakukan perubahan
     */
    void onStatusChanged(String complaintNumber, String oldStatus, String newStatus, String updatedBy);
    
    /**
     * Dipanggil ketika ada aduan baru
     * @param complaintNumber nomor aduan baru
     * @param category kategori aduan
     * @param priority prioritas aduan
     */
    void onComplaintAdded(String complaintNumber, String category, String priority);
    
    /**
     * Dipanggil ketika aduan dihapus
     * @param complaintNumber nomor aduan yang dihapus
     */
    void onComplaintDeleted(String complaintNumber);
}

