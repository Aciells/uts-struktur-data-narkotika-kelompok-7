package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.DynamicArray;
import id.ac.umm.strukturdata.io.PutusanEksporter;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class LaporanEksport {
    private final PutusanEksporter eksporter;

    public LaporanEksport(PutusanEksporter eksporter) {
        if (eksporter == null) {
            throw new IllegalArgumentException("eksporter wajib diisi");
        }
        this.eksporter = eksporter;
    }

    public int eksporHasilCari(HasilCari hasil, String lokasi) throws IOException {
        if (hasil == null) {
            throw new IllegalArgumentException("hasil pencarian wajib diisi");
        }
        DynamicArray<Putusan> isi = new DynamicArray<>();
        if (hasil.ditemukan()) {
            isi.add(hasil.putusan());
        }
        return eksporter.eksporCsv(isi, lokasi);
    }

    public void eksporLaporanCari(HasilCari hasil, String lokasi) throws IOException {
        if (hasil == null) {
            throw new IllegalArgumentException("hasil pencarian wajib diisi");
        }
        eksporter.eksporTeks(teksPencarian(hasil), lokasi);
    }

    public void eksporStatistik(RingkasanStatistik ringkasan, String lokasi) throws IOException {
        if (ringkasan == null) {
            throw new IllegalArgumentException("ringkasan statistik wajib diisi");
        }
        eksporter.eksporTeks(teksStatistik(ringkasan), lokasi);
    }

    public static String teksPencarian(HasilCari hasil) {
        StringBuilder sb = new StringBuilder();
        sb.append("LAPORAN PENCARIAN\n");
        sb.append("Ditemukan        : ").append(hasil.ditemukan() ? "ya" : "tidak").append('\n');
        sb.append("Jumlah langkah   : ").append(hasil.langkah()).append('\n');
        if (hasil.ditemukan()) {
            sb.append("Posisi (indeks)  : ").append(hasil.indeks()).append('\n');
            sb.append("caseId           : ").append(hasil.putusan().caseId()).append('\n');
            sb.append("Nomor perkara    : ").append(hasil.putusan().nomorPerkara()).append('\n');
        }
        return sb.toString();
    }

    public static String teksStatistik(RingkasanStatistik ringkasan) {
        StringBuilder sb = new StringBuilder();
        sb.append("LAPORAN STATISTIK PUTUSAN\n");
        sb.append("Jumlah perkara   : ").append(ringkasan.jumlahPerkara()).append('\n');
        sb.append("Rata-rata vonis  : ")
                .append(String.format(Locale.ROOT, "%.2f", ringkasan.rataRataVonisBulan()))
                .append(" bulan (hanya dari putusan yang memiliki vonis)\n");
        sb.append("Total denda      : Rp").append(ringkasan.totalDendaRupiah()).append('\n');
        sb.append("Distribusi jenis narkotika:\n");
        for (Map.Entry<String, Integer> jenis : ringkasan.distribusiJenis().entrySet()) {
            sb.append("- ").append(jenis.getKey()).append(": ").append(jenis.getValue()).append('\n');
        }
        return sb.toString();
    }
}