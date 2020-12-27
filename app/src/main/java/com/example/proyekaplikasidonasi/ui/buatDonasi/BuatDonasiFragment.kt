package com.example.proyekaplikasidonasi.ui.buatDonasi

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyekaplikasidonasi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_buat_donasi.*
import java.text.SimpleDateFormat
import java.util.*


class BuatDonasiFragment : Fragment() {

    private lateinit var mAuth : FirebaseAuth
    lateinit var filepath : Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buat_donasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.email.toString()

        buat_donasi_upload_image_button.setOnClickListener {
            var i = Intent()
            i.setType("image/*")
            i.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(i, "Choose Picture"), 111)
        }

        buat_donasi_tambah_button.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val dbCol = "galang_dana"

            val descriptionTemp = if (buat_donasi_deskripsi.text.toString() != "") buat_donasi_deskripsi.text.toString() else ""

            val sdf = SimpleDateFormat("yyyyMMdd")
            val currentDate = sdf.format(Date())
            val sdf2 = SimpleDateFormat("dd/MM/yyyy")
            val currentDate2 = sdf2.format(Date())
            var tempBulan = buat_donasi_limit_date.month + 1
            var tempBulanStr = tempBulan.toString()
            if(tempBulan < 10){
                tempBulanStr = "0" + tempBulanStr
            }
            var tempHari = buat_donasi_limit_date.dayOfMonth + 1
            var tempHariStr = tempHari.toString()
            if(tempHari < 10){
                tempHariStr = "0" + tempHariStr
            }

            val limitDateTemp = buat_donasi_limit_date.year.toString() + tempBulanStr + tempHariStr

            try{
                if(buat_donasi_target_donasi.text.toString().toInt() < 0){
                    Toast.makeText(context, "Target donasi yang dimasukkan salah", Toast.LENGTH_SHORT).show()
                }
                else if (buat_donasi_target_donasi.text.toString().toInt() <= 0){
                    Toast.makeText(context, "Target donasi yang dimasukkan salah", Toast.LENGTH_SHORT).show()
                }
                else if (buat_donasi_min_donasi.text.toString().toInt() <= 0){
                    Toast.makeText(context, "Minimal donasi yang dimasukkan salah", Toast.LENGTH_SHORT).show()
                }
                else if (currentDate.toInt() > limitDateTemp.toInt()){
                    Toast.makeText(context, "Tanggal akhir donasi yang dimasukkan salah", Toast.LENGTH_SHORT).show()
                }
                else {
                    val data = hashMapOf(
                        "name" to buat_donasi_judul.text.toString(),
                        "target" to buat_donasi_target_donasi.text.toString(),
                        "limit" to buat_donasi_limit_date.dayOfMonth.toString() + "/" + buat_donasi_limit_date.month.toString() + "/" + buat_donasi_limit_date.year.toString(),
                        "minimal" to buat_donasi_min_donasi.text.toString(),
                        "id_penggalang" to userId,
                        "donated_num" to "0",
                        "donated_cash" to "0",
                        "description" to descriptionTemp,
                        "date" to currentDate2,
                        "status" to "1"
                    )

                    db.collection(dbCol).add(data)
                        .addOnSuccessListener {
                                documentReference ->
                            uploadFile(documentReference.id)
                            Toast.makeText(context, "Berhasil Menggalang Donasi", Toast.LENGTH_SHORT)
                                .show()
                            buat_donasi_judul.text.clear()
                            buat_donasi_target_donasi.text.clear()
                            buat_donasi_min_donasi.text.clear()
                            buat_donasi_deskripsi.text.clear()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Gagal Menggalang Donasi", Toast.LENGTH_SHORT).show()
                        }
                }

            }
            catch (e : Exception){
                android.widget.Toast.makeText(context, "Mohon masukkan data dengan benar", android.widget.Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data != null){
            filepath = data.data!!
            Toast.makeText(context, "Berhasil Memilih Gambar", Toast.LENGTH_SHORT).show()
            buat_donasi_image_condition.setText("Successfully Selected File")
        }
    }

    private fun uploadFile(filename: String){
        if(filepath != null){
            var pd = ProgressDialog(context)
            pd.setTitle("Uploading")
            pd.show()

            var imageRef = FirebaseStorage.getInstance().reference.child("images/donasi/" + filename + ".jpg")
            imageRef.putFile(filepath)
                .addOnSuccessListener {
                    pd.dismiss()
                    Toast.makeText(context, "Berhasil Mengupload Gambar", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    pd.dismiss()
                    Toast.makeText(context, "Gagal Mengupload Gambar", Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { p0 ->
                    var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                    pd.setMessage("Uploaded ${progress.toInt()}%")
                }
        }
    }
}