package com.phc.project.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phc.project.Adapters.SpecializationAdapter
import com.phc.project.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_doctor_detail.*
import java.util.Random

class DoctorDetail : AppCompatActivity() {

    lateinit var backButton:ImageView
    lateinit var profilePic:CircleImageView
    lateinit var name:TextView
    lateinit var phone:TextView
    lateinit var email:TextView
    lateinit var address:TextView
    lateinit var hourlyRate:TextView
    lateinit var rating:TextView
    lateinit var spAdapter: SpecializationAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var spList:ArrayList<String>
    lateinit var complaintButton:Button
    lateinit var experience: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_detail)
        initViews()
        val random = Random()

        val intent = getIntent()
        val email = intent.getStringExtra("email")
        val adrs = intent.getStringExtra("address")
        val ph = intent.getStringExtra("phone")
        val rtng = intent.getStringExtra("rating")
        val htr = intent.getStringExtra("hourly_rate")
        val url = intent.getStringExtra("image")
        val specialization = intent.getStringExtra("specialization")
        val username = intent.getStringExtra("name")
        spList = specialization.split(",").map { it -> it.trim() } as ArrayList<String>
        spAdapter = SpecializationAdapter(spList,this)
        recyclerView.adapter = spAdapter
        if( url != null && url != ""){
            Picasso.get().load(url).error(R.drawable.ph).into(profilePic)
        }
        name.setText(username)
        phone.setText(ph)
        address.setText(adrs)
        rating.setText(rtng)
        hourlyRate.setText(htr)
        experience.setText((random.nextInt(20 - 1) + 1).toString()+" yrs. Experience")

        complaintButton.setOnClickListener {
            val intent = Intent(this,ComplainForm::class.java)
            intent.putExtra("email",email)
            startActivity(intent)
            overridePendingTransition(R.anim.enter,R.anim.exit)
        }

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initViews() {
        backButton= findViewById(R.id.back)
        name= findViewById(R.id.name)
        phone= findViewById(R.id.phone)
        address = findViewById(R.id.address)
        hourlyRate = findViewById(R.id.hourly_rate)
        rating =  findViewById(R.id.rating)
        complaintButton = findViewById(R.id.contact_btn)
        profilePic = findViewById(R.id.profile_pic)
        recyclerView = findViewById(R.id.sp_recyclerview)
        spList  = ArrayList()
        experience = findViewById(R.id.experience)
        recyclerView.layoutManager = LinearLayoutManager(this ,LinearLayoutManager.HORIZONTAL, false)
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left)

    }
}
