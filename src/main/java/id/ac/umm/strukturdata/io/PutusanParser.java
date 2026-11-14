package id.ac.umm.strukturdata.io;

import id.ac.umm.strukturdata.model.Putusan;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/** Mengubah satu baris CSV menjadi Putusan. Melempar IllegalArgumentException bila tidak valid. */
public final class PutusanParser {
    public static final int JUMLAH_KOLOM = 16;

    private PutusanParser() {
    }

    public static Putusan dariBaris(String baris) {
        String[] f = CsvLineParser.parse(baris);
        if (f.length != JUMLAH_KOLOM) {
            throw new IllegalArgumentException(
                    "jumlah kolom " + f.length + ", seharusnya " + JUMLAH_KOLOM);
        }
        return new Putusan(
                wajib(f[0], "caseId"),
                wajib(f[1], "nomorPerkara"),
                wajib(f[2], "pengadilan"),
                tanggal(f[3]),
                teksAtauNull(f[4]),
                teksAtauNull(f[5]),
                desimal(f[6], "beratBarangBuktiGram"),
                teksAtauNull(f[7]),
                teksAtauNull(f[8]),
                bulat(f[9], "tuntutanBulan"),
                bulat(f[10], "vonisBulan"),
                panjang(f[11], "dendaRupiah"),
                teksAtauNull(f[12]),
                teksAtauNull(f[13]),
                teksAtauNull(f[14]),
                teksAtauNull(f[15]));
    }

    private static String wajib(String s, String nama) {
        if (s.isBlank()) {
            throw new IllegalArgumentException(nama + " kosong");
        }
        return s.trim();
    }

    private static String teksAtauNull(String s) {
        return s.isBlank() ? null : s.trim();
    }

    private static LocalDate tanggal(String s) {
        if (s.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(s.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("tanggalPutusan tidak valid: " + s);
        }
    }

    private static double desimal(String s, String nama) {
        if (s.isBlank()) {
            return 0;
        }
        try {
            double nilai = Double.parseDouble(s.trim());
            if (!Double.isFinite(nilai) || nilai < 0) {
                throw new IllegalArgumentException(nama + " tidak valid: " + s);
            }
            return nilai;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nama + " bukan angka: " + s);
        }
    }

    private static int bulat(String s, String nama) {
        if (s.isBlank()) {
            return 0;
        }
        try {
            int nilai = Integer.parseInt(s.trim());
            if (nilai < 0) {
                throw new IllegalArgumentException(nama + " negatif: " + s);
            }
            return nilai;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nama + " bukan bilangan bulat: " + s);
        }
    }

    private static long panjang(String s, String nama) {
        if (s.isBlank()) {
            return 0;
        }
        try {
            long nilai = Long.parseLong(s.trim());
            if (nilai < 0) {
                throw new IllegalArgumentException(nama + " negatif: " + s);
            }
            return nilai;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nama + " bukan bilangan bulat: " + s);
        }
    }
}