package com.example.flo_BBangJun

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

class BannerViewpagerAdapter (fragment : Fragment) : FragmentStateAdapter(fragment){ // activity: appcompat fragment: Fragment adpater: fragmentstateadapter 상속

    private val fragmentlist : ArrayList<Fragment> = ArrayList() // fragment들을 담아주는 list / private: 이 클래스 안에서만 사용 가능하게 함(외부에서 데이터 접근 x)

    override fun getItemCount(): Int = fragmentlist.size  // item 갯수를 전달하는 함수

    override fun createFragment(position: Int): Fragment = fragmentlist[position] // fragmentlist 안의 item(fragment)들을 생성해주는 함수

    fun addFragment(fragment: Fragment){ // homefragment에서 private에 의해 접근하지 못하기 때문에 함수를 통해서 접근하게 해줌
        fragmentlist.add(fragment) // fragment 추가
        notifyItemInserted(fragmentlist.size -  1) // viewpager에게 아이템이 삽입됨을 알려줌
    }
}