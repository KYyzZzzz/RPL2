package com.klinikpro.view;

import com.klinikpro.controller.DokterController;
import com.klinikpro.view.kit.Ikon;
import com.klinikpro.view.kit.PanelBulat;
import com.klinikpro.view.kit.PanelHalaman;
import com.klinikpro.view.kit.Tema;
import com.klinikpro.view.kit.TombolBulat;
import com.klinikpro.view.kit.Ui;
import com.klinikpro.view.page.BerandaPanel;
import com.klinikpro.view.page.CariDokterPanel;
import com.klinikpro.view.page.ProfilPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/** VIEW: kerangka aplikasi, berisi menu samping di kiri dan halaman aktif di kanan. */
class KerangkaAplikasi extends JPanel implements Navigasi {

    private final JendelaUtama jendela;
    private final CardLayout tumpukan = new CardLayout();
    private final JPanel isi = new JPanel(tumpukan);
    private final Map<Halaman, TombolBulat> tombolMenu = new EnumMap<>(Halaman.class);
    private final BerandaPanel beranda;
    private final CariDokterPanel cariDokter;

    KerangkaAplikasi(JendelaUtama jendela, DokterController dokterController) {
        super(new BorderLayout());
        this.jendela = jendela;

        beranda = new BerandaPanel(this, dokterController);
        cariDokter = new CariDokterPanel(this, dokterController);

        tambah(Halaman.BERANDA, beranda);
        tambah(Halaman.CARI_DOKTER, cariDokter);
        tambah(Halaman.JANJI_TEMU, halamanKosong(Halaman.JANJI_TEMU,
                "Kelola janji temu dan ambil nomor antrean dari rumah",
                "Belum ada janji temu",
                "Pilih dokter di menu Cari Dokter, lalu buat janji untuk mendapatkan nomor antrean.",
                "Cari Dokter", Halaman.CARI_DOKTER));
        tambah(Halaman.REKAM_MEDIS, halamanKosong(Halaman.REKAM_MEDIS,
                "Riwayat pemeriksaan Anda di KlinikPro",
                "Belum ada rekam medis",
                "Hasil pemeriksaan akan muncul di sini setelah Anda berobat di klinik.",
                null, null));
        tambah(Halaman.OBAT, halamanKosong(Halaman.OBAT,
                "Tebus resep dari dokter atau beli obat bebas di apotek klinik",
                "Belum ada resep",
                "Resep dari dokter akan muncul di sini. Katalog obat bisa dilihat setelah fitur apotek aktif.",
                null, null));
        tambah(Halaman.PEMBAYARAN, halamanKosong(Halaman.PEMBAYARAN,
                "Bayar tagihan dan pantau pengeluaran berobat Anda",
                "Belum ada tagihan",
                "Tagihan konsultasi dan obat beserta riwayat pembayarannya akan muncul di sini.",
                null, null));
        tambah(Halaman.ARTIKEL, halamanKosong(Halaman.ARTIKEL,
                "Tips sehat dan informasi dari tim dokter KlinikPro",
                "Belum ada artikel",
                "Artikel kesehatan dari dokter klinik akan tampil di halaman ini.",
                null, null));
        tambah(Halaman.PROFIL, new ProfilPanel());

        add(buatMenuSamping(), BorderLayout.WEST);
        isi.setBackground(Tema.LATAR);
        add(isi, BorderLayout.CENTER);
        buka(Halaman.BERANDA);
    }

    private void tambah(Halaman halaman, JComponent panel) {
        isi.add(Ui.gulir(panel), halaman.name());
    }

    private JComponent halamanKosong(Halaman halaman, String keterangan, String judulKosong,
            String pesanKosong, String teksAksi, Halaman tujuanAksi) {
        PanelHalaman p = new PanelHalaman();
        p.add(Ui.kepalaHalaman(halaman.judul, keterangan), BorderLayout.NORTH);
        JComponent aksi = null;
        if (teksAksi != null) {
            TombolBulat t = new TombolBulat(teksAksi, TombolBulat.Gaya.UTAMA);
            t.addActionListener(e -> buka(tujuanAksi));
            aksi = t;
        }
        p.add(Ui.kosong(judulKosong, pesanKosong, aksi), BorderLayout.CENTER);
        return p;
    }

    private JComponent buatMenuSamping() {
        JPanel samping = new JPanel(new BorderLayout());
        samping.setBackground(Tema.PERMUKAAN);
        samping.setPreferredSize(new Dimension(240, 0));
        samping.setBorder(new CompoundBorder(new MatteBorder(0, 0, 0, 1, Tema.GARIS), new EmptyBorder(24, 16, 20, 16)));

        PanelBulat logo = new PanelBulat(new GridBagLayout(), Tema.UTAMA, null, 10, 0);
        logo.setPreferredSize(new Dimension(36, 36));
        logo.add(new JLabel(new Ikon("plus", 20, Color.WHITE)));
        JPanel atas = Ui.baris(10, logo, Ui.teks("KlinikPro", 20, Font.BOLD, Tema.UTAMA));
        atas.setBorder(new EmptyBorder(0, 10, 22, 0));

        JPanel menu = Ui.transparan(new GridLayout(0, 1, 0, 4));
        for (Halaman h : Halaman.values()) {
            if (h == Halaman.PROFIL) continue;
            TombolBulat t = new TombolBulat(h.judul, h.ikon, TombolBulat.Gaya.MENU);
            t.addActionListener(e -> buka(h));
            tombolMenu.put(h, t);
            menu.add(t);
        }

        TombolBulat profil = new TombolBulat(Halaman.PROFIL.judul, Halaman.PROFIL.ikon, TombolBulat.Gaya.MENU);
        profil.addActionListener(e -> buka(Halaman.PROFIL));
        tombolMenu.put(Halaman.PROFIL, profil);
        TombolBulat keluar = new TombolBulat("Keluar", "keluar", TombolBulat.Gaya.MENU_BAHAYA);
        keluar.addActionListener(e -> keluar());
        JPanel bawah = Ui.transparan(new GridLayout(0, 1, 0, 4));
        bawah.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, Tema.GARIS), new EmptyBorder(14, 0, 0, 0)));
        bawah.add(profil);
        bawah.add(keluar);

        JPanel tengah = Ui.transparan(new BorderLayout());
        tengah.add(menu, BorderLayout.NORTH);
        samping.add(atas, BorderLayout.NORTH);
        samping.add(tengah, BorderLayout.CENTER);
        samping.add(bawah, BorderLayout.SOUTH);
        return samping;
    }

    @Override
    public void buka(Halaman halaman) {
        tombolMenu.forEach((h, t) -> t.setGaya(h == halaman ? TombolBulat.Gaya.MENU_AKTIF : TombolBulat.Gaya.MENU));
        if (halaman == Halaman.BERANDA) {
            beranda.segarkan();
        }
        tumpukan.show(isi, halaman.name());
    }

    @Override
    public void cariDokter(String kataKunci) {
        cariDokter.cari(kataKunci);
        buka(Halaman.CARI_DOKTER);
    }

    @Override
    public void keluar() {
        int pilih = JOptionPane.showConfirmDialog(this, "Yakin ingin keluar dari aplikasi?",
                "Keluar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (pilih == JOptionPane.YES_OPTION) {
            jendela.dispose();
            System.exit(0);
        }
    }
}
