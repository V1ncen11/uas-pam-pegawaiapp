package com.example.pegawaiapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pegawaiapp.databinding.ActivityAddPegawaiBinding
import java.util.*

class AddPegawaiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPegawaiBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPegawaiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper()

        // Klik pada EditText untuk memunculkan kalender
        binding.etTanggal.setOnClickListener {
            showDatePicker()
        }

        binding.btnSave.setOnClickListener {
            // 1. Mengambil teks dari semua inputan (Termasuk Alamat & Kontak)
            val nip = binding.etNip.text.toString().trim()
            val nama = binding.etNama.text.toString().trim()
            val tanggal = binding.etTanggal.text.toString().trim()
            val jabatan = binding.etJabatan.text.toString().trim()
            val alamat = binding.etAlamat.text.toString().trim() // Tambahan
            val kontak = binding.etKontak.text.toString().trim() // Tambahan

            // 2. Validasi Input Kosong
            if (nip.isEmpty()) {
                binding.etNip.error = "NIP tidak boleh kosong"
                return@setOnClickListener
            }
            if (nama.isEmpty()) {
                binding.etNama.error = "Nama tidak boleh kosong"
                return@setOnClickListener
            }

            // 3. Buat Objek Pegawai dengan data LENGKAP (6 Parameter)
            val peg = Pegawai(
                id = 0,
                nip = nip,
                nama = nama,
                tanggalMasuk = tanggal,
                jabatan = jabatan,
                alamat = alamat, // Data alamat dikirim
                kontak = kontak   // Data kontak dikirim
            )

            Toast.makeText(this, "Sedang menyimpan...", Toast.LENGTH_SHORT).show()

            // 4. Panggil Fungsi Insert ke Database
            db.insertPegawai(peg) { sukses, pesan ->
                runOnUiThread {
                    if (sukses) {
                        Toast.makeText(this, "Berhasil Simpan!", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal: $pesan", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val dpd = DatePickerDialog(this, { _, y, m, d ->
            val mm = m + 1
            // Format YYYY-MM-DD agar sinkron dengan database
            binding.etTanggal.setText(String.format("%04d-%02d-%02d", y, mm, d))
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
        dpd.show()
    }
}