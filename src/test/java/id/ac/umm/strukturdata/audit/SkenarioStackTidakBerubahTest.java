package id.ac.umm.strukturdata.audit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Panduan 4.1 nomor 6: membaca Stack tidak mengubah urutan akhirnya. */
class SkenarioStackTidakBerubahTest {

    private static AuditLog logDenganTigaCatatan() {
        AuditLog log = new AuditLog();
        log.catat(JenisOperasi.IMPOR, "berkas=putusan-50.csv");
        log.catat(JenisOperasi.CARI, "caseId=P025");
        log.catat(JenisOperasi.FILTER, "jenis=sabu");
        return log;
    }

    @Test
    void skenario6_membacaRiwayatBerkali2TidakMengubahUrutan() {
        AuditLog log = logDenganTigaCatatan();
        String sebelum = log.tampilkan();
        int nomorTerakhirSebelum = log.terakhir().nomor();

        String kedua = log.tampilkan();
        String ketiga = log.tampilkan();

        assertEquals(sebelum, kedua);
        assertEquals(sebelum, ketiga);
        assertEquals(3, log.jumlah());
        assertEquals(nomorTerakhirSebelum, log.terakhir().nomor());
        assertTrue(log.validateInvariant());
    }

    @Test
    void skenario6_urutanTerbaruDiAtasDanCatatanBaruTetapDiPuncak() {
        AuditLog log = logDenganTigaCatatan();
        log.tampilkan();

        log.catat(JenisOperasi.CARI, "caseId=P999");

        assertEquals(4, log.jumlah());
        assertEquals(4, log.terakhir().nomor());
        assertTrue(log.validateInvariant());
    }

    @Test
    void skenario6_riwayatKosongTetapKosongSetelahDibaca() {
        AuditLog log = new AuditLog();

        assertEquals("(riwayat kosong)", log.tampilkan());
        assertTrue(log.kosong());
        assertEquals(0, log.jumlah());
        assertTrue(log.validateInvariant());
    }
}