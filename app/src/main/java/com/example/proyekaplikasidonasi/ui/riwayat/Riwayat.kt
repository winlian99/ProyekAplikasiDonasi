package com.example.proyekaplikasidonasi.ui.riwayat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.donasi.Donasi
import com.example.proyekaplikasidonasi.ui.donasi.adapterDonasi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_donasi.view.*
import kotlinx.android.synthetic.main.item_riwayat.view.*
import java.text.DecimalFormat
import java.text.NumberFormat

data class Riwayat (
    var judul : String,
    var jumlah_donasi : String,
    var komentar : String,
    var nama_donatur: String,
    var tanggal_donasi: String
)

class adapterRiwayat(private val listRiwayat : ArrayList<Riwayat>)  :RecyclerView.Adapter<adapterRiwayat.ListViewHolder>(){
    private lateinit var mAuth: FirebaseAuth

    interface RecyclerViewClickListener{
        fun itemKlik(view: View, dataDonasi: Donasi){

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterRiwayat.ListViewHolder {
        //TODO("Not yet implemented")
        val view : View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_riwayat,
            parent,
            false
        )
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterRiwayat.ListViewHolder, position: Int) {
        //TODO("Not yet implemented")
        var dataRiwayat = listRiwayat[position]
        val db  = FirebaseFirestore.getInstance()
        val db1  = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.email.toString()

        db.collection("galang_dana")
            .document(dataRiwayat.judul)
            .get()
            .addOnSuccessListener {
                val temp = it.data as MutableMap<String, String>
                holder.txtJudulDonasi.setText(temp.getValue("name").toString())
                Log.d("Textttt",temp.getValue("name").toString())
            }

        val format: NumberFormat = DecimalFormat("#,###")
        val tempDonasi = dataRiwayat.jumlah_donasi.toInt()

        holder.txtJumlahDonasi.setText("Rp. " + format.format(tempDonasi).toString())
        holder.txtKomentar.setText(dataRiwayat.komentar)
        holder.txtTanggalDonasi.setText(dataRiwayat.tanggal_donasi)
        holder.txtNamaDonatur.setText(dataRiwayat.nama_donatur)

    }

    override fun getItemCount(): Int {
        //TODO("Not yet implemented")
        return listRiwayat.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var txtJudulDonasi : TextView = itemView.item_riwayat_judul
        var txtJumlahDonasi : TextView = itemView.item_riwayat_jumlah_donasi
        var txtKomentar : TextView = itemView.item_riwayat_komentar
        var txtNamaDonatur : TextView = itemView.item_riwayat_nama_donatur
        var txtTanggalDonasi : TextView = itemView.item_riwayat_tanggal_donasi
    }
}