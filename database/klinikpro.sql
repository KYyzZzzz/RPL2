-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 06 Nov 2026 pada 03.53
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `klinikpro`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `dokter`
--

CREATE TABLE `dokter` (
  `id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `spesialis` varchar(50) NOT NULL,
  `deskripsi` text DEFAULT NULL,
  `tarif` decimal(12,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `dokter`
--

INSERT INTO `dokter` (`id`, `nama`, `spesialis`, `deskripsi`, `tarif`) VALUES
(1, 'dr. Andika Abner', 'Umum', 'Dokter umum berpengalaman 8 tahun.', 75000.00),
(2, 'dr. Fitrah Naufal, Sp.A', 'Anak', 'Spesialis kesehatan anak.', 150000.00),
(3, 'dr. Farhan Aditya, Sp.PD', 'Penyakit Dalam', 'Spesialis penyakit dalam.', 175000.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `jadwal_dokter`
--

CREATE TABLE `jadwal_dokter` (
  `id` int(11) NOT NULL,
  `dokter_id` int(11) NOT NULL,
  `hari` enum('Senin','Selasa','Rabu','Kamis','Jumat','Sabtu','Minggu') NOT NULL,
  `jam_mulai` time NOT NULL,
  `jam_selesai` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `jadwal_dokter`
--

INSERT INTO `jadwal_dokter` (`id`, `dokter_id`, `hari`, `jam_mulai`, `jam_selesai`) VALUES
(1, 1, 'Senin', '08:00:00', '12:00:00'),
(2, 1, 'Rabu', '08:00:00', '12:00:00'),
(3, 2, 'Selasa', '09:00:00', '13:00:00'),
(4, 2, 'Kamis', '09:00:00', '13:00:00'),
(5, 3, 'Jumat', '13:00:00', '17:00:00'),
(6, 3, 'Sabtu', '09:00:00', '12:00:00');

-- --------------------------------------------------------

--
-- Struktur dari tabel `janji`
--

CREATE TABLE `janji` (
  `id` int(11) NOT NULL,
  `pengguna_id` int(11) NOT NULL,
  `dokter_id` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `jam` time NOT NULL,
  `no_antrean` int(11) NOT NULL,
  `keluhan` varchar(255) DEFAULT NULL,
  `status` enum('dijadwalkan','selesai','dibatalkan') NOT NULL DEFAULT 'dijadwalkan'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `obat`
--

CREATE TABLE `obat` (
  `id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `deskripsi` varchar(255) DEFAULT NULL,
  `harga` decimal(12,2) NOT NULL,
  `stok` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `obat`
--

INSERT INTO `obat` (`id`, `nama`, `deskripsi`, `harga`, `stok`) VALUES
(1, 'Paracetamol 500 mg', 'Penurun demam dan pereda nyeri', 5000.00, 100),
(2, 'Amoxicillin 500 mg', 'Antibiotik', 12000.00, 60),
(3, 'Vitamin C 500 mg', 'Suplemen daya tahan tubuh', 8000.00, 80),
(4, 'Antasida', 'Pereda sakit maag', 6000.00, 50);

-- --------------------------------------------------------

--
-- Struktur dari tabel `pembayaran`
--

CREATE TABLE `pembayaran` (
  `id` int(11) NOT NULL,
  `pengguna_id` int(11) NOT NULL,
  `janji_id` int(11) DEFAULT NULL,
  `pesanan_id` int(11) DEFAULT NULL,
  `total` decimal(12,2) NOT NULL,
  `metode` enum('tunai','transfer','e-wallet') DEFAULT NULL,
  `status` enum('belum_bayar','lunas') NOT NULL DEFAULT 'belum_bayar',
  `tanggal` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengguna`
--

CREATE TABLE `pengguna` (
  `id` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `no_telp` varchar(20) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `peran` enum('pasien','admin') NOT NULL DEFAULT 'pasien',
  `tanggal_daftar` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data untuk tabel `pengguna`
--

INSERT INTO `pengguna` (`id`, `nama`, `email`, `no_telp`, `password`, `peran`, `tanggal_daftar`) VALUES
(1, 'Admin Klinik', 'admin@klinik.com', '0800000000', 'admin123', 'admin', '2026-10-23 14:32:38');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pesanan_obat`
--

CREATE TABLE `pesanan_obat` (
  `id` int(11) NOT NULL,
  `pengguna_id` int(11) NOT NULL,
  `obat_id` int(11) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `tanggal` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `rekam_medis`
--

CREATE TABLE `rekam_medis` (
  `id` int(11) NOT NULL,
  `janji_id` int(11) NOT NULL,
  `keluhan` text DEFAULT NULL,
  `diagnosis` text DEFAULT NULL,
  `tindakan` text DEFAULT NULL,
  `catatan` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `resep`
--

CREATE TABLE `resep` (
  `id` int(11) NOT NULL,
  `rekam_medis_id` int(11) NOT NULL,
  `obat_id` int(11) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `aturan_pakai` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `dokter`
--
ALTER TABLE `dokter`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `jadwal_dokter`
--
ALTER TABLE `jadwal_dokter`
  ADD PRIMARY KEY (`id`),
  ADD KEY `dokter_id` (`dokter_id`);

--
-- Indeks untuk tabel `janji`
--
ALTER TABLE `janji`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `dokter_id` (`dokter_id`,`tanggal`,`jam`),
  ADD KEY `pengguna_id` (`pengguna_id`);

--
-- Indeks untuk tabel `obat`
--
ALTER TABLE `obat`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pengguna_id` (`pengguna_id`),
  ADD KEY `janji_id` (`janji_id`),
  ADD KEY `pesanan_id` (`pesanan_id`);

--
-- Indeks untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indeks untuk tabel `pesanan_obat`
--
ALTER TABLE `pesanan_obat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pengguna_id` (`pengguna_id`),
  ADD KEY `obat_id` (`obat_id`);

--
-- Indeks untuk tabel `rekam_medis`
--
ALTER TABLE `rekam_medis`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `janji_id` (`janji_id`);

--
-- Indeks untuk tabel `resep`
--
ALTER TABLE `resep`
  ADD PRIMARY KEY (`id`),
  ADD KEY `rekam_medis_id` (`rekam_medis_id`),
  ADD KEY `obat_id` (`obat_id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `dokter`
--
ALTER TABLE `dokter`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `jadwal_dokter`
--
ALTER TABLE `jadwal_dokter`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `janji`
--
ALTER TABLE `janji`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `obat`
--
ALTER TABLE `obat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `pembayaran`
--
ALTER TABLE `pembayaran`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `pesanan_obat`
--
ALTER TABLE `pesanan_obat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `rekam_medis`
--
ALTER TABLE `rekam_medis`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `resep`
--
ALTER TABLE `resep`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `jadwal_dokter`
--
ALTER TABLE `jadwal_dokter`
  ADD CONSTRAINT `jadwal_dokter_ibfk_1` FOREIGN KEY (`dokter_id`) REFERENCES `dokter` (`id`);

--
-- Ketidakleluasaan untuk tabel `janji`
--
ALTER TABLE `janji`
  ADD CONSTRAINT `janji_ibfk_1` FOREIGN KEY (`pengguna_id`) REFERENCES `pengguna` (`id`),
  ADD CONSTRAINT `janji_ibfk_2` FOREIGN KEY (`dokter_id`) REFERENCES `dokter` (`id`);

--
-- Ketidakleluasaan untuk tabel `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD CONSTRAINT `pembayaran_ibfk_1` FOREIGN KEY (`pengguna_id`) REFERENCES `pengguna` (`id`),
  ADD CONSTRAINT `pembayaran_ibfk_2` FOREIGN KEY (`janji_id`) REFERENCES `janji` (`id`),
  ADD CONSTRAINT `pembayaran_ibfk_3` FOREIGN KEY (`pesanan_id`) REFERENCES `pesanan_obat` (`id`);

--
-- Ketidakleluasaan untuk tabel `pesanan_obat`
--
ALTER TABLE `pesanan_obat`
  ADD CONSTRAINT `pesanan_obat_ibfk_1` FOREIGN KEY (`pengguna_id`) REFERENCES `pengguna` (`id`),
  ADD CONSTRAINT `pesanan_obat_ibfk_2` FOREIGN KEY (`obat_id`) REFERENCES `obat` (`id`);

--
-- Ketidakleluasaan untuk tabel `rekam_medis`
--
ALTER TABLE `rekam_medis`
  ADD CONSTRAINT `rekam_medis_ibfk_1` FOREIGN KEY (`janji_id`) REFERENCES `janji` (`id`);

--
-- Ketidakleluasaan untuk tabel `resep`
--
ALTER TABLE `resep`
  ADD CONSTRAINT `resep_ibfk_1` FOREIGN KEY (`rekam_medis_id`) REFERENCES `rekam_medis` (`id`),
  ADD CONSTRAINT `resep_ibfk_2` FOREIGN KEY (`obat_id`) REFERENCES `obat` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
