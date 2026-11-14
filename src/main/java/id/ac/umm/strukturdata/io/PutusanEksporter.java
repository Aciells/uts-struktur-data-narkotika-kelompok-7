package id.ac.umm.strukturdata.io;

import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.audit.AuditLog;
import id.ac.umm.strukturdata.audit.JenisOperasi;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PutusanEksporter {
    public static final String HEADER = "caseId,nomorPerkara,pengadilan,tanggalPutusan,terdakwaKode,"
            + "jenisNarkotika,beratBarangBuktiGram,pasalDakwaan,pasalTerbukti,tuntutanBulan,"
            + "vonisBulan,dendaRupiah,barangBukti,keadaanMemberatkan,keadaanMeringankan,sourcePages";

    private final AuditLog audit;

    public PutusanEksporter() {
        this(new AuditLog());
    }

    public PutusanEksporter(AuditLog audit) {
        this.audit = audit;
    }

    public int eksporCsv(StrukturData<Putusan> data, String lokasi) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("data wajib diisi");
        }
        Path tujuan = siapkan(lokasi);
        try (BufferedWriter tulis = Files.newBufferedWriter(tujuan, StandardCharsets.UTF_8)) {
            tulis.write(HEADER);
            tulis.write('\n');
            for (int i = 0; i < data.size(); i++) {
                tulis.write(baris(data.get(i)));
                tulis.write('\n');
            }
        }
        audit.catat(JenisOperasi.EKSPOR,
                "csv=" + tujuan.getFileName() + ", baris=" + data.size());
        return data.size();
    }

    public void eksporTeks(String isi, String lokasi) throws IOException {
        if (isi == null) {
            throw new IllegalArgumentException("isi laporan wajib diisi");
        }
        Path tujuan = siapkan(lokasi);
        Files.writeString(tujuan, isi, StandardCharsets.UTF_8);
        audit.catat(JenisOperasi.EKSPOR,
                "teks=" + tujuan.getFileName() + ", karakter=" + isi.length());
    }

    private static Path siapkan(String lokasi) throws IOException {
        if (lokasi == null || lokasi.isBlank()) {
            throw new IllegalArgumentException("lokasi berkas wajib diisi");
        }
        Path tujuan = Path.of(lokasi);
        Path induk = tujuan.toAbsolutePath().getParent();
        if (induk != null) {
            Files.createDirectories(induk);
        }
        return tujuan;
    }

    private static String baris(Putusan p) {
        StringBuilder sb = new StringBuilder();
        sb.append(kolom(p.caseId())).append(',')
                .append(kolom(p.nomorPerkara())).append(',')
                .append(kolom(p.pengadilan())).append(',')
                .append(p.tanggalPutusan() == null ? "" : p.tanggalPutusan().toString()).append(',')
                .append(kolom(p.terdakwaKode())).append(',')
                .append(kolom(p.jenisNarkotika())).append(',')
                .append(BigDecimal.valueOf(p.beratBarangBuktiGram()).toPlainString()).append(',')
                .append(kolom(p.pasalDakwaan())).append(',')
                .append(kolom(p.pasalTerbukti())).append(',')
                .append(p.tuntutanBulan()).append(',')
                .append(p.vonisBulan()).append(',')
                .append(p.dendaRupiah()).append(',')
                .append(kolom(p.barangBukti())).append(',')
                .append(kolom(p.keadaanMemberatkan())).append(',')
                .append(kolom(p.keadaanMeringankan())).append(',')
                .append(kolom(p.sourcePages()));
        return sb.toString();
    }

    private static String kolom(String nilai) {
        if (nilai == null) {
            return "";
        }
        String bersih = nilai.replace('\r', ' ').replace('\n', ' ');
        boolean perluKutip = bersih.indexOf(',') >= 0 || bersih.indexOf('"') >= 0;
        if (!perluKutip) {
            return bersih;
        }
        return "\"" + bersih.replace("\"", "\"\"") + "\"";
    }
}