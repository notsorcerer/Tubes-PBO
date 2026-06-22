# LiquidPedia - E-Commerce Liquid Vape

Aplikasi e-commerce untuk liquid vape berbasis Spring Boot 3.2.5 dengan Thymeleaf, Spring Security, JPA, dan MySQL.

## Prerequisites

Sebelum menjalankan aplikasi, pastikan environment sudah memenuhi kebutuhan berikut:

| Komponen | Keterangan |
|----------|-----------|
| **JDK** | Java 17+ (dikembangkan menggunakan JDK 25, tanpa Lombok) |
| **XAMPP / MySQL Server** | MySQL berjalan di localhost (default port 3306) |
| **Maven** | Menggunakan Maven Wrapper (`mvnw.cmd`) yang sudah termasuk dalam project |
| **Browser** | Chrome / Firefox / Edge untuk mengakses web |

> **Catatan**: Project ini tidak menggunakan Lombok karena ketidakcocokan dengan JDK 25. Semua getter, setter, dan constructor ditulis manual.

## Cara Menjalankan Aplikasi

### 1. Setup Database

Buka phpMyAdmin (http://localhost/phpmyadmin) atau terminal MySQL, lalu jalankan:

```sql
CREATE DATABASE IF NOT EXISTS liquid2;
```

Pastikan MySQL server berjalan sebelum melanjutkan.

### 2. Konfigurasi Environment

File `src/main/resources/application.properties` sudah terkonfigurasi untuk koneksi MySQL default:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/liquid2?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=
```

Sesuaikan `username` dan `password` jika menggunakan konfigurasi MySQL yang berbeda.

### 3. Jalankan Aplikasi

Buka terminal di root project, lalu jalankan:

```bash
.\mvnw.cmd spring-boot:run
```

Proses yang terjadi saat pertama kali dijalankan:
1. **Maven** mengunduh dependencies (hanya sekali)
2. **Hibernate** membuat tabel-tabel secara otomatis (`spring.jpa.hibernate.ddl-auto=update`)
3. **DatabaseSeeder** mengisi data awal (kategori, produk, voucher, admin)
4. Aplikasi berjalan di **http://localhost:8080**

> **Troubleshooting**: Jika muncul error `Unknown database 'liquid2'`, pastikan database sudah dibuat manual (langkah 1). Hibernate hanya auto-create table, bukan database.

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

## Struktur Package

Berikut adalah struktur package utama dalam project:

```
src/main/java/com/liquid/liquidpedia/
├── LiquidpediaApplication.java          # Entry point aplikasi
│
├── config/
│   ├── SecurityConfig.java              # Konfigurasi Spring Security (2 filter chain)
│   └── DatabaseSeeder.java              # Seeder data awal (CommandLineRunner)
│
├── controller/
│   ├── AuthController.java              # Login & registrasi customer
│   ├── CartController.java              # Keranjang belanja
│   ├── CheckoutController.java          # Checkout & pembayaran
│   ├── HomeController.java              # Halaman utama
│   ├── OrderController.java             # Riwayat pesanan customer
│   ├── ProdukController.java            # Daftar & detail produk, review
│   ├── ProfileController.java           # Profil & alamat customer
│   └── admin/
│       ├── AdminController.java         # Dashboard admin
│       ├── AdminCategoryController.java # Manajemen kategori
│       ├── AdminOrderController.java    # Manajemen pesanan
│       └── AdminProdukController.java   # Manajemen produk
│
├── dto/
│   ├── AddressDto.java                  # DTO untuk form alamat
│   ├── CheckoutDto.java                 # DTO untuk form checkout
│   ├── DashboardStats.java              # DTO statistik dashboard (dgn inner class ProdukPerCategory)
│   ├── ProdukDto.java                   # DTO untuk form produk
│   ├── RegisterDto.java                 # DTO untuk form registrasi
│   └── ReviewDto.java                   # DTO untuk form review
│
├── entity/
│   ├── User.java                        # Entity utama — single table, @ElementCollection Set<Role>
│   ├── Cart.java                        # Keranjang (OneToOne User, OneToMany CartItem)
│   ├── CartItem.java                    # Item dalam keranjang
│   ├── Category.java                    # Kategori produk
│   ├── Order.java                       # Pesanan
│   ├── OrderItem.java                   # Item pesanan
│   ├── Produk.java                      # Produk
│   ├── Review.java                      # Review produk
│   ├── Voucher.java                     # Voucher diskon
│   ├── Payment.java                     # Abstract payment (JOINED)
│   ├── TransferBank.java                # Pembayaran transfer bank
│   ├── Qris.java                        # Pembayaran QRIS
│   ├── Cod.java                         # Pembayaran COD
│   ├── Delivery.java                    # Informasi pengiriman
│   └── enums/
│       ├── Role.java                    # Enum: ADMIN, CUSTOMER
│       ├── OrderStatus.java             # Enum status pesanan
│       └── PaymentStatus.java           # Enum status pembayaran
│
├── repository/                          # Spring Data JPA Repositories
│   ├── AddressRepository.java
│   ├── CartItemRepository.java
│   ├── CartRepository.java
│   ├── CategoryRepository.java
│   ├── OrderRepository.java
│   ├── PaymentRepository.java
│   ├── ProdukRepository.java
│   ├── ReviewRepository.java
│   ├── UserRepository.java
│   └── VoucherRepository.java
│
└── service/
    ├── AddressService.java              # CRUD alamat
    ├── AdminService.java                # Dashboard & CRUD produk/kategori (admin)
    ├── AuthService.java                 # Registrasi customer
    ├── CartService.java                 # Keranjang belanja
    ├── CustomUserDetailsService.java    # Load user dari DB untuk Spring Security
    ├── OrderService.java                # Pembuatan & manajemen pesanan
    ├── ProdukService.java               # Produk & kategori (customer)
    ├── ReviewService.java               # Review produk
    └── VoucherService.java              # Validasi & aplikasi voucher
```

Templates (Thymeleaf) berada di `src/main/resources/templates/`:
```
templates/
├── admin/                               # Halaman admin panel
│   ├── layout/adminLayout.html          # Layout admin (sidebar, topbar)
│   ├── login.html                       # Login admin (terpisah dari customer)
│   ├── dashboard.html
│   ├── products.html / product-form.html
│   ├── categories.html / category-form.html
│   └── orders.html
├── auth/                                # Login & register customer
├── cart/                                # Keranjang belanja
├── checkout/                            # Checkout
├── error/                               # Halaman error (403, 404, 500)
├── fragments/                           # Navbar, footer (shared)
├── index.html                           # Halaman utama
├── orders/                              # Riwayat & detail pesanan
└── products/                            # Daftar & detail produk
```

Static assets di `src/main/resources/static/`:
```
static/
├── css/style.css                        # Semua styling (customer + admin)
├── js/main.js                           # JavaScript bersama (mobile menu, dropdown, alert)
└── uploads/products/                    # Upload gambar produk
```

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
