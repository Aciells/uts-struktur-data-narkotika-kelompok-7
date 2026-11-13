# ADR-001: Pemilihan Struktur Data

- **Status:** Draf
- **Tanggal:** 13 November 2026
- **Pengambil keputusan:** Kelompok 7
- **Proyek:** UTS Struktur Data Putusan Narkotika

## 1. Keputusan

Proyek UTS Struktur Data Putusan Narkotika menggunakan dua struktur data:

1. **Array Dinamis (Dynamic Array)** sebagai struktur data utama.
2. **Stack** sebagai struktur data sekunder.

Kedua struktur data akan diimplementasikan secara mandiri menggunakan Java Generics.

Struktur data utama wajib dibuat dari nol (*from scratch*) dan tidak boleh digantikan oleh implementasi koleksi bawaan Java seperti `ArrayList` atau koleksi sejenis.

## 2. Alasan Pemilihan

### 2.1 Array Dinamis

Array Dinamis dipilih sebagai struktur utama karena aplikasi membutuhkan penyimpanan dan pengelolaan 50 data putusan pengadilan.

Alasan pemilihan:
- Mendukung akses data berdasarkan indeks dengan kompleksitas O(1).
- Penambahan elemen di akhir memiliki kompleksitas O(1) secara amortisasi.
- Memudahkan proses penelusuran, pencarian, filter, dan pengurutan data.
- Sesuai untuk jumlah data yang dikelola dalam proyek.

### 2.2 Stack

Stack dipilih sebagai struktur sekunder untuk mencatat riwayat aktivitas pengguna.

Alasan pemilihan:
- Menggunakan prinsip LIFO (*Last In, First Out*).
- Mendukung pencatatan aktivitas terbaru.
- Operasi utama seperti `push()`, `pop()`, dan `peek()` memiliki kompleksitas O(1).
- Dapat dikembangkan untuk mendukung fitur undo sebagai fitur tambahan.

## 3. Rancangan Implementasi

### 3.1 Array Dinamis

Array Dinamis digunakan untuk menyimpan objek `Putusan` sebagai data utama aplikasi.

Implementasi dibuat sendiri menggunakan array internal dengan atribut:
- `data`: tempat penyimpanan elemen.
- `size`: jumlah elemen yang tersimpan.
- `capacity`: kapasitas maksimum array saat ini.

Operasi yang disediakan meliputi:
- `add()`: menambahkan data.
- `get()`: mengambil data berdasarkan indeks.
- `remove()`: menghapus data.
- `search()`: mencari data.

Implementasi juga harus mampu memperbesar kapasitas array ketika penuh dan menjaga validitas struktur data melalui `validateInvariant()`.

### 3.2 Stack

Stack digunakan untuk menyimpan riwayat aktivitas pengguna, seperti:
- Import data.
- Penambahan data putusan.
- Penghapusan data putusan.
- Pencarian data.
- Filter data.

Operasi yang disediakan:
- `push()`: menambahkan aktivitas ke bagian atas Stack.
- `pop()`: mengambil sekaligus menghapus aktivitas teratas.
- `peek()`: melihat aktivitas teratas tanpa menghapusnya.

Implementasi Stack juga dibuat secara mandiri menggunakan Java Generics.