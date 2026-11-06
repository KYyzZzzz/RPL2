package com.klinikpro.view.kit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

/** Fungsi bantu untuk membuat label, susunan, dan format angka. */
public final class Ui {

    public static final Locale INDONESIA = Locale.forLanguageTag("id-ID");

    private Ui() {
    }

    // ---------- Label ----------
    public static JLabel teks(String isi, float ukuran, int gaya, Color warna) {
        JLabel l = new JLabel(isi);
        l.setFont(Tema.huruf(gaya, ukuran));
        l.setForeground(warna);
        return l;
    }

    public static JLabel judul(String isi, float ukuran) {
        return teks(isi, ukuran, Font.BOLD, Tema.TEKS);
    }

    public static JLabel pudar(String isi, float ukuran) {
        return teks(isi, ukuran, Font.PLAIN, Tema.TEKS_PUDAR);
    }

    /** Label yang otomatis turun baris pada lebar tertentu. */
    public static JLabel paragraf(String isi, int lebar, float ukuran, Color warna) {
        return paragraf(isi, lebar, ukuran, warna, "left");
    }

    public static JLabel paragraf(String isi, int lebar, float ukuran, Color warna, String rata) {
        String aman = isi.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        return teks("<html><div style='width:" + lebar + "px;text-align:" + rata + "'>" + aman + "</div></html>",
                ukuran, Font.PLAIN, warna);
    }

    /** Label kecil berbentuk pil, misalnya status "Lunas" atau hari praktik. */
    public static JComponent lencana(String isi, Color latar, Color warna) {
        PanelBulat p = new PanelBulat(new FlowLayout(FlowLayout.CENTER, 0, 0), latar, null, 22, 0);
        p.setBorder(new EmptyBorder(3, 10, 3, 10));
        p.add(teks(isi, 12, Font.BOLD, warna));
        return p;
    }

    // ---------- Susunan ----------
    /** Susun komponen dari atas ke bawah, lebar penuh, rata atas. */
    public static JPanel kolom(int jarak, Component... isi) {
        JPanel p = transparan(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.NORTHWEST;
        for (int i = 0; i < isi.length; i++) {
            c.gridy = i;
            c.insets = new Insets(i == 0 ? 0 : jarak, 0, 0, 0);
            p.add(isi[i], c);
        }
        c.gridy = isi.length;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 0);
        p.add(transparan(null), c);
        return p;
    }

    /** Susun komponen dari kiri ke kanan dengan ukuran aslinya, rata tengah vertikal. */
    public static JPanel baris(int jarak, Component... isi) {
        JPanel p = transparan(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        for (int i = 0; i < isi.length; i++) {
            c.gridx = i;
            c.insets = new Insets(0, i == 0 ? 0 : jarak, 0, 0);
            p.add(isi[i], c);
        }
        c.gridx = isi.length;
        c.weightx = 1;
        p.add(transparan(null), c);
        return p;
    }

    /** Grid dengan jumlah kolom tetap dan ukuran sel sama besar. */
    public static JPanel grid(int kolom, int jarak, Component... isi) {
        JPanel p = transparan(new GridLayout(0, kolom, jarak, jarak));
        for (Component c : isi) {
            p.add(c);
        }
        return p;
    }

    /** Komponen kiri mengambil sisa ruang, komponen kanan memakai ukuran aslinya. */
    public static JPanel kiriKanan(Component kiri, Component kanan, int jarak) {
        JPanel p = transparan(new BorderLayout(jarak, 0));
        if (kiri != null) p.add(kiri, BorderLayout.CENTER);
        if (kanan != null) p.add(kanan, BorderLayout.EAST);
        return p;
    }

    public static JPanel transparan(java.awt.LayoutManager layout) {
        JPanel p = layout == null ? new JPanel() : new JPanel(layout);
        p.setOpaque(false);
        return p;
    }

    public static JScrollPane gulir(JComponent isi) {
        JScrollPane s = new JScrollPane(isi);
        s.setBorder(BorderFactory.createEmptyBorder());
        s.setOpaque(false);
        s.getViewport().setOpaque(false);
        s.getVerticalScrollBar().setUnitIncrement(18);
        s.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return s;
    }

    // ---------- Format ----------
    public static String rupiah(BigDecimal nilai) {
        NumberFormat nf = NumberFormat.getIntegerInstance(INDONESIA);
        return "Rp " + nf.format(nilai);
    }

    /** Kotak ikon berwarna, dipakai di menu cepat dan daftar obat. */
    public static JComponent kotakIkon(String namaIkon, int ukuran, Color latar, Color warna) {
        PanelBulat p = new PanelBulat(new GridBagLayout(), latar, null, 14, 0);
        p.setPreferredSize(new java.awt.Dimension(ukuran, ukuran));
        p.setMinimumSize(p.getPreferredSize());
        p.add(new JLabel(new Ikon(namaIkon, ukuran / 2, warna)));
        return p;
    }

    /** Judul halaman beserta keterangan di bawahnya. */
    public static JPanel kepalaHalaman(String judul, String keterangan) {
        return kolom(4, Ui.judul(judul, 26), pudar(keterangan, 14));
    }

    /** Tampilan saat belum ada data, lengkap dengan ilustrasi. */
    public static JPanel kosong(String judul, String pesan, JComponent aksi) {
        PanelBulat kartu = PanelBulat.kartu(40);
        kartu.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(6, 0, 6, 0);
        kartu.add(new Ilustrasi(Ilustrasi.Jenis.KOSONG, 96, 96, Tema.UTAMA_MUDA), c);
        kartu.add(Ui.judul(judul, 18), c);
        kartu.add(paragraf(pesan, 380, 14, Tema.TEKS_PUDAR, "center"), c);
        if (aksi != null) {
            c.insets = new Insets(14, 0, 0, 0);
            kartu.add(aksi, c);
        }
        return kartu;
    }
}
