# PegawaiApp - Android Kotlin

Aplikasi manajemen data pegawai berbasis Android menggunakan **Kotlin**, **MySQL**, dan **PHP API**. Aplikasi ini dirancang untuk memudahkan admin dalam mengelola data karyawan secara real-time.

## Fitur Utama
- **CRUD Data Pegawai**: Tambah, Lihat, Edit, dan Hapus data.
- **Detail Pegawai Modern**: Tampilan detail menggunakan UI CardView yang bersih.
- **Integrasi WhatsApp**: Klik nomor telepon langsung diarahkan ke chat WhatsApp (Auto-format 62).
- **Online Database**: Sinkronisasi data menggunakan server MySQL melalui OkHttp3.

## ️ Tech Stack
- **Language**: Kotlin
- **Networking**: OkHttp3 & Retrofit (Optional)
- **UI Design**: Material Design, CardView, ScrollView
- **Backend**: PHP Native
- **Database**: MySQL

##  Screenshots
| Main List | Detail Profil | Add Data |
|---|---|---|
| ![Main](https://via.placeholder.com/200x400?text=Main+List) | ![Detail](https://via.placeholder.com/200x400?text=Detail+Profil) | ![Add](https://via.placeholder.com/200x400?text=Add+Data) |

##  Cara Instalasi
1. Clone repository ini.
2. Import database `pegawai.sql` ke phpMyAdmin .
3. Sesuaikan `BASE_URL` di `DatabaseHelper.kt`
4. Build & Run menggunakan Android Studio.
