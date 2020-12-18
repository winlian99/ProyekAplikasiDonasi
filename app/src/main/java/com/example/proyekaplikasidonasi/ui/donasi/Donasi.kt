package com.example.proyekaplikasidonasi.ui.donasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekaplikasidonasi.R
import kotlinx.android.synthetic.main.item_donasi.view.*

data class Donasi(
    var nama : String,
    var tanggal : String,
    var batasWaktu : String,
    var deskripsi : String,
    var targetDonasi : String,
    var minimalDonasi : String,
    var jumlahDonasiSaatIni : String,
    var idPenggalang : String
)

class adapterDonasi (private val listDonasi : ArrayList<Donasi>) : RecyclerView.Adapter<adapterDonasi.ListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_donasi, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var dataDonasi = listDonasi[position]
    }

    override fun getItemCount(): Int {
        return listDonasi.size
    }

    inner class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var txtJudulDonasi : TextView = itemView.item_donasi_judul
        var txtPenyelenggara : TextView = itemView.item_donasi_penyelenggara
        var progressBar : ProgressBar = itemView.item_donasi_progressbar
        var txtTerkumpul : TextView = itemView.item_donasi_terkumpul
        var txtSisaHari : TextView = itemView.item_donasi_sisa_hari
    }

}