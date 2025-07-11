# 📖 CARA FORMAT DOKUMENTASI KODE DI MICROSOFT WORD

> **Panduan Step-by-Step untuk Formatting Dokumentasi Kode**  
> Khusus untuk Project SIPRIMA

---

## 🎯 **LANGKAH-LANGKAH FORMATTING**

### **STEP 1: Setup Document**

#### **1.1 Pengaturan Dasar**
- **Font**: Times New Roman 12pt
- **Line Spacing**: 1.5 lines
- **Margins**: 3cm (left), 2.5cm (right, top, bottom)
- **Alignment**: Justified

#### **1.2 Header & Footer**
```
Header: LAPORAN PROJECT SIPRIMA - BAB V IMPLEMENTASI
Footer: Nama Mahasiswa - NIM - Halaman [X]
```

### **STEP 2: Struktur Heading**

#### **2.1 Heading Hierarchy**
```
BAB V - IMPLEMENTASI                    → Heading 1 (16pt, Bold, Center)
5.3 Implementasi User Interface         → Heading 2 (14pt, Bold, Left)
5.3.1 WelcomeForm.java                  → Heading 3 (12pt, Bold, Left)
A. Deskripsi Umum                       → Heading 4 (12pt, Bold, Left)
1. Deklarasi Kelas                      → Heading 5 (12pt, Normal, Left)
```

#### **2.2 Style Settings**
- **Heading 1**: Times New Roman, 16pt, Bold, Center, Space After 12pt
- **Heading 2**: Times New Roman, 14pt, Bold, Left, Space After 6pt
- **Heading 3**: Times New Roman, 12pt, Bold, Left, Space After 6pt
- **Heading 4**: Times New Roman, 12pt, Bold, Left, Space After 3pt

### **STEP 3: Code Formatting**

#### **3.1 Code Block Style**
```
Font: Consolas 10pt
Background: Light Gray (RGB: 245, 245, 245)
Border: Single line, Gray
Padding: 6pt all sides
Alignment: Left
```

#### **3.2 Cara Membuat Code Block**
1. **Insert → Text Box → Simple Text Box**
2. **Format Text Box:**
   - Fill: Light Gray
   - Outline: Gray, 0.5pt
   - Internal Margin: 6pt
3. **Text Formatting:**
   - Font: Consolas 10pt
   - Color: Dark Blue (RGB: 0, 0, 139)

#### **3.3 Inline Code Style**
```
Font: Consolas 11pt
Background: Light Gray
Format: `kode inline seperti ini`
```

### **STEP 4: Content Formatting**

#### **4.1 Body Text**
```
Font: Times New Roman 12pt
Line Spacing: 1.5
Alignment: Justified
First Line Indent: 1cm
```

#### **4.2 List Formatting**
**Bullet Points:**
- Symbol: • (bullet)
- Indent: 1cm from left
- Hanging indent: 0.5cm

**Numbered Lists:**
- Format: 1. 2. 3.
- Indent: 1cm from left
- Hanging indent: 0.5cm

### **STEP 5: Tables**

#### **5.1 Table Style Settings**
```
Border: All borders, 0.5pt, Black
Header Row: Bold, White text, Blue background (RGB: 41, 128, 185)
Alternate Rows: Light gray background
Font: Times New Roman 11pt
Cell Padding: 6pt
```

#### **5.2 Example Table Format**
```
+------------------+------------------+-------------------------+
| Komponen         | Fungsi           | Styling Key Features    |
+==================+==================+=========================+
| headerPanel      | Header utama     | Gradient blue background|
+------------------+------------------+-------------------------+
| titleLabel       | Judul aplikasi   | Segoe UI Bold 28pt     |
+------------------+------------------+-------------------------+
```

### **STEP 6: Images & Screenshots**

#### **6.1 Image Formatting**
```
Alignment: Center
Wrap Text: Top and Bottom
Caption: Figure X.X - [Description]
Caption Format: Times New Roman 11pt, Italic, Center
```

#### **6.2 Caption Numbering**
- **Format**: Gambar 5.1, Gambar 5.2, dst.
- **Position**: Below image
- **Style**: Italic, Center aligned

---

## 📝 **TEMPLATE COPY-PASTE UNTUK WORD**

### **A. Header Section**
```
5.3.1 WelcomeForm.java - Halaman Selamat Datang

A. Deskripsi Umum

WelcomeForm.java merupakan halaman utama (landing page) dari aplikasi SIPRIMA yang berfungsi sebagai pintu masuk bagi pengguna. Form ini mengimplementasikan desain modern dengan responsive layout yang dapat menyesuaikan tampilan berdasarkan ukuran layar.

Fitur Utama:
• Responsive design dengan breakpoints untuk mobile, tablet, dan desktop
• Modern UI dengan tema warna biru konsisten  
• Navigasi ke form login dan registrasi
• Efek hover pada tombol dengan animasi
```

### **B. Code Explanation Template**
```
1. Deklarasi Kelas (Line 22)

[CODE BLOCK]
public class WelcomeForm extends javax.swing.JFrame {
[/CODE BLOCK]

Penjelasan:
Kelas WelcomeForm mewarisi dari JFrame yang merupakan jendela utama aplikasi Swing. Pewarisan ini memberikan akses ke semua method dan properti window management.
```

### **C. Method Explanation Template**
```
2. Konstruktor Utama (Lines 35-39)

[CODE BLOCK]
public WelcomeForm() {
    initComponents();
    customizeModernComponents();
    setupWindowProperties();
}
[/CODE BLOCK]

Penjelasan:
• initComponents(): Method auto-generated NetBeans untuk inisialisasi komponen UI
• customizeModernComponents(): Method custom untuk styling modern
• setupWindowProperties(): Pengaturan properti window dan responsivitas
```

---

## 🎨 **FORMATTING CHECKLIST**

### ✅ **Before Submitting**
- [ ] All headings menggunakan style yang konsisten
- [ ] Code blocks menggunakan Consolas font
- [ ] Tables memiliki header styling
- [ ] Images memiliki caption
- [ ] Page numbers sudah benar
- [ ] Table of Contents di-update
- [ ] Spell check completed

### ✅ **Visual Consistency**
- [ ] Font sizes konsisten untuk setiap level
- [ ] Line spacing uniform (1.5)
- [ ] Margins sesuai standard
- [ ] Code highlighting konsisten
- [ ] Color scheme seragam

---

## 💡 **TIPS ADVANCED FORMATTING**

### **1. Auto Table of Contents**
```
1. Use Heading Styles consistently
2. Insert → Table of Contents
3. Choose Automatic Table 1
4. Update before final submission
```

### **2. Cross-References**
```
"Seperti yang dijelaskan pada Gambar 5.1..."
"Merujuk pada Tabel 5.2..."
"Sesuai dengan Listing 5.3..."
```

### **3. Code Syntax Highlighting**
```
Keywords (public, class, private): Blue, Bold
Comments (// /*): Green, Italic
Strings ("text"): Red
Numbers: Dark Red
```

### **4. Professional Formatting**
```
• Consistent spacing between sections
• Proper indentation for nested content
• Aligned tables and images
• Clean, readable font choices
• Appropriate use of bold/italic
```

---

## 📄 **SAMPLE OUTPUT FORMAT**

```
BAB V - IMPLEMENTASI

5.3 Implementasi User Interface

5.3.1 WelcomeForm.java - Halaman Selamat Datang

A. Deskripsi Umum

    WelcomeForm.java merupakan halaman utama (landing page) dari 
aplikasi SIPRIMA yang berfungsi sebagai pintu masuk bagi pengguna. 
Form ini mengimplementasikan desain modern dengan responsive layout 
yang dapat menyesuaikan tampilan berdasarkan ukuran layar.

    Fitur utama yang diimplementasikan meliputi responsive design 
dengan breakpoints untuk mobile, tablet, dan desktop, modern UI 
dengan tema warna biru konsisten, navigasi ke form login dan 
registrasi, serta efek hover pada tombol dengan animasi.

B. Struktur Kelas dan Properti

1. Deklarasi Kelas (Line 22)

┌─────────────────────────────────────────────────────────────┐
│ public class WelcomeForm extends javax.swing.JFrame {      │
└─────────────────────────────────────────────────────────────┘

    Kelas WelcomeForm mewarisi dari JFrame yang merupakan jendela 
utama aplikasi Swing. Pewarisan ini memberikan akses ke semua 
method dan properti window management.
```

---

## 🚀 **QUICK START GUIDE**

### **1. Setup Document (5 menit)**
1. Buka Microsoft Word
2. Set margins: Page Layout → Margins → Custom (3cm, 2.5cm, 2.5cm, 2.5cm)
3. Set font: Home → Font → Times New Roman 12pt
4. Set line spacing: Home → Line Spacing → 1.5

### **2. Create Styles (10 menit)**
1. Home → Styles → Create New Style
2. Buat 5 heading styles sesuai hierarchy
3. Buat "Code Block" style dengan Consolas font
4. Buat "Caption" style untuk gambar/tabel

### **3. Copy Content (15 menit)**
1. Copy dari file markdown/text
2. Apply styles ke setiap section
3. Format code blocks
4. Insert dan format tables

### **4. Final Review (10 menit)**
1. Update Table of Contents
2. Check page numbering
3. Spell check
4. Review formatting consistency

---

*📋 Total waktu formatting: ~40 menit untuk dokumentasi lengkap*

---

**💡 Pro Tip**: Simpan sebagai template (.dotx) untuk project selanjutnya!

