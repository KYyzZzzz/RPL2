package com.klinikpro.view.page;

import com.klinikpro.model.Dokter;
import com.klinikpro.model.JadwalDokter;
import com.klinikpro.controller.DokterController;
import com.klinikpro.view.Halaman;
import com.klinikpro.view.Navigasi;
import com.klinikpro.view.kit.Avatar;
import com.klinikpro.view.kit.KolomIsian;
import com.klinikpro.view.kit.PanelBulat;
import com.klinikpro.view.kit.PanelHalaman;
import com.klinikpro.view.kit.Tema;
import com.klinikpro.view.kit.TombolBulat;
import com.klinikpro.view.kit.Ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/** VIEW: halaman Cari Dokter: pencarian, filter spesialis dan hari, serta detail dokter. */
public class CariDokterPanel extends PanelHalaman {

    private static final String[] HARI = {"Semua hari", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};

    private final Navigasi navigasi;
    private final DokterController dokterController;
    private final JTextField kolomCari = KolomIsian.teks("Cari nama dokter atau spesialis");
    private final JComboBox<String> pilihanHari = new JComboBox<>(HARI);
    private final JPanel wadahChip = Ui.transparan(new BorderLayout());
    private final JPanel wadahDaftar = Ui.transparan(new BorderLayout());
    private final JPanel wadahDetail = Ui.transparan(new BorderLayout());

    private String spesialisDipilih;
    private Dokter dokterDipilih;
    private List<Dokter> hasil = new ArrayList<>();

    public CariDokterPanel(Navigasi navigasi, DokterController dokterController) {
        this.navigasi = navigasi;
        this.dokterController = dokterController;

        add(Ui.kepalaHalaman("Cari Dokter", "Temukan dokter sesuai keluhan Anda, lalu buat janji temu"),
                BorderLayout.NORTH);

        pilihanHari.setPreferredSize(new Dimension(170, 46));
        pilihanHari.setFont(Tema.huruf(Font.PLAIN, 15));
        pilihanHari.addActionListener(e -> muat());
        kolomCari.addActionListener(e -> muat());
        TombolBulat tombolCari = new TombolBulat("Cari", TombolBulat.Gaya.UTAMA);
        tombolCari.setPreferredSize(new Dimension(90, 46));
        tombolCari.addActionListener(e -> muat());

        JPanel barisCari = Ui.transparan(new BorderLayout(10, 0));
        barisCari.add(kolomCari, BorderLayout.CENTER);
        barisCari.add(Ui.baris(10, pilihanHari, tombolCari), BorderLayout.EAST);

        JPanel kiri = Ui.kolom(16, barisCari, wadahChip, wadahDaftar);
        JPanel kanan = Ui.transparan(new BorderLayout());
        kanan.setPreferredSize(new Dimension(340, 10));
        kanan.add(wadahDetail, BorderLayout.NORTH);
        add(Ui.kiriKanan(kiri, kanan, 20), BorderLayout.CENTER);

        muat();
    }

    /** Dipanggil dari halaman lain, misalnya kolom cari di Beranda. */
    public void cari(String kataKunci) {
        kolomCari.setText(kataKunci == null ? "" : kataKunci.trim());
        spesialisDipilih = null;
        pilihanHari.setSelectedIndex(0);
        muat();
    }

    private void muat() {
        String hari = pilihanHari.getSelectedIndex() <= 0 ? null : (String) pilihanHari.getSelectedItem();
        try {
            hasil = dokterController.cari(kolomCari.getText(), spesialisDipilih, hari);
            isiChip(dokterController.daftarSpesialis());
            if (dokterDipilih == null || hasil.stream().noneMatch(d -> Objects.equals(d.getId(), dokterDipilih.getId()))) {
                dokterDipilih = hasil.isEmpty() ? null : hasil.get(0);
            }
            isiDaftar();
        } catch (RuntimeException ex) {
            hasil = new ArrayList<>();
            dokterDipilih = null;
            ganti(wadahDaftar, Ui.kosong("Data dokter tidak bisa dimuat",
                    "Pastikan MySQL di XAMPP sudah menyala, lalu tekan Cari lagi.", null));
            ex.printStackTrace();
        }
        isiDetail();
    }

    private void isiChip(List<String> spesialis) {
        List<JComponent> chip = new ArrayList<>();
        chip.add(buatChip("Semua", null));
        for (String s : spesialis) {
            chip.add(buatChip(s, s));
        }
        ganti(wadahChip, Ui.baris(8, chip.toArray(new JComponent[0])));
    }

    private JComponent buatChip(String teks, String nilai) {
        boolean aktif = Objects.equals(nilai, spesialisDipilih);
        TombolBulat t = new TombolBulat(teks, aktif ? TombolBulat.Gaya.CHIP_AKTIF : TombolBulat.Gaya.CHIP);
        t.addActionListener(e -> {
            spesialisDipilih = nilai;
            muat();
        });
        return t;
    }

    private void isiDaftar() {
        if (hasil.isEmpty()) {
            ganti(wadahDaftar, Ui.kosong("Dokter tidak ditemukan",
                    "Coba kata kunci lain, atau pilih spesialis dan hari yang berbeda.", null));
            return;
        }
        List<JComponent> baris = new ArrayList<>();
        for (int i = 0; i < hasil.size(); i++) {
            baris.add(barisDokter(hasil.get(i), i));
        }
        ganti(wadahDaftar, Ui.kolom(12, baris.toArray(new JComponent[0])));
    }

    private JComponent barisDokter(Dokter d, int urutan) {
        boolean dipilih = dokterDipilih != null && Objects.equals(d.getId(), dokterDipilih.getId());
        PanelBulat k = new PanelBulat(new BorderLayout(16, 0), Tema.PERMUKAAN,
                dipilih ? Tema.UTAMA : Tema.GARIS, 14, 0);
        if (dipilih) {
            k.setWarna(Tema.PERMUKAAN, Tema.UTAMA, 2);
        }
        k.setBorder(new EmptyBorder(16, 18, 16, 18));
        k.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        List<JComponent> hari = new ArrayList<>();
        for (String h : d.getHariPraktik()) {
            hari.add(Ui.lencana(h, Tema.UTAMA_MUDA, Tema.UTAMA_GELAP));
        }
        JPanel teks = Ui.kolom(3,
                Ui.teks(d.getNama(), 16, Font.BOLD, Tema.TEKS),
                Ui.pudar(d.getSpesialis(), 14),
                Ui.baris(8, hari.toArray(new JComponent[0])));

        JPanel tarif = Ui.kolom(2, Ui.pudar("Tarif", 13), Ui.teks(Ui.rupiah(d.getTarif()), 16, Font.BOLD, Tema.TEKS));

        k.add(new Avatar(d.getInisial(), 56, urutan), BorderLayout.WEST);
        k.add(teks, BorderLayout.CENTER);
        k.add(tarif, BorderLayout.EAST);
        k.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dokterDipilih = d;
                isiDaftar();
                isiDetail();
            }
        });
        return k;
    }

    private void isiDetail() {
        if (dokterDipilih == null) {
            PanelBulat kosong = PanelBulat.kartu(22);
            kosong.add(Ui.kolom(6, Ui.judul("Detail dokter", 16),
                    Ui.paragraf("Klik salah satu dokter di daftar untuk melihat jadwal dan tarifnya.", 220, 14, Tema.TEKS_PUDAR)));
            ganti(wadahDetail, kosong);
            return;
        }
        Dokter d = dokterDipilih;

        PanelBulat kepala = new PanelBulat(new BorderLayout(14, 0), Tema.UTAMA, null, 16, 22);
        kepala.add(new Avatar(d.getInisial(), 64, Color.WHITE, Tema.UTAMA_GELAP), BorderLayout.WEST);
        kepala.add(Ui.kolom(2,
                Ui.teks("<html><div style='width:200px'>" + d.getNama() + "</div></html>", 18, Font.BOLD, Color.WHITE),
                Ui.teks(d.getSpesialis(), 14, Font.PLAIN, Tema.TEKS_DI_UTAMA)), BorderLayout.CENTER);

        List<JComponent> jadwal = new ArrayList<>();
        jadwal.add(Ui.teks("JADWAL PRAKTIK", 13, Font.BOLD, Tema.TEKS_PUDAR));
        for (JadwalDokter j : d.getJadwal()) {
            jadwal.add(Ui.kiriKanan(Ui.teks(j.getHari(), 14, Font.PLAIN, Tema.TEKS),
                    Ui.teks(j.getJamTeks(), 14, Font.BOLD, Tema.TEKS), 8));
        }
        if (d.getJadwal().isEmpty()) {
            jadwal.add(Ui.pudar("Jadwal belum tersedia.", 14));
        }

        PanelBulat kotakTarif = new PanelBulat(Tema.LATAR, null, 10, 12);
        kotakTarif.add(Ui.kolom(2, Ui.pudar("Tarif konsultasi", 12), Ui.teks(Ui.rupiah(d.getTarif()), 16, Font.BOLD, Tema.TEKS)));

        TombolBulat buatJanji = new TombolBulat("Buat Janji dengan Dokter Ini", TombolBulat.Gaya.UTAMA);
        buatJanji.setPreferredSize(new Dimension(296, 48));
        buatJanji.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Fitur buat janji dengan " + d.getNama() + " akan aktif pada tahap berikutnya.",
                    "Buat Janji", JOptionPane.INFORMATION_MESSAGE);
            navigasi.buka(Halaman.JANJI_TEMU);
        });

        JPanel badan = Ui.kolom(16,
                Ui.kolom(6, Ui.teks("TENTANG DOKTER", 13, Font.BOLD, Tema.TEKS_PUDAR),
                        Ui.paragraf(d.getDeskripsi().isBlank() ? "-" : d.getDeskripsi(), 280, 14, Tema.TEKS)),
                Ui.kolom(8, jadwal.toArray(new JComponent[0])),
                kotakTarif,
                buatJanji);
        badan.setBorder(new EmptyBorder(20, 20, 20, 20));

        PanelBulat kartu = PanelBulat.kartu(0);
        kartu.setLayout(new BorderLayout());
        kartu.add(kepala, BorderLayout.NORTH);
        kartu.add(badan, BorderLayout.CENTER);
        ganti(wadahDetail, kartu);
    }

    private static void ganti(JPanel wadah, JComponent isi) {
        wadah.removeAll();
        wadah.add(isi, BorderLayout.CENTER);
        wadah.revalidate();
        wadah.repaint();
    }
}
