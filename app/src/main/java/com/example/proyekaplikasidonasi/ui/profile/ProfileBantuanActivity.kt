package com.example.proyekaplikasidonasi.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyekaplikasidonasi.MainActivity
import com.example.proyekaplikasidonasi.R
import kotlinx.android.synthetic.main.activity_profile_bantuan.*

class ProfileBantuanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_bantuan)

        profile_bantuan_btn_home.setOnClickListener{
            val intent = Intent(this@ProfileBantuanActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}