package com.example.flo_BBangJun

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerViewpagerAdapter (fragment : Fragment) : FragmentStateAdapter(fragment){ // activity: appcompat fragment: Fragment adpater: fragmentstateadapter 상속

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SavedSongFragment()
            else -> MusicFileFragment()
        }
    }

}