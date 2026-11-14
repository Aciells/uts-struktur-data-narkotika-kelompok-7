package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.audit.AuditLog;
import id.ac.umm.strukturdata.audit.JenisOperasi;
import id.ac.umm.strukturdata.model.Putusan;
import java.util.function.Predicate;

public class PutusanFilter {

    private final StrukturData<Putusan> data;
    private final AuditLog audit;
    private Predicate<Putusan> syarat = p -> true;
    private final StringBuilder kriteria = new StringBuilder();

    public PutusanFilter(StrukturData<Putusan> data) {
        this(data, new AuditLog());
    }

    public PutusanFilter(StrukturData<Putusan> data, AuditLog audit) {
        this.data = data;
        this.audit = audit;
    }

    public PutusanFilter jenisNarkotika(String kata) {
        String kunci = kata.toLowerCase();
        return dan("jenis mengandung '" + kata + "'",
                p -> p.jenisNarkotika() != null
                        && p.jenisNarkotika().toLowerCase().contains(kunci));
    }

    public PutusanFilter beratGram(double min, double max) {
        return dan("berat " + min + "-" + max + " gram",
                p -> p.beratBarangBuktiGram() >= min && p.beratBarangBuktiGram() <= max);
    }

    public PutusanFilter pasalTerbukti(String pasal) {
        String kunci = pasal.toLowerCase();
        return dan("pasal mengandung '" + pasal + "'",
                p -> p.pasalTerbukti() != null
                        && p.pasalTerbukti().toLowerCase().contains(kunci));
    }

    public PutusanFilter vonisBulan(int min, int max) {
        return dan("vonis " + min + "-" + max + " bln",
                p -> p.vonisBulan() >= min && p.vonisBulan() <= max);
    }

    public DynamicArray<Putusan> hasil() {
        DynamicArray<Putusan> keluar = new DynamicArray<>();
        for (int i = 0; i < data.size(); i++) {
            Putusan p = data.get(i);
            if (syarat.test(p)) {
                keluar.add(p);
            }
        }
        String deskripsi = kriteria.length() == 0 ? "(tanpa kriteria)" : kriteria.toString();
        audit.catat(JenisOperasi.FILTER, "kriteria=" + deskripsi + ", hasil=" + keluar.size());
        return keluar;
    }

    private PutusanFilter dan(String deskripsi, Predicate<Putusan> tambahan) {
        syarat = syarat.and(tambahan);
        if (kriteria.length() > 0) {
            kriteria.append("; ");
        }
        kriteria.append(deskripsi);
        return this;
    }
}