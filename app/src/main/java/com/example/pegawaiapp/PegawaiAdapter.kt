package com.example.pegawaiapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pegawaiapp.databinding.CardItemPegawaiBinding

class PegawaiAdapter(
    private var listPegawai: MutableList<Pegawai>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PegawaiAdapter.PegawaiViewHolder>() {

    // List cadangan untuk menyimpan data asli dari server demi fitur search
    private var listFull = mutableListOf<Pegawai>()

    // Interface untuk menangani klik Edit, Hapus, dan Detail
    interface OnItemClickListener {
        fun onEdit(pegawai: Pegawai)
        fun onDelete(pegawai: Pegawai)
        fun onDetail(pegawai: Pegawai) // Tambahan baru biar gak merah
    }

    inner class PegawaiViewHolder(val binding: CardItemPegawaiBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PegawaiViewHolder {
        val binding = CardItemPegawaiBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PegawaiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PegawaiViewHolder, position: Int) {
        val p = listPegawai[position]

        // Menampilkan data ke UI kartu
        holder.binding.tvNama.text = p.nama
        holder.binding.tvJabatan.text = p.jabatan
        holder.binding.tvNip.text = "NIP: ${p.nip}"
        holder.binding.tvTanggal.text = "📅 Masuk: ${p.tanggalMasuk}"

        // Klik seluruh area Kartu (Detail Profil)
        holder.binding.root.setOnClickListener {
            listener.onDetail(p)
        }

        // Klik tombol Edit
        holder.binding.btnEdit.setOnClickListener {
            listener.onEdit(p)
        }

        // Klik tombol Hapus
        holder.binding.btnHapus.setOnClickListener {
            listener.onDelete(p)
        }
    }

    override fun getItemCount(): Int = listPegawai.size

    // Memperbarui data dan mencadangkan data asli
    fun updateData(newList: List<Pegawai>) {
        listFull = newList.toMutableList()
        listPegawai.clear()
        listPegawai.addAll(newList)
        notifyDataSetChanged()
    }

    // Logika Pencarian Real-time (React Style)
    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            listFull
        } else {
            listFull.filter {
                it.nama.contains(query, ignoreCase = true) ||
                        it.nip.contains(query)
            }
        }
        listPegawai.clear()
        listPegawai.addAll(filteredList)
        notifyDataSetChanged()
    }
}