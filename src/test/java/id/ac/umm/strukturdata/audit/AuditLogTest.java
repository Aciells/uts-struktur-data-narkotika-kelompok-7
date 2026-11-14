package id.ac.umm.strukturdata.audit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class AuditLogTest {

    @Test
    void awalnyaKosong() {
        AuditLog log = new AuditLog();
        assertTrue(log.kosong());
        assertEquals(0, log.jumlah());
        assertEquals("(riwayat kosong)", log.tampilkan());
        assertThrows(NoSuchElementException.class, log::terakhir);
        assertTrue(log.validateInvariant());
    }

    @Test
    void catatMenambahRiwayatDenganNomorBerurutan() {
        AuditLog log = new AuditLog();
        log.catat(JenisOperasi.TAMBAH, "caseId=P051");
        CatatanAudit kedua = log.catat(JenisOperasi.HAPUS, "posisi=0");

        assertEquals(2, log.jumlah());
        assertEquals(2, kedua.nomor());
        assertEquals(JenisOperasi.HAPUS, log.terakhir().jenis());
        assertTrue(log.validateInvariant());
    }

    @Test
    void tampilkanUrutDariTerbaruKeTerlama() {
        AuditLog log = new AuditLog();
        log.catat(JenisOperasi.TAMBAH, "caseId=P051");
        log.catat(JenisOperasi.HAPUS, "posisi=0");

        String teks = log.tampilkan();
        assertTrue(teks.indexOf("HAPUS") < teks.indexOf("TAMBAH"));
    }

    @Test
    void tampilkanTidakMengubahRiwayat() {
        AuditLog log = new AuditLog();
        log.catat(JenisOperasi.IMPOR, "diterima=50");
        log.catat(JenisOperasi.CARI, "kunci=P001");
        log.catat(JenisOperasi.FILTER, "jenis=sabu");
        CatatanAudit akhir = log.terakhir();

        String pertama = log.tampilkan();
        String kedua = log.tampilkan();

        assertEquals(pertama, kedua);
        assertEquals(3, log.jumlah());
        assertSame(akhir, log.terakhir());
        assertTrue(log.validateInvariant());
    }

    @Test
    void riwayatMelebihiKapasitasAwalTetapUtuh() {
        AuditLog log = new AuditLog();
        for (int i = 1; i <= 25; i++) {
            log.catat(JenisOperasi.CARI, "kunci=" + i);
        }
        assertEquals(25, log.jumlah());
        assertEquals(25, log.terakhir().nomor());
        assertTrue(log.validateInvariant());

        int baris = 0;
        for (char c : log.tampilkan().toCharArray()) {
            if (c == '\n') {
                baris++;
            }
        }
        assertEquals(25, baris);
    }

    @Test
    void jenisNullDitolakDanRiwayatTetapKonsisten() {
        AuditLog log = new AuditLog();
        assertThrows(IllegalArgumentException.class, () -> log.catat(null, "x"));
        assertEquals(0, log.jumlah());
        assertTrue(log.validateInvariant());
    }
}