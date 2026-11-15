# Sistem Pengelolaan Data Putusan Narkotika

Proyek ini dikembangkan oleh Kelompok 7 untuk memenuhi Ujian Tengah Semester (UTS) mata kuliah Struktur Data. Aplikasi menggunakan Java dan Maven untuk mengelola data putusan pengadilan terkait perkara narkotika.

## Identitas Kelompok

- Firdausy Ilmi Febiyanti
- Marcello Airlangga Saputra

## Teknologi

- Java 17
- Apache Maven
- JUnit 5
- IntelliJ IDEA (lingkungan pengembangan)

## Fitur dan Komponen

Berdasarkan komponen proyek yang dikembangkan, aplikasi mencakup pengelolaan data putusan, impor dan ekspor CSV, pencarian, penyaringan, pengurutan, agregasi, serta pencatatan audit.

Komponen struktur data dan layanan yang terdapat pada proyek meliputi:

- `DynamicArray`
- `Stack`
- `PutusanImporter` dan komponen parsing CSV
- Layanan pencarian, penyaringan, pengurutan, dan agregasi
- Komponen audit

> Sesuaikan daftar fitur ini dengan fitur yang benar-benar tersedia dan dapat dijalankan pada versi final aplikasi.

## Struktur Direktori

```text
.
├── data/
│   └── putusan-50.csv
├── docs/
│   ├── adr/
│   │   ├── 001-pemilihan-struktur.md
│   │   └── 002-strategi-pencarian-pengurutan.md
│   └── data-provenance.csv
├── src/
│   ├── main/
│   │   └── java/
│   └── test/
│       └── java/
├── README.md
├── CONTRIBUTIONS.md
├── AI_USAGE.md
└── pom.xml
```

Struktur di atas merupakan ringkasan direktori utama. Lihat repository untuk susunan package dan kelas secara lengkap.

## Persyaratan

- JDK 17
- Apache Maven

## Clone Repository

```bash
git clone <URL_REPOSITORY>
cd uts-struktur-data-narkotika-kelompok-7
```

Ganti `<URL_REPOSITORY>` dengan URL repository GitHub kelompok.

## Build dan Pengujian

Build proyek:

```bash
mvn clean package
```

Menjalankan seluruh pengujian:

```bash
mvn test
```

Pastikan kedua perintah selesai tanpa error sebelum menandai proyek sebagai versi final.

## Menjalankan Aplikasi

Jalankan kelas `Main.java` melalui IntelliJ IDEA dengan memilih kelas tersebut lalu menekan tombol **Run**.

Jika ingin menjalankan dari terminal, gunakan perintah yang sesuai dengan lokasi package dan konfigurasi kelas utama di repository. Perintah terminal belum dicantumkan di sini karena lokasi package dan konfigurasi entry point perlu dicocokkan dengan source code final.

## Dataset dan Sumber Data

Dataset proyek berada di:

```text
data/putusan-50.csv
```

Dokumentasi asal-usul data berada di:

```text
docs/data-provenance.csv
```

Periksa dokumentasi provenance untuk informasi sumber dan proses pengumpulan data. File sumber PDF tidak disertakan sebagai bagian dari dataset repository.

## Architecture Decision Records (ADR)

Keputusan desain teknis didokumentasikan pada:

- `docs/adr/001-pemilihan-struktur.md`
- `docs/adr/002-strategi-pencarian-pengurutan.md`

## Kontribusi dan Penggunaan AI

Informasi pembagian pekerjaan dan riwayat kolaborasi tersedia di `CONTRIBUTIONS.md`. Keterangan penggunaan alat AI tersedia di `AI_USAGE.md`.

## Tujuan Akademik

Repository ini dibuat untuk keperluan akademik Ujian Tengah Semester mata kuliah Struktur Data.
