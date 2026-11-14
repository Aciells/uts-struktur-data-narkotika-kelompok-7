package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.io.PutusanParser;
import id.ac.umm.strukturdata.model.Putusan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PutusanManagerTest {

    private DynamicArray<Putusan> data;
    private PutusanManager manager;

    private static Putusan putusan(String id) {
        return PutusanParser.dariBaris(id
                + ",10/Pid.Sus/2020/PN Smg,Pengadilan Negeri Semarang,2020-03-09,Terdakwa-001,"
                + "Metamfetamina (sabu),6.59856,Pasal 114,Pasal 114,96,84,1000000000,"
                + "\"plastik klip, timbangan\",memberatkan,meringankan,\"Identitas: 1\"");
    }

    @BeforeEach
    void siapkan() {
        data = new DynamicArray<>();
        manager = new PutusanManager(data);
        manager.tambah(putusan("P001"));
        manager.tambah(putusan("P002"));
        manager.tambah(putusan("P003"));
        data.validateInvariant();
    }

    @Test
    void tampilSesuaiUrutanStruktur() {
        String hasil = manager.ringkasan();

        assertTrue(hasil.indexOf("P001") < hasil.indexOf("P002"));
        assertTrue(hasil.indexOf("P002") < hasil.indexOf("P003"));
        assertEquals(3, hasil.lines().count());
    }

    @Test
    void tampilStrukturKosongMenghasilkanTeksKosong() {
        DynamicArray<Putusan> kosong = new DynamicArray<>();

        assertEquals("", new PutusanManager(kosong).ringkasan());
        kosong.validateInvariant();
    }

    @Test
    void hapusElemenPertama() {
        Putusan dihapus = manager.hapus(0);

        assertEquals("P001", dihapus.caseId());
        assertEquals(2, data.size());
        assertEquals("P002", data.get(0).caseId());
        assertEquals("P003", data.get(1).caseId());
        data.validateInvariant();
    }

    @Test
    void hapusElemenTengah() {
        Putusan dihapus = manager.hapus(1);

        assertEquals("P002", dihapus.caseId());
        assertEquals(2, data.size());
        assertEquals("P001", data.get(0).caseId());
        assertEquals("P003", data.get(1).caseId());
        data.validateInvariant();
    }

    @Test
    void hapusElemenTerakhir() {
        Putusan dihapus = manager.hapus(2);

        assertEquals("P003", dihapus.caseId());
        assertEquals(2, data.size());
        assertEquals("P002", data.get(1).caseId());
        data.validateInvariant();
    }

    @Test
    void tambahKembaliSetelahHapus() {
        manager.hapus(1);
        data.validateInvariant();

        manager.tambah(putusan("P004"));

        assertEquals(3, data.size());
        assertEquals("P004", data.get(2).caseId());
        data.validateInvariant();
    }
}