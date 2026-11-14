# ADR-002: Strategi Pencarian dan Pengurutan

- **Status:** Diterima
- **Tanggal:** 14 November 2026
- **Penulis:** Firdausy Ilmi Febiyanti (@firdaaa7)
- **Terkait:** #10, #12, #19

## Konteks

Aplikasi menyimpan 50 putusan perkara narkotika (`data/putusan-50.csv`) di struktur data buatan sendiri (`DynamicArray<Putusan>`, diakses lewat antarmuka `StrukturData<Putusan>`). Ada dua kebutuhan:

1. **Mencari** satu putusan berdasarkan `caseId` atau `nomorPerkara`.
2. **Mengurutkan** putusan berdasarkan lama vonis (`vonisBulan`), naik atau turun. Sebagian putusan tidak punya data vonis (kosong), dan rekaman itu harus selalu berada di akhir hasil, baik urutan naik maupun turun.

Dua kebutuhan tambahan memengaruhi pilihan:

- Struktur data utama **tidak boleh berubah** akibat pencarian atau pengurutan.
- Aplikasi perlu menampilkan **jumlah langkah** (pemeriksaan atau perbandingan) supaya kompleksitas bisa dibuktikan lewat demo, bukan hanya teori.

## Opsi yang Dipertimbangkan

### Pencarian

| Opsi | Kompleksitas waktu | Syarat data | Catatan |
|------|--------------------|-------------|---------|
| Linear search | O(n) | Tidak perlu terurut | Sederhana, cocok untuk pencarian pada beberapa atribut berbeda |
| Binary search | O(log n) | Harus terurut pada kunci yang dicari | Lebih cepat, tetapi butuh pengurutan terpisah untuk tiap kunci |

### Pengurutan

| Opsi | Terbaik | Rata-rata | Terburuk | Stabil | Memori tambahan |
|------|---------|-----------|----------|--------|-----------------|
| Bubble sort | O(n) | O(n²) | O(n²) | Ya | O(1) |
| Insertion sort | O(n) | O(n²) | O(n²) | Ya | O(1) |
| Merge sort | O(n log n) | O(n log n) | O(n log n) | Ya | O(n) |
| Quick sort | O(n log n) | O(n log n) | O(n²) | Tidak | O(log n) |

## Keputusan

**Pencarian: linear search** (`PutusanSearcher`), yaitu memeriksa rekaman dari indeks 0 sampai ketemu atau habis. Alasannya:

- Data tidak terurut menurut `caseId` maupun `nomorPerkara`. Binary search akan memaksa kita mengurutkan data dulu, dan urutannya bisa hanya cocok untuk satu kunci.
- Ada dua kunci pencarian. Satu fungsi pencarian dengan predikat (`Predicate<Putusan>`) bisa dipakai untuk keduanya tanpa duplikasi kode.
- Dengan n = 50, selisih kecepatan dengan binary search tidak terasa.

**Pengurutan: insertion sort** (`PutusanSorter`) pada salinan data. Alasannya:

- **Stabil.** Perbandingan memakai `<` dan `>` ketat, sehingga putusan dengan vonis sama tetap dalam urutan aslinya (diuji di `vonisSamaTetapBerurutanAsli`).
- **Sederhana dan mudah diverifikasi.** Langkah bisa dihitung persis: 4 perbandingan untuk 5 data yang sudah terurut (n − 1), dan 10 untuk data terbalik (n(n − 1)/2).
- **Efisien untuk data kecil.** Pada n = 50, kasus terburuk hanya 1.225 perbandingan.
- **Tidak butuh memori tambahan** selain salinan data.

Aturan vonis kosong ditangani di fungsi pembanding `harusSebelum`, sehingga rekaman tanpa vonis selalu berada di akhir tanpa kode khusus di luar pembanding.

Pengurutan dilakukan pada **salinan** (`DynamicArray` baru). Struktur utama tetap utuh dan tetap valid (diuji di `strukturUtamaTidakBerubahDanTetapValid`).

## Analisis Kompleksitas

Pada bagian ini, "langkah" berarti satu kali pemeriksaan rekaman (untuk pencarian) atau satu kali perbandingan dua rekaman (untuk pengurutan). Aplikasi menghitungnya lewat `HasilCari.langkah()` dan `PutusanSorter.getLangkah()`. Huruf n adalah jumlah putusan yang tersimpan.

**Linear search.** Cara kerjanya mirip mencari satu berkas di tumpukan map: kami membuka map dari yang paling atas satu per satu sampai ketemu, atau sampai map habis.

- Kasus terbaik: putusan yang dicari ada di posisi pertama, sehingga hanya 1 langkah. Ini O(1).
- Kasus terburuk: putusan ada di posisi terakhir atau tidak ada sama sekali, sehingga n langkah. Ini O(n).
- Kasus rata-rata: sekitar n/2 langkah. Konstanta 1/2 diabaikan dalam notasi Big-O, jadi tetap O(n).

Dengan 50 putusan, pencarian paling banyak memeriksa 50 rekaman. Pada tes dengan 5 data, mencari posisi pertama, tengah, dan terakhir masing-masing butuh 1, 3, dan 5 langkah, sedangkan mencari data yang tidak ada butuh 5 langkah.

**Insertion sort.** Cara kerjanya mirip menyusun kartu di tangan: kami mengambil satu kartu berikutnya, lalu menggesernya ke kiri sampai menemukan tempat yang pas di antara kartu yang sudah rapi.

- Data sudah terurut: setiap rekaman cukup dibandingkan satu kali dengan tetangga di kirinya, lalu langsung berhenti. Totalnya n − 1 langkah, yaitu O(n). Pada tes dengan 5 data, hasilnya 4 langkah.
- Data terbalik (kasus terburuk): rekaman kedua dibandingkan 1 kali, rekaman ketiga 2 kali, dan seterusnya sampai rekaman terakhir (n − 1 kali). Totalnya 1 + 2 + ... + (n − 1) = n(n − 1)/2 langkah, yaitu O(n²). Pada tes dengan 5 data, hasilnya 10 langkah.
- Data acak: rata-rata sekitar setengah dari kasus terburuk. Ini tetap O(n²) karena pertumbuhannya mengikuti kuadrat dari n.

Dengan 50 putusan, kasus terburuk adalah 50 × 49 / 2 = 1.225 langkah. Sebagai pembanding, algoritma O(n log n) seperti merge sort butuh sekitar 50 × log₂50 ≈ 282 langkah. Selisih ini kami anggap masih wajar untuk data sekecil ini.

**Memori.** Pencarian tidak membuat data baru, sehingga memorinya O(1). Pengurutan bekerja pada salinan data, sehingga memerlukan memori tambahan O(n). Insertion sort sendiri tidak butuh array bantu selain salinan itu.

## Konsekuensi

**Positif**
- Kode pendek dan mudah diuji, dan jumlah langkah bisa dibuktikan lewat tes dan demo.
- Satu fungsi pencarian melayani banyak atribut.
- Pengurutan stabil dan tidak merusak data asli.

**Negatif dan keterbatasan**
- Pencarian dan pengurutan lambat jika data membesar menjadi ribuan rekaman (O(n) dan O(n²)).
- Pengurutan menyalin seluruh data setiap dipanggil, sehingga memakai memori tambahan O(n).
- Pencarian memakai `data.get(i)`. Pada `DynamicArray`, `get` bernilai O(1) sehingga pencarian tetap O(n). Jika `StrukturData` diganti dengan struktur yang `get(i)`-nya O(n) (misalnya linked list), pencarian menjadi O(n²).

**Jika data membesar:** insertion sort dapat diganti merge sort (O(n log n), tetap stabil), dan pencarian dapat memakai indeks atau binary search pada data terurut. Karena keduanya berada di kelas `PutusanSorter` dan `PutusanSearcher`, penggantian tidak mengubah kode di bagian lain.

## Bukti Demo

Bukti menjalankan tiap skenario ada di `docs/demo/`. Contoh perintah menjalankan aplikasi ada di `README.md`.# ADR-002: Strategi Pencarian dan Pengurutan

- **Status:** Diterima
- **Tanggal:** 14 November 2026
- **Penulis:** Firdausy Ilmi Febiyanti (@firdaaa7)
- **Terkait:** #10, #12, #19

## Konteks

Aplikasi menyimpan 50 putusan perkara narkotika (`data/putusan-50.csv`) di struktur data buatan sendiri (`DynamicArray<Putusan>`, diakses lewat antarmuka `StrukturData<Putusan>`). Ada dua kebutuhan:

1. **Mencari** satu putusan berdasarkan `caseId` atau `nomorPerkara`.
2. **Mengurutkan** putusan berdasarkan lama vonis (`vonisBulan`), naik atau turun. Sebagian putusan tidak punya data vonis (kosong), dan rekaman itu harus selalu berada di akhir hasil, baik urutan naik maupun turun.

Dua kebutuhan tambahan memengaruhi pilihan:

- Struktur data utama **tidak boleh berubah** akibat pencarian atau pengurutan.
- Aplikasi perlu menampilkan **jumlah langkah** (pemeriksaan atau perbandingan) supaya kompleksitas bisa dibuktikan lewat demo, bukan hanya teori.

## Opsi yang Dipertimbangkan

### Pencarian

| Opsi | Kompleksitas waktu | Syarat data | Catatan |
|------|--------------------|-------------|---------|
| Linear search | O(n) | Tidak perlu terurut | Sederhana, cocok untuk pencarian pada beberapa atribut berbeda |
| Binary search | O(log n) | Harus terurut pada kunci yang dicari | Lebih cepat, tetapi butuh pengurutan terpisah untuk tiap kunci |

### Pengurutan

| Opsi | Terbaik | Rata-rata | Terburuk | Stabil | Memori tambahan |
|------|---------|-----------|----------|--------|-----------------|
| Bubble sort | O(n) | O(n²) | O(n²) | Ya | O(1) |
| Insertion sort | O(n) | O(n²) | O(n²) | Ya | O(1) |
| Merge sort | O(n log n) | O(n log n) | O(n log n) | Ya | O(n) |
| Quick sort | O(n log n) | O(n log n) | O(n²) | Tidak | O(log n) |

## Keputusan

**Pencarian: linear search** (`PutusanSearcher`), yaitu memeriksa rekaman dari indeks 0 sampai ketemu atau habis. Alasannya:

- Data tidak terurut menurut `caseId` maupun `nomorPerkara`. Binary search akan memaksa kita mengurutkan data dulu, dan urutannya bisa hanya cocok untuk satu kunci.
- Ada dua kunci pencarian. Satu fungsi pencarian dengan predikat (`Predicate<Putusan>`) bisa dipakai untuk keduanya tanpa duplikasi kode.
- Dengan n = 50, selisih kecepatan dengan binary search tidak terasa.

**Pengurutan: insertion sort** (`PutusanSorter`) pada salinan data. Alasannya:

- **Stabil.** Perbandingan memakai `<` dan `>` ketat, sehingga putusan dengan vonis sama tetap dalam urutan aslinya (diuji di `vonisSamaTetapBerurutanAsli`).
- **Sederhana dan mudah diverifikasi.** Langkah bisa dihitung persis: 4 perbandingan untuk 5 data yang sudah terurut (n − 1), dan 10 untuk data terbalik (n(n − 1)/2).
- **Efisien untuk data kecil.** Pada n = 50, kasus terburuk hanya 1.225 perbandingan.
- **Tidak butuh memori tambahan** selain salinan data.

Aturan vonis kosong ditangani di fungsi pembanding `harusSebelum`, sehingga rekaman tanpa vonis selalu berada di akhir tanpa kode khusus di luar pembanding.

Pengurutan dilakukan pada **salinan** (`DynamicArray` baru). Struktur utama tetap utuh dan tetap valid (diuji di `strukturUtamaTidakBerubahDanTetapValid`).

## Konsekuensi

**Positif**
- Kode pendek dan mudah diuji, dan jumlah langkah bisa dibuktikan lewat tes dan demo.
- Satu fungsi pencarian melayani banyak atribut.
- Pengurutan stabil dan tidak merusak data asli.

**Negatif dan keterbatasan**
- Pencarian dan pengurutan lambat jika data membesar menjadi ribuan rekaman (O(n) dan O(n²)).
- Pengurutan menyalin seluruh data setiap dipanggil, sehingga memakai memori tambahan O(n).
- Pencarian memakai `data.get(i)`. Pada `DynamicArray`, `get` bernilai O(1) sehingga pencarian tetap O(n). Jika `StrukturData` diganti dengan struktur yang `get(i)`-nya O(n) (misalnya linked list), pencarian menjadi O(n²).

**Jika data membesar:** insertion sort dapat diganti merge sort (O(n log n), tetap stabil), dan pencarian dapat memakai indeks atau binary search pada data terurut. Karena keduanya berada di kelas `PutusanSorter` dan `PutusanSearcher`, penggantian tidak mengubah kode di bagian lain.

## Bukti Demo

Bukti menjalankan tiap skenario ada di `docs/demo/`. Contoh perintah menjalankan aplikasi ada di `README.md`.

| Skenario | Bukti |
|----------|-------|
| Cari `caseId` di posisi pertama | `docs/demo/01-cari-case-id-pertama.txt` |
| Cari `caseId` di posisi terakhir | `docs/demo/02-cari-case-id-terakhir.txt` |
| Cari data yang tidak ada | `docs/demo/03-cari-tidak-ada.txt` |
| Cari berdasarkan `nomorPerkara` | `docs/demo/04-cari-nomor-perkara.txt` |
| Urut vonis naik | `docs/demo/05-urut-vonis-naik.txt` |
| Urut vonis turun | `docs/demo/06-urut-vonis-turun.txt` |
| Vonis kosong berada di akhir | `docs/demo/07-vonis-kosong.txt` |