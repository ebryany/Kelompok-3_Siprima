/*
 * SIPRIMA Desa Tarabbi - Generator Laporan
 * Sistem Pengaduan Masyarakat Desa Tarabbi
 * Report Generator dengan Export Excel, PDF dan Print
 */
package Laporan;

import Utils.DatabaseConfig;
import models.User;
import java.awt.*;
import java.awt.print.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author febry
 */
public class GeneratorLaporan {
    
    // SIPRIMA Official Colors
    private static final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private static final Color PRIMARY_GREEN = new Color(39, 174, 96);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private static final Color BG_PRIMARY = new Color(236, 240, 241);
    
    /**
     * Generates comprehensive report for SIPRIMA
     */
    public void generateComprehensiveReport() throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            // Create report directory
            File reportDir = new File(System.getProperty("user.home") + "/Documents/SIPRIMA_Reports");
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }
            
            // Generate timestamp for filename
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String filename = "SIPRIMA_Report_" + timestamp + ".txt";
            File reportFile = new File(reportDir, filename);
            
            // Generate report content
            StringBuilder report = new StringBuilder();
            report.append("\n");
            report.append("╔═══════════════════════════════════════════════════════════════════════════════╗\n");
            report.append("║                           SIPRIMA DESA TARABBI                               ║\n");
            report.append("║                        LAPORAN PENGADUAN MASYARAKAT                         ║\n");
            report.append("╠═══════════════════════════════════════════════════════════════════════════════╣\n");
            report.append(String.format("║ Tanggal Generate: %-59s ║\n", new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(new java.util.Date())));
            report.append("╚═══════════════════════════════════════════════════════════════════════════════╝\n\n");
            
            // Statistics Summary
            report.append("📊 RINGKASAN STATISTIK\n");
            report.append("═".repeat(50)).append("\n");
            report.append(String.format("📋 Total Aduan         : %d\n", getTotalComplaints(conn)));
            report.append(String.format("⏳ Menunggu            : %d\n", getComplaintsByStatus(conn, "menunggu")));
            report.append(String.format("⚙️  Sedang Diproses     : %d\n", getComplaintsByStatus(conn, "proses")));
            report.append(String.format("✅ Selesai             : %d\n", getComplaintsByStatus(conn, "selesai")));
            report.append(String.format("❌ Ditolak             : %d\n", getComplaintsByStatus(conn, "ditolak")));
            report.append(String.format("👥 Total Pengguna      : %d\n\n", getTotalUsers(conn)));
            
            // Complaints by Category
            report.append("📂 ADUAN BERDASARKAN KATEGORI\n");
            report.append("═".repeat(50)).append("\n");
            Map<String, Integer> categoryStats = getComplaintsByCategory(conn);
            for (Map.Entry<String, Integer> entry : categoryStats.entrySet()) {
                report.append(String.format("%-25s : %d\n", entry.getKey(), entry.getValue()));
            }
            report.append("\n");
            
            // Complaints by Priority
            report.append("🚨 ADUAN BERDASARKAN PRIORITAS\n");
            report.append("═".repeat(50)).append("\n");
            report.append(String.format("🔴 Tinggi              : %d\n", getComplaintsByPriority(conn, "tinggi")));
            report.append(String.format("🟡 Sedang              : %d\n", getComplaintsByPriority(conn, "sedang")));
            report.append(String.format("🟢 Rendah              : %d\n\n", getComplaintsByPriority(conn, "rendah")));
            
            // Recent Complaints (Last 30 days)
            report.append("📅 ADUAN TERBARU (30 HARI TERAKHIR)\n");
            report.append("═".repeat(80)).append("\n");
            List<String[]> recentComplaints = getRecentComplaints(conn, 30);
            if (!recentComplaints.isEmpty()) {
                report.append(String.format("%-4s %-15s %-20s %-12s %-15s %-12s\n", 
                    "ID", "Tanggal", "Judul", "Status", "Kategori", "Prioritas"));
                report.append("-".repeat(80)).append("\n");
                for (String[] complaint : recentComplaints) {
                    report.append(String.format("%-4s %-15s %-20s %-12s %-15s %-12s\n", 
                        complaint[0], complaint[1], 
                        complaint[2].length() > 18 ? complaint[2].substring(0, 18) + ".." : complaint[2],
                        complaint[3], complaint[4], complaint[5]));
                }
            } else {
                report.append("Tidak ada aduan dalam 30 hari terakhir.\n");
            }
            report.append("\n");
            
            // Footer
            report.append("\n");
            report.append("╔═══════════════════════════════════════════════════════════════════════════════╗\n");
            report.append("║                     SIPRIMA - Sistem Pengaduan Masyarakat                   ║\n");
            report.append("║                              Desa Tarabbi                                    ║\n");
            report.append("╚═══════════════════════════════════════════════════════════════════════════════╝\n");
            
            // Write to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile, StandardCharsets.UTF_8))) {
                writer.print(report.toString());
            }
            
            // Success notification with file path
            JOptionPane.showMessageDialog(null,
                "✅ Laporan berhasil dibuat!\n\n" +
                "📁 Lokasi: " + reportFile.getAbsolutePath() + "\n" +
                "📄 Nama File: " + filename + "\n\n" +
                "Laporan dapat dibuka dengan text editor atau notepad.",
                "Report Generated - SIPRIMA",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new Exception("File writing error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Export data to CSV format for Excel compatibility
     */
    public void exportToExcel() throws Exception {
        try (Connection conn = DatabaseConfig.getConnection()) {
            // Create export directory
            File exportDir = new File(System.getProperty("user.home") + "/Downloads");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String filename = "SIPRIMA_Data_" + timestamp + ".csv";
            File csvFile = new File(exportDir, filename);
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile, StandardCharsets.UTF_8))) {
                // Write BOM for proper UTF-8 encoding in Excel
                writer.write("\uFEFF");
                
                // CSV Header
                writer.println("ID,Tanggal,Judul,Deskripsi,Status,Kategori,Prioritas,Pelapor,Email");
                
                // Get all complaints
                String query = "SELECT c.id, c.created_at, c.title, c.description, c.status, " +
                              "c.category, c.priority, u.full_name, u.email " +
                              "FROM complaints c " +
                              "LEFT JOIN users u ON c.user_id = u.id " +
                              "ORDER BY c.created_at DESC";
                              
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {
                    
                    while (rs.next()) {
                        writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                            rs.getInt("id"),
                            rs.getTimestamp("created_at"),
                            escapeCSV(rs.getString("title")),
                            escapeCSV(rs.getString("description")),
                            rs.getString("status"),
                            rs.getString("category"),
                            rs.getString("priority"),
                            escapeCSV(rs.getString("full_name")),
                            rs.getString("email")
                        );
                    }
                }
            }
            
            JOptionPane.showMessageDialog(null,
                "📈 Export Excel berhasil!\n\n" +
                "📁 Lokasi: " + csvFile.getAbsolutePath() + "\n" +
                "📄 Nama File: " + filename + "\n\n" +
                "File dapat dibuka dengan Microsoft Excel atau Google Sheets.",
                "Export Excel - SIPRIMA",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new Exception("File writing error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Print current statistics
     */
    public void printReport() {
        try {
            // Create print content
            StringBuilder content = new StringBuilder();
            content.append("SIPRIMA DESA TARABBI - LAPORAN RINGKAS\n");
            content.append("Generated: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date())).append("\n\n");
            
            try (Connection conn = DatabaseConfig.getConnection()) {
                content.append("STATISTIK PENGADUAN:\n");
                content.append("Total Aduan: ").append(getTotalComplaints(conn)).append("\n");
                content.append("Menunggu: ").append(getComplaintsByStatus(conn, "menunggu")).append("\n");
                content.append("Diproses: ").append(getComplaintsByStatus(conn, "proses")).append("\n");
                content.append("Selesai: ").append(getComplaintsByStatus(conn, "selesai")).append("\n");
                content.append("Total Pengguna: ").append(getTotalUsers(conn)).append("\n");
            }
            
            // Print the content
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable((Graphics graphics, PageFormat pageFormat, int pageIndex) -> {
                if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
                
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
                
                String[] lines = content.toString().split("\n");
                int y = 20;
                for (String line : lines) {
                    g2d.drawString(line, 10, y);
                    y += 15;
                }
                
                return Printable.PAGE_EXISTS;
            });
            
            if (job.printDialog()) {
                job.print();
                JOptionPane.showMessageDialog(null,
                    "🖨️ Laporan berhasil dikirim ke printer!",
                    "Print Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "❌ Error printing: " + e.getMessage(),
                "Print Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper methods for database queries
    private int getTotalComplaints(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) FROM complaints";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    private int getComplaintsByStatus(Connection conn, String status) throws SQLException {
        String query = "SELECT COUNT(*) FROM complaints WHERE status = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
    
    private int getComplaintsByPriority(Connection conn, String priority) throws SQLException {
        String query = "SELECT COUNT(*) FROM complaints WHERE priority = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, priority);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
    
    private int getTotalUsers(Connection conn) throws SQLException {
        String query = "SELECT COUNT(*) FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
    
    private Map<String, Integer> getComplaintsByCategory(Connection conn) throws SQLException {
        Map<String, Integer> categories = new LinkedHashMap<>();
        String query = "SELECT category, COUNT(*) as count FROM complaints GROUP BY category ORDER BY count DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.put(rs.getString("category"), rs.getInt("count"));
            }
        }
        return categories;
    }
    
    private List<String[]> getRecentComplaints(Connection conn, int days) throws SQLException {
        List<String[]> complaints = new ArrayList<>();
        String query = "SELECT c.id, c.created_at, c.title, c.status, c.category, c.priority " +
                      "FROM complaints c " +
                      "WHERE c.created_at >= DATE_SUB(NOW(), INTERVAL ? DAY) " +
                      "ORDER BY c.created_at DESC LIMIT 20";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, days);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    complaints.add(new String[] {
                        String.valueOf(rs.getInt("id")),
                        new SimpleDateFormat("dd/MM/yyyy").format(rs.getTimestamp("created_at")),
                        rs.getString("title"),
                        rs.getString("status"),
                        rs.getString("category"),
                        rs.getString("priority")
                    });
                }
            }
        }
        return complaints;
    }
    
    private String escapeCSV(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"").replace("\n", " ").replace("\r", "");
    }
}
