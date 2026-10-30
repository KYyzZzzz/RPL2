package com.klinikpro;

import javax.swing.SwingUtilities;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class KlinikProApplication {

    public static void main(String[] args) {
        System.out.println(">>> MAIN VERSI BARU DIMULAI");
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(KlinikProApplication.class)
                .headless(false)
                .run(args);
        DokterRepository dokterRepo = ctx.getBean(DokterRepository.class);
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println(">>> Membuka jendela KlinikPro...");
                MenuUtama menu = new MenuUtama(dokterRepo);
                menu.setVisible(true);
                menu.toFront();
                System.out.println(">>> Jendela KlinikPro terbuka.");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Bean
    CommandLineRunner tesKoneksi(JdbcTemplate jdbc) {
        return args -> {
            Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM dokter", Integer.class);
            System.out.println("Koneksi berhasil. Jumlah dokter: " + n);
        };
    }
}