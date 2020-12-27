package com.example.proyekaplikasidonasi.ui.galang

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.donasi.adapterDonasi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_history_komen_galang.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

data class Donatur(
    var id_history_donasi: String,
    var id_donasi: String,
    var jumlah_donasi: String,
    var komentar: String,
    var nama_donatur: String,
    var tanggal_donasi: String
)

class adapterDonatur(private val listDonatur: ArrayList<Donatur>) :
    RecyclerView.Adapter<adapterDonatur.ListViewHolder>() {

    var listener: adapterDonasi.RecyclerViewClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterDonatur.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_history_komen_galang,
            parent,
            false
        )
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDonatur.size
    }

    override fun onBindViewHolder(holder: adapterDonatur.ListViewHolder, position: Int) {
        val dataDonatur = listDonatur[position]
        val db = FirebaseFirestore.getInstance()

        // Ambil dari history_donasi donasi yang memiliki id_donasi sesuai dengan document galang_dana
        holder.nama_donatur.setText(dataDonatur.nama_donatur)

        val localID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localID)
        var nomDonatur = formatRupiah.format(dataDonatur.jumlah_donasi.toInt())
        nomDonatur = nomDonatur.toString().substringAfterLast("p")

        holder.nominal_donatur.setText("Mendonasi Rp. " + nomDonatur)
        holder.tanggal_donatur.setText("Mendonasi pada tanggal " + dataDonatur.tanggal_donasi)
        holder.konten_donatur.setText(dataDonatur.komentar)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nama_donatur: TextView = itemView.nama_donatur_komen
        var nominal_donatur: TextView = itemView.nominal_donatur_komen
        var tanggal_donatur: TextView = itemView.tanggal_donatur_komen
        var konten_donatur: TextView = itemView.konten_komen_donatur
    }
}