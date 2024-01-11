package com.phc.project.Fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.phc.project.Activities.DoctorDetail
import com.phc.project.Adapters.DoctorAdapter
import com.phc.project.Models.Doctor

import com.phc.project.R
import com.phc.project.Utils.DBReferences
import com.phc.project.Utils.Utils

class TopRated : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var dialog: Dialog
    lateinit var list: ArrayList<Doctor.DoctorData>
    lateinit var adapter: DoctorAdapter
    val TAG = "TopRated"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_top_rated, container, false)
        initViews(view)
        getDataFromDB()

        adapter.onItemClick = object : (Doctor.DoctorData) -> Unit {
            override fun invoke(p1: Doctor.DoctorData) {
                val intent = Intent(context, DoctorDetail::class.java)
                intent.putExtra("name",p1.username)
                intent.putExtra("email",p1.email)
                intent.putExtra("address",p1.address)
                intent.putExtra("phone",p1.phone)
                intent.putExtra("hourly_rate",p1.rate)
                intent.putExtra("rating",p1.rating)
                intent.putExtra("specialization",p1.specialization)
                intent.putExtra("image",p1.image)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.enter,R.anim.exit)
            }

        }
        return view
    }

    private fun getDataFromDB(){
        dialog.show()
        DBReferences.doctorRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                dialog.dismiss()
                Log.d(TAG,"Error "+ p0.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                dialog.dismiss()
                list.clear()
                for(d in snapshot.children ){
                    if(d.child("status").value.toString().equals("approved") && d.child("rating").value.toString().toInt() >= 4){
                        val obj = Doctor.DoctorData( d.child("username").value.toString(),
                            d.child("email").value.toString(),
                            d.child("password").value.toString(),
                            d.child("address").value.toString(),
                            d.child("phone").value.toString(),
                            d.child("image").value.toString(),
                            d.child("specialization").value.toString(),
                            d.child("rate").value.toString(),
                            d.child("status").value.toString(),
                            d.child("rating").value.toString())
                        list.add(obj)
                    }
                }
                if(list != null && list.size >0){
                    Log.d(TAG,"List size is "+list.size)
                    adapter.updateList(list)
                }
            }
        })
    }

    private fun initViews(view: View?) {
        recyclerView = view!!.findViewById(R.id.topdoc_recycler_view)
        dialog = Utils.progressDialog(context)
        list = ArrayList<Doctor.DoctorData>()
        adapter = DoctorAdapter(list,context!!)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

}
