package id.ac.umm.strukturdata.service;

import java.util.Map;

public record RingkasanStatistik(
        int jumlahPerkara,
        double rataRataVonisBulan,
        long totalDendaRupiah,
        Map<String, Integer> distribusiJenis) {
}