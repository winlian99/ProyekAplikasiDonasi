package com.example.proyekaplikasidonasi.ui.donasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekaplikasidonasi.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_donasi.view.*

data class Donasi(
    var judul : String,
    var tanggal : String,
    var batasWaktu : String,
    var deskripsi : String,
    var targetDonasi : String,
    var minimalDonasi : String,
    var jumlahDonasiSaatIni : String,
    var idPenggalang : String
)

class adapterDonasi (private val listDonasi : ArrayList<Donasi>) : RecyclerView.Adapter<adapterDonasi.ListViewHolder>(){

    var listener : RecyclerViewClickListener?=null

    interface RecyclerViewClickListener{
        fun itemKlik(view : View, dataDonasi : Donasi){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_donasi, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        var dataDonasi = listDonasi[position]

        val db = FirebaseFirestore.getInstance()
        val dbCol = "galang_dana"
        var namaPenyelenggara = ""

        db.collection(dbCol).document(dataDonasi.idPenggalang)
            .get()
            .addOnSuccessListener {
                var data = it?.data as MutableMap<String, String>
                namaPenyelenggara = data.getValue("name").toString()
            }

        holder.txtJudulDonasi.setText(dataDonasi.judul)
        holder.txtPenyelenggara.setText(namaPenyelenggara)
        var progressPercent = ((dataDonasi.targetDonasi.toInt() - dataDonasi.jumlahDonasiSaatIni.toInt()) / dataDonasi.targetDonasi.toInt()) * 100
        holder.progressBar.progress = progressPercent.toInt()
        holder.txtTerkumpul.setText(dataDonasi.jumlahDonasiSaatIni)
        holder.txtBatasWaktu.setText(dataDonasi.batasWaktu)

    }

    override fun getItemCount(): Int {
        return listDonasi.size
    }

    inner class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var txtJudulDonasi : TextView = itemView.item_donasi_judul
        var txtPenyelenggara : TextView = itemView.item_donasi_penyelenggara
        var progressBar : ProgressBar = itemView.item_donasi_progressbar
        var txtTerkumpul : TextView = itemView.item_donasi_terkumpul
        var txtBatasWaktu : TextView = itemView.item_donasi_batas_waktu
    }

}