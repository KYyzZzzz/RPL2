package com.klinikpro.controller;

/** Kesalahan input yang pesannya aman ditampilkan ke pengguna. */
public class ValidasiException extends Exception {
    public ValidasiException(String pesan) {
        super(pesan);
    }
}