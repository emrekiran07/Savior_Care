package com.phc.project.Activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.phc.project.MainActivity
import com.phc.project.R
import com.phc.project.Utils.NetworkHelper

class Splash : AppCompatActivity() {
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_splash)
        pref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)

        if(!NetworkHelper.isConnected(this)){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Network Connection Alert")
            builder.setMessage("It seems you have no internet. Please enable your network first and try again.")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setCancelable(false)
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                Toast.makeText(
                    applicationContext,
                    "", Toast.LENGTH_SHORT
                ).show()
                this.finish()
            }
            builder.show()
        }else{
            Handler().apply {
                val runnable = object : Runnable {
                    override fun run() {
                        if (pref.getBoolean("isUserLogedIn", false)) {
                            sendUserToDashboard()
                        }else{
                            sendUserToLoginActivity()
                        }
                    }
                }
                postDelayed(runnable, 3000)
            }
        }

    }

    private fun sendUserToLoginActivity() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.enter, R.anim.exit)
        finish()
    }

    fun sendUserToDashboard() {
        Log.d("USERTYPE is ",pref.getBoolean("isDoctor",false).toString())
        if(pref.getBoolean("isDoctor",false)){
            val intent = Intent(this, DoctorDashboard::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.enter, R.anim.exit)
            finish()
        }else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.enter, R.anim.exit)
            finish()
        }

    }
}
