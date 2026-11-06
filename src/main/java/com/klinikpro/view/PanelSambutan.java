package com.klinikpro.view;

import com.klinikpro.view.kit.Ikon;
import com.klinikpro.view.kit.Ilustrasi;
import com.klinikpro.view.kit.PanelBulat;
import com.klinikpro.view.kit.Tema;
import com.klinikpro.view.kit.Ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/** VIEW: panel hijau di sisi kiri halaman Masuk dan Daftar. */
class PanelSambutan extends JPanel {

    PanelSambutan(String judul, String keterangan) {
        super(new BorderLayout());
        setBackground(Tema.UTAMA);
        setPreferredSize(new Dimension(560, 0));
        setBorder(new EmptyBorder(48, 52, 52, 52));

        PanelBulat logo = new PanelBulat(new GridBagLayout(), Color.WHITE, null, 12, 0);
        logo.setPreferredSize(new Dimension(40, 40));
        logo.add(new JLabel(new Ikon("plus", 22, Tema.UTAMA)));
        add(Ui.baris(12, logo, Ui.teks("KlinikPro", 24, Font.BOLD, Color.WHITE)), BorderLayout.NORTH);

        add(new Ilustrasi(Ilustrasi.Jenis.KLINIK, 420, 300, Tema.UTAMA_BANNER), BorderLayout.CENTER);

        add(Ui.kolom(12,
                Ui.teks("<html><div style='width:340px'>" + judul + "</div></html>", 28, Font.BOLD, Color.WHITE),
                Ui.paragraf(keterangan, 320, 16, Tema.TEKS_DI_UTAMA)), BorderLayout.SOUTH);
    }
}