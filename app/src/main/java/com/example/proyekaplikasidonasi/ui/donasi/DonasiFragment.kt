package com.example.proyekaplikasidonasi.ui.donasi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekaplikasidonasi.R
import com.example.proyekaplikasidonasi.ui.galang.GalangActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_donasi.*

class DonasiFragment : Fragment(), adapterDonasi.RecyclerViewClickListener  {

    var arDonasi : ArrayList<Donasi> = arrayListOf()
    lateinit var adapter : adapterDonasi

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AmbilData()
    }

    override fun itemKlik(view: View, dataDonasi: Donasi) {
        super.itemKlik(view, dataDonasi)
//        val bundle = Bundle()
//        bundle.putParcelable("kirimDonasi", dataDonasi)
//
//        val galangFragment = GalangFragment()
//        galangFragment.arguments = bundle
//        val mFragmentManager = activity?.supportFragmentManager
//        mFragmentManager?.beginTransaction()?.apply {
//            replace(R.id.frameContainer, galangFragment, galangFragment::class.java.simpleName)
//            addToBackStack(null)
//            commit()
//        }
        val intent = Intent(context, GalangActivity::class.java)
        intent.putExtra("kirimDonasi", dataDonasi)
        startActivity(intent)
    }

    private fun AmbilData(){
        val db = FirebaseFirestore.getInstance()
        val dbCol = "galang_dana"

        db.collection(dbCol)
            .get()
            .addOnSuccessListener {
                documents ->
                for (document in documents) {
                    var data = document.data as MutableMap<String, String>
                    var tempDonasi = Donasi(
                        document.id,
                        data.getValue("description_title").toString(),
                        data.getValue("date").toString(),
                        data.getValue("limit").toString(),
                        data.getValue("description").toString(),
                        data.getValue("target").toString(),
                        data.getValue("minimal").toString(),
                        data.getValue("donated_cash").toString(),
                        data.getValue("id_penggalang").toString()
                    )
                    arDonasi.add(tempDonasi)
                }
                rvDaftarDonasi.layoutManager = LinearLayoutManager(activity)
                adapter = adapterDonasi(arDonasi)
                rvDaftarDonasi.adapter = adapter

                adapter.listener = this
            }
    }
}