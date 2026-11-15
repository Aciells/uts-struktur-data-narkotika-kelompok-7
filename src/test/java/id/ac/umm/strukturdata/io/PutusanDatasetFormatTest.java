package id.ac.umm.strukturdata.io;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PutusanDatasetFormatTest {
    private static final Path BERKAS = Path.of("data/putusan-50.csv");

    @TempDir
    Path folder;

    @Test
    void berkasUtf8TanpaBomDanTanpaKarakterRusak() throws IOException {
        byte[] isi = Files.readAllBytes(BERKAS);

        boolean adaBom = isi.length >= 3
                && (isi[0] & 0xFF) == 0xEF && (isi[1] & 0xFF) == 0xBB && (isi[2] & 0xFF) == 0xBF;
        assertFalse(adaBom, "berkas tidak boleh memakai BOM");

        String teks = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(isi))
                .toString();
        assertFalse(teks.contains("\uFFFD"), "ada karakter pengganti, tanda encoding salah");
    }

    @Test
    void kolomTanggalKosongAtauFormatIso() throws IOException {
        List<String> baris = Files.readAllLines(BERKAS, StandardCharsets.UTF_8);
        int iso = 0;
        for (String b : baris.subList(1, baris.size())) {
            if (b.isBlank()) {
                continue;
            }
            String tanggal = CsvLineParser.parse(b)[3];
            if (tanggal.isBlank()) {
                continue;
            }
            assertTrue(tanggal.matches("\\d{4}-\\d{2}-\\d{2}"), "bukan yyyy-MM-dd: " + tanggal);
            LocalDate.parse(tanggal);
            iso++;
        }
        // 49 tanggal terisi; P029 kosong. Ubah ke 50 bila tanggal P029 ditemukan di PDF.
        assertEquals(49, iso);
    }

    @Test
    void tanggalTerbacaBenarSetelahImpor() throws IOException {
        DynamicArray<Putusan> data = new DynamicArray<>();
        PutusanImporter importer = new PutusanImporter();

        importer.impor(BERKAS.toString(), data);

        assertEquals(50, importer.getDiterima());
        assertEquals(0, importer.getDitolak());
        assertEquals(LocalDate.of(2020, 3, 9), data.get(0).tanggalPutusan());
        assertEquals("P029", data.get(28).caseId());
        assertNull(data.get(28).tanggalPutusan());
        data.validateInvariant();
    }

    @Test
    void berkasEncodingWindows1252Ditolak() throws IOException {
        Path berkas = folder.resolve("excel.csv");
        byte[] isi = "header\nP001,10/Pid.Sus/2020/PN Smg,Pengadilan \u2013 Negeri\n"
                .getBytes(Charset.forName("windows-1252"));
        Files.write(berkas, isi);

        DynamicArray<Putusan> data = new DynamicArray<>();
        PutusanImporter importer = new PutusanImporter();

        assertThrows(IOException.class, () -> importer.impor(berkas.toString(), data));
        assertTrue(data.isEmpty());
    }
}