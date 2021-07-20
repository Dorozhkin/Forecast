package com.example.forecast.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.forecast.ui.hours.FragmentHours
import com.example.forecast.ui.today_tomorrow.FragmentToday
import com.example.forecast.ui.today_tomorrow.FragmentTomorrow

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FragmentToday.instance
            1 -> fragment = FragmentTomorrow.instance
            2 -> fragment = FragmentHours.instance
        }
        return fragment!!
    }

    override fun getItemCount(): Int {
        return 3
    }
}