package com.example.proyekaplikasidonasi.ui.galang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.donasi.Donasi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_galang.*
import java.text.DecimalFormat
import java.text.NumberFormat

class GalangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galang)

        val dataIntent = intent.getParcelableExtra<Donasi>("kirimDonasi")

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
            val c = ((a/b) * 100).toInt()

            progress_galang.setProgress(c)
            Log.d("Value of c: ", c.toString())
            jumlah_donasi.setText(data.get("donated_num").toString() + " donasi")
            batas_galang.setText("Berakhir pada: " + data.get("limit").toString())
            description_title.setText(data.get("name").toString())
            description_date.setText(data.get("date").toString())
            description.setText(data.get("description").toString())
        }.addOnFailureListener{
            Toast.makeText(this@GalangActivity,"Error", Toast.LENGTH_SHORT).show()
        }
    }
}