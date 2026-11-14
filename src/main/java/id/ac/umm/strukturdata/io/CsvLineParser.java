package id.ac.umm.strukturdata.io;

public final class CsvLineParser {
    private static final int BATAS_KOLOM = 64;

    private CsvLineParser() {
    }

    public static String[] parse(String baris) {
        String[] penampung = new String[BATAS_KOLOM];
        int jumlah = 0;
        StringBuilder kolom = new StringBuilder();
        boolean didalamKutip = false;

        for (int i = 0; i < baris.length(); i++) {
            char c = baris.charAt(i);
            if (didalamKutip) {
                if (c == '"') {
                    boolean kutipGanda = i + 1 < baris.length() && baris.charAt(i + 1) == '"';
                    if (kutipGanda) {
                        kolom.append('"');
                        i++;
                    } else {
                        didalamKutip = false;
                    }
                } else {
                    kolom.append(c);
                }
            } else if (c == '"') {
                didalamKutip = true;
            } else if (c == ',') {
                if (jumlah == BATAS_KOLOM) {
                    throw new IllegalArgumentException("kolom melebihi " + BATAS_KOLOM);
                }
                penampung[jumlah++] = kolom.toString();
                kolom.setLength(0);
            } else {
                kolom.append(c);
            }
        }
        if (didalamKutip) {
            throw new IllegalArgumentException("tanda kutip tidak ditutup");
        }
        if (jumlah == BATAS_KOLOM) {
            throw new IllegalArgumentException("kolom melebihi " + BATAS_KOLOM);
        }
        penampung[jumlah++] = kolom.toString();

        String[] hasil = new String[jumlah];
        System.arraycopy(penampung, 0, hasil, 0, jumlah);
        return hasil;
    }
}