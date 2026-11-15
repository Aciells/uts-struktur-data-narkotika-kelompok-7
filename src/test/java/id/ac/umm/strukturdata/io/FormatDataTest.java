package id.ac.umm.strukturdata.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Tes regresi format data/putusan-50.csv: terdakwaKode dan sourcePages harus seragam. */
class FormatDataTest {

    private static final String KODE = "Terdakwa-\\d{3}[A-Z]";
    private static final String HALAMAN = "(\\d+(-\\d+)?(, \\d+(-\\d+)?)*|tidak dicatat)";
    private static final String POLA_KODE = KODE + "(;" + KODE + ")*";
    private static final String POLA_HALAMAN = "identitas: " + HALAMAN
            + "; dakwaan: " + HALAMAN
            + "; barang bukti dan berat: " + HALAMAN
            + "; tuntutan: " + HALAMAN
            + "; pertimbangan: " + HALAMAN
            + "; amar: " + HALAMAN;

    private static String[] barisData() throws IOException {
        String isi = Files.readString(Path.of("data/putusan-50.csv"), StandardCharsets.UTF_8);
        String[] baris = isi.split("\n");
        String[] data = new String[baris.length - 1];
        System.arraycopy(baris, 1, data, 0, data.length);
        return data;
    }

    @Test
    void terdakwaKodeSeragamDanSesuaiNomorCaseId() throws IOException {
        for (String baris : barisData()) {
            String[] f = CsvLineParser.parse(baris);
            assertTrue(f[4].matches(POLA_KODE), f[0] + " terdakwaKode tidak seragam: " + f[4]);
            assertEquals(f[0].substring(1), f[4].substring(9, 12), f[0] + " nomor kode tidak sama dengan caseId");
        }
    }

    @Test
    void sourcePagesSeragamPerKelompokDanTanpaEnDash() throws IOException {
        for (String baris : barisData()) {
            String[] f = CsvLineParser.parse(baris);
            assertTrue(f[15].matches(POLA_HALAMAN), f[0] + " sourcePages tidak seragam: " + f[15]);
            assertFalse(f[15].contains("\u2013"), f[0] + " masih memuat en dash");
        }
    }

    @Test
    void berkasHanyaBerisiAscii() throws IOException {
        String isi = Files.readString(Path.of("data/putusan-50.csv"), StandardCharsets.UTF_8);
        for (int i = 0; i < isi.length(); i++) {
            assertTrue(isi.charAt(i) < 128, "karakter non-ASCII pada posisi " + i + ": " + isi.charAt(i));
        }
    }
}