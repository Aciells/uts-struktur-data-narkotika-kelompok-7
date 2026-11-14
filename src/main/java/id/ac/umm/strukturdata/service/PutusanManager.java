package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.model.Putusan;

public class PutusanManager {

    private final StrukturData<Putusan> data;

    public PutusanManager(StrukturData<Putusan> data) {
        this.data = data;
    }

    public String ringkasan() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            Putusan p = data.get(i);
            sb.append(i + 1).append(". ")
                    .append(p.caseId()).append(" | ")
                    .append(p.tanggalPutusan()).append(" | vonis ")
                    .append(p.vonisBulan()).append(" bln | denda Rp")
                    .append(p.dendaRupiah()).append('\n');
        }
        return sb.toString();
    }

    public void tampil() {
        System.out.print(ringkasan());
    }
}