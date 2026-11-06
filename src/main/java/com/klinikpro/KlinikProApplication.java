package com.klinikpro;

import com.klinikpro.controller.DokterController;
import com.klinikpro.view.JendelaUtama;
import com.klinikpro.view.kit.Tema;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Titik awal aplikasi: menyalakan Spring dan Hibernate (koneksi database),
 * lalu membuka View dengan Controller yang sudah siap.
 */
@SpringBootApplication
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

        DokterController dokterController = ctx.getBean(DokterController.class);
        SwingUtilities.invokeLater(() -> new JendelaUtama(dokterController).setVisible(true));
    }
}
