package com.example.proyekaplikasidonasi.ui.buatDonasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyekaplikasidonasi.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_buat_donasi.*
import java.text.SimpleDateFormat
import java.util.*


class BuatDonasiFragment : Fragment() {

    private lateinit var mAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buat_donasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.email.toString()

        buat_donasi_tambah_button.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val dbCol = "galang_dana"

            val descriptionTemp = if (buat_donasi_deskripsi.text.toString() != "") buat_donasi_deskripsi.text.toString() else ""

            val sdf = SimpleDateFormat("yyyyMMdd")
            val currentDate = sdf.format(Date())
            val sdf2 = SimpleDateFormat("dd/MM/yyyy")
            val currentDate2 = sdf2.format(Date())
            val limitDateTemp = buat_donasi_limit_date.year.toString() + buat_donasi_limit_date.month.toString() + buat_donasi_limit_date.dayOfMonth.toString()

            if(buat_donasi_target_donasi.text.toString().toInt() < 0){
                Toast.makeText(context, "Target donasi yang dimasukkan salah", Toast.LENGTH_SHORT).show()
            }
            else if (buat_donasi_min_donasi.text.toString().toInt() <= 0){
                Toast.makeText(context, "Minimal donasi yang dimasukkan salah", Toast.LENGTH_SHORT).show()
            }
            else if (currentDate.toInt() > limitDateTemp.toInt()){
                Toast.makeText(context, "Tanggal batas yang dimasukkan salah", Toast.LENGTH_SHORT).show()
            }
            else {
                val data = hashMapOf(
                    "name" to buat_donasi_judul.text.toString(),
                    "target" to buat_donasi_target_donasi.text.toString(),
                    "limit" to buat_donasi_limit_date.dayOfMonth.toString() + "/" + buat_donasi_limit_date.month.toString() + "/" + buat_donasi_limit_date.year.toString(),
                    "minimal" to buat_donasi_min_donasi.text.toString(),
                    "id_penggalang" to userId,
                    "donated_num" to "0",
                    "donated_cash" to "0",
                    "description" to descriptionTemp,
                    "date" to currentDate2,
                )

                db.collection(dbCol).add(data)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Berhasil Menggalang Donasi", Toast.LENGTH_SHORT)
                            .show()
                        buat_donasi_judul.text.clear()
                        buat_donasi_target_donasi.text.clear()
                        buat_donasi_min_donasi.text.clear()
                        buat_donasi_deskripsi.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Gagal Menggalang Donasi", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}