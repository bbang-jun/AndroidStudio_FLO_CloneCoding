package com.example.flo_BBangJun

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_BBangJun.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding



    val information = arrayListOf("저장한 곡", "음악파일", "저장앨범")

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

        binding.lockerLoginTV.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initView()
    }

    private fun initView(){
         val jwt = getUserIdx(requireContext())// jwt를 가져오는 함수

        if(jwt==0){
//            binding.lockerNameTV.text=""
            binding.lockerLoginTV.text="로그인"
            binding.lockerLoginTV.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        } else{
//            val songDB = SongDatabase.getInstance(requireContext())!!
            binding.lockerLoginTV.text="로그아웃"
//            binding.lockerNameTV.text=songDB.userDao().getUsers(jwt).name
            binding.lockerLoginTV.setOnClickListener {
                // 로그아웃을 시켜주는 함수
                logout()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }



    private fun logout(){
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        editor.remove("jwt")
        editor.apply()
    }


}