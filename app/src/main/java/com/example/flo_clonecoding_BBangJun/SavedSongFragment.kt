package com.example.flo_clonecoding_BBangJun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clonecoding_BBangJun.databinding.FragmentLockerSavedsongBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedsongBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!! // db로부터 인스턴스 가져오기

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = SongRVAdapter()
        //리스너 객체 생성 및 전달

        songRVAdapter.setMyItemClickListener(object : SongRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = songRVAdapter


        // 좋아요 버튼이 눌린 노래들의 데이터가 songRVAdapter에 들어감
        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList)

    }
}
//class SavedSongFragment : Fragment(){
//
//    lateinit var binding : FragmentSavedsongBinding
//
//    private var lockerAlbumDatas = ArrayList<LockerAlbum>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        binding = FragmentSavedsongBinding.inflate(inflater, container, false)
//
//        lockerAlbumDatas.apply{
//            add(LockerAlbum("VVS", "미란이, 먼치맨, 쿤디판다, 머쉬베놈, 저스디스", R.drawable.img_album_vvs))
//            add(LockerAlbum("After School", "Weekly(위클리)", R.drawable.img_album_afterschool))
//            add(LockerAlbum("CREDIT (Feat. 염따, 기리보이, Zion.T)", "릴보이(lilBOI)", R.drawable.img_album_credit))
//            add(LockerAlbum("ASAP", "STAYC(스테이씨)", R.drawable.img_album_asap))
//            add(LockerAlbum("ON AIR (Feat. 로꼬, 박재범 & GRAY)", "릴보이(lilBOI)", R.drawable.img_album_onair))
//            add(LockerAlbum("Queendom", "Red Velvet (레드벨벳)", R.drawable.img_album_queendom))
//        }
//
//
//        // 더미데이터와 Adapter 연결
//        val lockerRVAdapter = LockerRVAdapter(lockerAlbumDatas)
//        // 리사이클러뷰에 어댑터를 연결
//        binding.lockerMusicAlbumRecyclerView.adapter = lockerRVAdapter
//
//        lockerRVAdapter.setMyItemClickListener(object : LockerRVAdapter.MyItemClickListener{
//            override fun onRemoveAlbum(position: Int) {
//                lockerRVAdapter.removeItem(position)
//            }
//        })
//
//        // 레이아웃 매니저 설정
//        binding.lockerMusicAlbumRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
//
//        return binding.root
//    }
//
//}