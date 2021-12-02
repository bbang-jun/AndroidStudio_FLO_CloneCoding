package com.example.flo_clonecoding_BBangJun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clonecoding_BBangJun.databinding.FragmentLockerSavedalbumBinding

class SavedAlbumFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedalbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedalbumBinding.inflate(inflater, container, false)

        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview() {
        binding.lockerSavedSongRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val albumRVAdapter = AlbumLockerRVAdapter()

        albumRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                albumDB.albumDao().getLikedAlbums(getUserIdx(requireContext()))
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = albumRVAdapter

        albumRVAdapter.addAlbums(albumDB.albumDao().getLikedAlbums(getUserIdx(requireContext())))
    }

//    private fun getUserIdx() : Int{
//        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
//        val jwt = spf!!.getInt("jwt", 0)
//        Log.d("MAIN_ACT/GET_JWT", "jwt_token: $jwt")
//
//        return jwt
//    }
}
