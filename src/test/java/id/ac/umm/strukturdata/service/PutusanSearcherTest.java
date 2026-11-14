package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;
import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.Stack;
import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.io.PutusanParser;
import id.ac.umm.strukturdata.model.Putusan;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PutusanSearcherTest {

    private static final int JUMLAH = 5;

    private DynamicArray<Putusan> data;
    private PutusanSearcher searcher;

    private static Putusan putusan(String id, String nomorPerkara) {
        return PutusanParser.dariBaris(id + "," + nomorPerkara
                + ",Pengadilan Negeri Semarang,2020-03-09,Terdakwa-001,"
                + "Metamfetamina (sabu),6.59856,Pasal 114,Pasal 114,96,84,1000000000,"
                + "\"plastik klip, timbangan\",memberatkan,meringankan,\"Identitas: 1\"");
    }

    private static void isi(StrukturData<Putusan> struktur) {
        for (int i = 1; i <= JUMLAH; i++) {
            struktur.add(putusan("P00" + i, i + "/Pid.Sus/2020/PN Smg"));
        }
    }

    private static List<String> urutanId(StrukturData<Putusan> struktur) {
        List<String> id = new ArrayList<>();
        for (int i = 0; i < struktur.size(); i++) {
            id.add(struktur.get(i).caseId());
        }
        return id;
    }

    @BeforeEach
    void siapkan() {
        data = new DynamicArray<>();
        isi(data);
        searcher = new PutusanSearcher(data);
    }

    @Test
    void cariCaseIdPosisiPertama() {
        HasilCari hasil = searcher.cariByCaseId("P001");

        assertTrue(hasil.ditemukan());
        assertEquals("P001", hasil.putusan().caseId());
        assertEquals(0, hasil.indeks());
        assertEquals(1, hasil.langkah());
    }

    @Test
    void cariCaseIdPosisiTengah() {
        HasilCari hasil = searcher.cariByCaseId("P003");

        assertTrue(hasil.ditemukan());
        assertEquals(2, hasil.indeks());
        assertEquals(3, hasil.langkah());
    }

    @Test
    void cariCaseIdPosisiTerakhir() {
        HasilCari hasil = searcher.cariByCaseId("P005");

        assertTrue(hasil.ditemukan());
        assertEquals(4, hasil.indeks());
        assertEquals(JUMLAH, hasil.langkah());
    }

    @Test
    void cariCaseIdTidakAdaMemeriksaSemuaElemen() {
        HasilCari hasil = searcher.cariByCaseId("P999");

        assertFalse(hasil.ditemukan());
        assertNull(hasil.putusan());
        assertEquals(-1, hasil.indeks());
        assertEquals(JUMLAH, hasil.langkah());
    }

    @Test
    void cariNomorPerkaraAda() {
        HasilCari hasil = searcher.cariByNomorPerkara("4/Pid.Sus/2020/PN Smg");

        assertTrue(hasil.ditemukan());
        assertEquals("P004", hasil.putusan().caseId());
        assertEquals(4, hasil.langkah());
    }

    @Test
    void cariNomorPerkaraTidakAda() {
        HasilCari hasil = searcher.cariByNomorPerkara("99/Pid.Sus/2020/PN Smg");

        assertFalse(hasil.ditemukan());
        assertEquals(JUMLAH, hasil.langkah());
    }

    @Test
    void strukturKosongMenghasilkanNolLangkah() {
        PutusanSearcher kosong = new PutusanSearcher(new DynamicArray<>());

        HasilCari hasil = kosong.cariByCaseId("P001");

        assertFalse(hasil.ditemukan());
        assertEquals(0, hasil.langkah());
    }

    @Test
    void urutanDynamicArrayTidakBerubahSetelahPencarian() {
        List<String> sebelum = urutanId(data);

        searcher.cariByCaseId("P003");
        searcher.cariByCaseId("P999");
        searcher.cariByNomorPerkara("5/Pid.Sus/2020/PN Smg");

        assertEquals(sebelum, urutanId(data));
        assertEquals(JUMLAH, data.size());
        data.validateInvariant();
    }


}