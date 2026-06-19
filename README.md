# BookWorm

Aplikasi Android toko buku digital yang menampilkan koleksi buku klasik pilihan beserta informasi toko fisik. Dibangun dengan pure Java tanpa framework MVVM atau Jetpack Compose.

---

## Fitur

- **Splash screen** dengan animasi pembuka
- **Autentikasi** — register dan login dengan validasi form
- **Home** — banner carousel buku featured + grid buku pilihan
- **Books** — daftar lengkap semua buku dengan filter kategori (Fiction / Non-Fiction)
- **Stores** — daftar toko fisik beserta alamat, nomor telepon, dan jam operasional
- **Book Detail** — informasi lengkap buku, harga, dan tombol order
- **Avatar menu** — akses profil dan logout dari semua layar utama

---

## Tangkapan Layar

> *(Tambahkan screenshot aplikasi di sini)*

---

## Spesifikasi Teknis

| Atribut | Nilai |
|---|---|
| Bahasa | Java |
| Min SDK | 35 (Android 15) |
| Target SDK | 35 |
| Build Tools | Gradle (Kotlin DSL) |
| View Binding | Aktif |

### Dependensi Utama

| Library | Kegunaan |
|---|---|
| `androidx.appcompat` | Kompatibilitas Activity & tema |
| `androidx.recyclerview` | Daftar buku dan toko |
| `androidx.viewpager2` | Carousel banner infinite-scroll |
| `androidx.core:core-splashscreen` | Splash screen native Android 12+ |
| `com.google.android.material` | Komponen Material Design |
| `com.github.bumptech.glide` | Loading & caching gambar |

---

## Struktur Project

```
app/src/main/java/com/example/bookworm/
│
├── model/
│   ├── Book.java           # POJO buku (id, title, author, price, dll)
│   └── Store.java          # POJO toko (nama, alamat, telepon, jam)
│
├── data/
│   ├── Catalogue.java      # Semua data buku & toko hardcoded di sini
│   └── UserSession.java    # Menyimpan state user yang sedang login
│
├── ui/
│   ├── splash/
│   │   └── SplashActivity.java
│   ├── auth/
│   │   ├── LoginActivity.java
│   │   └── RegisterActivity.java
│   └── home/
│       ├── MainActivity.java        # Shell utama dengan bottom navbar
│       ├── HomeFragment.java        # Tab Home
│       ├── BooksFragment.java       # Tab Books
│       ├── StoresFragment.java      # Tab Stores
│       ├── BookDetailActivity.java  # Halaman detail buku
│       └── BookGridDecoration.java  # Spacing grid buku
│
├── adapter/
│   ├── BookAdapter.java        # RecyclerView daftar buku
│   ├── StoreAdapter.java       # RecyclerView daftar toko
│   └── CarouselAdapter.java    # ViewPager2 banner (infinite scroll)
│
└── view/
    ├── BookCoverView.java              # Custom view cover buku 3D via Canvas
    ├── NavbarView.java                 # Bottom navigation bar kustom
    └── ScrollableNestedScrollView.java # NestedScrollView kompatibel ViewPager2
```

---

## Alur Navigasi

```
SplashActivity
      │
      ▼
LoginActivity  ◄──►  RegisterActivity
      │
      ▼
MainActivity
  ├── HomeFragment    ──► BookDetailActivity
  ├── BooksFragment   ──► BookDetailActivity
  └── StoresFragment
```

Navigasi antar tab menggunakan `FLAG_ACTIVITY_REORDER_TO_FRONT` — instance lama diangkat ke depan tanpa dibuat ulang, sehingga scroll position tetap terjaga.

---

## Data Katalog

Semua data bersifat statis dan disimpan di `Catalogue.java`. Tidak ada database maupun koneksi jaringan.

### Koleksi Buku (8 judul)

| Judul | Penulis | Kategori | Harga |
|---|---|---|---|
| Meditations | Marcus Aurelius | Non-Fiction | Rp 95.000 |
| The Republic | Plato | Non-Fiction | Rp 110.000 |
| Walden | Henry David Thoreau | Non-Fiction | Rp 89.000 |
| Letters from a Stoic | Seneca | Non-Fiction | Rp 92.000 |
| Pride and Prejudice | Jane Austen | Fiction | Rp 99.000 |
| The Great Gatsby | F. Scott Fitzgerald | Fiction | Rp 89.000 |
| Jane Eyre | Charlotte Brontë | Fiction | Rp 115.000 |
| Moby-Dick | Herman Melville | Fiction | Rp 129.000 |

### Toko Fisik (4 lokasi)

| Nama Toko | Kota |
|---|---|
| Ivory Tower Books | Bandung |
| Plato's Atheneum | Yogyakarta |
| The Serpent's Archive | Jakarta |
| Alexandria Branch | Surabaya |

---

## Build & Run

```bash
# Build debug APK
./gradlew assembleDebug

# Install ke perangkat yang terhubung
./gradlew installDebug

# Jalankan lint check
./gradlew lint
```

> Pastikan Android SDK sudah terinstal dan perangkat/emulator sudah terhubung sebelum menjalankan `installDebug`.

---

## Konvensi Resource

| Prefix | Jenis |
|---|---|
| `bg_` | Shape / background drawable (XML) |
| `ic_` | Ikon vektor (XML) |
| `img_` | Ilustrasi / gambar dekoratif |
| `cover_art_` | Gambar cover buku 3D (PNG, `drawable-nodpi`) |
| `store_` | Foto toko (PNG, `drawable-nodpi`) |
| `color_primary_1/2/3` | Palet warna utama di `colors.xml` |

**Font:**
- **Montserrat** — teks UI umum (body, label, tombol)
- **Playfair Display** — teks display (harga, judul buku)

---

## Catatan Pengembangan

- **Tidak ada unit test** — hanya skeleton test runner yang dideklarasikan di `build.gradle.kts`
- **Tidak ada database** — tambah/edit buku langsung di `Catalogue.java`
- **BookCoverView** menggambar cover buku 3D menggunakan `Canvas` dengan pixel math yang di-derive dari Figma; konstanta `FACE_B = 0.2409` adalah nilai kritis yang menentukan batas bawah area cover art
- **Carousel** menggunakan `LOOP_FACTOR = 10.000` pada total item count agar efek infinite scroll tidak pernah mencapai batas nyata

---

## Lisensi

Project ini dibuat untuk keperluan akademik — **User Experience Lab, Cawu 5**.
