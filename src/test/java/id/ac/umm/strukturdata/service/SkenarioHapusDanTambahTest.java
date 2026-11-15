package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Panduan 4.1 nomor 3 dan 4. Trace manual pergeseran ada di docs/trace/. */
class SkenarioHapusDanTambahTest {

    private DynamicArray<Putusan> data;
    private PutusanManager manager;

    @BeforeEach
    void muat() throws IOException {
        data = DataAsli.muat();
        manager = new PutusanManager(data);
    }

    /** Elemen sebelum indeks yang dihapus tetap; sesudahnya bergeser satu posisi ke kiri. */
    private static void assertGeserKiri(List<String> sebelum, DynamicArray<Putusan> sesudah, int index) {
        assertEquals(sebelum.size() - 1, sesudah.size());
        for (int i = 0; i < sesudah.size(); i++) {
            String diharapkan = i < index ? sebelum.get(i) : sebelum.get(i + 1);
            assertEquals(diharapkan, sesudah.get(i).caseId(), "posisi " + i);
        }
    }

    @Test
    void skenario3_hapusElemenPertamaSemuaElemenBergeserKeKiri() {
        List<String> sebelum = DataAsli.ids(data);

        Putusan dihapus = manager.hapus(0);

        assertEquals("P001", dihapus.caseId());
        assertGeserKiri(sebelum, data, 0);
        data.validateInvariant();
    }

    @Test
    void skenario3_hapusElemenTengahHanyaSebagianBergeser() {
        List<String> sebelum = DataAsli.ids(data);
        int tengah = data.size() / 2;

        Putusan dihapus = manager.hapus(tengah);

        assertEquals(sebelum.get(tengah), dihapus.caseId());
        assertGeserKiri(sebelum, data, tengah);
        data.validateInvariant();
    }

    @Test
    void skenario3_hapusElemenTerakhirTanpaPergeseran() {
        List<String> sebelum = DataAsli.ids(data);
        int terakhir = data.size() - 1;

        Putusan dihapus = manager.hapus(terakhir);

        assertEquals("P050", dihapus.caseId());
        assertGeserKiri(sebelum, data, terakhir);
        data.validateInvariant();
    }

    @Test
    void skenario4_tambahKembaliTigaRecordInvariantDanUrutanTerjaga() {
        Putusan pertama = manager.hapus(0);
        Putusan tengah = manager.hapus(data.size() / 2);
        Putusan terakhir = manager.hapus(data.size() - 1);
        assertEquals(47, data.size());
        data.validateInvariant();
        List<String> sebelumTambah = DataAsli.ids(data);

        manager.tambah(pertama);
        data.validateInvariant();
        manager.tambah(tengah);
        data.validateInvariant();
        manager.tambah(terakhir);
        data.validateInvariant();

        List<String> sesudah = DataAsli.ids(data);
        assertEquals(50, data.size());
        assertEquals(sebelumTambah, sesudah.subList(0, 47));
        assertEquals(List.of("P001", "P026", "P050"), sesudah.subList(47, 50));
        assertEquals(50, new HashSet<>(sesudah).size());
    }
}