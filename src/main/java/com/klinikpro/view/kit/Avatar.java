package com.klinikpro.view.kit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/** Lingkaran berisi inisial nama, pengganti foto profil. */
public class Avatar extends JComponent {

    /** Pasangan warna latar dan teks yang dipakai bergiliran. */
    private static final Color[][] PALET = {
        {Tema.UTAMA_MUDA, Tema.UTAMA_GELAP},
        {Tema.AKSEN_MUDA, Tema.AKSEN_TEKS},
        {Tema.BIRU_MUDA, Tema.BIRU},
        {Tema.UNGU_MUDA, Tema.UNGU},
    };

    private final String inisial;
    private final int ukuran;
    private final Color latar;
    private final Color teks;

    public Avatar(String inisial, int ukuran, int indeksWarna) {
        this(inisial, ukuran, PALET[Math.floorMod(indeksWarna, PALET.length)][0],
                PALET[Math.floorMod(indeksWarna, PALET.length)][1]);
    }

    public Avatar(String inisial, int ukuran, Color latar, Color teks) {
        this.inisial = inisial;
        this.ukuran = ukuran;
        this.latar = latar;
        this.teks = teks;
        Dimension d = new Dimension(ukuran, ukuran);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(latar);
        g2.fillOval(0, 0, ukuran, ukuran);
        g2.setColor(teks);
        g2.setFont(Tema.huruf(Font.BOLD, ukuran * 0.34f));
        FontMetrics fm = g2.getFontMetrics();
        int x = (ukuran - fm.stringWidth(inisial)) / 2;
        int y = (ukuran - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(inisial, x, y);
        g2.dispose();
    }
}
