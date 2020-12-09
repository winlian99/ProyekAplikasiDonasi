package com.example.proyekaplikasidonasi.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.proyekaplikasidonasi.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = FirebaseFirestore.getInstance()
        val dbCol = getString(R.string.dbColUsersCollection)
        val userId = "DKpawmCmV7yuOqB1L8zq"

        db.collection(dbCol).document(userId)
            .get()
            .addOnSuccessListener {
                var data = it?.data as MutableMap<String, String>
                profile_name.setText(data.getValue("name").toString())
                profile_dompet_saya.setText("Rp. " + data.getValue("balance").toString())
            }

//        profile_dompet_saya.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_account_balance_wallet_24, 0,0,0)
//        profile_galang_dana_saya.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_box_24, 0, 0, 0)
//        profile_bantuan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_contact_support_24, 0,0,0)
//        profile_sign_out.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_assignment_return_24, 0, 0, 0)

        profile_dompet_saya.setOnClickListener {
            Toast.makeText(context,"Bisa ngeklik text",Toast.LENGTH_SHORT).show()
        }
    }
}