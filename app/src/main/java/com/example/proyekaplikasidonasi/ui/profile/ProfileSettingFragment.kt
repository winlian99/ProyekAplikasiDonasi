package com.example.proyekaplikasidonasi.ui.profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.proyekaplikasidonasi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_buat_donasi.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_setting_profile.*
import kotlinx.android.synthetic.main.fragment_setting_profile.buat_donasi_limit_date

class ProfileSettingFragment : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    lateinit var filepath : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_setting_profile)

        mAuth = FirebaseAuth.getInstance()

        val db = FirebaseFirestore.getInstance()
        val dbCol = "users"
        val userId = mAuth.currentUser?.email.toString()
        var saldo = "0"
        val storage = FirebaseStorage.getInstance()
        val imageRef = storage.getReference().child("images/profile/"+userId+".jpg")

        val ONE_MEGABYTE : Long = 1024 * 1024
        Log.d("UserL ",userId)
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            var bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Log.d("Gnti Profile","")
            profile_setting_image.setImageBitmap(bitmap)
        }
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
        profile_setting_image.setOnClickListener{
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Pict"),111)
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
                Log.d("Sebelum set","")
                db.collection(dbCol).document(userId).set(data)
                uploadGambar(userId)
                Log.d("Sesudah Upload","")
                Toast.makeText(this@ProfileSettingFragment,"Berhasil Mengubah Profile", Toast.LENGTH_LONG).show()
                val intent = Intent(this@ProfileSettingFragment, ProfileSettingFragment::class.java)
                startActivity(intent)
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data != null){
            filepath = data.data!!
            profile_setting_image.setImageURI(filepath)
            Toast.makeText(this@ProfileSettingFragment, "Berhasil Memilih Gambar", Toast.LENGTH_SHORT).show()
        }
    }
    private fun uploadGambar(User: String){
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.email.toString()
        if(filepath != null){
            val context = this@ProfileSettingFragment
            var pd = ProgressDialog(context)
            pd.setTitle("Uploading")
            pd.show()

            var imageRef = FirebaseStorage.getInstance().reference.child("images/profile/" + User + ".jpg")
            imageRef.putFile(filepath)
                .addOnSuccessListener {
                    pd.dismiss()
                    Toast.makeText(context, "Berhasil Mengupload Gambar", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    pd.dismiss()
                    Toast.makeText(context, "Gagal Mengupload Gambar", Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener {
                        p0 ->
                    var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                    pd.setMessage("Uploaded ${progress.toInt()}%")
                }
        }
    }
}