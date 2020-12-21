package com.example.proyekaplikasidonasi.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.donasi.Donasi
import com.example.proyekaplikasidonasi.ui.donasi.adapterDonasi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile_galang_dana_saya.*
import kotlinx.android.synthetic.main.fragment_donasi.*

class ProfileGalangDanaSayaActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    var arGDS : ArrayList<GalangDonasiSaya> = arrayListOf()
    lateinit var adapter : adapterGalangDonasiSaya
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_galang_dana_saya)

        tampilData()
    }

    private fun tampilData() {
        val db = FirebaseFirestore.getInstance()
        val dbCol = "galang_dana"
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.email.toString()
        db.collection(dbCol)
            .whereEqualTo("id_penggalang",userId )
            .get()
            .addOnSuccessListener {
                    documents ->
                    for (document in documents) {
                    var data = document.data as MutableMap<String, String>
                    var tempGDS = GalangDonasiSaya(
                        data.getValue("description_title").toString(),
                        data.getValue("date").toString(),
                        data.getValue("limit").toString(),
                        data.getValue("description").toString(),
                        data.getValue("target").toString(),
                        data.getValue("minimal").toString(),
                        data.getValue("donated_cash").toString(),
                        data.getValue("id_penggalang").toString()
                    )
                    arGDS.add(tempGDS)
                }
                rvDaftarGalangSaya.layoutManager = LinearLayoutManager(this)
                adapter = adapterGalangDonasiSaya(arGDS)
                rvDaftarGalangSaya.adapter = adapter
            }
    }
}