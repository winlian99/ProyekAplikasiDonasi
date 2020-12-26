package com.example.proyekaplikasidonasi.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyekaplikasidonasi.MainActivity
import com.example.proyekaplikasidonasi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile_topup.*
import java.text.NumberFormat
import java.util.*


class ProfileTopupActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_topup)

        mAuth = FirebaseAuth.getInstance()

        val db = FirebaseFirestore.getInstance()
        val dbCol = getString(R.string.dbColUsersCollection)
        val userId = mAuth.currentUser?.email.toString()

        var saldo= ""
        db.collection(dbCol).document(userId)
            .get()
            .addOnSuccessListener {
                var data = it?.data as MutableMap<String, String>
                saldo = data.getValue("balance").toString()
                val localID = Locale("in", "ID")
                val formatRupiah = NumberFormat.getCurrencyInstance(localID)
                var uangSaatIni = formatRupiah.format(saldo.toInt())
                uangSaatIni = uangSaatIni.toString()
                uangSaatIni = uangSaatIni.substringAfterLast("p")
                profile_saldo_sekarang.setText("Saldo Sekarang: Rp " + uangSaatIni)
            }
        profile_btn_topup.setOnClickListener{
            if(profile_nominal.text.toString() != ""){
                var nominal = profile_nominal.text.toString().toInt()
                var total = saldo.toInt() + nominal
                db.collection(dbCol).document(userId)
                    .get()
                    .addOnSuccessListener {
                        db.collection(dbCol).document(userId).update("balance", total.toString())
                    }
                Toast.makeText(this@ProfileTopupActivity, "Berhasil Mengisi Saldo",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ProfileTopupActivity, MainActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this@ProfileTopupActivity, "Isi Nominal",Toast.LENGTH_SHORT).show()
            }
        }
    }
}