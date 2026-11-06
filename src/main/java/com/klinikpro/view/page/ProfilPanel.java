package com.klinikpro.view.page;

import com.klinikpro.view.kit.Avatar;
import com.klinikpro.view.kit.PanelBulat;
import com.klinikpro.view.kit.PanelHalaman;
import com.klinikpro.view.kit.Tema;
import com.klinikpro.view.kit.Ui;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JComponent;

/** VIEW: halaman Profil, berisi data akun dan informasi tentang klinik. */
public class ProfilPanel extends PanelHalaman {

    // Ganti dengan data klinik yang sebenarnya.
    private static final String ALAMAT_KLINIK = "[Isi alamat klinik]";
    private static final String JAM_OPERASIONAL = "[Isi jam operasional]";
    private static final String TELEPON_KLINIK = "[Isi nomor telepon klinik]";

    public ProfilPanel() {
        add(Ui.kepalaHalaman("Profil", "Data akun Anda dan informasi klinik"), BorderLayout.NORTH);
        add(Ui.kolom(20, kartuAkun(), kartuKlinik()), BorderLayout.CENTER);
    }

    private JComponent kartuAkun() {
        // Data akun diisi dari tabel pengguna setelah fitur login dibuat (M4).
        PanelBulat kartu = PanelBulat.kartu(24);
        kartu.add(Ui.baris(16, new Avatar("?", 72, Tema.LATAR, Tema.TEKS_PUDAR),
                Ui.kolom(4, Ui.judul("Pengguna", 21),
                        Ui.pudar("Data akun akan tampil setelah fitur login tersedia.", 14))));
        return kartu;
    }

    private JComponent kartuKlinik() {
        PanelBulat kartu = PanelBulat.kartu(24);
        kartu.setLayout(new BorderLayout(0, 16));
        kartu.add(Ui.judul("Tentang Klinik", 17), BorderLayout.NORTH);
        kartu.add(Ui.grid(3, 16,
                info("Alamat", ALAMAT_KLINIK),
                info("Jam operasional", JAM_OPERASIONAL),
                info("Telepon", TELEPON_KLINIK)), BorderLayout.CENTER);
        return kartu;
    }

    private static JComponent info(String label, String nilai) {
        PanelBulat kotak = new PanelBulat(Tema.LATAR, null, 12, 14);
        kotak.add(Ui.kolom(4, Ui.pudar(label, 13), Ui.teks(nilai, 15, Font.BOLD, Tema.TEKS)));
        return kotak;
    }
}
