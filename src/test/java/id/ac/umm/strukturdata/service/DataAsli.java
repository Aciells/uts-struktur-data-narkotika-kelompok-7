package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.io.PutusanImporter;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Bantuan test: memuat data asli dan membaca urutan caseId tanpa mengubah struktur. */
final class DataAsli {

    private DataAsli() {
    }

    static DynamicArray<Putusan> muat() throws IOException {
        DynamicArray<Putusan> data = new DynamicArray<>();
        new PutusanImporter().impor("data/putusan-50.csv", data);
        return data;
    }

    static List<String> ids(StrukturData<Putusan> struktur) {
        List<String> id = new ArrayList<>();
        for (int i = 0; i < struktur.size(); i++) {
            id.add(struktur.get(i).caseId());
        }
        return id;
    }
}