# 🏛️ SIPRIMA - Sistem Informasi Pengaduan Masyarakat

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![NetBeans](https://img.shields.io/badge/NetBeans-IDE-green.svg)](https://netbeans.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **Sistem Informasi Pengaduan Masyarakat Desa Tarabbi**  
> Aplikasi desktop modern untuk digitalisasi pengelolaan pengaduan masyarakat

## 📋 Deskripsi Proyek

SIPRIMA adalah aplikasi desktop berbasis Java Swing yang dirancang untuk mengelola pengaduan masyarakat di Desa Tarabbi. Aplikasi ini mengimplementasikan konsep e-government untuk meningkatkan transparansi dan efisiensi pelayanan publik.

### 🌟 Fitur Utama

- ✅ **Multi-role Authentication** - Masyarakat, Petugas, Supervisor, Admin
- ✅ **Real-time Complaint Tracking** - Pelacakan status pengaduan secara real-time
- ✅ **Modern UI/UX** - Antarmuka modern dengan custom theme system
- ✅ **Database Integration** - Integrasi dengan MySQL database
- ✅ **Report Generation** - Pembuatan laporan dan analisis data
- ✅ **File Attachment Support** - Dukungan upload file pendukung
- ✅ **Session Management** - Manajemen sesi dengan fitur keamanan
- ✅ **Role-based Access Control** - Kontrol akses berdasarkan peran pengguna

## 🚀 Demo & Screenshots

### Login Interface
![Login Screen](docs/screenshots/login.png)

### Dashboard Petugas
![Dashboard](docs/screenshots/dashboard.png)

### Manajemen Pengaduan
![Complaint Management](docs/screenshots/complaint-management.png)

## 🏗️ Arsitektur Sistem

### Design Pattern yang Digunakan
- **MVC (Model-View-Controller)** - Pemisahan logika aplikasi
- **Singleton Pattern** - Untuk SessionManager dan DatabaseConfig
- **Observer Pattern** - Untuk real-time updates
- **Factory Pattern** - Untuk custom UI components

### Struktur Package
```
src/
├── Auth/              # Autentikasi dan otorisasi
├── aduan/             # Manajemen pengaduan
├── dashboard/         # Dashboard dan statistik
├── models/            # Data models
├── Utils/             # Utility classes
├── Theme/             # Custom UI components
├── Pengguna/          # Manajemen pengguna
├── Laporan/           # Sistem pelaporan
├── Profil/            # Profil pengguna
└── Home/              # Halaman utama
```

## 💻 Teknologi yang Digunakan

- **Java 17+** - Bahasa pemrograman utama
- **Java Swing** - Framework GUI
- **MySQL 8.0+** - Database management system
- **JDBC** - Database connectivity
- **NetBeans IDE** - Development environment

## 🔧 Instalasi dan Setup

### Prasyarat
- Java Development Kit (JDK) 17 atau lebih tinggi
- MySQL Server 8.0 atau lebih tinggi
- NetBeans IDE (opsional)

### Langkah Instalasi

1. **Clone repository**
   ```bash
   git clone https://github.com/[username]/siprima-desa-tarabbi.git
   cd siprima-desa-tarabbi
   ```

2. **Setup Database**
   ```sql
   -- Buat database
   CREATE DATABASE siprima_db;
   
   -- Import schema
   mysql -u root -p siprima_db < database/siprima_setup.sql
   
   -- Import sample data
   mysql -u root -p siprima_db < database/siprima_db.sql
   ```

3. **Konfigurasi Database**
   - Edit file `src/Utils/DatabaseConfig.java`
   - Sesuaikan connection string dengan konfigurasi MySQL Anda
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/siprima_db";
   private static final String USERNAME = "root";
   private static final String PASSWORD = "your_password";
   ```

4. **Compile dan Run**
   ```bash
   # Menggunakan NetBeans
   # File -> Open Project -> Pilih folder proyek
   # Klik kanan pada project -> Run
   
   # Atau menggunakan command line
   javac -cp ".:lib/*" src/**/*.java
   java -cp ".:lib/*" Auth.FormLogin
   ```

## 👥 User Roles & Akses

### 1. Masyarakat
- Membuat pengaduan baru
- Melihat status pengaduan
- Update profil

### 2. Petugas
- Melihat daftar pengaduan
- Update status pengaduan
- Mengelola data pengaduan

### 3. Supervisor
- Semua akses petugas
- Melihat dashboard analytics
- Generate laporan
- Mengelola petugas

### 4. Admin
- Full system access
- Manajemen pengguna
- Konfigurasi sistem
- Backup dan maintenance

## 📊 Database Schema

### Tabel Utama
- **users** - Data pengguna dan autentikasi
- **complaints** - Data pengaduan masyarakat
- **attachments** - File pendukung pengaduan
- **notifications** - Sistem notifikasi
- **reports** - Log dan laporan sistem

### Entity Relationship Diagram
```
Users (1) -----> (N) Complaints
Complaints (1) -> (N) Attachments
Users (1) -----> (N) Notifications
```

## 🎨 Custom UI Components

### SiprimaPalette
- 70+ predefined colors
- Government-friendly color scheme
- Utility methods untuk manipulasi warna

### CustomButton
- Multiple button types (PRIMARY, SUCCESS, WARNING, DANGER)
- Hover effects dan animations
- Consistent sizing dan typography

### CustomTable
- Enhanced JTable dengan modern styling
- Custom cell renderers
- Pagination support

## 🔐 Security Features

- **Password Hashing** - SHA-256 + Salt
- **Session Management** - Timeout dan validation
- **Role-based Access Control** - Granular permissions
- **SQL Injection Prevention** - Prepared statements
- **Input Validation** - Comprehensive form validation

## 📈 Performance Features

- **Connection Pooling** - Efficient database connections
- **Lazy Loading** - Optimized data loading
- **Event-driven Updates** - Real-time UI updates
- **Memory Management** - Proper resource cleanup

## 🧪 Testing

### Unit Tests
- DatabaseConfig connection testing
- Authentication flow testing
- Password verification testing

### Integration Tests
- End-to-end form submission
- Real-time event propagation
- Database CRUD operations

## 📚 Dokumentasi

- [Development Process](DEVELOPMENT_PROCESS.md) - Proses development lengkap
- [Technical Explanation](TECHNICAL_EXPLANATION.md) - Penjelasan teknis detail
- [UI Design Guide](SIPRIMA_UI_DESIGN.md) - Panduan desain UI
- [Database Schema](database/README.md) - Dokumentasi database

## 🤝 Kontribusi

1. Fork repository ini
2. Buat branch fitur baru (`git checkout -b feature/AmazingFeature`)
3. Commit perubahan (`git commit -m 'Add some AmazingFeature'`)
4. Push ke branch (`git push origin feature/AmazingFeature`)
5. Buat Pull Request

## 📄 License

Proyek ini dilisensikan di bawah MIT License - lihat file [LICENSE](LICENSE) untuk detail.

## 👥 Tim Pengembang

**Kelompok 3 Pemrograman Visual**

| No | Nama Anggota | Role / Kontribusi | GitHub | Spesialisasi |
|----|--------------|-------------------|--------|--------------|
| 1  | **Febryanus Tambing** | Lead Developer, Backend  | [@ebryany](https://github.com/ebryany) | 🏗️ Arsitektur Program, Code Development, Backend Implementation |
| 2  | **Alexzius Manan** | UI/UX Designer | - | 🎨 User Interface Design, User Experience |
| 3  | **Putri Rembulan** | UI/UX Designer | - | 🎨 Visual Design, Interface Layout |
| 4  | **Auliawati** | UI/UX Designer | - | 🎨 Graphic Design, Visual Elements |
| 5  | **Isma** | UI/UX Designer | - | 🎨 Design System, User Interface |

### 📊 Distribusi Kontribusi

```
🔧 Backend Development & Architecture: 60%
  └── Febryanus Tambing

🎨 UI/UX Design & Frontend: 40%
  ├── Alexzius Manan (10%)
  ├── Putri Rembulan (10%)
  ├── Auliawati (10%)
  └── Isma (10%)
```

### 🎯 Detail Kontribusi

#### 🏗️ **Lead Developer - Febryanus Tambing**
- ✅ Project Architecture & Structure
- ✅ Database Design & Implementation
- ✅ Backend Logic & API Development
- ✅ Authentication & Security System
- ✅ Custom UI Components (Java Swing)
- ✅ Real-time Features & Event System
- ✅ Documentation & Code Quality
- ✅ Testing & Deployment

#### 🎨 **UI/UX Design Team**
- **Alexzius Manan**: Interface conceptualization & wireframing
- **Putri Rembulan**: Visual design & layout composition
- **Auliawati**: Graphic elements & icon design
- **Isma**: Design system & user experience flow

### 🌟 Acknowledgments

Terima kasih kepada seluruh anggota **Kelompok 3** yang telah berkontribusi dalam pengembangan SIPRIMA. Setiap anggota memiliki peran penting dalam kesuksesan project ini.

---

**Contact Lead Developer:**
- GitHub: [@ebryany](https://github.com/ebryany)
- Web: [febryanidh.vercel.app](https://febryanidh.vercel.app)
- Email: ebryany6@gmail.com

## 🙏 Acknowledgments

- Dosen Pemrograman Visual untuk guidance dan feedback
- Komunitas Java Indonesia untuk resource dan support
- Oracle Java documentation untuk best practices

## 📞 Support

Jika Anda memiliki pertanyaan atau masalah:
- Buat issue di GitHub repository
- Email ke: ebryany6@gmail.com
- Atau hubungi melalui LinkedIn

---

<p align="center">
  <strong>SIPRIMA - Digitalisasi Pelayanan Publik untuk Desa yang Lebih Baik</strong>
</p>

<p align="center">
  ❤️ for Desa Tarabbi
</p>

