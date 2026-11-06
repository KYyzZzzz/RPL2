package com.klinikpro.view;

/** Cara halaman berpindah ke halaman lain tanpa harus saling mengenal. */
public interface Navigasi {

    void buka(Halaman halaman);

    /** Buka Cari Dokter dan langsung cari kata kunci tertentu. */
    void cariDokter(String kataKunci);

    void keluar();
}
