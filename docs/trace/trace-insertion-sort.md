# Trace manual: insertion sort pada vonisBulan (naik)

Data awal: 60, 12, 120, 36, 84. Satu langkah = satu perbandingan (`harusSebelum`).

| i | kunci | perbandingan (nomor langkah) | elemen yang bergeser ke kanan | isi setelah |
|---|---|---|---|---|
| 1 | 12 | 12 < 60 benar (1) | 60 | 12 60 120 36 84 |
| 2 | 120 | 120 < 60 salah (2) | tidak ada | 12 60 120 36 84 |
| 3 | 36 | 36 < 120 benar (3), 36 < 60 benar (4), 36 < 12 salah (5) | 120, 60 | 12 36 60 120 84 |
| 4 | 84 | 84 < 120 benar (6), 84 < 60 salah (7) | 120 | 12 36 60 84 120 |

Total: 7 langkah. Bukti otomatis: `AnalisisKompleksitasTest.contohAcakLimaDataButuhTujuhLangkah`.