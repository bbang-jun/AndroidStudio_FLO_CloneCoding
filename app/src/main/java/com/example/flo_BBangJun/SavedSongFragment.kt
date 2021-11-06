package com.example.flo_BBangJun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_BBangJun.databinding.FragmentSavedsongBinding

class SavedSongFragment : Fragment(){

    lateinit var binding : FragmentSavedsongBinding

    private var lockerAlbumDatas = ArrayList<LockerAlbum>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSavedsongBinding.inflate(inflater, container, false)

        lockerAlbumDatas.apply{
            add(LockerAlbum("VVS", "미란이, 먼치맨, 쿤디판다, 머쉬베놈, 저스디스", R.drawable.img_album_vvs))
            add(LockerAlbum("After School", "Weekly(위클리)", R.drawable.img_album_afterschool))
            add(LockerAlbum("CREDIT (Feat. 염따, 기리보이, Zion.T)", "릴보이(lilBOI)", R.drawable.img_album_credit))
            add(LockerAlbum("ASAP", "STAYC(스테이씨)", R.drawable.img_album_asap))
            add(LockerAlbum("ON AIR (Feat. 로꼬, 박재범 & GRAY)", "릴보이(lilBOI)", R.drawable.img_album_onair))
            add(LockerAlbum("Queendom", "Red Velvet (레드벨벳)", R.drawable.img_album_queendom))
        }

        val lockerRVAdapter = LockerRVAdapter(lockerAlbumDatas)
        binding.lockerMusicAlbumRecyclerView.adapter = lockerRVAdapter
        binding.lockerMusicAlbumRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        return binding.root
    }

}