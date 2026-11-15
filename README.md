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

## Kebijakan nilai kosong

Dua keadaan dibedakan.

| Keadaan | Contoh | Perlakuan importer |
|---|---|---|
| Nilai tidak ditemukan | Sel kosong karena informasinya memang tidak tertulis di putusan (sudah dicek ke PDF) | Record diterima |
| Record tidak valid | Jumlah kolom bukan 16, caseId, nomorPerkara, atau pengadilan kosong, tanggal atau angka salah bentuk, angka negatif | Record ditolak, pesan dicetak, struktur tidak berubah |

Penyimpanan nilai tidak ditemukan:
- Teks dan tanggal: null.
- Angka: 0. Berat barang bukti dan vonis yang nyata selalu lebih dari 0, jadi 0 berarti tidak ditemukan. Untuk denda, 0 berarti tidak ada denda di amar atau tidak ditemukan (keduanya tidak dibedakan).

Perlakuan fitur:
- Filter teks (jenis, pasal): nilai kosong tidak lolos.
- Filter rentang (berat, vonis): nilai kosong tidak lolos, termasuk rentang yang mencakup 0.
- Agregasi: vonis kosong tidak ikut rata-rata, denda kosong menambah 0 pada total, jenis kosong dihitung "(tidak diketahui)".
- Pengurutan vonis: vonis kosong selalu di akhir, baik naik maupun turun.
- Di data-provenance.csv, sel kosong dengan status verified berarti sudah dicek ke PDF dan memang tidak tertulis.

Disepakati oleh Marcello Airlangga Saputra dan Firdausy Ilmi Febiyanti pada 15-11-2026.