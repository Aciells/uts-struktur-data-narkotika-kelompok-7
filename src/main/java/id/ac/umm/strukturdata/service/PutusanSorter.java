package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.model.Putusan;

public class PutusanSorter {
    private final StrukturData<Putusan> data;
    private int langkah;
    public PutusanSorter(StrukturData<Putusan> data) {
        this.data = data;
    }

    public int getLangkah() {
        return langkah;
    }

    public DynamicArray<Putusan> urutkanByVonis(boolean naik) {
        DynamicArray<Putusan> salinan = new DynamicArray<>();
        for (int i = 0; i < data.size(); i++) {
            salinan.add(data.get(i));
        }

        langkah = 0;
        for (int i = 1; i < salinan.size(); i++) {
            Putusan kunci = salinan.get(i);
            int j = i - 1;
            while (j >= 0 && harusSebelum(kunci, salinan.get(j), naik)) {
                salinan.set(j + 1, salinan.get(j));
                j--;
            }
            salinan.set(j + 1, kunci);
        }
        return salinan;
    }
    public String ringkasanTerurut(boolean naik) {
        DynamicArray<Putusan> terurut = urutkanByVonis(naik);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < terurut.size(); i++) {
            Putusan p = terurut.get(i);
            sb.append(i + 1).append(". ").append(p.caseId()).append(" | vonis ")
                    .append(p.vonisBulan() > 0 ? p.vonisBulan() + " bln" : "(kosong)")
                    .append('\n');
        }
        return sb.toString();
    }

    private boolean harusSebelum(Putusan a, Putusan b, boolean naik) {
        langkah++;
        boolean aKosong = a.vonisBulan() <= 0;
        boolean bKosong = b.vonisBulan() <= 0;
        if (aKosong || bKosong) {
            return !aKosong && bKosong;
        }
        return naik ? a.vonisBulan() < b.vonisBulan() : a.vonisBulan() > b.vonisBulan();
    }
}