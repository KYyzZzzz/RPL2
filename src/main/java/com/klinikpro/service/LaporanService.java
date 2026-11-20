package com.klinikpro.service;

import com.klinikpro.model.Pengguna;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;

/**
 * SERVICE: membuat laporan dengan JasperReports.
 * DataSource (koneksi database dari applicationContext.xml) disuntikkan oleh Spring.
 */
@Service
public class LaporanService {

    private static final String FILE_RIWAYAT_JANJI = "/reports/RiwayatJanji.jrxml";

    private final DataSource dataSource;
    private JasperReport riwayatJanji;   // hasil compile disimpan agar tidak compile ulang

    public LaporanService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** Laporan riwayat janji temu milik satu pasien. */
    public JasperPrint riwayatJanji(Pengguna pasien) throws Exception {
        // 1-2. Ambil file desain .jrxml lalu compile (cukup sekali)
        if (riwayatJanji == null) {
            try (InputStream desain = getClass().getResourceAsStream(FILE_RIWAYAT_JANJI)) {
                if (desain == null) {
                    throw new JRException("File laporan tidak ditemukan: " + FILE_RIWAYAT_JANJI);
                }
                riwayatJanji = JasperCompileManager.compileReport(desain);
            }
        }

        // 4. Parameter yang dikirim ke laporan
        Map<String, Object> param = new HashMap<>();
        param.put("PENGGUNA_ID", pasien.getId());
        param.put("NAMA_PASIEN", pasien.getNama());
        param.put("EMAIL_PASIEN", pasien.getEmail());
        param.put(JRParameter.REPORT_LOCALE, Locale.forLanguageTag("id-ID"));  // nama bulan bahasa Indonesia

        // 3 dan 5. Ambil koneksi dari Spring DataSource, lalu isi laporan dengan data dari database
        try (Connection conn = dataSource.getConnection()) {
            return JasperFillManager.fillReport(riwayatJanji, param, conn);
        }
    }
}
