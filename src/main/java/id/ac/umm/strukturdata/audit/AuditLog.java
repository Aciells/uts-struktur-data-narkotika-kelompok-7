package id.ac.umm.strukturdata.audit;

import id.ac.umm.strukturdata.Stack;
import java.time.LocalDateTime;

/** Merekam operasi pengguna ke Stack. Catatan terbaru ada di puncak. */
public class AuditLog {
    private final Stack<CatatanAudit> riwayat = new Stack<>();
    private int nomorTerakhir = 0;

    public CatatanAudit catat(JenisOperasi jenis, String detail) {
        if (jenis == null) {
            throw new IllegalArgumentException("jenis operasi wajib diisi");
        }
        nomorTerakhir++;
        CatatanAudit catatan = new CatatanAudit(
                nomorTerakhir, LocalDateTime.now(), jenis, detail == null ? "" : detail);
        riwayat.push(catatan);
        return catatan;
    }

    public int jumlah() {
        return riwayat.size();
    }

    public boolean kosong() {
        return riwayat.isEmpty();
    }

    /** Catatan terbaru. Melempar NoSuchElementException bila riwayat kosong. */
    public CatatanAudit terakhir() {
        return riwayat.peek();
    }

    /** Teks riwayat dari terbaru ke terlama. Isi dan urutan Stack tidak berubah setelahnya. */
    public String tampilkan() {
        if (riwayat.isEmpty()) {
            return "(riwayat kosong)";
        }
        Stack<CatatanAudit> bantu = new Stack<>();
        StringBuilder teks = new StringBuilder();
        try {
            while (!riwayat.isEmpty()) {
                CatatanAudit catatan = riwayat.pop();
                bantu.push(catatan);              // simpan dulu sebelum dipakai
                teks.append(catatan).append('\n');
            }
        } finally {
            while (!bantu.isEmpty()) {            // pulihkan urutan asli
                riwayat.push(bantu.pop());
            }
        }
        return teks.toString();
    }

    public void cetak() {
        System.out.print(tampilkan());
    }

    public boolean validateInvariant() {
        if (!riwayat.validateInvariant()) {
            return false;
        }
        if (riwayat.isEmpty()) {
            return nomorTerakhir == 0;
        }
        return riwayat.peek().nomor() == nomorTerakhir && riwayat.size() == nomorTerakhir;
    }
}