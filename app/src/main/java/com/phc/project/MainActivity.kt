package com.phc.project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TableLayout
import android.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.utils.Utils
import com.google.android.material.tabs.TabLayout
import com.google.firebase.storage.internal.Util
import com.phc.project.Activities.DoctorDashboard
import com.phc.project.Activities.Login
import com.phc.project.Adapters.FragmentAdapter

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var viewPager: ViewPager
    lateinit var tableLayout: TabLayout
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setSupportActionBar(toolbar)

        initTabLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.main_menue, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.log_out) {
            editor.clear().commit()
            com.phc.project.Utils.Utils.showShortMessage(this,"Log Out")
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

    private fun initTabLayout() {
        tableLayout.addTab(tableLayout.newTab().setText("All Doctors"))
        tableLayout.addTab(tableLayout.newTab().setText("Top Rated"))
        tableLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = FragmentAdapter(tableLayout.tabCount,supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tableLayout))
        tableLayout.setOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
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
        toolbar = findViewById(R.id.toolbar)
        viewPager = findViewById(R.id.view_pager)
        tableLayout = findViewById(R.id.tab_layout)
        pref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        editor = pref.edit()
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left)

    }
}
