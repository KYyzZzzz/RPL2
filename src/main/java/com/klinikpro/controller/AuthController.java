package com.klinikpro.controller;

import com.klinikpro.model.Pengguna;
import com.klinikpro.repository.PenggunaRepository;
import java.util.regex.Pattern;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

/** CONTROLLER: aturan registrasi dan login. */
@Controller
public class AuthController {

    private static final Pattern POLA_EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern POLA_TELEPON = Pattern.compile("^(\\+62|0)8\\d{7,12}$");

    private final PenggunaRepository penggunaRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(PenggunaRepository penggunaRepository) {
        this.penggunaRepository = penggunaRepository;
    }

    public Pengguna registrasi(String nama, String email, String noTelp,
            String password, String konfirmasi) throws ValidasiException {
        nama = nama.trim();
        email = email.trim().toLowerCase();
        noTelp = noTelp.trim().replace(" ", "").replace("-", "");

        if (nama.length() < 3) throw new ValidasiException("Nama minimal 3 huruf.");
        if (!POLA_EMAIL.matcher(email).matches()) throw new ValidasiException("Format email tidak valid.");
        if (!POLA_TELEPON.matcher(noTelp).matches()) throw new ValidasiException("Nomor telepon harus diawali 08 atau +628.");
        if (password.length() < 8) throw new ValidasiException("Password minimal 8 karakter.");
        if (!password.equals(konfirmasi)) throw new ValidasiException("Konfirmasi password tidak sama.");
        if (penggunaRepository.existsByEmailIgnoreCase(email)) throw new ValidasiException("Email sudah terdaftar. Silakan masuk.");

        Pengguna baru = new Pengguna(nama, email, noTelp, encoder.encode(password));
        return penggunaRepository.save(baru);   // ORM: INSERT dibuat otomatis oleh Hibernate
    }

    public Pengguna login(String email, String password) throws ValidasiException {
        if (email.isBlank() || password.isEmpty()) throw new ValidasiException("Email dan password wajib diisi.");
        Pengguna p = penggunaRepository.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new ValidasiException("Email atau password salah."));
        if (!"pasien".equals(p.getPeran())) throw new ValidasiException("Akun ini bukan akun pasien.");
        if (!encoder.matches(password, p.getPassword())) throw new ValidasiException("Email atau password salah.");
        return p;
    }
}