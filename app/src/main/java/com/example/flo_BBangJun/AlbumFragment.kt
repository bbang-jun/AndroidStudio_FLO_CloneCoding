package com.example.flo_ByeongJunKang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo_BBangJun.*
import com.example.flo_BBangJun.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment(){ // 콜론을 이용해서 fragment 상속 받기 (안드로이드에서 fragment 기능을 사용 가능하게 하는 클래스)

    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson() // Gson 선언

    val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked: Boolean = false

    override fun onCreateView( // activity에서 oncreate와 같이 처음 실행되는 함수
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false) // 바인딩 초기화. activity에서의 inflate(layoutinflater)와 같은 기능

        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)
        isLiked = isLikedAlbum(album.id)

        setViews(album)
        setClickListeners(album)

        //ROOM_DB
        val songs = getSongs(album.id) //앨범안에 있는 수록곡들을 불러옵니다.
        // 이 다음에 수록곡 프래그먼트에 songs을 전달해주는 식으로 사용하시면 됩니다.

        binding.albumArrowbuttonleftIB.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction() // context as Mainactivity = mainactivity에서의 startactivity와 같은 기능.
                .replace(R.id.main_frm, HomeFragment()) // replace: albumfragment를 homefragment로 대체한다.
                .commitAllowingStateLoss() // 내부 동작 하나의 패턴.
        }

        val albumAdapter = AlbumViewpagerAdapter(this) // 탭레이아웃 연결

        binding.albumContentVP.adapter = albumAdapter // adpater 연결
        TabLayoutMediator(binding.albumContentTB, binding.albumContentVP){ // Mediatior: 탭 레이아웃을 viewpager와 연결해주는 중재자 역할 (위치 동기화)
            tab, position -> // 탭레이아웃에 어떤 텍스트가 들어갈지
            tab.text = information[position]
        }.attach() // tablayout과 viewapger를 붙여주는 작업


        return binding.root // activity에서의 setContentView(binding.root)와 같은 기능
    }

    private fun setViews(album: Album) {
        binding.albumMusicTitleTV.text = album.title.toString()
        binding.albumSingerNameTV.text = album.singer.toString()
        binding.albumAlbumIV.setImageResource(album.coverImg!!)

        if(isLiked){
            binding.albumLikeIB.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIB.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun setClickListeners(album : Album){
        val userId: Int = getUserIdx(requireContext())

        binding.albumLikeIB.setOnClickListener {
            if(isLiked){
                binding.albumLikeIB.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(userId, album.id)
            } else{
                binding.albumLikeIB.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }
    }

    private fun likeAlbum(userId: Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum((like))
    }

    private fun isLikedAlbum(albumId: Int): Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getUserIdx(requireContext())

        val likeId: Int? = songDB.albumDao().isLikeAlbum(userId, albumId)

        return likeId != null
    }

    private fun disLikedAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!

        songDB.albumDao().disLikeAlbum(userId, albumId)
    }

    private fun getUserIdx(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt", 0)
    }

    //ROOM_DB
    private fun getSongs(albumIdx: Int): ArrayList<Song>{
        val songDB = SongDatabase.getInstance(requireContext())!!

        val songs = songDB.songDao().getSongsInAlbum(albumIdx) as ArrayList

        return songs
    }
}