package com.example.proyekaplikasidonasi.ui.riwayat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.donasi.Donasi
import com.example.proyekaplikasidonasi.ui.donasi.adapterDonasi
import com.example.proyekaplikasidonasi.ui.profile.GalangDonasiSaya
import com.example.proyekaplikasidonasi.ui.profile.adapterGalangDonasiSaya
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile_galang_dana_saya.*
import kotlinx.android.synthetic.main.fragment_riwayat.*

class RiwayatFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    var arRiwayat : ArrayList<Riwayat> = arrayListOf()
    lateinit var adapter : adapterRiwayat

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_riwayat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tampilkanData()
    }

    private fun tampilkanData() {
        val db = FirebaseFirestore.getInstance()
        val dbCol = "history_donasi"
        val db1 = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.email.toString()

        db.collection(dbCol)
            .whereEqualTo("id_user", userId)
            .get()
            .addOnSuccessListener {
                    documents ->
                for (document in documents) {
                    var data = document.data as MutableMap<String, String>

                    var tempRiwayat = Riwayat(
                        data.getValue("id_donasi").toString(),
                        data.getValue("jumlah_donasi").toString(),
                        "Komentar:\r\n"+data.getValue("komentar").toString(),
                        "Berdonasi sebagai: "+data.getValue("nama_donatur").toString(),
                        data.getValue("tanggal_donasi").toString()
                    )
                    tempRiwayat
                    arRiwayat.add(tempRiwayat)
                }
                rvRiwayat.layoutManager = LinearLayoutManager(activity)
                adapter = adapterRiwayat(arRiwayat)
                rvRiwayat.adapter = adapter
            }
            .addOnFailureListener{
                Toast.makeText(context, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()
            }
    }
}