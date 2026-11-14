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
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PutusanEksporterTest {

    @TempDir
    Path folder;

    private DynamicArray<Putusan> bacaUlang(Path berkas) throws IOException {
        DynamicArray<Putusan> hasil = new DynamicArray<>();
        new PutusanImporter().impor(berkas.toString(), hasil);
        return hasil;
    }

    @Test
    void eksporLaluImporMenghasilkanDataYangSama() throws IOException {
        DynamicArray<Putusan> asli = ContohPutusan.lima();
        Path berkas = folder.resolve("hasil.csv");

        int ditulis = new PutusanEksporter().eksporCsv(asli, berkas.toString());
        DynamicArray<Putusan> kembali = bacaUlang(berkas);

        assertEquals(5, ditulis);
        assertEquals(asli.size(), kembali.size());
        for (int i = 0; i < asli.size(); i++) {
            assertEquals(asli.get(i), kembali.get(i));
        }
        kembali.validateInvariant();
    }

    @Test
    void berkasBerisiHeaderDanSatuBarisPerPutusan() throws IOException {
        Path berkas = folder.resolve("hasil.csv");

        new PutusanEksporter().eksporCsv(ContohPutusan.lima(), berkas.toString());

        String[] baris = Files.readString(berkas, StandardCharsets.UTF_8).split("\n");
        assertEquals(6, baris.length);
        assertEquals(PutusanEksporter.HEADER, baris[0]);
        assertTrue(baris[1].startsWith("P001,"));
    }

    @Test
    void nilaiBerkomaKutipDanKarakterUnikodeTerbacaUtuh() throws IOException {
        Putusan khusus = new Putusan("P900", "9/Pid.Sus/2020/PN Smg",
                "Pengadilan Negeri Semarang", LocalDate.of(2021, 5, 17), "Terdakwa-900",
                "Ganja", 0.25, "Pasal 111", "Pasal 111", 12, 10, 5_000_000L,
                "ada \"kutip\", koma, dan lain-lain", "memberatkan", "meringankan",
                "amar: 36–37");
        DynamicArray<Putusan> data = new DynamicArray<>();
        data.add(khusus);
        Path berkas = folder.resolve("khusus.csv");

        new PutusanEksporter().eksporCsv(data, berkas.toString());
        DynamicArray<Putusan> kembali = bacaUlang(berkas);

        assertEquals(1, kembali.size());
        assertEquals(khusus, kembali.get(0));
    }

    @Test
    void barisBaruDidalamNilaiDiubahMenjadiSpasi() throws IOException {
        Putusan p = new Putusan("P901", "9/Pid.Sus/2020/PN Smg", "Pengadilan Negeri Semarang",
                LocalDate.of(2021, 5, 17), "Terdakwa-901", "Ganja", 1.5, "Pasal 111",
                "Pasal 111", 12, 10, 0L, "baris satu\nbaris dua", null, null, null);
        DynamicArray<Putusan> data = new DynamicArray<>();
        data.add(p);
        Path berkas = folder.resolve("baris.csv");

        new PutusanEksporter().eksporCsv(data, berkas.toString());
        DynamicArray<Putusan> kembali = bacaUlang(berkas);

        assertEquals(1, kembali.size());
        assertEquals("baris satu baris dua", kembali.get(0).barangBukti());
    }

    @Test
    void strukturKosongMenghasilkanHanyaHeader() throws IOException {
        Path berkas = folder.resolve("kosong.csv");

        int ditulis = new PutusanEksporter().eksporCsv(new DynamicArray<>(), berkas.toString());

        assertEquals(0, ditulis);
        assertEquals(PutusanEksporter.HEADER + "\n",
                Files.readString(berkas, StandardCharsets.UTF_8));
        assertEquals(0, bacaUlang(berkas).size());
    }

    @Test
    void hasilFilterBisaDiekspor() throws IOException {
        DynamicArray<Putusan> hasilFilter =
                new PutusanFilter(ContohPutusan.lima()).jenisNarkotika("ganja").hasil();
        Path berkas = folder.resolve("filter.csv");

        new PutusanEksporter().eksporCsv(hasilFilter, berkas.toString());

        assertEquals(2, bacaUlang(berkas).size());
    }

    @Test
    void eksporTeksMenyimpanIsiDenganBenar() throws IOException {
        Path berkas = folder.resolve("laporan.txt");
        String laporan = "Jumlah perkara: 5\nRata-rata vonis: 62,4 bulan\n";

        new PutusanEksporter().eksporTeks(laporan, berkas.toString());

        assertEquals(laporan, Files.readString(berkas, StandardCharsets.UTF_8));
    }

    @Test
    void folderIndukDibuatOtomatis() throws IOException {
        Path berkas = folder.resolve("hasil").resolve("laporan.csv");

        new PutusanEksporter().eksporCsv(ContohPutusan.lima(), berkas.toString());

        assertTrue(Files.exists(berkas));
    }

    @Test
    void lokasiKosongDanDataNullDitolak() {
        PutusanEksporter eksporter = new PutusanEksporter();
        assertThrows(IllegalArgumentException.class,
                () -> eksporter.eksporCsv(ContohPutusan.lima(), "   "));
        assertThrows(IllegalArgumentException.class,
                () -> eksporter.eksporCsv(null, folder.resolve("x.csv").toString()));
        assertThrows(IllegalArgumentException.class,
                () -> eksporter.eksporTeks(null, folder.resolve("x.txt").toString()));
    }

    @Test
    void eksporTercatatDiAudit() throws IOException {
        AuditLog audit = new AuditLog();
        PutusanEksporter eksporter = new PutusanEksporter(audit);

        eksporter.eksporCsv(ContohPutusan.lima(), folder.resolve("a.csv").toString());
        eksporter.eksporTeks("laporan", folder.resolve("b.txt").toString());

        assertEquals(2, audit.jumlah());
        assertEquals(JenisOperasi.EKSPOR, audit.terakhir().jenis());
        assertTrue(audit.tampilkan().contains("baris=5"));
    }

    @Test
    void eksporGagalTidakTercatat() {
        AuditLog audit = new AuditLog();
        PutusanEksporter eksporter = new PutusanEksporter(audit);

        assertThrows(IllegalArgumentException.class, () -> eksporter.eksporCsv(null, "x.csv"));

        assertEquals(0, audit.jumlah());
    }
}