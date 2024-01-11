package com.phc.project.Activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.phc.project.MainActivity
import com.phc.project.R
import com.phc.project.Utils.DBReferences
import com.phc.project.Utils.Utils
import io.paperdb.Paper

class Login : AppCompatActivity() {

    lateinit var emailLayout: TextInputLayout
    lateinit var passwordLayout: TextInputLayout
    lateinit var email: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var mButton: Button
    lateinit var dialog: Dialog
    lateinit var registerText: TextView
    lateinit var checkBox: CheckBox
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    val TAG = "LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Paper.init(this)
        initViews()

        mButton.setOnClickListener {
            validateFields()
        }
        registerText.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.enter, R.anim.exit)
            finish()
        }
    }

    private fun validateFields() {
        val bool1 = validate(email, emailLayout)
        val bool2 = validate(password, passwordLayout)
        if (bool1 && bool2) {
            if (checkBox.isChecked) {
                signInAsDoctor()
            } else {
                signInAsPatient()
            }
        } else {
            Utils.showShortMessage(this, "Please fill all the fields...")
        }
    }

    private fun signInAsPatient() {
        dialog.show()
        DBReferences.patientRef.orderByChild("email").equalTo(email.text?.trim().toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Utils.showShortMessage(
                        this@Login,
                        "Something went wrong, please try later."
                    )
                    dialog.dismiss()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    dialog.dismiss()
                    if (p0.exists()) {
                        p0.children.forEach {
                            Log.d(TAG, "datasnpshot foreach " + it.child("email").value)
                            if (it.child("password").value!!.equals(
                                    password.text!!.trim().toString()
                                )
                            ) {
                                Utils.showShortMessage(
                                    this@Login,
                                    "Welcome " + it.child("username").value.toString()
                                )
                                sendUserToPatientDashboard(
                                    it.child("username").value.toString(),
                                    it.child("phone").value.toString(),
                                    it.child("email").value.toString(),
                                    it.child("image").value.toString(),
                                    it.child("address").value.toString()
                                )
                            } else {
                                Utils.showShortMessage(this@Login, "Wrong password entered")
                            }

                        }

                    } else {
                        Utils.showShortMessage(
                            this@Login,
                            "No Patient is registered with this email..."
                        )
                    }
                }
            })
    }
    private fun signInAsDoctor() {
        dialog.show()
        DBReferences.doctorRef.orderByChild("email").equalTo(email.text?.trim().toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Utils.showShortMessage(
                        this@Login,
                        "Something went wrong, please try later."
                    )
                    dialog.dismiss()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    dialog.dismiss()
                    if (p0.exists()) {
                        p0.children.forEach {
                            Log.d(TAG, "datasnpshot foreach " + it.child("email").value)
                            if (it.child("password").value!!.equals(
                                    password.text!!.trim().toString()
                                )
                            ) {
                                Utils.showShortMessage(this@Login, "Welcome " + it.child("username").value.toString())
                                sendUserToDoctorDashboard(
                                    it.child("username").value.toString(),
                                    it.child("phone").value.toString(),
                                    it.child("email").value.toString(),
                                    it.child("image").value.toString(),
                                    it.child("address").value.toString(),
                                    it.child("rating").value.toString(),
                                    it.child("specialization").toString(),
                                    it.child("status").value.toString(),
                                    it.child("rate").value.toString()
                                )

                            } else {
                                Utils.showShortMessage(this@Login, "Wrong password entered")
                            }

                        }

                    } else {
                        Utils.showShortMessage(
                            this@Login,
                            "No Doctor registered with this email..."
                        )
                    }
                }
            })
    }
    private fun sendUserToPatientDashboard(
        username: String,
        phone: String,
        email: String,
        image: String,
        adresss: String
    ) {
        val intent = Intent(this, MainActivity::class.java)
        editor.putBoolean("isUserLogedIn", true)
        editor.putString("email", email)
        editor.putString("phone", phone)
        editor.putString("address", adresss)
        editor.putString("image", image)
        editor.putString("username", username)
        if (checkBox.isChecked) {
            editor.putBoolean("isDoctor", true)
        } else {
            editor.putBoolean("isDoctor", false)
        }
        editor.commit()
        startActivity(intent)
        overridePendingTransition(R.anim.enter, R.anim.exit)
        finish()
    }



    private fun sendUserToDoctorDashboard(
        username: String,
        phone: String,
        email: String,
        image: String,
        address: String,
        rating: String,
        specialization: String,
        status: String,
        rate: String
    ) {

        val intent = Intent(this, DoctorDashboard::class.java)
//        intent.putExtra("email", email)
//        intent.putExtra("username", username)
//        intent.putExtra("image", image)
//        intent.putExtra("address", address)
//        intent.putExtra("rating", rating)
//        intent.putExtra("specialization", specialization)
        intent.putExtra("status", status)
//        intent.putExtra("rate", rate)
//        intent.putExtra("phone", phone)
        editor.putBoolean("isUserLogedIn", true)
        editor.putString("email", email)
        Paper.book().write("email",email)
        Paper.book().write("status",status)
        editor.putString("phone", phone)
        editor.putString("address", address)
        editor.putBoolean("isDoctor",true)
        editor.putString("image", image)
        editor.putString("rating", rating)
        editor.putString("username", username)
        editor.putString("specialization", specialization)
        editor.commit()
        Log.d(TAG, "Status is " + status)
        startActivity(intent)
        overridePendingTransition(R.anim.enter, R.anim.exit)
        finish()
    }

    private fun validate(
        email: TextInputEditText,
        emailLayout: TextInputLayout
    ): Boolean {
        if (email.text?.trim().toString().equals("") or TextUtils.isEmpty(email.text?.trim())) {
            emailLayout.setBackgroundResource(R.drawable.bg_error)
            return false
        }
        return true
    }

    private fun initViews() {
        registerText = findViewById(R.id.text_register)
        checkBox = findViewById(R.id.checkbox)
        mButton = findViewById(R.id.login)
        dialog = Utils.progressDialog(this)
        email = findViewById(R.id.edit_email)
        password = findViewById(R.id.edit_password)
        emailLayout = findViewById(R.id.edit_emaillayout)
        passwordLayout = findViewById(R.id.edit_passwordlayout)
        pref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        editor = pref.edit()
    }
}
