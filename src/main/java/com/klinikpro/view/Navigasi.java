package com.klinikpro.view;

import com.klinikpro.model.Dokter;

/** Cara halaman berpindah ke halaman lain tanpa harus saling mengenal. */
public interface Navigasi {

    void buka(Halaman halaman);

    /** Buka Cari Dokter dan langsung cari kata kunci tertentu. */
    void cariDokter(String kataKunci);

    /** Buka Janji Temu dengan dokter tertentu sudah terpilih di form. */
    void buatJanji(Dokter dokter);

    void keluar();
}
