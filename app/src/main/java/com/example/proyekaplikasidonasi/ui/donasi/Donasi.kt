package com.example.proyekaplikasidonasi.ui.donasi

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyekaplikasidonasi.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_donasi.view.*
import java.io.File
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


data class Donasi(
    var judul: String,
    var tanggal: String,
    var batasWaktu: String,
    var deskripsi: String,
    var targetDonasi: String,
    var minimalDonasi: String,
    var jumlahDonasiSaatIni: String,
    var idPenggalang: String
)

class adapterDonasi(private val listDonasi: ArrayList<Donasi>) : RecyclerView.Adapter<adapterDonasi.ListViewHolder>(){

    var listener : RecyclerViewClickListener?=null
    private var mStorageRef: StorageReference? = null

    interface RecyclerViewClickListener{
        fun itemKlik(view: View, dataDonasi: Donasi){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_donasi,
            parent,
            false
        )
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var dataDonasi = listDonasi[position]

        // Get Image From Firebase Storage
        val storage = FirebaseStorage.getInstance()
        val imageRef = storage.getReference().child("images/donasi/galang_test.jpg")

        val ONE_MEGABYTE : Long = 1024 * 1024
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            var bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            holder.imageShow.setImageBitmap(bitmap)
        }

//        val storageRef = storage.reference
//        val pathReference = storageRef.child("images/donasi/galang_test.jpg")
//
//        val ONE_MEGABYTE : Long = 1024 * 1024
//        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {
//            holder.imageShow.setImageResource(BitmapFactory.decodeByteArray())
//        }

//        mStorageRef = FirebaseStorage.getInstance().getReference()
//        val pathReference : StorageReference = mStorageRef.child("images/rivers.jpg")
//        val localFile : File = File.createTempFile("images", "jpg")
//        riversRef.getFile(localFile)
//            .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot?> {
//                // Successfully downloaded data to local file
//                // ...
//
//            }).addOnFailureListener(OnFailureListener {
//                // Handle failed download
//                // ...
//            })

        val db = FirebaseFirestore.getInstance()
        var namaPenyelenggara = ""

        db.collection("users").document(dataDonasi.idPenggalang)
            .get()
            .addOnSuccessListener {
                var data = it?.data as MutableMap<String, String>
                namaPenyelenggara = data.getValue("name").toString()
                holder.txtPenyelenggara.setText(namaPenyelenggara)
            }

        holder.txtJudulDonasi.setText(dataDonasi.judul)
        var progressPercent = dataDonasi.jumlahDonasiSaatIni.toInt() / dataDonasi.targetDonasi.toInt() * 100
        holder.progressBar.progress = progressPercent

        // Convert uang ke rupiah
        val localID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localID)
        var uangSaatIni = formatRupiah.format(dataDonasi.jumlahDonasiSaatIni.toInt())
        uangSaatIni = uangSaatIni.toString()
        uangSaatIni = uangSaatIni.substringAfterLast("p")

        holder.txtTerkumpul.setText("Rp. " + uangSaatIni)
        holder.txtBatasWaktu.setText(dataDonasi.batasWaktu)

    }

    override fun getItemCount(): Int {
        return listDonasi.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var txtJudulDonasi : TextView = itemView.item_donasi_judul
        var txtPenyelenggara : TextView = itemView.item_donasi_penyelenggara
        var progressBar : ProgressBar = itemView.item_donasi_progressbar
        var txtTerkumpul : TextView = itemView.item_donasi_terkumpul
        var txtBatasWaktu : TextView = itemView.item_donasi_batas_waktu
        var imageShow : ImageView = itemView.item_donasi_gambar
    }

}