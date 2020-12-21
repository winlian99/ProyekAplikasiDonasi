package com.example.proyekaplikasidonasi.ui.galang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.donasi.Donasi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_berdonasi.*
import kotlinx.android.synthetic.main.activity_galang.*
import java.text.SimpleDateFormat
import java.util.*

class BerdonasiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_berdonasi)

        val data_intent = intent.getParcelableExtra<Donasi>("kirimDonasi")

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dbColDonasi = "history_donasi"
        val sdf = SimpleDateFormat("DD/MM/YYYY")
        val current_date = sdf.format(Date()).toString()

        // Need info from both the page and the parcelable
        // Page is person that donates, parcel is galang
        // Save the donated data to history_donasi
        // Process the whole exchange

        final_donation_button.setOnClickListener {
            // Need to create new donation
            // And also edit existing money data of galang and current user
            val editText_money: EditText = findViewById(R.id.donated_money)

            val donator_money: Float = editText_money.text.toString().toFloat() // Input from page
            val email_user: String = mAuth.currentUser?.email.toString() // Get email to get balance
            var current_money_user: Float = 0.0F // This is for the calculated balance
            var current_money_galang: Float = 0.0F // This is for galang
            var dNum: Int = 0

            // To decrease the money of the user and add the one in galang
            db.collection("users").document(email_user).get().addOnSuccessListener {
                val data = it?.data as MutableMap<String, String>

                current_money_user = data.get("balance").toString().toFloat()
                current_money_galang = data_intent.jumlahDonasiSaatIni.toFloat()

            }

            if (donator_money <= current_money_user) {
                if (donator_money >= data_intent.minimalDonasi.toFloat()) {
                    current_money_user -= donator_money
                    current_money_galang += donator_money


                    var donator_name: String = nama_donatur.text.toString()
                    if (donator_name == "") {
                        donator_name = "Anonim"
                    }

                    val data = hashMapOf(
                        "id_donasi" to data_intent.idDonasi,
                        "id_user" to mAuth.currentUser?.email.toString(),
                        "jumlah_donasi" to donator_money,
                        "komentar" to komen_donasi.text.toString(),
                        "nama_donatur" to donator_name,
                        "tanggal_donasi" to current_date
                    )

                    // Adding +1 to the donation counter
                    db.collection("galang_dana").document(data_intent.idDonasi).get().addOnSuccessListener {
                        val data = it?.data as MutableMap<String, String>
                        dNum = data.get("donated_num").toString().toInt() + 1
                    }

                    // All of the updates and creates
                    db.collection("dbColDonasi").add(data)

                    val current_user = db.collection("users").document(email_user)
                    val galang_dana = db.collection("galang_dana").document(data_intent.idDonasi)
                    galang_dana.update("donated_cash", current_money_galang.toString())
                    galang_dana.update("donated_num", dNum.toString())
                    current_user.update("balance", current_money_user.toString())
                } else {
                    Toast.makeText(this@BerdonasiActivity ,"Uang dibawah batas minimal donasi", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@BerdonasiActivity,"Uang anda tidak cukup", Toast.LENGTH_SHORT).show()
            }
        }
    }
}