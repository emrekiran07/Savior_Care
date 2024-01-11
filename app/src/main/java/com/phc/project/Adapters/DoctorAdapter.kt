package com.phc.project.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.phc.project.Fragments.TAG
import com.phc.project.Models.Doctor
import com.phc.project.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.doctor_layout.view.*


class DoctorAdapter(var data_array: ArrayList<Doctor.DoctorData>, val context: Context) :
    RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

    var mList = ArrayList<Doctor.DoctorData>()
    init {
        mList = data_array
    }
    var onItemClick: ((Doctor.DoctorData) -> Unit)? = null

    var pos: Int? = null
    var currentPos: Int? = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = parent.inflate(R.layout.doctor_layout, false)
        return ViewHolder(inflatedView)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var data: Doctor.DoctorData? = null

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

        fun bindCategories(data: Doctor.DoctorData) {
            this.data = data
        }
    }

    fun updateList(list: ArrayList<Doctor.DoctorData>){
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
        holder.itemView.username.setText(mList[position].username)
        holder.itemView.specialization.setText(mList[position].specialization)
        holder.itemView.rate.setText(mList[position].rate+" $")
        holder.itemView.rating_bar.rating = mList[position].rating.toFloat()
        holder.bindCategories(mList[position])
    }

    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }
}