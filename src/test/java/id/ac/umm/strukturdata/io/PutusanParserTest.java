package id.ac.umm.strukturdata.io;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.umm.strukturdata.model.Putusan;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PutusanParserTest {

    private static final String VALID =
            "P001,10/Pid.Sus/2020/PN Smg,Pengadilan Negeri Semarang,2020-03-09,Terdakwa-001,"
                    + "Metamfetamina (sabu),6.59856,Pasal 114,Pasal 114,96,84,1000000000,"
                    + "\"plastik klip, timbangan\",memberatkan,meringankan,\"Identitas: 1\"";

    @Test
    void barisValidMenjadiPutusan() {
        Putusan p = PutusanParser.dariBaris(VALID);
        assertEquals("P001", p.caseId());
        assertEquals(LocalDate.of(2020, 3, 9), p.tanggalPutusan());
        assertEquals(6.59856, p.beratBarangBuktiGram(), 1e-9);
        assertEquals(84, p.vonisBulan());
        assertEquals(1_000_000_000L, p.dendaRupiah());
        assertEquals("plastik klip, timbangan", p.barangBukti());
    }

    @Test
    void jumlahKolomSalahDitolak() {
        assertThrows(IllegalArgumentException.class, () -> PutusanParser.dariBaris("P001,saja"));
    }

    @Test
    void caseIdKosongDitolak() {
        String baris = VALID.replaceFirst("^P001", "");
        assertThrows(IllegalArgumentException.class, () -> PutusanParser.dariBaris(baris));
    }

    @Test
    void tanggalSalahFormatDitolak() {
        String baris = VALID.replace("2020-03-09", "09/03/2020");
        assertThrows(IllegalArgumentException.class, () -> PutusanParser.dariBaris(baris));
    }

    @Test
    void angkaBeratBertitikGandaDitolak() {
        String baris = VALID.replace("6.59856", "76.474.492");
        assertThrows(IllegalArgumentException.class, () -> PutusanParser.dariBaris(baris));
    }

    @Test
    void vonisNegatifDitolak() {
        String baris = VALID.replace(",96,84,", ",96,-1,");
        assertThrows(IllegalArgumentException.class, () -> PutusanParser.dariBaris(baris));
    }

    @Test
    void nilaiKosongMenjadiNolAtauNull() {
        String baris = VALID.replace("2020-03-09", "").replace("6.59856", "").replace(",1000000000,", ",,");
        Putusan p = PutusanParser.dariBaris(baris);
        assertNull(p.tanggalPutusan());
        assertEquals(0, p.beratBarangBuktiGram());
        assertEquals(0, p.dendaRupiah());
    }
}