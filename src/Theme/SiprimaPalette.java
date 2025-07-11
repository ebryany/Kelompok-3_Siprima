/*
 * Siprima Color Palette
 * Kelas untuk mengatur semua warna yang digunakan dalam aplikasi Siprima
 */
package Theme;

import java.awt.Color;

/**
 * @author febry
 * Palette warna untuk aplikasi Siprima Desa Tarabbi
 */
public class SiprimaPalette {
    
    // ===================== PRIMARY COLORS =====================
    // Warna utama untuk branding aplikasi
    public static final Color PRIMARY_BLUE = new Color(41, 128, 185);        // #2980B9 - Biru utama
    public static final Color PRIMARY_DARK_BLUE = new Color(21, 101, 192);   // #1565C0 - Biru gelap
    public static final Color PRIMARY_LIGHT_BLUE = new Color(133, 193, 233); // #85C1E9 - Biru terang
    
    public static final Color PRIMARY_GREEN = new Color(39, 174, 96);        // #27AE60 - Hijau utama
    public static final Color PRIMARY_DARK_GREEN = new Color(30, 132, 73);   // #1E8449 - Hijau gelap
    public static final Color PRIMARY_LIGHT_GREEN = new Color(125, 206, 160); // #7DCEA0 - Hijau terang
    
    // ===================== SECONDARY COLORS =====================
    // Warna pendukung untuk elemen UI
    public static final Color SECONDARY_ORANGE = new Color(230, 126, 34);    // #E67E22 - Orange
    public static final Color SECONDARY_RED = new Color(231, 76, 60);        // #E74C3C - Merah
    public static final Color SECONDARY_PURPLE = new Color(155, 89, 182);    // #9B59B6 - Ungu
    public static final Color SECONDARY_YELLOW = new Color(241, 196, 15);    // #F1C40F - Kuning
    
    // ===================== NEUTRAL COLORS =====================
    // Warna netral untuk background dan teks
    public static final Color WHITE = new Color(255, 255, 255);             // #FFFFFF - Putih
    public static final Color LIGHT_GRAY = new Color(245, 245, 245);        // #F5F5F5 - Abu-abu terang
    public static final Color MEDIUM_GRAY = new Color(189, 195, 199);       // #BDC3C7 - Abu-abu sedang
    public static final Color DARK_GRAY = new Color(127, 140, 141);         // #7F8C8D - Abu-abu gelap
    public static final Color CHARCOAL = new Color(52, 73, 94);             // #34495E - Arang
    public static final Color BLACK = new Color(44, 62, 80);                // #2C3E50 - Hitam
    
    // ===================== STATUS COLORS =====================
    // Warna untuk status dan notifikasi
    public static final Color SUCCESS = new Color(46, 204, 113);            // #2ECC71 - Sukses
    public static final Color WARNING = new Color(243, 156, 18);            // #F39C12 - Peringatan
    public static final Color ERROR = new Color(231, 76, 60);               // #E74C3C - Error
    public static final Color INFO = new Color(52, 152, 219);               // #3498DB - Info
    
    // ===================== BACKGROUND COLORS =====================
    // Warna background untuk berbagai komponen
    public static final Color BG_PRIMARY = new Color(236, 240, 241);        // #ECF0F1 - Background utama
    public static final Color BG_SECONDARY = new Color(255, 255, 255);      // #FFFFFF - Background sekunder
    public static final Color BG_CARD = new Color(250, 250, 250);           // #FAFAFA - Background card
    public static final Color BG_SIDEBAR = new Color(44, 62, 80);           // #2C3E50 - Background sidebar
    public static final Color BG_HEADER = new Color(41, 128, 185);          // #2980B9 - Background header
    
    // ===================== TEXT COLORS =====================
    // Warna teks untuk berbagai kebutuhan
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);         // #2C3E50 - Teks utama
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141);    // #7F8C8D - Teks sekunder
    public static final Color TEXT_WHITE = new Color(255, 255, 255);        // #FFFFFF - Teks putih
    public static final Color TEXT_MUTED = new Color(149, 165, 166);        // #95A5A6 - Teks redup
    public static final Color TEXT_LINK = new Color(52, 152, 219);          // #3498DB - Teks link
    
    // ===================== BUTTON COLORS =====================
    // Warna untuk berbagai jenis tombol
    public static final Color BTN_PRIMARY = new Color(41, 128, 185);        // #2980B9 - Tombol utama
    public static final Color BTN_PRIMARY_HOVER = new Color(21, 101, 192);  // #1565C0 - Tombol utama hover
    public static final Color BTN_SUCCESS = new Color(39, 174, 96);         // #27AE60 - Tombol sukses
    public static final Color BTN_SUCCESS_HOVER = new Color(30, 132, 73);   // #1E8449 - Tombol sukses hover
    public static final Color BTN_WARNING = new Color(230, 126, 34);        // #E67E22 - Tombol peringatan
    public static final Color BTN_WARNING_HOVER = new Color(211, 84, 0);    // #D35400 - Tombol peringatan hover
    public static final Color BTN_DANGER = new Color(231, 76, 60);          // #E74C3C - Tombol bahaya
    public static final Color BTN_DANGER_HOVER = new Color(192, 57, 43);    // #C0392B - Tombol bahaya hover
    public static final Color BTN_SECONDARY = new Color(149, 165, 166);     // #95A5A6 - Tombol sekunder
    public static final Color BTN_SECONDARY_HOVER = new Color(127, 140, 141); // #7F8C8D - Tombol sekunder hover
    
    // ===================== BORDER COLORS =====================
    // Warna untuk border dan garis
    public static final Color BORDER_LIGHT = new Color(220, 221, 225);      // #DCDDE1 - Border terang
    public static final Color BORDER_MEDIUM = new Color(189, 195, 199);     // #BDC3C7 - Border sedang
    public static final Color BORDER_DARK = new Color(127, 140, 141);       // #7F8C8D - Border gelap
    public static final Color BORDER_PRIMARY = new Color(41, 128, 185);     // #2980B9 - Border utama
    
    // ===================== HOVER & FOCUS COLORS =====================
    // Warna untuk efek hover dan focus
    public static final Color HOVER_LIGHT = new Color(236, 240, 241);       // #ECF0F1 - Hover terang
    public static final Color HOVER_PRIMARY = new Color(133, 193, 233);     // #85C1E9 - Hover utama
    public static final Color FOCUS_PRIMARY = new Color(93, 173, 226);      // #5DADE2 - Focus utama
    
    // ===================== GRADIENT COLORS =====================
    // Warna untuk gradient
    public static final Color GRADIENT_START = new Color(41, 128, 185);     // #2980B9
    public static final Color GRADIENT_END = new Color(109, 213, 250);      // #6DD5FA
    public static final Color GRADIENT_DARK_START = new Color(44, 62, 80);  // #2C3E50
    public static final Color GRADIENT_DARK_END = new Color(52, 73, 94);    // #34495E
    
    // ===================== TABLE COLORS =====================
    // Warna untuk tabel
    public static final Color TABLE_HEADER = new Color(52, 73, 94);         // #34495E - Header tabel
    public static final Color TABLE_ROW_EVEN = new Color(255, 255, 255);    // #FFFFFF - Baris genap
    public static final Color TABLE_ROW_ODD = new Color(248, 249, 250);     // #F8F9FA - Baris ganjil
    public static final Color TABLE_SELECTED = new Color(133, 193, 233);    // #85C1E9 - Baris terpilih
    public static final Color TABLE_HOVER = new Color(214, 234, 248);       // #D6EAF8 - Baris hover
    
    // ===================== FORM COLORS =====================
    // Warna untuk form input
    public static final Color INPUT_BACKGROUND = new Color(255, 255, 255);  // #FFFFFF - Background input
    public static final Color INPUT_BORDER = new Color(189, 195, 199);      // #BDC3C7 - Border input
    public static final Color INPUT_FOCUS = new Color(41, 128, 185);        // #2980B9 - Input focus
    public static final Color INPUT_ERROR = new Color(231, 76, 60);         // #E74C3C - Input error
    public static final Color INPUT_PLACEHOLDER = new Color(149, 165, 166); // #95A5A6 - Placeholder
    
    // ===================== CARD COLORS =====================
    // Warna untuk kartu dan panel
    public static final Color CARD_BACKGROUND = new Color(255, 255, 255);   // #FFFFFF - Background kartu
    public static final Color CARD_BORDER = new Color(220, 221, 225);       // #DCDDE1 - Border kartu
    public static final Color CARD_SHADOW = new Color(0, 0, 0, 20);         // Shadow kartu (20% opacity)
    
    // ===================== TRANSPARENCY LEVELS =====================
    // Level transparansi untuk overlay
    public static final Color OVERLAY_LIGHT = new Color(0, 0, 0, 51);       // 20% black
    public static final Color OVERLAY_MEDIUM = new Color(0, 0, 0, 102);     // 40% black
    public static final Color OVERLAY_DARK = new Color(0, 0, 0, 153);       // 60% black
    
    // ===================== UTILITY METHODS =====================
    
    /**
     * Mengubah opacity dari warna yang ada
     * @param color Warna asli
     * @param alpha Nilai alpha (0-255)
     * @return Warna dengan opacity baru
     */
    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    /**
     * Membuat warna lebih terang
     * @param color Warna asli
     * @param factor Faktor kecerahan (0.0 - 1.0)
     * @return Warna yang lebih terang
     */
    public static Color brighter(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int)(color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int)(color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b, color.getAlpha());
    }
    
    /**
     * Membuat warna lebih gelap
     * @param color Warna asli
     * @param factor Faktor kegelapan (0.0 - 1.0)
     * @return Warna yang lebih gelap
     */
    public static Color darker(Color color, float factor) {
        int r = Math.max(0, (int)(color.getRed() * (1 - factor)));
        int g = Math.max(0, (int)(color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int)(color.getBlue() * (1 - factor)));
        return new Color(r, g, b, color.getAlpha());
    }
    
    /**
     * Konversi warna ke hex string
     * @param color Warna yang akan dikonversi
     * @return String hex (contoh: #FF0000)
     */
    public static String toHex(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    /**
     * Konversi hex string ke Color
     * @param hex String hex (contoh: #FF0000 atau FF0000)
     * @return Color object
     */
    public static Color fromHex(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        return new Color(Integer.parseInt(hex, 16));
    }
}

