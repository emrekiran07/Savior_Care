package com.phc.project.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.phc.project.Fragments.RegisterDoctor
import com.phc.project.Fragments.RegisterPatient
import com.phc.project.R

class Register : AppCompatActivity() {

    lateinit var fragmentContainer:FrameLayout
    lateinit var toggle: LinearLayout
    var selected: Int = 1
    lateinit var patientLayout: LinearLayout
    lateinit var doctorLayout: LinearLayout
    lateinit var patientText: TextView
    lateinit var doctorText: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initViews()
        setListeneronToggle()
        supportFragmentManager.beginTransaction().replace(R.id.container,RegisterPatient()).commit()

    }

    private fun setListeneronToggle() {
        toggle.setOnClickListener {
            if (selected == 1) {
                selected = 2
                patientLayout.setBackgroundResource(R.drawable.transparent_shape_white)
                patientText.setTextColor(
                    ContextCompat.getColor(
                        this!!,
                        R.color.text_color3
                    )
                )
                doctorLayout.setBackgroundResource(R.drawable.right_corner_round_purple)
                doctorText.setTextColor(ContextCompat.getColor(this, R.color.white))
                supportFragmentManager.beginTransaction().replace(R.id.container,RegisterDoctor()).commit()
            } else {
                selected = 1
                patientLayout.setBackgroundResource(R.drawable.left_corner_round_purple)
                patientText.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                doctorLayout.setBackgroundResource(R.drawable.transparent_shape_white)
                doctorText.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.text_color3
                    )
                )
                supportFragmentManager.beginTransaction().replace(R.id.container,RegisterPatient()).commit()

            }
        }
    }

    private fun initViews() {
        toggle = findViewById(R.id.toggle)
        patientLayout = findViewById(R.id.patient_l)
        patientText = findViewById(R.id.patient_text)
        doctorLayout = findViewById(R.id.doctor_l)
        doctorText = findViewById(R.id.doctor_text)
    }

}
