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
            text_progress.setText("Rp. " + data.get("donated_cash").toString()
                    + " terkumpul dari Rp. " + data.get("target").toString())
            val a = data.get("donated_cash").toString().toFloat()
            val b = data.get("target").toString().toFloat()
            val c = ((25000000/b) * 100).toInt()
            progress_galang.setProgress(c)
            Log.d("Value of c: ", c.toString())
            jumlah_donasi.setText(data.get("donated_num").toString())
            batas_galang.setText("Berakhir pada tanggal " + data.get("limit").toString())
            description_title.setText(data.get("description_title").toString())
            description_date.setText(data.get("date").toString())
            description.setText(data.get("description").toString())
        }.addOnFailureListener{
            Toast.makeText(context,"Error", Toast.LENGTH_SHORT).show()
        }
    }
}