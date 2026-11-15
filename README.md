# UTS Struktur Data: Putusan Narkotika (Kelompok 7)

Aplikasi berbasis Java untuk menyimpan, mengelola, mencari, mengurutkan, dan menganalisis 50 data putusan pengadilan narkotika menggunakan implementasi struktur data buatan sendiri.

## Identitas Kelompok

| Nama Anggota | GitHub |
|---|---|
| Firdausy Ilmi Febiyanti | [@firdaaa7](https://github.com/firdaaa7) |
| Marcello Airlangga Saputra | [@Aciells](https://github.com/Aciells) |

**Repository:** [uts-struktur-data-narkotika-kelompok-7](https://github.com/Aciells/uts-struktur-data-narkotika-kelompok-7)

## Teknologi yang Digunakan

- Java
- Maven
- Java Generics
- JUnit untuk unit testing
- Git dan GitHub untuk version control
- IntelliJ IDEA sebagai IDE pengembangan

## Struktur Data yang Digunakan

Proyek ini menggunakan dua struktur data yang diimplementasikan secara mandiri menggunakan Java Generics.

### 1. Dynamic Array (Struktur Utama)

Dynamic Array digunakan untuk menyimpan dan mengelola data putusan pengadilan.

Implementasi dibuat dari nol (*from scratch*) menggunakan array internal, tanpa menjadikan koleksi bawaan Java seperti `ArrayList` sebagai struktur data utama.

Operasi yang didukung:
- Penambahan data.
- Pengaksesan data berdasarkan indeks.
- Penghapusan data.
- Pencarian data.
- Pengelolaan kapasitas array secara dinamis.

### 2. Stack (Struktur Sekunder)

Stack digunakan untuk menyimpan riwayat aktivitas pengguna.

Struktur ini menerapkan prinsip LIFO (*Last In, First Out*).

Operasi yang didukung:
- `push()`: Menambahkan aktivitas ke dalam Stack.
- `pop()`: Mengambil dan menghapus aktivitas teratas.
- `peek()`: Melihat aktivitas teratas tanpa menghapusnya.

Stack juga digunakan dalam pencatatan audit aktivitas pengguna.

## Fitur Aplikasi

Aplikasi menyediakan beberapa fitur utama:

| No. | Fitur | Keterangan |
|---|---|---|
| 1 | Impor Dataset | Memuat data putusan dari file CSV |
| 2 | Tampilkan Data | Menampilkan seluruh data putusan |
| 3 | Tambah Data | Menambahkan satu data putusan baru |
| 4 | Hapus Data | Menghapus data berdasarkan nomor urut |
| 5 | Pencarian | Mencari berdasarkan Case ID atau nomor perkara |
| 6 | Filter | Menyaring data berdasarkan kriteria tertentu |
| 7 | Statistik | Menampilkan ringkasan statistik putusan |
| 8 | Pengurutan | Mengurutkan data berdasarkan vonis |
| 9 | Audit | Melihat riwayat operasi pengguna |
| 10 | Pemeriksaan Invariant | Memeriksa konsistensi struktur data |
| 11 | Ekspor | Mengekspor hasil ke format CSV dan teks |

## Struktur Repository

```text
uts-struktur-data-narkotika-kelompok-7/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── ...
│   └── test/
│       └── java/
│           └── ...
├── data/
│   ├── putusan-50.csv
│   └── data-provenance.csv
├── pom.xml
├── README.md
├── AI_USAGE.md
├── CONTRIBUTIONS.md
└── tugas.md
```

## Persyaratan Sistem

Sebelum menjalankan aplikasi, pastikan perangkat telah memiliki:

- Java Development Kit (JDK).
- Apache Maven.
- IntelliJ IDEA.
- Git.

## Cara Menjalankan Aplikasi

### 1. Clone Repository

Buka terminal dan jalankan:

```bash
git clone https://github.com/Aciells/uts-struktur-data-narkotika-kelompok-7.git
```

Masuk ke direktori proyek:

```bash
cd uts-struktur-data-narkotika-kelompok-7
```

### 2. Buka Project

1. Buka IntelliJ IDEA.
2. Pilih **Open**.
3. Pilih folder repository yang telah di-clone.
4. Tunggu hingga Maven selesai melakukan sinkronisasi dependency.

### 3. Jalankan Main.java

1. Buka folder `src/main/java`.
2. Temukan file `Main.java`.
3. Buka file tersebut.
4. Klik ikon Run (▶) pada deklarasi class `Main`.
5. Aplikasi akan berjalan melalui terminal IntelliJ IDEA.

### 4. Gunakan Menu Aplikasi

Setelah aplikasi berjalan, menu utama akan ditampilkan:

```text
=== Sistem Putusan Narkotika ===

1. Muat data (impor CSV)
2. Tampilkan semua putusan
3. Tambah putusan (satu baris CSV)
4. Hapus putusan (nomor urut)
5. Cari putusan
6. Filter
7. Ringkasan statistik
8. Urutkan berdasarkan vonis
9. Riwayat operasi (audit)
10. Periksa invariant struktur
11. Ekspor hasil ke berkas
0. Keluar

Pilih menu:
```

Masukkan nomor menu sesuai operasi yang ingin dilakukan.

## Menjalankan Unit Test

Proyek menggunakan JUnit untuk menguji implementasi struktur data dan fitur aplikasi.

Jalankan seluruh pengujian melalui terminal:

```bash
mvn test
```

Untuk membangun proyek:

```bash
mvn clean package
```

## Dokumentasi dan Keputusan Desain

Dokumentasi tambahan proyek tersedia pada file berikut:

| File | Keterangan |
|---|---|
| `tugas.md` | Informasi tugas dan ketentuan proyek |
| `AI_USAGE.md` | Catatan penggunaan AI generatif |
| `CONTRIBUTIONS.md` | Dokumentasi kontribusi anggota kelompok |
| ADR-001 | Dokumentasi pemilihan struktur data |
| ADR-002 | Dokumentasi strategi pencarian dan pengurutan |

## Dataset dan Data Provenance

Proyek menggunakan dataset sebanyak 50 putusan pengadilan narkotika.

Dataset utama disimpan dalam:

- `data/putusan-50.csv`: Data putusan pengadilan.
- `data/data-provenance.csv`: Informasi sumber dan asal-usul data (*data provenance*).

Data digunakan sebagai bahan pengujian dan demonstrasi fitur pengelolaan struktur data.

## Pengujian dan Kualitas Kode

Pengujian dilakukan menggunakan unit test untuk memastikan:

- Operasi Dynamic Array berjalan sesuai ketentuan.
- Operasi Stack berjalan sesuai prinsip LIFO.
- Proses impor dan ekspor CSV berjalan dengan benar.
- Pencarian, filter, dan pengurutan menghasilkan keluaran yang sesuai.
- Pencatatan audit berjalan dengan benar.
- Invariant struktur data tetap terjaga.

## Lisensi

Proyek ini dibuat untuk memenuhi tugas Ujian Tengah Semester (UTS) mata kuliah Struktur Data, dengan studi kasus pengelolaan data putusan pengadilan narkotika.

---

**Kelompok 7 — UTS Struktur Data**  
Firdausy Ilmi Febiyanti  
Marcello Airlangga Saputra