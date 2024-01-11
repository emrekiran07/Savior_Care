package com.phc.project.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.phc.project.Models.Doctor
import com.phc.project.R
import kotlinx.android.synthetic.main.specialization_layout.view.*


class SpecializationAdapter(var data_array: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<SpecializationAdapter.ViewHolder>() {


    var mList = ArrayList<String>()

    init {
        mList = data_array
    }

    var onItemClick: ((String) -> Unit)? = null

    var pos: Int? = null
    var currentPos: Int? = 0

    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var data: String? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            onItemClick?.invoke(mList.get(adapterPosition))
            if (pos == null)
                pos = 0

            currentPos = adapterPosition
            pos = adapterPosition

        }

        fun bindCategories(data: String) {
            this.data = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = parent.inflate(R.layout.specialization_layout, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.sp_name.setText(mList[position].toString())
    }
}