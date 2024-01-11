package com.phc.project.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.phc.project.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_doctor_feedback.*

class ComplaintDetail : AppCompatActivity() {

    lateinit var pImage:CircleImageView
    lateinit var pName:TextView
    lateinit var pPhone: TextView
    lateinit var pCNIC:TextView
    lateinit var pAddress:TextView
    lateinit var pEmail:TextView
    lateinit var pComplainId:TextView
    lateinit var pComplain:TextView
    lateinit var pBloodGroup:TextView
    lateinit var backButton: ImageView
    val TAG = "ComplaintDetail"

    lateinit var feedbackButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_detail)
        initViews()
        val extraIntent = getIntent()
        val image = extraIntent.getStringExtra("image")
        if(image != null && !image.equals("")){
            Picasso.get().load(image).into(pImage)
        }
        val name = extraIntent.getStringExtra("name")
        val phone = extraIntent.getStringExtra("phone")
        val cnic = extraIntent.getStringExtra("cnic")
        val email = extraIntent.getStringExtra("email")
        val address = extraIntent.getStringExtra("address")
        val complainId = extraIntent.getStringExtra("complain_id")
        val complain = extraIntent.getStringExtra("complain")
        val status = extraIntent.getStringExtra("status")
        val bloodGroup = extraIntent.getStringExtra("blood_group")

        Log.d(TAG,"CNIC is "+ cnic)
        pName.setText(name)
        pPhone.setText(phone)
        pCNIC.setText(cnic.toString())
        pEmail.setText(email)
        pAddress.setText(address)
        pComplainId.setText(complainId)
        pComplain.setText(complain)
        pBloodGroup.setText(bloodGroup)

        if(status.equals("approved")){
            feedbackButton.visibility = View.GONE
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        feedbackButton.setOnClickListener {
            val intent = Intent(this,DoctorFeedback::class.java)
            intent.putExtra("email",email)
            intent.putExtra("complainId",complainId)
            startActivity(intent)
            overridePendingTransition(R.anim.enter,R.anim.exit)
        }
    }

    private fun initViews() {
        pImage = findViewById(R.id.profile_pic)
        pName = findViewById(R.id.p_name)
        pPhone = findViewById(R.id.p_phone)
        pCNIC = findViewById(R.id.cnic)
        pAddress= findViewById(R.id.p_address)
        pComplain = findViewById(R.id.complain_description)
        pComplainId = findViewById(R.id.complain_id)
        pEmail = findViewById(R.id.p_email)
        pBloodGroup = findViewById(R.id.p_b_group)
        feedbackButton = findViewById(R.id.feedback_button)
        backButton  = findViewById(R.id.back)

    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left)

    }
}
