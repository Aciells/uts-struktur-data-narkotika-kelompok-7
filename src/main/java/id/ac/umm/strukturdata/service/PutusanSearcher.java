package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.model.Putusan;
import java.util.Objects;
import java.util.function.Predicate;

public class PutusanSearcher {
    private final StrukturData<Putusan> data;

    public PutusanSearcher(StrukturData<Putusan> data) {
        this.data = data;
    }

    public HasilCari cariByCaseId(String caseId) {
        return cari(p -> Objects.equals(caseId, p.caseId()));
    }

    public HasilCari cariByNomorPerkara(String nomorPerkara) {
        return cari(p -> Objects.equals(nomorPerkara, p.nomorPerkara()));
    }

    private HasilCari cari(Predicate<Putusan> cocok) {
        int langkah = 0;
        for (int i = 0; i < data.size(); i++) {
            langkah++;
            Putusan p = data.get(i);
            if (cocok.test(p)) {
                return new HasilCari(p, i, langkah);
            }
        }
        return new HasilCari(null, -1, langkah);
    }
}