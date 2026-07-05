# Laporan Audit Inkonsistensi UI/UX — BookWorm (D'Classics Books)

**Proyek:** COMP8129 User Experience — D'Classics Books
**Tanggal audit:** 2026-07-05
**Metode:** Perbandingan implementasi Android (Java + XML) terhadap dua acuan resmi:
1. **File Figma** `UX - LAB - Cawu 5` (fileKey `cPoIsPn2UtZfEVEIPxedXZ`) — sumber kebenaran design token & mockup layar.
2. **Requirement PDF** `E262-COMP8129-DC01-00.pdf` — spesifikasi fungsional.

> Catatan: folder `reference/UX-Lab/` (prototype JSX/CSS) TIDAK dipakai sebagai baseline karena tokennya menyimpang dari Figma resmi. Baseline token & layout = Figma.

---

## Ringkasan Eksekutif

| Aspek | Hasil |
|---|---|
| **Kepatuhan fungsional (PDF)** | ✅ Sangat baik — praktis 100% requirement wajib terpenuhi |
| **Konsistensi visual (Figma)** | 🟠 Masih ada inkonsistensi nyata, terutama **typography** |

Pola utama inkonsistensi: **heading yang di Figma memakai serif (Playfair Display) diimplementasikan dengan Montserrat**, dan **skala ukuran font menyimpang** dari design system.

---

## Bagian A — Sumber Kebenaran Figma (Design Tokens)

### A.1 Color Palette resmi (Figma page "Palette")

Palette resmi hanya terdiri dari 5 keluarga warna, masing-masing dengan step 100/80/60/40/20/10:

| Keluarga | 100 | 80 | 60 | 40 | 20 | 10 |
|---|---|---|---|---|---|---|
| Primary 1 | `#C08552` | `#CD996D` | `#D9AB83` | `#E5C2A4` | `#F5DFCC` | `#F8EDE4` |
| Primary 2 | `#8D5B3D` | `#9F6F53` | `#BC9279` | `#D1AF9B` | `#EAD3C5` | `#F7EAE2` |
| Primary 3 | `#553522` | `#77523C` | `#987560` | `#B49785` | `#CDBAAE` | `#E2DAD5` |
| Secondary | `#FFA131` | `#FFB45A` | `#FFC784` | `#FFDBB0` | `#FFE9CF` | `#FFF8F0` |
| Black | `#0E0D0D` | `#403F3F` | `#7A7979` | `#A4A3A3` | `#DCDCDC` | `#F2F2F2` |

### A.2 Text Palette resmi

Dua keluarga font: **Playfair Display** (title & body-serif) dan **Montserrat**. Skala H1–H5 = **28 / 24 / 20 / 16 / 12 sp**, lineHeight 100%.

| Style | Font | Ukuran |
|---|---|---|
| H1 | Playfair Display Medium/Bold | 28 |
| H2 | Playfair Display Medium/Bold | 24 |
| H3 | Playfair Display Medium/Bold | 20 |
| H4 | Playfair Display Medium/Bold | 16 |
| H5 | Playfair Display Medium/Bold | 12 |
| Button Text | Montserrat Bold | 14 |
| Caption | Montserrat Medium | 12 |
| Body 1–5 | Montserrat Regular | 28/24/20/16/12 |

---

## Bagian B — Inkonsistensi Visual (Android vs Figma)

### 🔴 B.1 — KRITIS: Book card, font judul & pengarang tertukar

Berkas: `app/src/main/res/layout/item_book_card.xml`

| Elemen | Figma (node 764:598-599) | Implementasi Android |
|---|---|---|
| Judul buku | **Playfair Display Bold, 16sp**, `#553522` | Montserrat, **11.5sp** |
| Pengarang | **Montserrat Bold, 12sp**, `#C08552` | Playfair Display, **13sp** |

Font judul dan pengarang **terbalik**, dan judul jauh lebih kecil dari desain. Ini inkonsistensi paling terlihat karena muncul di grid Home & Books.

### 🔴 B.2 — Login "Hello !" font & ukuran salah

Berkas: `app/src/main/res/layout/activity_login.xml` (baris ~90)

- **Figma (node 707:778):** Playfair Display Bold **40sp**, `#553522`
- **Android:** `@font/montserrat`, **45sp**

Seharusnya serif (Playfair) 40sp, bukan Montserrat 45sp.

### 🔴 B.3 — Home greeting "Hello, [user]" font salah

Berkas: `app/src/main/res/layout/fragment_home.xml` (baris ~47-57)

- **Figma (node 759:580):** Playfair Display Medium/Bold **28sp**
- **Android:** `@font/montserrat`, **27sp**

Seharusnya serif (Playfair), bukan Montserrat.

### 🟠 B.4 — Ukuran & warna teks Home menyimpang

Verifikasi terhadap Figma Home (node 785:600):

| Elemen | Figma | Android | Selisih |
|---|---|---|---|
| "WELCOME BACK" | Montserrat Bold **16sp** | 11sp | -5sp |
| "Have a nice day!" | Montserrat Bold **16sp** | 14sp (weight 600) | -2sp, weight beda |
| "Featured Books" | Montserrat Bold **16sp** | 14sp | -2sp |
| "See all" warna | `#9F6F53` (Primary2-80) | `#8D5B3D` (Primary2-100) | beda step |
| Carousel tinggi | **180dp** | 150dp | -30dp |

### 🟠 B.5 — Font weight hilang di resource

Berkas: `app/src/main/res/font/montserrat.xml`

Hanya mendefinisikan weight **600 & 700**. Figma membutuhkan:
- Montserrat **Regular (400)** untuk Body
- Montserrat **Medium (500)** untuk Caption

Akibatnya body/caption tidak bisa akurat sesuai Figma.

### 🟠 B.6 — Warna di luar palette Figma resmi

Berkas: `app/src/main/res/values/colors.xml`

| Token kode | Nilai | Catatan |
|---|---|---|
| `color_burnt` | `#7C3900` | Tidak ada di palette Figma (bubble nav aktif) |
| `color_error` | `#B76464` | Tidak ada di palette Figma |
| `color_success` | `#6F8E5A` | Tidak ada di palette Figma |
| `color_grey` | `#999999` | Figma Black-60 = `#7A7979` |
| `black` | `#000000` | Figma Black-100 = `#0E0D0D` |

### 🟡 B.7 — Store card image tidak pakai dimens

Berkas: `app/src/main/res/layout/item_store_card.xml`

Ukuran gambar di-hardcode `129dp × 126dp`, padahal `dimens.xml` mendefinisikan `store_card_image_width/height` = 96/112 yang tidak dipakai.

---

## Bagian C — Yang SUDAH SESUAI Figma (bukan inkonsistensi)

Berikut terverifikasi cocok dengan Figma — dicatat agar tidak keliru "diperbaiki":

- ✅ Bottom nav ikon: home / book / **cart** / logout (sesuai Figma, bukan store)
- ✅ Detail meta strip: strip caramel + chip coffee + teks putih (sesuai Figma)
- ✅ Stat chip angka (1813, 432) memakai Playfair Display
- ✅ Warna Primary 1/2/3 & background peach `#FFE9CF` / cream `#FFF8F0`
- ✅ Layout keseluruhan Login, Home, Books, Detail, Stores mengikuti Figma

---

## Bagian D — Kepatuhan Requirement PDF (Fungsional)

### ✅ Login Page — `LoginActivity.java`
- Username wajib, error jika kosong
- Password wajib, error jika kosong
- Password **alphanumeric** (`[a-zA-Z0-9]+`)
- Sukses → simpan `UserSession.username` (global var) + redirect Home

### ✅ Home Page — `HomeFragment.java`
- Greeting "Hello, [username]!" dari global var
- 4 featured books (repeater / RecyclerView)
- Carousel: auto-slide 3s + tombol prev/next + dots
- Menu avatar: All Books / Our Store / Log Out

### ✅ Book Detail — `BookDetailActivity.java`
- Back, cover, title, author
- Address & phone wajib
- Phone **numeric** (`[0-9]+`)
- Error → **MaterialAlertDialogBuilder** (sesuai PDF)
- Sukses → MaterialAlertDialog "A confirmation email has been sent..." + redirect Books

### ✅ Books Page — `Catalogue.java`
- Tab Non-Fiction / Fiction
- Non-Fiction: 4 buku, Fiction: 4 buku (requirement ≥3)

### ✅ Stores Page — `MainActivity.java` / `Catalogue.java`
- 4 stores (requirement ≥4)
- Menu: Home / Our Store / Log Out
- Hide dropdown saat klik luar (`PopupWindow` `setOutsideTouchable(true)` + focusable)

### Catatan minor PDF
- "Our Store" memakai ikon cart (label benar, ikon konsisten dengan Figma)
- `overlay_success` di `activity_book_detail.xml` adalah **dead code** (tidak dipakai; kode meng-inflate `dialog_order_success` ke MaterialAlertDialog)

---

## Bagian E — Prioritas Perbaikan

| Prioritas | Temuan | Berkas |
|---|---|---|
| 🔴 1 | Font judul/pengarang book card tertukar + ukuran judul | `item_book_card.xml` |
| 🔴 2 | "Hello !" Login: Montserrat 45 → Playfair 40 | `activity_login.xml` |
| 🔴 3 | Greeting Home: Montserrat 27 → Playfair 28 | `fragment_home.xml` |
| 🟠 4 | Ukuran teks Home (16sp banyak jadi 11-14sp) | `fragment_home.xml` |
| 🟠 5 | Tambah Montserrat Regular (400) & Medium (500) | `res/font/` |
| 🟠 6 | Warna di luar palette (burnt/error/success/grey/black) | `colors.xml` |
| 🟡 7 | Store card image pakai dimens | `item_store_card.xml` |
| 🟡 8 | Hapus dead code `overlay_success` | `activity_book_detail.xml` |

---

## Referensi Node Figma

| Screen | Node ID |
|---|---|
| Color Palette | 707:935 |
| Text Palette | 707:981 |
| Login Page | 707:774 (Heading 707:775) |
| Register Page | 707:797 |
| Home Page | 133:221 (konten utama 785:600) |
| Shop Page | 428:374 |
| Books Page Non-Fiction | 247:157 |
| Books Page Fiction | 411:428 |
| Book Detail (Pride) | 362:252 |
| Landing/Splash | 707:832 |

File key Figma: `cPoIsPn2UtZfEVEIPxedXZ` · Page "Prototype" = node `0:1`
