# UTS Struktur Data: Putusan Narkotika (Kelompok 7)

Aplikasi Java berorientasi objek untuk menyimpan dan mengolah 50 putusan
pengadilan narkotika dengan struktur data buatan sendiri.

## Identitas kelompok
- Firdausy Ilmi Febiyanti
- Marcello Airlangga Saputra

## Struktur data yang dipilih
Array Dinamis dan Stack

## Cara menjalankan
(diisi setelah aplikasi selesai)

## Struktur Data yang Dipilih

Proyek UTS Struktur Data Putusan Narkotika menggunakan dua struktur data yang diimplementasikan secara mandiri menggunakan Java Generics.

### 1. Array Dinamis (Struktur Utama)

Array Dinamis digunakan untuk menyimpan dan mengelola 50 data putusan pengadilan.

Implementasi dibuat dari nol (*from scratch*) menggunakan array internal, tanpa menggunakan koleksi bawaan Java seperti `ArrayList` sebagai pengganti struktur data utama.

Operasi yang didukung meliputi:
- Penambahan data.
- Pengaksesan data berdasarkan indeks.
- Penghapusan data.
- Pencarian data.
- Pengelolaan kapasitas array secara dinamis.

### 2. Stack (Struktur Sekunder)

Stack digunakan sebagai struktur data sekunder untuk mencatat riwayat aktivitas pengguna.

Stack menerapkan prinsip LIFO (*Last In, First Out*) dengan operasi:
- `push()`: menambahkan aktivitas.
- `pop()`: mengambil dan menghapus aktivitas teratas.
- `peek()`: melihat aktivitas teratas.

Fitur undo dapat dipertimbangkan sebagai pengembangan tambahan.

### Dokumentasi Keputusan

Penjelasan mengenai alasan pemilihan dan rancangan implementasi struktur data dapat dilihat pada [ADR-001: Pemilihan Struktur Data](docs/adr/001-pemilihan-struktur.md).