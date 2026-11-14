package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.model.Putusan;
import org.junit.jupiter.api.Test;

class PutusanAggregatorTest {

    @Test
    void jumlahPerkara() {
        RingkasanStatistik r = PutusanAggregator.hitung(ContohPutusan.lima());

        assertEquals(5, r.jumlahPerkara());
    }

    @Test
    void rataRataVonis() {
        RingkasanStatistik r = PutusanAggregator.hitung(ContohPutusan.lima());

        assertEquals(62.4, r.rataRataVonisBulan(), 1e-9);
    }

    @Test
    void totalDenda() {
        RingkasanStatistik r = PutusanAggregator.hitung(ContohPutusan.lima());

        assertEquals(4_300_000_000L, r.totalDendaRupiah());
    }

    @Test
    void distribusiJenisNarkotika() {
        RingkasanStatistik r = PutusanAggregator.hitung(ContohPutusan.lima());

        assertEquals(3, r.distribusiJenis().size());
        assertEquals(2, r.distribusiJenis().get("Metamfetamina (sabu)"));
        assertEquals(2, r.distribusiJenis().get("Ganja"));
        assertEquals(1, r.distribusiJenis().get("Heroin"));
    }

    @Test
    void strukturKosongMenghasilkanNol() {
        RingkasanStatistik r = PutusanAggregator.hitung(new DynamicArray<>());

        assertEquals(0, r.jumlahPerkara());
        assertEquals(0.0, r.rataRataVonisBulan(), 1e-9);
        assertEquals(0L, r.totalDendaRupiah());
        assertTrue(r.distribusiJenis().isEmpty());
    }

    @Test
    void agregasiAtasHasilFilter() {
        DynamicArray<Putusan> ganja =
                new PutusanFilter(ContohPutusan.lima()).jenisNarkotika("ganja").hasil();

        RingkasanStatistik r = PutusanAggregator.hitung(ganja);

        assertEquals(2, r.jumlahPerkara());
        assertEquals(24.0, r.rataRataVonisBulan(), 1e-9);
        assertEquals(500_000_000L, r.totalDendaRupiah());
    }

    @Test
    void nilaiKosongVonisNolDanJenisKosong() {
        DynamicArray<Putusan> data = new DynamicArray<>();
        data.add(ContohPutusan.buat("P001", "Ganja", 1.0, "Pasal 114", 60, 1_000_000L));
        data.add(ContohPutusan.buat("P002", "", 1.0, "Pasal 114", 0, 0L));

        RingkasanStatistik r = PutusanAggregator.hitung(data);

        assertEquals(2, r.jumlahPerkara());
        assertEquals(60.0, r.rataRataVonisBulan(), 1e-9);
        assertEquals(1, r.distribusiJenis().get(PutusanAggregator.TIDAK_DIKETAHUI));
    }
}