package com.klinikpro;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MenuUtama extends JFrame {

    private final CardLayout rootLayout = new CardLayout();
    private final JPanel root = new JPanel(rootLayout);
    private final CardLayout contentLayout = new CardLayout();
    private final JPanel content = new JPanel(contentLayout);

    private final DokterRepository dokterRepo;
    private DefaultTableModel modelDokter;
    private JTable tabelDokter;
    private java.util.List<Dokter> hasilDokter = new java.util.ArrayList<>();

    public MenuUtama(DokterRepository dokterRepo) {
        super("KlinikPro");
        this.dokterRepo = dokterRepo;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 600);
        setLocationRelativeTo(null);
        root.add(buatLogin(), "login");
        root.add(buatRegistrasi(), "register");
        root.add(buatAplikasi(), "app");
        add(root);
        rootLayout.show(root, "login");
    }

    // ---------- Helper ----------
    private JLabel judul(String teks) {
        JLabel l = new JLabel(teks);
        l.setFont(new Font("Serif", Font.BOLD, 22));
        l.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        return l;
    }

    private JPanel formPanel(String[] label, JComponent[] isi) {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        for (int i = 0; i < label.length; i++) {
            c.gridx = 0;
            c.gridy = i;
            p.add(new JLabel(label[i]), c);
            c.gridx = 1;
            p.add(isi[i], c);
        }
        return p;
    }

    private JButton tombolInfo(String teks) {
        JButton b = new JButton(teks);
        b.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Fitur " + teks + " aktif setelah database terhubung."));
        return b;
    }

    private JPanel halamanTabel(String namaHalaman, String[] kolom, JComponent atas, String... tombol) {
        JPanel p = new JPanel(new BorderLayout());
        JPanel north = new JPanel(new BorderLayout());
        north.add(judul(namaHalaman), BorderLayout.NORTH);
        if (atas != null) {
            north.add(atas, BorderLayout.CENTER);
        }
        p.add(north, BorderLayout.NORTH);
        p.add(new JScrollPane(new JTable(new DefaultTableModel(kolom, 0))), BorderLayout.CENTER);
        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        for (String s : tombol) {
            bawah.add(tombolInfo(s));
        }
        p.add(bawah, BorderLayout.SOUTH);
        return p;
    }

    // ---------- Login & Registrasi ----------
    private JPanel buatLogin() {
        JTextField email = new JTextField(20);
        JPasswordField pass = new JPasswordField(20);
        JButton login = new JButton("Login");
        JButton daftar = new JButton("Daftar");
        login.addActionListener(e -> rootLayout.show(root, "app"));
        daftar.addActionListener(e -> rootLayout.show(root, "register"));

        JLabel title = new JLabel("KlinikPro", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        JPanel tombol = new JPanel();
        tombol.add(login);
        tombol.add(daftar);

        JPanel box = new JPanel(new BorderLayout(0, 15));
        box.add(title, BorderLayout.NORTH);
        box.add(formPanel(new String[]{"Email", "Password"},
                new JComponent[]{email, pass}), BorderLayout.CENTER);
        box.add(tombol, BorderLayout.SOUTH);

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.add(box);
        return wrap;
    }

    private JPanel buatRegistrasi() {
        JButton daftar = tombolInfo("Daftar");
        JButton kembali = new JButton("Kembali");
        kembali.addActionListener(e -> rootLayout.show(root, "login"));

        JLabel title = new JLabel("Registrasi Akun", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        JPanel tombol = new JPanel();
        tombol.add(daftar);
        tombol.add(kembali);

        JPanel box = new JPanel(new BorderLayout(0, 15));
        box.add(title, BorderLayout.NORTH);
        box.add(formPanel(new String[]{"Nama", "Email", "No. Telepon", "Password"},
                new JComponent[]{new JTextField(20), new JTextField(20),
                    new JTextField(20), new JPasswordField(20)}), BorderLayout.CENTER);
        box.add(tombol, BorderLayout.SOUTH);

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.add(box);
        return wrap;
    }

    // ---------- Aplikasi utama ----------
    private JPanel buatAplikasi() {
        String[] menu = {"Homepage", "Cari Dokter", "Buat Janji", "Rekam Medis",
            "Obat dan Resep", "Pembayaran", "Riwayat Transaksi", "Profil", "About"};

        content.add(halamanHome(), "Homepage");
        content.add(halamanCariDokter(), "Cari Dokter");
        content.add(halamanJanji(), "Buat Janji");
        content.add(halamanTabel("Rekam Medis",
                new String[]{"Tanggal", "Dokter", "Keluhan", "Diagnosis", "Tindakan"}, null), "Rekam Medis");
        content.add(halamanTabel("Obat dan Resep",
                new String[]{"Nama Obat", "Harga", "Stok"},
                new JLabel("   Resep dari dokter dan katalog obat akan tampil di sini."),
                "Pesan Obat", "Tebus Resep"), "Obat dan Resep");
        content.add(halamanTabel("Pembayaran",
                new String[]{"Tanggal", "Keterangan", "Total", "Status"}, null,
                "Bayar", "Cetak Kuitansi"), "Pembayaran");
        content.add(halamanTabel("Riwayat Transaksi",
                new String[]{"Tanggal", "Keterangan", "Total", "Metode"},
                new JLabel("   Total pengeluaran bulan ini: Rp 0")), "Riwayat Transaksi");
        content.add(halamanProfil(), "Profil");
        content.add(halamanAbout(), "About");

        JPanel sidebar = new JPanel(new GridLayout(0, 1, 0, 6));
        sidebar.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        for (String m : menu) {
            JButton b = new JButton(m);
            b.addActionListener(e -> contentLayout.show(content, m));
            sidebar.add(b);
        }
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            int pilih = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?",
                    "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
            if (pilih == JOptionPane.YES_OPTION) {
                rootLayout.show(root, "login");
            }
        });
        sidebar.add(logout);

        JPanel west = new JPanel(new BorderLayout());
        west.add(sidebar, BorderLayout.NORTH);

        JPanel p = new JPanel(new BorderLayout());
        p.add(west, BorderLayout.WEST);
        p.add(content, BorderLayout.CENTER);
        return p;
    }

    private JPanel halamanHome() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel north = new JPanel(new BorderLayout());
        north.add(judul("Selamat Datang di KlinikPro"), BorderLayout.NORTH);
        JPanel cari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cari.add(new JTextField(25));
        cari.add(tombolInfo("Search"));
        north.add(cari, BorderLayout.CENTER);
        p.add(north, BorderLayout.NORTH);
        p.add(new JLabel("   Janji temu terdekat: belum ada"), BorderLayout.CENTER);
        return p;
    }

    // ---------- Cari Dokter (terhubung ke database) ----------
    private JPanel halamanCariDokter() {
        JTextField namaField = new JTextField(20);
        JComboBox<String> spesialis = new JComboBox<>(
                new String[]{"Semua spesialis", "Umum", "Anak", "Penyakit Dalam"});
        JButton cari = new JButton("Cari");
        JPanel atas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        atas.add(namaField);
        atas.add(spesialis);
        atas.add(cari);

        modelDokter = new DefaultTableModel(
                new String[]{"Nama Dokter", "Spesialis", "Jadwal Praktik", "Tarif"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tabelDokter = new JTable(modelDokter);

        Runnable muat = () -> {
            String sp = spesialis.getSelectedIndex() == 0 ? "" : (String) spesialis.getSelectedItem();
            hasilDokter = dokterRepo
                    .findByNamaContainingIgnoreCaseAndSpesialisContainingIgnoreCase(
                            namaField.getText().trim(), sp);
            modelDokter.setRowCount(0);
            for (Dokter d : hasilDokter) {
                modelDokter.addRow(new Object[]{d.getNama(), d.getSpesialis(),
                    d.getJadwalTeks(), "Rp " + String.format("%,d", d.getTarif().longValue())});
            }
        };
        cari.addActionListener(e -> muat.run());
        muat.run();

        JButton detail = new JButton("Lihat Detail");
        detail.addActionListener(e -> {
            int i = tabelDokter.getSelectedRow();
            if (i < 0) {
                JOptionPane.showMessageDialog(this, "Pilih dokter dulu.");
                return;
            }
            Dokter d = hasilDokter.get(i);
            JOptionPane.showMessageDialog(this, d.getNama() + "\nSpesialis: " + d.getSpesialis()
                    + "\nJadwal: " + d.getJadwalTeks() + "\n\n" + d.getDeskripsi());
        });
        JButton janji = new JButton("Buat Janji");
        janji.addActionListener(e -> contentLayout.show(content, "Buat Janji"));

        JPanel p = new JPanel(new BorderLayout());
        JPanel north = new JPanel(new BorderLayout());
        north.add(judul("Cari Dokter"), BorderLayout.NORTH);
        north.add(atas, BorderLayout.CENTER);
        p.add(north, BorderLayout.NORTH);
        p.add(new JScrollPane(tabelDokter), BorderLayout.CENTER);
        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bawah.add(detail);
        bawah.add(janji);
        p.add(bawah, BorderLayout.SOUTH);
        return p;
    }

    private JPanel halamanJanji() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(judul("Buat Janji"), BorderLayout.NORTH);
        JPanel form = formPanel(
                new String[]{"Dokter", "Tanggal", "Jam", "Keluhan"},
                new JComponent[]{
                    new JComboBox<>(new String[]{"- pilih dokter -"}),
                    new JTextField(12),
                    new JComboBox<>(new String[]{"08:00", "09:00", "10:00", "11:00"}),
                    new JScrollPane(new JTextArea(4, 25))});
        JPanel kiri = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kiri.add(form);
        p.add(kiri, BorderLayout.CENTER);
        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bawah.add(tombolInfo("Buat Janji"));
        bawah.add(tombolInfo("Batalkan"));
        p.add(bawah, BorderLayout.SOUTH);
        return p;
    }

    private JPanel halamanProfil() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(judul("Profil Pengguna"), BorderLayout.NORTH);
        JPanel kiri = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kiri.add(formPanel(new String[]{"Nama", "Email", "No. Telepon"},
                new JComponent[]{new JTextField(20), new JTextField(20), new JTextField(20)}));
        p.add(kiri, BorderLayout.CENTER);
        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bawah.add(tombolInfo("Simpan"));
        bawah.add(tombolInfo("Ganti Password"));
        p.add(bawah, BorderLayout.SOUTH);
        return p;
    }

    private JPanel halamanAbout() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(judul("About"), BorderLayout.NORTH);
        JTextArea t = new JTextArea(
                "KlinikPro\n\nAplikasi layanan klinik untuk pasien.\n"
                + "Alamat: -\nJam operasional: -\nKontak: -");
        t.setEditable(false);
        t.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        p.add(t, BorderLayout.CENTER);
        return p;
    }
}