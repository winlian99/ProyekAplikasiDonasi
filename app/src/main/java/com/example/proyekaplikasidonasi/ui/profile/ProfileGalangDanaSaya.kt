package com.example.proyekaplikasidonasi.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.donasi.Donasi
import com.example.proyekaplikasidonasi.ui.donasi.adapterDonasi
import com.example.proyekaplikasidonasi.ui.galang.GalangActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile_galang_dana_saya.*
import kotlinx.android.synthetic.main.fragment_donasi.*

class ProfileGalangDanaSaya : AppCompatActivity(), adapterDonasi.RecyclerViewClickListener {

    var arDonasi : ArrayList<Donasi> = arrayListOf()
    lateinit var adapter : adapterDonasi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_galang_dana_saya)

        AmbilData()
    }

    override fun itemKlik(view: View, dataDonasi: Donasi) {
        super.itemKlik(view, dataDonasi)

        val intent = Intent(this@ProfileGalangDanaSaya, GalangActivity::class.java)
        intent.putExtra("kirimDonasi", dataDonasi)
        startActivity(intent)
    }

    private fun AmbilData(){
        val mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.email.toString()

        val db = FirebaseFirestore.getInstance()
        val dbCol = "galang_dana"

        db.collection(dbCol)
            .whereEqualTo("id_penggalang", userId)
            .get()
            .addOnSuccessListener {
                    documents ->
                for (document in documents) {
                    var data = document.data as MutableMap<String, String>
                    var tempDonasi = Donasi(
                        document.id,
                        data.getValue("name").toString(),
                        data.getValue("date").toString(),
                        data.getValue("limit").toString(),
                        data.getValue("description").toString(),
                        data.getValue("target").toString(),
                        data.getValue("minimal").toString(),
                        data.getValue("donated_cash").toString(),
                        data.getValue("donated_num").toString(),
                        data.getValue("id_penggalang").toString()
                    )
                    arDonasi.add(tempDonasi)
                }
                rvGalangDanaSaya.layoutManager = LinearLayoutManager(this)
                adapter = adapterDonasi(arDonasi)
                rvGalangDanaSaya.adapter = adapter

                adapter.listener = this
            }
            .addOnFailureListener {
                Toast.makeText(this@ProfileGalangDanaSaya, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
            }
    }
}