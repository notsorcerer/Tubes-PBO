# LiquidPedia - E-Commerce Liquid Vape

Aplikasi e-commerce untuk liquid vape berbasis Spring Boot 3.2.5 dengan Thymeleaf, Spring Security, JPA, dan MySQL.

## Konfigurasi Lingkungan

- **JDK**: Java 17+ (dikembangkan menggunakan JDK 25, tanpa Lombok)
- **Framework**: Spring Boot 3.2.5, Thymeleaf, Spring Security, Spring Data JPA (Hibernate)
- **DBMS**: MySQL (via XAMPP atau server MySQL lainnya)
- **Database**: `liquid2` (harus dibuat manual sebelum menjalankan aplikasi)
- **Port**: 8080
- **Run**: `.\mvnw.cmd spring-boot:run`

### Langkah Menjalankan Aplikasi

1. Pastikan MySQL server berjalan
2. Buat database dengan nama `liquid2`
3. Jalankan perintah `.\mvnw.cmd spring-boot:run` di root project
4. Aplikasi akan membuat tabel-tabel secara otomatis (`ddl-auto=update`)
5. Data seeder akan mengisi data awal (admin, kategori, produk, voucher)

## Akun Login

| Role     | Email / Username        | Password   |
|----------|-------------------------|------------|
| Admin    | admin@liquidpedia.com   | Admin123!  |
| Customer | Registrasi via `/register` | (buat sendiri) |

## Struktur URLs

### Customer
| URL              | Deskripsi            |
|------------------|----------------------|
| `/`              | Halaman utama        |
| `/login`         | Login customer       |
| `/register`      | Registrasi customer  |
| `/products`      | Daftar produk        |
| `/products/{id}` | Detail produk        |
| `/cart`          | Keranjang belanja    |
| `/checkout`      | Checkout             |
| `/orders`        | Riwayat pesanan      |
| `/orders/{id}`   | Detail pesanan       |
| `/profile`       | Profil customer      |

### Admin
| URL                      | Deskripsi            |
|--------------------------|----------------------|
| `/admin/login`           | Login admin          |
| `/admin/dashboard`       | Dashboard admin      |
| `/admin/products`        | Manajemen produk     |
| `/admin/categories`      | Manajemen kategori   |
| `/admin/orders`          | Manajemen pesanan    |

## Fitur

- Autentikasi dan otorisasi (Customer & Admin)
- Manajemen produk (CRUD + upload gambar)
- Manajemen kategori
- Keranjang belanja
- Checkout dengan pilihan metode pembayaran (Transfer Bank, QRIS, COD)
- Pemesanan dan pelacakan status pesanan
- Review produk (hanya untuk pembeli)
- Voucher diskon
- Dashboard admin dengan statistik
