package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.io.PutusanImporter;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SkenarioRecordTidakValidTest {

    @TempDir
    Path folder;

    private static String baris(String id, String tanggal, String vonis) {
        return id + ",10/Pid.Sus/2020/PN Smg,Pengadilan Negeri Semarang," + tanggal
                + ",Terdakwa-001,Metamfetamina (sabu),6.59856,Pasal 114,Pasal 114,96," + vonis
                + ",1000000000,\"plastik klip, timbangan\",memberatkan,meringankan,\"Identitas: 1\"";
    }

    @Test
    void skenario7_recordTidakValidDitolakDenganPesanDanStrukturKonsisten() throws IOException {
        Path berkas = folder.resolve("campur.csv");
        Files.writeString(berkas, String.join("\n",
                "header",
                baris("P001", "2020-03-09", "84"),    // baris 2: valid
                "P002,kolom kurang",                  // baris 3: jumlah kolom salah
                baris("P003", "09-03-2020", "84"),    // baris 4: format tanggal salah
                baris("P004", "2020-03-09", "-5"),    // baris 5: angka negatif
                baris("P005", "2020-03-09", "abc"),   // baris 6: bukan angka
                baris("P006", "2020-03-09", "60")     // baris 7: valid
        ) + "\n", StandardCharsets.UTF_8);
        DynamicArray<Putusan> data = new DynamicArray<>();
        PutusanImporter importer = new PutusanImporter();

        PrintStream asli = System.out;
        ByteArrayOutputStream tangkap = new ByteArrayOutputStream();
        System.setOut(new PrintStream(tangkap, true, StandardCharsets.UTF_8));
        try {
            importer.impor(berkas.toString(), data);
        } finally {
            System.setOut(asli);
        }
        String keluaran = tangkap.toString(StandardCharsets.UTF_8);

        assertEquals(2, importer.getDiterima());
        assertEquals(4, importer.getDitolak());
        assertTrue(keluaran.contains("Baris 3 ditolak: jumlah kolom 2, seharusnya 16"));
        assertTrue(keluaran.contains("Baris 4 ditolak:"));
        assertTrue(keluaran.contains("Baris 5 ditolak:"));
        assertTrue(keluaran.contains("Baris 6 ditolak:"));
        assertEquals(List.of("P001", "P006"), DataAsli.ids(data));
        data.validateInvariant();
    }
}