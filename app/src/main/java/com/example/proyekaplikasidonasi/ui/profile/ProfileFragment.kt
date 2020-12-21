package com.example.proyekaplikasidonasi.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.proyekaplikasidonasi.MainActivity
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class ProfileFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        val db = FirebaseFirestore.getInstance()
        val dbCol = getString(R.string.dbColUsersCollection)
        val userId = mAuth.currentUser?.email.toString()

        db.collection(dbCol).document(userId)
            .get()
            .addOnSuccessListener {
                var data = it?.data as MutableMap<String, String>
                profile_name.setText(data.getValue("name").toString())
                val format: NumberFormat = DecimalFormat("#,###")
                val tempDompet = data.getValue("balance").toString().toInt()
                profile_dompet_saya.setText("Rp. " + format.format(tempDompet).toString())
            }

//        profile_dompet_saya.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_account_balance_wallet_24, 0,0,0)
//        profile_galang_dana_saya.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_box_24, 0, 0, 0)
//        profile_bantuan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_contact_support_24, 0,0,0)
//        profile_sign_out.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_assignment_return_24, 0, 0, 0)

        profile_topup_dompet.setOnClickListener {
            Toast.makeText(context, "Topup", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, ProfileTopupActivity::class.java)
            startActivity(intent)
        }

            profile_menu_setting_profile.setOnClickListener{
            val intent = Intent(context, ProfileSettingFragment::class.java)
            startActivity(intent)
//            val profileSettingProfileFragment = ProfileSettingProfileFragment()
//            var mFragmentManager = activity?.supportFragmentManager
//            mFragmentManager?.beginTransaction()?.apply {
//                replace(R.id.profile, profileSettingProfileFragment)
//                addToBackStack(null)
//                commit()
//            }
            }
            profile_btn_bantuan.setOnClickListener{
                val intent = Intent(context, ProfileBantuanActivity::class.java)
                startActivity(intent)
            }
            profile_btn_galang_dana_saya.setOnClickListener{
                val intent = Intent(context, ProfileGalangDanaSayaActivity::class.java)
                startActivity(intent)
            }

            profile_button_signout.setOnClickListener {
                mAuth.signOut()
                Toast.makeText(context, "Sign Out Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }

            profile_galang_dana_saya.setOnClickListener {
                val intent = Intent(context, ProfileGalangDanaSaya::class.java)
                startActivity(intent)
            }
        }

        override fun onResume() {
            super.onResume()

            mAuth = FirebaseAuth.getInstance()

            val db = FirebaseFirestore.getInstance()
            val dbCol = getString(R.string.dbColUsersCollection)
            val userId = mAuth.currentUser?.email.toString()

            db.collection(dbCol).document(userId)
                .get()
                .addOnSuccessListener {
                    var data = it?.data as MutableMap<String, String>
                    profile_name.setText(data.getValue("name").toString())
                    val format: NumberFormat = DecimalFormat("#,###")
                    val tempDompet = data.getValue("balance").toString().toInt()
                    profile_dompet_saya.setText("Rp. " + format.format(tempDompet).toString())
                }
        }
    }

