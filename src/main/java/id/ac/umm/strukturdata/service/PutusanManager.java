package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.audit.AuditLog;
import id.ac.umm.strukturdata.audit.JenisOperasi;
import id.ac.umm.strukturdata.model.Putusan;

public class PutusanManager {

    private final StrukturData<Putusan> data;
    private final AuditLog audit;

    public PutusanManager(StrukturData<Putusan> data) {
        this(data, new AuditLog());
    }

    public PutusanManager(StrukturData<Putusan> data, AuditLog audit) {
        this.data = data;
        this.audit = audit;
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

    public void tambah(Putusan putusan) {
        data.add(putusan);
        String id = putusan == null ? "(null)" : putusan.caseId();
        audit.catat(JenisOperasi.TAMBAH, "caseId=" + id);
    }

    public Putusan hapus(int index) {
        Putusan dihapus = data.remove(index);
        audit.catat(JenisOperasi.HAPUS, "posisi=" + index + ", caseId=" + dihapus.caseId());
        return dihapus;
    }
}