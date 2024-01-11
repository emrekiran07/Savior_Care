package com.phc.project.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.phc.project.Fragments.AllDoctors
import com.phc.project.Fragments.ApprovedComplaints
import com.phc.project.Fragments.PendingComplaints
import com.phc.project.Fragments.TopRated


class FragmentAdapterDoctor(val noOfTabs: Int, fm: FragmentManager):
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = when (position) {
        0 -> PendingComplaints()
        1 -> ApprovedComplaints()
        else -> null
    }!!

    override fun getCount(): Int {
        return noOfTabs
    }
}