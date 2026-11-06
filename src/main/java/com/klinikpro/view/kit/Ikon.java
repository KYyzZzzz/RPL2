package com.klinikpro.view.kit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;

/** Ikon garis sederhana yang digambar dengan Java2D (tidak butuh file gambar). */
public class Ikon implements Icon {

    private final String nama;
    private final int ukuran;
    private final Color warna;

    public Ikon(String nama, int ukuran, Color warna) {
        this.nama = nama;
        this.ukuran = ukuran;
        this.warna = warna;
    }

    @Override
    public int getIconWidth() { return ukuran; }

    @Override
    public int getIconHeight() { return ukuran; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.translate(x, y);
        g2.scale(ukuran / 24.0, ukuran / 24.0);
        g2.setColor(warna);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        switch (nama) {
            case "beranda" -> {
                g2.draw(jalur(3, 11, 12, 4, 21, 11, 21, 20, 15, 20, 15, 14, 9, 14, 9, 20, 3, 20, 3, 11));
            }
            case "cari" -> {
                g2.draw(new Ellipse2D.Double(4, 4, 14, 14));
                g2.draw(new Line2D.Double(16, 16, 20, 20));
            }
            case "kalender" -> {
                g2.draw(new RoundRectangle2D.Double(3, 5, 18, 16, 4, 4));
                g2.draw(new Line2D.Double(3, 10, 21, 10));
                g2.draw(new Line2D.Double(8, 3, 8, 7));
                g2.draw(new Line2D.Double(16, 3, 16, 7));
            }
            case "dokumen" -> {
                g2.draw(new RoundRectangle2D.Double(6, 3, 12, 18, 3, 3));
                g2.draw(new Line2D.Double(9.5, 8, 14.5, 8));
                g2.draw(new Line2D.Double(9.5, 12, 14.5, 12));
                g2.draw(new Line2D.Double(9.5, 16, 12, 16));
            }
            case "obat" -> {
                Graphics2D r = (Graphics2D) g2.create();
                r.rotate(Math.toRadians(-45), 12, 12);
                r.draw(new RoundRectangle2D.Double(3, 9, 18, 6, 6, 6));
                r.draw(new Line2D.Double(12, 9, 12, 15));
                r.dispose();
            }
            case "kartu" -> {
                g2.draw(new RoundRectangle2D.Double(3, 6, 18, 13, 4, 4));
                g2.draw(new Line2D.Double(3, 10, 21, 10));
                g2.draw(new Line2D.Double(7, 15, 10, 15));
            }
            case "buku" -> {
                g2.draw(jalur(5, 4, 19, 4, 19, 18, 7, 18));
                g2.draw(new RoundRectangle2D.Double(5, 16, 14, 4, 3, 3));
                g2.draw(new Line2D.Double(5, 4, 5, 18));
                g2.draw(new Line2D.Double(9, 8, 15, 8));
            }
            case "profil" -> {
                g2.draw(new Ellipse2D.Double(8, 4, 8, 8));
                Path2D p = new Path2D.Double();
                p.moveTo(4, 21);
                p.curveTo(5, 17, 8, 15, 12, 15);
                p.curveTo(16, 15, 19, 17, 20, 21);
                g2.draw(p);
            }
            case "keluar" -> {
                g2.draw(jalur(15, 4, 19, 4, 19, 20, 15, 20));
                g2.draw(jalur(10, 8, 6, 12, 10, 16));
                g2.draw(new Line2D.Double(6, 12, 16, 12));
            }
            case "lonceng" -> {
                Path2D p = new Path2D.Double();
                p.moveTo(6, 16);
                p.lineTo(6, 11);
                p.curveTo(6, 7.5, 8.7, 5, 12, 5);
                p.curveTo(15.3, 5, 18, 7.5, 18, 11);
                p.lineTo(18, 16);
                p.lineTo(20, 18);
                p.lineTo(4, 18);
                p.closePath();
                g2.draw(p);
                g2.draw(new Line2D.Double(10, 21, 14, 21));
            }
            case "plus" -> {
                g2.draw(new Line2D.Double(12, 5, 12, 19));
                g2.draw(new Line2D.Double(5, 12, 19, 12));
            }
            case "perisai" -> {
                g2.draw(jalur(12, 3, 20, 6, 20, 12));
                Path2D p = new Path2D.Double();
                p.moveTo(20, 12);
                p.curveTo(20, 16.5, 16.6, 20, 12, 21);
                p.curveTo(7.4, 20, 4, 16.5, 4, 12);
                p.lineTo(4, 6);
                p.lineTo(12, 3);
                g2.draw(p);
                g2.draw(jalur(9, 12, 11, 14, 15, 10));
            }
            case "mata" -> {
                Path2D p = new Path2D.Double();
                p.moveTo(2, 12);
                p.curveTo(5, 6, 19, 6, 22, 12);
                p.curveTo(19, 18, 5, 18, 2, 12);
                g2.draw(p);
                g2.draw(new Ellipse2D.Double(9, 9, 6, 6));
            }
            default -> g2.draw(new Ellipse2D.Double(4, 4, 16, 16));
        }
        g2.dispose();
    }

    private static Path2D jalur(double... titik) {
        Path2D p = new Path2D.Double();
        p.moveTo(titik[0], titik[1]);
        for (int i = 2; i < titik.length; i += 2) {
            p.lineTo(titik[i], titik[i + 1]);
        }
        return p;
    }
}
