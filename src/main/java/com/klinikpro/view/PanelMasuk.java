package com.klinikpro.view;

import com.klinikpro.controller.AuthController;
import com.klinikpro.controller.ValidasiException;
import com.klinikpro.model.Pengguna;
import com.klinikpro.view.kit.KolomIsian;
import com.klinikpro.view.kit.Tema;
import com.klinikpro.view.kit.TombolBulat;
import com.klinikpro.view.kit.Ui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/** VIEW: halaman Masuk (login). */
class PanelMasuk extends JPanel {

    private final AuthController authController;
    private final JendelaUtama jendela;
    private final JTextField email = KolomIsian.teks("nama@email.com");
    private final JPasswordField password = KolomIsian.password("Masukkan password");
    private final JLabel pesanSalah = Ui.teks(" ", 14, Font.BOLD, Tema.BAHAYA);

    PanelMasuk(AuthController authController, JendelaUtama jendela) {
        super(new BorderLayout());
        this.authController = authController;
        this.jendela = jendela;
        setBackground(Tema.LATAR);

        add(new PanelSambutan("Berobat lebih mudah, tanpa antre lama",
                "Cari dokter, buat janji temu, lihat rekam medis, dan bayar tagihan dalam satu aplikasi."),
                BorderLayout.WEST);

        JCheckBox lihat = new JCheckBox("Tampilkan password");
        lihat.setOpaque(false);
        lihat.setFont(Tema.huruf(Font.PLAIN, 14));
        lihat.setForeground(Tema.TEKS_SEDANG);
        lihat.addActionListener(e -> password.setEchoChar(lihat.isSelected() ? (char) 0 : '\u2022'));

        TombolBulat masuk = new TombolBulat("Masuk", TombolBulat.Gaya.UTAMA);
        masuk.setPreferredSize(new Dimension(400, 50));
        masuk.addActionListener(e -> prosesMasuk());
        email.addActionListener(e -> password.requestFocusInWindow());
        password.addActionListener(e -> prosesMasuk());

        TombolBulat daftar = new TombolBulat("Daftar sekarang", TombolBulat.Gaya.TAUTAN);
        daftar.addActionListener(e -> jendela.tampilkanDaftar());

        JPanel form = Ui.kolom(18,
                Ui.kolom(6, Ui.judul("Masuk ke akun Anda", 28), Ui.pudar("Selamat datang kembali di KlinikPro", 15)),
                isian("Email", email),
                isian("Password", password),
                lihat,
                pesanSalah,
                masuk,
                Ui.baris(4, Ui.pudar("Belum punya akun?", 14), daftar));
        form.setPreferredSize(new Dimension(400, 520));

        JPanel tengah = Ui.transparan(new GridBagLayout());
        tengah.add(form);
        add(tengah, BorderLayout.CENTER);
    }

    static JPanel isian(String label, JComponent kolom) {
        return Ui.kolom(8, Ui.teks(label, 14, Font.BOLD, Tema.TEKS), kolom);
    }

    /** Kosongkan isian, dan isi email bila baru saja mendaftar. */
    void siapkan(String emailTerisi) {
        email.setText(emailTerisi == null ? "" : emailTerisi);
        password.setText("");
        pesanSalah.setText(" ");
        (emailTerisi == null ? email : password).requestFocusInWindow();
    }

    private void prosesMasuk() {
        try {
            Pengguna pengguna = authController.login(email.getText(), new String(password.getPassword()));
            password.setText("");
            pesanSalah.setText(" ");
            jendela.masukAplikasi(pengguna);
        } catch (ValidasiException ex) {
            pesanSalah.setText(ex.getMessage());
        } catch (RuntimeException ex) {
            pesanSalah.setText("Tidak bisa terhubung ke database. Pastikan MySQL menyala.");
            ex.printStackTrace();
        }
    }
}