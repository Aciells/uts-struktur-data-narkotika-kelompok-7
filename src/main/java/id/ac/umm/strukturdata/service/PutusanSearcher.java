package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.audit.AuditLog;
import id.ac.umm.strukturdata.audit.JenisOperasi;
import id.ac.umm.strukturdata.model.Putusan;
import java.util.Objects;
import java.util.function.Predicate;

public class PutusanSearcher {
    private final StrukturData<Putusan> data;
    private final AuditLog audit;

    public PutusanSearcher(StrukturData<Putusan> data) {
        this(data, new AuditLog());
    }

    public PutusanSearcher(StrukturData<Putusan> data, AuditLog audit) {
        this.data = data;
        this.audit = audit;
    }

    public HasilCari cariByCaseId(String caseId) {
        return cari("caseId", caseId, p -> Objects.equals(caseId, p.caseId()));
    }

    public HasilCari cariByNomorPerkara(String nomorPerkara) {
        return cari("nomorPerkara", nomorPerkara, p -> Objects.equals(nomorPerkara, p.nomorPerkara()));
    }

    private HasilCari cari(String atribut, String kunci, Predicate<Putusan> cocok) {
        int langkah = 0;
        for (int i = 0; i < data.size(); i++) {
            langkah++;
            Putusan p = data.get(i);
            if (cocok.test(p)) {
                catatCari(atribut, kunci, true, langkah);
                return new HasilCari(p, i, langkah);
            }
        }
        catatCari(atribut, kunci, false, langkah);
        return new HasilCari(null, -1, langkah);
    }

    private void catatCari(String atribut, String kunci, boolean ditemukan, int langkah) {
        audit.catat(JenisOperasi.CARI,
                atribut + "=" + kunci + ", ditemukan=" + ditemukan + ", langkah=" + langkah);
    }
}