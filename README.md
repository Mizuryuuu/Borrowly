### 📱 Preview UI

<img src="assets/Screenshot_Onboarding.jpeg" width="200"> <img src="assets/Screenshot_Login.jpeg" width="200"> <img src="assets/Screenshot_Home.jpeg" width="200"> 
<img src="assets/Screenshot_Status.jpeg" width="200"> <img src="assets/Screenshot_Profile.jpeg" width="200"> <img src="assets/Screenshot_ProductDetail.jpeg" width="200">
<img src="assets/Screenshot_AddProduct.jpeg" width="200"> <img src="assets/Screenshot_MyProduct.jpeg" width="200">

# Borrowly

Borrowly adalah aplikasi platform peminjaman barang yang memudahkan pengguna untuk meminjamkan atau meminjam barang dari orang lain dengan manajemen yang terorganisir.

## Prototype Features

* **Product Catalog:** Menampilkan berbagai barang yang tersedia untuk dipinjam oleh pengguna lain.
* **Loan Management:** Sistem untuk mengajukan, menyetujui, dan memantau status peminjaman barang secara real-time.
* **Add & Edit Product:** Pengguna dapat mendaftarkan barang miliknya sendiri untuk dipinjamkan ke komunitas.
* **User Profile:** Manajemen informasi pengguna termasuk profil pribadi dan riwayat aktivitas.

## Technologies Used

**Languages:**
* Kotlin

**Frameworks/Libraries:**
* Jetpack Compose (UI)
* ViewModel & StateFlow
* Coroutines
* Navigation Compose (AppNavHost)

**Dependency Injection:**
* Hilt

**Backend/Services:**
* Firebase Authentication
* Cloud Firestore

---

## Architecture & Project Structure

Aplikasi ini dibangun menggunakan arsitektur **MVVM (Model-View-ViewModel)** dengan pemisahan tanggung jawab yang jelas untuk memastikan kode mudah dipelihara dan diuji.

* **`data.repository`**: Menangani logika pengambilan data dari sumber eksternal (Firebase).
* **`ui`**: Berisi seluruh komponen UI yang dipisahkan berdasarkan fitur (Auth, Home, LoanRequest, Product, dll).
* **`di`**: Konfigurasi Dependency Injection menggunakan Hilt.
* **`theme`**: Mengatur styling global aplikasi seperti warna, tipografi, dan tema Compose.

---

## How to Run

**Prerequisites:**
* Android Studio (versi terbaru direkomendasikan)
* Android Emulator atau Device Hp

**Steps:**
   1. **Clone repository-nya:**
      ```bash
      git clone [https://github.com/Mizuryuuu/Borrowly.git](https://github.com/Mizuryuuu/Borrowly.git)
      ```
   2. Buka di Aplikasi Android Studio:
      * Luncurkan Android Studio.
      * Jika Anda melihat Layar Selamat Datang, klik `Open`.
      * Jika proyek sudah terbuka, pilih `File` > `Open`....
      * Arahkan ke folder `Borrowly` yang telah dikloning dan pilih folder tersebut.
   3. Sync Gradle:
      * Tunggu hingga Android Studio mengindeks file dan menyinkronkan proyek dengan file Gradle-nya.
   4. Jalankan Applikasi:
      * Pilih `device/emulator` dari toolbar virtual device.
      * Klik tombol Run (Icon Play berwarna Hijau) atau klik `Shift + F10`.
