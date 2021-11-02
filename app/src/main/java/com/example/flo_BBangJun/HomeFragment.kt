package com.example.flo_BBangJun

import android.content.ContentValues.TAG
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_BBangJun.databinding.FragmentHomeBinding
import com.example.flo_ByeongJunKang.AlbumFragment



class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding // 뷰 바인딩 1

    lateinit var threadBanner : bannerThread

    var bannerTime = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false) // 뷰 바인딩 2

        binding.homeIU1IV.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction() // context as Mainactivity = mainactivity에서의 startactivity와 같은 기능.
                .replace(
                    R.id.main_frm,
                    AlbumFragment()
                ) // replace: mainactivity에 있는 homefragment를 albumfragment로 대체한다.
                .commitAllowingStateLoss() // 내부 동작 하나의 패턴.
        }

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