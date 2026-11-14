package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.model.Putusan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PutusanFilterTest {
    private DynamicArray<Putusan> data;

    @BeforeEach
    void siapkan() {
        data = ContohPutusan.lima();
    }

    @Test
    void filterJenisNarkotika() {
        DynamicArray<Putusan> hasil = new PutusanFilter(data).jenisNarkotika("sabu").hasil();
        assertEquals(2, hasil.size());
        assertEquals("P001", hasil.get(0).caseId());
        assertEquals("P002", hasil.get(1).caseId());
        hasil.validateInvariant();
    }

    @Test
    void filterRentangBerat() {
        DynamicArray<Putusan> hasil = new PutusanFilter(data).beratGram(1, 30).hasil();
        assertEquals(3, hasil.size());
        assertEquals("P004", hasil.get(2).caseId());
    }

    @Test
    void filterPasalTerbukti() {
        DynamicArray<Putusan> hasil = new PutusanFilter(data).pasalTerbukti("114").hasil();
        assertEquals(2, hasil.size());
        assertEquals("P001", hasil.get(0).caseId());
        assertEquals("P003", hasil.get(1).caseId());
    }

    @Test
    void filterRentangVonis() {
        DynamicArray<Putusan> hasil = new PutusanFilter(data).vonisBulan(50, 100).hasil();
        assertEquals(2, hasil.size());
    }

    @Test
    void filterGabunganSemuaSyaratHarusTerpenuhi() {
        DynamicArray<Putusan> hasil = new PutusanFilter(data)
                .jenisNarkotika("sabu")
                .vonisBulan(70, 120)
                .hasil();
        assertEquals(1, hasil.size());
        assertEquals("P001", hasil.get(0).caseId());
    }

    @Test
    void hasilKosongBilaTidakAdaYangCocok() {
        DynamicArray<Putusan> hasil = new PutusanFilter(data).jenisNarkotika("kokain").hasil();
        assertEquals(0, hasil.size());
        assertTrue(hasil.isEmpty());
        hasil.validateInvariant();
    }

    @Test
    void jenisKosongTidakLolosFilterJenis() {
        DynamicArray<Putusan> satu = new DynamicArray<>();
        satu.add(ContohPutusan.buat("P009", "", 1.0, "Pasal 114", 60, 1_000_000L));
        DynamicArray<Putusan> hasil = new PutusanFilter(satu).jenisNarkotika("sabu").hasil();
        assertTrue(hasil.isEmpty());
    }

    @Test
    void filterTidakMengubahDataAsli() {
        new PutusanFilter(data).jenisNarkotika("ganja").hasil();
        assertEquals(5, data.size());
        assertEquals("P001", data.get(0).caseId());
        data.validateInvariant();
    }
}