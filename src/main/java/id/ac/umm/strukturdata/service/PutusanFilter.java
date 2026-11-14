package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.model.Putusan;
import java.util.function.Predicate;


public class PutusanFilter {

    private final StrukturData<Putusan> data;
    private Predicate<Putusan> syarat = p -> true;

    public PutusanFilter(StrukturData<Putusan> data) {
        this.data = data;
    }

    public PutusanFilter jenisNarkotika(String kata) {
        String kunci = kata.toLowerCase();
        return dan(p -> p.jenisNarkotika() != null
                && p.jenisNarkotika().toLowerCase().contains(kunci));
    }

    public PutusanFilter beratGram(double min, double max) {
        return dan(p -> p.beratBarangBuktiGram() >= min && p.beratBarangBuktiGram() <= max);
    }

    public PutusanFilter pasalTerbukti(String pasal) {
        String kunci = pasal.toLowerCase();
        return dan(p -> p.pasalTerbukti() != null
                && p.pasalTerbukti().toLowerCase().contains(kunci));
    }

    public PutusanFilter vonisBulan(int min, int max) {
        return dan(p -> p.vonisBulan() >= min && p.vonisBulan() <= max);
    }

    public DynamicArray<Putusan> hasil() {
        DynamicArray<Putusan> keluar = new DynamicArray<>();
        for (int i = 0; i < data.size(); i++) {
            Putusan p = data.get(i);
            if (syarat.test(p)) {
                keluar.add(p);
            }
        }
        return keluar;
    }

    private PutusanFilter dan(Predicate<Putusan> tambahan) {
        syarat = syarat.and(tambahan);
        return this;
    }
}