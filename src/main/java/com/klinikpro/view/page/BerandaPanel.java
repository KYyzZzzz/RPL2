package com.klinikpro.view.page;

import com.klinikpro.model.Dokter;
import com.klinikpro.controller.DokterController;
import com.klinikpro.controller.Sesi;
import com.klinikpro.model.Pengguna;
import com.klinikpro.view.Halaman;
import com.klinikpro.view.Navigasi;
import com.klinikpro.view.kit.Avatar;
import com.klinikpro.view.kit.Ilustrasi;
import com.klinikpro.view.kit.KolomIsian;
import com.klinikpro.view.kit.PanelBulat;
import com.klinikpro.view.kit.PanelHalaman;
import com.klinikpro.view.kit.Tema;
import com.klinikpro.view.kit.TombolBulat;
import com.klinikpro.view.kit.Ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/** VIEW: halaman Beranda: sapaan, janji berikutnya, menu cepat, dokter, dan ringkasan. */
public class BerandaPanel extends PanelHalaman {

    private static final DateTimeFormatter FORMAT_TANGGAL =
            DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Ui.INDONESIA);

    private final Navigasi navigasi;
    private final DokterController dokterController;
    private final JLabel tanggal = Ui.pudar("", 14);
    private final JLabel sapaan = Ui.judul("", 26);
    private final JPanel wadahDokter = Ui.transparan(new BorderLayout());

    public BerandaPanel(Navigasi navigasi, DokterController dokterController) {
        this.navigasi = navigasi;
        this.dokterController = dokterController;

        add(buatKepala(), BorderLayout.NORTH);

        JPanel kiri = Ui.kolom(20, buatBanner(), buatMenuCepat(), buatKartuDokter());
        JPanel kanan = Ui.kolom(20, buatPengingatObat(), buatTagihan(), buatTips());
        kanan.setPreferredSize(new Dimension(300, kanan.getPreferredSize().height));
        add(Ui.kiriKanan(kiri, kanan, 20), BorderLayout.CENTER);

        segarkan();
    }

    /** Perbarui sapaan dan daftar dokter setiap kali Beranda dibuka. */
    public void segarkan() {
        tanggal.setText(LocalDate.now().format(FORMAT_TANGGAL));
        Pengguna p = Sesi.pengguna();
        sapaan.setText(salam() + ", " + (p == null ? "selamat datang" : p.getNamaDepan()));
        isiDokter();
    }

    private static String salam() {
        int jam = LocalTime.now().getHour();
        if (jam < 11) return "Selamat pagi";
        if (jam < 15) return "Selamat siang";
        if (jam < 18) return "Selamat sore";
        return "Selamat malam";
    }

    private JComponent buatKepala() {
        JTextField cari = KolomIsian.teks("Cari dokter atau spesialis");
        cari.setPreferredSize(new Dimension(320, 46));
        cari.addActionListener(e -> navigasi.cariDokter(cari.getText()));

        TombolBulat lonceng = new TombolBulat("", "lonceng", TombolBulat.Gaya.GARIS);
        lonceng.setToolTipText("Notifikasi");
        lonceng.setBorder(new EmptyBorder(0, 0, 0, 0));
        lonceng.setPreferredSize(new Dimension(46, 46));
        lonceng.addActionListener(e -> JOptionPane.showMessageDialog(this, "Belum ada notifikasi baru.",
                "Notifikasi", JOptionPane.INFORMATION_MESSAGE));

        return Ui.kiriKanan(Ui.kolom(2, tanggal, sapaan), Ui.baris(12, cari, lonceng), 16);
    }

    private JComponent buatBanner() {
        PanelBulat banner = new PanelBulat(new BorderLayout(20, 0), Tema.UTAMA, null, 16, 0);
        banner.setBorder(new EmptyBorder(22, 24, 22, 24));

        TombolBulat buatJanji = new TombolBulat("Cari Dokter & Buat Janji", TombolBulat.Gaya.PUTIH);
        buatJanji.addActionListener(e -> navigasi.buka(Halaman.CARI_DOKTER));

        banner.add(Ui.kolom(6,
                Ui.teks("JANJI TEMU BERIKUTNYA", 13, Font.BOLD, Tema.TEKS_DI_UTAMA),
                Ui.teks("Belum ada janji temu", 21, Font.BOLD, Color.WHITE),
                Ui.paragraf("Pilih dokter, tentukan jadwal, dan ambil nomor antrean dari rumah.",
                        280, 14, Tema.UTAMA_MUDA),
                Ui.baris(0, buatJanji)), BorderLayout.CENTER);
        banner.add(new Ilustrasi(Ilustrasi.Jenis.KLINIK, 190, 130, Tema.UTAMA_BANNER), BorderLayout.EAST);
        return banner;
    }

    private JComponent buatMenuCepat() {
        return Ui.grid(6, 12,
                ubin("Cari Dokter", "cari", Tema.UTAMA_MUDA, Tema.UTAMA, Halaman.CARI_DOKTER),
                ubin("Janji Temu", "kalender", Tema.AKSEN_MUDA, Tema.AKSEN, Halaman.JANJI_TEMU),
                ubin("Rekam Medis", "dokumen", Tema.BIRU_MUDA, Tema.BIRU, Halaman.REKAM_MEDIS),
                ubin("Beli Obat", "obat", Tema.UNGU_MUDA, Tema.UNGU, Halaman.OBAT),
                ubin("Tagihan", "kartu", Tema.UTAMA_MUDA, Tema.UTAMA, Halaman.PEMBAYARAN),
                ubin("Artikel", "buku", Tema.AKSEN_MUDA, Tema.AKSEN, Halaman.ARTIKEL));
    }

    private JComponent ubin(String teks, String ikon, Color latar, Color warna, Halaman tujuan) {
        TombolBulat t = TombolBulat.ubin(teks, ikon, latar, warna);
        t.addActionListener(e -> navigasi.buka(tujuan));
        return t;
    }

    private JComponent buatKartuDokter() {
        PanelBulat kartu = PanelBulat.kartu(20);
        kartu.setLayout(new BorderLayout(0, 14));
        TombolBulat semua = new TombolBulat("Lihat semua", TombolBulat.Gaya.TAUTAN);
        semua.addActionListener(e -> navigasi.buka(Halaman.CARI_DOKTER));
        kartu.add(Ui.kiriKanan(Ui.judul("Dokter yang tersedia", 17), semua, 0), BorderLayout.NORTH);
        kartu.add(wadahDokter, BorderLayout.CENTER);
        return kartu;
    }

    private void isiDokter() {
        wadahDokter.removeAll();
        try {
            List<Dokter> daftar = dokterController.semua();
            if (daftar.isEmpty()) {
                wadahDokter.add(Ui.pudar("Belum ada data dokter.", 14));
            } else {
                JPanel grid = Ui.grid(3, 14);
                for (int i = 0; i < Math.min(3, daftar.size()); i++) {
                    grid.add(kartuDokterKecil(daftar.get(i), i));
                }
                wadahDokter.add(grid);
            }
        } catch (RuntimeException ex) {
            wadahDokter.add(Ui.teks("Data dokter tidak bisa dimuat. Pastikan MySQL menyala.", 14, Font.BOLD, Tema.BAHAYA));
        }
        wadahDokter.revalidate();
        wadahDokter.repaint();
    }

    private JComponent kartuDokterKecil(Dokter d, int urutan) {
        PanelBulat k = new PanelBulat(Tema.PERMUKAAN, Tema.GARIS, 12, 14);
        TombolBulat janji = new TombolBulat("Lihat", TombolBulat.Gaya.UTAMA);
        janji.setFont(Tema.huruf(Font.BOLD, 13));
        janji.setPreferredSize(new Dimension(76, 36));
        janji.addActionListener(e -> navigasi.cariDokter(d.getNama()));

        k.add(Ui.kolom(10,
                new Avatar(d.getInisial(), 44, urutan),
                Ui.kolom(2,
                        Ui.teks("<html><div style='width:150px'>" + d.getNama() + "</div></html>", 14, Font.BOLD, Tema.TEKS),
                        Ui.pudar(d.getSpesialis(), 13)),
                Ui.teks(String.join(" & ", d.getHariPraktik()), 13, Font.PLAIN, Tema.TEKS_SEDANG),
                Ui.kiriKanan(Ui.teks(Ui.rupiah(d.getTarif()), 14, Font.BOLD, Tema.TEKS), janji, 8)));
        return k;
    }

    private JComponent buatPengingatObat() {
        PanelBulat kartu = PanelBulat.kartu(20);
        kartu.add(Ui.kolom(8,
                Ui.judul("Pengingat obat", 17),
                Ui.paragraf("Belum ada jadwal minum obat. Jadwal muncul otomatis dari resep dokter.",
                        200, 14, Tema.TEKS_PUDAR)));
        return kartu;
    }

    private JComponent buatTagihan() {
        PanelBulat kartu = PanelBulat.kartu(20);
        TombolBulat lihat = new TombolBulat("Lihat Pembayaran", TombolBulat.Gaya.GARIS);
        lihat.addActionListener(e -> navigasi.buka(Halaman.PEMBAYARAN));
        kartu.add(Ui.kolom(10,
                Ui.judul("Tagihan", 17),
                Ui.pudar("Tidak ada tagihan yang perlu dibayar.", 14),
                lihat));
        return kartu;
    }

    private JComponent buatTips() {
        PanelBulat kartu = new PanelBulat(new BorderLayout(14, 0), Tema.AKSEN_MUDA, null, 16, 18);
        TombolBulat baca = new TombolBulat("Baca artikel", TombolBulat.Gaya.TAUTAN);
        baca.setForeground(Tema.AKSEN_TEKS);
        baca.addActionListener(e -> navigasi.buka(Halaman.ARTIKEL));
        kartu.add(new Ilustrasi(Ilustrasi.Jenis.GELAS, 64, 64, new Color(0xF6D3B6)), BorderLayout.WEST);
        kartu.add(Ui.kolom(2,
                Ui.teks("TIPS SEHAT", 12, Font.BOLD, Tema.AKSEN_TEKS),
                Ui.teks("<html><div style='width:150px'>Minum air putih 8 gelas sehari</div></html>",
                        15, Font.BOLD, new Color(0x5C2D0E)),
                Ui.baris(0, baca)), BorderLayout.CENTER);
        return kartu;
    }
}
