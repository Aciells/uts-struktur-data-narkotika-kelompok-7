package id.ac.umm.strukturdata.service;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.audit.AuditLog;
import id.ac.umm.strukturdata.audit.JenisOperasi;
import id.ac.umm.strukturdata.io.PutusanImporter;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AuditIntegrasiTest {

    @TempDir
    Path folder;

    @Test
    void operasiTercatatBerurutanDiSatuRiwayat() {
        AuditLog audit = new AuditLog();
        DynamicArray<Putusan> data = ContohPutusan.lima();
        PutusanManager manager = new PutusanManager(data, audit);
        PutusanSearcher searcher = new PutusanSearcher(data, audit);
        PutusanFilter filter = new PutusanFilter(data, audit);
        PutusanSorter sorter = new PutusanSorter(data, audit);

        manager.tambah(ContohPutusan.buat("P006", "Ganja", 1.0, "Pasal 111", 24, 0L));
        searcher.cariByCaseId("P003");
        filter.jenisNarkotika("sabu").vonisBulan(60, 120).hasil();
        sorter.urutkanByVonis(true);
        manager.hapus(0);

        assertEquals(5, audit.jumlah());
        assertEquals(JenisOperasi.HAPUS, audit.terakhir().jenis());

        String riwayat = audit.tampilkan();
        assertTrue(riwayat.indexOf("HAPUS") < riwayat.indexOf("URUT"));
        assertTrue(riwayat.indexOf("URUT") < riwayat.indexOf("FILTER"));
        assertTrue(riwayat.indexOf("FILTER") < riwayat.indexOf("CARI"));
        assertTrue(riwayat.indexOf("CARI") < riwayat.indexOf("TAMBAH"));
        assertTrue(riwayat.contains("caseId=P003, ditemukan=true, langkah=3"));
        assertTrue(riwayat.contains("hasil=2"));
        assertTrue(riwayat.contains("jumlah=6"));
        assertTrue(audit.validateInvariant());
    }

    @Test
    void pencarianTidakDitemukanTetapTercatat() {
        AuditLog audit = new AuditLog();
        PutusanSearcher searcher = new PutusanSearcher(ContohPutusan.lima(), audit);

        searcher.cariByCaseId("P999");

        assertEquals(1, audit.jumlah());
        assertTrue(audit.terakhir().detail().contains("ditemukan=false, langkah=5"));
    }

    @Test
    void operasiGagalTidakTercatat() {
        AuditLog audit = new AuditLog();
        PutusanManager manager = new PutusanManager(ContohPutusan.lima(), audit);

        assertThrows(IndexOutOfBoundsException.class, () -> manager.hapus(99));

        assertEquals(0, audit.jumlah());
        assertTrue(audit.validateInvariant());
    }

    @Test
    void imporTercatatDenganJumlahDiterimaDanDitolak() throws IOException {
        String valid = "P001,1/Pid.Sus/2020/PN Smg,Pengadilan Negeri Semarang,2020-03-09,Terdakwa-001,"
                + "Ganja,1.5,Pasal 114,Pasal 114,96,84,1000000,"
                + "\"plastik klip, timbangan\",memberatkan,meringankan,\"Identitas: 1\"";
        Path berkas = folder.resolve("uji.csv");
        Files.writeString(berkas, "header\n" + valid + "\nrusak,saja\n", StandardCharsets.UTF_8);
        AuditLog audit = new AuditLog();

        new PutusanImporter(audit).impor(berkas.toString(), new DynamicArray<>());

        assertEquals(JenisOperasi.IMPOR, audit.terakhir().jenis());
        assertTrue(audit.terakhir().detail().contains("diterima=1, ditolak=1"));
    }
}