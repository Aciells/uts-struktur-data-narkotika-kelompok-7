package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Panduan 4.1 nomor 5. */
class SkenarioFilterDataAsliTest {

    /**
     * Hasil pemeriksaan manual ke data/data-provenance.csv: jenis memuat "sabu",
     * berat 1 sampai 10 gram, vonis 60 sampai 120 bulan. Periksa ulang sebelum dipercaya.
     */
    private static final List<String> DIHARAPKAN = List.of(
            "P001", "P006", "P019", "P026", "P027", "P030", "P032", "P034", "P037", "P042");

    private static DynamicArray<Putusan> saring() throws IOException {
        return new PutusanFilter(DataAsli.muat())
                .jenisNarkotika("sabu")
                .beratGram(1, 10)
                .vonisBulan(60, 120)
                .hasil();
    }

    @Test
    void skenario5_filterGabunganCocokDenganProvenanceManual() throws IOException {
        DynamicArray<Putusan> hasil = saring();

        assertEquals(DIHARAPKAN, DataAsli.ids(hasil));
        hasil.validateInvariant();
    }

    @Test
    void skenario5_agregasiHasilFilterCocokDenganHitungManual() throws IOException {
        RingkasanStatistik ringkasan = PutusanAggregator.hitung(saring());

        assertEquals(10, ringkasan.jumlahPerkara());
        assertEquals(67.2, ringkasan.rataRataVonisBulan(), 1e-9);
        assertEquals(10_800_000_000L, ringkasan.totalDendaRupiah());
    }
}