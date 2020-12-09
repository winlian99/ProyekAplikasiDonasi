package com.example.proyekaplikasidonasi

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.proyekaplikasidonasi.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class MainActivity : AppCompatActivity() {
    val NmPref = "CobaPref"
    val KeyData = "KeyData"
    lateinit var sP : SharedPreferences

    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        sP = getSharedPreferences(NmPref, MODE_PRIVATE)

        if(mAuth.currentUser.toString() == "null"){
            Log.d("Message: ",mAuth.currentUser.toString())
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        else{
            Log.d("Messagee: ",mAuth.currentUser!!.email.toString())
            val editor : SharedPreferences.Editor = sP.edit()
            editor.putString(KeyData, mAuth.currentUser!!.email.toString())
            editor.apply()
        }


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_donasi, R.id.navigation_galang, R.id.navigation_riwayat, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}