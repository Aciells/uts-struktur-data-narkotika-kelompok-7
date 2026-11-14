package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.io.PutusanImporter;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Panduan 4.1 nomor 1 dan 2. */
class SkenarioMuatDanCariTest {

    @Test
    void skenario1_muat50RecordDanSizeBernilai50() throws IOException {
        DynamicArray<Putusan> data = new DynamicArray<>();
        PutusanImporter importer = new PutusanImporter();

        importer.impor("data/putusan-50.csv", data);

        assertEquals(50, importer.getDiterima());
        assertEquals(0, importer.getDitolak());
        assertEquals(50, data.size());
        assertEquals(80, data.capacity()); // 10 -> 20 -> 40 -> 80 (tiga kali resize)
        data.validateInvariant();
    }

    @Test
    void skenario2_perkaraAdaDiPosisiTengah() throws IOException {
        DynamicArray<Putusan> data = DataAsli.muat();

        HasilCari hasil = new PutusanSearcher(data).cariByCaseId("P025");

        assertTrue(hasil.ditemukan());
        assertEquals("P025", hasil.putusan().caseId());
        assertEquals(24, hasil.indeks());
        assertEquals(25, hasil.langkah());
    }

    @Test
    void skenario2_perkaraTidakAda() throws IOException {
        DynamicArray<Putusan> data = DataAsli.muat();

        HasilCari hasil = new PutusanSearcher(data).cariByCaseId("P999");

        assertFalse(hasil.ditemukan());
        assertEquals(-1, hasil.indeks());
        assertEquals(50, hasil.langkah());
    }

    @Test
    void skenario2_posisiBatasPertamaDanTerakhir() throws IOException {
        DynamicArray<Putusan> data = DataAsli.muat();
        PutusanSearcher pencari = new PutusanSearcher(data);

        HasilCari pertama = pencari.cariByCaseId("P001");
        HasilCari terakhir = pencari.cariByCaseId("P050");

        assertEquals(0, pertama.indeks());
        assertEquals(1, pertama.langkah());
        assertEquals(49, terakhir.indeks());
        assertEquals(50, terakhir.langkah());
    }

    @Test
    void skenario2_cariNomorPerkaraDanUrutanTidakBerubah() throws IOException {
        DynamicArray<Putusan> data = DataAsli.muat();
        List<String> sebelum = DataAsli.ids(data);

        HasilCari hasil = new PutusanSearcher(data).cariByNomorPerkara("938/Pid.Sus/2019/PN Smg");

        assertTrue(hasil.ditemukan());
        assertEquals("P050", hasil.putusan().caseId());
        assertEquals(sebelum, DataAsli.ids(data));
        data.validateInvariant();
    }
}