package id.ac.umm.strukturdata.io;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CsvLineParserTest {
    @Test
    void barisSederhana() {
        String[] f = CsvLineParser.parse("a,b,c");
        assertArrayEquals(new String[] {"a", "b", "c"}, f);
    }

    @Test
    void komaDidalamKutipBukanPemisah() {
        String[] f = CsvLineParser.parse("P001,\"plastik klip, timbangan\",96");
        assertEquals(3, f.length);
        assertEquals("plastik klip, timbangan", f[1]);
    }

    @Test
    void kolomKosongTetapDihitung() {
        assertEquals(4, CsvLineParser.parse("a,,c,").length);
    }

    @Test
    void kutipGandaMenjadiSatuKutip() {
        assertEquals("ia berkata \"ya\"", CsvLineParser.parse("\"ia berkata \"\"ya\"\"\"")[0]);
    }

    @Test
    void kutipTidakDitutupDitolak() {
        assertThrows(IllegalArgumentException.class, () -> CsvLineParser.parse("a,\"belum ditutup"));
    }
}