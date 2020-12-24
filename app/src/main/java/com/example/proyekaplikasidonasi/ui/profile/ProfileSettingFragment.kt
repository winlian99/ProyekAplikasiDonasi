package com.example.proyekaplikasidonasi.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.proyekaplikasidonasi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_setting_profile.*

class ProfileSettingFragment : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_setting_profile)

        mAuth = FirebaseAuth.getInstance()

        val db = FirebaseFirestore.getInstance()
        val dbCol = "users"
        val userId = mAuth.currentUser?.email.toString()
        var saldo = "0"
        
        profile_setting_email.isEnabled = false

        Log.d("Ini Buat Ngecek", userId)

        db.collection(dbCol).document(userId)
            .get()
            .addOnSuccessListener {
                var data = it?.data as MutableMap<String, String>
                profile_setting_nama.setText(data.getValue("name").toString())
                profile_setting_email.setText(data.getValue("email").toString())
                saldo = data.getValue("balance").toString().toString()
                val tanggal_lahir = data.getValue("dob").toString().split("/").toTypedArray()
                buat_donasi_limit_date.updateDate(tanggal_lahir[2].toInt(), tanggal_lahir[1].toInt(), tanggal_lahir[0].toInt())
            }
        profile_update_button.setOnClickListener {
            var nama_lengkap : String = profile_setting_nama.text.toString()
            var email : String = userId
            var tgl_lahir : String = buat_donasi_limit_date.dayOfMonth.toString() + "/" + buat_donasi_limit_date.month.toString() + "/" + buat_donasi_limit_date.year.toString()

            if(nama_lengkap == ""){
                Toast.makeText(this@ProfileSettingFragment,"Nama tidak boleh kosong", Toast.LENGTH_LONG).show()
            }
            else if(tgl_lahir == ""){
                Toast.makeText(this@ProfileSettingFragment,"Tanggal lahir tidak boleh kosong", Toast.LENGTH_LONG).show()
            }
            else{
                val data = hashMapOf(
                    "name" to nama_lengkap,
                    "dob" to tgl_lahir,
                    "email" to email,
                    "balance" to saldo
                )
                db.collection(dbCol).document(userId).set(data)
                Toast.makeText(this@ProfileSettingFragment,"Berhasil Mengubah Profile", Toast.LENGTH_LONG).show()

                val intent = Intent(this@ProfileSettingFragment, ProfileFragment::class.java)
                startActivity(intent)
            }
        }
    }
}