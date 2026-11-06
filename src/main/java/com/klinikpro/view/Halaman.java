package com.klinikpro.view;

/** Daftar halaman di menu samping, beserta judul dan ikonnya. */
public enum Halaman {
    BERANDA("Beranda", "beranda"),
    CARI_DOKTER("Cari Dokter", "cari"),
    JANJI_TEMU("Janji Temu", "kalender"),
    REKAM_MEDIS("Rekam Medis", "dokumen"),
    OBAT("Obat & Resep", "obat"),
    PEMBAYARAN("Pembayaran", "kartu"),
    ARTIKEL("Artikel Kesehatan", "buku"),
    PROFIL("Profil", "profil");

    public final String judul;
    public final String ikon;

    Halaman(String judul, String ikon) {
        this.judul = judul;
        this.ikon = ikon;
    }
}
