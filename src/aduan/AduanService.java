/*
 * SIPRIMA Desa Tarabbi - Aduan Service
 * Service layer untuk menangani operasi database aduan
 */
package aduan;

import Utils.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * Service class untuk menangani operasi CRUD aduan
 */
public class AduanService {
    
    private static AduanService instance;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    private AduanService() {}
    
    public static synchronized AduanService getInstance() {
        if (instance == null) {
            instance = new AduanService();
        }
        return instance;
    }
    
    public static class AduanDTO {
        private String complaintNumber;
        private String title;
        private String reporterName;
        private String category;
        private String status;
        private String priority;
        private String createdAt;
        private int id;
        private String reporterNik;
        private String reporterPhone;
        private String reporterEmail;
        private String reporterAddress;
        private String locationAddress;
        private String description;
        
        // Getters and Setters
        public String getComplaintNumber() { return complaintNumber; }
        public void setComplaintNumber(String complaintNumber) { this.complaintNumber = complaintNumber; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getReporterName() { return reporterName; }
        public void setReporterName(String reporterName) { this.reporterName = reporterName; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getReporterNik() { return reporterNik; }
        public void setReporterNik(String reporterNik) { this.reporterNik = reporterNik; }
        
        public String getReporterPhone() { return reporterPhone; }
        public void setReporterPhone(String reporterPhone) { this.reporterPhone = reporterPhone; }
        
        public String getReporterEmail() { return reporterEmail; }
        public void setReporterEmail(String reporterEmail) { this.reporterEmail = reporterEmail; }
        
        public String getReporterAddress() { return reporterAddress; }
        public void setReporterAddress(String reporterAddress) { this.reporterAddress = reporterAddress; }
        
        public String getLocationAddress() { return locationAddress; }
        public void setLocationAddress(String locationAddress) { this.locationAddress = locationAddress; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    
    /**
     * Mengambil semua data aduan untuk ditampilkan di tabel
     */
    public List<AduanDTO> getAllAduan() throws SQLException {
        List<AduanDTO> aduanList = new ArrayList<>();
        
        String query = "SELECT complaint_number, title, reporter_name, category, " +
                      "status, priority, created_at, id, reporter_nik, reporter_phone, " +
                      "reporter_email, reporter_address, location_address, description " +
                      "FROM complaints ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                AduanDTO aduan = new AduanDTO();
                aduan.setComplaintNumber(rs.getString("complaint_number"));
                aduan.setTitle(rs.getString("title"));
                aduan.setReporterName(rs.getString("reporter_name"));
                aduan.setCategory(rs.getString("category"));
                aduan.setStatus(rs.getString("status"));
                aduan.setPriority(rs.getString("priority"));
                aduan.setCreatedAt(dateFormat.format(rs.getTimestamp("created_at")));
                aduan.setId(rs.getInt("id"));
                aduan.setReporterNik(rs.getString("reporter_nik"));
                aduan.setReporterPhone(rs.getString("reporter_phone"));
                aduan.setReporterEmail(rs.getString("reporter_email"));
                aduan.setReporterAddress(rs.getString("reporter_address"));
                aduan.setLocationAddress(rs.getString("location_address"));
                aduan.setDescription(rs.getString("description"));
                
                aduanList.add(aduan);
            }
        }
        
        return aduanList;
    }
    
    /**
     * Mengambil detail aduan berdasarkan nomor aduan
     */
    public AduanDTO getAduanByNumber(String complaintNumber) throws SQLException {
        String query = "SELECT * FROM complaints WHERE complaint_number = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, complaintNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                AduanDTO aduan = new AduanDTO();
                aduan.setComplaintNumber(rs.getString("complaint_number"));
                aduan.setTitle(rs.getString("title"));
                aduan.setReporterName(rs.getString("reporter_name"));
                aduan.setCategory(rs.getString("category"));
                aduan.setStatus(rs.getString("status"));
                aduan.setPriority(rs.getString("priority"));
                aduan.setCreatedAt(dateFormat.format(rs.getTimestamp("created_at")));
                aduan.setId(rs.getInt("id"));
                aduan.setReporterNik(rs.getString("reporter_nik"));
                aduan.setReporterPhone(rs.getString("reporter_phone"));
                aduan.setReporterEmail(rs.getString("reporter_email"));
                aduan.setReporterAddress(rs.getString("reporter_address"));
                aduan.setLocationAddress(rs.getString("location_address"));
                aduan.setDescription(rs.getString("description"));
                
                return aduan;
            }
        }
        
        return null;
    }
}

