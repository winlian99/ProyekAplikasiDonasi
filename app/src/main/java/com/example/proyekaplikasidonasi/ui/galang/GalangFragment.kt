package com.example.proyekaplikasidonasi.ui.galang

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyekaplikasidonasi.R
import com.google.common.primitives.UnsignedBytes.toInt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_galang.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class GalangFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_galang, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        val db = FirebaseFirestore.getInstance()
        val dbCol = "galang_dana"
        val userId = "galang_test"

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
            description_title.setText(data.get("description_title").toString())
            description_date.setText(data.get("date").toString())
            description.setText(data.get("description").toString())
        }.addOnFailureListener{
            Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show()
        }
    }
}