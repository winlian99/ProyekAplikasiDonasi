package com.example.proyekaplikasidonasi.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyekaplikasidonasi.MainActivity
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        login_button_login.setOnClickListener{
            loginUser()
        }
        login_button_register.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun loginUser() {
        var email : String = login_email.text.toString()
        var password : String = login_password.text.toString()

        if(email == ""){
            Toast.makeText(this@LoginActivity,"Fill the empt field", Toast.LENGTH_LONG).show()
        }
        else if(password == ""){
            Toast.makeText(this@LoginActivity,"Fill the empt field", Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        Toast.makeText(this@LoginActivity,"Login Success!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@LoginActivity,"Error Message: " + task.exception!!.message.toString(),
                            Toast.LENGTH_LONG).show()
                        login_email.setText("")
                        login_password.setText("")
                    }
                }
        }
    }
}