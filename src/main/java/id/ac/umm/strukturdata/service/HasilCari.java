package id.ac.umm.strukturdata.service;
import id.ac.umm.strukturdata.model.Putusan;

public record HasilCari(Putusan putusan, int indeks, int langkah) {
    public boolean ditemukan() {
        return putusan != null;
    }
}