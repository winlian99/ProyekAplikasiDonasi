package com.example.proyekaplikasidonasi.ui.galang


import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyekaplikasidonasi.MainActivity
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.donasi.Donasi
import com.example.proyekaplikasidonasi.ui.donasi.DonasiFragment
import com.example.proyekaplikasidonasi.ui.profile.ProfileSettingFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_galang.*
import java.text.DecimalFormat
import java.text.NumberFormat


class GalangActivity : AppCompatActivity() {

    var arDonatur: ArrayList<Donatur> = arrayListOf()
    lateinit var adapter: adapterDonatur

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.activity_galang , container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galang)

        val dataIntent = intent.getParcelableExtra<Donasi>("kirimDonasi")
        Log.d("Cek dataIntent", dataIntent.idPenggalang.toString())

        val mAuth = FirebaseAuth.getInstance()
        val userIdSession = mAuth.currentUser?.email.toString()

        val storage = FirebaseStorage.getInstance()
        val imageRef = storage.getReference().child("images/donasi/" + dataIntent.idDonasi + ".jpg")

        val ONE_MEGABYTE: Long = 1024 * 1024
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            var bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            image_galang.setImageBitmap(bitmap)
        }

        val db = FirebaseFirestore.getInstance()
        val dbCol = "galang_dana"
        var documentId = ""
        if (dataIntent.idDonasi != null) {
            documentId = dataIntent.idDonasi
        }

        if (userIdSession != dataIntent.idPenggalang) {
            donation_end_button.visibility = View.GONE
        }

        db.collection(dbCol).document(documentId).get().addOnSuccessListener {
            var data = it?.data as MutableMap<String, String>
            judul_galang.setText(data.get("name").toString())

            val donasi: Double = data.get("donated_cash").toString().toDouble()
            val tDonasi: Double = data.get("target").toString().toDouble()
            val format: NumberFormat = DecimalFormat("#,###")
            val finalDonasi: String = "Rp. " + format.format(donasi).toString()
            val targetDonasi: String = " terkumpul dari Rp. " + format.format(tDonasi).toString()

            donasi_sekarang.setText(finalDonasi)
            target_donasi.setText(targetDonasi)

            val a = data.get("donated_cash").toString().toFloat()
            val b = data.get("target").toString().toFloat()
            val c = ((a / b) * 100).toInt()

            progress_galang.setProgress(c)
            Log.d("Value of c: ", c.toString())
            jumlah_donasi.setText(data.get("donated_num").toString() + " donasi")
            batas_galang.setText("Berakhir pada: " + data.get("limit").toString())
            description_title.setText(data.get("name").toString())
            description_date.setText(data.get("date").toString())
            description.setText(data.get("description").toString())
        }.addOnFailureListener {
            Toast.makeText(this@GalangActivity, "Error", Toast.LENGTH_SHORT).show()
        }

        donation_button.setOnClickListener {
            val intent = Intent(this@GalangActivity, BerdonasiActivity::class.java)
            intent.putExtra("kirimDonasi", dataIntent)
            startActivity(intent)
        }

        donation_end_button.setOnClickListener {
            val alert: AlertDialog.Builder = AlertDialog.Builder(this@GalangActivity)
            alert.setTitle("Alert!!")
            alert.setMessage("Anda Yakin Menyelesaikan Donasi?")
//            val current_user = db.collection("users").document(email_user)
//                        val galang_dana = db.collection("galang_dana").document(data_intent.idDonasi)
//                        galang_dana.update("donated_cash", current_money_galang.toString())
//                        galang_dana.update("donated_num", dNum.toString())
//                        current_user.update("balance", current_money_user.toString())val current_user = db.collection("users").document(email_user)
//                        val galang_dana = db.collection("galang_dana").document(data_intent.idDonasi)
//                        galang_dana.update("donated_cash", current_money_galang.toString())
//                        galang_dana.update("donated_num", dNum.toString())
//                        current_user.update("balance", current_money_user.toString())
            alert.setPositiveButton("Yes") { dialog, which ->
                val currentGalang = db.collection(dbCol).document(documentId)
                currentGalang.update("status", "0")
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@GalangActivity,
                            "Berhasil Menyelesaikan Donasi",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@GalangActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@GalangActivity,
                            "Gagal Menyelesaikan Donasi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            }

            alert.setNegativeButton("No") { dialog, which ->
                Toast.makeText(
                    this@GalangActivity,
                    "Batal Menyelesaikan Donasi",
                    Toast.LENGTH_SHORT
                ).show()
            }

            alert.show()
        }

        ambilData(dataIntent.idDonasi.toString())
    }

    private fun ambilData(pass_id_donasi: String) {
        val db = FirebaseFirestore.getInstance()
        val dbCol = "history_donasi"

        // This is where you check for the same thingy
        db.collection(dbCol).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                val data = doc.data as MutableMap<String, String>
                if(data.getValue("id_donasi").toString() == pass_id_donasi){
                    val tempDonatur = Donatur(
                        doc.id,
                        data.getValue("id_donasi").toString(),
                        data.getValue("jumlah_donasi").toString(),
                        data.getValue("komentar").toString(),
                        data.getValue("nama_donatur").toString(),
                        data.getValue("tanggal_donasi").toString(),
                    )
                    arDonatur.add(tempDonatur)
                }
            }
            rv_komen_galang.layoutManager = LinearLayoutManager(this)
            adapter = adapterDonatur(arDonatur)
            rv_komen_galang.adapter = adapter
        }.addOnFailureListener {
            Toast.makeText(this@GalangActivity, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show()


        }
    }
}