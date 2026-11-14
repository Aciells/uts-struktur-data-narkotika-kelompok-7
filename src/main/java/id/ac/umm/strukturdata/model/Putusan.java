package id.ac.umm.strukturdata.model;

import java.time.LocalDate;
public record Putusan(
        String caseId,
        String nomorPerkara,
        String pengadilan,
        LocalDate tanggalPutusan,
        String terdakwaKode,
        String jenisNarkotika,
        double beratBarangBuktiGram,
        String pasalDakwaan,
        String pasalTerbukti,
        int tuntutanBulan,
        int vonisBulan,
        long dendaRupiah,
        String barangBukti,
        String keadaanMemberatkan,
        String keadaanMeringankan,
        String sourcePages) {
}