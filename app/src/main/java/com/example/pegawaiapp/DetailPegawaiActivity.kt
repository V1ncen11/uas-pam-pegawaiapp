package com.example.pegawaiapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pegawaiapp.databinding.ActivityDetailPegawaiBinding

class DetailPegawaiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPegawaiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inisialisasi View Binding
        binding = ActivityDetailPegawaiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Setting Toolbar (Header)
        supportActionBar?.hide() // Gue saranin hide kalau lo pake layout header gelap yang keren tadi

        // 3. Ambil data dari Intent
        val nama = intent.getStringExtra("nama") ?: "-"
        val jabatan = intent.getStringExtra("jabatan") ?: "-"
        val nip = intent.getStringExtra("nip") ?: "-"
        val tanggal = intent.getStringExtra("tanggal") ?: "-"
        val alamat = intent.getStringExtra("alamat") ?: "-"
        val kontak = intent.getStringExtra("kontak") ?: ""

        // 4. Tampilkan data ke TextView
        binding.tvDetailNama.text = nama
        binding.tvDetailJabatan.text = jabatan
        binding.tvDetailNip.text = nip
        binding.tvDetailTanggal.text = tanggal
        binding.tvDetailAlamat.text = alamat
        binding.tvDetailKontak.text = kontak

        // 5. FITUR WHATSAPP: Klik kontak langsung buka chat
        binding.tvDetailKontak.setOnClickListener {
            if (kontak.isNotEmpty()) {
                // Hapus karakter selain angka (misal spasi atau tanda strip)
                var cleanNumber = kontak.replace(Regex("[^0-9]"), "")

                // Trik Sakti: Ubah awalan '0' jadi '62' biar WA kenal nomornya
                if (cleanNumber.startsWith("0")) {
                    cleanNumber = "62" + cleanNumber.substring(1)
                }

                val url = "https://api.whatsapp.com/send?phone=$cleanNumber"

                try {
                    val intentWa = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    // Paksa buka aplikasi WhatsApp biar gak nanya buka pake browser apa
                    intentWa.setPackage("com.whatsapp")
                    startActivity(intentWa)
                } catch (e: Exception) {
                    // Kalau WA gak ada, buka pake Browser sebagai cadangan
                    val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intentBrowser)
                }
            } else {
                Toast.makeText(this, "Nomor kontak kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi tombol back (jika action bar tidak di-hide)
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}