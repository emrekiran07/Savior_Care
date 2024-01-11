package com.phc.project.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.phc.project.Models.Complaint
import com.phc.project.Models.Doctor
import com.phc.project.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.complaint_layout.view.*
import kotlinx.android.synthetic.main.doctor_layout.view.*
import kotlinx.android.synthetic.main.doctor_layout.view.img
import kotlinx.android.synthetic.main.doctor_layout.view.username

class ComplaintsAdapter(var data_array: ArrayList<Complaint.ComplaintData>, val context: Context): RecyclerView.Adapter<ComplaintsAdapter.ViewHolder>() {
    var mList = ArrayList<Complaint.ComplaintData>()
    init {
        mList = data_array
    }
    var onItemClick: ((Complaint.ComplaintData) -> Unit)? = null

    var pos: Int? = null
    var currentPos: Int? = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintsAdapter.ViewHolder {
        val inflatedView = parent.inflate(R.layout.complaint_layout, false)
        return ViewHolder(inflatedView)
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var data: Complaint.ComplaintData? = null

        init {
            v.setOnClickListener(this)
        }
        override fun onClick(v: View) {
            onItemClick?.invoke(mList.get(adapterPosition))
            if (pos == null)
                pos = 0

            currentPos = adapterPosition

            notifyDataSetChanged()

            pos = adapterPosition

        }
        fun bindCategories(data: Complaint.ComplaintData) {
            this.data = data
        }
    }

    fun updateList(list: ArrayList<Complaint.ComplaintData>){
        mList = list
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(mList[position].image != null && !mList[position].image.equals("")){
            Picasso.get().load(mList[position].image).into(holder.itemView.img)
        }
        holder.itemView.username.setText(mList[position].name)
        holder.itemView.blood_group.setText(mList[position].blood_group)
        holder.itemView.phone.setText(mList[position].phone)
        holder.itemView.status.setText(mList[position].status)
        holder.bindCategories(mList[position])
    }

    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

}