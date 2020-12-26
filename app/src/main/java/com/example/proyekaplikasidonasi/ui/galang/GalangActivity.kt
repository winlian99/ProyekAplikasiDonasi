package com.example.proyekaplikasidonasi.ui.galang

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.donasi.Donasi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_galang.*
import java.text.DecimalFormat
import java.text.NumberFormat

class GalangActivity : AppCompatActivity() {

    var arDonatur: ArrayList<Donatur> = arrayListOf()
    lateinit var adapter: adapterDonatur

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.activity_galang , container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galang)

        val dataIntent = intent.getParcelableExtra<Donasi>("kirimDonasi")
        Log.d("Cek dataIntent", dataIntent.idPenggalang.toString())

        val mAuth = FirebaseAuth.getInstance()

        val db = FirebaseFirestore.getInstance()
        val dbCol = "galang_dana"
        var userId = ""
        if (dataIntent.idDonasi != null) {
            userId = dataIntent.idDonasi
        }

        db.collection(dbCol).document(userId).get().addOnSuccessListener {
            var data = it?.data as MutableMap<String, String>
            judul_galang.setText(data.get("name").toString())

            val donasi: Double = data.get("donated_cash").toString().toDouble()
            val tDonasi: Double = data.get("target").toString().toDouble()
            val format: NumberFormat = DecimalFormat("#,###")
            val finalDonasi: String = "Rp. " + format.format(donasi).toString()
            val targetDonasi: String = " terkumpul dari Rp. " + format.format(tDonasi).toString()

            donasi_sekarang.setText(finalDonasi)
            target_donasi.setText(targetDonasi)

            val a = data.get("donated_cash").toString().toFloat()
            val b = data.get("target").toString().toFloat()
            val c = ((a / b) * 100).toInt()

            progress_galang.setProgress(c)
            Log.d("Value of c: ", c.toString())
            jumlah_donasi.setText(data.get("donated_num").toString() + " donasi")
            batas_galang.setText("Berakhir pada: " + data.get("limit").toString())
            description_title.setText(data.get("name").toString())
            description_date.setText(data.get("date").toString())
            description.setText(data.get("description").toString())
        }.addOnFailureListener {
            Toast.makeText(this@GalangActivity, "Error", Toast.LENGTH_SHORT).show()
        }

        donation_button.setOnClickListener {
            val intent = Intent(this@GalangActivity, BerdonasiActivity::class.java)
            intent.putExtra("kirimDonasi", dataIntent)
            startActivity(intent)
        }

        ambilData(dataIntent.idDonasi.toString())
    }

    private fun ambilData(pass_id_donasi: String) {
        val db = FirebaseFirestore.getInstance()
        val dbCol = "history_donasi"

        // This is where you check for the same thingy
        db.collection(dbCol).get().addOnSuccessListener {
            documents ->
            for (doc in documents) {
                val data = doc.data as MutableMap<String, String>
                val tempDonatur = Donatur(doc.id,
                    data.getValue("id_donasi").toString(),
                    data.getValue("jumlah_donasi").toString(),
                    data.getValue("komentar").toString(),
                    data.getValue("nama_donatur").toString(),
                    data.getValue("tanggal_donasi").toString(),
                )
                if (tempDonatur.id_donasi == pass_id_donasi) {
                    arDonatur.add(tempDonatur)
                }
            }
            rv_komen_galang.layoutManager = LinearLayoutManager(this)
            adapter = adapterDonatur(arDonatur)
            rv_komen_galang.adapter = adapter
        }.addOnFailureListener{
            Toast.makeText(this@GalangActivity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
        }
    }
}