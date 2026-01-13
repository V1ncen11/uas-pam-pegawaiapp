package com.example.pegawaiapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pegawaiapp.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity(), PegawaiAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseHelper
    private lateinit var adapter: PegawaiAdapter

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        loadData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper()

        adapter = PegawaiAdapter(mutableListOf(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Search Real-time (React Style)
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.fabAdd.setOnClickListener {
            val i = Intent(this, AddPegawaiActivity::class.java)
            launcher.launch(i)
        }

        loadData()
    }

    private fun loadData() {
        db.getAllPegawai { listPegawai ->
            runOnUiThread {
                adapter.updateData(listPegawai)
                binding.tvEmpty.visibility = if (listPegawai.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    // Fungsi klik kartu untuk Detail
    override fun onDetail(pegawai: Pegawai) {
        val i = Intent(this, DetailPegawaiActivity::class.java)
        i.putExtra("nip", pegawai.nip)
        i.putExtra("nama", pegawai.nama)
        i.putExtra("jabatan", pegawai.jabatan)
        i.putExtra("tanggal", pegawai.tanggalMasuk)
        i.putExtra("alamat", pegawai.alamat)
        i.putExtra("kontak", pegawai.kontak)
        startActivity(i)
    }

    override fun onEdit(pegawai: Pegawai) {
        val i = Intent(this, EditPegawaiActivity::class.java)
        i.putExtra("nip_lama", pegawai.nip)
        i.putExtra("nip", pegawai.nip)
        i.putExtra("nama", pegawai.nama)
        i.putExtra("tanggal", pegawai.tanggalMasuk)
        i.putExtra("jabatan", pegawai.jabatan)
        i.putExtra("alamat", pegawai.alamat)
        i.putExtra("kontak", pegawai.kontak)
        launcher.launch(i)
    }

    override fun onDelete(pegawai: Pegawai) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Yakin mau hapus ${pegawai.nama}?")
            .setNegativeButton("Batal", null)
            .setPositiveButton("Hapus") { _, _ ->
                db.deletePegawai(pegawai.nip) { sukses ->
                    runOnUiThread {
                        if (sukses) {
                            Toast.makeText(this, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                            loadData()
                        } else {
                            Toast.makeText(this, "Gagal Hapus dari Server", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .show()
    }
}