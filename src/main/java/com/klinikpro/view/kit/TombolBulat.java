package com.klinikpro.view.kit;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/** Tombol membulat dengan beberapa gaya warna. */
public class TombolBulat extends JButton {

    public enum Gaya {
        UTAMA, GARIS, PUTIH, MENU, MENU_AKTIF, MENU_BAHAYA, CHIP, CHIP_AKTIF, TAUTAN, UBIN
    }

    /** Tombol kotak berisi ikon di atas dan teks di bawah, untuk menu cepat Beranda. */
    public static TombolBulat ubin(String teks, String namaIkon, Color latarIkon, Color warnaIkon) {
        TombolBulat t = new TombolBulat(teks, TombolBulat.Gaya.UBIN);
        t.setIcon(new IkonKotak(namaIkon, 44, latarIkon, warnaIkon));
        t.setVerticalTextPosition(SwingConstants.BOTTOM);
        t.setHorizontalTextPosition(SwingConstants.CENTER);
        t.setIconTextGap(10);
        return t;
    }

    private Color latar;
    private Color latarHover;
    private Color garis;
    private String namaIkon;
    private int busur = 12;

    public TombolBulat(String teks, Gaya gaya) {
        this(teks, null, gaya);
    }

    public TombolBulat(String teks, String namaIkon, Gaya gaya) {
        super(teks);
        this.namaIkon = namaIkon;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setIconTextGap(12);
        setGaya(gaya);
    }

    public final void setGaya(Gaya gaya) {
        Color teks;
        int tinggi = 44;
        Font font = Tema.huruf(Font.BOLD, 14);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(new EmptyBorder(0, 18, 0, 18));
        busur = 12;
        switch (gaya) {
            case UTAMA -> {
                latar = Tema.UTAMA; latarHover = Tema.UTAMA_HOVER; garis = null; teks = Color.WHITE;
            }
            case GARIS -> {
                latar = Tema.PERMUKAAN; latarHover = Tema.UTAMA_MUDA; garis = Tema.UTAMA; teks = Tema.UTAMA;
            }
            case PUTIH -> {
                latar = Color.WHITE; latarHover = Tema.UTAMA_MUDA; garis = null; teks = Tema.UTAMA_GELAP;
            }
            case MENU, MENU_AKTIF, MENU_BAHAYA -> {
                boolean aktif = gaya == Gaya.MENU_AKTIF;
                latar = aktif ? Tema.UTAMA_MUDA : null;
                latarHover = aktif ? Tema.UTAMA_MUDA : Tema.LATAR;
                garis = null;
                teks = gaya == Gaya.MENU_BAHAYA ? Tema.BAHAYA : aktif ? Tema.UTAMA_GELAP : Tema.TEKS_SEDANG;
                font = Tema.huruf(aktif ? Font.BOLD : Font.PLAIN, 15);
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                tinggi = 46;
                busur = 10;
            }
            case CHIP, CHIP_AKTIF -> {
                boolean aktif = gaya == Gaya.CHIP_AKTIF;
                latar = aktif ? Tema.UTAMA : Tema.PERMUKAAN;
                latarHover = aktif ? Tema.UTAMA_HOVER : Tema.UTAMA_MUDA;
                garis = aktif ? null : Tema.GARIS_INPUT;
                teks = aktif ? Color.WHITE : Tema.TEKS;
                font = Tema.huruf(aktif ? Font.BOLD : Font.PLAIN, 14);
                setBorder(new EmptyBorder(0, 16, 0, 16));
                tinggi = 38;
                busur = 38;
            }
            case UBIN -> {
                latar = Tema.PERMUKAAN; latarHover = Tema.UTAMA_MUDA; garis = Tema.GARIS; teks = Tema.TEKS;
                font = Tema.huruf(Font.BOLD, 13);
                setBorder(new EmptyBorder(14, 6, 14, 6));
                tinggi = 104;
                busur = 14;
            }
            default -> { // TAUTAN
                latar = null; latarHover = null; garis = null; teks = Tema.UTAMA;
                setBorder(new EmptyBorder(0, 2, 0, 2));
                tinggi = 30;
            }
        }
        setForeground(teks);
        setFont(font);
        if (namaIkon != null) {
            setIcon(new Ikon(namaIkon, 20, teks));
        }
        setPreferredSize(null);
        Dimension d = super.getPreferredSize();
        setPreferredSize(new Dimension(d.width, tinggi));
        repaint();
    }

    /** Jangan sampai tombol dipipihkan layout saat ruangnya sempit. */
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(super.getMinimumSize().width, getPreferredSize().height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color isi = getModel().isRollover() && isEnabled() && latarHover != null ? latarHover : latar;
        if (isi != null) {
            g2.setColor(isEnabled() ? isi : new Color(0xF1F4F3));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), busur, busur);
        }
        if (garis != null) {
            g2.setColor(garis);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, busur, busur);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
