# 🚀 Panduan Instalasi SIPRIMA

> Panduan lengkap untuk menginstal dan menjalankan SIPRIMA di lingkungan lokal Anda

## 📋 Prasyarat Sistem

### Perangkat Lunak yang Diperlukan
- **Java Development Kit (JDK) 17 atau lebih tinggi**
- **MySQL Server 8.0 atau lebih tinggi**
- **NetBeans IDE 12+ (Opsional)**
- **Git (untuk clone repository)**

### Spesifikasi Minimum
- **RAM**: 4GB (8GB direkomendasikan)
- **Storage**: 2GB ruang kosong
- **OS**: Windows 10+, macOS 10.15+, atau Linux Ubuntu 18.04+

## 🔧 Langkah Instalasi

### 1. Persiapan Environment

#### Install Java JDK
```bash
# Verifikasi Java terinstall
java -version
javac -version

# Jika belum terinstall, download dari:
# https://www.oracle.com/java/technologies/downloads/
```

#### Install MySQL
```bash
# Download MySQL dari: https://dev.mysql.com/downloads/mysql/
# Atau gunakan package manager

# Ubuntu/Debian
sudo apt update
sudo apt install mysql-server

# macOS (dengan Homebrew)
brew install mysql

# Windows
# Download MySQL Installer dari website resmi
```

### 2. Clone Repository

```bash
# Clone dari GitHub
git clone https://github.com/[username]/siprima-desa-tarabbi.git

# Masuk ke direktori project
cd siprima-desa-tarabbi

# Lihat struktur folder
ls -la
```

### 3. Setup Database

#### Jalankan MySQL Server
```bash
# Linux/macOS
sudo systemctl start mysql
# atau
sudo service mysql start

# Windows
# Start MySQL dari Services atau MySQL Workbench
```

#### Buat Database
```sql
-- Login ke MySQL
mysql -u root -p

-- Buat database
CREATE DATABASE siprima_db;

-- Pilih database
USE siprima_db;

-- Verifikasi database telah dibuat
SHOW DATABASES;
```

#### Import Schema Database
```bash
# Import schema database
mysql -u root -p siprima_db < database/siprima_setup.sql

# Import sample data
mysql -u root -p siprima_db < database/siprima_db.sql

# Verifikasi tables telah dibuat
mysql -u root -p -e "USE siprima_db; SHOW TABLES;"
```

### 4. Konfigurasi Database Connection

#### Edit DatabaseConfig.java
```java
// File: src/Utils/DatabaseConfig.java

public class DatabaseConfig {
    // Sesuaikan dengan konfigurasi MySQL Anda
    private static final String URL = "jdbc:mysql://localhost:3306/siprima_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "your_mysql_password";
    
    // Jangan lupa ganti password dengan password MySQL Anda
}
```

#### Test Database Connection
```bash
# Compile dan test database connection
javac -cp ".:lib/*" src/Utils/DatabaseConfig.java
java -cp ".:lib/*" Utils.DatabaseConfig

# Jika berhasil, akan muncul pesan "Connection successful!"
```

### 5. Compile dan Run Project

#### Menggunakan NetBeans IDE
1. Buka NetBeans IDE
2. File → Open Project
3. Pilih folder `siprima-desa-tarabbi`
4. Klik kanan pada project → Run

#### Menggunakan Command Line
```bash
# Compile semua Java files
find src -name "*.java" -exec javac -cp ".:lib/*" {} \;

# Run aplikasi
java -cp ".:lib/*:src" Auth.FormLogin

# Atau run dengan script
./run.sh  # Linux/macOS
./run.bat # Windows
```

## 🔐 Default Login Credentials

### Admin Account
- **Username**: admin
- **Password**: admin123
- **Role**: Admin

### Supervisor Account
- **Username**: supervisor
- **Password**: supervisor123
- **Role**: Supervisor

### Petugas Account
- **Username**: petugas
- **Password**: petugas123
- **Role**: Petugas

### Masyarakat Account
- **Username**: masyarakat
- **Password**: masyarakat123
- **Role**: Masyarakat

## 🐛 Troubleshooting

### Database Connection Error
```
Problem: java.sql.SQLException: Access denied for user 'root'@'localhost'
Solution: 
1. Cek username dan password di DatabaseConfig.java
2. Reset password MySQL jika perlu
3. Pastikan MySQL server berjalan
```

### ClassNotFoundException
```
Problem: java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver
Solution:
1. Download MySQL Connector/J dari https://dev.mysql.com/downloads/connector/j/
2. Tambahkan ke classpath atau folder lib/
3. Restart aplikasi
```

### Port Already in Use
```
Problem: MySQL port 3306 already in use
Solution:
1. Stop MySQL service yang berjalan
2. Atau ubah port di DatabaseConfig.java
3. Restart MySQL dengan port yang berbeda
```

### Java Version Issues
```
Problem: UnsupportedClassVersionError
Solution:
1. Pastikan menggunakan JDK 17+
2. Set JAVA_HOME environment variable
3. Recompile project dengan versi Java yang benar
```

## 📁 Struktur Project Setelah Setup

```
siprima-desa-tarabbi/
├── src/
│   ├── Auth/
│   ├── aduan/
│   ├── dashboard/
│   ├── models/
│   ├── Utils/
│   ├── Theme/
│   └── ...
├── database/
│   ├── siprima_setup.sql
│   └── siprima_db.sql
├── lib/
│   └── mysql-connector-java-8.0.33.jar
├── build/
├── dist/
├── nbproject/
├── README.md
└── INSTALLATION_GUIDE.md
```

## 🎯 Langkah Selanjutnya

Setelah instalasi berhasil:

1. **Login ke aplikasi** dengan salah satu akun default
2. **Explore fitur-fitur** yang tersedia
3. **Buat pengaduan baru** untuk testing
4. **Coba berbagai role** untuk memahami workflow
5. **Lihat dokumentasi** untuk detail teknis

## 🔄 Update dan Maintenance

### Update Aplikasi
```bash
# Pull latest changes
git pull origin main

# Recompile jika ada perubahan
javac -cp ".:lib/*" src/**/*.java

# Restart aplikasi
```

### Backup Database
```bash
# Backup database
mysqldump -u root -p siprima_db > backup_$(date +%Y%m%d).sql

# Restore database
mysql -u root -p siprima_db < backup_20231201.sql
```

## 📞 Support

Jika mengalami masalah:
- Buat issue di GitHub repository
- Email: ebryany6@gmail.com
- Cek dokumentasi [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

---

*Selamat menggunakan SIPRIMA! 🎉*

