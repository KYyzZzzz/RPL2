package com.klinikpro.view.kit;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import javax.swing.UIManager;

/** Warna dan huruf aplikasi (Opsi 1 - Teal Tenang). Ubah di sini supaya semua halaman ikut berubah. */
public final class Tema {

    public static final Color UTAMA = new Color(0x1F6F68);
    public static final Color UTAMA_GELAP = new Color(0x17564F);
    public static final Color UTAMA_HOVER = new Color(0x195D57);
    public static final Color UTAMA_MUDA = new Color(0xE3F1EF);
    public static final Color UTAMA_BANNER = new Color(0x2B8279);
    public static final Color TEKS_DI_UTAMA = new Color(0xD6ECE8);

    public static final Color LATAR = new Color(0xF4F7F6);
    public static final Color PERMUKAAN = Color.WHITE;
    public static final Color GARIS = new Color(0xDDE6E4);
    public static final Color GARIS_INPUT = new Color(0xC9D6D3);

    public static final Color TEKS = new Color(0x1E2B2A);
    public static final Color TEKS_SEDANG = new Color(0x44524F);
    public static final Color TEKS_PUDAR = new Color(0x5B6967);

    public static final Color AKSEN_MUDA = new Color(0xFCEBDD);
    public static final Color AKSEN = new Color(0xA4511C);
    public static final Color AKSEN_TEKS = new Color(0x8A4215);
    public static final Color KREM = new Color(0xF2C9A8);

    public static final Color BIRU_MUDA = new Color(0xE6EEF8);
    public static final Color BIRU = new Color(0x244A80);
    public static final Color UNGU_MUDA = new Color(0xEFE9F7);
    public static final Color UNGU = new Color(0x6A4A9C);

    public static final Color BAHAYA = new Color(0xA6402A);
    public static final Color BAHAYA_MUDA = new Color(0xFBE7E2);

    private static final String NAMA_HURUF = pilihHuruf();

    private Tema() {
    }

    private static String pilihHuruf() {
        String[] tersedia = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String calon : new String[]{"Segoe UI", "Plus Jakarta Sans", "Inter", "Helvetica Neue", "Arial"}) {
            if (Arrays.asList(tersedia).contains(calon)) {
                return calon;
            }
        }
        return Font.SANS_SERIF;
    }

    public static Font huruf(int gaya, float ukuran) {
        return new Font(NAMA_HURUF, gaya, Math.round(ukuran));
    }

    /** Dipanggil sekali di awal aplikasi, sebelum jendela dibuat. */
    public static void pasang() {
        try {
            FlatLightLaf.setup();
        } catch (Throwable t) {
            System.err.println("FlatLaf tidak tersedia, memakai tampilan bawaan: " + t.getMessage());
        }
        UIManager.put("defaultFont", huruf(Font.PLAIN, 14));
        UIManager.put("Component.arc", 12);
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("Component.focusColor", UTAMA);
        UIManager.put("Component.focusWidth", 1);
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new java.awt.Insets(2, 2, 2, 2));
        UIManager.put("OptionPane.messageFont", huruf(Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", huruf(Font.BOLD, 14));
    }
}
