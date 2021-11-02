package com.example.flo_BBangJun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo_BBangJun.databinding.FragmentBannerBinding

class BannerFragment(val imgRes : Int) : Fragment(){

    lateinit var binding : FragmentBannerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBannerBinding.inflate(inflater, container, false)

        binding.bannerImageIV.setImageResource(imgRes) // BannerFragment의 인자값으로 받은 이미지 리소스

        return binding.root
    }
}