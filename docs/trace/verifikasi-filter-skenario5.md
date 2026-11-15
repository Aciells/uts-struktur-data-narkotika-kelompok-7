# Verifikasi filter gabungan (skenario 5)

## Kombinasi filter
Jenis narkotika memuat "sabu", berat barang bukti 1 sampai 10 gram, vonis 60 sampai 120 bulan.

## Hasil
- Hasil program (`SkenarioFilterDataAsliTest`, data/putusan-50.csv): 10 perkara, yaitu P001, P006, P019, P026, P027, P030, P032, P034, P037, P042.
- Rata-rata vonis 67,2 bulan, total denda Rp10.800.000.000.
- Daftar pembanding disusun dari data/data-provenance.csv dengan bantuan Claude (lihat AI_USAGE.md). Hasil program cocok dengan daftar itu.

## Pemeriksaan manual ke data/data-provenance.csv
Dicentang hanya setelah anggota kelompok membandingkan nilainya sendiri.

- [ ] P019: vonis 60 (batas bawah)
- [ ] P026: vonis 60 (batas bawah)
- [ ] P027: vonis 60 (batas bawah)
- [ ] P034: vonis 60 (batas bawah)
- [ ] P006: berat 1.35173 (dekat batas bawah)
- [ ] P046: berat 0.81823, seharusnya tidak lolos (di bawah 1 gram)
- [ ] P047: berat 10.91563, seharusnya tidak lolos (di atas 10 gram)
- [ ] P011: berat di provenance bukan angka tunggal ("1,16376 gram + 0,25499 gram.."). Cek nilainya di putusan-50.csv.
