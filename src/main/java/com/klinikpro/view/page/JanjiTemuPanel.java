package com.klinikpro.view.page;

import com.klinikpro.controller.DokterController;
import com.klinikpro.controller.JanjiController;
import com.klinikpro.controller.ValidasiException;
import com.klinikpro.model.Dokter;
import com.klinikpro.model.Janji;
import com.klinikpro.service.JanjiService;
import com.klinikpro.view.kit.Avatar;
import com.klinikpro.view.kit.PanelBulat;
import com.klinikpro.view.kit.PanelHalaman;
import com.klinikpro.view.kit.Tema;
import com.klinikpro.view.kit.TombolBulat;
import com.klinikpro.view.kit.Ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/** VIEW: halaman Janji Temu: daftar janji pasien dan form untuk membuat janji baru. */
public class JanjiTemuPanel extends PanelHalaman {

    private static final DateTimeFormatter FORMAT_TANGGAL = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Ui.INDONESIA);
    private static final DateTimeFormatter FORMAT_PENDEK = DateTimeFormatter.ofPattern("d MMM yyyy", Ui.INDONESIA);
    private static final DateTimeFormatter FORMAT_JAM = DateTimeFormatter.ofPattern("HH:mm");
    private static final String[] TAB = {"Akan Datang", "Selesai", "Dibatalkan"};

    private final JanjiController janjiController;
    private final JPanel wadahTab = Ui.transparan(new BorderLayout());
    private final JPanel wadahDaftar = Ui.transparan(new BorderLayout());
    private final JComboBox<Dokter> pilihanDokter = new JComboBox<>();
    private final JComboBox<LocalDate> pilihanTanggal = new JComboBox<>();
    private final JPanel wadahJam = Ui.transparan(new BorderLayout());
    private final JTextArea kolomKeluhan = new JTextArea(3, 20);
    private final JLabel labelBiaya = Ui.teks("-", 15, Font.BOLD, Tema.TEKS);
    private final JLabel pesanSalah = Ui.teks(" ", 13, Font.BOLD, Tema.BAHAYA);

    private List<Janji> semuaJanji = new ArrayList<>();
    private int tabAktif = 0;
    private LocalTime jamDipilih;

    public JanjiTemuPanel(JanjiController janjiController, DokterController dokterController) {
        this.janjiController = janjiController;

        add(Ui.kepalaHalaman("Janji Temu", "Kelola janji temu dan ambil nomor antrean dari rumah"),
                BorderLayout.NORTH);

        for (Dokter d : dokterController.semua()) {
            pilihanDokter.addItem(d);
        }
        pilihanDokter.setRenderer(tampilan(o -> ((Dokter) o).getNama() + " (" + ((Dokter) o).getSpesialis() + ")"));
        pilihanTanggal.setRenderer(tampilan(o -> ((LocalDate) o).format(FORMAT_TANGGAL)));
        pilihanDokter.addActionListener(e -> isiTanggal());
        pilihanTanggal.addActionListener(e -> isiJam());

        JPanel kiri = Ui.kolom(14, wadahTab, wadahDaftar);
        JPanel kanan = Ui.transparan(new BorderLayout());
        kanan.add(buatForm(), BorderLayout.NORTH);
        add(Ui.kiriKanan(kiri, kanan, 20), BorderLayout.CENTER);

        isiTanggal();
        segarkan();
    }

    /** Muat ulang data janji dari database setiap kali halaman dibuka. */
    public void segarkan() {
        semuaJanji = janjiController.janjiSaya();
        isiTab();
        isiDaftar();
        isiJam();
    }

    /** Dipanggil dari Cari Dokter: langsung pilih dokter di form. */
    public void pilihDokter(Dokter dokter) {
        for (int i = 0; i < pilihanDokter.getItemCount(); i++) {
            if (pilihanDokter.getItemAt(i).getId().equals(dokter.getId())) {
                pilihanDokter.setSelectedIndex(i);
                return;
            }
        }
    }

    // ---------- Form buat janji ----------
    private JComponent buatForm() {
        gayaPilihan(pilihanDokter);
        gayaPilihan(pilihanTanggal);

        kolomKeluhan.setLineWrap(true);
        kolomKeluhan.setWrapStyleWord(true);
        kolomKeluhan.setFont(Tema.huruf(Font.PLAIN, 15));
        kolomKeluhan.setOpaque(false);
        kolomKeluhan.setBorder(new EmptyBorder(10, 12, 10, 12));
        PanelBulat kotakKeluhan = new PanelBulat(Tema.PERMUKAAN, Tema.GARIS_INPUT, 12, 0);
        kotakKeluhan.add(kolomKeluhan);

        TombolBulat konfirmasi = new TombolBulat("Konfirmasi Janji", TombolBulat.Gaya.UTAMA);
        konfirmasi.setPreferredSize(new Dimension(316, 48));
        konfirmasi.addActionListener(e -> prosesBuatJanji());

        PanelBulat kartu = PanelBulat.kartu(22);
        kartu.add(Ui.kolom(14,
                Ui.judul("Buat janji baru", 18),
                isian("Dokter", pilihanDokter),
                isian("Tanggal", pilihanTanggal),
                isian("Pilih jam", wadahJam),
                isian("Keluhan (opsional)", kotakKeluhan),
                Ui.kiriKanan(Ui.pudar("Biaya konsultasi", 14), labelBiaya, 8),
                pesanSalah,
                konfirmasi));
        return kartu;
    }

    private void isiTanggal() {
        Dokter d = (Dokter) pilihanDokter.getSelectedItem();
        pilihanTanggal.removeAllItems();
        if (d != null) {
            for (LocalDate t : janjiController.tanggalPraktik(d)) {
                pilihanTanggal.addItem(t);
            }
            labelBiaya.setText(Ui.rupiah(d.getTarif()));
        }
        isiJam();
    }

    /** Tampilkan pilihan jam. Jam yang sudah diambil pasien lain tidak bisa diklik. */
    private void isiJam() {
        jamDipilih = null;
        Dokter d = (Dokter) pilihanDokter.getSelectedItem();
        LocalDate t = (LocalDate) pilihanTanggal.getSelectedItem();
        if (d == null || t == null) {
            ganti(wadahJam, Ui.pudar("Dokter belum memiliki jadwal praktik.", 13));
            return;
        }
        List<LocalTime> terisi = janjiController.jamTerisi(d, t);
        JPanel grid = Ui.grid(3, 8);
        for (LocalTime jam : janjiController.slotJam(d, t)) {
            TombolBulat tombol = new TombolBulat(jam.format(FORMAT_JAM), TombolBulat.Gaya.CHIP);
            if (terisi.contains(jam)) {
                tombol.setEnabled(false);
                tombol.setToolTipText("Sudah diambil pasien lain");
            }
            tombol.addActionListener(e -> {
                jamDipilih = jam;
                for (Component c : grid.getComponents()) {
                    ((TombolBulat) c).setGaya(c == tombol ? TombolBulat.Gaya.CHIP_AKTIF : TombolBulat.Gaya.CHIP);
                }
            });
            grid.add(tombol);
        }
        ganti(wadahJam, grid);
    }

    private void prosesBuatJanji() {
        try {
            Janji j = janjiController.buatJanji((Dokter) pilihanDokter.getSelectedItem(),
                    (LocalDate) pilihanTanggal.getSelectedItem(), jamDipilih, kolomKeluhan.getText());
            pesanSalah.setText(" ");
            kolomKeluhan.setText("");
            JOptionPane.showMessageDialog(this,
                    "Janji temu berhasil dibuat.\n\n"
                    + j.getDokter().getNama() + "\n"
                    + j.getTanggal().format(FORMAT_TANGGAL) + ", pukul " + j.getJam().format(FORMAT_JAM) + "\n"
                    + "Nomor antrean Anda: " + j.getKodeAntrean(),
                    "Janji Temu", JOptionPane.INFORMATION_MESSAGE);
            tabAktif = 0;
            segarkan();
        } catch (ValidasiException ex) {
            pesanSalah.setText("<html><div style='width:260px'>" + ex.getMessage() + "</div></html>");
        }
    }

    // ---------- Daftar janji ----------
    /** 0 = akan datang, 1 = selesai (atau tanggalnya sudah lewat), 2 = dibatalkan. */
    private static int kategori(Janji j) {
        if (Janji.DIBATALKAN.equals(j.getStatus())) return 2;
        if (Janji.SELESAI.equals(j.getStatus()) || j.getTanggal().isBefore(LocalDate.now())) return 1;
        return 0;
    }

    private void isiTab() {
        int[] jumlah = new int[3];
        for (Janji j : semuaJanji) {
            jumlah[kategori(j)]++;
        }
        JComponent[] tombol = new JComponent[3];
        for (int i = 0; i < 3; i++) {
            int indeks = i;
            TombolBulat t = new TombolBulat(TAB[i] + " (" + jumlah[i] + ")",
                    i == tabAktif ? TombolBulat.Gaya.CHIP_AKTIF : TombolBulat.Gaya.CHIP);
            t.addActionListener(e -> {
                tabAktif = indeks;
                isiTab();
                isiDaftar();
            });
            tombol[i] = t;
        }
        ganti(wadahTab, Ui.baris(8, tombol));
    }

    private void isiDaftar() {
        List<JComponent> kartu = new ArrayList<>();
        for (Janji j : semuaJanji) {
            if (kategori(j) == tabAktif) {
                kartu.add(tabAktif == 0 ? kartuAkanDatang(j) : barisRiwayat(j));
            }
        }
        if (kartu.isEmpty()) {
            String[] judul = {"Belum ada janji temu", "Belum ada janji yang selesai", "Tidak ada janji yang dibatalkan"};
            String[] pesan = {
                "Pilih dokter, tanggal, dan jam di form sebelah kanan untuk mendapatkan nomor antrean.",
                "Janji yang sudah lewat atau selesai diperiksa akan muncul di sini.",
                "Janji yang Anda batalkan akan muncul di sini."};
            ganti(wadahDaftar, Ui.kosong(judul[tabAktif], pesan[tabAktif], null));
            return;
        }
        ganti(wadahDaftar, Ui.kolom(12, kartu.toArray(new JComponent[0])));
    }

    private JComponent kartuAkanDatang(Janji j) {
        Dokter d = j.getDokter();

        JPanel isiTanggal = Ui.transparan(new GridLayout(3, 1));
        isiTanggal.add(tengah(Ui.teks(JanjiService.namaHari(j.getTanggal()).toUpperCase(), 11, Font.BOLD, Tema.UTAMA_GELAP)));
        isiTanggal.add(tengah(Ui.teks(String.valueOf(j.getTanggal().getDayOfMonth()), 26, Font.BOLD, Tema.UTAMA_GELAP)));
        isiTanggal.add(tengah(Ui.teks(j.getTanggal().format(DateTimeFormatter.ofPattern("MMM", Ui.INDONESIA)).toUpperCase(),
                11, Font.BOLD, Tema.UTAMA_GELAP)));
        PanelBulat ubinTanggal = new PanelBulat(new GridBagLayout(), Tema.UTAMA_MUDA, null, 14, 0);
        ubinTanggal.setPreferredSize(new Dimension(76, 76));
        ubinTanggal.add(isiTanggal);

        JPanel isiAntrean = Ui.transparan(new GridLayout(2, 1));
        isiAntrean.add(tengah(Ui.teks("Antrean", 12, Font.PLAIN, Tema.TEKS_DI_UTAMA)));
        isiAntrean.add(tengah(Ui.teks(j.getKodeAntrean(), 26, Font.BOLD, Color.WHITE)));
        PanelBulat kotakAntrean = new PanelBulat(new GridBagLayout(), Tema.UTAMA, null, 14, 0);
        kotakAntrean.setPreferredSize(new Dimension(104, 76));
        kotakAntrean.add(isiAntrean);

        JPanel tengah = Ui.kolom(4,
                Ui.teks(d.getNama(), 18, Font.BOLD, Tema.TEKS),
                Ui.pudar(d.getSpesialis() + " · Pukul " + j.getJam().format(FORMAT_JAM), 14),
                Ui.paragraf(j.getKeluhan().isEmpty() ? " " : "Keluhan: " + j.getKeluhan(), 280, 14, Tema.TEKS_SEDANG));

        JPanel atas = Ui.transparan(new BorderLayout(16, 0));
        atas.add(Ui.baris(0, ubinTanggal), BorderLayout.WEST);
        atas.add(tengah, BorderLayout.CENTER);
        atas.add(Ui.baris(0, kotakAntrean), BorderLayout.EAST);

        PanelBulat info = new PanelBulat(new BorderLayout(), Tema.LATAR, null, 12, 14);
        info.add(Ui.baris(32,
                infoKecil("Status", "Terjadwal", Tema.UTAMA_GELAP),
                infoKecil("Tanggal", j.getTanggal().format(FORMAT_TANGGAL), Tema.TEKS),
                infoKecil("Biaya konsultasi", Ui.rupiah(d.getTarif()), Tema.TEKS)));

        TombolBulat batal = new TombolBulat("Batalkan Janji", TombolBulat.Gaya.GARIS);
        batal.setForeground(Tema.BAHAYA);
        batal.addActionListener(e -> prosesBatal(j));

        PanelBulat kartu = PanelBulat.kartu(20);
        kartu.setLayout(new BorderLayout(0, 16));
        kartu.add(atas, BorderLayout.NORTH);
        kartu.add(info, BorderLayout.CENTER);
        kartu.add(Ui.kiriKanan(null, batal, 0), BorderLayout.SOUTH);
        return kartu;
    }

    private JComponent barisRiwayat(Janji j) {
        Dokter d = j.getDokter();
        boolean dibatalkan = Janji.DIBATALKAN.equals(j.getStatus());
        PanelBulat k = PanelBulat.kartu(14);
        k.setLayout(new BorderLayout(14, 0));
        k.add(new Avatar(d.getInisial(), 44, d.getId()), BorderLayout.WEST);
        k.add(Ui.kolom(2,
                Ui.teks(d.getNama(), 15, Font.BOLD, Tema.TEKS),
                Ui.pudar(j.getTanggal().format(FORMAT_PENDEK) + " · " + j.getJam().format(FORMAT_JAM)
                        + " · Antrean " + j.getKodeAntrean(), 13)), BorderLayout.CENTER);
        k.add(Ui.baris(0, Ui.lencana(dibatalkan ? "Dibatalkan" : "Selesai",
                dibatalkan ? Tema.BAHAYA_MUDA : Tema.UTAMA_MUDA,
                dibatalkan ? Tema.BAHAYA : Tema.UTAMA_GELAP)), BorderLayout.EAST);
        return k;
    }

    private void prosesBatal(Janji j) {
        int pilih = JOptionPane.showConfirmDialog(this,
                "Batalkan janji dengan " + j.getDokter().getNama() + "\npada "
                + j.getTanggal().format(FORMAT_TANGGAL) + ", pukul " + j.getJam().format(FORMAT_JAM) + "?",
                "Batalkan Janji", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (pilih != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            janjiController.batalkan(j);
            segarkan();
        } catch (ValidasiException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Batalkan Janji", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- Bantuan tampilan ----------
    private static JComponent isian(String label, JComponent komponen) {
        return Ui.kolom(6, Ui.teks(label, 14, Font.BOLD, Tema.TEKS), komponen);
    }

    private static JComponent infoKecil(String label, String nilai, Color warna) {
        return Ui.kolom(2, Ui.pudar(label, 13), Ui.teks(nilai, 14, Font.BOLD, warna));
    }

    private static JLabel tengah(JLabel label) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private static void gayaPilihan(JComboBox<?> pilihan) {
        pilihan.setFont(Tema.huruf(Font.PLAIN, 15));
        pilihan.setPreferredSize(new Dimension(316, 44));
        pilihan.setMinimumSize(new Dimension(120, 44));
    }

    /** Cara menampilkan isi pilihan (JComboBox) sebagai teks. */
    private static DefaultListCellRenderer tampilan(Function<Object, String> ubahKeTeks) {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Object teks = value == null ? "" : ubahKeTeks.apply(value);
                return super.getListCellRendererComponent(list, teks, index, isSelected, cellHasFocus);
            }
        };
    }

    private static void ganti(JPanel wadah, JComponent isi) {
        wadah.removeAll();
        wadah.add(isi, BorderLayout.CENTER);
        wadah.revalidate();
        wadah.repaint();
    }
}
