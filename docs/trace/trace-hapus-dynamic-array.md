# Trace manual: hapus dan tambah pada DynamicArray

`DynamicArray` berbasis array, jadi tidak ada pointer. Yang berubah adalah posisi elemen
(pergeseran) dan `size`. Kode `remove(index)`:

```java
for (int i = index; i < size - 1; i++) { data[i] = data[i + 1]; }
data[size - 1] = null;
size--;
```

Jumlah pergeseran = size - 1 - index.

## A. Contoh kecil (5 elemen A B C D E, size = 5)

### remove(0)
| langkah | i | aksi | isi array | size |
|---|---|---|---|---|
| awal | | | A B C D E | 5 |
| 1 | 0 | data[0] = data[1] | B B C D E | 5 |
| 2 | 1 | data[1] = data[2] | B C C D E | 5 |
| 3 | 2 | data[2] = data[3] | B C D D E | 5 |
| 4 | 3 | data[3] = data[4] | B C D E E | 5 |
| akhir | | data[4] = null; size-- | B C D E null | 4 |

Pergeseran: 4.

### remove(2)
| langkah | i | aksi | isi array | size |
|---|---|---|---|---|
| awal | | | A B C D E | 5 |
| 1 | 2 | data[2] = data[3] | A B D D E | 5 |
| 2 | 3 | data[3] = data[4] | A B D E E | 5 |
| akhir | | data[4] = null; size-- | A B D E null | 4 |

Pergeseran: 2.

### remove(4) (terakhir)
Perulangan tidak berjalan karena 4 < 4 salah. Pergeseran: 0. Hanya `data[4] = null; size--`
sehingga isi menjadi A B C D null.

### tambah (add)
`data[size] = x; size++`. Pergeseran: 0. Bila `size == data.length`, array disalin ke array
berkapasitas dua kali lipat (resize, O(n)).

## B. Data asli 50 record (kapasitas 80 setelah impor: 10, 20, 40, 80)

| operasi | size sebelum | pergeseran | size sesudah | catatan |
|---|---|---|---|---|
| remove(0) | 50 | 49 | 49 | P001 hilang; P002 pindah dari indeks 1 ke 0, dan seterusnya |
| remove(24) | 49 | 24 | 48 | P026 hilang; P027 dan seterusnya bergeser |
| remove(47) | 48 | 0 | 47 | P050 hilang tanpa pergeseran |
| add P001, P026, P050 | 47 | 0 | 50 | menempati indeks 47, 48, 49; kapasitas tetap 80 |

Total pergeseran tiga penghapusan: 73. Bukti otomatis: `SkenarioHapusDanTambahTest`.