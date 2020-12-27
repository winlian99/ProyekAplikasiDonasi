package com.example.proyekaplikasidonasi.ui.register

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var refUsers : DatabaseReference
    private var firebaseUserID: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        register_button_register.setOnClickListener{
            registerUser()
        }
    }
    private fun registerUser() {
        var nama_lengkap : String = register_nama_lengkap.text.toString()
        var email : String = register_email.text.toString()
        var tgl_lahir : String = register_tanggal_lahir.dayOfMonth.toString() + "/" + register_tanggal_lahir.month.toString() + "/" + register_tanggal_lahir.year.toString()
        var password : String = register_password.text.toString()

        if(nama_lengkap == ""){
            Toast.makeText(this@RegisterActivity, "Fill the empty field", Toast.LENGTH_LONG).show()
        }
        else if(email == ""){
            Toast.makeText(this@RegisterActivity, "Fill the empty field", Toast.LENGTH_LONG).show()
        }
        else if(tgl_lahir == ""){
            Toast.makeText(this@RegisterActivity, "Fill the empty field", Toast.LENGTH_LONG).show()
        }
        else if(password==""){
            Toast.makeText(this@RegisterActivity, "Fill the empty field", Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                        task->
                    if (task.isSuccessful){
                        val db = FirebaseFirestore.getInstance()
                        val dbcol = "users"

                        firebaseUserID = mAuth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("users").child(
                            firebaseUserID
                        )

                        val data = hashMapOf(
                            "name" to nama_lengkap,
                            "dob" to tgl_lahir,
                            "email" to email,
                            "balance" to "0"
                        )
                        db.collection(dbcol).document(register_email.text.toString()).set(data)

                        // Get Default Image From Firebase Storage
                        val storage = FirebaseStorage.getInstance()
                        val imageRef = storage.getReference().child("images/profile/default.jpg")

                        val ONE_MEGABYTE : Long = 1024 * 1024
                        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                            var bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)

                            // Set Default Image to Profile in Firestorage
                            val imageRef2 = storage.reference.child("images/profile/" + email + ".jpg")
                            val baos = ByteArrayOutputStream()

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val data = baos.toByteArray()
                            imageRef2.putBytes(data)
                                .addOnSuccessListener {
                                    Toast.makeText(this@RegisterActivity, "Berhasil Mengupload Gambar", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{
                                    Toast.makeText(this@RegisterActivity, "Gagal Mengupload Gambar", Toast.LENGTH_SHORT).show()
                                }
                        }

                        Toast.makeText(
                            this@RegisterActivity,
                            "Register Success!",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error Message: " + task.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        register_email.setText("")
                        register_password.setText("")
                    }
                }
        }
    }
}