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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/** VIEW: halaman Daftar (registrasi akun pasien baru). */
class PanelDaftar extends JPanel {

    private final AuthController authController;
    private final JendelaUtama jendela;
    private final JTextField nama = KolomIsian.teks("Nama sesuai KTP");
    private final JTextField email = KolomIsian.teks("nama@email.com");
    private final JTextField telepon = KolomIsian.teks("08xxxxxxxxxx");
    private final JPasswordField password = KolomIsian.password("Minimal 8 karakter");
    private final JPasswordField konfirmasi = KolomIsian.password("Ulangi password");
    private final JLabel pesanSalah = Ui.teks(" ", 14, Font.BOLD, Tema.BAHAYA);

    PanelDaftar(AuthController authController, JendelaUtama jendela) {
        super(new BorderLayout());
        this.authController = authController;
        this.jendela = jendela;
        setBackground(Tema.LATAR);

        add(new PanelSambutan("Buat akun dan mulai berobat dari rumah",
                "Setelah mendaftar, Anda bisa mencari dokter dan mengambil nomor antrean tanpa datang lebih dulu."),
                BorderLayout.WEST);

        TombolBulat daftar = new TombolBulat("Daftar", TombolBulat.Gaya.UTAMA);
        daftar.setPreferredSize(new Dimension(420, 50));
        daftar.addActionListener(e -> prosesDaftar());
        konfirmasi.addActionListener(e -> prosesDaftar());

        TombolBulat masuk = new TombolBulat("Masuk", TombolBulat.Gaya.TAUTAN);
        masuk.addActionListener(e -> {
            bersihkan();
            jendela.tampilkanMasuk(null);
        });

        JPanel form = Ui.kolom(14,
                Ui.kolom(6, Ui.judul("Buat akun baru", 28), Ui.pudar("Isi data diri Anda dengan benar", 15)),
                PanelMasuk.isian("Nama lengkap", nama),
                Ui.grid(2, 14, PanelMasuk.isian("Email", email), PanelMasuk.isian("No. telepon", telepon)),
                Ui.grid(2, 14, PanelMasuk.isian("Password", password), PanelMasuk.isian("Ulangi password", konfirmasi)),
                pesanSalah,
                daftar,
                Ui.baris(4, Ui.pudar("Sudah punya akun?", 14), masuk));
        form.setPreferredSize(new Dimension(420, 560));

        JPanel tengah = Ui.transparan(new GridBagLayout());
        tengah.add(form);
        add(tengah, BorderLayout.CENTER);
    }

    private void prosesDaftar() {
        try {
            Pengguna baru = authController.registrasi(nama.getText(), email.getText(), telepon.getText(),
                    new String(password.getPassword()), new String(konfirmasi.getPassword()));
            JOptionPane.showMessageDialog(this,
                    "Akun untuk " + baru.getNama() + " berhasil dibuat.\nSilakan masuk dengan email dan password Anda.",
                    "Pendaftaran berhasil", JOptionPane.INFORMATION_MESSAGE);
            bersihkan();
            jendela.tampilkanMasuk(baru.getEmail());
        } catch (ValidasiException ex) {
            pesanSalah.setText(ex.getMessage());
        } catch (RuntimeException ex) {
            pesanSalah.setText("Tidak bisa terhubung ke database. Pastikan MySQL menyala.");
            ex.printStackTrace();
        }
    }

    private void bersihkan() {
        nama.setText("");
        email.setText("");
        telepon.setText("");
        password.setText("");
        konfirmasi.setText("");
        pesanSalah.setText(" ");
    }
}