package com.example.proyekaplikasidonasi.ui.profile

import android.graphics.BitmapFactory
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_donasi.view.*
import kotlinx.android.synthetic.main.item_galang_donasi_saya.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

data class GalangDonasiSaya(
    var judul: String,
    var tanggal: String,
    var batasWaktu: String,
    var deskripsi: String,
    var targetDonasi: String,
    var minimalDonasi: String,
    var jumlahDonasiSaatIni: String,
    var idPenggalang: String
)

class adapterGalangDonasiSaya(private val listGDS :ArrayList<GalangDonasiSaya>) : RecyclerView.Adapter<adapterGalangDonasiSaya.ListViewHolder>(){
    private lateinit var mAuth: FirebaseAuth
    var listener : adapterDonasi.RecyclerViewClickListener?=null
    private var mStorageRef: StorageReference? = null

    interface RecyclerViewClickListener{
        fun itemKlik(view: View, dataGDS : GalangDonasiSaya){

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterGalangDonasiSaya.ListViewHolder {
        //TODO("Not yet implemented")
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_galang_donasi_saya, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterGalangDonasiSaya.ListViewHolder, position: Int) {
        //TODO("Not yet implemented")
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.email.toString()

        var dataGDS = listGDS[position]

        // Get Image From Firebase Storage
        val storage = FirebaseStorage.getInstance()
        val imageRef = storage.getReference().child("images/donasi/galang_test.jpg")

        val ONE_MEGABYTE : Long = 1024 * 1024
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            var bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            holder.imageShow.setImageBitmap(bitmap)
        }

        val db = FirebaseFirestore.getInstance()
        var namaPenyelenggara = ""

        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener {
                var data = it?.data as MutableMap<String, String>
                namaPenyelenggara = data.getValue("name").toString()
                holder.txtPenyelenggara.setText(namaPenyelenggara)
            }

        holder.txtJudulDonasi.setText(dataGDS.judul)
        var progressPercent = dataGDS.jumlahDonasiSaatIni.toInt() / dataGDS.targetDonasi.toInt() * 100
        holder.progressBar.progress = progressPercent

        // Convert uang ke rupiah
        val localID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localID)
        var uangSaatIni = formatRupiah.format(dataGDS.jumlahDonasiSaatIni.toInt())
        uangSaatIni = uangSaatIni.toString()
        uangSaatIni = uangSaatIni.substringAfterLast("p")

        holder.txtTerkumpul.setText("Rp. " + uangSaatIni)
        holder.txtBatasWaktu.setText(dataGDS.batasWaktu)
    }

    override fun getItemCount(): Int {
        //TODO("Not yet implemented")
        return listGDS.size
    }
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var txtJudulDonasi : TextView = itemView.item_gds_judul
        var txtPenyelenggara : TextView = itemView.item_gds_penyelenggara
        var progressBar : ProgressBar = itemView.item_gds_progressbar
        var txtTerkumpul : TextView = itemView.item_gds_terkumpul
        var txtBatasWaktu : TextView = itemView.item_gds_batas_waktu
        var imageShow : ImageView = itemView.item_gds_gambar
    }
}