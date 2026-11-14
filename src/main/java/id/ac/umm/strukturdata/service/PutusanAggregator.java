package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.model.Putusan;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PutusanAggregator {

    public static final String TIDAK_DIKETAHUI = "(tidak diketahui)";

    private PutusanAggregator() {
    }

    public static RingkasanStatistik hitung(StrukturData<Putusan> data) {
        long totalVonis = 0;
        int adaVonis = 0;
        long totalDenda = 0;
        Map<String, Integer> distribusi = new LinkedHashMap<>();

        for (int i = 0; i < data.size(); i++) {
            Putusan p = data.get(i);
            if (p.vonisBulan() > 0) {
                totalVonis += p.vonisBulan();
                adaVonis++;
            }
            totalDenda += p.dendaRupiah();
            String jenis = p.jenisNarkotika() == null ? TIDAK_DIKETAHUI : p.jenisNarkotika();
            distribusi.merge(jenis, 1, Integer::sum);
        }

        double rata = adaVonis == 0 ? 0.0 : (double) totalVonis / adaVonis;
        return new RingkasanStatistik(data.size(), rata, totalDenda, distribusi);
    }
}