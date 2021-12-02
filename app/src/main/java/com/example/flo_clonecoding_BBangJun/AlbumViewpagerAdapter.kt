package com.example.flo_clonecoding_BBangJun

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumViewpagerAdapter (fragment : Fragment) : FragmentStateAdapter(fragment){ //

    override fun getItemCount(): Int = 3 // 수록곡, 상세정보, 영상 fragment로 총 3개 배너와 달리 갯수가 정해져 있기 때문에 값 반환

    override fun createFragment(position: Int): Fragment {
        return when(position){ // when = 스위치문
            0 -> SongFragment()
            1 -> DetailFragment()
            else -> VideoFragment() // 어떤 곡에서는 영상 부분이 빠져 있는 경우가 있어서 else로 표현
        }
    }
}