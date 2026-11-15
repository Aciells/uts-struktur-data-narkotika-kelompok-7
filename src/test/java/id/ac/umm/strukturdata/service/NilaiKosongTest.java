package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.io.PutusanImporter;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Kebijakan nilai kosong: nilai tidak ditemukan diterima, record tidak valid ditolak. */
class NilaiKosongTest {

    @TempDir
    Path folder;

    private static final String KOLOM_AKHIR =
            "\"plastik klip, timbangan\",memberatkan,meringankan,\"Identitas: 1\"";

    /** Tanggal, berat, pasalTerbukti, vonis, dan denda dikosongkan. */
    private static String barisNilaiKosong(String id) {
        return id + ",10/Pid.Sus/2020/PN Smg,Pengadilan Negeri Semarang,,Terdakwa-001,"
                + "Metamfetamina (sabu),,Pasal 114,,96,,," + KOLOM_AKHIR;
    }

    private static String barisBeratBukanAngka(String id) {
        return id + ",10/Pid.Sus/2020/PN Smg,Pengadilan Negeri Semarang,2020-03-09,Terdakwa-001,"
                + "Metamfetamina (sabu),abc,Pasal 114,Pasal 114,96,84,1000000000," + KOLOM_AKHIR;
    }

    private static String barisCaseIdKosong() {
        return ",10/Pid.Sus/2020/PN Smg,Pengadilan Negeri Semarang,2020-03-09,Terdakwa-001,"
                + "Metamfetamina (sabu),6.59856,Pasal 114,Pasal 114,96,84,1000000000," + KOLOM_AKHIR;
    }

    @Test
    void nilaiTidakDitemukanDiterimaDanRecordTidakValidDitolak() throws IOException {
        Path berkas = folder.resolve("kosong.csv");
        Files.writeString(berkas, String.join("\n",
                "header",
                barisNilaiKosong("P001"),
                barisBeratBukanAngka("P002"),
                barisCaseIdKosong(),
                barisNilaiKosong("P003")) + "\n", StandardCharsets.UTF_8);
        DynamicArray<Putusan> data = new DynamicArray<>();
        PutusanImporter importer = new PutusanImporter();

        importer.impor(berkas.toString(), data);

        assertEquals(2, importer.getDiterima());
        assertEquals(2, importer.getDitolak());
        assertEquals(List.of("P001", "P003"), DataAsli.ids(data));
        Putusan p = data.get(0);
        assertNull(p.tanggalPutusan());
        assertEquals(0.0, p.beratBarangBuktiGram(), 1e-9);
        assertNull(p.pasalTerbukti());
        assertEquals(0, p.vonisBulan());
        assertEquals(0L, p.dendaRupiah());
        data.validateInvariant();
    }

    @Test
    void filterRentangTidakMeloloskanNilaiKosong() {
        DynamicArray<Putusan> data = new DynamicArray<>();
        data.add(ContohPutusan.buat("P001", "Ganja", 5.0, "Pasal 114", 60, 1_000_000L));
        data.add(ContohPutusan.buat("P002", "Ganja", 0.0, "Pasal 114", 0, 0L));

        DynamicArray<Putusan> berat = new PutusanFilter(data).beratGram(0, 10).hasil();
        DynamicArray<Putusan> vonis = new PutusanFilter(data).vonisBulan(0, 120).hasil();

        assertEquals(List.of("P001"), DataAsli.ids(berat));
        assertEquals(List.of("P001"), DataAsli.ids(vonis));
    }
}