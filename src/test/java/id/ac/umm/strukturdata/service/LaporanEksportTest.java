package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.audit.AuditLog;
import id.ac.umm.strukturdata.audit.JenisOperasi;
import id.ac.umm.strukturdata.io.PutusanEksporter;
import id.ac.umm.strukturdata.io.PutusanImporter;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LaporanEksportTest {

    @TempDir
    Path folder;

    private DynamicArray<Putusan> bacaUlang(Path berkas) throws IOException {
        DynamicArray<Putusan> hasil = new DynamicArray<>();
        new PutusanImporter().impor(berkas.toString(), hasil);
        return hasil;
    }

    @Test
    void hasilCariDitemukanDiekspordanTerbacaUlang() throws IOException {
        HasilCari hasil = new PutusanSearcher(ContohPutusan.lima()).cariByCaseId("P003");
        Path berkas = folder.resolve("cari.csv");

        int baris = new LaporanEksport(new PutusanEksporter()).eksporHasilCari(hasil, berkas.toString());

        DynamicArray<Putusan> kembali = bacaUlang(berkas);
        assertEquals(1, baris);
        assertEquals(1, kembali.size());
        assertEquals("P003", kembali.get(0).caseId());
        assertEquals(hasil.putusan(), kembali.get(0));
    }

    @Test
    void hasilCariTidakDitemukanMenghasilkanHanyaHeader() throws IOException {
        HasilCari hasil = new PutusanSearcher(ContohPutusan.lima()).cariByCaseId("P999");
        Path berkas = folder.resolve("kosong.csv");

        int baris = new LaporanEksport(new PutusanEksporter()).eksporHasilCari(hasil, berkas.toString());

        assertEquals(0, baris);
        assertEquals(PutusanEksporter.HEADER + "\n", Files.readString(berkas, StandardCharsets.UTF_8));
    }

    @Test
    void laporanCariMemuatJumlahLangkah() throws IOException {
        HasilCari hasil = new PutusanSearcher(ContohPutusan.lima()).cariByCaseId("P003");
        Path berkas = folder.resolve("cari.txt");

        new LaporanEksport(new PutusanEksporter()).eksporLaporanCari(hasil, berkas.toString());

        String teks = Files.readString(berkas, StandardCharsets.UTF_8);
        assertTrue(teks.contains("Ditemukan        : ya"));
        assertTrue(teks.contains("Jumlah langkah   : 3"));
        assertTrue(teks.contains("Posisi (indeks)  : 2"));
        assertTrue(teks.contains("caseId           : P003"));
    }

    @Test
    void laporanCariTidakDitemukanMemuatLangkahTanpaPosisi() {
        HasilCari hasil = new PutusanSearcher(ContohPutusan.lima()).cariByCaseId("P999");

        String teks = LaporanEksport.teksPencarian(hasil);

        assertTrue(teks.contains("Ditemukan        : tidak"));
        assertTrue(teks.contains("Jumlah langkah   : 5"));
        assertFalse(teks.contains("Posisi (indeks)"));
    }

    @Test
    void laporanStatistikBerisiAngkaYangBenar() throws IOException {
        RingkasanStatistik ringkasan = PutusanAggregator.hitung(ContohPutusan.lima());
        Path berkas = folder.resolve("statistik.txt");

        new LaporanEksport(new PutusanEksporter()).eksporStatistik(ringkasan, berkas.toString());

        String teks = Files.readString(berkas, StandardCharsets.UTF_8);
        assertTrue(teks.contains("Jumlah perkara   : 5"));
        assertTrue(teks.contains("62.40 bulan"));
        assertTrue(teks.contains("Total denda      : Rp4300000000"));
        assertTrue(teks.contains("- Metamfetamina (sabu): 2"));
        assertTrue(teks.contains("- Ganja: 2"));
        assertTrue(teks.contains("- Heroin: 1"));
    }

    @Test
    void eksporHasilCariDanStatistikTercatatDiAudit() throws IOException {
        AuditLog audit = new AuditLog();
        LaporanEksport laporan = new LaporanEksport(new PutusanEksporter(audit));
        HasilCari hasil = new PutusanSearcher(ContohPutusan.lima()).cariByCaseId("P001");

        laporan.eksporHasilCari(hasil, folder.resolve("a.csv").toString());
        laporan.eksporLaporanCari(hasil, folder.resolve("b.txt").toString());
        laporan.eksporStatistik(PutusanAggregator.hitung(ContohPutusan.lima()),
                folder.resolve("c.txt").toString());

        assertEquals(3, audit.jumlah());
        assertEquals(JenisOperasi.EKSPOR, audit.terakhir().jenis());
        assertTrue(audit.validateInvariant());
    }

    @Test
    void argumenNullDitolak() {
        LaporanEksport laporan = new LaporanEksport(new PutusanEksporter());
        String lokasi = folder.resolve("x.txt").toString();
        assertThrows(IllegalArgumentException.class, () -> new LaporanEksport(null));
        assertThrows(IllegalArgumentException.class, () -> laporan.eksporHasilCari(null, lokasi));
        assertThrows(IllegalArgumentException.class, () -> laporan.eksporLaporanCari(null, lokasi));
        assertThrows(IllegalArgumentException.class, () -> laporan.eksporStatistik(null, lokasi));
    }
}