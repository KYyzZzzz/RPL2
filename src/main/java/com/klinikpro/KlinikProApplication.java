package com.klinikpro;

import com.klinikpro.controller.AuthController;
import com.klinikpro.controller.DokterController;
import com.klinikpro.controller.JanjiController;
import com.klinikpro.view.JendelaUtama;
import com.klinikpro.view.kit.Tema;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource; // BARU

/**
 * Titik awal aplikasi: menyalakan Spring dan Hibernate (koneksi database),
 * lalu membuka View dengan Controller yang sudah siap.
 */
@SpringBootApplication
@ImportResource("classpath:applicationContext.xml") // BARU: membaca konfigurasi Spring dari XML
public class KlinikProApplication {

    public static void main(String[] args) {
        Tema.pasang();
        ConfigurableApplicationContext ctx;
        try {
            ctx = new SpringApplicationBuilder(KlinikProApplication.class)
                    .headless(false)
                    .run(args);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null,
                    "Aplikasi tidak bisa terhubung ke database.\n"
                    + "Pastikan MySQL di XAMPP sudah menyala dan database 'klinikpro' sudah dibuat.",
                    "KlinikPro", JOptionPane.ERROR_MESSAGE);
            throw ex;
        }

        AuthController authController = ctx.getBean(AuthController.class);
        DokterController dokterController = ctx.getBean(DokterController.class);
        JanjiController janjiController = ctx.getBean(JanjiController.class);
        SwingUtilities.invokeLater(() ->
                new JendelaUtama(authController, dokterController, janjiController).setVisible(true));
    }
}