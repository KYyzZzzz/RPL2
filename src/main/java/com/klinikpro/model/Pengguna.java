package com.klinikpro.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** MODEL: akun pengguna (pasien), dipetakan ke tabel "pengguna". */
@Entity
@Table(name = "pengguna")
public class Pengguna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nama;
    private String email;

    @Column(name = "no_telp")
    private String noTelp;

    /** Berisi hash BCrypt, bukan password asli. */
    private String password;

    private String peran = "pasien";

    @Column(name = "tanggal_daftar", insertable = false, updatable = false)
    private LocalDateTime tanggalDaftar;

    protected Pengguna() {
    }

    public Pengguna(String nama, String email, String noTelp, String passwordHash) {
        this.nama = nama;
        this.email = email;
        this.noTelp = noTelp;
        this.password = passwordHash;
    }

    public Integer getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
    public String getNoTelp() { return noTelp == null ? "" : noTelp; }
    public String getPassword() { return password; }
    public String getPeran() { return peran; }
    public LocalDateTime getTanggalDaftar() { return tanggalDaftar; }

    public String getNamaDepan() {
        return nama.trim().split("\\s+")[0];
    }

    public String getInisial() {
        String[] kata = nama.trim().split("\\s+");
        String hasil = kata[0].substring(0, 1);
        if (kata.length > 1) hasil += kata[1].substring(0, 1);
        return hasil.toUpperCase();
    }
}