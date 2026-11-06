package com.klinikpro.view.kit;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/** Panel dengan sudut membulat, dipakai untuk kartu, banner, dan kotak info. */
public class PanelBulat extends JPanel {

    private Color latar;
    private Color garis;
    private int tebalGaris = 1;
    private final int busur;

    public PanelBulat(Color latar, Color garis, int busur, int padding) {
        this(new BorderLayout(), latar, garis, busur, padding);
    }

    public PanelBulat(LayoutManager layout, Color latar, Color garis, int busur, int padding) {
        super(layout);
        this.latar = latar;
        this.garis = garis;
        this.busur = busur;
        setOpaque(false);
        setBorder(new EmptyBorder(padding, padding, padding, padding));
    }

    /** Kartu putih standar dengan garis tipis. */
    public static PanelBulat kartu(int padding) {
        return new PanelBulat(Tema.PERMUKAAN, Tema.GARIS, 16, padding);
    }

    public void setWarna(Color latar, Color garis, int tebalGaris) {
        this.latar = latar;
        this.garis = garis;
        this.tebalGaris = tebalGaris;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        if (latar != null) {
            g2.setColor(latar);
            g2.fillRoundRect(0, 0, w, h, busur, busur);
        }
        if (garis != null) {
            g2.setColor(garis);
            g2.setStroke(new BasicStroke(tebalGaris));
            float o = tebalGaris / 2f;
            g2.draw(new java.awt.geom.RoundRectangle2D.Float(o, o, w - tebalGaris, h - tebalGaris, busur, busur));
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
