package com.example.thandroid.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.thandroid.InformationFragment
import com.example.thandroid.ListFragment
import com.example.thandroid.SearchStatisticFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ListFragment()
            1 -> InformationFragment()
            2 -> SearchStatisticFragment()
            else -> ListFragment()
        }
    }
}