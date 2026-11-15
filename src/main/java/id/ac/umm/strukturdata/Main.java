package id.ac.umm.strukturdata;

import id.ac.umm.strukturdata.audit.AuditLog;
import id.ac.umm.strukturdata.io.PutusanEksporter;
import id.ac.umm.strukturdata.io.PutusanImporter;
import id.ac.umm.strukturdata.io.PutusanParser;
import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.service.HasilCari;
import id.ac.umm.strukturdata.service.LaporanEksport;
import id.ac.umm.strukturdata.service.PutusanAggregator;
import id.ac.umm.strukturdata.service.PutusanFilter;
import id.ac.umm.strukturdata.service.PutusanManager;
import id.ac.umm.strukturdata.service.PutusanSearcher;
import id.ac.umm.strukturdata.service.PutusanSorter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;

/**
 * Titik masuk aplikasi: menu konsol yang menghubungkan data dengan semua fitur.
 * Satu AuditLog dipakai bersama oleh semua kelas, dan tiap kelas mencatat operasinya
 * sendiri, jadi Main tidak mencatat audit secara manual.
 */
public class Main {

    private static final String BERKAS = "data/putusan-50.csv";
    private static final String FOLDER_EKSPOR = "ekspor/";

    private final Scanner masukan = new Scanner(System.in);
    private final DynamicArray<Putusan> data = new DynamicArray<>();
    private final AuditLog audit = new AuditLog();

    private final PutusanManager manajer = new PutusanManager(data, audit);
    private final PutusanSearcher pencari = new PutusanSearcher(data, audit);
    private final PutusanSorter pengurut = new PutusanSorter(data, audit);
    private final PutusanEksporter eksporter = new PutusanEksporter(audit);
    private final LaporanEksport laporan = new LaporanEksport(eksporter);

    private HasilCari pencarianTerakhir;
    private DynamicArray<Putusan> filterTerakhir;

    public static void main(String[] args) {
        new Main().jalankan();
    }

    private void jalankan() {
        boolean lanjut = true;
        while (lanjut) {
            cetakMenu();
            if (!masukan.hasNextLine()) {
                break;
            }
            String pilihan = masukan.nextLine().trim();
            try {
                switch (pilihan) {
                    case "1" -> muat();
                    case "2" -> tampil();
                    case "3" -> tambah();
                    case "4" -> hapus();
                    case "5" -> cari();
                    case "6" -> saring();
                    case "7" -> System.out.print(
                            LaporanEksport.teksStatistik(PutusanAggregator.hitung(data)));
                    case "8" -> urut();
                    case "9" -> audit.cetak();
                    case "10" -> periksaInvariant();
                    case "11" -> ekspor();
                    case "0" -> lanjut = false;
                    default -> System.out.println("Pilihan tidak dikenal.");
                }
            } catch (NoSuchFileException e) {
                System.out.println("Berkas tidak ditemukan: " + e.getMessage()
                        + ". Jalankan dari folder utama proyek.");
            } catch (IOException | RuntimeException e) {
                System.out.println("Gagal: " + e.getMessage());
            }
            System.out.println();
        }
        System.out.println("Selesai.");
    }

    private static void cetakMenu() {
        System.out.println("=== Sistem Putusan Narkotika ===");
        System.out.println("1. Muat data (impor CSV)");
        System.out.println("2. Tampilkan semua putusan");
        System.out.println("3. Tambah putusan (satu baris CSV)");
        System.out.println("4. Hapus putusan (nomor urut)");
        System.out.println("5. Cari putusan");
        System.out.println("6. Filter");
        System.out.println("7. Ringkasan statistik");
        System.out.println("8. Urutkan berdasarkan vonis");
        System.out.println("9. Riwayat operasi (audit)");
        System.out.println("10. Periksa invariant struktur");
        System.out.println("11. Ekspor hasil ke berkas");
        System.out.println("0. Keluar");
        System.out.print("Pilih menu: ");
    }

    private void muat() throws IOException {
        data.clear();
        pencarianTerakhir = null;
        filterTerakhir = null;
        PutusanImporter importer = new PutusanImporter(audit);
        importer.impor(BERKAS, data);
        System.out.println("Diterima: " + importer.getDiterima()
                + ", ditolak: " + importer.getDitolak() + ", size: " + data.size());
    }

    private void tampil() {
        if (data.isEmpty()) {
            System.out.println("Data kosong. Pilih menu 1 untuk memuat data.");
            return;
        }
        System.out.print(manajer.ringkasan());
    }

    private void tambah() {
        System.out.println("Tempel satu baris CSV (16 kolom):");
        String baris = masukan.hasNextLine() ? masukan.nextLine() : "";
        try {
            Putusan p = PutusanParser.dariBaris(baris);
            manajer.tambah(p);
            System.out.println("Ditambahkan: " + p.caseId() + ", size sekarang " + data.size());
        } catch (IllegalArgumentException e) {
            System.out.println("Record ditolak: " + e.getMessage());
            System.out.println("Struktur tidak berubah, size tetap " + data.size());
        }
    }

    private void hapus() {
        if (data.isEmpty()) {
            System.out.println("Data kosong. Pilih menu 1 untuk memuat data.");
            return;
        }
        int nomor = tanyaBulat("Nomor urut yang dihapus (1-" + data.size() + ")");
        if (nomor < 1 || nomor > data.size()) {
            throw new IllegalArgumentException("nomor harus antara 1 dan " + data.size());
        }
        Putusan dihapus = manajer.hapus(nomor - 1);
        System.out.println("Dihapus: " + dihapus.caseId() + ", size sekarang " + data.size());
    }

    private void cari() {
        String jenis = tanya("Cari berdasarkan (1) caseId atau (2) nomorPerkara");
        if (!jenis.equals("1") && !jenis.equals("2")) {
            throw new IllegalArgumentException("pilih 1 atau 2");
        }
        String kunci = tanya("Kata kunci");
        HasilCari hasil = jenis.equals("2")
                ? pencari.cariByNomorPerkara(kunci) : pencari.cariByCaseId(kunci);
        pencarianTerakhir = hasil;
        if (hasil.ditemukan()) {
            System.out.println("Ditemukan di indeks " + hasil.indeks() + " setelah "
                    + hasil.langkah() + " langkah: " + hasil.putusan().caseId()
                    + " | " + hasil.putusan().nomorPerkara());
        } else {
            System.out.println("Tidak ditemukan setelah " + hasil.langkah() + " langkah.");
        }
    }

    private void saring() {
        PutusanFilter filter = new PutusanFilter(data, audit);

        String jenis = tanya("Jenis narkotika mengandung (kosongkan untuk lewati)");
        if (!jenis.isEmpty()) {
            filter.jenisNarkotika(jenis);
        }
        String berat = tanya("Rentang berat gram, contoh 1-10 (kosongkan untuk lewati)");
        if (!berat.isEmpty()) {
            String[] r = pecahRentang(berat);
            filter.beratGram(bacaDesimal(r[0]), bacaDesimal(r[1]));
        }
        String pasal = tanya("Pasal terbukti mengandung (kosongkan untuk lewati)");
        if (!pasal.isEmpty()) {
            filter.pasalTerbukti(pasal);
        }
        String vonis = tanya("Rentang vonis bulan, contoh 60-120 (kosongkan untuk lewati)");
        if (!vonis.isEmpty()) {
            String[] r = pecahRentang(vonis);
            filter.vonisBulan(bacaBulat(r[0]), bacaBulat(r[1]));
        }

        DynamicArray<Putusan> hasil = filter.hasil();
        filterTerakhir = hasil;
        System.out.println("Hasil filter: " + hasil.size() + " perkara");
        System.out.print(new PutusanManager(hasil).ringkasan());
        if (!hasil.isEmpty()) {
            System.out.print(LaporanEksport.teksStatistik(PutusanAggregator.hitung(hasil)));
        }
    }

    private void urut() {
        boolean naik = !tanya("Urutan (naik/turun)").equalsIgnoreCase("turun");
        System.out.print(pengurut.ringkasanTerurut(naik));
        System.out.println("Jumlah perbandingan: " + pengurut.getLangkah());
        System.out.println("Urutan data utama tidak berubah (pengurutan memakai salinan).");
    }

    private void periksaInvariant() {
        try {
            data.validateInvariant();
            System.out.println("DynamicArray valid: size=" + data.size()
                    + ", kapasitas=" + data.capacity());
        } catch (IllegalStateException e) {
            System.out.println("DynamicArray TIDAK valid: " + e.getMessage());
        }
        System.out.println("Stack audit valid : " + audit.validateInvariant()
                + " (" + audit.jumlah() + " catatan)");
    }

    private void ekspor() throws IOException {
        String pilihan = tanya("Ekspor (1) semua data CSV, (2) hasil filter terakhir CSV, "
                + "(3) laporan statistik, (4) hasil pencarian terakhir");
        switch (pilihan) {
            case "1" -> {
                int n = eksporter.eksporCsv(data, FOLDER_EKSPOR + "semua-putusan.csv");
                System.out.println("Tersimpan: " + FOLDER_EKSPOR + "semua-putusan.csv (" + n + " baris)");
            }
            case "2" -> {
                if (filterTerakhir == null) {
                    System.out.println("Belum ada hasil filter. Jalankan menu 6 dulu.");
                    return;
                }
                int n = eksporter.eksporCsv(filterTerakhir, FOLDER_EKSPOR + "hasil-filter.csv");
                System.out.println("Tersimpan: " + FOLDER_EKSPOR + "hasil-filter.csv (" + n + " baris)");
            }
            case "3" -> {
                laporan.eksporStatistik(PutusanAggregator.hitung(data), FOLDER_EKSPOR + "statistik.txt");
                System.out.println("Tersimpan: " + FOLDER_EKSPOR + "statistik.txt");
            }
            case "4" -> {
                if (pencarianTerakhir == null) {
                    System.out.println("Belum ada pencarian. Jalankan menu 5 dulu.");
                    return;
                }
                laporan.eksporHasilCari(pencarianTerakhir, FOLDER_EKSPOR + "hasil-cari.csv");
                laporan.eksporLaporanCari(pencarianTerakhir, FOLDER_EKSPOR + "laporan-cari.txt");
                System.out.println("Tersimpan: " + FOLDER_EKSPOR + "hasil-cari.csv dan laporan-cari.txt");
            }
            default -> System.out.println("Pilihan ekspor tidak dikenal.");
        }
    }

    private String tanya(String pertanyaan) {
        System.out.print(pertanyaan + ": ");
        return masukan.hasNextLine() ? masukan.nextLine().trim() : "";
    }

    private int tanyaBulat(String pertanyaan) {
        return bacaBulat(tanya(pertanyaan));
    }

    private static int bacaBulat(String teks) {
        try {
            return Integer.parseInt(teks.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + teks + "' bukan bilangan bulat");
        }
    }

    private static double bacaDesimal(String teks) {
        try {
            return Double.parseDouble(teks.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + teks + "' bukan angka");
        }
    }

    private static String[] pecahRentang(String teks) {
        String[] bagian = teks.split("-");
        if (bagian.length != 2) {
            throw new IllegalArgumentException("format rentang harus min-maks, contoh 1-10");
        }
        return new String[] {bagian[0].trim(), bagian[1].trim()};
    }
}