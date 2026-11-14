package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.model.Putusan;
import org.junit.jupiter.api.Test;

/** Pencacah langkah dipakai untuk membuktikan analisis kompleksitas di ADR-002. */
class AnalisisKompleksitasTest {

    private static final int[] UKURAN = {10, 20, 40, 50, 80};

    private static DynamicArray<Putusan> dataVonis(int... vonis) {
        DynamicArray<Putusan> data = new DynamicArray<>();
        for (int i = 0; i < vonis.length; i++) {
            data.add(ContohPutusan.buat("P" + (i + 1), "Metamfetamina (sabu)", 6.59856,
                    "Pasal 114", vonis[i], 1_000_000_000L));
        }
        return data;
    }

    private static DynamicArray<Putusan> terurut(int n) {
        int[] vonis = new int[n];
        for (int i = 0; i < n; i++) {
            vonis[i] = i + 1;
        }
        return dataVonis(vonis);
    }

    private static DynamicArray<Putusan> terbalik(int n) {
        int[] vonis = new int[n];
        for (int i = 0; i < n; i++) {
            vonis[i] = n - i;
        }
        return dataVonis(vonis);
    }

    private static int langkahUrut(DynamicArray<Putusan> data) {
        PutusanSorter pengurut = new PutusanSorter(data);
        pengurut.urutkanByVonis(true);
        return pengurut.getLangkah();
    }

    private static int langkahCariTidakAda(int n) {
        return new PutusanSearcher(terurut(n)).cariByCaseId("TIDAK-ADA").langkah();
    }

    @Test
    void pencarianTerbaikSatuLangkahTerburukNLangkah() {
        for (int n : UKURAN) {
            PutusanSearcher pencari = new PutusanSearcher(terurut(n));

            assertEquals(1, pencari.cariByCaseId("P1").langkah());
            assertEquals(n, pencari.cariByCaseId("P" + n).langkah());
            assertEquals(n, pencari.cariByCaseId("TIDAK-ADA").langkah());
        }
    }

    @Test
    void dataDuaKaliLebihBanyakMembuatLangkahCariTerburukDuaKali() {
        assertEquals(2.0, (double) langkahCariTidakAda(40) / langkahCariTidakAda(20), 1e-9);
    }

    @Test
    void pengurutanSudahTerurutButuhNMinSatuLangkah() {
        for (int n : UKURAN) {
            assertEquals(n - 1, langkahUrut(terurut(n)));
        }
    }

    @Test
    void pengurutanTerbalikButuhNKaliNMinSatuPerDuaLangkah() {
        for (int n : UKURAN) {
            assertEquals(n * (n - 1) / 2, langkahUrut(terbalik(n)));
        }
        assertEquals(1225, langkahUrut(terbalik(50)));
    }

    @Test
    void dataDuaKaliLebihBanyakMembuatLangkahUrutTerburukSekitarEmpatKali() {
        double rasio = (double) langkahUrut(terbalik(40)) / langkahUrut(terbalik(20));

        assertTrue(rasio > 3.5 && rasio < 4.5, "rasio = " + rasio);
    }

    @Test
    void contohAcakLimaDataButuhTujuhLangkah() {
        assertEquals(7, langkahUrut(dataVonis(60, 12, 120, 36, 84)));
    }
}