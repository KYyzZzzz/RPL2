package com.klinikpro.view.kit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;

/** Ilustrasi kesehatan sederhana yang digambar dengan Java2D. */
public class Ilustrasi extends JComponent {

    public enum Jenis {
        /** Papan catatan medis, pil, dan garis detak jantung. Untuk banner. */
        KLINIK,
        /** Gelas air. Untuk kartu tips sehat. */
        GELAS,
        /** Kalender kosong. Untuk halaman yang belum ada datanya. */
        KOSONG
    }

    private final Jenis jenis;
    private final Color latar;

    public Ilustrasi(Jenis jenis, int lebar, int tinggi, Color latar) {
        this.jenis = jenis;
        this.latar = latar;
        setPreferredSize(new Dimension(lebar, tinggi));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double dasarW = jenis == Jenis.KLINIK ? 200 : 64;
        double dasarH = jenis == Jenis.KLINIK ? 140 : 64;
        double skala = Math.min(getWidth() / dasarW, getHeight() / dasarH);
        g2.translate((getWidth() - dasarW * skala) / 2, (getHeight() - dasarH * skala) / 2);
        g2.scale(skala, skala);
        switch (jenis) {
            case KLINIK -> lukisKlinik(g2);
            case GELAS -> lukisGelas(g2);
            default -> lukisKosong(g2);
        }
        g2.dispose();
    }

    private void lukisKlinik(Graphics2D g2) {
        g2.setColor(latar);
        g2.fillOval(56, 8, 128, 128);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(84, 28, 70, 92, 14, 14));
        g2.setColor(Tema.KREM);
        g2.fill(new RoundRectangle2D.Double(104, 20, 30, 16, 8, 8));
        g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(Tema.UTAMA);
        g2.drawLine(108, 62, 130, 62);
        g2.drawLine(119, 51, 119, 73);
        g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(Tema.GARIS_INPUT);
        g2.drawLine(96, 92, 142, 92);
        g2.drawLine(96, 104, 126, 104);
        g2.setColor(Tema.KREM);
        Path2D detak = new Path2D.Double();
        detak.moveTo(10, 92);
        detak.lineTo(32, 92);
        detak.lineTo(40, 76);
        detak.lineTo(50, 106);
        detak.lineTo(58, 92);
        detak.lineTo(80, 92);
        g2.draw(detak);
        Graphics2D pil = (Graphics2D) g2.create();
        pil.rotate(Math.toRadians(-35), 175, 98);
        pil.fill(new RoundRectangle2D.Double(156, 90, 38, 16, 16, 16));
        pil.dispose();
    }

    private void lukisGelas(Graphics2D g2) {
        g2.setColor(latar);
        g2.fillOval(2, 2, 60, 60);
        Path2D gelas = new Path2D.Double();
        gelas.moveTo(22, 16);
        gelas.lineTo(42, 16);
        gelas.lineTo(39, 50);
        gelas.lineTo(25, 50);
        gelas.closePath();
        g2.setColor(Color.WHITE);
        g2.fill(gelas);
        Path2D air = new Path2D.Double();
        air.moveTo(23.6, 30);
        air.lineTo(40.4, 30);
        air.lineTo(38.6, 48);
        air.lineTo(25.4, 48);
        air.closePath();
        g2.setColor(new Color(0x6FB7AE));
        g2.fill(air);
    }

    private void lukisKosong(Graphics2D g2) {
        g2.setColor(latar);
        g2.fillOval(2, 2, 60, 60);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(16, 18, 32, 28, 6, 6));
        g2.setColor(Tema.UTAMA);
        g2.fill(new RoundRectangle2D.Double(16, 18, 32, 8, 6, 6));
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(24, 14, 24, 20);
        g2.drawLine(40, 14, 40, 20);
        g2.setColor(Tema.GARIS_INPUT);
        g2.drawLine(22, 34, 42, 34);
        g2.drawLine(22, 40, 34, 40);
    }
}
