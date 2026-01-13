package com.example.pegawaiapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pegawaiapp.databinding.ActivityEditPegawaiBinding
import java.util.*

class EditPegawaiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPegawaiBinding
    private lateinit var db: DatabaseHelper
    private var nipLama: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPegawaiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper()

        // Ambil data dari Intent (MainActivity)
        nipLama = intent.getStringExtra("nip_lama") ?: ""
        binding.etNip.setText(intent.getStringExtra("nip"))
        binding.etNama.setText(intent.getStringExtra("nama"))
        binding.etJabatan.setText(intent.getStringExtra("jabatan"))
        binding.etTanggal.setText(intent.getStringExtra("tanggal"))
        binding.etAlamat.setText(intent.getStringExtra("alamat"))
        binding.etKontak.setText(intent.getStringExtra("kontak"))

        binding.etTanggal.setOnClickListener { showDatePicker() }

        binding.btnUpdate.setOnClickListener {
            val nip = binding.etNip.text.toString()
            val nama = binding.etNama.text.toString()
            val jab = binding.etJabatan.text.toString()
            val tgl = binding.etTanggal.text.toString()
            val alm = binding.etAlamat.text.toString()
            val knk = binding.etKontak.text.toString()

            if (nip.isEmpty() || nama.isEmpty()) {
                Toast.makeText(this, "NIP dan Nama wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val p = Pegawai(0, nip, nama, tgl, jab, alm, knk)

            db.updatePegawai(p, nipLama) { sukses, pesan ->
                runOnUiThread {
                    if (sukses) {
                        Toast.makeText(this, "Berhasil Update!", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal: $pesan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val dpd = DatePickerDialog(this, { _, y, m, d ->
            binding.etTanggal.setText(String.format("%04d-%02d-%02d", y, m + 1, d))
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
        dpd.show()
    }
}