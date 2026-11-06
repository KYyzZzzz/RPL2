package com.klinikpro.view;

import com.klinikpro.controller.AuthController;
import com.klinikpro.controller.DokterController;
import com.klinikpro.controller.Sesi;
import com.klinikpro.model.Pengguna;
import com.klinikpro.view.kit.Tema;
import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** VIEW: jendela utama. Berganti antara halaman Masuk, Daftar, dan isi aplikasi. */
public class JendelaUtama extends JFrame {

    private final DokterController dokterController;
    private final CardLayout tumpukan = new CardLayout();
    private final JPanel akar = new JPanel(tumpukan);
    private final PanelMasuk panelMasuk;
    private KerangkaAplikasi kerangka;

    public JendelaUtama(AuthController authController, DokterController dokterController) {
        super("KlinikPro");
        this.dokterController = dokterController;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1280, 800);
        setLocationRelativeTo(null);

        panelMasuk = new PanelMasuk(authController, this);
        akar.setBackground(Tema.LATAR);
        akar.add(panelMasuk, "masuk");
        akar.add(new PanelDaftar(authController, this), "daftar");
        setContentPane(akar);
        tumpukan.show(akar, "masuk");
    }

    public void tampilkanMasuk(String emailTerisi) {
        panelMasuk.siapkan(emailTerisi);
        tumpukan.show(akar, "masuk");
    }

    public void tampilkanDaftar() {
        tumpukan.show(akar, "daftar");
    }

    /** Dipanggil setelah login berhasil. Isi aplikasi dibuat ulang untuk pengguna ini. */
    public void masukAplikasi(Pengguna pengguna) {
        Sesi.mulai(pengguna);
        if (kerangka != null) {
            akar.remove(kerangka);
        }
        kerangka = new KerangkaAplikasi(this, dokterController);
        akar.add(kerangka, "aplikasi");
        tumpukan.show(akar, "aplikasi");
    }

    public void keluar() {
        Sesi.akhiri();
        tampilkanMasuk(null);
    }
}