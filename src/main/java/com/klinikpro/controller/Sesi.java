package com.klinikpro.controller;

import com.klinikpro.model.Pengguna;

/** Menyimpan pengguna yang sedang login selama aplikasi berjalan. */
public final class Sesi {
    private static Pengguna penggunaAktif;

    private Sesi() {
    }

    public static void mulai(Pengguna p) { penggunaAktif = p; }
    public static void akhiri() { penggunaAktif = null; }
    public static Pengguna pengguna() { return penggunaAktif; }
}