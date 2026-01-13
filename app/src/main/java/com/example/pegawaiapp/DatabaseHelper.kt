package com.example.pegawaiapp

import android.util.Log
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class DatabaseHelper {

    private val client = OkHttpClient()
    // Pastikan URL mengarah ke file PHP lo
    private val BASE_URL = "https://appocalypse.my.id/pegawai.php"

    // 1. Ambil Semua Data Pegawai (Read)
    fun getAllPegawai(callback: (List<Pegawai>) -> Unit) {
        val link = "$BASE_URL?proc=getdata"
        val req = Request.Builder().url(link).build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(emptyList())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        val dMentah = responseBody.string()
                        try {
                            val dMentahJson = JSONArray(dMentah)
                            val list = mutableListOf<Pegawai>()

                            for (i in 0 until dMentahJson.length()) {
                                val obj = dMentahJson.getJSONObject(i)
                                list.add(Pegawai(
                                    id = obj.optInt("id", 0),
                                    nip = obj.optString("nip", ""),
                                    nama = obj.optString("nama", ""),
                                    tanggalMasuk = obj.optString("tanggalMasuk", ""),
                                    jabatan = obj.optString("jabatan", ""),
                                    alamat = obj.optString("alamat", ""),
                                    kontak = obj.optString("kontak","")
                                ))
                            }
                            callback(list)
                        } catch (e: Exception) {
                            Log.e("SERVER_ERROR", "Gagal parsing JSON: ${e.message}")
                            callback(emptyList())
                        }
                    }
                } else {
                    callback(emptyList())
                }
            }
        })
    }

    // 2. Tambah Data Pegawai (Create)
    fun insertPegawai(p: Pegawai, onResult: (Boolean, String) -> Unit) {
        val link = "$BASE_URL?proc=in&nip=${p.nip}&nama=${p.nama}&tanggalMasuk=${p.tanggalMasuk}&jabatan=${p.jabatan}&alamat=${p.alamat}&kontak=${p.kontak}"
        val req = Request.Builder().url(link).build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false, "Koneksi Gagal")
            }

            override fun onResponse(call: Call, response: Response) {
                val resBody = response.body?.string() ?: ""
                try {
                    val json = JSONObject(resBody)
                    val status = json.optString("status")
                    val pesanError = json.optString("error")

                    if (status == "Sukses") {
                        onResult(true, "Berhasil Simpan")
                    } else {
                        onResult(false, pesanError)
                    }
                } catch (e: Exception) {
                    onResult(false, "Server Error")
                }
            }
        })
    }

    // 3. Edit Data Pegawai (Update) - Parameter disesuaikan biar gak merah
    fun updatePegawai(p: Pegawai, nipLama: String, onResult: (Boolean, String) -> Unit) {
        val link = "$BASE_URL?proc=ed&nip=${p.nip}&nama=${p.nama}&tanggalMasuk=${p.tanggalMasuk}&jabatan=${p.jabatan}&alamat=${p.alamat}&kontak=${p.kontak}&niplama=$nipLama"
        val req = Request.Builder().url(link).build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false, "Koneksi Gagal")
            }

            override fun onResponse(call: Call, response: Response) {
                val resBody = response.body?.string() ?: ""
                try {
                    val json = JSONObject(resBody)
                    val status = json.optString("status")
                    if (status == "Sukses") {
                        onResult(true, "Berhasil Update")
                    } else {
                        onResult(false, json.optString("error", "Gagal Update"))
                    }
                } catch (e: Exception) {
                    onResult(false, "Server Error")
                }
            }
        })
    }

    // 4. Hapus Data Pegawai (Delete) - Tambah callback biar MainActivity tau kapan harus refresh
    fun deletePegawai(nip: String, onResult: (Boolean) -> Unit) {
        val link = "$BASE_URL?proc=del&nip=$nip"
        val req = Request.Builder().url(link).build()

        client.newCall(req
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(false)
            }

            override fun onResponse(call: Call, response: Response) {
                onResult(response.isSuccessful)
            }
        })
    }
}