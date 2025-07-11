# 🎨 SIPRIMA DESA TARABBI - UI DESIGN DOCUMENTATION

## 📋 PROJECT OVERVIEW

**Project Name**: SIPRIMA (Sistem Pengaduan Masyarakat)  
**Location**: Desa Tarabbi  
**Platform**: Java Swing Desktop Application  
**Design System**: Modern Material Design + Government Style  
**Target Users**: Masyarakat, Petugas, Supervisor, Admin  

---

## 🎨 DESIGN SYSTEM

### 🌈 COLOR PALETTE

**✨ Sudah Diimplementasi dalam SiprimaPalette.java**

#### **Primary Colors**
```java
// Brand & Primary Actions (SiprimaPalette)
PRIMARY_BLUE        = RGB(41, 128, 185)     // #2980B9 - Main brand
PRIMARY_DARK_BLUE   = RGB(21, 101, 192)     // #1565C0 - Dark variant
PRIMARY_LIGHT_BLUE  = RGB(133, 193, 233)    // #85C1E9 - Light variant
PRIMARY_GREEN       = RGB(39, 174, 96)      // #27AE60 - Success states
PRIMARY_DARK_GREEN  = RGB(30, 132, 73)      // #1E8449 - Dark variant
PRIMARY_LIGHT_GREEN = RGB(125, 206, 160)    // #7DCEA0 - Light variant
```

#### **Secondary Colors**
```java
// Supporting Colors
SECONDARY_ORANGE    = RGB(230, 126, 34)     // #E67E22 - Warnings
SECONDARY_RED       = RGB(231, 76, 60)      // #E74C3C - Errors
SECONDARY_PURPLE    = RGB(155, 89, 182)     // #9B59B6 - Admin features
SECONDARY_YELLOW    = RGB(241, 196, 15)     // #F1C40F - Highlights
```

#### **Status Colors**
```java
// Notification & Status Colors
SUCCESS             = RGB(46, 204, 113)     // #2ECC71 - Success
WARNING             = RGB(243, 156, 18)     // #F39C12 - Warning
ERROR               = RGB(231, 76, 60)      // #E74C3C - Error
INFO                = RGB(52, 152, 219)     // #3498DB - Information
```

#### **Neutral Colors**
```java
// Text & Backgrounds
WHITE               = RGB(255, 255, 255)    // #FFFFFF - Pure white
LIGHT_GRAY          = RGB(245, 245, 245)    // #F5F5F5 - Light background
MEDIUM_GRAY         = RGB(189, 195, 199)    // #BDC3C7 - Medium gray
DARK_GRAY           = RGB(127, 140, 141)    // #7F8C8D - Dark gray
CHARCOAL            = RGB(52, 73, 94)       // #34495E - Charcoal
BLACK               = RGB(44, 62, 80)       // #2C3E50 - Primary text
```

#### **Background Colors**
```java
// Background System
BG_PRIMARY          = RGB(236, 240, 241)    // #ECF0F1 - Main background
BG_SECONDARY        = RGB(255, 255, 255)    // #FFFFFF - Card background
BG_CARD             = RGB(250, 250, 250)    // #FAFAFA - Card background alt
BG_SIDEBAR          = RGB(44, 62, 80)       // #2C3E50 - Sidebar
BG_HEADER           = RGB(41, 128, 185)     // #2980B9 - Header
```

### 🎯 TYPOGRAPHY

```java
// Font System
PRIMARY_FONT     = "Segoe UI, Arial, sans-serif"
MONO_FONT        = "Consolas, monospace"

// Font Sizes
H1_SIZE          = 32px  // Page titles
H2_SIZE          = 24px  // Section headers
H3_SIZE          = 18px  // Card headers
BODY_SIZE        = 14px  // Normal text
SMALL_SIZE       = 12px  // Labels, metadata
TINY_SIZE        = 10px  // Timestamps
```

### 📐 SPACING & LAYOUT

```java
// Spacing Scale (8px base)
XS = 4px    // Tight spacing
SM = 8px    // Small spacing
MD = 16px   // Medium spacing
LG = 24px   // Large spacing
XL = 32px   // Extra large spacing
XXL = 48px  // Section spacing

// Component Sizes
BUTTON_HEIGHT    = 36px
INPUT_HEIGHT     = 40px
CARD_RADIUS      = 8px
BUTTON_RADIUS    = 6px
INPUT_RADIUS     = 4px
```

---

## 🖼️ SCREEN LAYOUTS

### 1. 🏠 **WELCOME SCREEN**

```
┌─────────────────────────────────────────────────────────────┐
│                    SIPRIMA DESA TARABBI                    │
│                 Sistem Pengaduan Masyarakat               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│           🏛️                                               │
│      [LOGO DESA TARABBI]                                   │
│                                                             │
│    "Melayani Dengan Hati, Mendengar Setiap Aspirasi"      │
│                                                             │
│         ┌─────────────┐  ┌─────────────┐                 │
│         │  🔐 MASUK   │  │ 📋 DAFTAR   │                 │
│         │    SISTEM   │  │   ADUAN     │                 │
│         └─────────────┘  └─────────────┘                 │
│                                                             │
│                                                             │
│    📞 Kontak: (0411) 123-4567 | 📧 admin@desatarabbi.go.id │
└─────────────────────────────────────────────────────────────┘
```

### 2. 🔐 **LOGIN SCREEN**

```
┌─────────────────────────────────────────────────────────────┐
│  ← Kembali          MASUK SISTEM          🏠 Beranda        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│                    🔐 LOGIN PETUGAS                        │
│                                                             │
│         ┌─────────────────────────────────────────┐       │
│         │ 👤 Username atau Email                  │       │
│         │ ________________________________       │       │
│         │                                         │       │
│         │ 🔒 Password                             │       │
│         │ ________________________________       │       │
│         │                                         │       │
│         │  ☐ Ingat saya    Lupa password? →      │       │
│         │                                         │       │
│         │    ┌─────────────────────────────┐     │       │
│         │    │      🚪 MASUK SISTEM        │     │       │
│         │    └─────────────────────────────┘     │       │
│         │                                         │       │
│         │  Role: ○ Petugas ○ Supervisor ○ Admin  │       │
│         └─────────────────────────────────────────┘       │
│                                                             │
│              💡 Hubungi admin untuk akun baru              │
└─────────────────────────────────────────────────────────────┘
```

### 3. 📊 **DASHBOARD PETUGAS**

```
┌─────────────────────────────────────────────────────────────┐
│🏠 Beranda │📋 Aduan │👥 Masyarakat │📊 Laporan │👤 Febry ▼│
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  🌅 Selamat Pagi, Febry!          📅 Senin, 16 Jun 2025   │
│  Petugas Desa Tarabbi                    🕐 09:30 WIB      │
│                                                             │
├─────────────────┬─────────────────┬─────────────────────────┤
│  📋 ADUAN BARU  │ ⚙️ DIPROSES     │   📈 STATISTIK HARI INI │
│       12        │       8         │                         │
│   ↗️ +3 hari ini │  ↗️ +2 hari ini │  • Total Aduan: 156    │
├─────────────────┼─────────────────┤  • Selesai: 89 (57%)   │
│  ✅ SELESAI     │ 🚨 DARURAT      │  • Proses: 45 (29%)    │
│       89        │       2         │  • Baru: 22 (14%)      │
│   ↗️ +5 hari ini │  ⚠️ Perlu aksi  │                         │
└─────────────────┴─────────────────┴─────────────────────────┤
│                                                             │
│  🚨 ADUAN PRIORITAS TINGGI                                 │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ 🔴 #001 Jalan Rusak Parah - Jl. Mawar Raya         │   │
│  │ 👤 Siti Aminah • 📍 RT 02/RW 01 • ⏰ 2 jam lalu    │   │
│  │ [👁️ Lihat] [📝 Proses] [📞 Hubungi]               │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │ 🟡 #002 Lampu Jalan Mati - Jl. Melati              │   │
│  │ 👤 Budi Santoso • 📍 RT 03/RW 02 • ⏰ 4 jam lalu   │   │
│  │ [👁️ Lihat] [📝 Proses] [📞 Hubungi]               │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  🔔 NOTIFIKASI TERBARU                   [👁️ Lihat Semua] │
│  • 📢 Rapat koordinasi besok pukul 09:00                   │
│  • ✅ Aduan #087 telah diselesaikan                        │
│  • 📋 3 aduan baru menunggu penanganan                     │
└─────────────────────────────────────────────────────────────┘
```

### 4. 📋 **DAFTAR ADUAN**

```
┌─────────────────────────────────────────────────────────────┐
│🏠 Beranda │📋 Aduan │👥 Masyarakat │📊 Laporan │👤 Febry ▼│
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  📋 MANAJEMEN ADUAN                      [🔍 Cari] [⚙️ Filter] │
│                                                             │
│  Status: [🆕 Semua ▼] Prioritas: [🎯 Semua ▼] Kategori: [📂 Semua ▼] │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  📊 RINGKASAN: 156 Total • 22 Baru • 45 Proses • 89 Selesai │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ ┌───────────────────────────────────────────────────────┐   │
│ │ 🔴 #001 • DARURAT • Jalan Rusak Parah               │   │
│ │ 📍 Jl. Mawar Raya, RT 02/RW 01                      │   │
│ │ 👤 Siti Aminah • 📅 16 Jun 2025 • ⏰ 07:30 WIB      │   │
│ │ 🏗️ Infrastruktur • 🆕 BARU                           │   │
│ │ [👁️ Detail] [📝 Proses] [📞 Hubungi] [📎 Files]    │   │
│ └───────────────────────────────────────────────────────┘   │
│                                                             │
│ ┌───────────────────────────────────────────────────────┐   │
│ │ 🟡 #002 • SEDANG • Lampu Jalan Mati                 │   │
│ │ 📍 Jl. Melati, RT 03/RW 02                          │   │
│ │ 👤 Budi Santoso • 📅 16 Jun 2025 • ⏰ 05:15 WIB     │   │
│ │ ⚡ Utilitas • ⚙️ DIPROSES                            │   │
│ │ [👁️ Detail] [✏️ Update] [📞 Hubungi] [📎 Files]    │   │
│ └───────────────────────────────────────────────────────┘   │
│                                                             │
│ ┌───────────────────────────────────────────────────────┐   │
│ │ 🟢 #003 • RENDAH • Sampah Tidak Diangkut            │   │
│ │ 📍 Jl. Anggrek, RT 01/RW 01                         │   │
│ │ 👤 Ratna Sari • 📅 15 Jun 2025 • ⏰ 16:45 WIB       │   │
│ │ 🗑️ Kebersihan • ✅ SELESAI                           │   │
│ │ [👁️ Detail] [📊 Report] [⭐ Rate] [📎 Files]       │   │
│ └───────────────────────────────────────────────────────┘   │
│                                                             │
│                  [← Prev] 1 2 3 ... 12 [Next →]            │
└─────────────────────────────────────────────────────────────┘
```

### 5. 📝 **FORM INPUT ADUAN**

```
┌─────────────────────────────────────────────────────────────┐
│  ← Kembali          INPUT ADUAN BARU         💾 Simpan       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  📝 FORMULIR ADUAN MASYARAKAT                              │
│                                                             │
│  👤 DATA PELAPOR                                           │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ Nama Lengkap: ________________________________     │   │
│  │ NIK: ________________  No. HP: _______________     │   │
│  │ Email: ________________________________________     │   │
│  │ Alamat: _______________________________________     │   │
│  │ RT/RW: _______ / _______  Desa: Tarabbi       │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  📋 DETAIL ADUAN                                           │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ Kategori: [🏗️ Infrastruktur ▼]                    │   │
│  │ Prioritas: [🔴 Darurat ▼]                          │   │
│  │ Lokasi Kejadian: ______________________________     │   │
│  │                                                     │   │
│  │ Judul Aduan:                                        │   │
│  │ _______________________________________________     │   │
│  │                                                     │   │
│  │ Deskripsi Lengkap:                                  │   │
│  │ ┌─────────────────────────────────────────────┐     │   │
│  │ │                                             │     │   │
│  │ │                                             │     │   │
│  │ │                                             │     │   │
│  │ │                                             │     │   │
│  │ │                                             │     │   │
│  │ └─────────────────────────────────────────────┘     │   │
│  │                                                     │   │
│  │ 📎 Lampiran: [📁 Pilih File] (Max 5 MB)            │   │
│  │ 📷 Foto: [📷 Ambil Foto] [🖼️ Upload Gambar]        │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│     [🚫 Batal]          [💾 Simpan Draft]    [📤 Kirim]    │
└─────────────────────────────────────────────────────────────┘
```

### 6. 👁️ **DETAIL ADUAN**

```
┌─────────────────────────────────────────────────────────────┐
│  ← Kembali     DETAIL ADUAN #001     [📝 Edit] [🗑️ Hapus]   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  🔴 DARURAT • Jalan Rusak Parah                            │
│  📅 Dibuat: 16 Jun 2025, 07:30 WIB  📊 Status: 🆕 BARU     │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  👤 PELAPOR                    │  📍 LOKASI                 │
│  Nama: Siti Aminah            │  Alamat: Jl. Mawar Raya    │
│  NIK: 1234567890123456        │  RT/RW: 02/01              │
│  HP: 0812-3456-7890          │  Desa: Tarabbi             │
│  Email: siti@email.com        │  Koordinat: -5.xxx, 119.xx │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  📋 DESKRIPSI ADUAN                                        │
│  Jalan di depan rumah saya sudah rusak parah sejak        │
│  seminggu yang lalu. Banyak lubang besar yang berbahaya   │
│  untuk kendaraan. Sudah ada beberapa motor yang jatuh.    │
│  Mohon segera diperbaiki karena ini jalur utama menuju    │
│  pasar desa.                                               │
│                                                             │
│  📎 LAMPIRAN                                               │
│  🖼️ foto_jalan_rusak_1.jpg (2.1 MB) [👁️ Lihat]          │
│  🖼️ foto_jalan_rusak_2.jpg (1.8 MB) [👁️ Lihat]          │
│  📄 surat_rt.pdf (856 KB) [📖 Buka]                       │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│  ⚙️ PENANGANAN                                             │
│  Petugas: [Pilih Petugas ▼]  Estimasi: [__ hari]         │
│  Catatan: ____________________________________________     │
│                                                             │
│  [🚀 Mulai Proses] [👥 Assign] [📞 Hubungi Pelapor]      │
├─────────────────────────────────────────────────────────────┤
│  📊 RIWAYAT STATUS                                         │
│  • ✅ 16 Jun 2025, 07:30 - Aduan diterima sistem          │
│  • 🔄 16 Jun 2025, 08:15 - Validasi admin                 │
│  • ⏳ Menunggu penugasan petugas...                        │
└─────────────────────────────────────────────────────────────┘
```

### 7. 📊 **DASHBOARD SUPERVISOR**

```
┌─────────────────────────────────────────────────────────────┐
│🏠 Dashboard │📊 Analytics │👥 Tim │⚙️ Pengaturan │👤 Admin ▼│
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  👑 DASHBOARD SUPERVISOR              📅 16 Juni 2025      │
│  Desa Tarabbi - Kabupaten Gowa           🕐 09:30 WIB      │
│                                                             │
├─────────────────┬─────────────────┬─────────────────────────┤
│  📈 PERFORMA    │ 👥 TIM AKTIF    │   🎯 TARGET BULAN INI   │
│     92.5%       │       12        │                         │
│  ↗️ +5.2% minggu │ dari 15 petugas │  • Response: <2 jam     │
├─────────────────┼─────────────────┤  • Resolusi: <7 hari    │
│  ⏱️ AVG RESPONSE│ 🏆 TOP PETUGAS  │  • Kepuasan: >85%       │
│    1.2 jam      │   Febry (94%)   │  📊 Progress: 87%       │
│  🎯 Target: 2 jam│  🥇 15 selesai   │                         │
└─────────────────┴─────────────────┴─────────────────────────┤
│                                                             │
│  📊 ANALISIS REAL-TIME                                     │
│  ┌─────────────────────────────────────────────────────┐   │
│  │     DISTRIBUSI ADUAN PER KATEGORI                  │   │
│  │  🏗️ Infrastruktur  ████████████████░░  67%        │   │
│  │  🗑️ Kebersihan     ████████░░░░░░░░░░  45%        │   │
│  │  ⚡ Utilitas       ██████░░░░░░░░░░░░  34%        │   │
│  │  🛡️ Keamanan      ████░░░░░░░░░░░░░░  23%        │   │
│  │  🏥 Kesehatan     ██░░░░░░░░░░░░░░░░  12%        │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  🚨 ALERT & MONITORING                                     │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ ⚠️  2 aduan DARURAT belum ditangani > 1 jam         │   │
│  │ 📊 Response time rata-rata naik 15 menit            │   │
│  │ 👥 3 petugas offline lebih dari 2 jam               │   │
│  │ 📈 Lonjakan aduan infrastruktur +45% minggu ini     │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  📋 ADUAN BUTUH ESKALASI           [👁️ Lihat Semua]       │
│  • #089 Jembatan Roboh - Butuh anggaran khusus             │
│  • #087 Konflik Warga - Perlu mediasi                      │
│  • #085 Pencemaran Air - Koordinasi Dinas Lingkungan       │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎨 UI COMPONENTS LIBRARY

### 🔘 **BUTTONS**

```
PRIMARY BUTTON:
┌─────────────────────┐
│  🔵 PRIMARY ACTION  │  ← Blue background, white text
└─────────────────────┘

SECONDARY BUTTON:
┌─────────────────────┐
│  ⚪ SECONDARY       │  ← Gray border, dark text
└─────────────────────┘

SUCCESS BUTTON:
┌─────────────────────┐
│  ✅ SUCCESS         │  ← Green background, white text
└─────────────────────┘

DANGER BUTTON:
┌─────────────────────┐
│  ❌ DANGER          │  ← Red background, white text
└─────────────────────┘

ICON BUTTON:
┌─────┐
│  🔍 │  ← Icon only, square
└─────┘
```

### 📝 **FORM ELEMENTS**

```
TEXT INPUT:
┌─────────────────────────────────────┐
│ 👤 Placeholder text...              │
└─────────────────────────────────────┘

SELECT DROPDOWN:
┌─────────────────────────────────────┐
│ 📂 Pilih Kategori                ▼ │
└─────────────────────────────────────┘

TEXTAREA:
┌─────────────────────────────────────┐
│ Deskripsi lengkap...                │
│                                     │
│                                     │
└─────────────────────────────────────┘

CHECKBOX:
☐ Unchecked option
☑️ Checked option

RADIO BUTTON:
○ Unselected option
● Selected option
```

### 📋 **CARDS & PANELS**

```
INFORMATION CARD:
┌─────────────────────────────────────┐
│ 📊 CARD TITLE                      │
├─────────────────────────────────────┤
│ Content area with relevant          │
│ information and actions             │
│                                     │
│        [Action Button]              │
└─────────────────────────────────────┘

STATUS CARD:
┌─────────────────────────────────────┐
│ 🟢 STATUS INDICATOR                 │
│ Main metric: 125                    │
│ ↗️ Change indicator                 │
└─────────────────────────────────────┘

LIST ITEM:
┌─────────────────────────────────────┐
│ 🔴 #001 • Priority • Title         │
│ 📍 Location • 👤 Reporter           │
│ [Action] [Action] [Action]          │
└─────────────────────────────────────┘
```

### 🔔 **NOTIFICATIONS & ALERTS**

```
SUCCESS ALERT:
┌─────────────────────────────────────┐
│ ✅ Operasi berhasil dilakukan!      │
└─────────────────────────────────────┘

WARNING ALERT:
┌─────────────────────────────────────┐
│ ⚠️ Perhatian! Ada yang perlu dicek  │
└─────────────────────────────────────┘

ERROR ALERT:
┌─────────────────────────────────────┐
│ ❌ Error! Silahkan coba lagi        │
└─────────────────────────────────────┘

INFO ALERT:
┌─────────────────────────────────────┐
│ ℹ️ Informasi tambahan untuk Anda    │
└─────────────────────────────────────┘
```

---

## 📱 RESPONSIVE BEHAVIOR

### 🖥️ **DESKTOP (1024px+)**
- Full sidebar navigation
- Multi-column layouts
- Detailed data tables
- Rich interactions

### 💻 **TABLET (768px - 1023px)**
- Collapsible sidebar
- 2-column layouts
- Simplified tables
- Touch-optimized buttons

### 📱 **MOBILE (< 768px)**
- Bottom navigation
- Single column
- Card-based layouts
- Large touch targets

---

## 🎯 DESIGN PRINCIPLES

### ✨ **ACCESSIBILITY**
- Minimum contrast ratio 4.5:1
- Keyboard navigation support
- Screen reader compatibility
- Clear focus indicators

### 🚀 **PERFORMANCE**
- Lazy loading for images
- Efficient component rendering
- Minimal resource usage
- Fast interaction feedback

### 🎨 **CONSISTENCY**
- Unified color system
- Consistent spacing
- Standard component library
- Predictable interactions

### 👥 **USER EXPERIENCE**
- Intuitive navigation
- Clear action feedback
- Helpful error messages
- Progressive disclosure

---

## 🛠️ IMPLEMENTATION GUIDE

### 📁 **File Structure** ✅ *Sudah Diimplementasi*
```
src/
├── Theme/                          # ✅ SELESAI
│   ├── SiprimaPalette.java         # ✅ Color palette lengkap
│   ├── CustomButtton.java          # ✅ Custom button dengan berbagai type
│   ├── CustomTextField.java        # ✅ Custom text field dengan placeholder
│   ├── CustomPanel.java            # ✅ Custom panel dengan berbagai style
│   ├── CustomTable.java            # ✅ Custom table dengan hover effect
│   ├── ThemeManager.java           # ✅ Theme manager untuk styling
│   ├── ThemeExample.java           # ✅ Demo lengkap semua komponen
│   └── java.java                   # ✅ NetBeans-compatible demo form
├── Auth/                           # 🔄 PERLU UPDATE THEME
│   └── FormLogin.java/.form
├── dashboard/                      # 🔄 PERLU UPDATE THEME
│   ├── DashboardPetugas.java/.form
│   └── DashboardSuperVisor.java/.form
├── aduan/                          # 🔄 PERLU UPDATE THEME
│   ├── FormInputanAduan.java/.form
│   └── FormDetailAduan.java/.form
├── Pengguna/                       # 🔄 PERLU UPDATE THEME
│   ├── FormProfil.java/.form
│   └── ManajemenUser.java/.form
├── models/                         # ✅ SUDAH ADA
│   ├── User.java
│   └── Aduan.java
└── Utils/                          # ✅ SUDAH ADA
    ├── DatabaseConfig.java
    └── SessionManager.java
```

### 🔧 **NetBeans GUI Integration**

#### **1. Menambahkan Custom Components ke Palette NetBeans**
```java
// Di NetBeans, tambahkan ke Palette:
// 1. Klik kanan pada Palette > Palette Manager
// 2. Add from JAR... > Pilih compiled JAR project
// 3. Atau Add from Project > Pilih folder Theme/

// Komponen yang tersedia:
CustomButtton     - Tombol dengan berbagai style dan size
CustomTextField   - Text field dengan placeholder dan validation
CustomPanel       - Panel dengan rounded corners dan shadow
CustomTable       - Table dengan hover effect dan styling
```

#### **2. Cara Menggunakan di NetBeans Form Designer**
```java
// Langkah 1: Inisialisasi tema di constructor
public FormLogin() {
    ThemeManager.initializeTheme();  // Wajib di awal
    initComponents();
    setupCustomStyling();            // Styling tambahan
}

// Langkah 2: Deklarasi komponen custom (bisa drag-drop dari palette)
private CustomButtton btnLogin;
private CustomTextField txtUsername;
private CustomPanel panelMain;

// Langkah 3: Inisialisasi di initComponents() atau setelahnya
private void setupCustomStyling() {
    // Auto-style semua komponen
    ThemeManager.styleComponent(this);
    
    // Atau style manual
    btnLogin = new CustomButtton("Login", CustomButtton.ButtonType.PRIMARY);
    txtUsername = new CustomTextField("Masukkan username");
    panelMain = new CustomPanel(CustomPanel.PanelType.CARD);
}
```

#### **3. Template Form NetBeans-Ready**
```java
// Template yang bisa langsung digunakan:
// File: SiprimaThemeDemo.java (sudah tersedia)

// Fitur:
// ✅ Compatible dengan NetBeans GUI Designer
// ✅ Semua komponen custom tersedia
// ✅ Event handlers sudah setup
// ✅ Getter/setter untuk property binding
// ✅ Live preview di design mode
```

### 🎨 **Theme Implementation**
```java
// ColorPalette.java
public class ColorPalette {
    public static final Color PRIMARY_BLUE = new Color(24, 119, 242);
    public static final Color SUCCESS_GREEN = new Color(66, 183, 42);
    // ... other colors
}

// CustomButton.java
public class CustomButton extends JButton {
    public CustomButton(String text, ButtonType type) {
        super(text);
        applyTheme(type);
    }
    
    private void applyTheme(ButtonType type) {
        switch(type) {
            case PRIMARY:
                setBackground(ColorPalette.PRIMARY_BLUE);
                setForeground(Color.WHITE);
                break;
            // ... other cases
        }
    }
}
```

---

## ✅ DESIGN CHECKLIST

### 🎨 **Visual Design**
- [ ] Color palette implemented
- [ ] Typography system defined
- [ ] Icon library created
- [ ] Component library built
- [ ] Spacing system applied

### 🖱️ **Interaction Design**
- [ ] Navigation flow mapped
- [ ] Button states defined
- [ ] Form validation designed
- [ ] Loading states planned
- [ ] Error handling designed

### 📱 **Responsive Design**
- [ ] Desktop layout created
- [ ] Tablet adaptation planned
- [ ] Mobile version designed
- [ ] Touch interactions optimized

### ♿ **Accessibility**
- [ ] Color contrast verified
- [ ] Keyboard navigation planned
- [ ] Screen reader support
- [ ] Focus indicators designed

---

---

## 🗄️ DATABASE DESIGN

### 📊 **Entity Relationship Diagram**
```sql
-- TABEL USERS (Pengguna Sistem)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('masyarakat', 'petugas', 'supervisor', 'admin') NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    rt_rw VARCHAR(10),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- TABEL CATEGORIES (Kategori Aduan)
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    icon VARCHAR(50),
    color VARCHAR(7) DEFAULT '#2980B9',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TABEL COMPLAINTS (Aduan)
CREATE TABLE complaints (
    id INT PRIMARY KEY AUTO_INCREMENT,
    complaint_number VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    category_id INT NOT NULL,
    reporter_id INT NOT NULL,
    assigned_to INT NULL,
    priority ENUM('rendah', 'sedang', 'tinggi', 'darurat') DEFAULT 'sedang',
    status ENUM('baru', 'validasi', 'proses', 'selesai', 'ditolak') DEFAULT 'baru',
    location_address TEXT NOT NULL,
    rt_rw VARCHAR(10),
    latitude DECIMAL(10, 8) NULL,
    longitude DECIMAL(11, 8) NULL,
    expected_completion DATE NULL,
    actual_completion DATE NULL,
    rating INT NULL CHECK (rating >= 1 AND rating <= 5),
    feedback TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (reporter_id) REFERENCES users(id),
    FOREIGN KEY (assigned_to) REFERENCES users(id)
);

-- TABEL ATTACHMENTS (Lampiran)
CREATE TABLE attachments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    complaint_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    uploaded_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (complaint_id) REFERENCES complaints(id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES users(id)
);

-- TABEL COMPLAINT_LOGS (Riwayat Status)
CREATE TABLE complaint_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    complaint_id INT NOT NULL,
    old_status VARCHAR(20),
    new_status VARCHAR(20) NOT NULL,
    notes TEXT,
    changed_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (complaint_id) REFERENCES complaints(id) ON DELETE CASCADE,
    FOREIGN KEY (changed_by) REFERENCES users(id)
);

-- TABEL NOTIFICATIONS (Notifikasi)
CREATE TABLE notifications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    complaint_id INT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type ENUM('info', 'success', 'warning', 'error') DEFAULT 'info',
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (complaint_id) REFERENCES complaints(id) ON DELETE SET NULL
);

-- INSERT DATA AWAL
INSERT INTO categories (name, description, icon, color) VALUES
('Infrastruktur', 'Jalan, jembatan, gedung publik', '🏗️', '#E67E22'),
('Kebersihan', 'Sampah, drainase, kebersihan lingkungan', '🗑️', '#27AE60'),
('Utilitas', 'Listrik, air, telekomunikasi', '⚡', '#F39C12'),
('Keamanan', 'Keamanan dan ketertiban', '🛡️', '#E74C3C'),
('Kesehatan', 'Fasilitas kesehatan, sanitasi', '🏥', '#9B59B6'),
('Pendidikan', 'Fasilitas pendidikan', '📚', '#3498DB'),
('Sosial', 'Masalah sosial kemasyarakatan', '👥', '#1ABC9C');

INSERT INTO users (username, email, password_hash, full_name, role, phone) VALUES
('admin', 'admin@desatarabbi.go.id', '$2a$10$...', 'Administrator', 'admin', '081234567890'),
('supervisor', 'supervisor@desatarabbi.go.id', '$2a$10$...', 'Supervisor Desa', 'supervisor', '081234567891'),
('petugas1', 'petugas1@desatarabbi.go.id', '$2a$10$...', 'Febriansyah', 'petugas', '081234567892');
```

### 🔧 **Database Configuration (Java)**
```java
// File: Utils/DatabaseConfig.java (sudah ada, perlu update)
public class DatabaseConfig {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/siprima_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}

// File: models/User.java (sudah ada, perlu update)
public class User {
    private int id;
    private String username;
    private String email;
    private String fullName;
    private UserRole role;
    private String phone;
    private boolean isActive;
    // ... getters, setters, methods
}

// File: models/Aduan.java (sudah ada, perlu update)
public class Aduan {
    private int id;
    private String complaintNumber;
    private String title;
    private String description;
    private Category category;
    private User reporter;
    private Priority priority;
    private Status status;
    private String locationAddress;
    // ... getters, setters, methods
}
```

---

## 🎯 PROJECT ROADMAP - 20% COMPLETION

### ✅ **PHASE 1: FOUNDATION (SELESAI)**
- [x] ✅ Project structure setup
- [x] ✅ Theme system implementation (SiprimaPalette)
- [x] ✅ Custom components (Button, TextField, Panel, Table)
- [x] ✅ Theme manager
- [x] ✅ NetBeans integration ready
- [x] ✅ Documentation (UI Design)

### 🔄 **PHASE 2: CORE FEATURES (TARGET 20%)**

#### **Week 1-2: Database & Authentication**
- [ ] 🗄️ Setup database MySQL
- [ ] 🔧 Update DatabaseConfig.java
- [ ] 📊 Update models (User.java, Aduan.java)
- [ ] 🔐 Implement FormLogin dengan theme
- [ ] 🏠 Update WelcomeForm dengan theme
- [ ] 🔒 Basic authentication system

#### **Week 3: Dashboard Implementation**
- [ ] 📊 Update DashboardPetugas dengan theme
- [ ] 👑 Update DashboardSuperVisor dengan theme
- [ ] 📈 Basic dashboard widgets
- [ ] 🎨 Apply custom components

#### **Week 4: Form Management**
- [ ] 📝 Update FormInputanAduan dengan theme
- [ ] 👁️ Update FormDetailAduan dengan theme
- [ ] 👤 Update FormProfil dengan theme
- [ ] 👥 Update ManajemenUser dengan theme

### 🎯 **DELIVERABLES - 20% TARGET**
1. ✅ **Working Login System** - Authentication dengan database
2. ✅ **Themed Dashboard** - Dashboard dengan custom components
3. ✅ **Basic CRUD Aduan** - Input, view, edit aduan sederhana
4. ✅ **User Management** - Basic user management
5. ✅ **Consistent UI** - Semua form menggunakan theme system

---

## 🚀 **GETTING STARTED - IMPLEMENTASI 20%**

### **Step 1: Setup Database**
```sql
-- 1. Buat database
CREATE DATABASE siprima_db;
USE siprima_db;

-- 2. Jalankan script SQL di atas
-- 3. Test koneksi dari Java
```

### **Step 2: Update Existing Forms**
```java
// Untuk setiap form yang sudah ada:

// 1. Tambahkan di constructor:
ThemeManager.initializeTheme();

// 2. Setelah initComponents():
setupCustomStyling();

// 3. Buat method baru:
private void setupCustomStyling() {
    ThemeManager.styleComponent(this);
    
    // Replace komponen standard dengan custom:
    // JButton -> CustomButtton
    // JTextField -> CustomTextField
    // JPanel -> CustomPanel
}
```

### **Step 3: Test & Iterate**
```java
// 1. Compile project
// 2. Test SiprimaThemeDemo.java
// 3. Test login functionality
// 4. Test basic CRUD operations
// 5. Verify UI consistency
```

---

## 👥 **TEAM MEMBERS & RESPONSIBILITIES**

### **🧑‍💻 Febriansyah (Lead Developer)**
- ✅ Theme system implementation
- 🔄 Database design & setup
- 🔄 Authentication system
- 🔄 Main dashboard development

### **👩‍💻 [Team Member 2]**
- 🔄 Form development (Aduan)
- 🔄 User management features
- 🔄 Testing & quality assurance

### **👨‍💻 [Team Member 3]**
- 🔄 Report generation
- 🔄 Data visualization
- 🔄 Documentation updates

*💡 Tip: Assign team members sesuai keahlian masing-masing*

---

## 📋 **20% COMPLETION CHECKLIST**

### **🎨 UI/UX Foundation**
- [x] ✅ Color palette implemented
- [x] ✅ Custom components created
- [x] ✅ Theme manager ready
- [x] ✅ NetBeans integration
- [ ] 🔄 All forms themed consistently

### **🗄️ Database & Backend**
- [ ] 🔄 MySQL database setup
- [ ] 🔄 All tables created
- [ ] 🔄 Sample data inserted
- [ ] 🔄 Java models updated
- [ ] 🔄 Database connectivity tested

### **🔐 Authentication**
- [ ] 🔄 Login form functional
- [ ] 🔄 Role-based access
- [ ] 🔄 Session management
- [ ] 🔄 Password hashing

### **📊 Core Features**
- [ ] 🔄 Dashboard with real data
- [ ] 🔄 Basic CRUD operations
- [ ] 🔄 Form validation
- [ ] 🔄 Navigation between forms

### **🧪 Testing**
- [ ] 🔄 Login/logout functionality
- [ ] 🔄 Database operations
- [ ] 🔄 UI responsiveness
- [ ] 🔄 Error handling

---

## 🔄 **NEXT STEPS**

1. **✅ Theme Implementation** - Setup color palette dan typography *(SELESAI)*
2. **✅ Component Library** - Buat custom components *(SELESAI)*
3. **🔄 Database Setup** - Implement database dan models *(IN PROGRESS)*
4. **🔄 Authentication System** - Setup login dan session management *(NEXT)*
5. **🔄 Form System** - Update existing forms dengan theme *(NEXT)*
6. **⏳ Testing & Refinement** - Test functionality dan usability *(UPCOMING)*

---

*📝 Dokumentasi ini akan terus diupdate seiring perkembangan project*

**Author**: SIPRIMA Development Team  
**Lead Developer**: Febriansyah  
**Version**: 2.0  
**Last Updated**: 16 Juni 2025  
**Target Completion 20%**: 30 Juni 2025

