package com.phc.project.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.phc.project.Fragments.AllDoctors
import com.phc.project.Fragments.TopRated


class FragmentAdapter(val noOfTabs: Int, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = when (position) {
        0 -> AllDoctors()
        1 -> TopRated()
        else -> null
    }!!

    override fun getCount(): Int {
        return noOfTabs
    }
}