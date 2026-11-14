package id.ac.umm.strukturdata.io;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PutusanImporterTest {

    @TempDir
    Path folder;

    private static String valid(String id) {
        return id + ",10/Pid.Sus/2020/PN Smg,Pengadilan Negeri Semarang,2020-03-09,Terdakwa-001,"
                + "Metamfetamina (sabu),6.59856,Pasal 114,Pasal 114,96,84,1000000000,"
                + "\"plastik klip, timbangan\",memberatkan,meringankan,\"Identitas: 1\"";
    }

    private Path tulis(String... baris) throws IOException {
        Path berkas = folder.resolve("uji.csv");
        Files.writeString(berkas, String.join("\n", baris) + "\n", StandardCharsets.UTF_8);
        return berkas;
    }

    @Test
    void recordValidMasukDanRecordRusakDitolak() throws IOException {
        Path berkas = tulis("header", valid("P001"), "P002,kolom kurang", valid("P003"));
        DynamicArray<Putusan> data = new DynamicArray<>();
        PutusanImporter importer = new PutusanImporter();

        importer.impor(berkas.toString(), data);

        assertEquals(2, importer.getDiterima());
        assertEquals(1, importer.getDitolak());
        assertEquals(2, data.size());
        assertEquals("P001", data.get(0).caseId());
        assertEquals("P003", data.get(1).caseId());
        data.validateInvariant();
    }

    @Test
    void berkasHanyaHeaderMenghasilkanStrukturKosong() throws IOException {
        Path berkas = tulis("header");
        DynamicArray<Putusan> data = new DynamicArray<>();
        PutusanImporter importer = new PutusanImporter();

        importer.impor(berkas.toString(), data);

        assertEquals(0, importer.getDiterima());
        assertTrue(data.isEmpty());
        data.validateInvariant();
    }

    @Test
    void imporDataAsliMenghasilkanTepat50Record() throws IOException {
        DynamicArray<Putusan> data = new DynamicArray<>();
        PutusanImporter importer = new PutusanImporter();

        importer.impor("data/putusan-50.csv", data);

        assertEquals(50, importer.getDiterima());
        assertEquals(0, importer.getDitolak());
        assertEquals(50, data.size());
        assertEquals("P001", data.get(0).caseId());
        assertEquals("P050", data.get(49).caseId());
        data.validateInvariant();
    }
}