package com.example.flo_BBangJun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_BBangJun.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding

    val information = arrayListOf("저장한 곡", "음악파일")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerViewpagerAdapter(this)
        binding.lockerViewpagerVP.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerTablayoutLO, binding.lockerViewpagerVP){
            tab, position -> // 탭레이아웃에 어떤 텍스트가 들어갈지
            tab.text = information[position]
        }.attach()

        return binding.root
    }


}