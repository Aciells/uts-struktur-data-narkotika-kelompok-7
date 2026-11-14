package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.io.PutusanParser;
import id.ac.umm.strukturdata.model.Putusan;

final class ContohPutusan {

    private ContohPutusan() {
    }

    static Putusan buat(String id, String jenis, double berat, String pasalTerbukti,
                        int vonis, long denda) {
        return PutusanParser.dariBaris(id
                + ",1/Pid.Sus/2020/PN Smg,Pengadilan Negeri Semarang,2020-03-09,Terdakwa-001,"
                + jenis + "," + berat + ",Pasal 114," + pasalTerbukti + ",96," + vonis + ","
                + denda + ",\"plastik klip, timbangan\",memberatkan,meringankan,\"Identitas: 1\"");
    }

    static DynamicArray<Putusan> lima() {
        DynamicArray<Putusan> data = new DynamicArray<>();
        data.add(buat("P001", "Metamfetamina (sabu)", 5.0, "Pasal 114", 84, 1_000_000_000L));
        data.add(buat("P002", "Metamfetamina (sabu)", 20.0, "Pasal 112", 60, 800_000_000L));
        data.add(buat("P003", "Ganja", 100.0, "Pasal 114", 36, 500_000_000L));
        data.add(buat("P004", "Ganja", 2.5, "Pasal 111", 12, 0L));
        data.add(buat("P005", "Heroin", 0.5, "Pasal 112", 120, 2_000_000_000L));
        return data;
    }
}