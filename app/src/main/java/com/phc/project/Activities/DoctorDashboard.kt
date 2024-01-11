package com.phc.project.Activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.phc.project.Adapters.FragmentAdapter
import com.phc.project.Adapters.FragmentAdapterDoctor
import com.phc.project.R
import com.phc.project.Utils.DBReferences
import com.phc.project.Utils.Utils
import io.paperdb.Paper
import okhttp3.internal.Util

class DoctorDashboard : AppCompatActivity() {

    val TAG = "DoctorDashboar"
    lateinit var statusPendingNotification: CardView
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var viewPager: ViewPager
    lateinit var tableLayout: TabLayout
    lateinit var dialog: Dialog
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_dashboard)
        initViews()
        setSupportActionBar(toolbar)
        Paper.init(this)
        val status = Paper.book().read<String>("status")//intent.getStringExtra("status")
        if (status.equals("pending")) {
            //   statusPendingNotification.visibility = View.VISIBLE
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Account Varification Alert")
            builder.setMessage("Your account is currently not approved. Please wait untill your account is approved by admin ")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setCancelable(false)
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                Toast.makeText(
                    applicationContext,
                    "You are logged out now", Toast.LENGTH_SHORT
                ).show()
                editor.clear().commit()
                this.finish()
            }

//            builder.setNegativeButton(android.R.string.no) { dialog, which ->
//                Toast.makeText(applicationContext,
//                    android.R.string.no, Toast.LENGTH_SHORT).show()
//            }
//
//            builder.setNeutralButton("Maybe") { dialog, which ->
//                Toast.makeText(applicationContext,
//                    "Maybe", Toast.LENGTH_SHORT).show()
//            }
            builder.show()
            Log.d(TAG, "inside dialog builder")
        } else {
            initTabLayout()
        }

    }

    private fun initTabLayout() {
        tableLayout.addTab(tableLayout.newTab().setText("Pending Complaints"))
        tableLayout.addTab(tableLayout.newTab().setText("Resolved Complaints"))
        tableLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = FragmentAdapterDoctor(tableLayout.tabCount, supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tableLayout))
        tableLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                viewPager.setCurrentItem(p0!!.position)
            }

        })
    }

    private fun initViews() {
        statusPendingNotification = findViewById(R.id.card_status)
        toolbar = findViewById(R.id.toolbar)
        viewPager = findViewById(R.id.view_pager)
        tableLayout = findViewById(R.id.tab_layout)
        dialog = Utils.progressDialog(this)
        pref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.main_menue, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.log_out) {
            editor.clear().commit()
            com.phc.project.Utils.Utils.showShortMessage(this, "Log Out")
            sendUserToLoginActivity()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun sendUserToLoginActivity() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left)

    }
}
