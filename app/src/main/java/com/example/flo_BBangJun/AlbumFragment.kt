package com.example.flo_ByeongJunKang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo_BBangJun.*
import com.example.flo_BBangJun.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment(){ // 콜론을 이용해서 fragment 상속 받기 (안드로이드에서 fragment 기능을 사용 가능하게 하는 클래스)

    lateinit var binding : FragmentAlbumBinding

    private var gson: Gson = Gson() // Gson 선언언

   val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView( // activity에서 oncreate와 같이 처음 실행되는 함수
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false) // 바인딩 초기화. activity에서의 inflate(layoutinflater)와 같은 기능

        binding.albumArrowbuttonleftIB.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction() // context as Mainactivity = mainactivity에서의 startactivity와 같은 기능.
                .replace(R.id.main_frm, HomeFragment()) // replace: albumfragment를 homefragment로 대체한다.
                .commitAllowingStateLoss() // 내부 동작 하나의 패턴.
        }

        // HomeFragment에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)
        // HomeFragment에서 넘어온 데이터를 반영
        setInit(album)


        val albumAdapter = AlbumViewpagerAdapter(this) // 탭레이아웃 연결
        binding.albumContentVP.adapter = albumAdapter // adpater 연결
        TabLayoutMediator(binding.albumContentTB, binding.albumContentVP){ // Mediatior: 탭 레이아웃을 viewpager와 연결해주는 중재자 역할 (위치 동기화)
            tab, position -> // 탭레이아웃에 어떤 텍스트가 들어갈지
            tab.text = information[position]
        }.attach() // tablayout과 viewapger를 붙여주는 작업


        return binding.root // activity에서의 setContentView(binding.root)와 같은 기능
    }

    private fun setInit(album: Album) {
        binding.albumAlbumIV.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTV.text = album.title.toString()
        binding.albumSingerNameTV.text = album.singer.toString()
    }
}