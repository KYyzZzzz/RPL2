package com.klinikpro.view.kit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Icon;

/** Ikon garis di dalam kotak berwarna, dipakai di menu cepat Beranda. */
public class IkonKotak implements Icon {

    private final Ikon ikon;
    private final int ukuran;
    private final Color latar;

    public IkonKotak(String nama, int ukuran, Color latar, Color warna) {
        this.ikon = new Ikon(nama, ukuran / 2, warna);
        this.ukuran = ukuran;
        this.latar = latar;
    }

    @Override
    public int getIconWidth() { return ukuran; }

    @Override
    public int getIconHeight() { return ukuran; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(latar);
        g2.fillRoundRect(x, y, ukuran, ukuran, 14, 14);
        g2.dispose();
        ikon.paintIcon(c, g, x + ukuran / 4, y + ukuran / 4);
    }
}
