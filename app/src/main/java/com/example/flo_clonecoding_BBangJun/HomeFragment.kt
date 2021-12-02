package com.example.flo_clonecoding_BBangJun

import android.content.ContentValues.TAG
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_clonecoding_BBangJun.databinding.FragmentHomeBinding
import com.example.flo_ByeongJunKang.AlbumFragment
import com.google.gson.Gson


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding // 뷰 바인딩 1
    private var albums = ArrayList<Album>()

    private lateinit var songDB: SongDatabase

    lateinit var threadBanner : bannerThread

    var bannerTime = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false) // 뷰 바인딩 2

        //ROOM_DB
        songDB = SongDatabase.getInstance(requireContext())!!
        albums.addAll(songDB.albumDao().getAlbums()) // songDB에서 album list를 가져옵니다.

        // step5-3(레이아웃 매니저 추가) 레이아웃 매니저 설정(아이템 배치를 어떻게 할 것인지) 리니어레이아웃에서 수평으로.
        binding.homeTodayMusicAlbumRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // step5-1(리사이클러뷰에 어댑터 연결) 더미데이터와 Adapter 연결
        val albumRVAdapter = AlbumRVAdapter(albums)

        // 아이템뷰를 클릭했을 때 fragment 전환
        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{

            override fun onItemclick(album: Album) {
                startAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })

        // step5-2(리사이클러뷰에 어댑터 연결) recyclerview에 Adapter 연결

        binding.homeTodayMusicAlbumRecyclerview.adapter = albumRVAdapter

//        binding.homeIU1IV.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction() // context as Mainactivity = mainactivity에서의 startactivity와 같은 기능.
//                .replace(R.id.main_frm, AlbumFragment()) // replace: mainactivity에 있는 homefragment를 albumfragment로 대체한다.
//                .commitAllowingStateLoss() // 내부 동작 하나의 패턴.
//        }


        val panelAdapter = PanelViewpagerAdapter(this) // PanelViewpager와 Homefragment의 연결
        binding.homePanelVP.adapter = panelAdapter




        val bannerAdapter =
            BannerViewpagerAdapter(this) // adpater: viewpager를 사용하기 위한 공급 장치 역할(새로운 어댑터 클래스를 만들어준 후)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp)) // BannerFragment에서 이미지 리소스를 받은 후에 이미지를 넣어줌
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))


        binding.homeBannerVP.adapter =
            bannerAdapter // viewpager를 binding으로 가져옴. (fragment와 viewpager를 연결)
        binding.homeBannerVP.orientation = ViewPager2.ORIENTATION_HORIZONTAL // viewpager 좌우 스크롤


        // 인디케이터 사용
        val indicator = binding.wormDotsIndicator
        indicator.setViewPager2(binding.homePanelVP)

        threadBanner = bannerThread()
        threadBanner.start()

        return binding.root // 뷰바인딩 3
    }

    fun startAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    override fun onPause(){
        threadBanner.interrupt()
        super.onPause()
    }

    inner class bannerThread() : Thread(){
        override fun run() {
            try{
                while(true){
                    sleep(2000)
                    bannerTime++
                    Log.d(TAG, "run:$bannerTime")

                    activity!!.runOnUiThread {
                        binding.homeBannerVP.currentItem=bannerTime%5
                    }
                }
            }
            catch (e:InterruptedException){
                Log.d(TAG, "쓰레드 종료")
            }
        }
    }
}