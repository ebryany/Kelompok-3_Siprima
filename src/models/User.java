/*
 * SIPRIMA User Model
 * Model untuk menangani data pengguna sistem
 */
package models;

import java.sql.Timestamp;

/**
 * @author febry
 * Model User untuk aplikasi SIPRIMA
 */
public class User {
    
    // User properties
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private UserRole role;
    private String phone;
    private String address;
    private String rtRw;
    private String nik;
    private String photoUrl;
    private boolean isActive;
    private Timestamp lastLogin;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Enum for user roles
    public enum UserRole {
        MASYARAKAT("masyarakat", "Masyarakat"),
        PETUGAS("petugas", "Petugas"),
        SUPERVISOR("supervisor", "Supervisor"),
        ADMIN("admin", "Admin");
        
        private final String value;
        private final String displayName;
        
        UserRole(String value, String displayName) {
            this.value = value;
            this.displayName = displayName;
        }
        
        public String getValue() {
            return value;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public static UserRole fromString(String value) {
            for (UserRole role : UserRole.values()) {
                if (role.value.equalsIgnoreCase(value)) {
                    return role;
                }
            }
            return MASYARAKAT; // default
        }
    }
    
    // Constructors
    public User() {
        this.role = UserRole.MASYARAKAT;
        this.isActive = true;
    }
    
    public User(String username, String email, String fullName, UserRole role) {
        this();
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getRtRw() {
        return rtRw;
    }
    
    public void setRtRw(String rtRw) {
        this.rtRw = rtRw;
    }
    
    public String getNik() {
        return nik;
    }
    
    public void setNik(String nik) {
        this.nik = nik;
    }
    
    public String getPhotoUrl() {
        return photoUrl;
    }
    
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    public Timestamp getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Utility methods
    public String getRoleDisplayName() {
        return role != null ? role.getDisplayName() : "Unknown";
    }
    
    public String getDisplayName() {
        // Prioritas: fullName -> username -> email -> default
        if (fullName != null && !fullName.trim().isEmpty()) {
            return fullName.trim();
        }
        if (username != null && !username.trim().isEmpty()) {
            return username.trim();
        }
        if (email != null && !email.trim().isEmpty()) {
            return email.trim();
        }
        return "User";
    }
    
    public boolean hasRole(UserRole checkRole) {
        return this.role == checkRole;
    }
    
    public boolean isStaff() {
        return role == UserRole.PETUGAS || role == UserRole.SUPERVISOR || role == UserRole.ADMIN;
    }
    
    public boolean canManageUsers() {
        return role == UserRole.ADMIN || role == UserRole.SUPERVISOR;
    }
    
    public boolean canAssignComplaints() {
        return role == UserRole.SUPERVISOR || role == UserRole.ADMIN;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User user = (User) obj;
        return id == user.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
