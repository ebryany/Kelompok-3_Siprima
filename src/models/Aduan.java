/*
 * SIPRIMA Desa Tarabbi - Model Aduan
 * Model untuk data aduan/pengaduan masyarakat
 */
package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class untuk Aduan/Pengaduan
 * @author febry
 */
public class Aduan {
    
    // Enum untuk status aduan
    public enum Status {
        BARU("baru", "🆕 Baru", "#3498DB"),
        VALIDASI("validasi", "🔍 Validasi", "#F39C12"),
        PROSES("proses", "⚙️ Diproses", "#E67E22"),
        SELESAI("selesai", "✅ Selesai", "#27AE60"),
        DITOLAK("ditolak", "❌ Ditolak", "#E74C3C");
        
        private final String value;
        private final String displayName;
        private final String color;
        
        Status(String value, String displayName, String color) {
            this.value = value;
            this.displayName = displayName;
            this.color = color;
        }
        
        public String getValue() { return value; }
        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
        
        public static Status fromValue(String value) {
            for (Status status : Status.values()) {
                if (status.value.equals(value)) {
                    return status;
                }
            }
            return BARU;
        }
    }
    
    // Enum untuk prioritas aduan
    public enum Priority {
        RENDAH("rendah", "🟢 Rendah", "#27AE60"),
        SEDANG("sedang", "🟡 Sedang", "#F39C12"),
        TINGGI("tinggi", "🟠 Tinggi", "#E67E22"),
        DARURAT("darurat", "🔴 Darurat", "#E74C3C");
        
        private final String value;
        private final String displayName;
        private final String color;
        
        Priority(String value, String displayName, String color) {
            this.value = value;
            this.displayName = displayName;
            this.color = color;
        }
        
        public String getValue() { return value; }
        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
        
        public static Priority fromValue(String value) {
            for (Priority priority : Priority.values()) {
                if (priority.value.equals(value)) {
                    return priority;
                }
            }
            return SEDANG;
        }
    }
    
    // Enum untuk kategori aduan
    public enum Category {
        INFRASTRUKTUR("Infrastruktur", "🏗️", "Jalan, jembatan, gedung publik"),
        KEBERSIHAN("Kebersihan", "🗑️", "Sampah, drainase, kebersihan lingkungan"),
        UTILITAS("Utilitas", "⚡", "Listrik, air, telekomunikasi"),
        KEAMANAN("Keamanan", "🛡️", "Keamanan dan ketertiban"),
        KESEHATAN("Kesehatan", "🏥", "Fasilitas kesehatan, sanitasi"),
        PENDIDIKAN("Pendidikan", "📚", "Fasilitas pendidikan"),
        SOSIAL("Sosial", "👥", "Masalah sosial kemasyarakatan");
        
        private final String name;
        private final String icon;
        private final String description;
        
        Category(String name, String icon, String description) {
            this.name = name;
            this.icon = icon;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getIcon() { return icon; }
        public String getDescription() { return description; }
        public String getDisplayName() { return icon + " " + name; }
        
        public static Category fromName(String name) {
            for (Category category : Category.values()) {
                if (category.name.equals(name)) {
                    return category;
                }
            }
            return INFRASTRUKTUR;
        }
    }
    
    // Properties
    private int id;
    private String complaintNumber;
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private Status status;
    
    // Pelapor information
    private String reporterName;
    private String reporterNik;
    private String reporterEmail;
    private String reporterPhone;
    private String reporterAddress;
    private String rtRw;
    
    // Location information
    private String locationAddress;
    private Double latitude;
    private Double longitude;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expectedCompletion;
    private LocalDateTime actualCompletion;
    
    // Assignment
    private int reporterId;
    private Integer assignedTo;
    
    // Feedback
    private Integer rating;
    private String feedback;
    
    // Attachments
    private List<String> attachmentPaths;
    
    /**
     * Default constructor
     */
    public Aduan() {
        this.status = Status.BARU;
        this.priority = Priority.SEDANG;
        this.category = Category.INFRASTRUKTUR;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.attachmentPaths = new ArrayList<>();
    }
    
    /**
     * Constructor dengan parameter dasar
     */
    public Aduan(String title, String description, Category category, Priority priority) {
        this();
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.complaintNumber = generateComplaintNumber();
    }
    
    /**
     * Generate complaint number
     */
    private String generateComplaintNumber() {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "ADU" + dateTime;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getComplaintNumber() { return complaintNumber; }
    public void setComplaintNumber(String complaintNumber) { this.complaintNumber = complaintNumber; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { 
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { 
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { 
        this.priority = priority;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { 
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Reporter information
    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
    
    public String getReporterNik() { return reporterNik; }
    public void setReporterNik(String reporterNik) { this.reporterNik = reporterNik; }
    
    public String getReporterEmail() { return reporterEmail; }
    public void setReporterEmail(String reporterEmail) { this.reporterEmail = reporterEmail; }
    
    public String getReporterPhone() { return reporterPhone; }
    public void setReporterPhone(String reporterPhone) { this.reporterPhone = reporterPhone; }
    
    public String getReporterAddress() { return reporterAddress; }
    public void setReporterAddress(String reporterAddress) { this.reporterAddress = reporterAddress; }
    
    public String getRtRw() { return rtRw; }
    public void setRtRw(String rtRw) { this.rtRw = rtRw; }
    
    // Location
    public String getLocationAddress() { return locationAddress; }
    public void setLocationAddress(String locationAddress) { this.locationAddress = locationAddress; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    // Timestamps
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public LocalDateTime getExpectedCompletion() { return expectedCompletion; }
    public void setExpectedCompletion(LocalDateTime expectedCompletion) { this.expectedCompletion = expectedCompletion; }
    
    public LocalDateTime getActualCompletion() { return actualCompletion; }
    public void setActualCompletion(LocalDateTime actualCompletion) { this.actualCompletion = actualCompletion; }
    
    // Assignment
    public int getReporterId() { return reporterId; }
    public void setReporterId(int reporterId) { this.reporterId = reporterId; }
    
    public Integer getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Integer assignedTo) { this.assignedTo = assignedTo; }
    
    // Feedback
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
    // Attachments
    public List<String> getAttachmentPaths() { return attachmentPaths; }
    public void setAttachmentPaths(List<String> attachmentPaths) { this.attachmentPaths = attachmentPaths; }
    
    public void addAttachment(String path) {
        if (this.attachmentPaths == null) {
            this.attachmentPaths = new ArrayList<>();
        }
        this.attachmentPaths.add(path);
    }
    
    // Utility methods
    public String getFormattedCreatedAt() {
        if (createdAt != null) {
            return createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
        }
        return "";
    }
    
    public String getFormattedUpdatedAt() {
        if (updatedAt != null) {
            return updatedAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
        }
        return "";
    }
    
    public String getShortDescription() {
        if (description != null && description.length() > 100) {
            return description.substring(0, 97) + "...";
        }
        return description;
    }
    
    public boolean isDraft() {
        return status == Status.BARU && title == null;
    }
    
    public boolean isUrgent() {
        return priority == Priority.DARURAT;
    }
    
    public boolean isCompleted() {
        return status == Status.SELESAI;
    }
    
    public String getDisplaySummary() {
        return String.format("%s %s - %s", 
            priority.getDisplayName(), 
            complaintNumber, 
            title != null ? title : "[Tanpa Judul]");
    }
    
    @Override
    public String toString() {
        return String.format("Aduan{id=%d, number='%s', title='%s', status=%s, priority=%s, category=%s}",
            id, complaintNumber, title, status, priority, category);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Aduan aduan = (Aduan) obj;
        return id == aduan.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
