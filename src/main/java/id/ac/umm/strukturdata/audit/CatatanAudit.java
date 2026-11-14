package id.ac.umm.strukturdata.audit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record CatatanAudit(int nomor, LocalDateTime waktu, JenisOperasi jenis, String detail) {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String toString() {
        return "#" + nomor + " [" + waktu.format(FORMAT) + "] " + jenis + ": " + detail;
    }
}