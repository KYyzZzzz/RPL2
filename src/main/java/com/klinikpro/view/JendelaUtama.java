package com.klinikpro.view;

import com.klinikpro.controller.DokterController;
import com.klinikpro.view.kit.Tema;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * VIEW: jendela utama aplikasi.
 * Halaman Masuk dan Daftar akan ditambahkan di tahap berikutnya (M4).
 */
public class JendelaUtama extends JFrame {

    public JendelaUtama(DokterController dokterController) {
        super("KlinikPro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1280, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Tema.LATAR);
        setContentPane(new KerangkaAplikasi(this, dokterController));
    }
}
