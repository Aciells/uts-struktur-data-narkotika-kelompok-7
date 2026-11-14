package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.io.PutusanParser;
import id.ac.umm.strukturdata.model.Putusan;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PutusanSorterTest {

    private static DynamicArray<Putusan> data(int... vonis) {
        DynamicArray<Putusan> hasil = new DynamicArray<>();
        for (int i = 0; i < vonis.length; i++) {
            String kolomVonis = vonis[i] == 0 ? "" : String.valueOf(vonis[i]);
            hasil.add(PutusanParser.dariBaris("P" + (i + 1)
                    + ",1/Pid.Sus/2020/PN Smg,Pengadilan Negeri Semarang,2020-03-09,Terdakwa-001,"
                    + "Metamfetamina (sabu),6.59856,Pasal 114,Pasal 114,96," + kolomVonis
                    + ",1000000000,\"plastik klip, timbangan\",memberatkan,meringankan,"
                    + "\"Identitas: 1\""));
        }
        return hasil;
    }

    private static List<String> ids(DynamicArray<Putusan> struktur) {
        List<String> id = new ArrayList<>();
        for (int i = 0; i < struktur.size(); i++) {
            id.add(struktur.get(i).caseId());
        }
        return id;
    }

    @Test
    void dataAcakTerurutNaik() {
        DynamicArray<Putusan> asal = data(60, 12, 120, 36, 84);

        DynamicArray<Putusan> hasil = new PutusanSorter(asal).urutkanByVonis(true);

        assertEquals(List.of("P2", "P4", "P1", "P5", "P3"), ids(hasil));
        for (int i = 1; i < hasil.size(); i++) {
            assertTrue(hasil.get(i - 1).vonisBulan() <= hasil.get(i).vonisBulan());
        }
        hasil.validateInvariant();
    }

    @Test
    void dataAcakTerurutTurun() {
        DynamicArray<Putusan> asal = data(60, 12, 120, 36, 84);

        DynamicArray<Putusan> hasil = new PutusanSorter(asal).urutkanByVonis(false);

        assertEquals(List.of("P3", "P5", "P1", "P4", "P2"), ids(hasil));
    }

    @Test
    void sudahTerurutTetapDanHanyaNMinSatuLangkah() {
        DynamicArray<Putusan> asal = data(12, 36, 60, 84, 120);
        PutusanSorter pengurut = new PutusanSorter(asal);

        DynamicArray<Putusan> hasil = pengurut.urutkanByVonis(true);

        assertEquals(List.of("P1", "P2", "P3", "P4", "P5"), ids(hasil));
        assertEquals(4, pengurut.getLangkah());
    }

    @Test
    void dataTerbalikButuhLangkahTerburuk() {
        DynamicArray<Putusan> asal = data(120, 84, 60, 36, 12);
        PutusanSorter pengurut = new PutusanSorter(asal);

        DynamicArray<Putusan> hasil = pengurut.urutkanByVonis(true);

        assertEquals(List.of("P5", "P4", "P3", "P2", "P1"), ids(hasil));
        assertEquals(10, pengurut.getLangkah());
    }

    @Test
    void vonisSamaTetapBerurutanAsli() {
        DynamicArray<Putusan> asal = data(60, 36, 60, 36, 60);

        DynamicArray<Putusan> hasil = new PutusanSorter(asal).urutkanByVonis(true);

        assertEquals(List.of("P2", "P4", "P1", "P3", "P5"), ids(hasil));
    }

    @Test
    void vonisKosongSelaluDiAkhirNaik() {
        DynamicArray<Putusan> asal = data(60, 0, 12, 0, 36);

        DynamicArray<Putusan> hasil = new PutusanSorter(asal).urutkanByVonis(true);

        assertEquals(List.of("P3", "P5", "P1", "P2", "P4"), ids(hasil));
    }

    @Test
    void vonisKosongSelaluDiAkhirTurun() {
        DynamicArray<Putusan> asal = data(60, 0, 12, 0, 36);

        DynamicArray<Putusan> hasil = new PutusanSorter(asal).urutkanByVonis(false);

        assertEquals(List.of("P1", "P5", "P3", "P2", "P4"), ids(hasil));
    }

    @Test
    void strukturKosongDanSatuElemenTanpaPerbandingan() {
        PutusanSorter kosong = new PutusanSorter(new DynamicArray<>());
        assertTrue(kosong.urutkanByVonis(true).isEmpty());
        assertEquals(0, kosong.getLangkah());

        PutusanSorter satu = new PutusanSorter(data(60));
        assertEquals(List.of("P1"), ids(satu.urutkanByVonis(true)));
        assertEquals(0, satu.getLangkah());
    }

    @Test
    void strukturUtamaTidakBerubahDanTetapValid() {
        DynamicArray<Putusan> asal = data(60, 12, 120, 36, 84);
        List<String> sebelum = ids(asal);

        new PutusanSorter(asal).urutkanByVonis(true);
        new PutusanSorter(asal).urutkanByVonis(false);

        assertEquals(sebelum, ids(asal));
        assertEquals(5, asal.size());
        asal.validateInvariant();
    }
}