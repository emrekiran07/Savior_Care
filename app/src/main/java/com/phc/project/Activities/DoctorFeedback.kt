package com.phc.project.Activities

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.utils.Utils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.phc.project.R
import com.phc.project.Utils.DBReferences


class DoctorFeedback : AppCompatActivity() {

    val TAG = "DoctorFeedback"
    lateinit var backButton: ImageView
    lateinit var patientEmail:TextView
    lateinit var prescription:EditText
    var email:String? = null
    var complainId:String? = null
    lateinit var sendEmailButton: Button
    lateinit var dialog:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_feedback)
        initViews()
        val extraIntent = getIntent()
        email = extraIntent.getStringExtra("email")
        complainId = extraIntent.getStringExtra("complainId")
        if(email != null){
            patientEmail.setText(email)
        }
        backButton.setOnClickListener {
            onBackPressed()
        }
        sendEmailButton.setOnClickListener {
            if(validate(prescription)){
                    sendEmail()
                }else{
                com.phc.project.Utils.Utils.showShortMessage(this,"Please write something in body first...")
            }
        }

    }

    private fun validate(
        email: EditText
    ): Boolean {
        if (email.text?.trim().toString().equals("") or TextUtils.isEmpty(email.text?.trim())) {
            email.setBackgroundResource(R.drawable.bg_error)
            return false
        }
        return true
    }
    private fun sendEmail() {
        dialog.show()
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null
            )
        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Prescription for your complain from Savior Care")
        emailIntent.putExtra(Intent.EXTRA_TEXT, prescription.text.trim().toString())
        DBReferences.complaintRef.child(complainId.toString()).child("status").setValue("approved").addOnCompleteListener {
            if(it.isSuccessful){
                dialog.dismiss()
                startActivity(Intent.createChooser(emailIntent, "Send email..."))
            }
        }
    }

    private fun initViews() {
        backButton = findViewById(R.id.back)
        patientEmail = findViewById(R.id.email_patient)
        prescription = findViewById(R.id.complain)
        sendEmailButton = findViewById(R.id.email_button)
        dialog = com.phc.project.Utils.Utils.progressDialog(this)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left)

    }
}
