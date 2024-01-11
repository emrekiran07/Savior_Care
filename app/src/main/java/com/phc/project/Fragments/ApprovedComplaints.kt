package com.phc.project.Fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.phc.project.Activities.ComplaintDetail
import com.phc.project.Adapters.ComplaintsAdapter
import com.phc.project.Models.Complaint

import com.phc.project.R
import com.phc.project.Utils.DBReferences
import com.phc.project.Utils.Utils
import io.paperdb.Paper

/**
 * A simple [Fragment] subclass.
 */
class ApprovedComplaints : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var dialog: Dialog
    lateinit var list : ArrayList<Complaint.ComplaintData>
    lateinit var adapter : ComplaintsAdapter
    lateinit var pref: SharedPreferences
    val TAG= "PendingComplaints"
    lateinit var noDataText : TextView
    var email:String?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_approved_complaints, container, false)
        initViews(view)
        Paper.init(activity)
        email = Paper.book().read("email")
        getDataFromDB()
        adapter.onItemClick = {data ->
            if(data != null){
                val intent = Intent(activity!!, ComplaintDetail::class.java)
                intent.putExtra("name", data.name)
                intent.putExtra("phone", data.phone)
                intent.putExtra("email", data.email)
                intent.putExtra("cnic", data.cnic)
                intent.putExtra("address", data.address)
                intent.putExtra("status", data.status)
                intent.putExtra("image", data.image)
                intent.putExtra("blood_group", data.blood_group)
                intent.putExtra("complain", data.complain)
                intent.putExtra("complain_id", data.complaintId)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.enter,R.anim.exit)
            }

        }
        return view
    }
    private fun getDataFromDB() {
        DBReferences.complaintRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(TAG,"Error "+ p0.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG,"email is "+email)

                list.clear()
                Log.d(TAG,"DatasnapShot is "+ snapshot.value.toString())
                for(d in snapshot.children ){
                    if(d.child("status").value.toString().equals("approved") && d.child("doc_email").value.toString().equals(email)){
                        Log.d(TAG,"Got the Data")
                        val obj = Complaint.ComplaintData(
                            d.child("name").value.toString(),
                            d.child("email").value.toString(),
                            d.child("blood_group").value.toString(),
                            d.child("cnic").value.toString(),
                            d.child("phone").value.toString(),
                            d.child("address").value.toString(),
                            d.child("image").value.toString(),
                            d.child("complain").value.toString(),
                            d.child("status").value.toString(),
                            d.child("doc_email").value.toString(),
                            d.child("complaintId").value.toString())
                        list.add(obj)
                    }
                }
                if(list != null && list.size >0){
                    noDataText.visibility = View.GONE
                    Log.d(TAG,"List size is "+list.size)
                    adapter.updateList(list)
                }else{
                    noDataText.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun initViews(view: View?) {
        recyclerView = view!!.findViewById(R.id.recyclerViewApproved)
        dialog = Utils.progressDialog(context)
        pref = activity!!.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        noDataText = view!!.findViewById(R.id.no_data)
        list = ArrayList<Complaint.ComplaintData>()
        adapter = ComplaintsAdapter(list,context!!)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter



    }

}
