package id.ac.umm.strukturdata.io;

import id.ac.umm.strukturdata.StrukturData;
import id.ac.umm.strukturdata.model.Putusan;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PutusanImporter {
    private int diterima;
    private int ditolak;

    public void impor(String lokasi, StrukturData<Putusan> tujuan) throws IOException {
        diterima = 0;
        ditolak = 0;
        try (BufferedReader pembaca = Files.newBufferedReader(Path.of(lokasi), StandardCharsets.UTF_8)) {
            pembaca.readLine();
            int nomorBaris = 1;
            String baris;
            while ((baris = pembaca.readLine()) != null) {
                nomorBaris++;
                if (baris.isBlank()) {
                    continue;
                }
                try {
                    tujuan.add(PutusanParser.dariBaris(baris));
                    diterima++;
                } catch (IllegalArgumentException e) {
                    ditolak++;
                    System.out.println("Baris " + nomorBaris + " ditolak: " + e.getMessage());
                }
            }
        }
    }

    public int getDiterima() { return diterima; }
    public int getDitolak() { return ditolak; }
}