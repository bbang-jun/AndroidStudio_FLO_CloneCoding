package com.example.flo_BBangJun

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PanelViewpagerAdapter (fragment : Fragment) : FragmentStateAdapter(fragment){ // 상속받는 FragmentStateAdapter는 fragment를 인자값으로 가짐

    private val fragmentlist : ArrayList<Fragment> = ArrayList() // 리스트로 담기

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> PanelFragment()
            1 -> Panel2Fragment()
            else -> Panel3Fragment()
        }
    }
}